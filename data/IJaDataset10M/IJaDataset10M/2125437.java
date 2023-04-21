package fileshare.GUI.ont;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Eddy_User
 */
public class TextValueRetreiver extends AbstractValueRetreiver {

    /** Creates a new instance of TextValueFiller */
    private String property;

    private String value;

    private String label;

    private JTextField txt;

    private JLabel lbl;

    public TextValueRetreiver(String property, String label) {
        this.property = property;
        this.label = label;
        init();
    }

    private void init() {
        txt = new JTextField();
        lbl = new JLabel();
        this.setLayout(new java.awt.GridLayout(1, 2, 0, 1));
        lbl.setText(label);
        lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lbl.setMinimumSize(new java.awt.Dimension(100, 14));
        this.add(lbl);
        txt.setMinimumSize(new java.awt.Dimension(100, 14));
        txt.setText(this.value);
        this.add(txt);
    }

    public String getProperty() {
        return property;
    }

    public void setValue(String s) {
        value = s;
        this.txt.setText(s);
        if (s.trim().equals("")) return;
        this.txt.setEditable(false);
    }

    public String getValue() {
        return (value == null || value.trim().equals("")) ? this.txt.getText().trim() : value;
    }

    public String getLabel() {
        return this.label;
    }
}
