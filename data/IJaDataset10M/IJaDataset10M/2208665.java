package org.slasoi.businessManager.reporting.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slasoi.common.eventschema.AssessmentResultType;
import org.slasoi.common.eventschema.EventInstance;
import org.slasoi.common.eventschema.SLAType;
import org.slasoi.common.eventschema.impl.MonitoringEventServiceImpl;
import org.xml.sax.SAXException;

/**
 * @author Davide Lorenzoli
 * 
 * @date Apr 6, 2011
 */
public class MonitoringEventGenerator {

    private static final String MONITORING_RESULT_EVENT_TEMPLATE = "monitoringResultEventTemplate.xml";

    public static ArrayList<EventInstance> getMonitoringEvents(Date fromDate, Date toDate, long offset) {
        ArrayList<EventInstance> events = new ArrayList<EventInstance>();
        ArrayList<Date> dates = DateGenerator.generateDates(fromDate, toDate, offset);
        for (Date date : dates) {
            events.add(createEventInstance(date));
        }
        return events;
    }

    /**
	 * @param events
	 * @param fileName
	 * @return
	 */
    public static boolean saveMonitoringEvents(ArrayList<EventInstance> events, String fileName) {
        MonitoringEventServiceImpl monitoringEventServiceImpl = new MonitoringEventServiceImpl();
        FileOutputStream fileOutputStream = null;
        boolean result = false;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            int counter = 1;
            for (EventInstance event : events) {
                fileOutputStream.write(("<!-- Event " + counter++ + " -->").getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(monitoringEventServiceImpl.marshall(event).getBytes());
                fileOutputStream.write("\n\n".getBytes());
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
	 * @param date
	 * @return
	 */
    private static EventInstance createEventInstance(Date date) {
        EventInstance event = null;
        try {
            event = loadMonitoringResultEventTemplate();
            GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            gregorianCalendar.setTimeInMillis(date.getTime());
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            event.getEventContext().getTime().setCollectionTime(xmlGregorianCalendar);
            event.getEventContext().getTime().setReportTime(xmlGregorianCalendar);
            event.getEventContext().getTime().setTimestamp(gregorianCalendar.getTimeInMillis());
            SLAType slaType = event.getEventPayload().getMonitoringResultEvent().getSLAInfo();
            slaType.setAssessmentResult(getAssessmentResultType());
            slaType.getAgreementTerm().get(0).setAssessmentResult(getAssessmentResultType());
            slaType.getAgreementTerm().get(0).getGuaranteedStateOrGuaranteedAction().get(0).setAssessmentResult(getAssessmentResultType());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    private static EventInstance loadMonitoringResultEventTemplate() throws Exception {
        String monitoringResultEventTemplateFileName = ClassLoader.getSystemResource(MONITORING_RESULT_EVENT_TEMPLATE).getPath();
        String monitoringResultEventXML = XMLUtils.fileToString(monitoringResultEventTemplateFileName);
        return (EventInstance) XMLUtils.unmarshall(monitoringResultEventXML, null, EventInstance.class);
    }

    /**
	 * @return
	 */
    private static AssessmentResultType getAssessmentResultType() {
        long milliseconds = System.currentTimeMillis();
        AssessmentResultType assessmentResultType = null;
        if (milliseconds % 2 == 0) {
            assessmentResultType = AssessmentResultType.SATISFACTION;
        } else if (milliseconds % 3 == 0) {
            assessmentResultType = AssessmentResultType.VIOLATION;
        } else {
            assessmentResultType = AssessmentResultType.NOT_ASSESSED;
        }
        return assessmentResultType;
    }

    public static void main(String[] args) {
        String MONITORING_RESULT_EVENTS_FILE = "monitoringResultEvents.xml";
        GregorianCalendar fromDate = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar toDate = (GregorianCalendar) GregorianCalendar.getInstance();
        fromDate.set(2011, 1, 1, 12, 0, 0);
        toDate.set(2012, 1, 1, 12, 0, 0);
        System.out.println("From date: " + fromDate.getTime());
        System.out.println("To date: " + toDate.getTime());
        System.out.println("Generating events...");
        ArrayList<EventInstance> events = MonitoringEventGenerator.getMonitoringEvents(fromDate.getTime(), toDate.getTime(), DateGenerator.WEEK);
        System.out.println("Generated events: " + events.size());
        System.out.println("Saving events to: " + MONITORING_RESULT_EVENTS_FILE);
    }
}
