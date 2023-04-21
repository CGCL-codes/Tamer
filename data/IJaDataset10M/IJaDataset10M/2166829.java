package backend.webservice.webinterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ajaxtags.servlets.BaseAjaxServlet;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.query.QueryEnv;

public class HtmlContentServlet extends BaseAjaxServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private QueryEnv qenv = null;

    private Session s = null;

    /**
   * @see org.ajaxtags.demo.servlet.BaseAjaxServlet#getXmlContent(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
    public String getXmlContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fieldname = request.getParameter("make");
        AbstractONDEXGraph aog = (AbstractONDEXGraph) request.getAttribute("graph");
        Session s = (Session) request.getAttribute("session");
        System.out.println(s);
        System.out.println(aog);
        StringBuilder html = new StringBuilder();
        int num = 3;
        if (fieldname.equals("Concept")) {
            for (int i = 2; i <= num; i++) {
                html.append("<input type='hidden' name='Concept' value='Concept' id='Concept' />");
                html.append("Search for :");
                html.append("<input type='text' name='textarea" + i + "' />");
                html.append("in field:&nbsp;");
                html.append("<select name='make" + i + "' id='make" + i + "' value='+'>");
                html.append("<option value='ConceptName'> ConceptName</option>");
                html.append("<option value='ConceptDescription'> ConceptDescription</option>");
                html.append("<option value='ConceptAnnotation'> ConceptAnnotation</option>");
                html.append("<option value='ConceptID'> ConceptID</option>");
                html.append("<option value='ConceptAccession'> ConceptAccession</option>");
                html.append("<option value='ConceptGDS'> ConceptGDS</option>");
                html.append("<option value='CV'> CV</option>");
                html.append("<option value='ConceptClass'> ConceptClass</option>");
                html.append("</select>&nbsp;");
                html.append("<input type='radio' name='and_or" + (i + 1) + "' value='and' />and &nbsp;");
                html.append("<input type='radio' name='and_or" + (i + 1) + "' value='or' />or");
                html.append("</div>");
                html.append("<input type='hidden' name='field" + i + "' value='Concept'/>");
                html.append("<br/>");
                html.append("<input type='hidden' id='number' name='number' value='" + num + "'/>");
            }
        }
        if (fieldname.equals("Relation")) {
            for (int i = 2; i <= num; i++) {
                html.append("<input type='hidden' name='Relation' value='Relation' id='Relation' />");
                html.append("Search for :");
                html.append("<input type='text' name='textarea" + i + "' />");
                html.append("in field:&nbsp;");
                html.append("<select name='make" + i + "' id='make" + i + "' value='+'>");
                html.append("<option value='FromConcept'> FromConcept</option>");
                html.append("<option value='ToConcept'> ToConcept</option>");
                html.append("<option value='Qualifier'> Qualifier</option>");
                html.append("<option value='OfTypeSet'> OfTypeSet</option>");
                html.append("<option value='RelationGDS'> RelationGDS</option>");
                html.append("<option value='ConceptGDS'> ConceptGDS</option>");
                html.append("</select>&nbsp;");
                html.append("<input type='radio' name='and_or" + (i + 1) + "' value='and' />and &nbsp;");
                html.append("<input type='radio' name='and_or" + (i + 1) + "' value='or' />or");
                html.append("</div>");
                html.append("<input type='hidden' name='field" + i + "' value='Relation'/>");
                html.append("<br/>");
                html.append("<input type='hidden' id='number' name='number' value='" + num + "'/>");
            }
        } else {
            html.append("<b> Insert a sequence</b><br/>");
            html.append("<input type='textarea' name ='testa'/>");
            html.append("<br/> <br/><b>");
            html.append("or load a flat file </b> <br/>");
            html.append("<input type='file' name='file' alt='' />");
        }
        return html.toString();
    }

    public void setValues(QueryEnv qenv, Session s) {
        this.qenv = qenv;
        this.s = s;
    }
}
