package net.pandoragames.far.ui.swing.component;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.apache.commons.logging.LogFactory;
import net.pandoragames.far.ui.swing.ComponentRepository;
import net.pandoragames.far.ui.swing.SwingConfig;

/**
 * A JPopupMenu that displays the Actions made available by an
 * {@link net.pandoragames.far.ui.swing.component.UndoHistory UndoHistory}.
 * The meneu will always show the items "Copy" and "Paste". It will show
 * the items "Undo" and "Redo" if the respective text component has been registered
 * for an undo history. It will show the items "Previous" and "Next" if the
 * text component has been registered for a snapshot history.
 * <p>
 * The text component must register with the UndoHistory <i>before</i> the creation of
 * the popup menu.
 *
 * @author Olivier Wehner at 28/11/2009
 * <!-- copyright note -->
 */
public class UndoHistoryPopupMenu extends JPopupMenu {

    private JTextComponent textComponent;

    /**
	 * Creates a popup menu that operates on the specified text component. 
	 * The text component must already been registerd with an 
	 * {@link net.pandoragames.far.ui.swing.component.UndoHistory UndoHistory}.
	 * @param config application configuration
	 * @param componentRepository shared components
	 * @param component previously registered with an <code>UndoHistory</code>
	 */
    public UndoHistoryPopupMenu(SwingConfig config, ComponentRepository componentRepository, JTextComponent component) {
        super();
        textComponent = component;
        init(config, componentRepository);
    }

    /**
	 * Creates a new UndoHistoryPopupMenu and wires it as component popup menu to the
	 * specified text component. The text component must already been registerd with an 
	 * {@link net.pandoragames.far.ui.swing.component.UndoHistory UndoHistory}.
	 * @param config application configuration
	 * @param componentRepository shared components
	 * @param component previously registered with an <code>UndoHistory</code>
	 * @return UndoHistoryPopupMenu the menu that has bee created.
	 */
    public static UndoHistoryPopupMenu createPopUpMenu(SwingConfig config, ComponentRepository componentRepository, JTextComponent component) {
        UndoHistoryPopupMenu popup = new UndoHistoryPopupMenu(config, componentRepository, component);
        component.setComponentPopupMenu(popup);
        return popup;
    }

    private void init(SwingConfig config, ComponentRepository componentRepository) {
        JMenuItem copy = new JMenuItem(config.getLocalizer().localize("label.copy"));
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String selection = textComponent.getSelectedText();
                if (selection != null) {
                    StringSelection textTransfer = new StringSelection(selection);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(textTransfer, null);
                }
            }
        });
        this.add(copy);
        JMenuItem paste = new JMenuItem(config.getLocalizer().localize("label.paste"));
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Transferable transfer = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                try {
                    if (transfer != null && transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String text = (String) transfer.getTransferData(DataFlavor.stringFlavor);
                        String selected = textComponent.getSelectedText();
                        if (selected == null) {
                            int insertAt = textComponent.getCaretPosition();
                            textComponent.getDocument().insertString(insertAt, text, null);
                        } else {
                            int start = textComponent.getSelectionStart();
                            int end = textComponent.getSelectionEnd();
                            textComponent.getDocument().remove(start, end - start);
                            textComponent.getDocument().insertString(start, text, null);
                        }
                    }
                } catch (UnsupportedFlavorException e) {
                    LogFactory.getLog(this.getClass()).error("UnsupportedFlavorException reading from clipboard", e);
                } catch (IOException iox) {
                    LogFactory.getLog(this.getClass()).error("IOException reading from clipboard", iox);
                } catch (BadLocationException blx) {
                    LogFactory.getLog(this.getClass()).error("BadLocationException reading from clipboard", blx);
                }
            }
        });
        this.add(paste);
        Action undoAction = textComponent.getActionMap().get(UndoHistory.ACTION_KEY_UNDO);
        if (undoAction != null) {
            undoAction.putValue(Action.NAME, config.getLocalizer().localize("label.undo"));
            this.add(undoAction);
        }
        Action redoAction = textComponent.getActionMap().get(UndoHistory.ACTION_KEY_REDO);
        if (redoAction != null) {
            redoAction.putValue(Action.NAME, config.getLocalizer().localize("label.redo"));
            this.add(redoAction);
        }
        Action prevAction = textComponent.getActionMap().get(UndoHistory.ACTION_KEY_PREVIOUS);
        if (prevAction != null) {
            prevAction.putValue(Action.NAME, config.getLocalizer().localize("label.previous"));
            this.add(prevAction);
        }
        Action nextAction = textComponent.getActionMap().get(UndoHistory.ACTION_KEY_NEXT);
        if (nextAction != null) {
            nextAction.putValue(Action.NAME, config.getLocalizer().localize("label.next"));
            this.add(nextAction);
        }
    }
}
