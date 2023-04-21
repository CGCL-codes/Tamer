package com.griddynamics.openspaces.convergence.monitor.gigaspaces;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Test;
import com.griddynamics.openspaces.convergence.gigapult.Machine;
import com.griddynamics.openspaces.convergence.gigapult.StartedProcess;
import com.griddynamics.openspaces.convergence.gigapult.TestLab;
import com.griddynamics.openspaces.convergence.monitor.ComponentInfoStorage;
import com.griddynamics.openspaces.convergence.monitor.RouteAdvisor;
import com.griddynamics.openspaces.convergence.monitor.exceptions.NoRouteException;

public class GigaSpacesDataGridMonitorFunctionalTest {

    private static final int SECOND_IN_MILLIS = 1000;

    private GigaSpacesDataGridMonitor monitor;

    private TestLab lab;

    @After
    public void tearDown() {
        if (monitor != null) {
            monitor.shutdown();
        }
        for (Machine machine : lab.getMachines()) {
            System.out.println("Killing everything on machine " + machine.toString());
            machine.killAllStartedProcesses();
        }
    }

    @Test
    public void dataGridMonitorShouldSeesPartitionedSpaces() {
        Machine gsmMachine = startGrid(2, "partitioned-total-2");
        sleepFor(30, "Let grid to start");
        gsmMachine.deploySpace();
        sleepFor(45, "Let space to deploy");
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(30, "Let monitor to find partitions");
        assertThatAllPartitionsFound(2, monitor);
    }

    @Test
    public void dataGridMonitorShouldSeesPartitionedSpacesEvenIfStartedBeforeGrid() {
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(10, "Let monitor to start");
        Machine gsmMachine = startGrid(2, "partitioned-total-2");
        sleepFor(30, "Let grid to start");
        gsmMachine.deploySpace();
        sleepFor(45, "Let space to deploy");
        assertThatAllPartitionsFound(2, monitor);
    }

    @Test
    public void dataGridMonitorShouldNotSeeZombieSpacesIfGsmCrashedAndThenReturned() {
        lab = TestLab.createConvergenceLab("partitioned-total-2");
        Machine machine = lab.getMachines().get(0);
        StartedProcess gsm = machine.startGsm();
        StartedProcess gsc1 = machine.startGsc();
        machine.startGsc();
        sleepFor(30, "Let grid to start");
        machine.deploySpace();
        sleepFor(45, "Let space to deploy");
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(30, "Let monitor to find partitions");
        assertThatAllPartitionsFound(2, monitor);
        gsm.kill();
        sleepFor(20, "Let all containers to find that GSM disappeared");
        gsc1.kill();
        sleepFor(20, "Wait a little after killing one GSC");
        machine.startGsm();
        sleepFor(30, "Let all containers and monitor to find that GSM went back");
        assertThatStorageDoesNotContainZombies(monitor.getSpacesStorage());
    }

    @Test
    public void routeAdvisorShouldReturnLocalhostForSpaceDeployedLocally() {
        Machine machine = startGrid(2, "partitioned-total-2");
        sleepFor(30, "Let grid to start");
        machine.deploySpace();
        sleepFor(30, "Let space to deploy");
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(30, "Let monitor to find partitions");
        assertThatLocalhostAdvised(monitor.getRouteAdvisor(), new SpaceAffinityKey("dataGrid", 1));
    }

    @Test
    public void routeAdvisorShouldAdviseDifferentHostsForPartitionedSpaceDeployedNonLocally() {
        List<Machine> testMachines = new ArrayList<Machine>();
        Machine gsmMachine = startGrid(2, "partitioned-total-2", testMachines);
        sleepFor(30, "Let grid to start");
        gsmMachine.deploySpace();
        sleepFor(45, "Let space to deploy");
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(30, "Let monitor to find partitions");
        assertThatDifferentHostsAdvisedForAllPartitions(2, monitor.getRouteAdvisor());
    }

    @Test
    public void routeAdvisorShouldRouteToBackupPartitionIfPrimaryFailed() {
        lab = TestLab.createConvergenceLab("partitioned-sync2backup-total-2-backups-1");
        Machine machine1 = lab.getMachines().get(0);
        Machine machine2 = lab.getMachines().get(1);
        machine1.startGsm();
        StartedProcess gsc = machine1.startGsc();
        machine2.startGsc();
        sleepFor(30, "Let grid to start");
        machine1.deploySpace();
        sleepFor(60, "Let space to deploy");
        monitor = new GigaSpacesDataGridMonitor();
        sleepFor(30, "Let monitor to find partitions");
        assertThatAllPartitionsFound(2, monitor);
        gsc.kill();
        sleepFor(20, "Wait a little after killing one GSC");
        assertThatAllPartitionsFound(2, monitor);
    }

    private void assertThatStorageDoesNotContainZombies(ComponentInfoStorage<SpaceInfo> spacesStorage) {
        for (SpaceInfo spaceInfo : spacesStorage.selectAll()) {
            try {
                spaceInfo.getSpaceProxy().ping();
            } catch (RemoteException e) {
                fail("Zombie detected: " + spaceInfo);
            }
        }
    }

    private void assertThatDifferentHostsAdvisedForAllPartitions(int totalPartitions, RouteAdvisor routeAdvisor) {
        Set<String> advisedHosts = new HashSet<String>();
        for (int i = 0; i < totalPartitions; i++) {
            String host = routeAdvisor.adviseHostByAffinityKey(new SpaceAffinityKey("dataGrid", i));
            advisedHosts.add(host);
        }
        assertThat(advisedHosts.size(), equalTo(totalPartitions));
    }

    private void assertThatAllPartitionsFound(int totalPartitions, GigaSpacesDataGridMonitor monitor) {
        RouteAdvisor routeAdvisor = monitor.getRouteAdvisor();
        for (int i = 1; i <= totalPartitions; i++) {
            try {
                routeAdvisor.adviseHostByAffinityKey(new SpaceAffinityKey("dataGrid", i));
            } catch (NoRouteException ex) {
                fail("Partition #" + i + " not found");
            }
        }
    }

    private void assertThatLocalhostAdvised(RouteAdvisor routeAdvisor, SpaceAffinityKey affinityKey) {
        try {
            String localhost = InetAddress.getByName("localhost").getHostAddress();
            String host = routeAdvisor.adviseHostByAffinityKey(affinityKey);
            String ip = InetAddress.getByName(host).getHostAddress();
            if (!localhost.equals(ip)) {
                fail(localhost + " should be advised, but advised " + ip);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private Machine startGrid(int totalContainers, String commonLabSettings) {
        return startGrid(totalContainers, commonLabSettings, new ArrayList<Machine>());
    }

    private Machine startGrid(int totalContainers, String commonLabSettings, List<Machine> testMachines) {
        lab = TestLab.createConvergenceLab(commonLabSettings);
        Machine gsmMachine = lab.getMachines().get(0);
        for (int i = 0; i < totalContainers; i++) {
            testMachines.add(lab.getMachines().get(i));
        }
        gsmMachine.startGsm();
        for (Machine machine : testMachines) {
            machine.startGsc();
        }
        return gsmMachine;
    }

    private void sleepFor(int timeout, String message) {
        try {
            System.out.println(message + " (" + timeout + " secs.)");
            Thread.sleep(timeout * SECOND_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
