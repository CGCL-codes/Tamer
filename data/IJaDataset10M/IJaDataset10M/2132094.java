package fi.hip.gb.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fi.hip.gb.portlet.beans.ResultsBrowserResultBean;
import fi.hip.gb.portlet.beans.ResultsBrowserViewBean;
import fi.hip.gb.portlet.results.Result;
import fi.hip.gb.portlet.results.ResultException;
import fi.hip.gb.portlet.results.ResultFindController;

/**
 * TODO: write description for the class
 *
 * @author Antti Ahvenlampi
 * @version $Id: ResultsBrowserPortlet.java 475 2005-08-15 14:14:04Z ahvenlam $
 */
public class ResultsBrowserPortlet extends GenericPortlet {

    private static final String BEAN_PACKAGE = "fi.hip.gb.portlet.beans";

    public static final String VIEW_BEAN = BEAN_PACKAGE + ".ResultsBrowserViewBean";

    public static final String RESULT_BEAN = BEAN_PACKAGE + ".ResultsBrowserResultBean";

    public static final String SESSION_BEAN = BEAN_PACKAGE + ".ResultsBrowserSessionBean";

    private static String VIEW_JSP = null;

    private static String RESULT_JSP = null;

    private static final Log log = LogFactory.getLog(ResultsBrowserPortlet.class);

    /**
	 * @see javax.portlet.GenericPortlet#init()
	 */
    public void init() throws PortletException {
        super.init();
        initEnv();
    }

    /**
	 * @see javax.portlet.Portlet#init(javax.portlet.PortletConfig)
	 */
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        initEnv();
    }

    private void initEnv() {
        VIEW_JSP = getInitParameter("VIEW_JSP");
        RESULT_JSP = getInitParameter("RESULT_JSP");
    }

    /**
	 * Renders the list of the job results
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        if (request.getParameter("jobID") != null) {
            ResultsBrowserResultBean resultBean = new ResultsBrowserResultBean();
            request.setAttribute(RESULT_BEAN, resultBean);
            resultBean.setResult(getResult(request));
            response.setContentType("text/html");
            PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(RESULT_JSP);
            rd.include(request, response);
        } else {
            ResultsBrowserViewBean viewBean = new ResultsBrowserViewBean();
            request.setAttribute(VIEW_BEAN, viewBean);
            viewBean.setResults(getResults(request));
            PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(VIEW_JSP);
            rd.include(request, response);
        }
    }

    /**
	 * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        if (request.getParameter("remove") != null) {
            removeResults(request);
        }
    }

    /**
	 * Use result finders to find all the results available
	 * 
	 * @param request
	 * @return list of the job results
	 */
    private List getResults(PortletRequest request) {
        ResultFindController rfc = getRFC(request);
        List results = null;
        try {
            results = rfc.findResults();
        } catch (ResultException e) {
            log.fatal(e);
            results = new ArrayList();
        }
        return results;
    }

    /**
	 * Use result finders to locate specific result
	 * 
	 * @param request
	 * @return results of the job with specific id
	 */
    private Result getResult(PortletRequest request) {
        ResultFindController rfc = getRFC(request);
        String jobIDString = request.getParameter("jobID");
        Long jobID = new Long(jobIDString);
        return rfc.findResult(jobID);
    }

    /**
	 * Remove results from the server
	 * 
	 * @param request
	 */
    private void removeResults(PortletRequest request) {
        String[] values = request.getParameterValues("checkedResults");
        if (values == null || values.length == 0) return;
        Long[] ids = new Long[values.length];
        for (int i = 0; i < ids.length; i++) ids[i] = new Long(values[i]);
        List results = getResults(request);
        Iterator it = results.iterator();
        while (it.hasNext()) {
            Result result = (Result) it.next();
            for (int i = 0; i < ids.length; i++) {
                if (result.getJobID().equals(ids[i])) {
                    try {
                        result.remove();
                    } catch (ResultException e) {
                        log.warn(e);
                    }
                }
            }
        }
    }

    /**
	 * Just to properly initialize ResultFindController singleton
	 *  
	 * @param request
	 * @return ResultFindController singleton
	 */
    private ResultFindController getRFC(PortletRequest request) {
        ResultFindController rfc = ResultFindController.getInstance();
        if (rfc == null) {
            String[] values = request.getPreferences().getValues("ResultFinderClasses", new String[0]);
            ArrayList classNames = new ArrayList(values.length);
            for (int i = 0; i < values.length; i++) classNames.add(values[i]);
            ResultFindController.init(classNames);
            rfc = ResultFindController.getInstance();
        }
        return rfc;
    }
}
