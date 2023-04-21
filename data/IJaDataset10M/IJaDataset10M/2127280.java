package hu.gbalage.owlforms.xforms.gen;

import hu.gbalage.owlforms.api.Field;
import hu.gbalage.owlforms.api.Form;

/**
 * @author Grill Balazs (balage.g@gmail.com)
 *
 */
public interface FormSerializer {

    public SerializationState initState(Form form);

    public SerializationState serializeForm(Form form, SerializationState state, Boolean selected);

    public SerializationState serializeField(Field field, SerializationState state);

    public void putInputDiv(SerializationState state);

    public SerializationState finalize(SerializationState state);
}
