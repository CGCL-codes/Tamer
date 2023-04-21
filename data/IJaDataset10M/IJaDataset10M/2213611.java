package net.sf.komodo.lang.jsp.ir.jsp;

import org.apache.jasper.JasperException;
import org.xml.sax.Attributes;
import net.sf.komodo.lang.jsp.ir.IRMark;

/**
 * Represents a <jsp:element>.
 */
public class IRJspElement extends IRNode {

    private IRJspAttribute[] jspAttrs;

    private IRJspAttribute nameAttr;

    public IRJspElement(Attributes attrs, IRMark start, IRNode parent) {
        this(JSP_ELEMENT_ACTION, attrs, null, null, start, parent);
    }

    public IRJspElement(String qName, Attributes attrs, Attributes nonTaglibXmlnsAttrs, Attributes taglibAttrs, IRMark start, IRNode parent) {
        super(qName, ELEMENT_ACTION, attrs, nonTaglibXmlnsAttrs, taglibAttrs, start, parent);
    }

    public void accept(IRVisitorInterface v) throws JasperException {
        v.visit(this);
    }

    public void setJspAttributes(IRJspAttribute[] jspAttrs) {
        this.jspAttrs = jspAttrs;
    }

    public IRJspAttribute[] getJspAttributes() {
        return jspAttrs;
    }

    public void setNameAttribute(IRJspAttribute nameAttr) {
        this.nameAttr = nameAttr;
    }

    public IRJspAttribute getNameAttribute() {
        return this.nameAttr;
    }

    public String print(int level) {
        return null;
    }
}
