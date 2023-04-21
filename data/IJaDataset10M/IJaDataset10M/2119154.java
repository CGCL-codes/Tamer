package com.objetdirect.gwt.umlapi.client.controls;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.gfx.GfxObject;
import com.objetdirect.gwt.umlapi.client.gfx.GfxObjectListener;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * Concrete listener to add on the canvas widget. It allows to interact directly with the canvas by simulating mouse
 * events, for example to use in the animated demo.
 * 
 * @author Rapahel Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class CanvasListener implements GfxObjectListener {

    private final UMLCanvas canvas;

    public CanvasListener(UMLCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseDoubleClicked(final GfxObject graphicObject, final Event event) {
        if (canvas.isMouseEnabled()) {
            doubleClick(graphicObject, new Point(event.getClientX(), event.getClientY()));
        }
    }

    /**
	 * This method represent a double click with the mouse. <br />
	 * It's automatically called on double click but can also be called manually for testing purpose
	 * 
	 * @param gfxObject
	 *            The object on which this event has occurred
	 * @param location
	 *            The location of the event
	 */
    public void doubleClick(final GfxObject gfxObject, final Point location) {
        Log.trace("Mouse double clicked on " + gfxObject + " at " + location);
        canvas.mouseDoubleClicked(gfxObject);
    }

    @Override
    public void mouseMoved(final Event event) {
        if (canvas.isMouseEnabled()) {
            move(new Point(event.getClientX(), event.getClientY()), event.getButton(), event.getCtrlKey(), event.getShiftKey());
        }
    }

    /**
	 * This method represent a movement with the mouse. <br />
	 * It's automatically called on mouse move but can also be called manually for testing purpose
	 * 
	 * @param location
	 *            The location of the event
	 * @param triggerButton
	 *            A number representing which button has triggered the event
	 * @param isCtrlDown
	 *            True if ctrl key was down during the event
	 * @param isShiftDown
	 *            True if shift key was down during the event
	 */
    public void move(final Point location, int triggerButton, boolean isCtrlDown, boolean isShiftDown) {
        Log.trace("Mouse moved to " + location + " with button " + triggerButton + " ctrl " + isCtrlDown + " shift " + isShiftDown);
        canvas.mouseMoved(isCtrlDown, isShiftDown);
    }

    @Override
    public void mousePressed(final GfxObject graphicObject, final Event event) {
        if (canvas.isMouseEnabled()) {
            press(graphicObject, new Point(event.getClientX(), event.getClientY()), event.getButton(), event.getCtrlKey(), event.getShiftKey());
        }
    }

    /**
	 * This method represent a mouse press with the mouse. <br />
	 * It's automatically called on mouse press but can also be called manually for testing purpose
	 * 
	 * @param gfxObject
	 *            The object on which this event has occurred
	 * @param location
	 *            The location of the event
	 * @param triggerButton
	 *            A number representing which button has triggered the event
	 * @param isCtrlDown
	 *            True if ctrl key was down during the event
	 * @param isShiftDown
	 *            True if shift key was down during the event
	 */
    public void press(final GfxObject gfxObject, final Point location, int triggerButton, boolean isCtrlDown, boolean isShiftDown) {
        Log.trace("Mouse pressed on " + gfxObject + " at " + location + " with button " + triggerButton + " ctrl " + isCtrlDown + " alt " + isShiftDown);
        if (triggerButton == NativeEvent.BUTTON_LEFT) {
            canvas.mouseLeftPressed(gfxObject, isCtrlDown, isShiftDown);
        } else if (triggerButton == NativeEvent.BUTTON_RIGHT) {
            canvas.mouseRightPressed(gfxObject, location);
        }
    }

    @Override
    public void mouseReleased(final GfxObject graphicObject, final Event event) {
        if (canvas.isMouseEnabled()) {
            release(graphicObject, new Point(event.getClientX(), event.getClientY()), event.getButton(), event.getCtrlKey(), event.getShiftKey());
        }
    }

    /**
	 * This method represent a mouse release with the mouse. <br />
	 * It's automatically called on release but can also be called manually for testing purpose
	 * 
	 * @param gfxObject
	 *            The object on which this event has occurred
	 * @param location
	 *            The location of the event
	 * @param triggerButton
	 *            A number representing which button has triggered the event
	 * @param isCtrlDown
	 *            True if ctrl key was down during the event
	 * @param isShiftDown
	 *            True if shift key was down during the event
	 */
    public void release(final GfxObject gfxObject, final Point location, int triggerButton, boolean isCtrlDown, boolean isShiftDown) {
        Log.trace("Mouse released on " + gfxObject + " at " + location + " with button " + triggerButton + " ctrl " + isCtrlDown + " shift " + isShiftDown);
        if (triggerButton == NativeEvent.BUTTON_LEFT) {
            canvas.mouseReleased(gfxObject, isCtrlDown, isShiftDown);
        }
    }
}
