package net.sf.istcontract.analyserfrontend.server.serverservice;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.istcontract.analyserfrontend.client.clientservice.GWTService;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.PartyDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ActionDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.MessageDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.PredicateDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ClauseDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ContractOverview;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ClauseReport;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ContractDescription;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ContractReportDescription;

/**
 *
 * @author hodik
 */
public class AnalyserServiceImpl extends RemoteServiceServlet implements GWTService {

    private AnalyserAgentConnector agentConnector;

    private static AnalyserInnerData innerData = null;

    private static Boolean isPassiveInterface = false;

    public AnalyserServiceImpl() {
        innerData = new AnalyserInnerData();
        agentConnector = new AnalyserAgentConnector();
        innerData.setAgentConnector(agentConnector);
        agentConnector.setAnalyserInnerData(innerData);
    }

    public HashMap<String, Integer> getExecutionContractsClausesStatuses() {
        return innerData.getExecutionContractsClausesStatuses();
    }

    public static boolean addActionDescription(final String iD, final String timeStamp, final String senderID, final String receiverID, final String message) {
        if (isPassiveInterface) {
            ActionDescription actiondescription = new ActionDescription(iD, timeStamp, senderID, receiverID, message);
            innerData.addActionDescription(actiondescription);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addPredicateDescription(final String iD, final String timeStamp, final List<String> params, final Boolean value) {
        if (isPassiveInterface) {
            PredicateDescription predicateDescription = new PredicateDescription(iD, timeStamp, params, value);
            innerData.addPredicateDescription(predicateDescription);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addMessageDescription(final String iD, final String timeStamp, final String senderID, final String receiverID, final String message, final String performative) {
        if (isPassiveInterface) {
            MessageDescription messageDescription = new MessageDescription(iD, timeStamp, senderID, receiverID, message, performative);
            innerData.addMessageDescription(messageDescription);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addClauseReport(final String iD, final String timeStamp, final String contractID, final String status) {
        if (isPassiveInterface) {
            ClauseReport clauseReport = new ClauseReport(iD, timeStamp, contractID, status);
            innerData.addClauseReport(clauseReport);
            return true;
        } else {
            return false;
        }
    }

    public static boolean updateContract(final String timeStamp, final String contractString, final String contractID, final String status) {
        if (isPassiveInterface) {
            ContractDataBundle contractBundle = new ContractDataBundle(contractString, contractID, status);
            innerData.updateContract(contractBundle);
            innerData.addContractReport(timeStamp, contractID, status);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeContract(final String timeStamp, final String contractID) {
        if (isPassiveInterface) {
            innerData.removeContract(contractID);
            String statusLF = "deleted";
            innerData.addContractReport(timeStamp, contractID, statusLF);
            return true;
        } else {
            return false;
        }
    }

    public void setInterfacePassive(boolean value) {
        synchronized (isPassiveInterface) {
            isPassiveInterface = value;
            innerData.setIsPassiveInterface(value);
            agentConnector.setIsPassiveInterface(value);
            System.out.println("AFE: setInterfacePassive: " + isPassiveInterface);
        }
    }

    public void resetInnerDataStores() {
        innerData.resetInnerDataStores();
    }

    public static boolean resetIDSws() {
        if (isPassiveInterface) {
            innerData.resetInnerDataStores();
            return true;
        } else {
            return false;
        }
    }

    public boolean setAgentInfo(String agentID, String agentIP, String agentPORT) {
        if (!isPassiveInterface) {
            return agentConnector.setAgentInfo(agentID, agentIP, agentPORT);
        } else {
            return false;
        }
    }

    public List<ContractOverview> getContractOverviews() {
        return innerData.getContractOverviews();
    }

    public List<String> getContractIDsList() {
        return innerData.getContractIDsList();
    }

    public ContractOverview getContractOverview(String contractID) {
        return innerData.getContractOverview(contractID);
    }

    public List<String> getPartyNamesList() {
        return innerData.getPartyNamesList();
    }

    public PartyDescription getContractParty(String selectedName) {
        PartyDescription pd = innerData.getContractParty(selectedName);
        return pd;
    }

    public List<PartyDescription> getContractPartiesList() {
        return innerData.getContractPartiesList();
    }

    public List<ClauseDescription> getPartyClausesList(String selectedName) {
        return innerData.getPartyClausesList(selectedName);
    }

    public String getXMLFileAddress(String contractID) {
        String toReturn = "under reconstruction XMLFileAddress";
        return toReturn;
    }

    public String getXMLtext(String contractID) {
        return innerData.getXMLtext(contractID);
    }

    public void updateFromContractRepository(boolean startOrStop, final String repositoryURI) {
        if (!isPassiveInterface) {
            agentConnector.updateFromContractRepository(startOrStop, repositoryURI);
        } else {
        }
    }

    public void updateFromObserver(boolean subscribeOrUnsubscribe) {
        if (!isPassiveInterface) {
            agentConnector.updateFromObserver(subscribeOrUnsubscribe);
        } else {
        }
    }

    public void getXMLFilesFromContractRepository(boolean startOrStop) {
        if (!isPassiveInterface) {
            agentConnector.getXMLFilesFromContractRepository(startOrStop);
        } else {
        }
    }

    public void startGetFromObserver(boolean startOrStop) {
        if (!isPassiveInterface) {
            agentConnector.startGetFromObserver(startOrStop);
        } else {
        }
    }

    public List<ClauseDescription> getContractClausesList(String selectedContractID) {
        return innerData.getContractClausesList(selectedContractID);
    }

    public List<PredicateDescription> getPredicateDescriptions() {
        return innerData.getPredicateDescriptions();
    }

    public List<ActionDescription> getActionDescriptions() {
        return innerData.getActionDescriptions();
    }

    public List<MessageDescription> getMessageDescriptions() {
        return innerData.getMessageDescriptions();
    }

    public List<ContractReportDescription> getContractReportDescriptions() {
        return innerData.getContractReportDescriptions();
    }

    public List<ClauseReport> getClauseReports() {
        return innerData.getClauseReports();
    }
}
