package org.hmaciel.sisingr.ejb.controladores;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.comunication.parserxml.xmlAdapter.TransformaJavaSigAXML;
import org.comunication.parserxml.xmlAdapter.TransformaXMLaJavaSig;
import org.hl7.rim.Patient;
import org.hl7.rim.Person;
import org.hl7.rim.QueryByParameter;
import org.hmaciel.sisingr.ejb.converters.aEJB.ConvertidorEjb_Entity;
import org.hmaciel.sisingr.ejb.converters.aEJB.ConvertidorEjb_Roles;
import org.hmaciel.sisingr.ejb.datatypes.IIBean;
import org.hmaciel.sisingr.ejb.datatypes.II_Query;
import org.hmaciel.sisingr.ejb.entity.PersonBean;
import org.hmaciel.sisingr.ejb.querys.ParameterItemBean;
import org.hmaciel.sisingr.ejb.querys.SemanticNames;
import org.hmaciel.sisingr.ejb.role.PatientBean;
import org.hmaciel.sisingr.ejb.role.RoleBean;
import org.opensih.webservices.ejb.InterfaceInvocadorWS;

@Stateless
public class BuscarPaciente implements IBuscarPacientes {

    @EJB
    IControladorQuery logicaQuery;

    @EJB
    private static InterfaceInvocadorWS ws;

