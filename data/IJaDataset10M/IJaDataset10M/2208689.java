package test.org.slasoi.businessManager.reporting.report;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slaatsoi.business.schema.ReportFormatType;
import org.slasoi.businessManager.reporting.core.FrameworkContext.PropertiesFileNames;
import org.slasoi.businessManager.reporting.core.FrameworkContextManager;
import org.slasoi.businessManager.reporting.report.PDFReportMaker;
import org.slasoi.businessManager.reporting.report.Report;
import org.slasoi.businessManager.reporting.utils.XMLUtils;
import org.slasoi.common.eventschema.AgreementTermType;
import org.slasoi.common.eventschema.AssessmentResultType;
import org.slasoi.common.eventschema.GuaranteedActionType;
import org.slasoi.common.eventschema.GuaranteedType;
import org.slasoi.common.eventschema.GuatanteedStateType;
import org.slasoi.common.eventschema.MonitoringInfoType;
import org.slasoi.common.eventschema.MonitoringResultEventType;
import org.slasoi.common.eventschema.PropertiesType;
import org.slasoi.common.eventschema.SLAType;
import org.slasoi.common.reportschema.AssessmentInfoType;
import org.slasoi.common.reportschema.AssessmentResultSummaryType;
import org.slasoi.common.reportschema.FunctionalAggregatorResultSummaryType;
import org.slasoi.common.reportschema.FunctionalAggregatorResultType;
import org.slasoi.common.reportschema.ObjectFactory;
import org.slasoi.common.reportschema.PostSaleReportType;
import org.slasoi.common.reportschema.ReportInfoType;
import org.xml.sax.SAXException;

/**
 * @author Davide Lorenzoli
 * 
 * @date Mar 24, 2011
 */
public class TestPDFReportMaker {

    static {
        StringBuilder frameworkConfigurationFolder = new StringBuilder();
        String slasoiHome = System.getenv("SLASOI_HOME");
        if (slasoiHome == null) {
            PropertyConfigurator.configure(ClassLoader.getSystemResource("./conf/log4j.properties").getPath());
        } else {
            frameworkConfigurationFolder.append(slasoiHome);
            frameworkConfigurationFolder.append(System.getProperty("file.separator"));
            frameworkConfigurationFolder.append("bmanager-postsale-reporting");
            PropertyConfigurator.configure(frameworkConfigurationFolder + "/log4j.properties");
        }
    }

    Logger logger = Logger.getLogger(getClass());

