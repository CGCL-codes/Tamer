package org.fenggui.event;

import org.fenggui.event.key.KeyEvent;
import org.fenggui.event.mouse.MouseEvent;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ElementEvent<T> extends Event {

    private T element;

    private MouseEvent mouseEvent = null;

    private KeyEvent keyEvent = null;

    /**
   * 
   */
    public ElementEvent(T element, KeyEvent keyEvent) {
        this.element = element;
        this.keyEvent = keyEvent;
    }

    public ElementEvent(T element, MouseEvent mouseEvent) {
        this.element = element;
        this.mouseEvent = mouseEvent;
    }

    public T getElement() {
        return element;
    }

    public boolean isMouseEvent() {
        return mouseEvent != null;
    }

    public boolean isKeyEvent() {
        return keyEvent != null;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
