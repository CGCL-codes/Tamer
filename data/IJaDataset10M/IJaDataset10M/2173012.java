package org.itsnat.impl.core.event.fromclient.domstd.msie;

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.domstd.DOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.request.RequestNormalEventImpl;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class MSIEKeyEventImpl extends MSIEUIEventImpl implements ItsNatKeyEvent {

    /**
     * Creates a new instance of MSIEKeyEventImpl
     */
    public MSIEKeyEventImpl(DOMStdEventListenerWrapperImpl listenerWrapper, RequestNormalEventImpl request) {
        super(listenerWrapper, request);
    }

    public void initKeyEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, int keyCodeArg, int charCodeArg) {
        throw new ItsNatException("Not implemented", this);
    }

    public boolean getAltKey() {
        return originalEvt.getAltKey();
    }

    public int getCharCode() {
        String type = getType();
        if (type.equals("keypress")) return originalEvt.getKeyCode(); else return 0;
    }

    public boolean getCtrlKey() {
        return originalEvt.getCtrlKey();
    }

    public int getKeyCode() {
        String type = getType();
        if (type.equals("keypress")) return 0; else return originalEvt.getKeyCode();
    }

    public boolean getMetaKey() {
        return false;
    }

    public boolean getShiftKey() {
        return originalEvt.getShiftKey();
    }
}
