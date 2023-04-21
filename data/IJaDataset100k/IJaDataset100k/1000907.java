package org.apache.batik.dom.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 * The abstract <code>Event</code> root class.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractEvent.java,v 1.1 2005/11/21 09:51:34 dev Exp $
 */
public abstract class AbstractEvent implements Event {

    /**
     * The event type.
     */
    protected String type;

    /**
     * Whether this event is bubbling.
     */
    protected boolean isBubbling;

    /**
     * Whether this event is cancelable.
     */
    protected boolean cancelable;

    /**
     * The EventTarget whose EventListeners are currently being processed.
     */
    protected EventTarget currentTarget;

    /**
     * The target of this event.
     */
    protected EventTarget target;

    /**
     * The event phase.
     */
    protected short eventPhase;

    /**
     * The time the event was created.
     */
    protected long timeStamp = System.currentTimeMillis();

    /**
     * Whether the event propagation must be stopped.
     */
    protected boolean stopPropagation = false;

    /**
     * Whether the default action must be processed.
     */
    protected boolean preventDefault = false;

    /**
     * DOM: The <code>type</code> property represents the event name
     * as a string property. The string must be an XML name.  
     */
    public String getType() {
        return type;
    }

    /**
     * DOM: The <code>target</code> property indicates the
     * <code>EventTarget</code> whose <code>EventListeners</code> are
     * currently being processed.
     */
    public EventTarget getCurrentTarget() {
        return currentTarget;
    }

    /**
     * DOM: The <code>target</code> property indicates the
     * <code>EventTarget</code> to which the event was originally
     * dispatched.  
     */
    public EventTarget getTarget() {
        return target;
    }

    /**
     * DOM: The <code>eventPhase</code> property indicates which phase
     * of event flow is currently being evaluated.  
     */
    public short getEventPhase() {
        return eventPhase;
    }

    /**
     * DOM: The <code>bubbles</code> property indicates whether or not
     * an event is a bubbling event.  If the event can bubble the
     * value is true, else the value is false. 
     */
    public boolean getBubbles() {
        return isBubbling;
    }

    /**
     * DOM: The <code>cancelable</code> property indicates whether or
     * not an event can have its default action prevented.  If the
     * default action can be prevented the value is true, else the
     * value is false. 
     */
    public boolean getCancelable() {
        return cancelable;
    }

    /**
     * DOM: Used to specify the time (in milliseconds relative to the
     * epoch) at 
     * which the event was created. Due to the fact that some systems may not 
     * provide this information the value of <code>timeStamp</code> may be 
     * returned. Examples of epoch time are the time of the system start or 
     * 0:0:0 UTC 1st January 1970. 
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * DOM: The <code>stopPropagation</code> method is used prevent
     * further propagation of an event during event flow. If this
     * method is called by any <code>EventListener</code> the event
     * will cease propagating through the tree.  The event will
     * complete dispatch to all listeners on the current
     * <code>EventTarget</code> before event flow stops.  This method
     * may be used during any stage of event flow.  
     */
    public void stopPropagation() {
        this.stopPropagation = true;
    }

    /**
     * DOM: If an event is cancelable, the <code>preventDefault</code>
     * method is used to signify that the event is to be canceled,
     * meaning any default action normally taken by the implementation
     * as a result of the event will not occur.  If, during any stage
     * of event flow, the <code>preventDefault</code> method is called
     * the event is canceled.  Any default action associated with the
     * event will not occur.  Calling this method for a non-cancelable
     * event has no effect.  Once <code>preventDefault</code> has been
     * called it will remain in effect throughout the remainder of the
     * event's propagation.  This method may be used during any stage
     * of event flow.  
     */
    public void preventDefault() {
        this.preventDefault = true;
    }

    /**
     * DOM: The <code>initEvent</code> method is used to initialize the
     * value of interface.  This method may only be called before the 
     * <code>Event</code> has been dispatched via the 
     * <code>dispatchEvent</code> method, though it may be called multiple 
     * times during that phase if necessary.  If called multiple times the 
     * final invocation takes precedence.  If called from a subclass of 
     * <code>Event</code> interface only the values specified in the 
     * <code>initEvent</code> method are modified, all other attributes are 
     * left unchanged.
     * @param eventTypeArg  Specifies the event type.  This type may be any 
     *   event type currently defined in this specification or a new event 
     *   type.. The string must be an  XML name .  Any new event type must 
     *   not begin with any upper, lower, or mixed case version  of the 
     *   string "DOM".  This prefix is reserved for future DOM event sets.
     * @param canBubbleArg  Specifies whether or not the event can bubble.
     * @param cancelableArg  Specifies whether or not the event's default  
     *   action can be prevented.
     */
    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
        this.type = eventTypeArg;
        this.isBubbling = canBubbleArg;
        this.cancelable = cancelableArg;
    }

    boolean getPreventDefault() {
        return preventDefault;
    }

    boolean getStopPropagation() {
        return stopPropagation;
    }

    void setEventPhase(short eventPhase) {
        this.eventPhase = eventPhase;
    }

    void stopPropagation(boolean state) {
        this.stopPropagation = state;
    }

    void preventDefault(boolean state) {
        this.preventDefault = state;
    }

    void setCurrentTarget(EventTarget currentTarget) {
        this.currentTarget = currentTarget;
    }

    void setTarget(EventTarget target) {
        this.target = target;
    }

    public static boolean getEventPreventDefault(Event evt) {
        AbstractEvent ae = (AbstractEvent) evt;
        return ae.getPreventDefault();
    }
}
