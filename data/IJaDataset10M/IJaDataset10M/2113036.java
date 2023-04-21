package de.wadndadn.simplekeys.key;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import de.wadndadn.midikeys.Note;

/**
 * TODO Document.
 * 
 * @author SchubertCh
 */
public final class BlackKey extends AbstractKey {

    /**
     * TODO Document.
     * 
     * @param note
     *            TODO Document
     */
    public BlackKey(final Note note) {
        super(note);
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.simplekeys.key.IKeyboardPart#getSize()
     */
    @Override
    public int getSize() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.simplekeys.key.AbstractKey#keyForeground(Display)
     */
    @Override
    protected Color keyForeground(final Display display) {
        return display.getSystemColor(SWT.COLOR_WHITE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.simplekeys.key.AbstractKey#keyBackground(Display)
     */
    @Override
    protected Color keyBackground(final Display display) {
        return display.getSystemColor(SWT.COLOR_BLACK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.simplekeys.key.AbstractKey#keyBackgroundOn(Display)
     */
    @Override
    protected Color keyBackgroundOn(final Display display) {
        return display.getSystemColor(SWT.COLOR_DARK_GRAY);
    }
}
