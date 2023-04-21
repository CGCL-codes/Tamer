package com.sun.perseus.model;

import com.sun.perseus.util.SVGConstants;
import org.w3c.dom.events.Event;

/**
 * A <code>AccessKeyCondition</code> generates a <code>TimeInstance</code> 
 * everytime the associated key event occurs with the expected 
 * <code>accessKey</code> value.
 *
 * @version $Id: AccessKeyCondition.java,v 1.2 2006/04/21 06:36:24 st125089 Exp $
 */
public final class AccessKeyCondition extends EventBaseCondition {

    /**
     * The key code which triggers the condition.
     */
    char accessKey;

    /**
     * @param timedElement the associated <code>TimedElementSupport</code>. 
     *        Should not be null.
     * @param isBegin defines whether this condition is for a begin list.
     * @param offset offset from the sync base. This means that time instances
     *        synchronized on the syncBase begin or end time are offset by 
     *        this amount.
     * @param accessKey only when the eventBase generates a key event with
     *        this accessKey will a <code>TimeInstance</code> be generated. 
     */
    public AccessKeyCondition(final TimedElementSupport timedElement, final boolean isBegin, final long offset, final char accessKey) {
        super(timedElement, isBegin, null, timedElement.animationElement.ownerDocument, SVGConstants.SVG_KEYDOWN_EVENT_TYPE, offset);
        this.accessKey = accessKey;
    }

    /**
     * Implementation of the <code>EventListener</code> interface.
     * This is a simple filtered version of the handleEvent implementation
     * in <code>EventBaseCondition</code>.
     *
     * @param evt the event that occured
     */
    public void handleEvent(final Event evt) {
        if (((ModelEvent) evt).keyChar == accessKey) {
            super.handleEvent(evt);
        }
    }

    /**
     * Converts this <code>AccessKeyCondition</code> to a String trait.
     *
     * @return a string describing this <code>TimeCondition</code>
     */
    protected String toStringTrait() {
        StringBuffer sb = new StringBuffer();
        sb.append("accessKey(");
        sb.append(accessKey);
        sb.append(')');
        if (offset != 0) {
            if (offset > 0) {
                sb.append('+');
            }
            sb.append(offset / 1000f);
            sb.append('s');
        }
        return sb.toString();
    }
}
