package org.corrib.s3b.sscf.tools;

import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foafrealm.db.DbFace;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Statement;

/**
 * @author adagze
 *
 */
public class RewriteRepository extends HttpServlet {

    /**
	 * generated
	 */
    private static final long serialVersionUID = 3736160348884462553L;

    /**
	 * @param request  
	 */
    protected void process(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        long statementsChanged = 0;
        try {
            ModelSet model = DbFace.getModel();
            Model modelNew = DbFace.getTempModel();
            Iterator<Statement> it = model.findStatements(null, null, null, null);
            while (it.hasNext()) {
                Statement st = it.next();
                modelNew.addStatement(st);
                statementsChanged++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.getWriter().write("--- Conversion report ---<br>");
            response.getWriter().write("<br>** Overall number of statements moved: " + statementsChanged);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        process(arg0, arg1);
    }

    @Override
    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        process(arg0, arg1);
    }
}
