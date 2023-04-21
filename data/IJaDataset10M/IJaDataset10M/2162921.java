package org.jboss.web.tomcat.service.session;

import org.jboss.web.tomcat.service.session.distributedcache.spi.DistributableSipSessionMetadata;
import org.jboss.web.tomcat.service.session.distributedcache.spi.OutgoingDistributableSipSessionData;
import org.mobicents.servlet.sip.core.session.SipApplicationSessionKey;
import org.mobicents.servlet.sip.core.session.SipSessionKey;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public class OutgoingDistributableSipSessionDataImpl extends OutgoingDistributableSessionDataImpl implements OutgoingDistributableSipSessionData {

    private String sipApplicationSessionKey;

    private String sipSessionKey;

    private boolean isSessionMetaDataDirty;

    public OutgoingDistributableSipSessionDataImpl(String realId, int version, Long timestamp, String sipApplicationSessionKey, String sipSessionKey, DistributableSipSessionMetadata metadata) {
        super(realId, version, timestamp, metadata);
        this.sipApplicationSessionKey = sipApplicationSessionKey;
        this.sipSessionKey = sipSessionKey;
    }

    public String getSipApplicationSessionKey() {
        return this.sipApplicationSessionKey;
    }

    public String getSipSessionKey() {
        return this.sipSessionKey;
    }

    /**
	 * @param isSessionMetaDataDirty the isSessionMetaDataDirty to set
	 */
    public void setSessionMetaDataDirty(boolean isSessionMetaDataDirty) {
        this.isSessionMetaDataDirty = isSessionMetaDataDirty;
    }

    /**
	 * @return the isSessionMetaDataDirty
	 */
    public boolean isSessionMetaDataDirty() {
        return isSessionMetaDataDirty;
    }
}
