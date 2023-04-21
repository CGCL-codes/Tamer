package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	UpdateTest2.java
 * 
 * Description 	: 	Servlet that displays an input form for updating
 * 					a TPTeam Test by selecting it from the Test tree
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateTest2 extends ServletUtil {

    private static final long serialVersionUID = 7456848419577223441L;

    protected String mProjID = null;

    protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateTest3\">\n";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
	 * Gets the ID of the Project selected and renders test tree for
	 * Test selection
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            mProjID = req.getParameter("projId");
            getPage(req, resp);
        } catch (Exception e) {
            StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>" + e.getCause() + "</h3>");
            throwError(req, resp, error, this);
            return;
        }
    }

    /**
	 * Helper method for rendering a Project's Test tree
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
    protected void getPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
        StringBuffer reply = new StringBuffer();
        reply.append("<h4>Select a Folder or Test, then Click the Update Button</h4>\n");
        reply.append("<form method=\"post\" onSubmit=\"return validateForm(this);\">\n");
        reply.append("<table border=\"1\"><tr><td>\n");
        reply.append(ServletUtil.getTestTree(mProjID, false));
        reply.append(mFormAction);
        reply.append("<input type=\"hidden\" name=\"testID\" value=\"\">\n</td></tr></table><p>\n<input type=\"submit\" value=\"Update\">\n</form>\n");
        showPage(req, resp, reply, ServletUtil.UPDATE_TEST_TREE_JS + ServletUtil.ADD_TEST_TREE_JS + ServletUtil.ADD_TEST_TREE_CSS, this);
    }
}
