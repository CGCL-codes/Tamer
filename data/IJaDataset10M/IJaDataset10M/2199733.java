package fr.esrf.tangoatk.core.attribute;

import fr.esrf.tangoatk.core.*;
import fr.esrf.Tango.*;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoApi.events.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DevStateSpectrum extends AAttribute implements IDevStateSpectrum {

    DevStateSpectrumHelper dsSpectrumHelper;

    String[] dsSpectrumValue = null;

    String[] dsSpectrumSetPointValue = null;

    String[] stateLabels;

    boolean[] invertOpenClose;

    boolean[] invertInsertExtract;

    public DevStateSpectrum() {
        stateLabels = null;
        invertOpenClose = null;
        invertInsertExtract = null;
        dsSpectrumHelper = new DevStateSpectrumHelper(this);
    }

    public int getXDimension() {
        return 1;
    }

    public int getMaxXDimension() {
        return 1;
    }

    public String[] getValue() {
        return dsSpectrumValue;
    }

    public String[] getSetPoint() {
        return dsSpectrumSetPointValue;
    }

    public String[] getDeviceValue() {
        DeviceAttribute da;
        try {
            da = readValueFromNetwork();
            dsSpectrumValue = dsSpectrumHelper.getStateSpectrumValue(da);
            dsSpectrumSetPointValue = dsSpectrumHelper.getStateSpectrumSetPoint(da);
        } catch (DevFailed e) {
            readAttError(e.getMessage(), new AttributeReadException(e));
        } catch (Exception e) {
            System.out.println("DevStateSpectrum.getDeviceValue() Exception caught ------------------------------");
            e.printStackTrace();
            System.out.println("DevStateSpectrum.getDeviceValue()------------------------------------------------");
        }
        return dsSpectrumValue;
    }

    public void refresh() {
        DeviceAttribute att = null;
        long t0 = System.currentTimeMillis();
        if (skippingRefresh) return;
        refreshCount++;
        trace(DeviceFactory.TRACE_REFRESHER, "DevStateSpectrum.refresh() method called for " + getName(), t0);
        try {
            try {
                att = readValueFromNetwork();
                trace(DeviceFactory.TRACE_REFRESHER, "DevStateSpectrum.refresh(" + getName() + ") readValueFromNetwork success", t0);
                if (att == null) return;
                dsSpectrumValue = dsSpectrumHelper.getStateSpectrumValue(att);
                dsSpectrumSetPointValue = dsSpectrumHelper.getStateSpectrumSetPoint(att);
                fireValueChanged(dsSpectrumValue);
                trace(DeviceFactory.TRACE_REFRESHER, "DevStateSpectrum.refresh(" + getName() + ") fireValueChanged(devStateValue) success", t0);
            } catch (DevFailed e) {
                trace(DeviceFactory.TRACE_REFRESHER, "DevStateSpectrum.refresh(" + getName() + ") failed, caught DevFailed; will call readAttError", t0);
                readAttError(e.getMessage(), new AttributeReadException(e));
            }
        } catch (Exception e) {
            trace(DeviceFactory.TRACE_REFRESHER, "DevStateSpectrum.refresh(" + getName() + ") Code failure, caught other Exception", t0);
            System.out.println("DevStateSpectrum.refresh() Exception caught ------------------------------");
            e.printStackTrace();
            System.out.println("DevStateSpectrum.refresh()------------------------------------------------");
        }
    }

    public void dispatch(DeviceAttribute attValue) {
        if (skippingRefresh) return;
        refreshCount++;
        try {
            try {
                if (attValue == null) return;
                attribute = attValue;
                setState(attValue);
                timeStamp = attValue.getTimeValMillisSec();
                dsSpectrumValue = dsSpectrumHelper.getStateSpectrumValue(attValue);
                dsSpectrumSetPointValue = dsSpectrumHelper.getStateSpectrumSetPoint(attValue);
                fireValueChanged(dsSpectrumValue);
            } catch (DevFailed e) {
                dispatchError(e);
            }
        } catch (Exception e) {
            System.out.println("DevStateSpectrum.dispatch() Exception caught ------------------------------");
            e.printStackTrace();
            System.out.println("DevStateSpectrum.dispatch()------------------------------------------------");
        }
    }

    public void dispatchError(DevFailed e) {
        readAttError(e.getMessage(), new AttributeReadException(e));
    }

    public boolean isWritable() {
        return super.isWritable();
    }

    protected void fireValueChanged(String[] newValue) {
        dsSpectrumHelper.fireDevStateSpectrumValueChanged(newValue, timeStamp);
    }

    public void addDevStateSpectrumListener(IDevStateSpectrumListener l) {
        dsSpectrumHelper.addDevStateSpectrumListener(l);
        addStateListener(l);
    }

    public void removeDevStateSpectrumListener(IDevStateSpectrumListener l) {
        dsSpectrumHelper.removeDevStateSpectrumListener(l);
        removeStateListener(l);
    }

    public void periodic(TangoPeriodicEvent evt) {
        periodicCount++;
        DeviceAttribute da = null;
        long t0 = System.currentTimeMillis();
        trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodic method called for " + getName(), t0);
        try {
            da = evt.getValue();
            trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodicEvt.getValue(" + getName() + ") success", t0);
        } catch (DevFailed dfe) {
            trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodicEvt.getValue(" + getName() + ") failed, caught DevFailed", t0);
            if (dfe.errors[0].reason.equals("API_EventTimeout")) {
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodicEvt.getValue(" + getName() + ") failed, got heartbeat error", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            } else {
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodicEvt.getValue(" + getName() + ") failed, got other error", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            }
            return;
        } catch (Exception e) {
            trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodicEvt.getValue(" + getName() + ") failed, caught Exception, code failure", t0);
            System.out.println("DevStateSpectrum.periodic.getValue() Exception caught ------------------------------");
            e.printStackTrace();
            System.out.println("DevStateSpectrum.periodic.getValue()------------------------------------------------");
            return;
        }
        if (da != null) {
            try {
                setState(da);
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodic(" + getName() + ") setState(da) called", t0);
                attribute = da;
                timeStamp = da.getTimeValMillisSec();
                dsSpectrumValue = dsSpectrumHelper.getStateSpectrumValue(da);
                dsSpectrumSetPointValue = dsSpectrumHelper.getStateSpectrumSetPoint(da);
                fireValueChanged(dsSpectrumValue);
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodic(" + getName() + ") fireValueChanged(devStateValue) called", t0);
            } catch (DevFailed dfe) {
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodic(" + getName() + ") failed, got DevFailed when called fireValueChanged(devStateValue)", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            } catch (Exception e) {
                trace(DeviceFactory.TRACE_PERIODIC_EVENT, "DevStateSpectrum.periodic(" + getName() + ") failed, got other Exception when called fireValueChanged(devStateValue)", t0);
                System.out.println("DevStateSpectrum.periodic: Device.toString(extractState()) Exception caught ------------------------------");
                e.printStackTrace();
                System.out.println("DevStateSpectrum.periodic: Device.toString(extractState())------------------------------------------------");
            }
        }
    }

    public void change(TangoChangeEvent evt) {
        changeCount++;
        DeviceAttribute da = null;
        long t0 = System.currentTimeMillis();
        trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.change method called for " + getName(), t0);
        try {
            da = evt.getValue();
            trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.changeEvt.getValue(" + getName() + ") success", t0);
        } catch (DevFailed dfe) {
            trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.changeEvt.getValue(" + getName() + ") failed, caught DevFailed", t0);
            if (dfe.errors[0].reason.equals("API_EventTimeout")) {
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.changeEvt.getValue(" + getName() + ") failed, got heartbeat error", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            } else {
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.changeEvt.getValue(" + getName() + ") failed, got other error", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            }
            return;
        } catch (Exception e) {
            trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.changeEvt.getValue(" + getName() + ") failed, caught Exception, code failure", t0);
            System.out.println("DevStateSpectrum.change.getValue() Exception caught ------------------------------");
            e.printStackTrace();
            System.out.println("DevStateSpectrum.change.getValue()------------------------------------------------");
            return;
        }
        if (da != null) {
            try {
                setState(da);
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.change(" + getName() + ") setState(da) called", t0);
                attribute = da;
                timeStamp = da.getTimeValMillisSec();
                dsSpectrumValue = dsSpectrumHelper.getStateSpectrumValue(da);
                dsSpectrumSetPointValue = dsSpectrumHelper.getStateSpectrumSetPoint(da);
                fireValueChanged(dsSpectrumValue);
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.change(" + getName() + ") fireValueChanged(devStateValue) called", t0);
            } catch (DevFailed dfe) {
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.change(" + getName() + ") failed, got DevFailed when called fireValueChanged(devStateValue)", t0);
                readAttError(dfe.getMessage(), new AttributeReadException(dfe));
            } catch (Exception e) {
                trace(DeviceFactory.TRACE_CHANGE_EVENT, "DevStateSpectrum.change(" + getName() + ") failed, got other Exception when called fireValueChanged(devStateValue)", t0);
                System.out.println("DevStateSpectrum.change: Device.toString(extractState()) Exception caught ------------------------------");
                e.printStackTrace();
                System.out.println("DevStateSpectrum.change: Device.toString(extractState())------------------------------------------------");
            }
        }
    }

    public void setValue(String[] states) throws AttributeSetException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getStateLabels() {
        return stateLabels;
    }

    public boolean getInvertedOpenCloseForElement(int elemIndex) {
        if (invertOpenClose == null) return false;
        if ((elemIndex < 0) || (elemIndex >= invertOpenClose.length)) return false;
        return invertOpenClose[elemIndex];
    }

    public boolean getInvertedInsertExtractForElement(int elemIndex) {
        if (invertInsertExtract == null) return false;
        if ((elemIndex < 0) || (elemIndex >= invertInsertExtract.length)) return false;
        return invertInsertExtract[elemIndex];
    }

    void setInvertedOpenClose(String[] openCloseLogic) {
        String elemLogic;
        HashMap<Integer, Boolean> ocmap = new HashMap<Integer, Boolean>();
        for (int ind = 0; ind < openCloseLogic.length; ind++) {
            elemLogic = openCloseLogic[ind];
            parseElemLogic(elemLogic, ocmap);
        }
        Set<Integer> indexSet = ocmap.keySet();
        if (indexSet == null) return;
        if (indexSet.isEmpty()) return;
        TreeSet<Integer> indexSortedSet = new TreeSet<Integer>(indexSet);
        Integer lastElemIndex = indexSortedSet.last();
        int arrayLength = lastElemIndex.intValue() + 1;
        invertOpenClose = new boolean[arrayLength];
        for (int i = 0; i < arrayLength; i++) invertOpenClose[i] = false;
        Iterator<Integer> it = indexSet.iterator();
        while (it.hasNext()) {
            Integer key = it.next();
            int indexElem = key.intValue();
            if ((indexElem > 0) && (indexElem < invertOpenClose.length)) invertOpenClose[indexElem] = ocmap.get(key).booleanValue();
        }
    }

    void setInvertedInsertExtract(String[] insertExtractLogic) {
        String elemLogic;
        HashMap<Integer, Boolean> iemap = new HashMap<Integer, Boolean>();
        for (int ind = 0; ind < insertExtractLogic.length; ind++) {
            elemLogic = insertExtractLogic[ind];
            parseElemLogic(elemLogic, iemap);
        }
        Set<Integer> indexSet = iemap.keySet();
        if (indexSet == null) return;
        if (indexSet.isEmpty()) return;
        TreeSet<Integer> indexSortedSet = new TreeSet<Integer>(indexSet);
        Integer lastElemIndex = indexSortedSet.last();
        int arrayLength = lastElemIndex.intValue() + 1;
        invertInsertExtract = new boolean[arrayLength];
        for (int i = 0; i < arrayLength; i++) invertInsertExtract[i] = false;
        Iterator<Integer> it = indexSet.iterator();
        while (it.hasNext()) {
            Integer key = it.next();
            int indexElem = key.intValue();
            if ((indexElem > 0) && (indexElem < invertInsertExtract.length)) invertInsertExtract[indexElem] = iemap.get(key).booleanValue();
        }
    }

    private void parseElemLogic(String str, HashMap<Integer, Boolean> elemMap) {
        int colon = -1;
        String indexStr = null, boolStr = null;
        Integer index = null;
        Boolean elemLogic = null;
        if (str == null) return;
        if (str.length() < 3) return;
        colon = str.indexOf(":");
        if (colon < 1) return;
        if (colon >= (str.length() - 1)) return;
        try {
            indexStr = str.substring(0, colon);
            boolStr = str.substring(colon + 1);
        } catch (IndexOutOfBoundsException iob) {
            return;
        }
        try {
            index = Integer.valueOf(indexStr.trim());
        } catch (NumberFormatException nfe) {
            return;
        }
        elemLogic = Boolean.valueOf(boolStr.trim());
        if (elemMap.containsKey(index)) elemMap.remove(index);
        elemMap.put(index, elemLogic);
    }

    @Override
    public void loadAttProperties() {
        DbAttribute dbAtt = null;
        DbDatum propDbDatum = null;
        String[] invertedOpenCloseLogic = null;
        String[] invertedInsertExtractLogic = null;
        String[] stateLbls = null;
        try {
            attPropertiesLoaded = true;
            dbAtt = this.getDevice().get_attribute_property(this.getNameSansDevice());
            if (dbAtt == null) return;
            if (!dbAtt.is_empty(IDevStateSpectrum.STATE_LABELS)) {
                propDbDatum = dbAtt.datum(IDevStateSpectrum.STATE_LABELS);
                if (propDbDatum != null) if (!propDbDatum.is_empty()) stateLbls = propDbDatum.extractStringArray();
                if ((stateLbls != null) && (stateLbls.length > 0)) stateLabels = stateLbls;
            }
            if (!dbAtt.is_empty(fr.esrf.tangoatk.core.Device.OPEN_CLOSE_PROP)) {
                propDbDatum = dbAtt.datum(fr.esrf.tangoatk.core.Device.OPEN_CLOSE_PROP);
                if (propDbDatum != null) if (!propDbDatum.is_empty()) invertedOpenCloseLogic = propDbDatum.extractStringArray();
                if ((invertedOpenCloseLogic != null) && (invertedOpenCloseLogic.length > 0)) setInvertedOpenClose(invertedOpenCloseLogic);
            }
            if (!dbAtt.is_empty(fr.esrf.tangoatk.core.Device.INSERT_EXTRACT_PROP)) {
                propDbDatum = dbAtt.datum(fr.esrf.tangoatk.core.Device.INSERT_EXTRACT_PROP);
                if (propDbDatum != null) if (!propDbDatum.is_empty()) invertedInsertExtractLogic = propDbDatum.extractStringArray();
                if ((invertedInsertExtractLogic != null) && (invertedInsertExtractLogic.length > 0)) setInvertedInsertExtract(invertedInsertExtractLogic);
            }
        } catch (Exception ex) {
            System.out.println("get_attribute_property(" + this.getName() + ") thrown exception");
            ex.printStackTrace();
        }
    }

    private void trace(int level, String msg, long time) {
        DeviceFactory.getInstance().trace(level, msg, time);
    }

    public String getVersion() {
        return "$Id: DevStateSpectrum.java 13506 2009-06-29 16:18:22Z poncet $";
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        System.out.print("Loading attribute ");
        in.defaultReadObject();
        serializeInit();
    }
}
