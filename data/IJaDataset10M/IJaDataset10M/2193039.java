package jpicedt.graphic.event;

import java.util.EventListener;
import javax.swing.event.*;
import jpicedt.graphic.model.*;
import jpicedt.graphic.toolkit.*;
import jpicedt.graphic.view.*;
import jpicedt.graphic.*;

/**
 * A class that allows an EditorKit's mousetool to send high-level mouse-events, i.e. somehow pre-processed
 * by the EditorKit machinery. This is inspired from 
 * {@link javax.swing.event.HyperlinkListener javax.swing.event.HyperlinkListener}.<br>
 * Application cover :
 * <ul>
 * <li> UI implementation outside the jpicedt.graphic package can react to mouse-event w/o the burden
 *      of processing the event.
 * <li> Internal use by other EditorKit's mousetools.
 * <p> [pending] underway ; compile ok but not used yet.
 * @author Sylvain Reynal
 * @version $Id: EditorKitEvent.java,v 1.6 2011/07/23 05:22:58 vincentb1 Exp $
 * @since jpicedt 1.3.2
 */
public class EditorKitEvent {

    private EventType type;

    private EditorKit source;

    private HitInfo hitInfo;

    /**
	 * contruct a new EditorKitEvent.
	 * @param source the editorkit that sourced the event
	 * @param type the event type
	 * @param hitInfo the HitInfo associated with the mouse event
	 */
    public EditorKitEvent(EditorKit source, EventType type, HitInfo hitInfo) {
        this.source = source;
        this.type = type;
        this.hitInfo = hitInfo;
    }

    /**
 	 * Return the editor kit that sourced this event 
 	 */
    public EditorKit getSource() {
        return source;
    }

    /**
	 * Return the type of this event
	 */
    public EventType getType() {
        return type;
    }

    /**
	 * Return the HitInfo associated with the mouse-event
	 */
    public HitInfo getHitInfo() {
        return hitInfo;
    }

    /**
	 * Return a String representation of this event for debugging purpose.
	 */
    public String toString() {
        return "EditorKitEvent@" + Integer.toHexString(hashCode()) + ", type=" + type + ", value=" + hitInfo + ", source=" + source;
    }

    /**
	 * typesafe enumeration of allowed event types
	 */
    public static class EventType {

        private String name;

        private EventType(String s) {
            name = s;
        }

        /** 
		 * signal that the mouse-cursor entered the sensitive area of a graphic element
		 * [SR:pending] change name to ON_MOUVE_OVER to conform to W3C's DOM for XML-SVG ?
		 */
        public static final EventType ELEMENT_ENTERED = new EventType("element-entered");

        /** 
		 * signals that the mouse-cursor exited the sensitive area of a graphic element
		 */
        public static final EventType ELEMENT_EXITED = new EventType("element-exited");

        /** 
		 * signals that the user clicked on a graphic element
		 * [SR:pending] change name to ON_MOUSE_CLICK ?
		 */
        public static final EventType ELEMENT_CLICKED = new EventType("element-clicked");

        /** 
		 * Return the name of this event type ; this can be used by a GUI, but it's in english, so
		 *  it needs to be localized.
		 */
        public String toString() {
            return name;
        }
    }
}
