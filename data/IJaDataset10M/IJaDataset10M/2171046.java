package de.uni_leipzig.lots.common.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.logging.Level;

/**
 * PropertyEditor for {@link Level}.
 *
 * @author Alexander Kiel
 * @version $Id: LevelEditor.java,v 1.7 2007/10/23 06:30:37 mai99bxd Exp $
 * @see Level
 */
public class LevelEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Level.parse(text));
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
