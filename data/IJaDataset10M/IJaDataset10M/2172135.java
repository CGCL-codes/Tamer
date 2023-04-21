package org.simfony.transform;

import org.simfony.Element;
import org.simfony.Field;
import org.simfony.Transformer;

/**
 * Transformer is used to transform fields.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public abstract class FieldTransformer implements Transformer {

    /**
    * Transforms the element. This method invokes <code>{@link #transform(Field)}</code>
    * method if the element is instance of Field. Otherwise it does nothing.
    *
    * @param element Element to be transformed.
    */
    public void transform(Element element) {
        if (element instanceof Field) {
            transform((Field) element);
        }
    }

    /**
    * Transforms the field.
    *
    * @param field Field to be transformed.
    */
    public abstract void transform(Field field);
}
