package edu.ucla.cs.typecast.app.auction;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

/**
 * A dialog for entering text fields.
 */
public class TextFieldDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public enum Result {

        NONE, PENDING, OK, CANCEL
    }

    ;

    public Result result = Result.NONE;

    protected List<String> names = new ArrayList<String>();

    protected List<JTextField> boxes = new ArrayList<JTextField>();

    public static boolean isPositiveInteger(String value) {
        int id;
        try {
            id = Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }
        return id > 0;
    }

    public static boolean isEmpty(String value) {
        return value.trim().equals("");
    }

    public TextFieldDialog(Frame owner, String title, String[] fields) {
        this(owner, title, fields, null);
    }

    public TextFieldDialog(Frame owner, String title, String[] fields, String[] defaults) {
        super(owner, title, true);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(fields.length + 1, 2));
        names = Arrays.asList(fields);
        int idx = -1;
        for (String name : names) {
            idx++;
            panel.add(new JLabel(name + ": "));
            JTextField box = new JTextField();
            boxes.add(box);
            panel.add(box);
            if (defaults != null && defaults.length > idx && defaults[idx] != null) {
                box.setText(defaults[idx]);
            }
        }
        JButton okButton = new JButton("OK");
        panel.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        panel.add(cancelButton);
        getContentPane().add(panel);
        setSize(300, (fields.length + 1) * 20 + 10 + 60);
        final TextFieldDialog dialog = this;
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int idx = -1;
                for (String name : names) {
                    idx++;
                    String errorMsg = verifyField(name, boxes.get(idx).getText());
                    if (errorMsg != null) {
                        JOptionPane.showMessageDialog(dialog, errorMsg);
                        return;
                    }
                }
                result = Result.OK;
                dialog.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                result = Result.CANCEL;
                dialog.setVisible(false);
            }
        });
    }

    public Result getResult() {
        return result;
    }

    public String verifyField(String fieldName, String fieldValue) {
        return null;
    }

    public String getFieldValue(String fieldName) {
        int idx = names.indexOf(fieldName);
        if (idx > -1) {
            return boxes.get(idx).getText();
        } else {
            return null;
        }
    }
}
