package fileshare.GUI.ont;

import javax.swing.*;

public class SingleTextValueFiller extends AbstractTextFiller {

    SingleTextValueFiller(String s, String label) {
        super(s);
        txt = new JTextField();
        lbl = new JLabel();
        this.setLayout(new java.awt.GridLayout(1, 2, 10, 1));
        lbl.setText(label);
        lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lbl.setMinimumSize(new java.awt.Dimension(100, 14));
        this.add(lbl);
        txt.setMinimumSize(new java.awt.Dimension(100, 14));
        txt.setText("");
        this.add(txt);
    }

    public String getStatement() {
        if (txt.getText() == null || txt.getText().trim().equals("")) return "";
        String statement = new String();
        String val = object + property;
        String value = txt.getText().trim().toLowerCase();
        statement = object + " " + prefix + property + val + " ." + NL;
        statement += "filter regex(" + val + ",\".*" + value + ".*\",\"i\")";
        return statement;
    }

    JTextField txt;

    JLabel lbl;
}
