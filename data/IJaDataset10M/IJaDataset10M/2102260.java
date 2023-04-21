package de.schwarzrot.ui.validation.constraints;

import com.jgoodies.binding.PresentationModel;
import de.schwarzrot.data.Entity;

/**
 * checks whether a beans property is not empty
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 * @param <E>
 *            type of the bean, managed by a presentation model
 */
public class VCMandatory<E extends Entity> extends AbstractValidationConstraint<E> {

    public VCMandatory(PresentationModel<E> model, String propertyName) {
        super(model, propertyName);
    }

    @Override
    public boolean matches() {
        Object value = getValue();
        if (value == null) return false;
        if (value instanceof String) {
            String str = (String) value;
            int length = str.length();
            if (str.isEmpty()) return false;
            for (int i = length - 1; i >= 0; i--) {
                if (!Character.isWhitespace(str.charAt(i))) return true;
            }
            return false;
        }
        return true;
    }
}
