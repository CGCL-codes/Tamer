package org.mobicents.servlet.sip.core.b2bua;

import javax.servlet.sip.B2buaHelper;
import org.mobicents.servlet.sip.core.MobicentsSipFactory;
import org.mobicents.servlet.sip.core.SipManager;
import org.mobicents.servlet.sip.core.message.MobicentsSipServletRequest;

/**
 * Extenstion from the B2BUAHelpr from Sip Servlets spec giving access to internals
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public interface MobicentsB2BUAHelper extends B2buaHelper {

    void setMobicentsSipFactory(MobicentsSipFactory sipFactoryImpl);

    void setSipManager(SipManager sipManager);

    void unlinkOriginalRequestInternal(MobicentsSipServletRequest sipServletMessage, boolean b);
}
