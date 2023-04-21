package benchBox.renderers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import org.jdom.Document;
import org.jdom.output.Format;
import benchBox.BenchStylesheetCache;
import benchBox.interfaces.rendererInterface;

public class HTMLRenderer implements rendererInterface {

    /**
     * 
     * @uml.property name="stylesheetCache"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    BenchStylesheetCache stylesheetCache = new BenchStylesheetCache();

    public HTMLRenderer() {
    }

    private String stylesheetKey;

    private Document document;

    private HashMap parameterMap = null;

    /**
    pass in a pre-compiled style sheet
   * @param theSheet
   */
    public void setStyleSheet(String theSheetKey) {
        stylesheetKey = theSheetKey;
    }

    /**
    pass in a document (a jdom)
   * @param theDocument
   */
    public void setXmlDoc(Document theDocument) {
        document = theDocument;
    }

    /**
    pass in the parameters for the style sheet
   * @param theParameters
   */
    public void setParameters(HashMap theParameters) {
        parameterMap = theParameters;
    }

    /**
   * Render de uitvoer
   * @param request
   * @param response
   */
    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            javax.xml.transform.Result output = new javax.xml.transform.stream.StreamResult(writer);
            javax.xml.transform.Transformer transForm = BenchStylesheetCache.newTransformer(stylesheetKey);
            transForm.transform(JDOM2Source(this.document), output);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
   *
   * @param Jdoc Document
   * @return Source
   */
    private javax.xml.transform.Source JDOM2Source(Document Jdoc) {
        try {
            StringWriter sw = new StringWriter();
            org.jdom.output.XMLOutputter xmlout = new org.jdom.output.XMLOutputter(Format.getPrettyFormat());
            xmlout.output(Jdoc, sw);
            StringReader sr = new StringReader(sw.toString());
            Source xmlSource = new javax.xml.transform.stream.StreamSource(sr);
            return xmlSource;
        } catch (Exception e) {
        }
        return null;
    }
}
