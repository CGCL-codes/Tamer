package com.lowagie.text.xml;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlWriter;

/**
 * HTML-specific subclass of <code>XmlToXXX</code>.
 *
 * @version 1.0
 * @author <a href="mailto:orangeherbert@users.sourceforge.net">Matt Benson</a>
 */
public class XmlToHtml extends XmlToXXX {

    /**
 * Construct an <CODE>XmlToHtml</CODE> with the default page size.
 */
    public XmlToHtml() {
        super();
    }

    /**
 * Construct an <CODE>XmlToHtml</CODE> with the specified page size.
 * @param pageSize   <CODE>String</CODE> page size name from
 * <CODE>com.lowagie.text.PageSize</CODE>.
 */
    public XmlToHtml(String pageSize) {
        super(pageSize);
    }

    /**
 * Add a <CODE>DocWriter</CODE> for the specified <CODE>Document</CODE> and
 * <CODE>OutputStream</CODE>.
 * @throws DocumentException if document errors occur.
 */
    protected final void addWriter(Document doc, OutputStream out) throws DocumentException {
        HtmlWriter.getInstance(doc, out);
    }

    /**
 * Main method of the <CODE>XmlToHtml</CODE> class.
 * @param args   <CODE>String[]</CODE> of command-line arguments.
 */
    public static void main(String[] args) {
        int code = 0;
        if (args.length > 1) {
            try {
                XmlToHtml x;
                if (args.length > 2) {
                    x = new XmlToHtml(args[2]);
                } else {
                    x = new XmlToHtml();
                }
                x.parse(new FileInputStream(args[0]), new FileOutputStream(args[1]));
            } catch (Exception ex) {
                code = 2;
                ex.printStackTrace(System.err);
            }
        } else {
            code = 1;
            System.err.println("Usage:  XmlToHtml [XML file in] [PDF file out] [optional page size]");
        }
        System.exit(code);
    }
}
