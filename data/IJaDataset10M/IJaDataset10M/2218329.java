package com.cbsgmbh.xi.af.as2.as2.request;

import java.security.NoSuchAlgorithmException;
import com.cbsgmbh.xi.af.as2.jca.configuration.ConfigurationSettings;
import com.sap.dtr.client.lib.protocol.entities.ByteArrayEntity;

public interface As2Request {

    public abstract void enableBasicAuthentication(String user, String password) throws NoSuchAlgorithmException;

    public abstract void enableProxy(String proxyHost, int proxyPort, String proxyUser, String proxyPassword);

    public abstract void setEngineCertificateStores(String store);

    public abstract void close();

    public abstract As2Response send(ConfigurationSettings configuration, String url, String path, String query, String fromAS2Party, String toAS2Party, String messageId, ByteArrayEntity byteArrayEntity) throws Exception;
}
