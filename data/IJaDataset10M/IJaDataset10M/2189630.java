package sf.model.fields;

import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

@Deprecated
public class ZoomField extends JPanel implements Field {

    private static final long serialVersionUID = 1L;

    private JComponent field;

    private JButton button;

    private String pkTable, pkColumn;

    public ZoomField(String naziv, int columnSize, Class<?> cls, String pkTable, String pkColumn) {
        this.pkTable = pkTable;
        this.pkColumn = pkColumn;
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        field = FieldFactory.getFieldForClass(cls, naziv, columnSize, null);
        button = new JButton("...");
        button.setMargin(new Insets(1, 1, 1, 1));
        add(field);
        add(button);
    }

    public void clear() {
        ((Field) field).clear();
    }

    public Object getValue() {
        return ((Field) field).getValue();
    }

    public void setValue(Object value) {
        ((Field) field).setValue(value);
    }
}
