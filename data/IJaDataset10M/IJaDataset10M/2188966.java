package ch.jester.commonservices.impl.web;

import java.net.Proxy;
import ch.jester.common.utility.ServiceConsumer;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.api.web.IHTTPProxy;

/**
 * ComponentService für HTTProxy Manipulation via UI
 *
 */
public class HTTPProxyAdapter extends ServiceConsumer implements IHTTPProxy, IPreferenceManagerProvider {

    private static final String PP_ID_PORT = "port";

    private static final String PP_ID_ADDRESS = "address";

    private static final String PP_DEF_ADDRESS = "";

    private static final String PM_ID = "ch.jester.proxyservice";

    private String mAddress;

    private int mPort;

    private IPreferenceManager pm = getServiceUtility().getService(IPreferenceRegistration.class).createManager();

    public HTTPProxyAdapter() {
        pm.setId(PM_ID);
        pm.registerProviderAtRegistrationService(this);
        mAddress = pm.create(PP_ID_ADDRESS, "Address", PP_DEF_ADDRESS).getValue().toString();
        mPort = (Integer) pm.create(PP_ID_PORT, "Port", 0).getValue();
        getLogger().debug("HTTPProxyInit: " + mAddress + " / " + mPort);
        if (mAddress != PP_DEF_ADDRESS && mPort != 0) {
            createHTTPProxy(mAddress, mPort);
        } else {
            getLogger().info("no HTTPProxy created");
        }
        pm.addListener(new IPreferencePropertyChanged() {

            boolean[] created = { false, false };

            String address;

            int port;

            @Override
            public void propertyValueChanged(String internalKey, Object mValue, IPreferenceProperty preferenceProperty) {
                if (internalKey == PP_ID_ADDRESS) {
                    address = mValue.toString();
                    created[0] = true;
                }
                if (internalKey == PP_ID_PORT) {
                    port = (Integer) mValue;
                    created[1] = true;
                }
                if (created[0] == true && created[1] == true) {
                    created[0] = false;
                    created[1] = false;
                    if (port == 0 && address.equals(PP_DEF_ADDRESS)) {
                        HTTPProxyAdapter.this.deleteProxy();
                        getLogger().info("Deleted HTTP proxy: " + mAddress + " / " + mPort);
                        return;
                    }
                    mAddress = address;
                    mPort = (Integer) pm.getPropertyByInternalKey(PP_ID_PORT).getValue();
                    createHTTPProxy(mAddress, mPort);
                }
            }
        });
    }

    @Override
    public void createHTTPProxy(String pProxyAdress, int pProxyPort) {
        HTTPFactory.createHTTPProxy(pProxyAdress, pProxyPort);
        mAddress = pProxyAdress;
        mPort = pProxyPort;
        getLogger().info("Created HTTP proxy: " + pProxyAdress + " / " + pProxyPort);
    }

    @Override
    public Proxy getHTTPProxy() {
        return HTTPFactory.getHTTPProxy();
    }

    @Override
    public String getAddress() {
        return mAddress;
    }

    @Override
    public int getPort() {
        return mPort;
    }

    @Override
    public void deleteProxy() {
        HTTPFactory.reset();
    }

    @Override
    public IPreferenceManager getPreferenceManager(String pKey) {
        return pm.checkId(pKey);
    }
}
