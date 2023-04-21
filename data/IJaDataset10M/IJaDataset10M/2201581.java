package il.ac.biu.cs.grossmm.tests.cpptest;

import il.ac.biu.cs.grossmm.api.cpp.CppServiceManager;
import il.ac.biu.cs.grossmm.api.flow.ActiveDataManager;
import il.ac.biu.cs.grossmm.impl.activeData.ActiveDataManagerImpl;
import il.ac.biu.cs.grossmm.impl.cpp.CppManager;
import il.ac.biu.cs.grossmm.impl.server.RegistryServer;

public class Server extends RegistryServer {

    Server() {
        registerClass(ActiveDataManager.class, ActiveDataManagerImpl.class);
        registerClass(CppServiceManager.class, CppManager.class);
        registerClass("A", ProcessorA.class);
        registerClass("B", ProcessorB.class);
        registerClass("CC", CppConsumer.class);
        registerClass("FTVC", FooToValueConverter.class);
        addDependency("A", "B");
        addDependency("CC", "FTVC");
        addDependency("CC", "B");
        addToStartup("CC");
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
        Thread.sleep(40000);
        server.stop();
    }
}
