package de.wadndadn.midiclipse.ui.console;

import org.eclipse.jface.action.Action;
import de.wadndadn.midiclipse.ui.Messages;

/**
 * Action that removed the MIDIclipse console from the console view.
 * 
 * @author SchubertCh
 */
public final class ConsoleRemoveAction extends Action {

    /**
     * Default constructor.
     */
    public ConsoleRemoveAction() {
        setText(Messages.getMessages().bind(ConsoleConstants.LABEL_REMOVE_ACTION));
        setToolTipText(Messages.getMessages().bind(ConsoleConstants.TOOLTIP_REMOVE_ACTION));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        MidiclipseConsoleFactory.closeConsole();
    }
}
