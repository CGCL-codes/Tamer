package net.sf.dsorapart;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import org.apache.directory.daemon.DaemonApplication;
import org.apache.directory.daemon.InstallationLayout;
import org.apache.directory.server.configuration.ApacheDS;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.entry.DefaultServerEntry;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.interceptor.context.EntryOperationContext;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.SocketAcceptor;
import org.apache.directory.shared.ldap.constants.SchemaConstants;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.apache.directory.shared.ldap.util.DateUtils;
import org.apache.directory.shared.ldap.util.NamespaceTools;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eforce.util.config.ComponentConfig;
import eforce.util.config.Config;
import eforce.util.config.EntityConfig;
import eforce.util.config.initializers.FileConfigInitializer;
import eforce.util.config.sources.managers.FileSourceManager;

public class Daemon implements DaemonApplication {

    private static final Logger LOG = LoggerFactory.getLogger(Daemon.class);

    private Thread workerThread;

    private SynchWorker worker = new SynchWorker();

    private ApacheDS apacheDS;

    private FileSystemXmlApplicationContext factory;

    private Config config;

    public void init(InstallationLayout install, String[] args) throws Exception {
        printBanner();
        long startTime = System.currentTimeMillis();
        if (args.length > 0 && new File(args[0]).exists()) {
            LOG.info("server: loading settings from ", args[0]);
            factory = new FileSystemXmlApplicationContext(new File(args[0]).toURI().toURL().toString());
            apacheDS = (ApacheDS) factory.getBean("apacheDS");
        } else {
            LOG.info("server: using default settings ...");
            FileConfigInitializer fci = new FileConfigInitializer();
            fci.setConfigSourceManager(new FileSourceManager("entity-config"));
            fci.setStartChangeControl(false);
            fci.setStartGarbageCollector(false);
            config = new Config("main");
            config.init(fci);
            EntityConfig serverConfig = config.getEntity("server");
            DirectoryService directoryService = new OracleDirectoryService(config);
            Iterator i = serverConfig.getComponents().iterator();
            ArrayList<Partition> businessPartitions = new ArrayList<Partition>();
            while (i.hasNext()) {
                ComponentConfig cc = serverConfig.getComponent((String) i.next());
                Partition foo = new OraclePartition(directoryService.getRegistries(), config);
                foo.setId(cc.getName());
                LdapDN contextDn = new LdapDN(cc.getParameter("dn"));
                contextDn.normalize(directoryService.getRegistries().getAttributeTypeRegistry().getNormalizerMapping());
                foo.setSuffix(contextDn.toString());
                ServerEntry contextEntry = new DefaultServerEntry(directoryService.getRegistries(), contextDn);
                Iterator att = cc.getTableParameter("attributes").entrySet().iterator();
                while (att.hasNext()) {
                    Map.Entry e = (Entry) att.next();
                    contextEntry.add((String) e.getKey(), (String) e.getValue());
                }
                foo.setContextEntry(contextEntry);
                directoryService.addPartition(foo);
                businessPartitions.add(foo);
            }
            Partition system = new OraclePartition(directoryService.getRegistries(), config);
            system.setId("system");
            LdapDN contextDn = new LdapDN(ServerDNConstants.SYSTEM_DN);
            contextDn.normalize(directoryService.getRegistries().getAttributeTypeRegistry().getNormalizerMapping());
            system.setSuffix(contextDn.toString());
            ServerEntry contextEntry = new DefaultServerEntry(directoryService.getRegistries(), contextDn);
            contextEntry.put(SchemaConstants.OBJECT_CLASS_AT, SchemaConstants.TOP_OC, SchemaConstants.ORGANIZATIONAL_UNIT_OC, SchemaConstants.EXTENSIBLE_OBJECT_OC);
            contextEntry.put(SchemaConstants.CREATORS_NAME_AT, ServerDNConstants.ADMIN_SYSTEM_DN);
            contextEntry.put(SchemaConstants.CREATE_TIMESTAMP_AT, DateUtils.getGeneralizedTime());
            contextEntry.put(NamespaceTools.getRdnAttribute(ServerDNConstants.SYSTEM_DN), NamespaceTools.getRdnValue(ServerDNConstants.SYSTEM_DN));
            system.setContextEntry(contextEntry);
            directoryService.setSystemPartition(system);
            directoryService.startup();
            for (Partition foo : businessPartitions) if (!foo.hasEntry(new EntryOperationContext(directoryService.getRegistries(), foo.getContextEntry().getDn()))) foo.add(new AddOperationContext(directoryService.getRegistries(), foo.getContextEntry()));
            if (!system.hasEntry(new EntryOperationContext(directoryService.getRegistries(), system.getContextEntry().getDn()))) system.add(new AddOperationContext(directoryService.getRegistries(), system.getContextEntry()));
            SocketAcceptor socketAcceptor = new SocketAcceptor(Executors.newFixedThreadPool(serverConfig.getIntParameter("worker-threads")));
            LdapServer ldapServer = new LdapServer();
            ldapServer.setSocketAcceptor(socketAcceptor);
            ldapServer.setDirectoryService(directoryService);
            ldapServer.setIpPort(serverConfig.getIntParameter("ldapPort"));
            ldapServer.start();
            LdapServer ldapsServer = new LdapServer();
            ldapsServer.setEnableLdaps(true);
            ldapsServer.setSocketAcceptor(socketAcceptor);
            ldapsServer.setDirectoryService(directoryService);
            ldapsServer.setIpPort(serverConfig.getIntParameter("ldapsPort"));
            ldapsServer.start();
            apacheDS = new ApacheDS(directoryService, ldapServer, ldapsServer);
        }
        if (install != null) {
            apacheDS.getDirectoryService().setWorkingDirectory(install.getPartitionsDirectory());
        }
        apacheDS.startup();
        if (apacheDS.getSynchPeriodMillis() > 0) {
            workerThread = new Thread(worker, "SynchWorkerThread");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("server: started in {} milliseconds", (System.currentTimeMillis() - startTime) + "");
        }
    }

