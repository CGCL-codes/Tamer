package net.sf.komodo.lang.jsp.ir.jsp;

import org.apache.jasper.JasperException;
import net.sf.komodo.lang.jsp.ir.IRMark;

/**
 * Represents the root of a Jsp page or Jsp document
 */
public class IRRoot extends IRNode {

    private IRRoot parentRoot;

    private boolean isXmlSyntax;

    private String pageEnc;

    private String jspConfigPageEnc;

    private boolean isDefaultPageEncoding;

    private boolean isEncodingSpecifiedInProlog;

    public IRRoot(IRMark start, IRNode parent, boolean isXmlSyntax) {
        super(start, parent);
        this.isXmlSyntax = isXmlSyntax;
        this.qName = JSP_ROOT_ACTION;
        this.localName = ROOT_ACTION;
        IRNode r = parent;
        while ((r != null) && !(r instanceof IRRoot)) r = r.getParent();
        parentRoot = (IRRoot) r;
    }

    public void accept(IRVisitorInterface v) throws JasperException {
        v.visit(this);
    }

    public boolean isXmlSyntax() {
        return isXmlSyntax;
    }

    public void setJspConfigPageEncoding(String enc) {
        jspConfigPageEnc = enc;
    }

    public String getJspConfigPageEncoding() {
        return jspConfigPageEnc;
    }

    public void setPageEncoding(String enc) {
        pageEnc = enc;
    }

    public String getPageEncoding() {
        return pageEnc;
    }

    public void setIsDefaultPageEncoding(boolean isDefault) {
        isDefaultPageEncoding = isDefault;
    }

    public boolean isDefaultPageEncoding() {
        return isDefaultPageEncoding;
    }

    public void setIsEncodingSpecifiedInProlog(boolean isSpecified) {
        isEncodingSpecifiedInProlog = isSpecified;
    }

    public boolean isEncodingSpecifiedInProlog() {
        return isEncodingSpecifiedInProlog;
    }

    /**
     * @return The enclosing root to this Root. Usually represents the
     * page that includes this one.
     */
    public IRRoot getParentRoot() {
        return parentRoot;
    }

    public String print(int level) {
        String ret = printLevelGap(level, "");
        ret += "<ir-root>\n";
        ret += this.body.print(level + 1);
        ret += "</ir-root>\n";
        return ret;
    }
}
