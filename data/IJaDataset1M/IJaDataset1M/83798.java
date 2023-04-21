package si.cit.eprojekti.enews.controller.newsStates;

import java.util.Vector;
import si.cit.eprojekti.enews.dbobj.News;
import si.cit.eprojekti.enews.util.ComponentSecurityManager;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.ErrorCollection;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.misc.DateTime;
import org.apache.log4j.Category;

/**
 * @author polancic
 * 
 * This state changes news status from approved to dissapproved and opposite
 * 
 */
public class ApprovedNewsState extends State {

    private static final long serialVersionUID = -717729524307195364L;

    private static Category log = Category.getInstance("enews.state");

    /**
	 * 
	 */
    public ApprovedNewsState() {
        super();
    }

    public ApprovedNewsState(String stateName, String descrip) {
        super(stateName, descrip);
    }

    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        ErrorCollection errors = new ErrorCollection();
        try {
            if (ComponentSecurityManager.checkSecurityForProject(request.getParameter("projectId"), request.getParameter("category"), request.getUid()) < ComponentSecurityManager.ACCESS_ABSOLUTE) throw ComponentSecurityManager.accessDeniedOccured(response);
            News newsDetail = new News();
            newsDetail.setField("NewsId", request.getParameter("key"));
            newsDetail.retrieve();
            newsDetail.setLocale(request.getLocale());
            Output approvedStatus = new Output();
            approvedStatus.setName("ApprovedStatus");
            approvedStatus.setLabel("Approved Status");
            approvedStatus.setAttribute("NewsId", newsDetail.getField("NewsId"));
            approvedStatus.setAttribute("FileId", newsDetail.getField("FileId"));
            Vector validValues = newsDetail.getValidValues("FileId");
            Vector validKeys = new Vector();
            for (int i = 0; i < validValues.size(); i++) {
                validKeys.add(((ValidValue) validValues.elementAt(i)).getKey());
            }
            approvedStatus.setAttribute("ValidKeys", validKeys.toString());
            if (!validKeys.contains(newsDetail.getField("FileId"))) {
                newsDetail.setField("FileId", "0");
                approvedStatus.setAttribute("FileIdChanged", "true");
            } else {
                approvedStatus.setAttribute("FileIdChanged", "false");
            }
            if (newsDetail.getField("NewsApproved").equalsIgnoreCase("T")) {
                newsDetail.setField("NewsApproved", "F");
                approvedStatus.setContent("Approved status changed from \"approved\" to \"dissapproved\" ");
            } else if (newsDetail.getField("NewsApproved").equalsIgnoreCase("F")) {
                newsDetail.setField("NewsApproved", "T");
                approvedStatus.setContent("Approved status changed from \"dissapproved\" to \"approved\" ");
            }
            newsDetail.setField("DateApproved", DateTime.getDateTimeForDB());
            newsDetail.setField("ApprovedUid", request.getUid());
            newsDetail.setField("DateUpdated", DateTime.getDateTimeForDB());
            newsDetail.setField("UpdatedUid", request.getUid());
            newsDetail.update();
            response.addOutput(approvedStatus);
        } catch (Exception e) {
            errors.addError("Med delovanjem aplikacije eNews je pri&#353;lo do napake");
            log.warn("Med delovanjem aplikacije eNews je pri&#353;lo do napake");
            log.error("Napaka: " + e.getMessage());
        } finally {
            if (errors.getErrorCount() > 0) {
                response.saveErrors(errors);
                transition("listNewsCriteriaState", request, response);
            }
            if (errors.getErrorCount() < 1) {
                response.clearFormCache();
                Transition listNewsCriteria = new Transition();
                listNewsCriteria.setName("listNewsCriteria");
                listNewsCriteria.setLabel("Naprej");
                listNewsCriteria.setControllerObject(this.getController().getClass());
                listNewsCriteria.setState("listNewsState");
                response.add(listNewsCriteria);
            }
        }
    }
}
