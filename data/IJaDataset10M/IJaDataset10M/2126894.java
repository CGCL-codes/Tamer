package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;
import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JList;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.swing.SourceComboBoxModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

public class SourceListCellRenderer extends MaxLengthListCellRenderer {

    public static final long serialVersionUID = 0;

    private final String nullLabel;

    /**
	 * Creates a new Source list renderer, using the given string
	 * as the label when a source is not selected, with each item having
	 * a maximum length conputed from the specified component.
	 * @param nullLabel
	 * @param component
	 */
    public SourceListCellRenderer(String nullLabel, JComponent component) {
        super(component);
        this.nullLabel = nullLabel;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Object newValue = "";
        if (value instanceof Source) newValue = ((Source) value).getFullName(); else if (BuddiKeys.SPLITS.toString().equals(value)) newValue = TextFormatter.getTranslation(value.toString()); else if (index == -1) newValue = nullLabel; else newValue = value;
        super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
        if (value instanceof Source) {
            Source source = (Source) value;
            if (source.isDeleted()) {
                this.setText("<strike>" + this.getText() + "</strike>");
            }
            try {
                Field dataModelField = JList.class.getDeclaredField("dataModel");
                dataModelField.setAccessible(true);
                Object dataModel = dataModelField.get(list);
                if (dataModel instanceof SourceComboBoxModel) {
                    SourceComboBoxModel model = (SourceComboBoxModel) dataModel;
                    if (model.getLevel(source) > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < model.getLevel(source); i++) {
                            sb.append("&nbsp;&nbsp;");
                        }
                        this.setText(sb.toString() + this.getText());
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            this.setText("<html>" + this.getText() + "</html>");
        } else if (!BuddiKeys.SPLITS.toString().equals(value)) this.setText("<html><font color='gray'>" + this.getText() + "</font></html>");
        return this;
    }
}
