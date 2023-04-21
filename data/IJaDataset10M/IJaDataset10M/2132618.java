package fr.esrf.tangoatk.core.attribute;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.EventSupport;
import fr.esrf.tangoatk.core.IDevStateSpectrum;
import fr.esrf.tangoatk.core.IDevStateSpectrumListener;

class DevStateSpectrumHelper implements java.io.Serializable {

    AAttribute attribute;

    EventSupport propChanges;

    public DevStateSpectrumHelper(AAttribute attribute) {
        init(attribute);
    }

    void init(AAttribute attribute) {
        setAttribute(attribute);
        propChanges = attribute.getPropChanges();
    }

    public void setAttribute(AAttribute attribute) {
        this.attribute = attribute;
    }

    public AAttribute getAttribute() {
        return attribute;
    }

    protected void setProperty(String name, Number value) {
        attribute.setProperty(name, value);
        attribute.storeConfig();
    }

    protected void setProperty(String name, Number value, boolean writable) {
        attribute.setProperty(name, value, writable);
    }

    void fireDevStateSpectrumValueChanged(String[] newValue, long timeStamp) {
        propChanges.fireDevStateSpectrumEvent((IDevStateSpectrum) attribute, newValue, timeStamp);
    }

    void insert(String[] stateSpect) {
        DevState[] devStatesArray = new DevState[stateSpect.length];
        DeviceAttribute da = this.attribute.getAttribute();
        for (int i = 0; i < stateSpect.length; i++) {
            devStatesArray[i] = Device.getStateFromString(stateSpect[i]);
        }
        da.insert(devStatesArray);
    }

    String[] getStateSpectrumValue(DeviceAttribute da) throws DevFailed {
        String[] retval = null;
        DevState[] devStates = null;
        int nbReadElements;
        devStates = da.extractDevStateArray();
        nbReadElements = da.getNbRead();
        retval = new String[nbReadElements];
        for (int i = 0; i < nbReadElements; i++) {
            retval[i] = Device.toString(devStates[i]);
        }
        return retval;
    }

    String[] getStateSpectrumSetPoint(DeviceAttribute da) throws DevFailed {
        DevState[] devStates = null;
        int nbReadElements;
        int nbSetElements;
        String[] retval = null;
        devStates = da.extractDevStateArray();
        nbReadElements = da.getNbRead();
        nbSetElements = devStates.length - nbReadElements;
        if (nbSetElements <= 0) {
            return getStateSpectrumValue(da);
        } else {
            retval = new String[nbSetElements];
            int j = 0;
            for (int i = nbReadElements; i < devStates.length; i++) {
                retval[j] = Device.toString(devStates[i]);
                j++;
            }
            return retval;
        }
    }

    void addDevStateSpectrumListener(IDevStateSpectrumListener l) {
        propChanges.addDevStateSpectrumListener(l);
    }

    void removeDevStateSpectrumListener(IDevStateSpectrumListener l) {
        propChanges.removeDevStateSpectrumListener(l);
    }

    public String getVersion() {
        return "$Id: DevStateSpectrumHelper.java 15105 2010-09-24 13:11:54Z poncet $";
    }
}
