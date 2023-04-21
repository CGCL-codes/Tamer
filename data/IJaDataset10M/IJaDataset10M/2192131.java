package put.inf66281.rmiext.tests;

import java.rmi.registry.Registry;
import java.util.Date;
import junit.framework.TestCase;
import put.inf66281.vrmi.manager.AtomicTaskManagerService;
import put.inf66281.vrmi.registry.VersionedLocateRegistry;
import put.inf66281.vrmi.task.RmiAtomicTask;
import put.inf66281.vrmi.task.TaskDescription;

public class TestBVA extends TestCase {

    Registry reg;

    AtomicTaskManagerService taskManagerService;

    protected void setUp() throws Exception {
        taskManagerService = new AtomicTaskManagerService();
        reg = VersionedLocateRegistry.getRegistry(AtomicTaskManagerService.lookupTaskManager("polluks.cs.put.poznan.pl"));
        reg.bind("incA", new IncServiceImpl());
        reg.bind("incB", new IncServiceImpl());
    }

    public void testBVA() throws Exception {
        Thread.currentThread().setName("main");
        final Object cond = new Object();
        final Thread client1 = new Thread() {

            {
                setName("client1");
            }

            public void run() {
                try {
                    IncServiceInf incA = (IncServiceInf) reg.lookup("incA");
                    IncServiceInf incB = (IncServiceInf) reg.lookup("incB");
                    RmiAtomicTask task = new RmiAtomicTask(AtomicTaskManagerService.lookupTaskManager("polluks.cs.put.poznan.pl"));
                    TaskDescription td = new TaskDescription();
                    td.addObjectAccess(incA, 1);
                    td.addObjectAccess(incB);
                    System.out.println("[" + currentThread().getName() + "] start Task...");
                    task.startTask(td);
                    System.out.println("[" + currentThread().getName() + "] ...Task started.");
                    System.out.println("[" + currentThread().getName() + "] incA...");
                    int valA = incA.increment();
                    System.out.println("[" + currentThread().getName() + "] ...incA");
                    sleep(10000);
                    System.out.println("[" + currentThread().getName() + "] incB...");
                    int valB = incB.increment();
                    System.out.println("[" + currentThread().getName() + "] ...incB");
                    assertEquals(valA, valB);
                    task.endTask();
                    synchronized (cond) {
                        cond.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }
        };
        Thread client2 = new Thread() {

            {
                setName("client2");
            }

            public void run() {
                try {
                    IncServiceInf incA = (IncServiceInf) reg.lookup("incA");
                    IncServiceInf incB = (IncServiceInf) reg.lookup("incB");
                    System.out.println("[" + currentThread().getName() + "] start Task...");
                    RmiAtomicTask task = new RmiAtomicTask(AtomicTaskManagerService.lookupTaskManager("polluks.cs.put.poznan.pl"));
                    System.out.println("[" + currentThread().getName() + "] ...Task started");
                    TaskDescription td = new TaskDescription();
                    td.addObjectAccess(incA);
                    td.addObjectAccess(incB);
                    task.startTask(td);
                    sleep(5000);
                    System.out.println("[" + currentThread().getName() + "] incA...");
                    int valA = incA.increment();
                    System.out.println("[" + currentThread().getName() + "] ...incA");
                    System.out.println("[" + currentThread().getName() + "] incB...");
                    int valB = incB.increment();
                    System.out.println("[" + currentThread().getName() + "] ...incB");
                    task.endTask();
                    assertEquals(valA, valB);
                    synchronized (cond) {
                        cond.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }
        };
        Date startT = new Date();
        client1.start();
        client2.start();
        synchronized (cond) {
            cond.wait();
            cond.wait();
        }
        Date endT = new Date();
        System.err.println("time = " + (endT.getTime() - startT.getTime()));
    }
}
