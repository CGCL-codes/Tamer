package com.sanctuary.products;

import java.io.OutputStream;
import java.io.Writer;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtmlProduct extends BaseProduct {

    private static final Log LOG = LogFactory.getLog(HtmlProduct.class);

    private JspWriter out;

    private Writer output;

    private static String ct = "text/html";

    public HtmlProduct() {
    }

    public String getContentType() {
        return ct;
    }

    @Override
    public void setContentType(String ct) {
    }

    public void print() {
        try {
            if (out == null) LOG.error("out is null, not good");
            if (output == null) LOG.error("output is null, really not good");
            out.print(output.toString());
            out.flush();
            output.flush();
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception ex) {
            }
        }
    }

    public void setOutput(Object output) {
        this.output = (Writer) output;
    }

    public void setOutputStream(OutputStream outputStream) {
    }

    public void setWriter(Writer writer) {
        out = (JspWriter) writer;
    }
}
