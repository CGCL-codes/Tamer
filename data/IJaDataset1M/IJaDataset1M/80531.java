package fr.jade.examples.fraclite.adl.helloworld;

import org.objectweb.fractal.api.control.BindingController;

public class ClientImpl implements Runnable, BindingController {

    protected static final String serviceItfName = "s";

    protected Service service;

    protected static final String[] listItf = new String[] { serviceItfName };

    public void run() {
        service.print("hello world");
    }

    public String[] listFc() {
        return listItf;
    }

    public Object lookupFc(final String cItf) {
        if (cItf.equals(serviceItfName)) {
            return service;
        }
        return null;
    }

    public void bindFc(final String cItf, final Object sItf) {
        if (cItf.equals(serviceItfName)) {
            service = (Service) sItf;
        }
    }

    public void unbindFc(final String cItf) {
        if (cItf.equals(serviceItfName)) {
            service = null;
        }
    }
}
