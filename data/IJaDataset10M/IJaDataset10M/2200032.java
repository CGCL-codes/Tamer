package dot.chimera;

import java.util.Iterator;
import javax.swing.*;
import java.awt.event.*;
import dot.exceptions.ProgrammingErrorException;

/**
 * A utility <code>MouseListener</code> which can be used to trigger something,
 * such as displaying a pop-up menu, in response to a particular mouse event,
 * in particular clicking mouse button 2 or 3, or clicking and holding any
 * button for a length of time.  Any place that may display a popup menu should
 * use this to preserve a consistant look and feel, and also to properly 
 * support single button mice.
 * <p>
 * Perhaps this should implement KeyListener or something like that... that
 * way a certain key sequence could trigger a popup??
 * <p>
 * NOTE: netbeans has an interesting idea, using the mousePressed and
 * mouseReleased events to determine when to display the pop-up; they
 * claim it is more reliable that using mouseClicked.  If there are
 * problems with this, have a look at how they do it in the utility
 * class org.openide.awt.MouseUtils.PopupMouseAdapter.
 * 
 * @author Rob Clark
 * @version 0.0
 */
public class PopupTrigger extends MouseAdapter implements MouseMotionListener {

    private MouseDownTimer mouseDownTimer = null;

    private PopupListener l;

    /**
   * For the "click-and-hold" trigger, this is the threshold used to determine
   * how much movement is permissible before the "click-and-hold" becomes a
   * "click-and-drag".
   */
    private static final int CLICK_AND_HOLD_DISTANCE = 5;

    /**
   * This interface should be implemented by the user of this utility mouse
   * listener.
   */
    public interface PopupListener {

        /**
     * This method is called when it is determined that a popup menu should
     * be displayed.  It is called with the {@link MouseEvent} that triggers
     * the popup to be shown, and it's x and y coordinates can be used as the 
     * position to display the popup.
     * 
     * @param evt          the event triggering the popup
     */
        public void showPopup(MouseEvent evt);
    }

    /**
   * Class Constructor.
   * 
   * @param l            the listener to call when popup trigger has occured
   */
    public PopupTrigger(PopupListener l) {
        this.l = l;
    }

    public synchronized void mouseClicked(MouseEvent evt) {
        int mod = evt.getModifiers();
        if (((mod & InputEvent.BUTTON2_MASK) != 0) || ((mod & InputEvent.BUTTON3_MASK) != 0)) {
            l.showPopup(evt);
        }
        cancelTimer();
    }

    public synchronized void mousePressed(MouseEvent evt) {
        if (mouseDownTimer == null) {
            mouseDownTimer = new MouseDownTimer(evt);
            mouseDownTimer.start();
        }
    }

    public synchronized void mouseReleased(MouseEvent evt) {
        cancelTimer();
    }

    public void mouseMoved(MouseEvent evt) {
        if (mouseDownTimer != null) {
            int dx = evt.getX() - mouseDownTimer.evt.getX();
            int dy = evt.getY() - mouseDownTimer.evt.getY();
            if (((dx * dx) + (dy * dy)) > (CLICK_AND_HOLD_DISTANCE * CLICK_AND_HOLD_DISTANCE)) cancelTimer();
            evt.consume();
        }
    }

    public void mouseDragged(MouseEvent evt) {
        mouseMoved(evt);
    }

    private void cancelTimer() {
        if (mouseDownTimer != null) {
            mouseDownTimer.stop();
            mouseDownTimer = null;
        }
    }

    private class MouseDownTimer extends Timer {

        MouseEvent evt;

        MouseDownTimer(MouseEvent evt) {
            super(500, null);
            this.evt = evt;
            addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    stop();
                    l.showPopup(MouseDownTimer.this.evt);
                }
            });
        }
    }
}
