package com.watchthelan.web.schedule.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.watchthelan.domain.Event;
import com.watchthelan.service.schedule.event.EventManager;

public class EventsController implements Controller {

    private final Log logger = LogFactory.getLog(this.getClass());

    private EventManager eventManager;

    @Override
    public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        Map<String, Object> myModel = new HashMap<String, Object>();
        List<Event> events = this.eventManager.getEvents();
        myModel.put("events", this.eventManager.getEvents());
        return new ModelAndView("events", "model", myModel);
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
