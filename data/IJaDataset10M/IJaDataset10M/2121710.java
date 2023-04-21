package org.csiro.darjeeling.infuser.header;

import org.apache.bcel.generic.Type;
import org.csiro.darjeeling.infuser.Constants;
import org.csiro.darjeeling.infuser.structure.BaseType;
import org.csiro.darjeeling.infuser.structure.Element;
import org.csiro.darjeeling.infuser.structure.ParentElement;
import org.csiro.darjeeling.infuser.structure.elements.AbstractClassDefinition;
import org.csiro.darjeeling.infuser.structure.elements.AbstractField;
import org.csiro.darjeeling.infuser.structure.elements.AbstractHeader;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodDefinition;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodImplementation;
import org.csiro.darjeeling.infuser.structure.elements.AbstractStringTable;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethod;
import org.csiro.darjeeling.infuser.structure.elements.external.ExternalReferencedInfusionList;
import org.csiro.darjeeling.infuser.structure.elements.internal.InternalField;
import org.csiro.darjeeling.infuser.structure.visitors.DescendingVisitor;
import org.w3c.dom.Document;

public class HeaderVisitor extends DescendingVisitor {

    private Document doc;

    private org.w3c.dom.Element currentElement;

    public HeaderVisitor(Document doc) {
        this.doc = doc;
        currentElement = doc.createElement("dih");
        doc.appendChild(currentElement);
    }

    private org.w3c.dom.Element createElement(Element element) {
        org.w3c.dom.Element elem = doc.createElement(getElementName(element).toLowerCase());
        currentElement.appendChild(elem);
        return (elem);
    }

    @Override
    public <T extends Element> void visit(ParentElement<T> element) {
        org.w3c.dom.Element elem = createElement(element);
        org.w3c.dom.Element temp = currentElement;
        currentElement = elem;
        super.visit(element);
        currentElement = temp;
    }

    @Override
    public void visit(AbstractClassDefinition element) {
        org.w3c.dom.Element elem = createElement(element);
        elem.setAttribute("entity_id", "" + element.getGlobalId().getEntityId());
        elem.setAttribute("name", "" + element.getClassName());
        AbstractClassDefinition superClass = element.getSuperClass();
        if (superClass != null) {
            elem.setAttribute("superclass.infusion", "" + element.getSuperClass().getGlobalId().getInfusion());
            elem.setAttribute("superclass.entity_id", "" + element.getSuperClass().getGlobalId().getEntityId());
            elem.setAttribute("superclass.name", superClass.getClassName());
        } else {
            elem.setAttribute("superclass.infusion", "sys");
            elem.setAttribute("superclass.entity_id", "-1");
            elem.setAttribute("superclass.name", "");
        }
        org.w3c.dom.Element temp = currentElement;
        currentElement = elem;
        for (AbstractField field : element.getFieldList().getFields()) {
            field.accept(this);
        }
        for (AbstractMethod method : element.getChildren()) {
            method.accept(this);
        }
        currentElement = temp;
    }

    @Override
    public void visit(AbstractMethod element) {
        org.w3c.dom.Element elem = createElement(element);
        elem.setAttribute("methodimpl.infusion", element.getMethodImpl().getGlobalId().getInfusion());
        elem.setAttribute("methodimpl.entity_id", "" + element.getMethodImpl().getGlobalId().getEntityId());
        elem.setAttribute("methoddef.infusion", element.getMethodDef().getGlobalId().getInfusion());
        elem.setAttribute("methoddef.entity_id", "" + element.getMethodDef().getGlobalId().getEntityId());
    }

    /**
	 * Export the static field definition.
	 */
    @Override
    public void visit(AbstractField field) {
        org.w3c.dom.Element elem = createElement(field);
        Type type = Type.getType(field.getDescriptor());
        elem.setAttribute("entity_id", "" + field.getGlobalId().getEntityId());
        elem.setAttribute("type", "" + BaseType.fromType(type));
        elem.setAttribute("name", field.getName());
        elem.setAttribute("signature", field.getDescriptor());
        elem.setAttribute("parentclass.infusion", field.getParentClass().getGlobalId().getInfusion());
        elem.setAttribute("parentclass.entity_id", "" + field.getParentClass().getGlobalId().getEntityId());
    }

    @Override
    public void visit(AbstractStringTable stringTable) {
        org.w3c.dom.Element elem = createElement(stringTable);
        String strings[] = stringTable.elements();
        for (int i = 0; i < strings.length; i++) {
            org.w3c.dom.Element childElem = doc.createElement("string");
            childElem.setAttribute("entity_id", "" + i);
            childElem.setAttribute("value", strings[i]);
            elem.appendChild(childElem);
        }
    }

    @Override
    public void visit(AbstractHeader element) {
        org.w3c.dom.Element elem = createElement(element);
        elem.setAttribute("majorversion", "" + element.getMajorVersion());
        elem.setAttribute("minorversion", "" + element.getMinorVersion());
        elem.setAttribute("name", element.getInfusionName());
        int entryPoint = Constants.NO_ENTRYPOINT;
        if (element.getEntryPoint() != null) entryPoint = element.getEntryPoint().getGlobalId().getEntityId();
        elem.setAttribute("entrypoint", "" + entryPoint);
    }

    @Override
    public void visit(AbstractMethodImplementation element) {
        org.w3c.dom.Element elem = createElement(element);
        elem.setAttribute("entity_id", "" + element.getGlobalId().getEntityId());
        elem.setAttribute("methoddef.infusion", "" + element.getMethodDef().getGlobalId().getInfusion());
        elem.setAttribute("methoddef.entity_id", "" + element.getMethodDef().getGlobalId().getEntityId());
        elem.setAttribute("parentclass.infusion", "" + element.getParentClass().getGlobalId().getInfusion());
        elem.setAttribute("parentclass.entity_id", "" + element.getParentClass().getGlobalId().getEntityId());
    }

    @Override
    public void visit(AbstractMethodDefinition element) {
        org.w3c.dom.Element elem = createElement(element);
        elem.setAttribute("signature", element.getDescriptor());
        elem.setAttribute("name", element.getMethodName());
        elem.setAttribute("entity_id", "" + element.getGlobalId().getEntityId());
    }

    /**
	 * Write out a list of infusions referenced by this one. Since this is an external referenced infusion list,
	 * only write out the headers.
	 */
    @Override
    public void visit(ExternalReferencedInfusionList element) {
        super.visit(element);
    }

    @Override
    public void visit(Element element) {
        org.w3c.dom.Element elem = doc.createElement(getElementName(element));
        currentElement.appendChild(elem);
    }

    private String getElementName(Element element) {
        return element.getId().toString();
    }
}
