package edu.unc.med.lccc.tcgasra.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import edu.unc.med.lccc.tcgasra.datalogic.ExperimentHibDAO;
import edu.unc.med.lccc.tcgasra.datalogic.SampleHibDAO;
import edu.unc.med.lccc.tcgasra.hibernateobj.Experiment;
import edu.unc.med.lccc.tcgasra.hibernateobj.Sample;
import edu.unc.med.lccc.tcgasra.util.Debug;
import edu.unc.med.lccc.tcgasra.xsdobj.submission.*;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class CreateSubmissionXML {

    public static void createSubmissionXML(ConnectPostgreSQL db, Sample sample, Experiment experiment, String fileName, String withAnalysis) throws JAXBException, IOException, DatatypeConfigurationException, SQLException {
        ObjectFactory of = new ObjectFactory();
        SUBMISSIONSET submissionSet = new SUBMISSIONSET();
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.unc.med.lccc.tcgasra.xsdobj.submission");
        SubmissionType submissionType = new SubmissionType();
        submissionType.setAlias("UNCID: TCGA");
        submissionType.setCenterName("UNC-LCCC");
        submissionType.setTITLE(experiment.getTitle() + " UNCID:" + db.getNextValFromDB());
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String currentTime = sdf.format(cal.getTime());
        int year = Integer.parseInt(currentTime.toString().substring(0, 4));
        int month = Integer.parseInt(currentTime.toString().substring(5, 7));
        int day = Integer.parseInt(currentTime.toString().substring(8, 10));
        int hour = Integer.parseInt(currentTime.toString().substring(11, 13));
        int minute = Integer.parseInt(currentTime.toString().substring(14, 16));
        int second = Integer.parseInt(currentTime.toString().substring(17, 19));
        int millisecond = 0;
        submissionType.setSubmissionDate(datatypeFactory.newXMLGregorianCalendar(year, month, day, hour, minute, second, millisecond, DatatypeConstants.FIELD_UNDEFINED));
        SubmissionType.ACTIONS submissionType_actions = new SubmissionType.ACTIONS();
        submissionType.setACTIONS(submissionType_actions);
        SubmissionType.ACTIONS.ACTION submissionType_actions_action = new SubmissionType.ACTIONS.ACTION();
        SubmissionType.ACTIONS.ACTION.ADD submissionType_actions_action_add = new SubmissionType.ACTIONS.ACTION.ADD();
        submissionType_actions_action_add.setSchema("analysis");
        submissionType_actions_action_add.setSource("analysis.xml");
        submissionType_actions_action.setADD(submissionType_actions_action_add);
        SubmissionType.ACTIONS.ACTION submissionType_actions_action4 = new SubmissionType.ACTIONS.ACTION();
        SubmissionType.ACTIONS.ACTION.ADD submissionType_actions_action_add4 = new SubmissionType.ACTIONS.ACTION.ADD();
        submissionType_actions_action_add4.setSchema("experiment");
        submissionType_actions_action_add4.setSource("experiment.xml");
        submissionType_actions_action4.setADD(submissionType_actions_action_add4);
        SubmissionType.ACTIONS.ACTION submissionType_actions_action5 = new SubmissionType.ACTIONS.ACTION();
        SubmissionType.ACTIONS.ACTION.ADD submissionType_actions_action_add5 = new SubmissionType.ACTIONS.ACTION.ADD();
        submissionType_actions_action_add5.setSchema("run");
        submissionType_actions_action_add5.setSource("run.xml");
        submissionType_actions_action5.setADD(submissionType_actions_action_add5);
        SubmissionType.ACTIONS.ACTION submissionType_actions_action2 = new SubmissionType.ACTIONS.ACTION();
        SubmissionType.ACTIONS.ACTION.RELEASE submissionType_actions_action_release = new SubmissionType.ACTIONS.ACTION.RELEASE();
        submissionType_actions_action2.setRELEASE(submissionType_actions_action_release);
        SubmissionType.ACTIONS.ACTION submissionType_actions_action3 = new SubmissionType.ACTIONS.ACTION();
        SubmissionType.ACTIONS.ACTION.PROTECT submissionType_actions_action_protect = new SubmissionType.ACTIONS.ACTION.PROTECT();
        submissionType_actions_action3.setPROTECT(submissionType_actions_action_protect);
        if (!"false".equals(withAnalysis)) {
            submissionType.getACTIONS().getACTION().add(submissionType_actions_action);
        }
        submissionType.getACTIONS().getACTION().add(submissionType_actions_action4);
        submissionType.getACTIONS().getACTION().add(submissionType_actions_action5);
        submissionType.getACTIONS().getACTION().add(submissionType_actions_action2);
        submissionType.getACTIONS().getACTION().add(submissionType_actions_action3);
        SubmissionType.CONTACTS submissionType_contacts = new SubmissionType.CONTACTS();
        submissionType.setCONTACTS(submissionType_contacts);
        SubmissionType.CONTACTS.CONTACT submissionType_contacts_contact = new SubmissionType.CONTACTS.CONTACT();
        submissionType_contacts_contact.setInformOnError("mailto:seqware-support@unc.edu");
        submissionType_contacts_contact.setInformOnStatus("mailto:seqware-support@unc.edu");
        submissionType_contacts_contact.setName("SeqWare Support Email Address at UNC");
        submissionType_contacts.getCONTACT().add(submissionType_contacts_contact);
        submissionSet.getSUBMISSION().add(submissionType);
        PrintWriter writer = new PrintWriter(new FileWriter(fileName, false));
        Marshaller marshaller = jaxbContext.createMarshaller();
        JAXBElement<SubmissionType> sampleElement = of.createSUBMISSION(submissionType);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(sampleElement, writer);
    }
}
