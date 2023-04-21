package com.hp.hpl.guess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DebugGraphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import org.python.core.PyException;
import com.hp.hpl.guess.Version;

public class ExceptionWindow extends JFrame {

    private static final long serialVersionUID = -6837768932875931507L;

    JTextArea eMain = new JTextArea();

    JLabel eLabel = new JLabel();

    JButton copyButton = new JButton();

    JButton forwardB = new JButton();

    JButton backB = new JButton();

    JScrollPane jscrollpane1 = null;

    private Vector<Throwable> exceptions = new Vector<Throwable>();

    private int location = 0;

    private static ExceptionWindow singleton = null;

    public static ExceptionWindow getExceptionWindow() {
        return (getExceptionWindow(null));
    }

    public static ExceptionWindow getExceptionWindow(Throwable t) {
        if (singleton == null) {
            singleton = new ExceptionWindow();
        }
        singleton.newException(t);
        return (singleton);
    }

    public void newException(Throwable t) {
        if (t != null) {
            exceptions.add(t);
            setLocation(exceptions.size() - 1);
            if (t instanceof PyException) {
                StatusBar.setErrorStatus(((PyException) t).userFriendlyString());
            } else {
                StatusBar.setErrorStatus(t.toString());
            }
        }
    }

    private Clipboard clipboard = null;

    public void addToClipboard() {
        if (clipboard == null) {
            try {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            } catch (Exception ex) {
                ExceptionWindow.getExceptionWindow(ex);
            }
        }
        if (clipboard != null) {
            try {
                StringSelection ss = new StringSelection(eMain.getText());
                clipboard.setContents(ss, ss);
            } catch (Exception ex) {
                ExceptionWindow.getExceptionWindow(ex);
            }
        }
    }

    private void setLocation(int loc) {
        location = loc;
        if (loc <= 0) {
            location = 0;
            backB.setEnabled(false);
        } else {
            backB.setEnabled(true);
        }
        if (loc >= (exceptions.size() - 1)) {
            location = exceptions.size() - 1;
            forwardB.setEnabled(false);
        } else {
            forwardB.setEnabled(true);
        }
        displayException((Throwable) exceptions.elementAt(location));
    }

    public void displayException(Throwable t) {
        if (t != null) {
            eMain.setText(getStackTrace(t));
            if (t instanceof PyException) {
                eLabel.setText("<html><p>" + ((PyException) t).userFriendlyString() + "</p></html>");
            } else {
                eLabel.setText("<html><p>" + t.toString() + "</p></html>");
            }
            eMain.setCaretPosition(0);
        }
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        printWriter.println("\n\nGUESS version: " + Version.MAJOR_VERSION + "/" + Version.MINOR_VERSION);
        return result.toString();
    }

    /**
	 * Default constructor
	 */
    private ExceptionWindow() {
        super();
        getContentPane().setLayout(new BorderLayout());
        setTitle("Error Log - GUESS");
        setResizable(true);
        setSize(576, 416);
        setLocation(100, 100);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                setVisible(false);
            }
        });
        try {
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/guess-icon.png")));
            setIconImage(imageIcon.getImage());
        } catch (Exception ex) {
        }
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        createPanel();
    }

    private void createPanel() {
        final JPanel panel = new JPanel();
        panel.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 7 };
        panel.setLayout(gridBagLayout);
        getContentPane().add(panel);
        eMain.setEditable(false);
        eMain.setName("eMain");
        jscrollpane1 = new JScrollPane();
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new Insets(2, 12, 2, 12);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridx = 0;
        jscrollpane1.setAutoscrolls(true);
        jscrollpane1.setViewportView(eMain);
        jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(jscrollpane1, gridBagConstraints);
        eLabel.setForeground(new Color(255, 0, 0));
        eLabel.setAutoscrolls(true);
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.anchor = GridBagConstraints.WEST;
        gridBagConstraints_1.insets = new Insets(12, 12, 2, 12);
        gridBagConstraints_1.fill = GridBagConstraints.BOTH;
        gridBagConstraints_1.gridwidth = 4;
        gridBagConstraints_1.gridy = 0;
        gridBagConstraints_1.gridx = 0;
        panel.add(eLabel, gridBagConstraints_1);
        eLabel.setName("eLabel");
        eLabel.setText("No exception reported");
        copyButton.addActionListener(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ev) {
                addToClipboard();
            }
        });
        copyButton.setMnemonic(KeyEvent.VK_O);
        copyButton.setPreferredSize(new Dimension(90, 25));
        final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.weightx = 1;
        gridBagConstraints_2.anchor = GridBagConstraints.WEST;
        gridBagConstraints_2.insets = new Insets(2, 12, 12, 2);
        gridBagConstraints_2.gridy = 2;
        gridBagConstraints_2.gridx = 0;
        panel.add(copyButton, gridBagConstraints_2);
        copyButton.setActionCommand("Copy to clipboard");
        copyButton.setName("copyButton");
        copyButton.setText("Copy");
        forwardB.setEnabled(false);
        forwardB.setMnemonic(KeyEvent.VK_N);
        forwardB.setPreferredSize(new Dimension(90, 25));
        final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.anchor = GridBagConstraints.EAST;
        gridBagConstraints_3.insets = new Insets(2, 2, 12, 2);
        gridBagConstraints_3.gridy = 2;
        gridBagConstraints_3.gridx = 2;
        panel.add(forwardB, gridBagConstraints_3);
        forwardB.addActionListener(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ev) {
                setLocation(location + 1);
            }
        });
        forwardB.setActionCommand(">");
        forwardB.setName("forwardB");
        forwardB.setText("Next >");
        backB.setEnabled(false);
        backB.setMnemonic(KeyEvent.VK_B);
        backB.setPreferredSize(new Dimension(90, 25));
        final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
        gridBagConstraints_4.weightx = 0;
        gridBagConstraints_4.anchor = GridBagConstraints.EAST;
        gridBagConstraints_4.insets = new Insets(2, 2, 12, 2);
        gridBagConstraints_4.gridy = 2;
        gridBagConstraints_4.gridx = 1;
        panel.add(backB, gridBagConstraints_4);
        backB.addActionListener(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ev) {
                setLocation(location - 1);
            }
        });
        backB.setActionCommand("<");
        backB.setName("backB");
        backB.setText("< Back");
        final JButton closeButton = new JButton();
        closeButton.setMnemonic(KeyEvent.VK_C);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });
        closeButton.setPreferredSize(new Dimension(90, 25));
        closeButton.setText("Close");
        final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
        gridBagConstraints_5.insets = new Insets(2, 2, 12, 12);
        gridBagConstraints_5.gridy = 2;
        gridBagConstraints_5.gridx = 3;
        panel.add(closeButton, gridBagConstraints_5);
    }
}
