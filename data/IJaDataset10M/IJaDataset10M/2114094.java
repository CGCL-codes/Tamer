package it.javalinux.wise.seam.entities.treeElement;

import it.javalinux.wise.converter.code.WiseCodeSnippet;
import it.javalinux.wise.jaxCore.utils.IDGenerator;
import java.lang.reflect.Type;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

/**
 * A simple tree element to handle QName instances.
 * 
 * @author Alessio Soldano, alessio.soldano@javalinux.it
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * 
 */
public class QNameWiseTreeElement extends SimpleWiseTreeElement {

    private static final long serialVersionUID = 1L;

    private String localPart;

    private String nameSpace;

    private QNameWiseTreeElement() {
        this.kind = "qname";
        this.identifier = new Long(IDGenerator.nextVal());
    }

    public QNameWiseTreeElement(Type classType, String name, String localPart, String nameSpace) {
        super();
        this.kind = "qname";
        this.identifier = new Long(IDGenerator.nextVal());
        this.classType = classType;
        this.name = name;
        this.localPart = localPart;
        this.nameSpace = nameSpace;
    }

    @Override
    public String getValue() {
        return "{" + nameSpace + "}" + localPart;
    }

    public Object clone() {
        QNameWiseTreeElement element = new QNameWiseTreeElement();
        element.setName(this.name);
        element.setNil(this.nil);
        element.setClassType(this.classType);
        element.setLocalPart(localPart);
        element.setNameSpace(nameSpace);
        element.setRemovable(this.isRemovable());
        element.setNillable(this.isNillable());
        return element;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getLocalPart() {
        return localPart;
    }

    public void setLocalPart(String localPart) {
        this.localPart = localPart;
    }

    @Override
    public void parseObject(Object obj) {
        if (obj != null) {
            this.localPart = (((QName) obj).getLocalPart());
            this.nameSpace = (((QName) obj).getNamespaceURI());
        } else {
            this.localPart = null;
            this.nameSpace = null;
        }
    }

    @Override
    public Object toObject() {
        return new QName(nameSpace, localPart);
    }

    @Override
    public WiseCodeSnippet toCode(String prefix) {
        WiseCodeSnippet codeSnippet = new WiseCodeSnippet();
        codeSnippet.appendCode("QName " + prefix + this.name + this.getIdentifier() + " = new QName(" + this.nameSpace + "," + this.localPart + ");\n");
        codeSnippet.addImport(QName.class.getName());
        return codeSnippet;
    }
}