    private static final String REPORT_TEMPLATE_PROPERTY = FrameworkContextManager.getFrameworkContext().getFrameworkProperties(PropertiesFileNames.FRAMEWORK).getProperty("reporting.report.template.file");

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link test.org.slasoi.businessManager.reporting.report.PDFReportMaker#crerateReport(org.slasoi.common.reportschema.PostSaleReportType)}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DatatypeConfigurationException 
	 */
    @Test
    public void testCrerateReport() throws IOException, SAXException, DatatypeConfigurationException {
        PDFReportMaker pdfReportMaker = new PDFReportMaker();
        String reportTemplateFile = getClass().getClassLoader().getResource(REPORT_TEMPLATE_PROPERTY).getPath();
        PostSaleReportType postSaleReportType = getPostSaleReportType();
        try {
            logger.info(XMLUtils.marshall(postSaleReportType));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Report report = pdfReportMaker.createReport(reportTemplateFile, postSaleReportType);
        assertEquals(ReportFormatType.PDF, report.getFormat());
    }

    /**
	 * @return
	 * @throws DatatypeConfigurationException
	 */
    private PostSaleReportType getPostSaleReportType() throws DatatypeConfigurationException {
        ObjectFactory objectFactory = new ObjectFactory();
        PostSaleReportType report = new PostSaleReportType();
        report.setReportInfo(getReportInfo());
        report.setMonitoringResult(getMonitoringResultEvent());
        report.setAssessmentResultSummary(getAssessmentResultSummary());
        report.setFunctionalAggregatorResultSummary(getFunctionalAggregatorResultSummary());
        return report;
    }

    /**
	 * @return
	 */
    private ReportInfoType getReportInfo() {
        ReportInfoType reportInfo = new ReportInfoType();
        reportInfo.setReportCreatorId("Post-Sale Report Maker");
        try {
            reportInfo.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
        reportInfo.setReportId("B6-SLA1#" + reportInfo.getTimestamp().toString());
        return reportInfo;
    }

    /**
	 * @return
	 */
    private MonitoringResultEventType getMonitoringResultEvent() {
        MonitoringResultEventType monitoringResult = new MonitoringResultEventType();
        monitoringResult.setSLAInfo(getSlaInfo());
        monitoringResult.setExtraProperties(new PropertiesType());
        monitoringResult.setMonitoringInfo(new MonitoringInfoType());
        return monitoringResult;
    }

    /**
	 * @return
	 */
    private SLAType getSlaInfo() {
        SLAType slaInfo = new SLAType();
        slaInfo.setAssessmentResult(AssessmentResultType.VIOLATION);
        slaInfo.setSlaURI("http://slaatasoi/B6-SLA1.xml");
        slaInfo.setSlaUUID("B6-SLA1");
        slaInfo.getAgreementTerm().addAll(getAgreementTerms());
        return slaInfo;
    }

    /**
	 * @return
	 */
    private Collection<AgreementTermType> getAgreementTerms() {
        ArrayList<AgreementTermType> agreementTerms = new ArrayList<AgreementTermType>();
        AgreementTermType agreementTerm = new AgreementTermType();
        agreementTerm.setAgreementTermID("AT1");
        agreementTerm.setAssessmentResult(AssessmentResultType.VIOLATION);
        agreementTerm.getGuaranteedStateOrGuaranteedAction().addAll(getGuaranteed());
        agreementTerms.add(agreementTerm);
        agreementTerm = new AgreementTermType();
        agreementTerm.setAgreementTermID("AT2");
        agreementTerm.setAssessmentResult(AssessmentResultType.SATISFACTION);
        agreementTerm.getGuaranteedStateOrGuaranteedAction().addAll(getGuaranteed());
        agreementTerms.add(agreementTerm);
        return agreementTerms;
    }

    /**
	 * @return
	 */
    private Collection<GuaranteedType> getGuaranteed() {
        ArrayList<GuaranteedType> guaranteedList = new ArrayList<GuaranteedType>();
        GuaranteedType guaranteed = new GuatanteedStateType();
        guaranteed.setAssessmentResult(AssessmentResultType.SATISFACTION);
        guaranteed.setGuaranteedID("GT1");
        ((GuatanteedStateType) guaranteed).setQoSName(org.slasoi.slamodel.vocab.common.mttr.getValue());
        ((GuatanteedStateType) guaranteed).setQoSValue("45");
        guaranteedList.add(guaranteed);
        guaranteed = new GuatanteedStateType();
        guaranteed.setAssessmentResult(AssessmentResultType.VIOLATION);
        guaranteed.setGuaranteedID("GT2");
        ((GuatanteedStateType) guaranteed).setQoSName(org.slasoi.slamodel.vocab.common.mttf.getValue());
        ((GuatanteedStateType) guaranteed).setQoSValue("30");
        guaranteedList.add(guaranteed);
        guaranteed = new GuaranteedActionType();
        guaranteed.setAssessmentResult(AssessmentResultType.NOT_ASSESSED);
        guaranteed.setGuaranteedID("GT3");
        ((GuaranteedActionType) guaranteed).setSpecification(null);
        guaranteedList.add(guaranteed);
        return guaranteedList;
    }

    /**
	 * @return
	 */
    private AssessmentResultSummaryType getAssessmentResultSummary() {
        AssessmentResultSummaryType assessmentResultSummary = new AssessmentResultSummaryType();
        AssessmentInfoType assessmentInfo = new AssessmentInfoType();
        assessmentInfo.setNotAssessted(0);
        assessmentInfo.setSatisfactions(0);
        assessmentInfo.setViolations(1);
        assessmentResultSummary.setSLA(assessmentInfo);
        assessmentInfo.setNotAssessted(0);
        assessmentInfo.setSatisfactions(1);
        assessmentInfo.setViolations(1);
        assessmentResultSummary.setAgreementTerm(assessmentInfo);
        assessmentInfo.setNotAssessted(2);
        assessmentInfo.setSatisfactions(2);
        assessmentInfo.setViolations(2);
        assessmentResultSummary.setGuaranteed(assessmentInfo);
        return assessmentResultSummary;
    }

    /**
	 * @return
	 */
    private FunctionalAggregatorResultSummaryType getFunctionalAggregatorResultSummary() {
        FunctionalAggregatorResultSummaryType functionalAggregatorResultSummary = new FunctionalAggregatorResultSummaryType();
        FunctionalAggregatorResultType functionalAggregatorResult = new FunctionalAggregatorResultType();
        functionalAggregatorResult.setSlaId("B6-SLA1");
        functionalAggregatorResult.setAgreementTermId("AT1");
        functionalAggregatorResult.setGuaranteedTermId("GT1");
        functionalAggregatorResult.setFunctionalAggregatorId("qos:max");
        functionalAggregatorResult.setAggregateValue(45.0);
        functionalAggregatorResultSummary.getFunctionalAggregatorResult().add(functionalAggregatorResult);
        functionalAggregatorResult = new FunctionalAggregatorResultType();
        functionalAggregatorResult.setSlaId("B6-SLA1");
        functionalAggregatorResult.setAgreementTermId("AT1");
        functionalAggregatorResult.setGuaranteedTermId("GT3");
        functionalAggregatorResult.setFunctionalAggregatorId("qos:min");
        functionalAggregatorResult.setAggregateValue(12.5);
        functionalAggregatorResultSummary.getFunctionalAggregatorResult().add(functionalAggregatorResult);
        return functionalAggregatorResultSummary;
    }
}
