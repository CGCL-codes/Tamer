package org.xngr.browser.editor;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.xml.sax.SAXParseException;

/**
 * Validates the editors text and saves it to disk.
 *
 * @version	$Revision: 1.8 $, $Date: 2007/06/28 13:14:10 $
 * @author Edwin Dankert <edankert@cladonia.com>
 */
public class SaveAction extends AbstractAction {

    private static final long serialVersionUID = 6832499738508012003L;

    private static final boolean DEBUG = false;

    private Editor editor = null;

    /**
	 * The constructor for the action which saves the 
	 * editors text to disk.
	 *
	 * @param editor the editor pane.
	 */
    public SaveAction(Editor editor) {
        super("Save");
        putValue(MNEMONIC_KEY, new Integer('S'));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, false));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/org/xngr/browser/icons/Save16.gif")));
        putValue(SHORT_DESCRIPTION, "Save");
        this.editor = editor;
    }

    /**
	 * The implementation of the save action, called 
	 * after a user action.
	 *
	 * @param event the action event.
	 */
    public synchronized void actionPerformed(ActionEvent event) {
        if (DEBUG) System.out.println("SaveAction.actionPerformed( " + event + ")");
        Runnable runner = new Runnable() {

            public void run() {
                Exception exception = null;
                try {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            editor.setWait(true);
                            editor.setStatus(-1, "Parsing...");
                        }
                    });
                    editor.validateXml();
                } catch (final SAXParseException e) {
                    exception = e;
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            editor.setStatus(e.getLineNumber(), e.getMessage());
                        }
                    });
                } catch (final IOException e) {
                    exception = e;
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            editor.setStatus(0, "ERROR: " + e.getMessage());
                        }
                    });
                } finally {
                    try {
                        if (exception == null) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    editor.setStatus(-1, "Saving...");
                                }
                            });
                        }
                        editor.save();
                    } catch (final SAXParseException e) {
                    } catch (final IOException e) {
                    } catch (final Throwable t) {
                        t.printStackTrace();
                    } finally {
                        if (exception == null) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    editor.setStatus(-1, "Done");
                                }
                            });
                        }
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                editor.setWait(false);
                            }
                        });
                    }
                }
            }
        };
        Thread thread = new Thread(runner);
        thread.start();
    }
}
