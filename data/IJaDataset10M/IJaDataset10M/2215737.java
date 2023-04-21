package org.apache.xerces.dom.events;

import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

/**
 * An implementation of the DOM Level 2 <code>UIEvent</code> interface.
 * 
 * @xerces.internal 
 * 
 * @version $Id: UIEventImpl.java 533574 2007-04-30 00:29:47Z mrglavas $
 */
public class UIEventImpl extends EventImpl implements UIEvent {

    private AbstractView fView;

    private int fDetail;

    public AbstractView getView() {
        return fView;
    }

    public int getDetail() {
        return fDetail;
    }

    public void initUIEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg) {
        fView = viewArg;
        fDetail = detailArg;
        super.initEvent(typeArg, canBubbleArg, cancelableArg);
    }
}
