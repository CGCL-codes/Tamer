package fr.esrf.tangoatk.core.attribute;

import fr.esrf.tangoatk.core.*;
import fr.esrf.Tango.*;
import fr.esrf.TangoApi.*;

class RawImageHelper implements java.io.Serializable {

    IAttribute attribute;

    EventSupport propChanges;

    String encFormat = null;

    public RawImageHelper(IAttribute attribute) {
        init(attribute);
    }

    void init(IAttribute attribute) {
        setAttribute(attribute);
        propChanges = ((AAttribute) attribute).getPropChanges();
    }

    public void setAttribute(IAttribute attribute) {
        this.attribute = attribute;
    }

    public IAttribute getAttribute() {
        return attribute;
    }

    protected void setProperty(String name, Number value) {
        attribute.setProperty(name, value);
        attribute.storeConfig();
    }

    protected void setProperty(String name, Number value, boolean writable) {
        attribute.setProperty(name, value, writable);
    }

    void fireRawImageValueChanged(String encFormat, byte[] newValue, long timeStamp) {
        propChanges.fireRawImageEvent((IRawImage) attribute, encFormat, newValue, timeStamp);
    }

    byte[] getRawImageValue(DeviceAttribute deviceAttribute) throws DevFailed {
        DevEncoded e = deviceAttribute.extractDevEncoded();
        encFormat = e.encoded_format;
        return e.encoded_data;
    }

    String getRawImageFormat() {
        return encFormat;
    }

    void addRawImageListener(IRawImageListener l) {
        propChanges.addRawImageListener(l);
    }

    void removeRawImageListener(IRawImageListener l) {
        propChanges.removeRawImageListener(l);
    }

    public String getVersion() {
        return "$Id: RawImageHelper.java 13210 2009-03-26 10:00:03Z jlpons $";
    }
}
