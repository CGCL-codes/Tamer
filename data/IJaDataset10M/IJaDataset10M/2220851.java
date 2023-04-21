package net.sf.mxlosgi.vcard;

import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.vcard.impl.VCardManagerImpl;
import net.sf.mxlosgi.vcard.parser.VCardExtensionParser;
import net.sf.mxlosgi.vcard.parser.VCardTempUpdateExtensionParser;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, ServiceListener {

    private Bundle bundle;

    private ServiceRegistration vCardParserRegistration;

    private ServiceRegistration vCardTempParserRegistration;

    private ServiceRegistration vCardFeatureRegistration;

    private ServiceRegistration vCardManagerRegistration;

    public void start(BundleContext context) throws Exception {
        this.bundle = context.getBundle();
        VCardExtensionParser vCardParser = new VCardExtensionParser();
        vCardParserRegistration = context.registerService(ExtensionParser.class.getName(), vCardParser, null);
        VCardTempUpdateExtensionParser vCardTempParser = new VCardTempUpdateExtensionParser();
        vCardTempParserRegistration = context.registerService(ExtensionParser.class.getName(), vCardTempParser, null);
        DiscoInfoFeature vCardFeature = new DiscoInfoFeature(null, "vcard-temp");
        vCardFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), vCardFeature, null);
        VCardManagerImpl vCardManager = new VCardManagerImpl();
        vCardManagerRegistration = context.registerService(VCardManager.class.getName(), vCardManager, null);
        String filter = "(objectclass=" + XmppParser.class.getName() + ")";
        context.addServiceListener(this, filter);
    }

    public void stop(BundleContext context) throws Exception {
        if (vCardFeatureRegistration != null) {
            vCardFeatureRegistration.unregister();
            vCardFeatureRegistration = null;
        }
        if (vCardParserRegistration != null) {
            vCardParserRegistration.unregister();
            vCardParserRegistration = null;
        }
        if (vCardTempParserRegistration != null) {
            vCardTempParserRegistration.unregister();
            vCardTempParserRegistration = null;
        }
        if (vCardManagerRegistration != null) {
            vCardManagerRegistration.unregister();
            vCardManagerRegistration = null;
        }
        bundle = null;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        int eventType = event.getType();
        if (eventType == ServiceEvent.UNREGISTERING) {
            try {
                bundle.uninstall();
            } catch (BundleException e) {
            }
        }
    }
}
