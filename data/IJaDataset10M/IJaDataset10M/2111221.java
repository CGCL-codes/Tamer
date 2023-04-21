package org.simfony.transform;

import org.simfony.Element;
import org.simfony.Form;
import org.simfony.Transformer;

/**
 * Transformer is used to transform forms.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public abstract class FormTransformer implements Transformer {

    /**
    * Transforms the element. This method invokes <code>{@link #transform(Form)}</code>
    * method if the element is instance of Form. Otherwise it does nothing.
    *
    * @param element Element to be transformed.
    */
    public void transform(Element element) {
        if (element instanceof Form) {
            transform((Form) element);
        }
    }

    /**
    * Transforms the form.
    *
    * @param form Form to be transformed.
    */
    public abstract void transform(Form form);
}
