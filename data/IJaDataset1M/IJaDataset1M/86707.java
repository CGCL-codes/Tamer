package org.mobicents.slee.resource.diameter.sh.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;
import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ObjectFactory;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactoryImpl;

/**
 * 
 * Implementation of {@link UserDataAnswer} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserDataAnswerImpl extends DiameterShMessageImpl implements UserDataAnswer {

    private static final long serialVersionUID = -6240588207973076841L;

    private static JAXBContext jaxbContext = initJAXBContext();

    private static UserDataObjectFactory udof = new UserDataObjectFactoryImpl(new ObjectFactory());

    private static JAXBContext initJAXBContext() {
        try {
            return JAXBContext.newInstance(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * 
   * @param msg
   */
    public UserDataAnswerImpl(Message msg) {
        super(msg);
        msg.setRequest(false);
        msg.setReTransmitted(false);
        super.longMessageName = "User-Data-Answer";
        super.shortMessageName = "UDA";
    }

    public boolean hasUserData() {
        return hasAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public byte[] getUserData() {
        return getAvpAsOctetString(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public ShData getUserDataObject() throws IOException {
        ShData shDataObject = null;
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            byte[] data = getAvpAsRaw(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
            JAXBElement<TShData> jaxbElem = udof.createShData((TShData) unmarshaller.unmarshal(new ByteArrayInputStream(data)));
            shDataObject = (ShData) jaxbElem.getValue();
        } catch (Exception e) {
            throw new IOException("Failed to unmarshal User-Data AVP into JAXB Object", e);
        }
        return shDataObject;
    }

    public void setUserData(byte[] userData) {
        addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
    }

    public void setUserDataObject(ShData userData) throws IOException {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(userData, baos);
            addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, baos.toByteArray());
        } catch (Exception e) {
            throw new IOException("Failed to marshal JAXB Object to User-Data AVP", e);
        }
    }

    public boolean hasExperimentalResult() {
        return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
    }

    public ExperimentalResultAvp getExperimentalResult() {
        return (ExperimentalResultAvp) getAvpAsCustom(Avp.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
    }

    public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
        addAvp(Avp.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
    }

    public boolean hasWildcardedPSI() {
        return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public String getWildcardedPSI() {
        return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public void setWildcardedPSI(String wildcardedPSI) {
        addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
    }

    public boolean hasWildcardedIMPU() {
        return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public String getWildcardedIMPU() {
        return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
    }

    public void setWildcardedIMPU(String wildcardedIMPU) {
        addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
    }
}
