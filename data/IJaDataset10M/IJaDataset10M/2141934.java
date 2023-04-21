package groupcomm.api.dynabcast;

/**
 * This interface defines the methods to use by applications to interact with a Dynamic Atomic Broadcast protocol stack
 * with a adaptive consensus layer
 *  
 * @author Olivier Rutti
 *
 */
public interface ApiDynAbcastWithReplaceableProtocolStack extends ApiDynAbcastStack {

    /**
     * This method tells the stack to replace the current Consensus Layer. We have the possiblity to
     * choose between "Paxos", "CT" for Chandra-Toueg and "MR" for Most�afoui-Raynal
     * The stack will use the <i>newProtocol</i> callback to notify the change of consensus 
     * protocol.
     * 
     * @param name the name("Paxos","CT","MR") of the new consensus layer
     */
    public void sendReplaceConsensus(String name);

    /**
     * This method tells the stack to replace the current Abcast Layer. We have the possiblity to
     * choose between "CT" for Chandra-Toueg and ... (TO BE COMPLETED)
     * The stack will use the <i>newProtocol</i> callback to notify the change of abcast
     * protocol.
     * 
     * @param name the name("CT",...) of the new abcast layer
     */
    public void sendReplaceAbcast(String name);

    /**
     * This method tells the stack to replace the current GMP Layer. We have the possiblity to
     * choose between "GMP" for GMP based on Abcast and ... (TO BE COMPLETED)
     * The stack will use the <i>newProtocol</i> callback to notify the change of gmp
     * protocol.
     * 
     * @param name the name("GMP",...) of the new gmp layer
     */
    public void sendReplaceGmp(String name);
}
