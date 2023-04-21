package com.tenline.pinecone.platform.monitor;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.tenline.pinecone.platform.monitor.ui.MainWindow;

/**
 * @author Bill
 * 
 */
public class Activator implements BundleActivator {

    /**
	 * Logger
	 */
    private Logger logger = Logger.getLogger(getClass());

    /**
	 * Main Window
	 */
    private MainWindow window;

    /**
	 * 
	 */
    public Activator() {
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        BundleHelper.getInstance(bundleContext);
        ServiceHelper.getInstance(bundleContext);
        window = new MainWindow();
        window.setVisible(true);
        logger.info("Start Bundle");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        window.close();
        logger.info("Stop Bundle");
    }
}
