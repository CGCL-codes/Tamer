package net.sf.daro.timetracker.internal;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import net.sf.daro.core.page.Page;
import net.sf.daro.timetracker.domain.AbstractEntity;
import net.sf.daro.timetracker.page.ActivityManagerPage;
import net.sf.daro.timetracker.persistence.DataStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author daniel
 */
public class TimeTrackerPlugin implements BundleActivator {

    /**
	 * shared instance
	 */
    private static TimeTrackerPlugin instance;

    /**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
    public static TimeTrackerPlugin getInstance() {
        return instance;
    }

    /**
	 * context
	 */
    private BundleContext context;

    /**
	 * page services
	 */
    private List<Page> pages = new LinkedList<Page>();

    private List<ServiceRegistration> pageRegistrations = new LinkedList<ServiceRegistration>();

    /**
	 * Creates a new TimeTrackerPlugin.
	 */
    public TimeTrackerPlugin() {
        super();
        pages.add(new ActivityManagerPage());
    }

    /**
	 * @return the context
	 */
    public BundleContext getContext() {
        return context;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see BundleActivator#start(BundleContext)
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        AbstractEntity.setDesktopSystem(true);
        DataStore.setInstance(new DataStore());
        for (Page page : pages) {
            page.init();
            pageRegistrations.add(context.registerService(Page.class.getName(), page, new Hashtable<Object, Object>()));
        }
        instance = this;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see BundleActivator#stop(BundleContext)
	 */
    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
        for (ServiceRegistration registration : pageRegistrations) {
            registration.unregister();
        }
        for (Page page : pages) {
            page.dispose();
        }
        DataStore.setInstance(null);
        this.context = null;
    }
}
