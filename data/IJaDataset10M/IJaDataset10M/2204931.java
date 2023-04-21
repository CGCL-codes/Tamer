package benchBox.renderers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jdom.Document;
import benchBox.interfaces.rendererInterface;

/**
 * @author ReNo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExistDefaultHTMLpage implements rendererInterface {

    private static final String CONTENT_TYPE = "text/html";

    /**
	   *
	   */
    public ExistDefaultHTMLpage() {
    }

    /**
	   *
	   * @param theSheetKey String
	   */
    public void setStyleSheet(String theSheetKey) {
    }

    /**
	      pass in a document (a jdom)
	   * @param theDocument
	   */
    public void setXmlDoc(Document theDocument) {
    }

    /**
	      pass in the parameters for the style sheet
	   * @param theParameters
	   */
    public void setParameters(HashMap theParameters) {
    }

    /**
	   * take the writer from the response and render the document
	   * @param request
	   * @param response
	   */
    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><body><h1>Dit is de default page. Het verzoek kan niet worden verwerkt.</h1></body></html>");
    }
}
