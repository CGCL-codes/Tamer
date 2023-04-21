package org.fspmboard.client.mvc;

import org.fspmboard.client.AppEvents;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

public class ContentController extends Controller {

    private ContentView view;

    public ContentController() {
        registerEventTypes(AppEvents.Init, AppEvents.ShowModule);
        registerEventTypes(AppEvents.HidePage);
        registerEventTypes(AppEvents.LoggedIn);
        registerEventTypes(AppEvents.Logout);
    }

    public void initialize() {
        view = new ContentView(this);
    }

    public void handleEvent(AppEvent<?> event) {
        forwardToView(view, event);
    }
}
