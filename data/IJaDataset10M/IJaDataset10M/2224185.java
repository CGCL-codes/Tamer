package org.cyberaide.emolst.clients;

import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.cyberaide.emolst.app.Configuration;
import org.cyberaide.emolst.interfaces.PatientInterface;
import org.cyberaide.emolst.jdbc.PatientBean;

/**
 * Implements the PatientInterface with a client that accesses the
 * deployed Web Services to deliver patient information
 * 
 */
public class PatientWSClient implements PatientInterface {

    private final String endPtRef = Configuration.getProp("services_host") + "PatientService";

    public PatientBean getPatientInfo(int patientKey) {
        RPCServiceClient serviceClient;
        PatientBean patient = new PatientBean();
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperationPatient = new QName("http://services.emolst.cyberaide.org", "getPatientInfo");
            Object[] patientArg = new Object[] { patientKey };
            Class[] returnTypes = new Class[] { PatientBean.class };
            Object[] response = serviceClient.invokeBlocking(serviceOperationPatient, patientArg, returnTypes);
            patient = (PatientBean) response[0];
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return patient;
    }

    public PatientBean[] getPatientsByDoctor(int docKey) {
        RPCServiceClient serviceClient;
        PatientBean[] patients = null;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperationPatient = new QName("http://services.emolst.cyberaide.org", "getPatientsByDoctor");
            Object[] docArg = new Object[] { docKey };
            Class[] returnTypes = new Class[] { PatientBean[].class };
            Object[] response = serviceClient.invokeBlocking(serviceOperationPatient, docArg, returnTypes);
            patients = (PatientBean[]) response[0];
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return patients;
    }

    public int createPatient(PatientBean patient) {
        RPCServiceClient serviceClient;
        int patientID = -1;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperation = new QName("http://services.emolst.cyberaide.org", "createPatient");
            Object[] arguments = new Object[] { patient };
            Class[] returnTypes = new Class[] { Integer.class };
            Object[] response = serviceClient.invokeBlocking(serviceOperation, arguments, returnTypes);
            patientID = (Integer) response[0];
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return patientID;
    }

    public void deletePatient(int patientID) {
        RPCServiceClient serviceClient;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperation = new QName("http://services.emolst.cyberaide.org", "deletePatient");
            Object[] arguments = new Object[] { patientID };
            serviceClient.invokeRobust(serviceOperation, arguments);
        } catch (AxisFault e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(PatientBean patient) {
        RPCServiceClient serviceClient;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperation = new QName("http://services.emolst.cyberaide.org", "updatePatient");
            Object[] arguments = new Object[] { patient };
            serviceClient.invokeRobust(serviceOperation, arguments);
        } catch (AxisFault e) {
            e.printStackTrace();
        }
    }

    public PatientBean[] getPatients(String lname, String fname, String address, String city, String state, String zipcode, String gender, int offset, int limit, String col, boolean ascending, int fkDoctor) {
        RPCServiceClient serviceClient;
        PatientBean[] patients = null;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperation = new QName("http://services.emolst.cyberaide.org", "getPatients");
            Object[] arguments = new Object[] { lname, fname, address, city, state, zipcode, gender, offset, limit, col, ascending, fkDoctor };
            Class[] returnTypes = new Class[] { PatientBean[].class };
            Object[] response = serviceClient.invokeBlocking(serviceOperation, arguments, returnTypes);
            patients = (PatientBean[]) response[0];
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return patients;
    }

    public int getPatientsCount(String lname, String fname, String address, String city, String state, String zipcode, String gender, int fkDoctor) {
        RPCServiceClient serviceClient;
        int rowCount = 0;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(endPtRef);
            options.setTo(targetEPR);
            QName serviceOperation = new QName("http://services.emolst.cyberaide.org", "getPatientsCount");
            Object[] arguments = new Object[] { lname, fname, address, city, state, zipcode, gender, fkDoctor };
            Class[] returnTypes = new Class[] { Integer.class };
            Object[] response = serviceClient.invokeBlocking(serviceOperation, arguments, returnTypes);
            rowCount = (Integer) response[0];
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return rowCount;
    }
}
