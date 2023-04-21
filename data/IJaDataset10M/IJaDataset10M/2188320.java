package org.adapit.wctoolkit.uml.diagraminterchange;

import java.io.Serializable;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.ElementImpl;
import org.w3c.dom.Node;

@SuppressWarnings({ "serial" })
public class SimpleSemanticModelElement extends ElementImpl implements IElement, Serializable {

    private String presentation = "";

    private TypeInfo typeInfo;

    public SimpleSemanticModelElement(IElement parent) {
        super(parent);
    }

    public SimpleSemanticModelElement() {
        super();
    }

    @Override
    public void merge(IElement el) throws Exception {
        super.merge(el);
    }

    @Override
    public void importXMI1_2(Node element) throws Exception {
        super.importXMI1_2(element);
        if (element.getNodeName().equalsIgnoreCase("UML:SimpleSemanticModelElement")) {
            try {
                this.presentation = element.getAttributes().getNamedItem("presentation").getNodeValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.typeInfo = TypeInfo.valueOf(element.getAttributes().getNamedItem("typeInfo").getNodeValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String exportXMI1_2(int tab) throws Exception {
        String str = "";
        str += '\n';
        for (int i = 0; i < tab; i++) str += '\t';
        str += "<UML:SimpleSemanticModelElement xmi.id=\"" + getId() + "\" presentation=\"" + presentation + "\" typeInfo=\"" + typeInfo.name() + "\"/>";
        return str;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }
}
