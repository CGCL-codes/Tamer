package org.springframework.richclient.binding.form.swing;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.binding.form.Binding;
import org.springframework.richclient.binding.form.swing.AbstractSwingBinder;
import org.springframework.richclient.binding.form.swing.TextComponentBinding;
import org.springframework.util.Assert;

/**
 * @author Oliver Hutchison
 */
public class TextAreaBinder extends AbstractSwingBinder {

    public static final String ROWS_KEY = "rows";

    public static final String COLUMNS_KEY = "columns";

    public TextAreaBinder() {
        super(String.class, new String[] { ROWS_KEY, COLUMNS_KEY });
    }

    protected Binding doBind(JComponent control, FormModel formModel, String field, Map context) {
        Assert.isTrue(control instanceof JTextArea, "Control must be an instance of JTextArea.");
        JTextArea textArea = (JTextArea) control;
        Integer rows = (Integer) context.get(ROWS_KEY);
        if (rows != null) {
            textArea.setRows(rows.intValue());
        }
        Integer columns = (Integer) context.get(COLUMNS_KEY);
        if (columns != null) {
            textArea.setColumns(columns.intValue());
        }
        return new TextComponentBinding((JTextArea) control, formModel, field);
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createTextArea();
    }
}
