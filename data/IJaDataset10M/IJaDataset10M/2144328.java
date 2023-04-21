package net.sf.istcontract.analyserfrontend.server.serverservice;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.net.MalformedURLException;
import java.net.URL;
import net.sf.istcontract.aws.input.domain.AgentInfo;
import net.sf.istcontract.aws.stubs.AgentEndpoint.AgentEndpoint;
import net.sf.istcontract.aws.stubs.AgentEndpoint.AgentEndpointService;
import net.sf.istcontract.aws.stubs.AgentFactory.AgentFactory;
import net.sf.istcontract.aws.stubs.AgentFactory.AgentFactoryService;
import net.sf.istcontract.aws.analyser.ontology.AnalyserCommand;
import net.sf.istcontract.aws.analyser.ontology.ContractDataCollected;
import net.sf.istcontract.aws.analyser.ontology.MonitorContractDetails;
import net.sf.istcontract.aws.analyser.ontology.ObserverDataCollected;

/**
 *
 * @author hodik
 */
public class AWSPlatformConnector implements AAConnectorInterface {

    private AgentInfo analyserAgentDetails = null;

    private AgentEndpoint endPoint = null;

    private boolean subscriptionAllowed = false;

    private static final String TESTER_DO_GET_FROM_REPOSITORY = "get-data-from-repository";

    private static final String TESTER_DO_GET_FROM_OBSERVER = "get-data-from-observer";

    private static final String SUBSCRIBE_OBSERVER = "subscribeObserver";

    private static final String UN_SUBSCRIBE_OBSERVER = "unSubscribeObserver";

    private static final String SUBSCRIBE_MONITOR = "subscribeMonitor";

    private String analyserKeyContractRepository = "analyserKeyContractRepository";

    private String analyserKeyObserver = "analyserKeyObserver";

    private AnalyserInnerData analyserInnerData;

    HashMap<String, List<String>> contractDataSet = new HashMap<String, List<String>>();

    public void setAnalyserInnerData(AnalyserInnerData analyserInnerData) {
        this.analyserInnerData = analyserInnerData;
    }