    @SuppressWarnings("unchecked")
    public PatientBean buscarPaciente(List<ParameterItemBean> listpib) {
        List<Patient> listapacientes = new LinkedList<Patient>();
        LinkedList<PatientBean> listpatBean = new LinkedList<PatientBean>();
        Date ext = new Date();
        String extQuery = ext.getTime() + "";
        if (!listpib.isEmpty()) {
            QueryByParameter qbp = logicaQuery.crearQuery(listpib, extQuery);
            TransformaJavaSigAXML tjs_XML = TransformaJavaSigAXML.getInstance();
            String xmlrequest = tjs_XML.queryByParameterPDQToXML(qbp, 0);
            String xmlrespuesta = "";
            xmlrespuesta = ws.consultaRepositorio(xmlrequest);
            TransformaXMLaJavaSig tXML_js = TransformaXMLaJavaSig.getInstance();
            listapacientes = tXML_js.XML2Jsig(xmlrespuesta);
            if (!listapacientes.isEmpty()) {
                for (Iterator iter = listapacientes.iterator(); iter.hasNext(); ) {
                    Patient pat = (Patient) iter.next();
                    Person per = (Person) pat.getPlayer();
                    ConvertidorEjb_Entity converter = ConvertidorEjb_Entity.getInstance();
                    PersonBean perBean = converter.convertirPersona(per);
                    ConvertidorEjb_Roles converter2 = ConvertidorEjb_Roles.getInstance();
                    PatientBean patBean = converter2.convertirPaciente(pat);
                    List<RoleBean> roles = new LinkedList<RoleBean>();
                    roles.add(patBean);
                    patBean.setPlayer(perBean);
                    perBean.setPlayedRole(roles);
                    listpatBean.add(patBean);
                }
                return listpatBean.get(0);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<PatientBean> buscarPacientePaginado(List<ParameterItemBean> listpib, String extQuery, int pagina) {
        List<Patient> listapacientes = new LinkedList<Patient>();
        LinkedList<PatientBean> listpatBean = new LinkedList<PatientBean>();
        if (!listpib.isEmpty()) {
            QueryByParameter qbp = logicaQuery.crearQuery(listpib, extQuery);
            TransformaJavaSigAXML tjs_XML = TransformaJavaSigAXML.getInstance();
            String xmlrequest = tjs_XML.queryByParameterPDQToXML(qbp, pagina);
            String xmlrespuesta = "";
            xmlrespuesta = ws.consultaRepositorio(xmlrequest);
            TransformaXMLaJavaSig tXML_js = TransformaXMLaJavaSig.getInstance();
            listapacientes = tXML_js.XML2Jsig(xmlrespuesta);
            if (!listapacientes.isEmpty()) {
                for (Iterator iter = listapacientes.iterator(); iter.hasNext(); ) {
                    Patient pat = (Patient) iter.next();
                    Person per = (Person) pat.getPlayer();
                    ConvertidorEjb_Entity converter = ConvertidorEjb_Entity.getInstance();
                    PersonBean perBean = converter.convertirPersona(per);
                    ConvertidorEjb_Roles converter2 = ConvertidorEjb_Roles.getInstance();
                    PatientBean patBean = converter2.convertirPaciente(pat);
                    List<RoleBean> roles = new LinkedList<RoleBean>();
                    roles.add(patBean);
                    patBean.setPlayer(perBean);
                    perBean.setPlayedRole(roles);
                    listpatBean.add(patBean);
                }
            }
        }
        return listpatBean;
    }

    @SuppressWarnings("unchecked")
    public void contBuscarPacPaginado(String extQuery, List<PatientBean> listpatBean, int pagina) {
        List<Patient> listapacientes = new LinkedList<Patient>();
        String xmlrespuesta = "";
        String xmlrequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "<QUQI_IN000003UV01 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../../schema/HL7V3/multicacheschemas/QUQI_IN000003UV01.xsd\" xmlns=\"urn:hl7-org:v3\" ITSVersion=\"XML_1.0\">" + "\n" + "<id root=\"1.2.840.114350.1.13.0.1.7.1.1\" extension=\"35424\" />" + "\n" + "<creationTime value=\"20070428150303\" />" + "\n" + "<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"QUQI_IN000003UV01\" />" + "\n" + "<processingCode code=\"T\" />" + "\n" + "<processingModeCode code=\"I\" />" + "\n" + "<acceptAckCode code=\"AL\" />" + "\n" + "<receiver typeCode=\"RCV\">" + "\n" + "<device determinerCode=\"INSTANCE\">" + "\n" + "<id root=\"1.2.840.114350.1.13.999.234\" />" + "\n" + "<telecom value=\"http://servicelocation/PDQuery\" />" + "\n" + "</device>" + "\n" + "</receiver>" + "\n" + "<sender typeCode=\"SND\">" + "\n" + "<device determinerCode=\"INSTANCE\">" + "\n" + "<id root=\"1.2.840.114350.1.13.999.567\" />" + "\n" + "</device>" + "\n" + "</sender>" + "\n" + "<controlActProcess moodCode=\"RQO\">" + "\n" + "<queryContinuation>" + "\n" + "<queryId root=\"1.2.840.114350.1.13.28.1.18.5.999\" extension=\"" + extQuery + "\" />" + "\n" + "<continuationQuantity value=\"" + pagina + "\" />" + "\n" + "<statusCode code=\"waitContinuedQueryResponse\" />" + "\n" + "</queryContinuation>" + "\n" + "</controlActProcess>" + "\n" + "</QUQI_IN000003UV01>" + "\n";
        xmlrespuesta = ws.consultaContinuada(xmlrequest);
        TransformaXMLaJavaSig tXML_js = TransformaXMLaJavaSig.getInstance();
        listapacientes = tXML_js.XML2Jsig(xmlrespuesta);
        if (!listapacientes.isEmpty()) {
            for (Iterator iter = listapacientes.iterator(); iter.hasNext(); ) {
                Patient pat = (Patient) iter.next();
                Person per = (Person) pat.getPlayer();
                ConvertidorEjb_Entity converter = ConvertidorEjb_Entity.getInstance();
                PersonBean perBean = converter.convertirPersona(per);
                ConvertidorEjb_Roles converter2 = ConvertidorEjb_Roles.getInstance();
                PatientBean patBean = converter2.convertirPaciente(pat);
                List<RoleBean> roles = new LinkedList<RoleBean>();
                roles.add(patBean);
                patBean.setPlayer(perBean);
                perBean.setPlayedRole(roles);
                listpatBean.add(patBean);
            }
        }
    }

    public void cancelarConsultaPaginada(String extensionQuery) {
        System.out.println("cancelar " + extensionQuery);
        String xmlRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "<QUQI_IN000003UV01_Cancel xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../../schema/HL7V3/multicacheschemas/QUQI_IN000003UV01.xsd\" xmlns=\"urn:hl7-org:v3\" ITSVersion=\"XML_1.0\">" + "\n" + "<id root=\"1.2.840.114350.1.13.0.1.7.1.1\" extension=\"35425\" />" + "\n" + "<creationTime value=\"20070428150306\" />" + "\n" + "<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"QUQI_IN000003UV01\" />" + "\n" + "<processingCode code=\"T\" />" + "\n" + "<processingModeCode code=\"I\" />" + "\n" + "<acceptAckCode code=\"AL\" />" + "\n" + "<receiver typeCode=\"RCV\">" + "\n" + "<device determinerCode=\"INSTANCE\">" + "\n" + "<id root=\"1.2.840.114350.1.13.999.234\" />" + "\n" + "<telecom value=\"http://servicelocation/PDQuery\" />" + "\n" + "</device>" + "\n" + "</receiver>" + "\n" + "<sender typeCode=\"SND\">" + "\n" + "<device determinerCode=\"INSTANCE\">" + "\n" + "<id root=\"1.2.840.114350.1.13.999.567\" />" + "\n" + "</device>" + "\n" + "</sender>" + "\n" + "<controlActProcess moodCode=\"RQO\">" + "\n" + "<queryContinuation>" + "\n" + "<queryId root=\"1.2.840.114350.1.13.28.1.18.5.999\" extension=\"" + extensionQuery + "\" />" + "\n" + "<statusCode code=\"aborted\" />" + "\n" + "</queryContinuation>" + "\n" + "</controlActProcess>" + "\n" + "</QUQI_IN000003UV01_Cancel>" + "\n";
        ws.cancelarConsulta(xmlRequest);
    }

    public boolean verificarExistencia(String cipat, String root) {
        PatientBean patBean = null;
        List<ParameterItemBean> listpib = new LinkedList<ParameterItemBean>();
        ParameterItemBean livingSubjectId = new ParameterItemBean();
        IIBean iib = new II_Query();
        iib.setExtension(cipat);
        iib.setAssigningAuthorityName("Ministerio del Interior");
        iib.setDisplayable("true");
        iib.setRoot(root);
        livingSubjectId.setValue(iib);
        livingSubjectId.setSemanticText(SemanticNames.LIVING_SUBJECT_ID);
        IIBean ii = new II_Query();
        ii.setRoot("2.16.840.656.3");
        ii.setExtension("70707070");
        livingSubjectId.setIi(ii);
        listpib.add(livingSubjectId);
        try {
            patBean = this.buscarPaciente(listpib);
            if (patBean != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex2) {
            return false;
        }
    }
}
