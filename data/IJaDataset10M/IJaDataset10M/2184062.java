package de.uni_leipzig.lots.common.propertyeditor;

import de.uni_leipzig.lots.common.objects.task.TaskId;
import java.beans.PropertyEditorSupport;

/**
 * PropertyEditor for {@link java.util.logging.Level}.
 *
 * @author Alexander Kiel
 * @version $Id: TaskIdEditor.java,v 1.5 2007/10/23 06:30:37 mai99bxd Exp $
 * @see java.util.logging.Level
 */
public class TaskIdEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            throw new IllegalArgumentException("The text cannot be null.");
        }
        setValue(new TaskId(text));
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
