package org.comunication.parserxml.xmlAdapter;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.comunication.helper.constats.SemanticNames;
import org.comunication.helper.constats.TypeFieldsFromMessage;
import org.comunication.parserxml.xmlAdapter.xmltransformers.XMLToJSigTransformers;
import org.hl7.rim.ParameterItem;
import org.hl7.rim.Person;
import org.hl7.rim.QueryByParameter;
import org.hl7.types.AD;
import org.hl7.types.ADXP;
import org.hl7.types.II;

/**
 * @author pclavijo
 */
public class AnswersXMLGenerator extends Generator {

    private static AnswersXMLGenerator instance = null;

    /**
	 * @author pclavijo
	 *
	 */
    private AnswersXMLGenerator() {
    }

    /**
	 * @author pclavijo
	 * @return retorna la unica instancia del generador que hay en el sistema
	 */
    public static AnswersXMLGenerator getInstance() {
        if (instance == null) {
            instance = new AnswersXMLGenerator();
        }
        return instance;
    }

    /**
	 * @author pclavijo
	 * 
	 * arma un mensaje xml 201310 (respueta)
	 * 
	 * 
	 * Envia la lista de identifiadores de los canididatos para la consulta
	 */
    public static void make310Message(List<Person> resultList, QueryByParameter qbp) {
        System.out.println("makePRPA_in201310UVXMLMessage");
        Person person = null;
        String registrationEventListXML = "";
        for (Iterator iter = resultList.iterator(); iter.hasNext(); ) {
            person = (Person) iter.next();
            String registrationEventXML = XMLToJSigTransformers.getInstance().registrarionEventToXML(person, qbp);
            registrationEventListXML = registrationEventListXML + registrationEventXML;
        }
        String assigningAuthorityName = "Hospital Maciel";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHmmss");
        String actulTime = sdf.format(new Date(System.currentTimeMillis()));
        try {
            PrintWriter escribir = new PrintWriter(new BufferedWriter(new FileWriter("PRPA_IN201310Prueba" + System.currentTimeMillis() + ".xml")));
            escribir.write("<PRPA_IN201310UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 file:/L:/Norma%20HL7v3%202007/processable/multicacheschemas/PRPA_IN201310UV.xsd\" ITSVersion=\"XML_1.0\">\n" + "<id extension=\"8787\" root=\"1.3.6.1.4.1.23650.1.1\" assigningAuthorityName=\"" + assigningAuthorityName + "\"></id>\n" + "<creationTime value=\"" + actulTime + "\"/>\n" + "<interactionId extension=\"PRPA_IN201310\" root=\"2.16.840.1.113883.1.6\"/>\n" + "<processingCode code=\"P\"/>\n" + "<processingModeCode code=\"T\"/>\n" + "<acceptAckCode code=\"NE\"/>\n" + "<receiver typeCode=\"RCV\">\n" + "<device determinerCode=\"INSTANCE\">\n" + "<id root=\"1.3.6.1.4.1.23650.2.1\"></id>\n" + "<name>\n" + "<given>\n" + " DescOp\n " + "</given>\n" + "</name>\n" + "<softwareName code=\"2\" codeSystemName=\"Codificacion de sistemas OpenSIH\" codeSystem=\"1.3.6.1.4.1.23650.10\" language=\"ES-es\" codeSystemVersion=\"1.0\"></softwareName>\n" + "<asAgent classCode=\"AGNT\">\n" + "<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n" + "<id root=\"1.3.6.1.4.1.23650.9\"/>\n" + "<name><given>OpenSIH</given></name>\n" + "</representedOrganization>\n" + "</asAgent>\n" + "</device>\n" + "</receiver>\n" + "<sender>\n" + "<device><id></id></device>\n" + "</sender>\n" + "<acknowledgement>\n" + "<typeCode code=\"CA\"/>\n" + "<targetMessage>\n" + "<id extension=\"1514\" root=\"1.3.6.1.4.1.23650.1.1\"  assigningAuthorityName=\"" + assigningAuthorityName + "\"></id>\n" + "</targetMessage>\n" + "</acknowledgement>\n" + "<controlActProcess moodCode=\"EVN\" classCode=\"CACT\">\n" + "<code code=\"PRPA_TE201310UV\"/>\n" + "<languageCode code=\"M\" codeSystemName=\"Spanish\"> </languageCode>\n" + "<subject typeCode=\"SUBJ\">\n" + registrationEventListXML + "</subject>\n" + "<queryAck>\n" + "<queryResponseCode/>\n" + "</queryAck>\n" + "</controlActProcess>\n" + "</PRPA_IN201310UV>\n".trim());
            escribir.close();
            System.out.println("RESPUESTA GENERADA");
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @author pclavijo
	 * 
	 * arma un mensaje de consulta 201306
	 */
    public static void make306Message(List<Person> resultList, QueryByParameter qbp) {
        ParameterItem personAddres = null;
        ParameterItem livingSubjectIdParameter = null;
        ParameterItem livingSubjectAdministrativeGender = null;
        ParameterItem LivingSubjectBirthTime = null;
        ParameterItem LivingSubjectName = null;
        for (Iterator iter = qbp.getParameter().iterator(); iter.hasNext(); ) {
            ParameterItem element = (ParameterItem) iter.next();
            if (element != null) {
                if (element.getSemanticsText().toString().trim().equals(SemanticNames.PATIENT_ADDRESS)) {
                    personAddres = element;
                } else if (element.getSemanticsText().toString().trim().equals(SemanticNames.LIVING_SUBJECT_ID)) {
                    livingSubjectIdParameter = element;
                } else if (element.getSemanticsText().toString().trim().equals(SemanticNames.LIVING_SUBJECT_ADMINISTRATIVE_GENDER)) {
                    livingSubjectAdministrativeGender = element;
                } else if (element.getSemanticsText().toString().trim().equals(SemanticNames.LIVING_SUBJECT_BIRTH_TIME)) {
                    LivingSubjectBirthTime = element;
                } else if (element.getSemanticsText().toString().trim().equals(SemanticNames.LIVING_SUBJECT_NAME)) {
                    LivingSubjectName = element;
                }
            }
        }
        String address = "J H y Reissig 512";
        String livingSubjectId = "livingSubjectId BLABLABLA";
        String deceasedTime = "20200429120100";
        String codeGender = "1";
        String livingSubjectBirth = "20070429120100";
        String idRegisterEvent = "idRegisterEvent";
        String creationTime = "200712301512";
        String languageCode = "M";
        II queryId = qbp.getQueryId();
        String queryIdXML = XMLToJSigTransformers.getInstance().iiToXML(TypeFieldsFromMessage.QUERY_ID, queryId);
        String responsePriorityCode = "I";
        String queryMatchObservation = "relleno";
        String assignedEntityId = "assignedEntityId";
        String assigningAuthorityName = "OpenSIH";
        String queryResponseCode = "1111";
        String resultCurrentQuantity = "2";
        String resultRemainingQuantity = "2";
        if (personAddres != null) {
            address = ((AD) personAddres.getValue()).head().toString();
            for (Iterator iter = ((AD) personAddres.getValue()).tail().iterator(); iter.hasNext(); ) {
                address = address + ((ADXP) iter.next()).toString();
            }
        } else {
            System.out.println("la direccion vale null");
        }
        if (livingSubjectAdministrativeGender != null) {
            codeGender = livingSubjectAdministrativeGender.getValue().toString();
        } else {
            System.out.println("livingSubjectAdministrativeGender vale null");
        }
        if (livingSubjectIdParameter != null) {
            livingSubjectId = livingSubjectIdParameter.getValue().toString();
        } else {
            System.out.println("livingSubjectIdParameter vale null");
        }
        if (LivingSubjectBirthTime != null) {
            livingSubjectBirth = LivingSubjectBirthTime.getValue().toString();
        } else {
            System.out.println("LivingSubjectBirthTime vale null");
        }
        try {
            PrintWriter escribir = new PrintWriter(new BufferedWriter(new FileWriter("PRPA_IN201306Prueba" + System.currentTimeMillis() + ".xml")));
            escribir.write("<PRPA_IN201306UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 PRPA_IN201306UV.xsd\" ITSVersion=\"XML_1.0\"> \n" + " <id/>  \n" + "<creationTime nullFlavor=\"NA\" value=\"" + creationTime + "\"/>  \n" + "<interactionId/> \n" + "<processingCode/> \n" + "<processingModeCode/> \n" + "<acceptAckCode/> \n" + "<receiver typeCode=\"RCV\"> \n" + "<device determinerCode=\"INSTANCE\"> \n" + "<id/> \n" + "</device> \n" + "</receiver> \n" + "<sender typeCode=\"SND\"> \n" + "<device determinerCode=\"INSTANCE\"> \n" + "<id/> \n" + "</device> \n" + "</sender> \n" + "<controlActProcess moodCode=\"EVN\" classCode=\"CACT\"> \n" + "<code code=\"PRPA_TE201306UV\"/> \n" + "<languageCode code=\"" + languageCode + "\" codeSystemName=\"controlActLanguage\"> </languageCode> \n" + "<subject typeCode=\"SUBJ\">  \n" + "<registrationEvent> \n" + "<id root=\"1.3.6.1.4.1.23650.15\" extension=\"" + idRegisterEvent + "\" displayable=\"true\" assigningAuthorityName=\"" + assigningAuthorityName + "\"/> \n" + "<statusCode/> \n" + "<subject1> \n" + "<patient classCode=\"asd\"> \n" + "<id/> \n" + "<statusCode/> \n" + "<patientPerson> \n" + "<name use=\"P\" nullFlavor=\"NA\"/> \n" + "<addr nullFlavor=\"NA\" use=\"DIR\" isNotOrdered=\"false\">" + address + "</addr> \n" + "</patientPerson> \n" + "<subjectOf1> \n" + "<queryMatchObservation> \n" + "<code/> \n" + "<value>" + queryMatchObservation + "</value> \n" + "</queryMatchObservation> \n" + "</subjectOf1> \n" + "</patient> \n" + "<queryByParameter> \n" + queryIdXML + "<statusCode code=\"new\"/> \n" + "<responsePriorityCode code=\"" + responsePriorityCode + "\"/> \n" + "<parameterList> \n" + "<id assigningAuthorityName=\"" + assigningAuthorityName + "\" displayable=\"true\" extension=\"extencion del queryId\" root=\"1.3.6.1.4.1.23650.3.1\"/> \n" + "<livingSubjectAdministrativeGender> \n" + "<value code=\"" + codeGender + "\"	 codeSystem=\"1.3.6.1.4.1.23650.6.1\" codeSystemName=\"gender\" codeSystemVersion=\"1.0\" displayName=\"true\"/> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</livingSubjectAdministrativeGender> \n" + "<livingSubjectBirthPlaceAddress> \n" + "<value nullFlavor=\"NA\" isNotOrdered=\"true\" use=\"DIR\"/> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</livingSubjectBirthPlaceAddress> \n" + "<livingSubjectBirthTime> \n" + "<value nullFlavor=\"NA\" value=\"" + livingSubjectBirth + "\"/> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</livingSubjectBirthTime> \n" + "<livingSubjectDeceasedTime> \n" + "<value nullFlavor=\"NA\" value=\"" + deceasedTime + "\"/> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</livingSubjectDeceasedTime> \n" + "<livingSubjectId> \n" + "<value nullFlavor=\"NI\" root=\"1.3.6.1.4.1.23650.6.1\" extension=\"" + livingSubjectId + "\" displayable=\"true\" assigningAuthorityName=\"" + assigningAuthorityName + "\"/> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</livingSubjectId> \n" + "<patientAddress> \n" + "<value isNotOrdered=\"false\">" + address + "</value> \n" + "<semanticsText nullFlavor=\"NA\" language=\"spanish\" mediaType=\"text/plain\" representation=\"TXT\"/> \n" + "</patientAddress> \n" + "</parameterList> \n" + "</queryByParameter> \n" + "</subject1> \n" + "<author typeCode=\"AUT\" contextControlCode=\"AP\"> \n" + "<noteText/> \n" + "<time/> \n" + "<modeCode/> \n" + "<assignedEntity nullFlavor=\"NA\" classCode=\"ASSIGNED\"> \n" + "<id nullFlavor=\"NA\"  root=\"1.3.6.1.4.1.23650.6.1\" extension=\"" + assignedEntityId + "\"  assigningAuthorityName=\"" + assigningAuthorityName + "\" displayable=\"true\"/> \n" + "</assignedEntity> \n" + "</author> \n" + "<custodian> \n" + "<assignedEntity classCode=\"ASSIGNED\"> \n" + "<id assigningAuthorityName=\"" + assigningAuthorityName + "\" displayable=\"true\"/> \n" + "</assignedEntity> \n" + "</custodian> \n" + "</registrationEvent> \n" + "</subject> \n" + "<queryAck nullFlavor=\"NA\"> \n" + "<queryResponseCode nullFlavor=\"NA\" code=\"" + queryResponseCode + "\"/> \n" + "	<resultCurrentQuantity nullFlavor=\"NA\" value=\"" + resultCurrentQuantity + "\"/>  \n" + "	<resultRemainingQuantity nullFlavor=\"NA\" value=\"" + resultRemainingQuantity + "\" /> \n" + "</queryAck> \n" + "</controlActProcess> \n" + "</PRPA_IN201306UV> \n".trim());
            escribir.close();
            System.out.println("ta pronto");
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
