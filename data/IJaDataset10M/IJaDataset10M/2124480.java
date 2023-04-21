package sf2.vm.impl.sbuml;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import sf2.core.Config;
import sf2.core.ConfigException;
import sf2.core.ProcessExecutor;
import sf2.core.ProcessExecutorException;
import sf2.vm.AbstractVMNetwork;
import sf2.vm.PortsMapping;
import sf2.vm.VMException;
import sf2.vm.VirtualMachine;

public class SBUMLNetwork extends AbstractVMNetwork {

    private static final long serialVersionUID = 1L;

    protected static final String PROP_ENABLE_SUDO = "sf2.vm.impl.sbuml.enableSudo";

    protected static final boolean DEFAULT_ENABLE_SUDO = true;

    protected static final String TAP0 = "tap0";

    protected static final String ETH0 = "eth0";

    protected static final int TAP0_WAIT = 5 * 1000;

    protected static boolean enableSudo;

    protected boolean running = false;

    static {
        try {
            Config config = Config.search();
            enableSudo = config.getBoolean(PROP_ENABLE_SUDO, DEFAULT_ENABLE_SUDO);
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getGuestAddress() {
        return hostAddr;
    }

    public void reference() throws VMException {
    }

    public void startBefore(VirtualMachine vm) throws VMException {
    }

    public void start(VirtualMachine vm) throws VMException {
        if (!running) {
            running = true;
            if (type == NET_TYPE.NAT) {
                prepareHostNetwork();
                for (PortsMapping port : ports) enableNetwork(port, true);
            }
        }
    }

    public void prerestore(VirtualMachine vm) throws VMException {
    }

    public void restore(VirtualMachine vm) throws VMException {
        if (!running) {
            running = true;
            if (type == NET_TYPE.NAT) {
                prepareHostNetwork();
                for (PortsMapping port : ports) enableNetwork(port, true);
            }
        }
    }

    public void stop() {
        try {
            if (running) {
                running = false;
                if (type == NET_TYPE.NAT) {
                    destroyHostNetwork();
                    for (PortsMapping port : ports) enableNetwork(port, false);
                }
            }
        } catch (VMException e) {
            e.printStackTrace();
        }
    }

    public void prepareHostNetwork() throws VMException {
        try {
            byte[] bmask = mask.getAddress(), group = addr.getAddress();
            for (int i = 0; i < group.length; i++) group[i] &= bmask[i];
            boolean found = false;
            do {
                logging.debug(LOG_NAME, "waiting for " + TAP0 + " is up...");
                found = false;
                Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface ni = e.nextElement();
                    if (ni.getName().equals(TAP0)) {
                        found = true;
                        break;
                    }
                }
                Thread.sleep(TAP0_WAIT);
            } while (!found);
            if (enableSudo) {
                String[] cmd = { "sudo", "/sbin/ifconfig", TAP0, gw.getHostAddress() };
                String[] cmd2 = { "sudo", "/sbin/iptables", "-t", "nat", "-A", "POSTROUTING", "-o", ETH0, "-s", InetAddress.getByAddress(group).getHostAddress() + "/" + mask.getHostAddress(), "-j", "MASQUERADE" };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
                logging.debug(LOG_NAME, Arrays.toString(cmd2));
                ProcessExecutor.exec(true, cmd2);
            } else {
                String[] cmd = { "/sbin/ifconfig", TAP0, gw.getHostAddress() };
                String[] cmd2 = { "/sbin/iptables", "-t", "nat", "-A", "POSTROUTING", "-o", ETH0, "-s", InetAddress.getByAddress(group).getHostAddress() + "/" + mask.getHostAddress(), "-j", "MASQUERADE" };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
                logging.debug(LOG_NAME, Arrays.toString(cmd2));
                ProcessExecutor.exec(true, cmd2);
            }
        } catch (ProcessExecutorException e) {
            throw new VMException(e);
        } catch (UnknownHostException e) {
            throw new VMException(e);
        } catch (SocketException e) {
            throw new VMException(e);
        } catch (InterruptedException e) {
            throw new VMException(e);
        }
    }

    public void destroyHostNetwork() {
        try {
            byte[] bmask = mask.getAddress(), group = addr.getAddress();
            for (int i = 0; i < group.length; i++) group[i] &= bmask[i];
            if (enableSudo) {
                String[] cmd = { "sudo", "/sbin/iptables", "-t", "nat", "-D", "POSTROUTING", "-o", ETH0, "-s", InetAddress.getByAddress(group).getHostAddress() + "/" + mask.getHostAddress(), "-j", "MASQUERADE" };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
            } else {
                String[] cmd = { "/sbin/iptables", "-t", "nat", "-D", "POSTROUTING", "-o", ETH0, "-s", InetAddress.getByAddress(group).getHostAddress() + "/" + mask.getHostAddress(), "-j", "MASQUERADE" };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
            }
        } catch (ProcessExecutorException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    protected void enableNetwork(PortsMapping port, boolean enable) throws VMException {
        try {
            String srcPort = (port.srcPort != port.srcPortEnd) ? port.srcPort + ":" + port.srcPortEnd : Integer.toString(port.srcPort);
            String dstPort = (port.dstPort != port.dstPortEnd) ? port.dstPort + "-" + port.dstPortEnd : Integer.toString(port.dstPort);
            if (enableSudo) {
                String[] cmd = { "sudo", "/sbin/iptables", "-t", "nat", enable ? "-A" : "-D", "PREROUTING", "-p", "tcp", "-d", hostAddr.getHostAddress(), "--dport", srcPort, "-j", "DNAT", "--to-destination", addr.getHostAddress() + ":" + dstPort };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
            } else {
                String[] cmd = { "/sbin/iptables", "-t", "nat", enable ? "-A" : "-D", "PREROUTING", "-p", "tcp", "-d", hostAddr.getHostAddress(), "--dport", srcPort, "-j", "DNAT", "--to-destination", addr.getHostAddress() + ":" + dstPort };
                logging.debug(LOG_NAME, Arrays.toString(cmd));
                ProcessExecutor.exec(true, cmd);
            }
        } catch (ProcessExecutorException e) {
            throw new VMException(e);
        }
    }

    public void reference(int ref) throws VMException {
    }
}
