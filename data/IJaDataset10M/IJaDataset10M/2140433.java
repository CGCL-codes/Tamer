package org.xmlhammer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.bounce.FormLayout;
import org.bounce.QDialog;
import org.bounce.image.ImageLoader;
import org.xmlhammer.gui.history.HistoryComboBox;
import org.xmlhammer.gui.history.HistoryComboBoxModel;
import org.xmlhammer.gui.history.HistoryUtilities;
import org.xmlhammer.gui.output.ConsolePanel;
import org.xmlhammer.gui.output.JavaPanel;
import org.xmlhammer.gui.output.LogPanel;
import org.xmlhammer.gui.output.ResultNode;
import org.xmlhammer.gui.output.ResultPanel;
import org.xmlhammer.gui.output.SearchablePanel;

/**
 * The find dialog.
 *
 * @version	$Revision$, $Date$
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class FindDialog extends QDialog {

    private static final long serialVersionUID = 6475728656462272652L;

    private XMLHammer parent = null;

    private static final ImageIcon NEXT_ICON = ImageLoader.get().getImage("/org/xmlhammer/gui/icons/etool16/search_next.gif");

    private static final ImageIcon PREVIOUS_ICON = ImageLoader.get().getImage("/org/xmlhammer/gui/icons/etool16/search_previous.gif");

    private static final ImageIcon WARNING_ICON = ImageLoader.get().getImage("/org/xmlhammer/gui/icons/etool16/search_warning.gif");

    private boolean foundSelectedNode = false;

    private JButton closeButton = null;

    private JButton nextButton = null;

    private JButton previousButton = null;

    private JLabel warningLabel = null;

    private HistoryComboBox searchField = null;

    private JCheckBox matchCaseButton = null;

    /**
	 * The dialog that displays the properties for the document.
	 *
	 * @param frame the parent frame.
	 */
    public FindDialog(XMLHammer parent) {
        super(parent, false);
        this.parent = parent;
        setResizable(false);
        setTitle("Find");
        JPanel main = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FormLayout(20, 7));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchField = new HistoryComboBox(new HistoryComboBoxModel(HistoryUtilities.getInstance().getSearches()));
        searchField.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    nextButtonPressed();
                }
            }
        });
        searchField.addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent e) {
                searchField.getEditor().selectAll();
                searchField.requestFocusInWindow();
            }

            public void ancestorMoved(AncestorEvent e) {
            }

            public void ancestorRemoved(AncestorEvent e) {
            }
        });
        searchField.setFont(searchField.getFont().deriveFont(Font.PLAIN));
        searchField.setEditable(true);
        searchPanel.add(new JLabel("Find:"), FormLayout.LEFT);
        searchPanel.add(searchField, FormLayout.RIGHT_FILL);
        matchCaseButton = new JCheckBox("Match case");
        matchCaseButton.setMnemonic('M');
        JPanel matchCasePanel = new JPanel(new BorderLayout());
        matchCasePanel.add(matchCaseButton, BorderLayout.WEST);
        warningLabel = new JLabel("Not found");
        warningLabel.setIcon(WARNING_ICON);
        warningLabel.setVisible(false);
        matchCasePanel.add(warningLabel, BorderLayout.EAST);
        searchPanel.add(new JLabel(""), FormLayout.LEFT);
        searchPanel.add(matchCasePanel, FormLayout.RIGHT_FILL);
        closeButton = new JButton("Close");
        closeButton.setMnemonic('C');
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        nextButton = new JButton("Find Next", NEXT_ICON);
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                nextButtonPressed();
            }
        });
        getRootPane().setDefaultButton(nextButton);
        previousButton = new JButton("Find Previous", PREVIOUS_ICON);
        previousButton.setMnemonic('P');
        previousButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                previousButtonPressed();
            }
        });
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButtonPanel.add(closeButton);
        JPanel findButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        findButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        findButtonPanel.add(nextButton);
        findButtonPanel.add(previousButton);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(5, 0, 3, 0));
        buttonPanel.add(closeButtonPanel, BorderLayout.EAST);
        buttonPanel.add(findButtonPanel, BorderLayout.WEST);
        main.add(searchPanel, BorderLayout.NORTH);
        main.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(main);
        pack();
        setLocationRelativeTo(parent);
    }

    private void nextButtonPressed() {
        search(true);
    }

    private void previousButtonPressed() {
        search(false);
    }

    private void search(boolean forward) {
        Object item = searchField.getEditor().getItem();
        foundSelectedNode = false;
        if (item != null) {
            SearchablePanel searchablePanel = getSelectedSearchablePanel();
            if (searchablePanel != null) {
                if (searchablePanel.search(item.toString(), matchCaseButton.isSelected(), forward)) {
                    warningLabel.setVisible(false);
                    return;
                }
            }
            warningLabel.setVisible(true);
        }
    }

    private SearchablePanel getSelectedSearchablePanel() {
        ProjectView view = parent.getProjectsView().getSelectedView();
        if (view != null) {
            ResultPanel panel = view.getResultPanel();
            if (panel.isShowing()) {
                return panel;
            }
            LogPanel logPanel = view.getLogPanel();
            if (logPanel.isShowing()) {
                return logPanel;
            }
            ConsolePanel consolePanel = view.getConsolePanel();
            if (consolePanel.isShowing()) {
                return consolePanel;
            }
            JavaPanel javaPanel = view.getJavaPanel();
            if (javaPanel.isShowing()) {
                return javaPanel;
            }
        }
        return null;
    }

    public ResultNode find(ResultNode start, ResultNode node, String search, boolean forwards, boolean matchCase) {
        if (foundSelectedNode && matches(node.getValue(), search, matchCase) && forwards) {
            return node;
        }
        if (node == start && forwards) {
            foundSelectedNode = true;
        }
        if (forwards) {
            for (int i = 0; i < node.getChildCount(); i++) {
                ResultNode result = find(start, (ResultNode) node.getChildAt(i), search, forwards, matchCase);
                if (result != null) {
                    return result;
                }
            }
        } else {
            for (int i = node.getChildCount() - 1; i >= 0; i--) {
                ResultNode result = find(start, (ResultNode) node.getChildAt(i), search, forwards, matchCase);
                if (result != null) {
                    return result;
                }
            }
        }
        if (foundSelectedNode && matches(node.getValue(), search, matchCase) && !forwards) {
            return node;
        }
        if (node == start && !forwards) {
            foundSelectedNode = true;
        }
        return null;
    }

    public boolean matches(String value, String search, boolean matchCase) {
        if (matchCase) {
            return value.indexOf(search) != -1;
        }
        return value.toLowerCase().indexOf(search.toLowerCase()) != -1;
    }

    public void cancel() {
        warningLabel.setVisible(false);
        super.cancel();
    }

    public void close() {
        warningLabel.setVisible(false);
        super.close();
    }

    /**
	 * Initialises the values in the dialog.
	 */
    public void init() {
        searchField.requestFocus();
        if (searchField.getItemCount() > 0) {
            searchField.removeAllItems();
        }
        searchField.getEditor().selectAll();
    }
}
