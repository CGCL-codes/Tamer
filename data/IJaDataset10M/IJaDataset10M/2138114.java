package org.xnap.commons.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
import org.xnap.commons.gui.util.GUIHelper;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Simple about dialog. A {@link JTabbedPane} is used as the main widget and
 * methods are provided to add tabs that contain a file, e.g. the software 
 * license.  
 */
public class AboutDialog extends DefaultDialog {

    private CloseableTabbedPane tabbedPane;

    private Map<String, JComponent> tabByFilename = new Hashtable<String, JComponent>();

    /**
     * Constructs an empty dialog with a single close button.
     * 
     * @param owner the dialog owner
     */
    public AboutDialog(JDialog owner) {
        super(owner, BUTTON_CLOSE);
        initialize();
    }

    /**
     * Constructs an empty dialog with a single close button.
     * 
     * @param owner the dialog owner
     */
    public AboutDialog(JFrame owner) {
        super(owner, BUTTON_CLOSE);
        initialize();
    }

    /**
     * Constructs an empty dialog with a single close button.
     */
    public AboutDialog() {
        super(BUTTON_CLOSE);
        initialize();
    }

    private void initialize() {
        tabbedPane = new CloseableTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(600, 300));
        tabbedPane.setCloseListener(new CloseableTabbedPane.CloseListener() {

            public void closeRequested(Component component) {
                tabbedPane.remove(component);
                tabByFilename.values().remove(component);
            }
        });
        setMainComponent(tabbedPane);
        setButtonSeparatorVisible(false);
        pack();
    }

    /**
	 * Adds a tab that displays an image that is followed by a file's content.
	 *  
	 * @param title the title of the tab
	 * @param image the image
	 * @param filename the name of the file to display
	 * @param alternativeMessage the text that is displayed if 
	 * <code>filename</code> can not be read
	 * @return the text area that was added to the created tab 
	 * @see GUIHelper#showFile(JTextComponent, String, String)
	 */
    public JTextArea addLogoTab(String title, Icon image, String filename, String alternativeMessage, boolean closeable) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new BorderLayout());
        JLabel logoLabel = new JLabel(image);
        logoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(logoLabel, BorderLayout.NORTH);
        JTextArea textArea = new JTextArea(5, 40);
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(textArea, BorderLayout.CENTER);
        addTabInternal(textArea, title, filename, alternativeMessage, closeable);
        return textArea;
    }

    /**
	 * Adds a tab that displays an image that is followed by a file's content.
	 *  
	 * @param title the title of the tab
	 * @param image the image
	 * @param filename the name of the file to display
	 * @return the text area that was added to the created tab 
	 * @see #addLogoTab(String, Icon, String, String, boolean)
	 */
    public JTextArea addLogoTab(String title, Icon image, String filename) {
        return addLogoTab(title, image, filename, getFileNotFoundMessage(filename), false);
    }

    /**
	 * Adds a tab that displays a file's content.
	 *  
	 * @param title the title of the tab
	 * @param filename the name of the file to display
	 * @param alternativeMessage the text that is displayed if 
	 * <code>filename</code> can not be read
	 * @return the text area that was added to the created tab 
	 * @see GUIHelper#showFile(JTextComponent, String, String)
	 */
    public JTextArea addTab(String title, String filename, String alternativeMessage, boolean closeable) {
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        textArea.setEditable(false);
        addTabInternal(textArea, title, filename, alternativeMessage, closeable);
        return textArea;
    }

    /**
	 * Adds a tab that displays a file's content.
	 *  
	 * @param title the title of the tab
	 * @param filename the name of the file to display
	 * @return the text area that was added to the created tab 
	 * @see #addTab(String, String, String, boolean)
	 */
    public JTextArea addTab(String title, String filename) {
        return addTab(title, filename, getFileNotFoundMessage(filename), false);
    }

    public JEditorPane addHTMLTab(String title, String filename, String alternativeMessage, boolean closeable, boolean followHyperlinks) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        if (followHyperlinks) {
            editorPane.addHyperlinkListener(new LicenseLinkListener());
        }
        editorPane.setBorder(GUIHelper.createEmptyBorder(5));
        addTabInternal(editorPane, title, filename, alternativeMessage, closeable);
        return editorPane;
    }

    public JEditorPane addHTMLTab(String title, String filename, boolean followHyperlinks) {
        return addHTMLTab(title, filename, getFileNotFoundMessage(filename), false, followHyperlinks);
    }

    private void addTabInternal(JTextComponent component, String title, String filename, String alternativeMessage, boolean closeable) {
        JScrollPane scrollPane = new JScrollPane(component);
        GUIHelper.showFile(component, filename, alternativeMessage);
        getTabbedPane().addTab(title, scrollPane, null, closeable);
        tabByFilename.put(filename, scrollPane);
    }

    /**
	 * Returns the main widget.
	 */
    public CloseableTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public String getFileNotFoundMessage(String filename) {
        return I18nFactory.getI18n(AboutDialog.class).tr("File {0} not found", filename);
    }

    /**
     * Adds a new license tab.
     *
     * License files are supposed to be pure text files.
     *
     * @param name of the license
     * @param filename filename of the license text to load
     */
    public Component getOrAddTab(String name, String filename) {
        JComponent c = tabByFilename.get(filename);
        if (c == null) {
            if (filename.endsWith("html") || filename.endsWith("htm")) {
                addHTMLTab(name, filename, getFileNotFoundMessage(filename), true, false);
            } else {
                JTextArea textArea = addTab(name, filename, getFileNotFoundMessage(filename), true);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            }
            c = tabByFilename.get(filename);
        }
        return c;
    }

    /**
     * Listens for hyperlink events and switches the panels of the tabbed pain
     * when the link description matches a license displayed in one of the
     * panels.
     */
    private class LicenseLinkListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getDescription() == null) {
                return;
            }
            if (event.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                setCursor(event, Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else if (event.getEventType() == HyperlinkEvent.EventType.EXITED) {
                setCursor(event, null);
            } else if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                setCursor(event, null);
                Component c = getOrAddTab(event.getDescription(), event.getDescription());
                tabbedPane.setSelectedComponent(c);
            }
        }

        private void setCursor(HyperlinkEvent event, Cursor cursor) {
            JEditorPane pane = (JEditorPane) event.getSource();
            AboutDialog.this.setCursor(cursor);
        }
    }
}
