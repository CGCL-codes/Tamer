package sears.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import sears.gui.search.FindDialog;
import sears.tools.SearsResourceBundle;

/**
 * This class represents a dialog to allow to search a word in the opened subtitle.
 */
public class JDialogFind extends SearsJDialog implements FindDialog {

    private static final long serialVersionUID = -3289742897531861980L;

    private static String ghost_text = "";

    private static String stringToFind = null;

    private JPanel panel = null;

    private JPanel jPanelButtons = null;

    private JLabel label = null;

    private JTextField textField = null;

    private JLabel ghostLabel = null;

    private JButton jButtonNext = null;

    private JButton jButtonPrevious = null;

    private MainWindow mainWindow;

    /**
	 * Constructs a new object
	 * @param frame the frame which calls the dialog
	 * @throws NullPointerException if <tt>frame</tt> is <tt>null</tt>
	 */
    public JDialogFind(MainWindow frame) {
        super(SearsResourceBundle.getResource("find_title"));
        if (frame == null) {
            throw new NullPointerException("MainWindow instance cannot be null");
        }
        mainWindow = frame;
        configureDialog();
    }

    /**
	 * Configure the size dialog and add all the panels
	 */
    private void configureDialog() {
        this.setResizable(false);
        this.setSize(new Dimension(300, 120));
        this.setLayout(new BorderLayout());
        this.add(getFindPanel(), BorderLayout.PAGE_START);
        this.add(getJPanelButtons(), BorderLayout.LINE_END);
    }

    /**
	 * initialize the ghost_text (final like)
	 * @return the initialize ghost text
	 */
    private String getGhostText() {
        if (ghost_text == "") {
            String text = SearsResourceBundle.getResource("find_not_found");
            for (int i = 0; i < text.length(); i++) {
                ghost_text += " ";
            }
        }
        return ghost_text;
    }

    private void enabled(boolean aFlag) {
        jButtonNext.setEnabled(aFlag);
        jButtonPrevious.setEnabled(aFlag);
        if (!aFlag) {
            ghostLabel.setText(getGhostText());
        }
    }

    /**
	 * this method constructs the principal panel
	 * @return the panel
	 */
    private JPanel getFindPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            label = new JLabel(SearsResourceBundle.getResource("find_title") + ":");
            textField = new JTextField(stringToFind);
            if (stringToFind != null && stringToFind.length() > 0) {
                textField.setSelectionStart(0);
                textField.setSelectionEnd(stringToFind.length());
            }
            textField.setPreferredSize(new Dimension(170, 20));
            textField.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent ke) {
                    onKeyPressed(ke);
                }
            });
            textField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    enabled((textField.getText().trim().length() > 0));
                }
            });
            ghostLabel = new JLabel(getGhostText());
            ghostLabel.setFont(new Font(null, textField.getFont().getStyle(), textField.getFont().getSize() - 2));
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.LINE_START;
            c.weighty = 0.5;
            c.insets = new Insets(10, 3, 2, 3);
            c.gridx = 0;
            c.gridy = 0;
            panel.add(label, c);
            c.gridx = 1;
            c.gridy = 0;
            panel.add(textField, c);
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(2, 3, 10, 3);
            c.gridwidth = 2;
            panel.add(ghostLabel, c);
        }
        return panel;
    }

    /**
	 * When user pressed <b>ENTER</b> key a next find action is perform
	 * if <b>SHIFT</b> key is also pressed then it's a previous find action that is perform.
	 */
    private void onKeyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!ke.isShiftDown()) {
                getJButtonNext().doClick();
            } else {
                getJButtonPrevious().doClick();
            }
        }
    }

    protected String getDialogName() {
        return "Find";
    }

    protected JPanel getJPanelButtons() {
        if (jPanelButtons == null) {
            jPanelButtons = new JPanel();
            jPanelButtons.add(getJButtonPrevious(), null);
            jPanelButtons.add(getJButtonNext(), null);
        }
        return jPanelButtons;
    }

    /**
	 * Construct the next button
	 * @return the next button
	 */
    private JButton getJButtonNext() {
        if (jButtonNext == null) {
            jButtonNext = new JButton(SearsResourceBundle.getResource("find_next"));
            if (textField.getText().trim().length() == 0) {
                jButtonNext.setEnabled(false);
            }
            jButtonNext.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Thread next = new Thread(new Runnable() {

                        public void run() {
                            findStringInSubtitle(false);
                        }
                    });
                    next.start();
                }
            });
        }
        return jButtonNext;
    }

    /**
	 * Construct the previous button
	 * @return the previous button
	 */
    private JButton getJButtonPrevious() {
        if (jButtonPrevious == null) {
            jButtonPrevious = new JButton(SearsResourceBundle.getResource("find_previous"));
            if (textField.getText().length() == 0) {
                jButtonPrevious.setEnabled(false);
            }
            jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Thread previous = new Thread(new Runnable() {

                        public void run() {
                            findStringInSubtitle(true);
                        }
                    });
                    previous.start();
                }
            });
        }
        return jButtonPrevious;
    }

    /**
	 * performs a find action in the subtitle.
	 * @param previous if true call a previous find action, if false it's a next one
	 */
    private void findStringInSubtitle(boolean previous) {
        mainWindow.find(this, previous);
    }

    /**
	 * Gets the text entered by user
	 * @return the text entered by user, if the text is null, null is returned
	 */
    public String getText() {
        return textField.getText();
    }

    /**
	 * Sets the count of occurrences of the searched text in subtitle file
	 * <br>This method is not thread-safe.
	 * @param occurencesCount the count of occurrences
	 */
    public void setOccurencesCount(int occurencesCount) {
        if (occurencesCount <= 0) {
            ghostLabel.setText(SearsResourceBundle.getResource("find_not_found"));
        } else {
            String text = "";
            if (occurencesCount == 1) {
                text = SearsResourceBundle.getResource("find_match_found");
            } else {
                text = SearsResourceBundle.getResource("find_matches_found");
            }
            ghostLabel.setText(String.valueOf(occurencesCount + " " + text));
        }
    }

    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        stringToFind = textField.getText();
        mainWindow.fireFindStop();
    }

    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
    }

    public void setFindEnabled(Boolean inFlag) {
    }
}
