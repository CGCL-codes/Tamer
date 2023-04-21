package org.columba.core.gui.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.columba.core.resourceloader.GlobalResourceLoader;

/**
 * Dialg showing information to the user. This can be either a URL to document
 * or a string.
 * 
 * @author fdietz
 */
public class InfoViewerDialog extends JDialog implements ActionListener {

    protected JButton helpButton;

    protected JButton closeButton;

    protected JTextPane textPane;

    /**
	 * Creates a new info dialog showing the specified string.
	 */
    public InfoViewerDialog(String message) {
        this();
        HTMLEditorKit editorKit = new HTMLEditorKit();
        StyleSheet styles = new StyleSheet();
        Font font = UIManager.getFont("Label.font");
        String name = font.getName();
        int size = font.getSize();
        String css = "<style type=\"text/css\"><!--p {font-family:\"" + name + "\"; font-size:\"" + size + "pt\"}--></style>";
        styles.addRule(css);
        editorKit.setStyleSheet(styles);
        textPane.setEditorKit(editorKit);
        textPane.setText(message);
        setVisible(true);
    }

    /**
	 * Creates a new info dialog showing the contents of the given URL.
	 */
    public InfoViewerDialog(URL url) throws IOException {
        this();
        textPane.setPage(url);
        setVisible(true);
    }

    /**
	 * Internal constructor used by the two public ones.
	 */
    protected InfoViewerDialog() {
        super(new JFrame(), "Info", true);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    protected void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        getContentPane().add(mainPanel);
        textPane = new JTextPane();
        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        scrollPane.getViewport().setBackground(Color.white);
        mainPanel.add(scrollPane);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 6, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        ButtonWithMnemonic closeButton = new ButtonWithMnemonic(GlobalResourceLoader.getString("global", "global", "close"));
        closeButton.setActionCommand("CLOSE");
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        ButtonWithMnemonic helpButton = new ButtonWithMnemonic(GlobalResourceLoader.getString("global", "global", "help"));
        buttonPanel.add(helpButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(closeButton);
        getRootPane().registerKeyboardAction(this, "CLOSE", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("CLOSE")) {
            setVisible(false);
        }
    }
}
