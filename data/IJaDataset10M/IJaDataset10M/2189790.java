package jmax.editors.chooser;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import jmax.commons.*;

class ChooserSearchBox extends JPanel {

    private JLabel searchLabel;

    private JTextField searchText;

    private JButton clearButton;

    private ChooserBrowser chooserBrowser;

    private ChooserModel chooserModel;

    ChooserSearchBox(ChooserBrowser chooserBrowser) {
        super();
        this.chooserBrowser = chooserBrowser;
        chooserModel = chooserBrowser.getChooserModel();
        setLayout(new BorderLayout());
        Font searchFont = getFont().deriveFont(Font.PLAIN);
        searchLabel = new JLabel("Search");
        searchLabel.setFont(searchFont);
        add(searchLabel, BorderLayout.WEST);
        searchText = new JTextField();
        searchText.setFont(searchFont);
        searchText.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                recomputeFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                recomputeFilter();
            }

            public void changedUpdate(DocumentEvent e) {
                recomputeFilter();
            }
        });
        add(searchText, BorderLayout.CENTER);
        clearButton = new JButton("Clear");
        clearButton.setFont(searchFont);
        clearButton.addActionListener(new MaxAction() {

            public void doAction() {
                searchText.setText("");
            }
        });
        add(clearButton, BorderLayout.EAST);
        recomputeFilter();
    }

    private void recomputeFilter() {
        String text = searchText.getText();
        if ((text == null) || (text.equals(""))) chooserBrowser.setSearchFilter(null); else {
            RowFilter<TableModel, Integer> rf = null;
            try {
                rf = RowFilter.regexFilter(getCaseInsentiveRegex(text));
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            chooserBrowser.setSearchFilter(rf);
        }
    }

    private String getCaseInsentiveRegex(String text) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            buffer.append('[');
            buffer.append(Character.toUpperCase(c));
            buffer.append(',');
            buffer.append(Character.toLowerCase(c));
            buffer.append(']');
        }
        return buffer.toString();
    }
}
