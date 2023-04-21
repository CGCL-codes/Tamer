package org.jsynthlib.jsynthlib.xml;

import java.util.prefs.Preferences;
import core.Device;

public class XMLDevice extends Device {

    /**
     * @param manufacturerName
     * @param modelName
     * @param inquiryID
     * @param infoText
     * @param authors
     */
    public XMLDevice(String manufacturerName, String modelName, String inquiryID, String infoText, String authors) {
        super(manufacturerName, modelName, inquiryID, infoText, authors);
    }

    public void setPreferences(Preferences p) {
        prefs = p;
    }

    public String getSynthName() {
        return prefs.get("synthName", getModelName() + " (XML)");
    }
}
