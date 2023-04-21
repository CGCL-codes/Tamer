package behaviours;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import ontologies.eBookContract;
import edu.upc.lsi.kemlg.aws.AgentInterfaceService;
import edu.upc.lsi.kemlg.aws.AgentParser;
import edu.upc.lsi.kemlg.aws.SimplePercept;
import edu.upc.lsi.kemlg.aws.communication.performative.ConsentSuggestion;
import edu.upc.lsi.kemlg.aws.communication.performative.DismissSuggestion;
import edu.upc.lsi.kemlg.aws.communication.performative.Performative;
import edu.upc.lsi.kemlg.aws.communication.performative.Propose;
import edu.upc.lsi.kemlg.aws.communication.performative.Suggest;
import edu.upc.lsi.kemlg.aws.configuration.AgentConfigurationProvider;
import edu.upc.lsi.kemlg.aws.configuration.InvalidAgentConfiguration;
import edu.upc.lsi.kemlg.aws.engine.AgentKernel;
import edu.upc.lsi.kemlg.aws.exception.AgentException;
import edu.upc.lsi.kemlg.aws.input.domain.AgentInfo;
import edu.upc.lsi.kemlg.aws.input.domain.AgentScenario;
import edu.upc.lsi.kemlg.aws.io.Percept;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;
import edu.upc.lsi.kemlg.aws.reasoning.behaviour.Behaviour;
import edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceServiceService;
import edu.upc.lsi.kemlg.aws.stubs.AgentInterfaceFactory.AgentInterfaceFactoryServiceService;
import edu.upc.lsi.kemlg.aws.utils.AgentLogger;
import edu.upc.lsi.kemlg.aws.utils.MessageIDGenerator;

/**
 * 
 * @author roberto
 */
class UpcPhdStudentBehaviour extends Behaviour {

    public final String AGENT_ID = "UPC";

    public final String NAME = "_name";

    public final String E_MAIL = "_eMail";

    public final String E_BOOK_TITLE = "_eBookTitle";

    public final String PRICE = "_price";

    public final String BANK_ACCOUNT = "_bankAccount";

    public final String CUSTOMER_ID = "_customerId";

    public final String RETRIEVE_FROM = "_retrieveFrom";

    public final String STATUS = "_status";

    public final String E_BOOK_CONTENT = "_eBookContent";

    public final String CMD_ACCEPT = "_commandAccept";

    public final String CMD_REJECT = "_commandRejected";

    public final String CMD_SUGGEST = "_commandSuggest";

