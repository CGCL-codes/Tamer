package org.adapit.wctoolkit.models.interceptors;

import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.uml.classes.kernel.Parameter;
import org.adapit.wctoolkit.uml.ext.core.ExceptionType;
import org.adapit.wctoolkit.uml.ext.core.TaggedValue;

@SuppressWarnings({ "unchecked" })
public class ExceptionTypeTaggedValueInterceptor extends AbstractTaggedValueInterceptor {

    public ExceptionTypeTaggedValueInterceptor() {
        tagName = "ExceptionType";
    }

    public void convert(String tagName, java.lang.Object content) {
        if (tagName.equalsIgnoreCase("ExceptionType")) {
            TaggedValue s = (TaggedValue) content;
            if (s.getValue().equals("true")) {
                Parameter parameter = (Parameter) owningElement;
                ExceptionType ex = new ExceptionType(parameter.getParentElement());
                if (parameter.getType() != null) ex.setType(parameter.getType()); else if (parameter.getIdType() != null && !parameter.getIdType().trim().equals("")) ex.setIdType(parameter.getIdType()); else {
                    logger.error("Unable to find the type of parameter");
                }
                ex.setName(parameter.getName());
                AllElements.getInstance().getElements().put(ex.getId(), ex);
                ex.setAssignedStereotypes(parameter.getAssignedStereotypes());
                ex.setAssignedTaggedValues(parameter.getAssignedTaggedValues());
                ex.setComments(parameter.getComments());
                parameter.setRemovable(true);
                ex.getParentElement().addElement(ex);
            }
        }
    }
}
