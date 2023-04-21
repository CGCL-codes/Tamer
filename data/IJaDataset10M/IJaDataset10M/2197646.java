package welo.servlet.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DataSourceFactory;
import org.apache.cayenne.conf.DriverDataSourceFactory;
import org.apache.cayenne.map.DataMap;
import org.apache.felix.framework.Felix;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import welo.db.cayenne.InputStreamConfiguration;
import welo.model.jcr.CmsCss;
import welo.model.jcr.CmsCssSection;
import welo.osgi.FelixLauncher;
import welo.osgi.Launcher;
import welo.osgi.OsgiRepoFactory;
import welo.osgi.service.config.ConfigBundleRepository;
import welo.osgi.service.extend.ICayenne;
import welo.osgi.service.orm.ORMBundleRepository;
import welo.osgi.service.textarea.TextAreaBundleRepository;
import welo.osgi.service.window.WindowBundleRepository;
import welo.setup.SetupRepository;
import welo.utility.jcr.JcrHelper;
import welo.utility.jcr.RepositoryUtil;

/**
 * Do the necessary initialization, including database data setup etc
 * @author james.yong
 *
 */
public class AppContextListener implements ServletContextListener {

    public static final String KEY_JCR_SERVER = "jcrServer";

    private static ObjectContentManager ocm = null;

    private static String pathToApp = "";

    private Object felix;

    private static BundleContext mainBundleContext;

    private TextAreaBundleRepository TABundleRepo;

    private WindowBundleRepository WBundleRepo;

    private ConfigBundleRepository CBundleRepo;

    private ORMBundleRepository OBundleRepo;

    private Launcher launcher;

