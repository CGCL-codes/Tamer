package eu.ict.persist.examples.OtherHelloWorld.gui;

import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;

public class GoodbyeCallbackListener implements ICallbackListener {

    OtherHelloServiceGUI gui;

    public GoodbyeCallbackListener(OtherHelloServiceGUI g) {
        gui = g;
    }

    @Override
    public void handleCallbackString(String returnObjectAsXML) {
        String message = (String) XMLConverter.xmlToObject(returnObjectAsXML, String.class);
        handleCallbackObject(message);
    }

    @Override
    public void handleCallbackObject(Object returnObject) {
        String message = (String) returnObject;
        gui.displayHello(message);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        gui.leave();
    }

    @Override
    public void handleErrorMessage(String errorMessage) {
        gui.displayError(errorMessage);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        gui.leave();
    }
}