    public DirectoryService getDirectoryService() {
        return apacheDS.getDirectoryService();
    }

    public void synch() throws Exception {
        apacheDS.getDirectoryService().sync();
    }

    public void start() {
        if (workerThread != null) {
            workerThread.start();
        }
    }

    public void stop(String[] args) throws Exception {
        if (workerThread != null) {
            worker.stop = true;
            synchronized (worker.lock) {
                worker.lock.notify();
            }
            while (workerThread.isAlive()) {
                LOG.info("Waiting for SynchWorkerThread to die.");
                workerThread.join(500);
            }
        }
        if (factory != null) {
            factory.close();
        }
        apacheDS.shutdown();
    }

    public void destroy() {
    }

    class SynchWorker implements Runnable {

        final Object lock = new Object();

        boolean stop;

        public void run() {
            while (!stop) {
                synchronized (lock) {
                    try {
                        lock.wait(apacheDS.getSynchPeriodMillis());
                    } catch (InterruptedException e) {
                        LOG.warn("SynchWorker failed to wait on lock.", e);
                    }
                }
                try {
                    synch();
                } catch (Exception e) {
                    LOG.error("SynchWorker failed to synch directory.", e);
                }
            }
        }
    }

    public static final String BANNER = "           _                     _          ____  ____   \n" + "          / \\   _ __   __ _  ___| |__   ___|  _ \\/ ___|  \n" + "         / _ \\ | '_ \\ / _` |/ __| '_ \\ / _ \\ | | \\___ \\   \n" + "        / ___ \\| |_) | (_| | (__| | | |  __/ |_| |___) |  \n" + "       /_/   \\_\\ .__/ \\__,_|\\___|_| |_|\\___|____/|____/   \n" + "               |_|                                                      Oracled!!\n";

    public static void printBanner() {
        System.out.println(BANNER);
    }
}
