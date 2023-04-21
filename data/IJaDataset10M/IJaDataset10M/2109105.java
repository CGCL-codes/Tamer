package org.objectstyle.cayenne.dataview.dvmodeler;

import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Nataliya Kholodna
 * @version 1.0
 */
class ErrorsPanel extends JPanel {

    public ErrorsPanel(java.util.List errors, String labelText) {
        super();
        String htmlErrors = "<html><body><font size=-1 >";
        for (Iterator j = errors.iterator(); j.hasNext(); ) {
            htmlErrors += (String) j.next();
        }
        htmlErrors += "</font></body></html>";
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(htmlErrors);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(500, 280));
        scrollPane.setMinimumSize(new Dimension(5, 200));
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setLabelFor(editorPane);
        label.setAlignmentX(LEFT_ALIGNMENT);
        this.add(label);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(scrollPane);
    }
}
