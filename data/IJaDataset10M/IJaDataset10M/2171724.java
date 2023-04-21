package org.kablink.teaming.module.definition.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.dom4j.Element;
import org.kablink.teaming.remoting.ws.model.CustomDateField;
import org.kablink.teaming.remoting.ws.model.CustomStringField;

/**
 *
 * @author Jong Kim
 */
public class ElementBuilderDate extends AbstractElementBuilder {

    protected boolean build(Element element, org.kablink.teaming.remoting.ws.model.DefinableEntity entityModel, Object obj, String dataElemType, String dataElemName) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            if (element != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                element.setText(sdf.format(date));
            }
            if (entityModel != null) entityModel.addCustomDateField(new CustomDateField(dataElemName, dataElemType, date));
        } else if (obj != null) {
            if (element != null) element.setText(obj.toString());
            if (entityModel != null) entityModel.addCustomStringField(new CustomStringField(dataElemName, dataElemType, obj.toString()));
        }
        return true;
    }
}
