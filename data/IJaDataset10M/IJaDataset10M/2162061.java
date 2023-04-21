package uk.ac.ebi.intact.webapp.search.struts.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.model.BioSource;
import uk.ac.ebi.intact.model.CvObject;
import uk.ac.ebi.intact.model.Interactor;
import uk.ac.ebi.intact.webapp.search.struts.util.SearchConstants;
import uk.ac.ebi.intact.webapp.search.struts.view.beans.BioSourceViewBean;
import uk.ac.ebi.intact.webapp.search.struts.view.beans.CvObjectViewBean;
import uk.ac.ebi.intact.webapp.search.struts.view.beans.InteractorViewBean;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * This Action class performs the construction of view beans that will be used for every  single view for Interactions,
 * Proteins, and CvObjects. It will give back the code to forward to the coresponding JSP site for the representation of
 * the results.
 *
 * @author Michael Kleen
 * @version $Id: SingleResultAction.java 6452 2006-10-16 16:09:42Z baranda $
 */
public class SingleResultAction extends AbstractResultAction {

    private static final Log logger = LogFactory.getLog(SingleResultAction.class);

    /**
     * Used to process a 'single item' view on Interaction, Proteins and CvObjects.
     *
     * @param request  The request object containing the data we want
     * @return String the return code for forwarding use by the execute method
     */
    protected String processResults(HttpServletRequest request) {
        logger.info("Single Result Action");
        Collection results = (Collection) request.getAttribute(SearchConstants.SEARCH_RESULTS);
        if (results.isEmpty()) {
            logger.info("SingleResultAction result is empty");
            return SearchConstants.FORWARD_FAILURE;
        }
        final String searchURL = super.getSearchURL();
        final Object result = results.iterator().next();
        if (Interactor.class.isAssignableFrom(result.getClass())) {
            logger.info("Creating a new InteractorViewBean");
            InteractorViewBean bean = new InteractorViewBean((Interactor) result);
            logger.info("Forward to single Protein View");
            request.getSession().setAttribute(SearchConstants.VIEW_BEAN, bean);
            return SearchConstants.FORWARD_INTERACTOR;
        } else if (BioSource.class.isAssignableFrom(result.getClass())) {
            logger.info("Creating a new BioSourceViewBean");
            BioSourceViewBean bean = new BioSourceViewBean((BioSource) result);
            logger.info("Forward to single BioSource View");
            request.getSession().setAttribute(SearchConstants.VIEW_BEAN, bean);
            return SearchConstants.FORWARD_BIOSOURCE;
        } else if (CvObject.class.isAssignableFrom(result.getClass())) {
            logger.info("Creating a new CvObjectViewBean");
            CvObjectViewBean bean = new CvObjectViewBean((CvObject) result);
            logger.info("Forward to single CvObject View");
            request.getSession().setAttribute(SearchConstants.VIEW_BEAN, bean);
            return SearchConstants.FORWARD_CVOBJECT;
        } else {
            logger.info("wrong result type ");
            logger.info("Forward to Error Page");
            return SearchConstants.FORWARD_FAILURE;
        }
    }
}
