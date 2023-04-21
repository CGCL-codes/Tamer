package org.ws4d.osgi.example.math.gui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class MyServiceTracker implements ServiceTrackerCustomizer {

    private BundleContext bc = null;

    private Object service = null;

    MathClientGui ct = null;

    MyThread updateTableThread;

    public MyServiceTracker(BundleContext bc, MathClientGui ct) {
        this.bc = bc;
        this.ct = ct;
    }

    public Object addingService(ServiceReference reference) {
        this.service = bc.getService(reference);
        updateTableThread = new MyThread();
        return this.service;
    }

    public void modifiedService(ServiceReference reference, Object service) {
        this.removedService(reference, service);
        service = this.addingService(reference);
    }

    public void removedService(ServiceReference reference, Object service) {
        updateTableThread = new MyThread();
    }

    protected class MyThread extends Thread {

        public MyThread() {
            this.start();
        }

        public void run() {
            try {
                sleep(10);
            } catch (Exception e) {
            }
            ct.updateTableData();
        }
    }
}
