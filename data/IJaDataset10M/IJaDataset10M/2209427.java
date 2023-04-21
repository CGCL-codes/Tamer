package uk.ac.kingston.aqurate.author_UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JToggleButton;
import javax.swing.text.html.HTMLEditorKit;

public class ToggleActionChangeListener implements PropertyChangeListener {

    private JToggleButton jToggleButton = null;

    public ToggleActionChangeListener(JToggleButton jToggleButton) {
        super();
        this.jToggleButton = jToggleButton;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        String propertyName = arg0.getPropertyName();
        if (arg0.getPropertyName().equals(HTMLEditorKit.BOLD_ACTION)) {
            Object source = arg0.getSource();
        }
    }
}