    public boolean setAgentInfo(String agentID, String agentIP, String agentPORT) {
        analyserAgentDetails = new AgentInfo();
        analyserAgentDetails.setAgentId("" + agentID);
        analyserAgentDetails.setIpAddress("" + agentIP);
        analyserAgentDetails.setPort("" + agentPORT);
        try {
            endPoint = this.getAgentEndpoint(analyserAgentDetails);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        System.out.println("AFE: AgentInfo set: IP = " + analyserAgentDetails.getIpAddress() + ", PORT = " + analyserAgentDetails.getPort() + ", ID = " + analyserAgentDetails.getAgentId());
        if (endPoint != null) {
            return true;
        } else {
            return false;
        }
    }

    private AgentEndpoint getAgentEndpoint(AgentInfo ai) throws MalformedURLException {
        if (ai == null) {
            return null;
        }
        AgentEndpointService aes;
        AgentEndpoint ae;
        AgentFactoryService afs;
        AgentFactory af;
        W3CEndpointReference er;
        try {
            afs = new AgentFactoryService(new URL("http://" + ai.getIpAddress() + ":" + ai.getPort() + "/AgentWebServiceLib/AgentFactoryService?wsdl"), new QName("http://aws.istcontract.sf.net/", "AgentFactoryService"));
            if (afs == null) {
                return null;
            }
            af = afs.getAgentFactoryPort();
            if (af == null) {
                return null;
            }
            er = af.createAgent(ai.getAgentId());
            if (er == null) {
                return null;
            }
            aes = new AgentEndpointService(new URL("http://" + ai.getIpAddress() + ":" + ai.getPort() + "/AgentWebServiceLib/AgentEndpointService?wsdl"), new QName("http://aws.istcontract.sf.net/", "AgentEndpointService"));
            if (aes == null) {
                return null;
            }
            ae = aes.getPort(er, AgentEndpoint.class);
            return ae;
        } catch (javax.xml.ws.soap.SOAPFaultException ex) {
            System.err.println("AFE: SOAPFaultException in getAgentEndpoint, AgentEndpoint set to null");
            return null;
        }
    }

    public void updateFromContractRepository(String repositoryURI) {
        String report = TESTER_DO_GET_FROM_REPOSITORY;
        String defaultURI = repositoryURI;
        try {
            if (endPoint == null) {
                return;
            }
            AnalyserCommand aCommand = new AnalyserCommand();
            aCommand.setCommand(defaultURI);
            final JAXBContext jc = JAXBContext.newInstance(AnalyserCommand.class);
            final Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            m.marshal(aCommand, writer);
            endPoint.provideData(analyserKeyContractRepository, "" + writer, report);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("AFE: error in connection to agent platform");
        }
    }

    public void addContractForMonitoring(String contractID, List<String> clausesIDList) {
        contractDataSet.put(contractID, clausesIDList);
        subscribeForMonitoring();
    }

    private void subscribeForMonitoring() {
        if (subscriptionAllowed) {
            synchronized (contractDataSet) {
                updateFromMonitor(contractDataSet);
                contractDataSet.clear();
            }
        }
    }

    private void updateFromMonitor(HashMap<String, List<String>> contractDataSet) {
        String report = TESTER_DO_GET_FROM_OBSERVER;
        String commandData = SUBSCRIBE_MONITOR;
        System.out.println("updateFromMonitor 1. commandData = " + commandData);
        try {
            if (endPoint == null) {
                return;
            }
            System.out.println("updateFromMonitor 2.");
            final JAXBContext jc = JAXBContext.newInstance(AnalyserCommand.class);
            final Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            System.out.println("updateFromMonitor 3.");
            AnalyserCommand aCommand = new AnalyserCommand();
            aCommand.setCommand(commandData);
            for (Entry<String, List<String>> contractData : contractDataSet.entrySet()) {
                MonitorContractDetails mcd = new MonitorContractDetails();
                mcd.setId(contractData.getKey());
                mcd.getClauses().addAll(contractData.getValue());
                aCommand.getMonitorContractDetails().add(mcd);
                System.out.println("updateFromMonitor 4. contractID: " + contractData.getKey() + " No. of clauses: " + contractData.getValue().size());
            }
            StringWriter writer = new StringWriter();
            m.marshal(aCommand, writer);
            System.out.println("updateFromMonitor 5.");
            endPoint.provideData(analyserKeyObserver, "" + writer, report);
            System.out.println("updateFromMonitor 6.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("AFE: error in connection to agent platform");
        }
    }

    public void updateFromObserver(boolean subscribeOrUnsubscribe) {
        subscriptionAllowed = subscribeOrUnsubscribe;
        this.subscribeForMonitoring();
        String report = TESTER_DO_GET_FROM_OBSERVER;
        String commandData = ((subscribeOrUnsubscribe) ? SUBSCRIBE_OBSERVER : UN_SUBSCRIBE_OBSERVER);
        String key = "analyserKeyObserver";
        try {
            if (endPoint == null) {
                return;
            }
            AnalyserCommand aCommand = new AnalyserCommand();
            aCommand.setCommand(commandData);
            final JAXBContext jc = JAXBContext.newInstance(AnalyserCommand.class);
            final Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            m.marshal(aCommand, writer);
            endPoint.provideData(key, "" + writer, report);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("AFE: error in connection to agent platform");
        }
    }

    public void getReportsFromObserver() {
        String key = "analyserKeyObserver";
        if (endPoint == null) {
            return;
        }
        String result = (String) endPoint.requestData(key, ObserverDataCollected.class.getName(), "DataPercept");
        ObserverDataCollected observerDataCollected = null;
        if (result != null) {
            try {
                JAXBContext jcCDL = JAXBContext.newInstance(ObserverDataCollected.class);
                Unmarshaller u = jcCDL.createUnmarshaller();
                Object output = u.unmarshal(new StringReader(result));
                observerDataCollected = (ObserverDataCollected) output;
            } catch (JAXBException ex) {
                System.err.println("AFE: Cannot unmarshal " + result);
            }
        }
        analyserInnerData.updateFromObserver(observerDataCollected);
    }

    public void getXMLFilesFromContractRepository() {
        String key = "analyserKeyContractRepository";
        if (endPoint == null) {
            return;
        }
        String result = (String) endPoint.requestData(key, ContractDataCollected.class.getName(), "DataPercept");
        ContractDataCollected contractDataCollected = null;
        if (result != null) {
            try {
                JAXBContext jcCDL = JAXBContext.newInstance(ContractDataCollected.class);
                Unmarshaller u = jcCDL.createUnmarshaller();
                Object output = u.unmarshal(new StringReader(result));
                contractDataCollected = (ContractDataCollected) output;
            } catch (JAXBException ex) {
                System.err.println("AFE: Cannot unmarshal " + result);
            }
        }
        analyserInnerData.updateFromCR(contractDataCollected);
    }
}
