package cn.vlabs.duckling.dct.services.dml.html2dml;

import java.io.PrintWriter;
import java.io.Writer;
import org.jdom.Element;

/**
 * Introduction Here.
 * @date 2010-3-8
 * @author 狄
 */
public abstract class AbstractParseHtmlElement {

    private Writer m_outTimmer = new WhitespaceTrimWriter();

    private PrintWriter m_out = new PrintWriter(m_outTimmer);

    Html2Dml h2d = new Html2Dml();

    public abstract void printElement(Element e, Html2DmlEngine html2dmlengine);

    public abstract void printAttribute(Element e, Html2DmlEngine html2dmlengine);

    public Writer getM_outTimmer() {
        return m_outTimmer;
    }

    public void setM_outTimmer(Writer timmer) {
        m_outTimmer = timmer;
    }

    public PrintWriter getM_out() {
        return m_out;
    }

    public void setM_out(PrintWriter m_out) {
        this.m_out = m_out;
    }
}
