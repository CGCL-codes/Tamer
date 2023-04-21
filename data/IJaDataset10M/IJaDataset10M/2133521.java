package balmysundaycandy.marble.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class TimelineController extends Controller {

    @Override
    protected Navigation run() {
        return forward("WEB-INF/jsp/timeline.jsp");
    }
}
