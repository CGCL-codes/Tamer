package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.WorkItemSolexaRunParser;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class SaveWorkItemSolexaRun extends GNomExCommand implements Serializable {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemSolexaRun.class);

    private String workItemXMLString;

    private Document workItemDoc;

    private WorkItemSolexaRunParser parser;

    private DictionaryHelper dictionaryHelper;

    private String appURL;

    private String serverName;

    private Map confirmedRequestMap = new HashMap();

    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {
        if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
            workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
            StringReader reader = new StringReader(workItemXMLString);
            try {
                SAXBuilder sax = new SAXBuilder();
                workItemDoc = sax.build(reader);
                parser = new WorkItemSolexaRunParser(workItemDoc);
            } catch (JDOMException je) {
                log.error("Cannot parse workItemXMLString", je);
                this.addInvalidField("WorkItemXMLString", "Invalid work item xml");
            }
        }
        try {
            appURL = this.getLaunchAppURL(request);
        } catch (Exception e) {
            log.warn("Cannot get launch app URL in SaveRequest", e);
        }
        serverName = request.getServerName();
    }

    public Command execute() throws RollBackCommandException {
        if (workItemXMLString != null) {
            try {
                Session sess = HibernateSession.currentSession(this.getUsername());
                if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
                    parser.parse(sess);
                    for (Iterator i = parser.getWorkItems().iterator(); i.hasNext(); ) {
                        WorkItem workItem = (WorkItem) i.next();
                        FlowCellChannel channel = (FlowCellChannel) parser.getFlowCellChannel(workItem.getIdWorkItem());
                        if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_ON_HOLD)) {
                            continue;
                        } else if (workItem.getStatus() != null && workItem.getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
                            continue;
                        }
                        if (channel.getLastCycleDate() != null || (channel.getLastCycleFailed() != null && channel.getLastCycleFailed().equals("Y"))) {
                            sess.delete(workItem);
                            if (channel.getLastCycleDate() != null) {
                                WorkItem wi = new WorkItem();
                                wi.setCodeStepNext(workItem.getCodeStepNext().equals(Step.SEQ_RUN) ? Step.SEQ_DATA_PIPELINE : Step.HISEQ_DATA_PIPELINE);
                                wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                                wi.setFlowCellChannel(channel);
                                sess.save(wi);
                            }
                        }
                        if (channel.getSequenceLanes() != null) {
                            for (Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext(); ) {
                                SequenceLane lane = (SequenceLane) i1.next();
                                Request request = (Request) sess.load(Request.class, lane.getIdRequest());
                                if (request.isConsideredFinished() && request.getCompletedDate() == null) {
                                    request.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
                                }
                            }
                        }
                    }
                    sess.flush();
                    parser.resetIsDirty();
                    XMLOutputter out = new org.jdom.output.XMLOutputter();
                    this.xmlResult = out.outputString(workItemDoc);
                    setResponsePage(this.SUCCESS_JSP);
                } else {
                    this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
                    setResponsePage(this.ERROR_JSP);
                }
            } catch (Exception e) {
                log.error("An exception has occurred in SaveWorkItemSolexaRun ", e);
                e.printStackTrace();
                throw new RollBackCommandException(e.getMessage());
            } finally {
                try {
                    HibernateSession.closeSession();
                } catch (Exception e) {
                }
            }
        } else {
            this.xmlResult = "<SUCCESS/>";
            setResponsePage(this.SUCCESS_JSP);
        }
        return this;
    }
}
