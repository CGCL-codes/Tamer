package org.jnativehook.mouse;

import java.util.EventListener;
import org.jnativehook.GlobalScreen;

/**
 * The listener interface for receiving native mouse motion events. 
 * (For clicks and other mouse events, use the <code>NativeMouseListener</code>.)
 * <p>
 * The class that is interested in processing a <code>NativeMouseMotionEvent</code> 
 * implements this interface, and the object created with that class is 
 * registered with the <code>GlobalScreen</code> using the 
 * {@link GlobalScreen#addNativeMouseMotionListener} method. When the 
 * NativeMouseMotion event occurs, that object's appropriate method is invoked.
 *
 * @author	Alexander Barker (<a href="mailto:alex@1stleg.com">alex@1stleg.com</a>)
 * @since	1.0
 * 
 * @see NativeMouseEvent
 */
public interface NativeMouseMotionListener extends EventListener {

    /**
	 * Invoked when the mouse has been moved.
	 *
	 * @param e The native mouse event.
	 */
    public void mouseMoved(NativeMouseEvent e);

    /**
	 * Invoked when the mouse has been moved while a button mask is present.
	 *
	 * @param e the native mouse event
	 * @since 1.1
	 */
    public void mouseDragged(NativeMouseEvent e);
}
