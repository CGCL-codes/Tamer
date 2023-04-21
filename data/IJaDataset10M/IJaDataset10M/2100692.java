package com.elibera.plugins.examples;

import com.elibera.m.plugin.Event;
import com.elibera.m.plugin.FactoryForEventPhase;
import com.elibera.m.plugin.internal.HTTPResponse;
import com.elibera.m.plugin.internal.InfoEvent;
import com.elibera.m.plugin.internal.InputDisplayElement;
import com.elibera.m.plugin.internal.PostRequestEvent;
import com.elibera.m.utils.ActionListener;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.utils.HelperStd;

/**
 * @author meisi
 *
 */
public class EventUpload implements Event, ActionListener {

    private String search = "";

    int formID = -1;

    public InfoEvent infoEvent;

    public EventUpload(String _search, int _formId) {
        search = _search;
        formID = _formId;
    }

    public EventUpload(byte[] data) {
        byte[][] b = HelperConverter.deSerialiseByteArray(data);
        formID = HelperConverter.getIntFromByte(b[0]);
        search = new String(b[1]);
    }

    public byte[] getBytes() {
        byte[][] b = { HelperConverter.getByte(this.formID), search.getBytes() };
        return HelperConverter.serialiseByteArray(b);
    }

    public void doEvent(InfoEvent infoEvent) {
        this.infoEvent = infoEvent;
        try {
            PostRequestEvent post = FactoryForEventPhase.postHTTPPlatformRessource("xml", "testing/uploadresult.php", "server", this, infoEvent, false, null);
            String[] input_elements = HelperStd.split(search, ',');
            for (int i = 0; i < input_elements.length; i++) {
                String value = FactoryForEventPhase.getValueOfInputElement(input_elements[i], this.formID, infoEvent);
                if (value != null) post.addString(input_elements[i], value);
            }
            post.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorOccured(int arg0, String error, int arg2) {
        FactoryForEventPhase.displayPopUpWindow("Could not load search page:" + error, infoEvent);
    }

    public void doAction(int arg0, Object result) {
        FactoryForEventPhase.displayPopUpWindow(new String(((HTTPResponse) result).getByte()), infoEvent);
    }

    public int getClassID() {
        return HelperEliberaPlugin.EVENT_EXAMPLES_UPLOAD_CLASS_ID;
    }

    public String getPluginID() {
        return HelperEliberaPlugin.PLUGIN_ID;
    }

    public void init(InfoEvent arg0) {
    }

    public void closeEvent(InfoEvent arg0) {
    }
}
