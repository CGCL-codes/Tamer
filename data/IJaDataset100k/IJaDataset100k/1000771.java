package si.cit.eprojekti.eprocess.controller.browsestate;

import java.util.Hashtable;
import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.DBException;
import si.cit.eprojekti.eprocess.ProcessSchema;
import si.cit.eprojekti.eprocess.controller.BrowseController;
import si.cit.eprojekti.eprocess.controller.WorkflowController;
import si.cit.eprojekti.eprocess.util.ComponentSecurityManager;
import si.cit.eprojekti.eprocess.util.link.ProcessFacade;
import si.cit.eprojekti.projectvianet.util.link.ICategory;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.7.19 15:58:29
 * 
 * ShowProjectState description:
 * 	Show categories from specified project
 */
public class ShowProjectState extends State {

    private static final long serialVersionUID = 23134546488887987L;

    /**
	 * Constructor
	 */
    public ShowProjectState() {
        super("ShowProjectState", "ShowProjectStateDescription");
        addRequiredParameter("projectId");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            int project = Integer.parseInt(request.getParameter("projectId"));
            Block nav = new Block("navigationBarBlock");
            response.add(nav);
            ICategory[] superCategories = (new ProcessFacade()).getTopLevelProjectCategories(project);
            for (int i = 0; i < superCategories.length; i++) {
                Block navBlock = new Block("oneCategoryNavigation");
                nav.add(navBlock);
                navBlock.addNested(new Output("name", superCategories[i].getCategoryName()));
                navBlock.addNested(new Output("description", superCategories[i].getCategoryDescription()));
                navBlock.addNested(new Output("id", superCategories[i].getCategoryID() + ""));
                Hashtable transitionParams = new Hashtable();
                transitionParams.put("categoryId", superCategories[i].getCategoryID() + "");
                transitionParams.put("projectId", request.getParameter("projectId"));
                Transition browseCategoryTrans = new Transition("transitionShowCategory", "transitionShowCategory", BrowseController.class, "ShowCategoriesState");
                browseCategoryTrans.setParams(transitionParams);
                navBlock.add(browseCategoryTrans);
            }
            Transition viewMyRespTrans = new Transition("viewMyRespTrans", "viewMyRespTrans", WorkflowController.class, "ViewMyResponsibilitiesState");
            viewMyRespTrans.addParam("projectId", request.getParameter("projectId"));
            response.add(viewMyRespTrans);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (ProcessSchema.standardLog.isEnabledFor(Priority.WARN)) ProcessSchema.standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (ProcessSchema.debugLog.isDebugEnabled()) ProcessSchema.debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (ProcessSchema.observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                ProcessSchema.observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
