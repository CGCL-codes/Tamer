package fairVote.panel;

import javax.swing.Icon;
import javax.swing.JLabel;

;

/**
 * Class meant to replace AccessibleLabel, for accessibiliy issues.
 *   ( JAWS doesn't read labels not focusable )
 * */
public class AccessibleLabel extends JLabel {

    public AccessibleLabel() {
        super();
        this.setFocusable(true);
    }

    public AccessibleLabel(String text) {
        super(text);
        this.setFocusable(true);
    }

    public AccessibleLabel(Icon image) {
        super(image);
        this.setFocusable(true);
    }

    public AccessibleLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        this.setFocusable(true);
    }

    public AccessibleLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        this.setFocusable(true);
    }

    public AccessibleLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        this.setFocusable(true);
    }

    public void setColors(String fieldPrefix) {
        StylePanel.setJComponent(this, fieldPrefix);
    }
}
