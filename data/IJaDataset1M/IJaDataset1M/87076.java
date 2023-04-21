package infrastructureAPI.algorithms.instanceTools;

import gossipServices.basic.nodeDescriptors.ConcreteNodeDescriptor;
import gossipServices.basic.nodeDescriptors.GeneralNodeDescriptor;
import gossipServices.basic.nodeDescriptors.NodeDescriptor;
import gossipServices.basic.view.ConcretePartialView;
import gossipServices.basic.view.PartialView;
import gossipServices.pss.PeerSamplingServiceAPI;
import infrastructureAPI.Requester;
import infrastructureAPI.Service;
import infrastructureAPI.APIMessages.DispatcherRequestMessage;
import infrastructureAPI.APIMessages.RequestMessage;
import infrastructureAPI.algorithms.instanceTools.facilities.TManStarter;
import infrastructureAPI.backendGate.APIScript;
import java.net.InetAddress;
import java.util.List;
import java.util.Vector;
import networkRmi.NetworkManager;

/**
 * Description: 
 * This class implements the algorithm that build a subcloud.
 * The caller has to specify the subcloud id and the number
 * of nodes which need to be retrieved. 
 * The RunNodes extends the NodeRetrieval class and 
 * takes advantage of its retrieval algorithm.
 * It implements of course the APIFunction and the
 * APIScript.
 *
 */
public class RunNodes extends NodeRetrieval implements APIScript {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected String subCloudID;

    protected int numberOfNodes;

    protected static int mandatoryParameters = 2;

    protected PartialView initialTManView;

    public RunNodes() {
    }

    public RunNodes(String subCloudID, int numberOfNode, Requester requester) {
        super(requester);
        this.subCloudID = subCloudID;
        this.numberOfNodes = numberOfNode;
    }

    @Override
    public String showUsage() {
        String ret = new String("");
        ret = "USAGE: " + RunNodes.class.getSimpleName() + " <subcloudID> <num> \n" + "where num is the number of required nodes.";
        return ret;
    }

    @Override
    public int mandatoryParametersNumber() {
        return mandatoryParameters;
    }

    public String toString() {
        String ret = new String("");
        ret += "RunNodes ";
        ret += "subcloud: " + subCloudID;
        ret += " nodes: " + numberOfNodes;
        return ret;
    }

    @Override
    public RequestMessage createRequestMessage(InetAddress targetAddress, String targetNodeName) {
        NodeDescriptor sender = new ConcreteNodeDescriptor(targetNodeName, targetAddress, null);
        return new DispatcherRequestMessage(sender, sender, this, requester);
    }

    @Override
    public List<Class<?>> requiredServices() {
        List<Class<?>> requiredListServices = new Vector<Class<?>>();
        requiredListServices.add(NetworkManager.class);
        requiredListServices.add(PeerSamplingServiceAPI.class);
        return requiredListServices;
    }

    @Override
    public Service extractSuppliedService() {
        return null;
    }

    @Override
    public boolean requireSystemRegistration() {
        return false;
    }

    @Override
    public Service extractServiceToDeRegister() {
        return null;
    }

    @Override
    public boolean requireSystemDeRegistration() {
        return false;
    }

    @Override
    public boolean requireReply() {
        return true;
    }

    @Override
    public void run() {
        boolean success = false;
        NodeDescriptor localNode;
        RequestMessage propagatingRequestMsg;
        TManStarter tmanStarter;
        System.out.println(Thread.currentThread() + " is executing the " + this.getClass().getSimpleName() + " class");
        PeerSamplingServiceAPI pss;
        NetworkManager nm;
        nm = (NetworkManager) services.get(0);
        pss = (PeerSamplingServiceAPI) services.get(1);
        localNode = new GeneralNodeDescriptor(nm.getNodeName());
        List<NodeDescriptor> vec = retrieveNodesFromPeerSamplingService(nm, pss, numberOfNodes, localNode, new Vector<NodeDescriptor>());
        initialTManView = new ConcretePartialView(vec);
        propagatingRequestMsg = new DispatcherRequestMessage(localNode, null, null, requester);
        System.out.println("Starting subcloud: \"" + subCloudID + "\" with nodes:\n" + initialTManView);
        tmanStarter = new TManStarter(nm, initialTManView, initialTManView.size(), subCloudID, propagatingRequestMsg, initialTManView);
        success = tmanStarter != null;
        returnValue = success;
    }
}
