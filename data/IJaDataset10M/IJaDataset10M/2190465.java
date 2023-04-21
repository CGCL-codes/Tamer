package blue.orchestra.blueSynthBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import electric.xml.Element;
import electric.xml.Elements;
import java.rmi.dgc.VMID;

/**
 * @author steven
 */
public class Preset implements Serializable, Comparable {

    private String presetName = "";

    private HashMap valuesMap = new HashMap();

    private String uniqueId;

    public Preset() {
        this.uniqueId = Integer.toString(new VMID().hashCode());
    }

    public static Preset createPreset(BSBGraphicInterface bsbInterface) {
        Preset preset = new Preset();
        for (Iterator iter = bsbInterface.iterator(); iter.hasNext(); ) {
            BSBObject bsbObj = (BSBObject) iter.next();
            String objectName = bsbObj.getObjectName();
            if (objectName == null || objectName.length() == 0) {
                continue;
            }
            String val = bsbObj.getPresetValue();
            if (val != null) {
                preset.valuesMap.put(objectName, val);
            }
        }
        return preset;
    }

    public void updatePresets(BSBGraphicInterface bsbInterface) {
        valuesMap.clear();
        for (Iterator iter = bsbInterface.iterator(); iter.hasNext(); ) {
            BSBObject bsbObj = (BSBObject) iter.next();
            String objectName = bsbObj.getObjectName();
            if (objectName == null || objectName.length() == 0) {
                continue;
            }
            String val = bsbObj.getPresetValue();
            if (val != null) {
                valuesMap.put(objectName, val);
            }
        }
    }

    public static Preset loadFromXML(Element data) {
        Preset p = new Preset();
        p.setPresetName(data.getAttributeValue("name"));
        String uniqueId = data.getAttributeValue("uniqueId");
        if (uniqueId != null && uniqueId.length() > 0) {
            p.uniqueId = uniqueId;
        }
        Elements nodes = data.getElements();
        while (nodes.hasMoreElements()) {
            Element node = nodes.next();
            if (node.getName().equals("setting")) {
                String name = node.getAttributeValue("name");
                String val = node.getTextString();
                p.addSetting(name, val);
            }
        }
        return p;
    }

    public Element saveAsXML() {
        Element retVal = new Element("preset");
        retVal.setAttribute("name", presetName);
        retVal.setAttribute("uniqueId", uniqueId);
        Object[] keys = valuesMap.keySet().toArray();
        Arrays.sort(keys);
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String val = (String) valuesMap.get(key);
            retVal.addElement("setting").setAttribute("name", key).setText(val);
        }
        return retVal;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void addSetting(String objectName, String objectVal) {
        valuesMap.put(objectName, objectVal);
    }

    public String getSetting(String objectName) {
        return (String) valuesMap.get(objectName);
    }

    /**
     * @return Returns the preset name.
     */
    public String getPresetName() {
        return presetName;
    }

    /**
     * @param name
     *            The preset name to set.
     */
    public void setPresetName(String name) {
        this.presetName = name;
    }

    public String toString() {
        return getPresetName();
    }

    public int compareTo(Object arg0) {
        Preset b = (Preset) arg0;
        return this.getPresetName().compareTo(b.getPresetName());
    }

    /**
     * @param graphicInterface
     */
    public void setInterfaceValues(BSBGraphicInterface graphicInterface) {
        for (Iterator iter = graphicInterface.iterator(); iter.hasNext(); ) {
            BSBObject bsbObj = (BSBObject) iter.next();
            String objName = bsbObj.getObjectName();
            String setting = getSetting(objName);
            if (setting != null) {
                bsbObj.setPresetValue(setting);
            }
        }
    }

    public void synchronizeWithInterface(BSBGraphicInterface graphicInterface) {
        HashMap nameMap = new HashMap();
        for (Iterator iter = graphicInterface.iterator(); iter.hasNext(); ) {
            BSBObject bsbObj = (BSBObject) iter.next();
            String objName = bsbObj.getObjectName();
            nameMap.put(objName, objName);
        }
        for (Iterator iter = this.valuesMap.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            if (!nameMap.containsKey(key)) {
                System.out.println("Removing preset with objectName: " + key);
                iter.remove();
            }
        }
        for (Iterator iter = graphicInterface.iterator(); iter.hasNext(); ) {
            BSBObject bsbObj = (BSBObject) iter.next();
            String objName = bsbObj.getObjectName();
            if (objName == null || objName.length() == 0) {
                continue;
            }
            if (!valuesMap.containsKey(objName)) {
                String val = bsbObj.getPresetValue();
                if (val != null) {
                    System.out.println("adding preset for objectName: " + objName);
                    valuesMap.put(objName, val);
                }
            }
        }
    }
}
