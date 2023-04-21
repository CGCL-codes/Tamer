package net.sf.doolin.gui.field;

import javax.swing.Icon;
import javax.swing.JLabel;
import net.sf.doolin.gui.expression.GUIExpression;
import net.sf.doolin.gui.field.support.SimpleField;
import net.sf.doolin.gui.service.IconSize;
import net.sf.doolin.gui.view.GUIView;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link JLabel} field descriptor.
 * 
 * @param <V>
 *            Type of object for the view
 */
public class FieldLabel<V> extends AbstractFieldDescriptor<V> {

    private static final long serialVersionUID = 1L;

    private String text;

    private String iconId;

    @Override
    public Field<V> createField(GUIView<V> view) {
        GUIExpression expression = new GUIExpression(view.getActionContext().getSubscriberValidator(), view.getViewData(), this.text);
        JLabel label = new JLabel();
        expression.connect(view.getActionContext().getSubscriberValidator(), label, "text");
        if (StringUtils.isNotBlank(this.iconId)) {
            Icon icon = getIconService().getIcon(this.iconId, IconSize.SMALL);
            label.setIcon(icon);
        }
        return new SimpleField<V>(view, this, label);
    }

    /**
	 * Gets the ID of the icon to associate with this label.
	 * 
	 * @return Icon ID or <code>null</code>
	 */
    public String getIconId() {
        return this.iconId;
    }

    /**
	 * Gets the text.
	 * 
	 * @return the text
	 */
    public String getText() {
        return this.text;
    }

    /**
	 * Sets the ID of the icon to associate with this label.
	 * 
	 * @param iconId
	 *            Icon ID or <code>null</code>
	 */
    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    /**
	 * Expression that contains the text of the label
	 * 
	 * @param text
	 *            the new text
	 */
    @Required
    public void setText(String text) {
        this.text = text;
    }
}
