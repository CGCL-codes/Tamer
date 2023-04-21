package org.ungoverned.radical.ui.editor;

import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

public class DimensionEditor extends PropertyEditorSupport {

    public DimensionEditor() {
    }

    public Component getCustomEditor() {
        Dimension d = (Dimension) getValue();
        JTextField field = new JTextField(d.width + ", " + d.height);
        field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JTextField field = (JTextField) event.getSource();
                String s = field.getText();
                StringTokenizer st = new StringTokenizer(field.getText(), ", \t\n\r\f");
                try {
                    if (st.countTokens() == 2) {
                        String sw = st.nextToken();
                        String sh = st.nextToken();
                        try {
                            int w = Integer.valueOf(sw).intValue();
                            int h = Integer.valueOf(sh).intValue();
                            setValue(new Dimension(w, h));
                        } catch (Exception ex) {
                        }
                    }
                } finally {
                    Dimension d = (Dimension) getValue();
                    field.setText(d.width + ", " + d.height);
                }
            }
        });
        return field;
    }

    /**
     * Convert the value object to its proper Java representation.
     * @return the appropriate Java source code if the
     *         conversion failed.
    **/
    public String getJavaInitializationString() {
        Dimension d = (Dimension) getValue();
        return "new Dimension(" + d.width + ", " + d.height + ")";
    }
}
