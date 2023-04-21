package com.monad.homerun.objmgt.impl;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.InvalidSyntaxException;
import com.monad.homerun.config.ConfigService;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.log.LogService;
import com.monad.homerun.modelmgt.ModelService;
import com.monad.homerun.msgmgt.MessageService;
import com.monad.homerun.objmgt.ActionService;
import com.monad.homerun.objmgt.HandlerFactory;
import com.monad.homerun.objmgt.ObjectFactory;
import com.monad.homerun.objmgt.ObjectService;
import com.monad.homerun.store.ObjectStore;
import com.monad.homerun.store.ObjectStoreFactory;
import com.monad.homerun.timing.TimingService;

/**
 * OSGi Activator class for object management bundle
 */
public class Activator implements BundleActivator {

    private static BundleContext bc;

    static LogService logSvc;

    static ConfigService cfgSvc;

    static TimingService timingSvc;

    static ObjectStoreFactory storeFact;

    static ActionService actSvc;

    static ModelService modelSvc;

    static ObjectService objSvc;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        bc = context;
        ServiceReference svcRef = bc.getServiceReference(ObjectStoreFactory.class.getName());
        storeFact = (ObjectStoreFactory) bc.getService(svcRef);
        svcRef = bc.getServiceReference(LogService.class.getName());
        logSvc = (LogService) bc.getService(svcRef);
        svcRef = bc.getServiceReference(ConfigService.class.getName());
        cfgSvc = (ConfigService) bc.getService(svcRef);
        svcRef = bc.getServiceReference(TimingService.class.getName());
        timingSvc = (TimingService) bc.getService(svcRef);
        svcRef = bc.getServiceReference(ModelService.class.getName());
        modelSvc = (ModelService) bc.getService(svcRef);
        objSvc = new ObjectManager();
        context.registerService(ObjectService.class.getName(), objSvc, new Hashtable());
        actSvc = new ActionManager();
        context.registerService(ActionService.class.getName(), actSvc, new Hashtable());
    }

    public void stop(BundleContext context) throws Exception {
        bc = null;
    }

    static ObjectFactory getObjectFactory(String factName) {
        ObjectFactory factory = null;
        String filter = "(name=" + factName + ")";
        try {
            ServiceReference[] refs = bc.getServiceReferences(ObjectFactory.class.getName(), filter);
            if (refs != null && refs.length > 0) {
                factory = (ObjectFactory) bc.getService(refs[0]);
            } else if (GlobalProps.DEBUG) {
                System.out.println("getObjFact - no factory for: " + factName);
            }
        } catch (InvalidSyntaxException isE) {
            if (GlobalProps.DEBUG) {
                System.out.println("getObjFact - ISE ");
                isE.printStackTrace();
            }
        }
        return factory;
    }

    static HandlerFactory getHandlerFactory(String factName) {
        HandlerFactory factory = null;
        String filter = "(name=" + factName + ")";
        try {
            ServiceReference[] refs = bc.getServiceReferences(ObjectFactory.class.getName(), filter);
            if (refs != null && refs.length > 0) {
                factory = (HandlerFactory) bc.getService(refs[0]);
            } else if (GlobalProps.DEBUG) {
                System.out.println("getHandlerFactory - no factory ");
            }
        } catch (InvalidSyntaxException isE) {
            if (GlobalProps.DEBUG) {
                System.out.println("getHandlerFact - ISE ");
                isE.printStackTrace();
            }
        }
        return factory;
    }

    static MessageService getMessageService() {
        ServiceReference ref = bc.getServiceReference(MessageService.class.getName());
        MessageService msgSvc = (MessageService) bc.getService(ref);
        return msgSvc;
    }

    static ObjectStore getStore(String area, Class factClass) {
        return storeFact.getInstance(area, factClass);
    }

    static BundleContext getBundleContext() {
        return bc;
    }
}
