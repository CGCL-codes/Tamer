package org.xulbooster.eclipse.xb.ui.editors.xul.tabletree;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xulbooster.eclipse.xb.ui.editors.xul.internal.properties.EnumeratedStringPropertyDescriptor;

public class DOMPropertyDescriptorFactory {

    protected static final String HACK = "hack";

    public DOMPropertyDescriptorFactory() {
    }

    public IPropertyDescriptor createAttributePropertyDescriptor(Attr attr) {
        IPropertyDescriptor result = null;
        String attributeName = attr.getName();
        ModelQuery mq = ModelQueryUtil.getModelQuery(attr.getOwnerDocument());
        if (mq != null) {
            CMAttributeDeclaration ad = mq.getCMAttributeDeclaration(attr);
            if (ad != null) {
                String[] valuesArray = mq.getPossibleDataTypeValues(attr.getOwnerElement(), ad);
                if ((valuesArray != null) && (valuesArray.length > 0)) {
                    result = new EnumeratedStringPropertyDescriptor(attributeName, attributeName, valuesArray);
                }
            }
        }
        if (result == null) {
            result = createDefaultPropertyDescriptor(attributeName);
        }
        return result;
    }

    public IPropertyDescriptor createCDATASectionPropertyDescriptor(CDATASection cdataSection) {
        return createDefaultPropertyDescriptor(HACK);
    }

    public IPropertyDescriptor createCommentPropertyDescriptor(Comment comment) {
        return createDefaultPropertyDescriptor(HACK);
    }

    protected IPropertyDescriptor createDefaultPropertyDescriptor(String attributeName) {
        TextPropertyDescriptor descriptor = new TextPropertyDescriptor(attributeName, attributeName);
        return descriptor;
    }

    public IPropertyDescriptor createDocumentTypePropertyDescriptor(DocumentType documentType) {
        return null;
    }

    public IPropertyDescriptor createElementPropertyDescriptor(Element element) {
        return createDefaultPropertyDescriptor(HACK);
    }

    public IPropertyDescriptor createEntityReferencePropertyDescriptor(EntityReference entityReference) {
        return createDefaultPropertyDescriptor(HACK);
    }

    public IPropertyDescriptor createProcessingInstructionPropertyDescriptor(ProcessingInstruction pi) {
        return createDefaultPropertyDescriptor(HACK);
    }

    public IPropertyDescriptor createPropertyDescriptor(Object object) {
        IPropertyDescriptor result = null;
        if (object instanceof Node) {
            Node node = (Node) object;
            int nodeType = node.getNodeType();
            switch(nodeType) {
                case Node.ATTRIBUTE_NODE:
                    {
                        result = createAttributePropertyDescriptor((Attr) node);
                        break;
                    }
                case Node.CDATA_SECTION_NODE:
                    {
                        result = createCDATASectionPropertyDescriptor((CDATASection) node);
                        break;
                    }
                case Node.COMMENT_NODE:
                    {
                        result = createCommentPropertyDescriptor((Comment) node);
                        break;
                    }
                case Node.DOCUMENT_TYPE_NODE:
                    {
                        result = createDocumentTypePropertyDescriptor((DocumentType) node);
                        break;
                    }
                case Node.ELEMENT_NODE:
                    {
                        result = createElementPropertyDescriptor((Element) node);
                        break;
                    }
                case Node.ENTITY_REFERENCE_NODE:
                    {
                        result = createEntityReferencePropertyDescriptor((EntityReference) node);
                        break;
                    }
                case Node.PROCESSING_INSTRUCTION_NODE:
                    {
                        result = createProcessingInstructionPropertyDescriptor((ProcessingInstruction) node);
                        break;
                    }
                case Node.TEXT_NODE:
                    {
                        result = createTextPropertyDescriptor((Text) node);
                        break;
                    }
            }
        }
        return result;
    }

    public IPropertyDescriptor createTextPropertyDescriptor(Text text) {
        return createDefaultPropertyDescriptor(HACK);
    }
}
