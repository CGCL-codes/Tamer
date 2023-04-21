package org.itsnat.impl.core.js.dom.event.domstd.msie;

import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public class JSMSIEMouseEventRenderImpl extends JSMSIEUIEventRenderImpl {

    public static final JSMSIEMouseEventRenderImpl SINGLETON = new JSMSIEMouseEventRenderImpl();

    /**
     * Creates a new instance of JSMSIEMouseEventRenderImpl
     */
    public JSMSIEMouseEventRenderImpl() {
    }

    public String getEventType() {
        return "MouseEvents";
    }

    public String getInitEvent(Event evt, String evtVarName, ClientAJAXDocumentImpl clientDoc) {
        MouseEvent mouseEvt = (MouseEvent) evt;
        StringBuffer code = new StringBuffer();
        code.append(evtVarName + ".screenX=" + mouseEvt.getScreenX() + ";\n");
        code.append(evtVarName + ".screenY=" + mouseEvt.getScreenY() + ";\n");
        code.append(evtVarName + ".clientX=" + mouseEvt.getClientX() + ";\n");
        code.append(evtVarName + ".clientY=" + mouseEvt.getClientY() + ";\n");
        code.append(evtVarName + ".ctrlKey=" + mouseEvt.getCtrlKey() + ";\n");
        code.append(evtVarName + ".shiftKey=" + mouseEvt.getShiftKey() + ";\n");
        code.append(evtVarName + ".altKey=" + mouseEvt.getAltKey() + ";\n");
        int button = 0;
        switch(mouseEvt.getButton()) {
            case 0:
                button = 1;
                break;
            case 2:
                button = 2;
                break;
            case 1:
                button = 4;
                break;
        }
        code.append(evtVarName + ".button=" + button + ";\n");
        return code.toString();
    }
}
