package org.chiba.web.flux;

import org.apache.log4j.Logger;
import org.chiba.web.WebAdapter;
import org.chiba.web.servlet.ChibaServlet;
import org.chiba.xml.events.ChibaEventNames;
import org.chiba.xml.events.DOMEventNames;
import org.chiba.xml.events.XFormsEventNames;
import org.chiba.xml.events.XMLEvent;
import org.chiba.xml.xforms.exception.XFormsException;
import org.chiba.adapter.ChibaEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class FluxAdapter extends WebAdapter {

    private static final Logger LOGGER = Logger.getLogger(FluxAdapter.class);

    private EventLog eventLog;

    public FluxAdapter() {
        super();
        this.eventLog = new EventLog();
    }

    public EventLog getEventLog() {
        return this.eventLog;
    }

    /**
     * initialize the Adapter. This is necessary cause often the using
     * application will need to configure the Adapter before actually using it.
     *
     * @throws org.chiba.xml.xforms.exception.XFormsException
     *
     */
    public void init() throws XFormsException {
        super.init();
        if (checkForExitEvent() != null) {
            return;
        }
        this.root.addEventListener(ChibaEventNames.STATE_CHANGED, this, true);
        this.root.addEventListener(ChibaEventNames.PROTOTYPE_CLONED, this, true);
        this.root.addEventListener(ChibaEventNames.ID_GENERATED, this, true);
        this.root.addEventListener(ChibaEventNames.ITEM_INSERTED, this, true);
        this.root.addEventListener(ChibaEventNames.ITEM_DELETED, this, true);
        this.root.addEventListener(ChibaEventNames.INDEX_CHANGED, this, true);
        this.root.addEventListener(ChibaEventNames.SWITCH_TOGGLED, this, true);
        this.root.addEventListener(XFormsEventNames.SUBMIT_ERROR, this, true);
        this.root.addEventListener(ChibaEventNames.SCRIPT_ACTION, this, true);
    }

    /**
     * Dispatch a ChibaEvent to trigger some XForms processing such as updating
     * of values or execution of triggers.
     *
     * @param event an application specific event
     * @throws org.chiba.xml.xforms.exception.XFormsException
     *
     * @see org.chiba.adapter.DefaultChibaEventImpl
     */
    public void dispatch(ChibaEvent event) throws XFormsException {
        super.dispatch(event);
        this.eventLog.flush();
        String targetId = event.getId();
        if (event.getEventName().equalsIgnoreCase(FluxFacade.FLUX_ACTIVATE_EVENT)) {
            chibaBean.dispatch(targetId, DOMEventNames.ACTIVATE);
        } else if (event.getEventName().equalsIgnoreCase("SETINDEX")) {
            int index = Integer.parseInt((String) event.getContextInfo());
            this.chibaBean.updateRepeatIndex(targetId, index);
        } else if (event.getEventName().equalsIgnoreCase("SETVALUE")) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Event contextinfo: " + event.getContextInfo());
            }
            this.chibaBean.updateControlValue(targetId, (String) event.getContextInfo());
        } else if (event.getEventName().equalsIgnoreCase("http-request")) {
            HttpServletRequest request = (HttpServletRequest) event.getContextInfo();
            getHttpRequestHandler().handleRequest(request);
        } else {
            throw new XFormsException("Unknown or illegal event type");
        }
    }

    /**
     * listen to processor and add a DefaultChibaEventImpl object to the
     * EventQueue.
     *
     * @param event the handled DOMEvent
     */
    public void handleEvent(Event event) {
        super.handleEvent(event);
        try {
            if (event instanceof XMLEvent) {
                XMLEvent xmlEvent = (XMLEvent) event;
                String type = xmlEvent.getType();
                if (ChibaEventNames.REPLACE_ALL.equals(type)) {
                    Map submissionResponse = new HashMap();
                    submissionResponse.put("header", xmlEvent.getContextInfo("header"));
                    submissionResponse.put("body", xmlEvent.getContextInfo("body"));
                    this.xformsSession.setProperty(ChibaServlet.CHIBA_SUBMISSION_RESPONSE, submissionResponse);
                    Element target = (Element) event.getTarget();
                    String targetId = target.getAttributeNS(null, "id");
                    String targetName = target.getLocalName();
                    this.eventLog.add(type, targetId, targetName);
                    this.exitEvent = xmlEvent;
                    shutdown();
                    return;
                } else if (ChibaEventNames.LOAD_URI.equals(type)) {
                    String show = (String) xmlEvent.getContextInfo("show");
                    this.eventLog.add(xmlEvent);
                    if ("replace".equals(show)) {
                        this.exitEvent = xmlEvent;
                        shutdown();
                        this.xformsSession.getManager().deleteXFormsSession(this.xformsSession.getKey());
                    }
                    return;
                }
                this.eventLog.add(xmlEvent);
            }
        } catch (Exception e) {
            this.chibaBean.getContainer().handleEventException(e);
        }
    }

    /**
     * terminates the XForms processing. right place to do cleanup of
     * resources.
     *
     * @throws org.chiba.xml.xforms.exception.XFormsException
     *
     */
    public void shutdown() throws XFormsException {
        this.root.removeEventListener(ChibaEventNames.STATE_CHANGED, this, true);
        this.root.removeEventListener(ChibaEventNames.PROTOTYPE_CLONED, this, true);
        this.root.removeEventListener(ChibaEventNames.ID_GENERATED, this, true);
        this.root.removeEventListener(ChibaEventNames.ITEM_INSERTED, this, true);
        this.root.removeEventListener(ChibaEventNames.ITEM_DELETED, this, true);
        this.root.removeEventListener(ChibaEventNames.INDEX_CHANGED, this, true);
        this.root.removeEventListener(ChibaEventNames.SWITCH_TOGGLED, this, true);
        this.root.removeEventListener(XFormsEventNames.SUBMIT_ERROR, this, true);
        this.root.removeEventListener(ChibaEventNames.SCRIPT_ACTION, this, true);
        super.shutdown();
    }
}
