package org.mbari.vars.annotation.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.mbari.swing.JFancyButton;
import org.mbari.swing.SwingUtils;
import org.mbari.vars.annotation.ui.actions.NewObservationAction;
import org.mbari.vars.annotation.ui.dispatchers.PredefinedDispatcher;

/**
 * <p>Create a new observation using the current time-code from the VCR</p>
 *
 * @author  <a href="http://www.mbari.org">MBARI</a>
 * @version  $Id: NewObservationButton.java 314 2006-07-10 02:38:46Z hohonuuli $
 */
public class NewObservationButton extends JFancyButton {

    /**
     *
     */
    private static final long serialVersionUID = 53820555698832688L;

    /**
     * Constructor for the NewObservationButton object
     */
    public NewObservationButton() {
        super();
        setAction(new NewObservationAction());
        setToolTipText("Create an Observation using the selected timecode [" + SwingUtils.getKeyString((KeyStroke) getAction().getValue(Action.ACCELERATOR_KEY)) + "]");
        setIcon(new ImageIcon(getClass().getResource("/images/vars/annotation/obs_copytc.png")));
        setEnabled(false);
        setText("");
        PredefinedDispatcher.OBSERVATION.getDispatcher().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                final String p = (String) PredefinedDispatcher.PERSON.getDispatcher().getValueObject();
                setEnabled((p != null) && (evt.getNewValue() != null));
            }
        });
    }
}
