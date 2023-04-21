package org.simfony.export;

import org.simfony.Element;
import org.simfony.Form;
import java.util.Properties;

/**
 * FormExporter exports forms. It converts form object
 * to different object and returns it. Type of the returned object
 * depends on Exporter implementation.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public abstract class FormExporter extends BasicExporter {

    /**
    * Exports element. It invokes <code>{@link #export(Form, Properties) export}</code>
    * if the element is instance of the Form. Otherwise method does nothing
    * and returns null.
    *
    * @param element Element to be exported.
    * @param props Export properties.
    *
    * @return Object that represents element in the specific environment.
    */
    public Object export(Element element, Properties props) {
        if (element instanceof Form) {
            return export((Form) element, props);
        } else {
            return null;
        }
    }

    /**
    * Exports form.
    *
    * @param form Form to be exported.
    * @param props Export properties.
    *
    * @return Object that represents Form in the specific environment.
    */
    public abstract Object export(Form form, Properties props);
}
