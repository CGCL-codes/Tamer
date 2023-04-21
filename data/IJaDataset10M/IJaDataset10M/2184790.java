package gleam.executive.workflow.action;

import gleam.executive.model.User;
import gleam.executive.model.WebAppBean;
import gleam.executive.service.MailEngine;
import gleam.executive.service.UserManager;
import gleam.executive.workflow.jms.EmailProducer;
import gleam.executive.workflow.model.AnnotationStatusInfo;
import gleam.executive.workflow.sm.JbpmDataflowHandlerProxy;
import gleam.executive.workflow.util.JPDLConstants;
import gleam.executive.workflow.util.WorkflowUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.springframework.mail.MailMessage;
import org.springframework.util.StringUtils;

public class MailActionHandler extends JbpmDataflowHandlerProxy {

    private static final long serialVersionUID = 1L;

    private Element targetProperties;

    MailEngine mailEngine;

    MailMessage mailMessage;

    UserManager userManager;

    EmailProducer emailProducer;

    WebAppBean webAppBean;

    protected final Log log = LogFactory.getLog(getClass());

    public void execute(ExecutionContext context) throws Exception {
        Map<String, String> variableMap = fetchAndValidateVariables(context, this.getClass().getName(), targetProperties);
        log.debug("MailActionHandler START");
        String mode = (String) variableMap.get(JPDLConstants.MODE);
        log.debug("@@@@@@@ mode = " + mode);
        String to = (String) variableMap.get(JPDLConstants.TO);
        log.debug("TO: " + to);
        String cc = (String) variableMap.get(JPDLConstants.CC);
        log.debug("CC: " + cc);
        String subject = (String) variableMap.get(JPDLConstants.SUBJECT);
        log.debug("@@@@@@@ subject " + subject);
        String template = (String) variableMap.get(JPDLConstants.TEMPLATE);
        log.debug("@@@@@@@ template " + template);
        String link = webAppBean.getUrlBase() + "/" + webAppBean.getName();
        Token token = context.getToken();
        String documentId = (String) context.getContextInstance().getVariable(JPDLConstants.DOCUMENT_ID, token);
        log.debug("@@@@@@@ documentId " + documentId);
        Map globalVariableMap = context.getContextInstance().getVariables();
        String processName = (String) globalVariableMap.get(JPDLConstants.PROCESS_INSTANCE_NAME);
        log.debug("@@@@@@@ processName " + processName);
        String taskName = context.getNode().getName();
        String statusVariableName = WorkflowUtil.createAnnotationStatusVariableName(context.getProcessDefinition().getName(), context.getProcessInstance().getId(), documentId);
        log.debug("look for variable: " + statusVariableName);
        AnnotationStatusInfo annotationStatusInfo = (AnnotationStatusInfo) context.getContextInstance().getVariable(statusVariableName);
        if (!JPDLConstants.TEST_MODE.equals(mode)) {
            String[] managers = StringUtils.commaDelimitedListToStringArray(to);
            for (int i = 0; i < managers.length; i++) {
                User user = userManager.getUserByUsername(managers[i]);
                mailMessage.setTo(user.getFullName() + "<" + user.getEmail() + ">");
                mailMessage.setSubject(subject);
                Map<String, String> model = new HashMap<String, String>();
                model.put("userName", user.getFullName());
                model.put("processName", processName);
                if (taskName != null) {
                    model.put("taskName", taskName);
                }
                if (annotationStatusInfo != null && annotationStatusInfo.getStatus().startsWith(AnnotationStatusInfo.STATUS_FAILED)) {
                    model.put("documentId", documentId);
                    model.put("error", annotationStatusInfo.getStatus());
                }
                model.put("link", link);
                log.debug("link " + link);
                emailProducer.sendMessage(mailMessage, template, model);
            }
        }
        log.debug("MailActionHandler END");
    }

    public MailEngine getMailEngine() {
        return mailEngine;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public MailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(MailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public EmailProducer getEmailProducer() {
        return emailProducer;
    }

    public void setEmailProducer(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    public WebAppBean getWebAppBean() {
        return webAppBean;
    }

    public void setWebAppBean(WebAppBean webAppBean) {
        this.webAppBean = webAppBean;
    }

    public Element getTargetProperties() {
        return targetProperties;
    }

    public void setTargetProperties(Element targetProperties) {
        this.targetProperties = targetProperties;
    }
}
