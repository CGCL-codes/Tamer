package si.cit.eprojekti.evocabulary.controller.managestates;

import java.util.Hashtable;
import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.i18n.Messages;
import si.cit.eprojekti.evocabulary.VocabularySchema;
import si.cit.eprojekti.evocabulary.controller.BrowseController;
import si.cit.eprojekti.evocabulary.controller.browsestates.ErrorState;
import si.cit.eprojekti.evocabulary.dbobj.*;
import si.cit.eprojekti.evocabulary.util.ComponentSecurityManager;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.7.21 11:07:41
 * 
 * CommentTermState description:
 * 	Add comments to term
 */
public class CommentTermState extends State {

    private static final long serialVersionUID = 23134546488887987L;

    /**
	 * States constructor
	 */
    public CommentTermState() {
        super("CommentTermState", "CommentTermStateDescription");
        addRequiredParameter("term");
        addRequiredParameter("category");
        addRequiredParameter("comment");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        Transition errorTrans = new Transition();
        errorTrans.setControllerObject(VocabularySchema.class);
        errorTrans.setState(ErrorState.STATE_NAME);
        errorTrans.setName("errorTrans");
        setErrorTransition(errorTrans);
        try {
            TermQuickHolder tqh = Term.getTerm(Integer.parseInt(request.getParameter("term")));
            if (ComponentSecurityManager.getAccessOnCategory(Integer.parseInt(request.getParameter("category")), request.getUid()) < ComponentSecurityManager.ACCESS_MEMBER) throw ComponentSecurityManager.accessDeniedOccured(response);
            Comment comment = new Comment();
            comment.setField("Term", tqh.ID);
            comment.setField("Description", request.getParameter("comment"));
            comment.add(request.getUid());
            Output termName = new Output("termName");
            termName.setName("CommentTermTermNameOutput");
            termName.setLabel("CommentTermTermNameOutput");
            termName.setContent(tqh.name);
            response.addOutput(termName);
            Hashtable transitionParams = new Hashtable();
            transitionParams.put("term", tqh.ID + "");
            transitionParams.put("category", request.getParameter("category"));
            Transition trans = new Transition("transitionSeeTerm", Messages.getString(VocabularySchema.class.getName(), request.getLocale(), "SeeTermTransition"), BrowseController.class, "ShowTermDetailsState");
            trans.setParams(transitionParams);
            response.addTransition(trans);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (VocabularySchema.standardLog.isEnabledFor(Priority.WARN)) VocabularySchema.standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (VocabularySchema.debugLog.isDebugEnabled()) VocabularySchema.debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (VocabularySchema.observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                VocabularySchema.observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
