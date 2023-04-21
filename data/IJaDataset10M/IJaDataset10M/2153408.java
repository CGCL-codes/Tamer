package com.frinika.sequencer.gui.menu.midi;

import static com.frinika.localization.CurrentLocale.getMessage;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.frinika.project.gui.ProjectFrame;
import com.frinika.sequencer.midi.groovepattern.gui.GroovePatternManagerDialog;

/**
 * Opens the manager dialog for groove patterns.
 * 
 * @author Jens Gulden
 */
public class GroovePatternManagerAction extends AbstractAction {

    protected ProjectFrame frame;

    public GroovePatternManagerAction(ProjectFrame frame) {
        super(getMessage("sequencer.midi.groovepattern.manager"));
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        GroovePatternManagerDialog.showDialog(frame);
    }
}
