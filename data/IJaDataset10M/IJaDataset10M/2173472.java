package javab.bling;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

/**
 * A label that is intended to signify the current 
 * status of something. An example of this would be the 
 * status bar for a web browser which indicates the 
 * current status and may have "special" statuses such as
 * hover text which, once finished, should cause the previous 
 * status to re-appear. 
 * 
 * @author Brett Geren
 *
 */
public class BStatusBarLabel extends JLabel {

    public String previousStatus;

    public boolean isStatus;

    public BStatusBarLabel(String text) {
        isStatus = true;
        setStatus(text);
        setForeground(new JMenuItem().getForeground());
    }

    /**
	 * Sets special non-status text to display. 
	 * 
	 * if text.equals(getText()) this call does nothing 
	 * since this call would just be wasteful
	 * 
	 * PRE: !text.equals(getText())
	 * POST: !isStatus && previousStatus = most recent status && getText() == text
	 */
    public void startSpecialText(String text) {
        text = (text == null || text.isEmpty()) ? "''" : text;
        if (getText().equals(text)) {
            return;
        } else {
            previousStatus = (isStatus) ? getText() : previousStatus;
            setText(text);
            isStatus = false;
        }
    }

    /**
	 * Stops the special text and reverts the text 
	 * back to the previousStatus. 
	 * 
	 * PRE: !isStatus
	 * POST: isStatus && previousStatus = null && text = old status
	 */
    public void endSpecialText() {
        if (!isStatus) {
            setText(previousStatus);
            previousStatus = null;
            isStatus = true;
        }
    }

    public void setStatus(String status) {
        if (getText().equals(status)) return;
        if (isStatus) {
            setText(status);
        } else {
            previousStatus = status;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(getMaximumSize().width, super.getPreferredSize().height);
    }

    public Dimension getMaximumSize() {
        if (getParent() != null) {
            return new Dimension(getParent().getWidth() / 2, super.getMaximumSize().height);
        } else {
            return new Dimension(Integer.MAX_VALUE, super.getMaximumSize().height);
        }
    }
}
