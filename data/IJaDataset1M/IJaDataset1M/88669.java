package controllers;

import framework.Framework;
import stream.StreamBlockReader;
import views.GetSourceView;
import application.Controller;
import application.EventManager;
import application.View;

public class DecryptStreamGetSourceController extends Controller {

    public DecryptStreamGetSourceController(EventManager eventmanager, Controller previous) {
        super(eventmanager, previous);
    }

    public View getView() {
        return new GetSourceView();
    }

    public void dispatch(String inputstring) {
        if (inputstring.equals("0")) {
            this.eventmanager.setController(this.previous);
            return;
        }
        try {
            this.eventmanager.setStreamBlockReader(new StreamBlockReader(Framework.getInstance().getInputStream(inputstring)));
            this.eventmanager.setController(new DecryptStreamGetDestinationController(this.eventmanager, this));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
