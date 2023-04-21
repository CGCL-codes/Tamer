package com.lake.pim.proxy;

import java.util.concurrent.Exchanger;
import com.lake.pim.api.GUI_Settings;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;

public class GUISettingsListener implements ICallbackListener {

    private Exchanger<GUI_Settings> exchange;

    public GUISettingsListener(Exchanger<GUI_Settings> exchange) {
        this.exchange = exchange;
    }

    @Override
    public void handleCallbackString(String returnObjectAsXML) {
        GUI_Settings guiSettings = (GUI_Settings) XMLConverter.xmlToObject(returnObjectAsXML, GUI_Settings.class);
        try {
            exchange.exchange(guiSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCallbackObject(Object returnObject) {
        System.out.println("GUISettingsListener:handleCallbackObject");
        GUI_Settings guiSettings = (GUI_Settings) returnObject;
        try {
            exchange.exchange(guiSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleErrorMessage(String errorMessage) {
        try {
            exchange.exchange(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
