package progranet.plugins.quartz.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContextAware;
import progranet.model.service.*;
import progranet.plugins.quartz.core.IGanesaSchedulerContext;
import org.springframework.context.ApplicationContext;
import progranet.plugins.quartz.SchedulerService;

public class GanesaSchedulerContextImpl implements IGanesaSchedulerContext, ApplicationContextAware {

    private static final String OBJECT_SERVICE_BEAN_NAME = "objectService";

    private static final String MODEL_SERVICE_BEAN_NAME = "modelService";

    private static final String XML_SERVICE_BEAN_NAME = "xmlService";

    private static final String REPORT_SERVICE_BEAN_NAME = "reportService";

    private static final String MAIL_SERVICE_BEAN_NAME = "mailService";

    private static final String SCHEDULER_SERVICE_BEAN_NAME = "schedulerService";

    private Log logger = LogFactory.getLog(this.getClass());

    private ObjectService objectService;

    private ModelService modelService;

    private XMLService xmlService;

    private ReportService reportService;

    private MailService mailService;

    private SchedulerService schedulerService;

    private ApplicationContext applicationContext;

    public GanesaSchedulerContextImpl() {
        this.logger.info("<GanesaSchedulerContextImpl initialized>");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ObjectService getObjectService() {
        if (this.objectService == null) {
            this.objectService = (ObjectService) this.lookupService(OBJECT_SERVICE_BEAN_NAME);
        }
        return this.objectService;
    }

    public ModelService getModelService() {
        if (this.modelService == null) {
            this.modelService = (ModelService) this.lookupService(MODEL_SERVICE_BEAN_NAME);
        }
        return this.modelService;
    }

    public XMLService getXmlService() {
        if (this.xmlService == null) {
            this.xmlService = (XMLService) this.lookupService(XML_SERVICE_BEAN_NAME);
        }
        return this.xmlService;
    }

    public ReportService getReportService() {
        if (this.reportService == null) {
            this.reportService = (ReportService) this.lookupService(REPORT_SERVICE_BEAN_NAME);
        }
        return this.reportService;
    }

    public MailService getMailService() {
        if (this.mailService == null) {
            this.mailService = (MailService) this.lookupService(MAIL_SERVICE_BEAN_NAME);
        }
        return this.mailService;
    }

    public SchedulerService getSchedulerService() {
        if (schedulerService == null) {
            this.schedulerService = (SchedulerService) this.lookupService(SCHEDULER_SERVICE_BEAN_NAME);
        }
        return this.schedulerService;
    }

    public void setObjectService(ObjectService objectService) {
        this.objectService = objectService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public void setXmlService(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public Object lookupService(String serviceBeanName) {
        return applicationContext.getBean(serviceBeanName);
    }
}