    static Configuration sharedConfiguration;

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("contextInitialized(ServletContextEvent event)");
        ServletContext context = event.getServletContext();
        pathToApp = context.getRealPath("/");
        System.out.println("pathToApp is " + pathToApp);
        shutdownDBnJCR(context);
        System.setProperty("java.security.auth.login.config", pathToApp + "WEB-INF/conf/jaas.config");
        launcher = new FelixLauncher();
        this.ocm = startOCM();
        context.setAttribute(KEY_JCR_SERVER, this.ocm);
        setDbConf();
        DataDomain dDomain = Configuration.getSharedConfiguration().getDomain();
        try {
            new SetupRepository().setup(getOCM(), pathToApp + "/WEB-INF");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        boolean startOSGi = true;
        if (startOSGi) {
            try {
                this.felix = this.launcher.startup();
                if (this.felix instanceof Felix) mainBundleContext = ((Felix) this.felix).getBundleContext();
                this.TABundleRepo = OsgiRepoFactory.getTextAreaBundleRepository();
                this.WBundleRepo = OsgiRepoFactory.getWindowBundleRepository();
                this.CBundleRepo = OsgiRepoFactory.getConfigBundleRepository();
                this.OBundleRepo = OsgiRepoFactory.getORMBundleRepository();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mainBundleContext != null) {
                Bundle[] bundles = mainBundleContext.getBundles();
                for (int i = 0; i < bundles.length; i++) {
                    System.out.println("location=" + bundles[i].getLocation());
                }
            }
        }
        DataNode dNode = (DataNode) dDomain.getDataNodes().iterator().next();
        if (startOSGi) {
            setupDB4Bundle(dDomain, dNode);
            try {
                this.launcher.startBundles((Felix) this.felix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collection<DataMap> maps = dDomain.getDataMaps();
        for (Iterator iterator = maps.iterator(); iterator.hasNext(); ) {
            DataMap dataMap = (DataMap) iterator.next();
            System.out.println("==>name of datamap=" + dataMap.getName());
        }
        if (startOSGi) setupDB4Bundle(dDomain, dNode);
    }

    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        shutdownDBnJCR(context);
        context.removeAttribute(KEY_JCR_SERVER);
        try {
            if (this.launcher != null) {
                this.launcher.shutdown(this.felix);
                context.log("OSGi shutdown");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shutdownDBnJCR(ServletContext context) {
        ObjectContentManager ocm = (ObjectContentManager) context.getAttribute(KEY_JCR_SERVER);
        if (ocm != null) {
            JcrHelper.shutdown(ocm.getSession());
            System.out.println("Shut down Jackrabbit repository");
        }
    }

    /**
     * FileConfiguration doesn't work in every app server . Need to set configuration explicitly
     */
    private static void setDbConf() {
        try {
            Configuration conf = new InputStreamConfiguration(new FileInputStream(new File(AppContextListener.getPathToConfig() + "cayenne.xml")), new FileInputStream(new File(AppContextListener.getPathToConfig() + "WeloMap.map.xml"))) {

                public DataSourceFactory getDataSourceFactory() {
                    DataSourceFactory factory = null;
                    try {
                        factory = new DriverDataSourceFactory() {

                            @Override
                            protected InputStream getInputStream(String location) {
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(new File(AppContextListener.getPathToConfig() + location));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return fis;
                            }
                        };
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return factory;
                }
            };
            Configuration.initializeSharedConfiguration(conf);
            sharedConfiguration = Configuration.getSharedConfiguration();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private List<String> setupList = new ArrayList<String>();

    public void setupDB4Bundle(DataDomain dDomain, DataNode dNode) {
        List<String> list1 = this.OBundleRepo.getList();
        for (int i = 0; i < list1.size(); i++) {
            String name = list1.get(i);
            if (setupList.contains(name)) continue;
            setupList.add(name);
            System.out.println("set up for " + name);
            try {
                Object iORM = this.OBundleRepo.getPlugin(name);
                if (iORM instanceof ICayenne) {
                    DataMap dmap = ((ICayenne) iORM).getDataMap();
                    if (dmap != null) {
                        dDomain.addMap(dmap);
                        dNode.addDataMap(dmap);
                        ObjectContext dataContext = Configuration.getSharedConfiguration().getDomain("Welo").createDataContext();
                        ((ICayenne) iORM).initialiseDB(dataContext, dmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPathToApp() {
        return pathToApp;
    }

    public static String getPathToBundle() {
        return pathToApp + "WEB-INF" + File.separator + "bundle" + File.separator;
    }

    public static String getPathToConfig() {
        return pathToApp + "WEB-INF" + File.separator + "conf" + File.separator;
    }

    public static BundleContext getMainBundleContext() {
        return mainBundleContext;
    }

    public static void copyDataDomainDataMap(String sourceDataDomainName, String sourceDataMapName, String targetDataDomainName) {
        DataDomain sharedDataDomain = Configuration.getSharedConfiguration().getDomain(sourceDataDomainName);
        DataDomain targetDataDomain = Configuration.getSharedConfiguration().getDomain(targetDataDomainName);
        targetDataDomain.addMap(sharedDataDomain.getMap(sourceDataMapName));
        Collection nodes = targetDataDomain.getDataNodes();
        if (nodes.size() != 1) {
            System.out.println("Expected only one DataNode for DataDomain '" + targetDataDomainName + "' -- this DataDomain is not usable.");
            return;
        }
        Iterator dataNodeIterator = nodes.iterator();
        while (dataNodeIterator.hasNext()) {
            DataNode node = (DataNode) dataNodeIterator.next();
            node.addDataMap(sharedDataDomain.getMap(sourceDataMapName));
        }
    }

    public static Configuration getSharedConfiguration() {
        if (sharedConfiguration == null) {
            setDbConf();
        }
        return sharedConfiguration;
    }

    private static Session getLocalSession() {
        Repository repository = RepositoryUtil.getTrancientRepository();
        Session session = RepositoryUtil.login(repository, "username", "superuser");
        return session;
    }

    private static ObjectContentManager startOCM() {
        Session session = getLocalSession();
        List<Class> classes = new ArrayList<Class>();
        classes.add(CmsCss.class);
        classes.add(CmsCssSection.class);
        Mapper mapper = new AnnotationMapperImpl(classes);
        return new ObjectContentManagerImpl(session, mapper);
    }

    public static ObjectContentManager getOCM() {
        return ocm;
    }
}
