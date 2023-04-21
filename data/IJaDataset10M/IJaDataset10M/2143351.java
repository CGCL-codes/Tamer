package org.adapit.wctoolkit.transformers.csharp.m2c;

import java.util.Iterator;
import org.adapit.wctoolkit.models.config.ApplicationReport;
import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.modeltocodeframework.AbstractAttributeCodeGenerator;
import org.adapit.wctoolkit.uml.classes.kernel.Class;
import org.adapit.wctoolkit.uml.classes.kernel.VisibilityKind;
import org.adapit.wctoolkit.uml.ext.core.Attribute;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.NamedElement;
import org.adapit.wctoolkit.uml.ext.core.ScopeKind;
import org.adapit.wctoolkit.uml.ext.core.TaggedValue;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;

@SuppressWarnings({ "serial", "unchecked" })
public class GenerateAttributesWithCollectionsCode extends AbstractAttributeCodeGenerator {

    /**
	 * @param observer
	 * @param _class
	 */
    public GenerateAttributesWithCollectionsCode(TransformationDescriptor observer, Class _class) {
        super(observer, _class);
    }

    /**
	 * @param _class
	 */
    public GenerateAttributesWithCollectionsCode(Class _class) {
        super(_class);
    }

    private org.adapit.wctoolkit.uml.classes.kernel.Class rootClass;

    public Object doTransformation() throws Exception {
        refactor();
        if (_class.getAttributes() != null && _class.getAttributes().size() > 0) {
            if (_class.getParentElement() instanceof org.adapit.wctoolkit.uml.classes.kernel.Class) {
                rootClass = (Class) _class.getLastAncestor(org.adapit.wctoolkit.uml.classes.kernel.Class.class);
            } else rootClass = _class;
            include = getIncludeTag(rootClass);
            Iterator it = _class.getAttributes().iterator();
            int i = 0;
            while (it.hasNext()) {
                try {
                    Attribute attribute = (Attribute) it.next();
                    if (attribute.getName() == null || attribute.getName().equals("")) {
                        logger.error("The attribute number " + i + "has not a name!");
                        logger.error("Please, review the paramValue " + attribute.getParentElement().getNamespaceWithoutModel() + " and defines a name such attribute.");
                        ApplicationReport.getInstance().getLogTextArea().setText("The attribute number " + i + "has not a name!" + '\n' + "Please, review the paramValue " + attribute.getParentElement().getNamespaceWithoutModel() + " and defines a name to such attribute.");
                        i++;
                        continue;
                    }
                    i++;
                    CsharpTransformerUtility.refactorDataTypeNameToCsharpType((NamedElement) attribute.getType());
                    String str = "";
                    TaggedValue tv = new TaggedValue(attribute);
                    tv.setName("HeaderCode");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("HeaderCode");
                        if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += replaceByIdValues(tv.getValue());
                    }
                    tv = new TaggedValue(attribute);
                    tv.setName("annotations");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("annotations");
                        if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += replaceByIdValues(tv.getValue());
                    }
                    tv = new TaggedValue(attribute);
                    tv.setName("documentation");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("documentation");
                        if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += "/**" + '\n' + replaceByIdValues(tv.getValue()) + '\n' + "*/";
                    }
                    tv = new TaggedValue(attribute);
                    tv.setName("comment");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("comment");
                        if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += "/**" + '\n' + replaceByIdValues(tv.getValue()) + '\n' + "*/";
                    }
                    str += "" + '\n' + "" + '\t' + "";
                    if (attribute.getVisibility() != null && attribute.getVisibility() != VisibilityKind.DEFAULT) {
                        if (attribute.getVisibility() == VisibilityKind.PROTECTED) str += "internal"; else str += attribute.getVisibility().name().toLowerCase();
                    }
                    if (attribute.getScope() != null && attribute.getScope() == ScopeKind.INSTANCE) ; else if (attribute.getScope() != null) str += " static";
                    if (attribute.isReadOnly()) str += " sealed";
                    CsharpTransformerUtility.refactorDataTypeNameToCsharpType((NamedElement) attribute.getType());
                    if (attribute.getType() != null && !attribute.isCollection()) {
                        str += " " + attribute.getType().getName();
                        CsharpTransformerUtility.addIncludeByCsharpType(attribute.getType(), include);
                    } else if (attribute.getType() != null && attribute.isCollection()) {
                        str += " Generic<" + attribute.getType().getName() + "> ";
                        if (include.getValue().indexOf("System.Collections.Generic") < 0) include.setValue(include.getValue() + '\n' + "using System.Collections.Generic;");
                    } else {
                        str += " object";
                    }
                    if (attribute.getName() != null) str += " " + attribute.getName();
                    if (!attribute.isCollection() && attribute.getBody() != null) {
                        str += " = " + attribute.getBody() + ";";
                    } else {
                        if (attribute.containsTaggedValue("TemplateClass")) {
                            String elid = attribute.getAssignedTaggedValue("TemplateClass").getValue();
                            IElement tempClass = (IElement) AllElements.getInstance().getElements().get(elid);
                            str += "<" + tempClass.getName() + ">";
                            CsharpTransformerUtility.addIncludeByCsharpType(tempClass, include);
                        }
                        str += ";";
                    }
                    tv = new TaggedValue(attribute);
                    tv.setName("BodyCode");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("BodyCode");
                        str += replaceByIdValues(tv.getValue());
                    }
                    tv = new TaggedValue(attribute);
                    tv.setName("BottomCode");
                    if (attribute.contains(tv)) {
                        tv = attribute.getAssignedTaggedValue("BottomCode");
                        str += replaceByIdValues(tv.getValue());
                    }
                    this.attributesCode.add(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return attributesCode;
    }
}
