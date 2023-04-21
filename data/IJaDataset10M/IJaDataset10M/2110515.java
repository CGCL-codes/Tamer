package si.unimb.isportal07.iiNews.controller.newsStates;

import java.util.ArrayList;
import java.util.Iterator;
import si.unimb.isportal07.iiChat.dbobj.UserInfo;
import si.unimb.isportal07.iiFaq.dbobj.Faqs;
import si.unimb.isportal07.iiIskalnik.iskanje.Vnasalec;
import si.unimb.isportal07.iiNews.controller.NewsController;
import si.unimb.isportal07.iiNews.dbobj.Newss;
import si.unimb.isportal07.iiNews.controller.newsStates.DeleteNewsState;
import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.services.dbobj.DefaultUserInfo;

public class UpdateNewsState extends State {

    static final long serialVersionUID = 1L;

    public static final String STATE_NAME = "updateNewsState";

    public UpdateNewsState() {
        super(UpdateNewsState.STATE_NAME, UpdateNewsState.STATE_NAME + "Description");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(NewsController.class);
            errorTrans.setState(ErrorNewsState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            Newss newsDB = new Newss();
            newsDB.setField(Newss.NEWS_ID, request.getParameter(Newss.NEWS_ID));
            newsDB.retrieve();
            newsDB.setField(Newss.TITLE, request.getParameter("titleInput"));
            newsDB.setField(Newss.CONTENT, request.getParameter("contentInput"));
            newsDB.setField(Newss.CATEGORY_ID, request.getParameter("categoryInput"));
            newsDB.setField(Newss.ASSES_ID, request.getParameter("assesInput"));
            newsDB.setField(Newss.DESCRIPTION, request.getParameter("descriptionInput"));
            newsDB.update();
            newsDB.setField(Newss.NEWS_ID, request.getParameter(Newss.NEWS_ID));
            newsDB.retrieve();
            Output titleOutput = new Output();
            titleOutput.setName("titleInput");
            titleOutput.setContent(newsDB.getField(Newss.TITLE));
            titleOutput.setLabel(getString("vnos_novice"));
            response.add(titleOutput);
            String ID = newsDB.getField(Newss.NEWS_ID);
            String indexUrl = "/iiNews/News.do?NewsId=" + ID + "&state=detailNewsState";
            Vnasalec.indeksiraj(indexUrl, newsDB.getField(Newss.TITLE), newsDB.getField(Newss.CONTENT), "");
            Output contentOutput = new Output("contentInput");
            contentOutput.setName("contentInput");
            contentOutput.setContent(newsDB.getField(Newss.CONTENT));
            contentOutput.setLabel(getString("vsebina"));
            response.add(contentOutput);
            Output categoryOutput = new Output();
            categoryOutput.setName("categoryInput");
            categoryOutput.setContent(newsDB.getField(Newss.CATEGORY_ID));
            categoryOutput.setLabel(getString("kategorija_novice"));
            response.add(categoryOutput);
            Output assesOutput = new Output();
            assesOutput.setName("assesInput");
            assesOutput.setContent(newsDB.getField(Newss.ASSES_ID));
            assesOutput.setLabel(getString("ocena_novice"));
            response.add(assesOutput);
            Output descriptionOutput = new Output();
            descriptionOutput.setName("descriptionInput");
            descriptionOutput.setContent(newsDB.getField(Newss.DESCRIPTION));
            descriptionOutput.setLabel(getString("vnos_povzetka"));
            response.add(descriptionOutput);
            Transition detailNewsTransition = new Transition();
            detailNewsTransition.setControllerObject(NewsController.class);
            detailNewsTransition.setState(DetailNewsState.STATE_NAME);
            detailNewsTransition.setName("detailNewsTransition");
            detailNewsTransition.setLabel("Nazaj na podrobnosti");
            detailNewsTransition.addParam(Newss.NEWS_ID, request.getParameter(Newss.NEWS_ID));
            response.add(detailNewsTransition);
            Transition detailNewsTransition_advanced = new Transition();
            detailNewsTransition_advanced.setControllerObject(NewsController.class);
            detailNewsTransition_advanced.setState(DetailNewsState.STATE_NAME + "&style=detailNewsState_advanced");
            detailNewsTransition_advanced.setName("detailNewsTransition_advanced");
            detailNewsTransition_advanced.setLabel("Nazaj na podrobnosti");
            detailNewsTransition_advanced.addParam(Newss.NEWS_ID, request.getParameter(Newss.NEWS_ID));
            response.add(detailNewsTransition_advanced);
        } catch (Exception e) {
        } finally {
        }
    }
}
