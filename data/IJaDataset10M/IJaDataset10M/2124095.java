package ca.sqlpower.architect.swingui.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import ca.sqlpower.architect.swingui.ArchitectFrame;
import ca.sqlpower.architect.swingui.PlayPen;

public class PasteSelectedAction extends AbstractArchitectAction {

    private static final Logger logger = Logger.getLogger(PasteSelectedAction.class);

    public PasteSelectedAction(final ArchitectFrame frame) {
        super(frame, Messages.getString("PasteSelectedAction.name"), Messages.getString("PasteSelectedAction.description"));
        putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e) {
        PlayPen playPen = getSession().getPlayPen();
        final Component focusOwner = getSession().getArchitectFrame().getFocusOwner();
        if (playPen.isAncestorOf(focusOwner) || playPen == focusOwner) {
            Transferable clipboardContents = getSession().getContext().getClipboardContents();
            logger.debug("Pasting " + clipboardContents + " into the playpen.");
            if (clipboardContents != null) {
                playPen.pasteData(clipboardContents);
            } else {
                JOptionPane.showMessageDialog(getSession().getArchitectFrame(), "There is no contents in the clipboard to paste.", "Clipboard empty", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
