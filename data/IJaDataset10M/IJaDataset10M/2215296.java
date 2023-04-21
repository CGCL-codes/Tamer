package br.org.databasetools.core.view.render;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import br.org.databasetools.core.view.fields.TCnpjField;

public class TCnpjCellRenderer extends DefaultTableCellRenderer {

    public static final long serialVersionUID = 1L;

    public TCnpjCellRenderer() {
        super();
    }

    protected void setValue(Object value) {
        TCnpjField field = new TCnpjField();
        field.setFieldValue(value);
        setText((field.getFieldValue() == null) ? "" : ((JFormattedTextField) field.getComponent()).getText());
        setHorizontalAlignment(JLabel.CENTER);
    }
}
