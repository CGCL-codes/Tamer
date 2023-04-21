package net.sourceforge.sandirc.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SearchPanel extends JComponent {

    private JButton closeButton;

    private JLabel searchLabel;

    private JTextField searchField;

    private JButton previousButton;

    private JButton nextButton;

    private JCheckBox matchCaseOption;

    private JTextComponent jtc;

    public SearchPanel() {
        initComponents();
        Dimension dim = new Dimension(350, 50);
        setMinimumSize(dim);
        setPreferredSize(dim);
    }

    public SearchPanel(JTextComponent jtc) {
        this();
        this.jtc = jtc;
    }

    public void find(boolean down) {
        if (searchField.getText().trim().equals("")) {
            Toolkit.getDefaultToolkit().beep();
            return;
        } else {
            String searchText = searchField.getText();
            boolean sensitiveSearch = matchCaseOption.isSelected();
            find(searchText, sensitiveSearch, down);
        }
    }

    public boolean find(String findString, boolean matchCase, boolean downDirection) {
        boolean found = false;
        int position = jtc.getCaretPosition();
        int direction = 1;
        Document openedDocument = jtc.getDocument();
        int tallies = openedDocument.getLength();
        final int len = findString.length();
        if (!downDirection) {
            position = jtc.getSelectionStart() - len;
            direction = -1;
        }
        for (int counter = position; counter < (tallies + 1); counter += direction) {
            String checkText;
            try {
                checkText = openedDocument.getText(counter, len);
            } catch (BadLocationException e) {
                return found;
            }
            if ((!matchCase && checkText.equalsIgnoreCase(findString)) || (matchCase && checkText.equals(findString))) {
                jtc.setCaretPosition(counter);
                jtc.moveCaretPosition(counter + len);
                jtc.grabFocus();
                found = true;
                Toolkit.getDefaultToolkit().beep();
                return found;
            }
        }
        Toolkit.getDefaultToolkit().beep();
        return found;
    }

    public void focusSearchField() {
        String tmpText = jtc.getSelectedText();
        if (tmpText != null && !tmpText.trim().equals("")) {
            searchField.setText(tmpText);
        }
        searchField.selectAll();
        searchField.requestFocus();
    }

    private JButton createButton(String imageLocation) {
        URL m_url = getClass().getResource(imageLocation);
        ImageIcon m_icon = new ImageIcon(m_url);
        JButton m_button = new ImageButton(m_icon);
        m_button.setBackground(getBackground());
        m_button.setFocusable(false);
        m_button.setContentAreaFilled(false);
        m_button.setBorderPainted(false);
        m_button.setOpaque(false);
        m_button.setMargin(null);
        return m_button;
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        String loc = "/net/sourceforge/sandirc/icons/close-button.png";
        closeButton = createButton(loc);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Container c = getParent();
                c.remove(SearchPanel.this);
                c.invalidate();
                c.validate();
                c.repaint();
            }
        });
        loc = "/net/sourceforge/sandirc/icons/Up16.gif";
        previousButton = createButton(loc);
        loc = "/net/sourceforge/sandirc/icons/Down16.gif";
        nextButton = createButton(loc);
        searchLabel = new JLabel("Search", JLabel.LEFT);
        searchField = new JTextField(15);
        matchCaseOption = new JCheckBox("Case sensitive");
        searchField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
        searchField.getActionMap().put("enter", new AbstractAction("enter") {

            public void actionPerformed(ActionEvent e) {
                find(true);
            }
        });
        add(closeButton);
        add(searchLabel);
        add(searchField);
        add(nextButton);
        add(previousButton);
        add(matchCaseOption);
        searchField.setRequestFocusEnabled(true);
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                find(true);
            }
        });
        previousButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                find(false);
            }
        });
    }
}
