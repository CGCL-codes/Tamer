package uk.co.nimp.scardterminalmanager;

import com.atolsystems.atolutilities.AFileUtilities;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.nimp.scard.SmartCardTaskProcessor;

public class RmiServerThread implements Runnable {

    Process rmiRegistryProcess;

    SmartCardTaskProcessor engine;

    public RmiServerThread(SmartCardTaskProcessor engine) {
        this.engine = engine;
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if (rmiRegistryProcess != null) {
                    rmiRegistryProcess.destroy();
                }
            }
        });
    }

    protected void startRmi() {
        try {
            if (null == System.getSecurityManager()) {
                System.setSecurityManager(new SecurityManager());
            }
            if (null == System.getProperty("java.rmi.server.codebase")) {
                String codebase = AFileUtilities.getRmiCodeBase(SmartCardTaskProcessor.class, "packageLocator");
                System.setProperty("java.rmi.server.codebase", codebase);
            }
            System.out.println("codebase: " + System.getProperty("java.rmi.server.codebase"));
            String serverAddress = InetAddress.getLocalHost().getHostAddress();
            System.setProperty("java.rmi.server.hostname", serverAddress);
            SmartCardTaskProcessor stub = (SmartCardTaskProcessor) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            boolean rmiregistryFound = true;
            try {
                System.out.println("try to get registry list");
                String[] a = registry.list();
                System.out.println(a.length + " names bound in the registry");
                for (int i = 0; i < a.length; i++) System.out.println(i + "\t" + a[i]);
            } catch (Exception e) {
                rmiregistryFound = false;
                String rmiRegistryPath = AFileUtilities.appendToPath(System.getProperty("java.home"), "bin");
                rmiRegistryPath = AFileUtilities.appendToPath(rmiRegistryPath, "rmiregistry");
                ProcessBuilder processBuilder = new ProcessBuilder(rmiRegistryPath);
                System.out.println("Start rmiregistry");
                rmiRegistryProcess = processBuilder.start();
                System.out.println("rmiregistry started");
            }
            long start = System.currentTimeMillis();
            while (false == rmiregistryFound) {
                System.out.println("waiting for registry");
                long now = System.currentTimeMillis();
                if (now - start > 1000) throw new RuntimeException("rmiregistry is not responsive");
                try {
                    registry = LocateRegistry.getRegistry();
                    String[] a = registry.list();
                    System.out.println(a.length + " names bound in the registry:");
                    for (int i = 0; i < a.length; i++) System.out.println(a[i]);
                    rmiregistryFound = true;
                } catch (Exception e) {
                    System.out.println("registry not found");
                    Thread.yield();
                }
            }
            registry.rebind(SmartCardTaskProcessor.registryId, stub);
            System.out.println("Terminal Manager bound");
            System.out.println("wait for task or command");
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(RmiServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        startRmi();
    }
}
