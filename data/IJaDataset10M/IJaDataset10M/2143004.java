package org.mobicents.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp;

/**
 * Start time:13:01:03 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * Implementation of {@link CcMoneyAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CcMoneyAvpImpl extends MoneyLikeAvpImpl implements CcMoneyAvp {

    public CcMoneyAvpImpl() {
        super();
    }

    public CcMoneyAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }
}
