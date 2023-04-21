package com.luxx.acms.gwt.client.ui.administration;

import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.luxx.acms.gwt.client.ui.ACMSEvents;
import com.luxx.acms.gwt.client.ui.administration.member.MemberController;
import com.luxx.acms.gwt.client.ui.administration.member.MemberMasterDetailController;
import com.luxx.acms.gwt.client.ui.administration.plane.PlaneController;
import com.luxx.acms.gwt.client.ui.administration.plane.PlaneMasterDetailController;
import com.luxx.acms.gwt.client.ui.administration.news.NewsController;
import com.luxx.acms.gwt.client.ui.administration.news.NewsMasterDetailController;
import lu.vdl.vdlfmk.gwt.client.ui.CommonEvents;
import lu.vdl.vdlfmk.gwt.client.ui.IView;

/**
 * Created by IntelliJ IDEA.
 * User: rreckel
 * Date: Jun 5, 2008
 * Time: 2:41:55 PM
 */
public class AdministrationPerspectiveController extends Controller {

    private AdministrationPerspectiveView administrationPerspectiveView;

    private NewsController newsController;

    private NewsMasterDetailController newsMasterDetailController;

    private MemberController memberController;

    private MemberMasterDetailController memberMasterDetailController;

    private PlaneController planeController;

    private PlaneMasterDetailController planeMasterDetailController;

    public AdministrationPerspectiveController() {
        registerEventTypes(CommonEvents.Init, ACMSEvents.NewsMenuSelected, ACMSEvents.MembersMenuSelected, ACMSEvents.PlanesMenuSelected);
        Dispatcher dispatcher = Dispatcher.get();
        newsController = new NewsController();
        dispatcher.addController(newsController);
        newsMasterDetailController = new NewsMasterDetailController(newsController);
        dispatcher.addController(newsMasterDetailController);
        memberController = new MemberController();
        dispatcher.addController(memberController);
        memberMasterDetailController = new MemberMasterDetailController(memberController);
        dispatcher.addController(memberMasterDetailController);
        planeController = new PlaneController();
        dispatcher.addController(planeController);
        planeMasterDetailController = new PlaneMasterDetailController(planeController);
        dispatcher.addController(planeMasterDetailController);
    }

    public void handleEvent(AppEvent event) {
        if (event.type == CommonEvents.Init) {
            forwardToView(administrationPerspectiveView, event);
        }
        if (event.type == ACMSEvents.NewsMenuSelected) {
            newsMasterDetailController.handleEvent(event);
            forwardToView(administrationPerspectiveView, new AppEvent<IView>(CommonEvents.Selected, newsMasterDetailController.getView()));
        }
        if (event.type == ACMSEvents.MembersMenuSelected) {
            memberMasterDetailController.handleEvent(event);
            forwardToView(administrationPerspectiveView, new AppEvent<IView>(CommonEvents.Selected, memberMasterDetailController.getView()));
        }
        if (event.type == ACMSEvents.PlanesMenuSelected) {
            planeMasterDetailController.handleEvent(event);
            forwardToView(administrationPerspectiveView, new AppEvent<IView>(CommonEvents.Selected, planeMasterDetailController.getView()));
        }
    }

    public void initialize() {
        administrationPerspectiveView = new AdministrationPerspectiveView(this);
    }
}
