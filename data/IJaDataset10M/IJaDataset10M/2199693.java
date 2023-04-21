package ezsudoku.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Markback to last hypothesis step.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public class MarkStepAction extends AbstractAction implements PropertyChangeListener {

    /**
     */
    private CommandHistory history = null;

    /**
     * {@inheritDocs}
     */
    public void propertyChange(PropertyChangeEvent evt) {
        CommandHistory h = (CommandHistory) evt.getSource();
        if (!this.history.equals(h)) {
            System.err.println("Not monitored history");
            return;
        }
        boolean enabled = this.isEnabled();
        int step = h.lastStep();
        int current = h.currentIndex();
        if (enabled && step == current) {
            this.setEnabled(false);
        } else if (!enabled && step != current) {
            this.setEnabled(true);
        }
    }

    /**
     * {@inheritDocs}
     */
    public void actionPerformed(ActionEvent e) {
        if (!this.isEnabled()) {
            return;
        }
        this.history.addStep();
    }

    /**
     * @param history
     */
    public MarkStepAction(final CommandHistory history) {
        this.history = history;
        this.history.addPropertyChangeListener(this);
        this.setEnabled(false);
    }
}