    public UpcPhdStudentBehaviour() {
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean isSuitable(Percept percept) {
        boolean result = false;
        if ((percept instanceof Propose) || (percept instanceof SimplePercept && ((SimplePercept) percept).getStInput().startsWith(AGENT_ID))) {
            result = true;
        }
        return result;
    }

    @Override
    protected void runRequest(AgentKernel comm) throws AgentException {
        Performative p;
        edu.upc.lsi.kemlg.aws.stubs.AgentInterfaceFactory.AgentInterfaceFactoryServiceService service;
        edu.upc.lsi.kemlg.aws.stubs.AgentInterfaceFactory.AgentInterfaceFactoryService port;
        edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceServiceService service_1;
        edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceService iface = null;
        try {
            AgentInfo ai = AgentConfigurationProvider.getConfiguration().getDirectoryFacilitator().getAgentInfo("UPC");
            String stURLReceiver = "http://" + ai.getIpAddress() + ":" + ai.getPort() + "/AgentWebServiceLib/AgentInterfaceFactoryService?WSDL";
            URL baseUrl = edu.upc.lsi.kemlg.aws.stubs.AgentInterfaceFactory.AgentInterfaceFactoryServiceService.class.getResource(".");
            URL url = new URL(baseUrl, stURLReceiver);
            AgentLogger.log(url.toString());
            AgentInterfaceFactoryServiceService.AGENTINTERFACEFACTORYSERVICESERVICE_WSDL_LOCATION = url;
            service = new edu.upc.lsi.kemlg.aws.stubs.AgentInterfaceFactory.AgentInterfaceFactoryServiceService();
            port = service.getAgentInterfaceFactoryServicePort();
            javax.xml.ws.wsaddressing.W3CEndpointReference result = port.createAgentInterface();
            stURLReceiver = "http://" + ai.getIpAddress() + ":" + ai.getPort() + "/AgentWebServiceLib/AgentInterfaceService?WSDL";
            baseUrl = edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceServiceService.class.getResource(".");
            url = new URL(baseUrl, stURLReceiver);
            AgentLogger.log(url.toString());
            AgentInterfaceServiceService.AGENTINTERFACESERVICESERVICE_WSDL_LOCATION = url;
            service_1 = new edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceServiceService();
            iface = service_1.getPort(result, edu.upc.lsi.kemlg.aws.stubs.AgentInterface.AgentInterfaceService.class);
            AgentLogger.log("Endpoint for AgentInterfaceService = " + result);
        } catch (Exception ex) {
            AgentLogger.log(ex.getMessage());
            ex.printStackTrace();
        }
        if (percept instanceof Performative) {
            p = (Performative) percept;
            if (p instanceof Propose) {
                eBookContract eBookContract = (eBookContract) p.getContent().getConcept();
                comm.getBeliefBase().put(AGENT_ID + NAME, eBookContract.getName());
                comm.getBeliefBase().put(AGENT_ID + E_MAIL, eBookContract.getEmail());
                comm.getBeliefBase().put(AGENT_ID + E_BOOK_TITLE, eBookContract.getEBookTitle());
                comm.getBeliefBase().put(AGENT_ID + BANK_ACCOUNT, eBookContract.getBankAccount());
                comm.getBeliefBase().put(AGENT_ID + CUSTOMER_ID, eBookContract.getCustomerId());
                comm.getBeliefBase().put(AGENT_ID + PRICE, String.valueOf(eBookContract.getPrice()));
                comm.getBeliefBase().put(AGENT_ID + RETRIEVE_FROM, eBookContract.getRetrieveFrom());
                comm.getBeliefBase().put(AGENT_ID + STATUS, "2");
            }
        } else if (percept instanceof SimplePercept) {
            SimplePercept sp = (SimplePercept) percept;
            String s = sp.getStInput();
            if (s.equals(AGENT_ID + CMD_SUGGEST)) {
                AgentLogger.log("suggest");
                eBookContract eBookContract = new eBookContract();
                eBookContract.setName((String) iface.getSimpleProperty(AGENT_ID + NAME));
                eBookContract.setEmail(((String) iface.getSimpleProperty(AGENT_ID + E_MAIL)));
                eBookContract.setEBookTitle(((String) iface.getSimpleProperty(AGENT_ID + E_BOOK_TITLE)));
                AgentLogger.log(eBookContract.getName());
                AgentLogger.log(eBookContract.getEmail());
                AgentLogger.log(eBookContract.getEBookTitle());
                sendMessage("UPC", "CTU", Suggest.class, eBookContract);
            }
            if (s.equals(AGENT_ID + CMD_ACCEPT)) {
                AgentLogger.log("accsept");
                eBookContract eBookContract = new eBookContract();
                eBookContract.setName(((String) iface.getSimpleProperty(AGENT_ID + NAME)));
                eBookContract.setEmail(((String) iface.getSimpleProperty(AGENT_ID + E_MAIL)));
                eBookContract.setEBookTitle(((String) iface.getSimpleProperty(AGENT_ID + E_BOOK_TITLE)));
                eBookContract.setBankAccount(((String) iface.getSimpleProperty(AGENT_ID + BANK_ACCOUNT)));
                eBookContract.setCustomerId(((String) iface.getSimpleProperty(AGENT_ID + CUSTOMER_ID)));
                eBookContract.setPrice(Double.parseDouble((String) iface.getSimpleProperty(AGENT_ID + PRICE)));
                eBookContract.setRetrieveFrom(((String) iface.getSimpleProperty(AGENT_ID + RETRIEVE_FROM)));
                sendMessage("UPC", "CTU", ConsentSuggestion.class, eBookContract);
            }
            if (s.equals(AGENT_ID + CMD_REJECT)) {
                AgentLogger.log("dismiss");
                eBookContract eBookContract = new eBookContract();
                eBookContract.setName(((String) iface.getSimpleProperty(AGENT_ID + NAME)));
                eBookContract.setEmail(((String) iface.getSimpleProperty(AGENT_ID + E_MAIL)));
                eBookContract.setEBookTitle(((String) iface.getSimpleProperty(AGENT_ID + E_BOOK_TITLE)));
                eBookContract.setBankAccount(((String) iface.getSimpleProperty(AGENT_ID + BANK_ACCOUNT)));
                eBookContract.setCustomerId(((String) iface.getSimpleProperty(AGENT_ID + CUSTOMER_ID)));
                eBookContract.setPrice(Double.parseDouble((String) iface.getSimpleProperty(AGENT_ID + PRICE)));
                eBookContract.setRetrieveFrom(((String) iface.getSimpleProperty(AGENT_ID + RETRIEVE_FROM)));
                sendMessage("UPC", "CTU", DismissSuggestion.class, eBookContract);
            }
        }
    }

    protected void sendMessage(String sender, String receiver, Class<? extends Performative> performative, eBookContract bookContract) {
        String stURLReceiver;
        AgentInfo ai;
        URL url, baseUrl;
        W3CEndpointReference er;
        String content;
        Marshaller m;
        JAXBContext jc;
        ByteArrayOutputStream baos;
        edu.upc.lsi.kemlg.aws.stubs.AgentFactory.AgentFactoryService afs;
        edu.upc.lsi.kemlg.aws.stubs.AgentFactory.AgentFactory af;
        edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpointService aes;
        edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpoint dest;
        content = null;
        try {
            baos = new ByteArrayOutputStream();
            jc = JAXBContext.newInstance(eBookContract.class);
            m = jc.createMarshaller();
            m.marshal(bookContract, baos);
            content = new String(baos.toByteArray());
        } catch (JAXBException e2) {
            e2.printStackTrace();
        }
        System.out.println("Content: " + content);
        AgentLogger.logMessage(sender, receiver, performative.getSimpleName(), null, null, bookContract);
        try {
            ai = AgentConfigurationProvider.getConfiguration().getDirectoryFacilitator().getAgentInfo(receiver);
        } catch (InvalidAgentConfiguration e1) {
            ai = null;
            e1.printStackTrace();
        }
        stURLReceiver = "http://" + ai.getIpAddress() + ":" + ai.getPort() + "/AgentWebServiceLib/AgentFactoryService?WSDL";
        AgentLogger.log("sendmessage: " + stURLReceiver);
        url = null;
        try {
            baseUrl = edu.upc.lsi.kemlg.aws.stubs.AgentFactory.AgentFactoryService.class.getResource(".");
            url = new URL(baseUrl, stURLReceiver);
            AgentLogger.log(url.toString());
            edu.upc.lsi.kemlg.aws.stubs.AgentFactory.AgentFactoryService.AGENTFACTORYSERVICE_WSDL_LOCATION = url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        afs = new edu.upc.lsi.kemlg.aws.stubs.AgentFactory.AgentFactoryService();
        af = afs.getAgentFactoryPort();
        aes = new edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpointService();
        er = af.createAgent(receiver);
        AgentLogger.log(er.toString());
        dest = aes.getPort(er, edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpoint.class);
        if (performative.equals(ConsentSuggestion.class)) {
            dest.consentSuggestion(MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), sender, receiver);
        } else if (performative.equals(Suggest.class)) {
            dest.suggest(MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), sender, receiver, content);
        } else if (performative.equals(DismissSuggestion.class)) {
            dest.dismissSuggestion(MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), sender, receiver);
        }
    }
}
