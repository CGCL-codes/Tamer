package spacewalklib;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcStruct;

/**
 *
 * RhnServer class represents a spacewalk or satellite server to quickly retrieve basic
 * information.
 *
 * @author Alfredo Moralejo
 *
 */
public class RhnServer {

    /** The connection to the satellite server */
    private RhnConn connection_;

    /** The list of systems in the spacewalk server */
    private RhnClients clients_;

    /** The list of software channels in the spacewalk server */
    private RhnSwChannels channels_;

    /** The list of configuration channels in the spacewalk server */
    private RhnConfigChannels config_channels_;

    /** The list of kickstart  the spacewalk server */
    private List<String> ks_profiles_ = new ArrayList<String>();

    /** The list of system groups in the spacewalk server */
    private RhnSystemsGroups system_groups_;

    /**
     *  Creates a new connection object with a spacewalk server
     *
     *  @param spacewalk the network name of the spacewalk or satellite server
     *  @param user the user name to establish the connection.
     *  @param password the password for the given name
	 *
	 *  @throws RhnConnFault
	 *
	 *
     */
    public RhnServer(String spacewalk, String user, String password) throws RhnConnFault {
        RhnConn connection = new RhnConn(spacewalk, user, password);
        this.connection_ = connection;
        this.loadInfo();
    }

    /**
     *  Creates a new object using an existing connection with a spacewalk server
     *
     *  @param connection a RhnConn connection with the spacewalk server
     *
	 *  @throws RhnConnFault
	 *
     */
    public RhnServer(RhnConn connection) throws RhnConnFault {
        this.connection_ = connection;
        this.loadInfo();
    }

    /**
	 *
	 * Read info from server and populates information in the object.
	 *
	 * @throws RhnConnFault
	 *
	 */
    public void loadInfo() throws RhnConnFault {
        this.clients_ = new RhnClients(this.connection_);
        this.channels_ = new RhnSwChannels(this.connection_);
        this.config_channels_ = new RhnConfigChannels(this.connection_);
        this.loadKsProfiles();
        this.system_groups_ = new RhnSystemsGroups(this.connection_);
    }

    /**
     *  @return the name of the spacewalk server.
     *
	 *  @throws RhnConnFault
	 *
     */
    public String getServer() {
        return this.connection_.getServer();
    }

    /**
     *  @return the user used for the connection to the spacewalk server.
     *
	 *  @throws RhnConnFault
	 *
     */
    public String getUser() {
        return this.connection_.getUser();
    }

    /**
     *  @return the list of names of the systems registered in the server.
	 *
	 *  @throws RhnConnFault
     *
     */
    public List<String> getClientNames() throws RhnConnFault {
        return this.clients_.getClients();
    }

    /**
     *  @return return the list of systems registered in the server in RhnClient objects.
	 *
	 *  @throws RhnConnFault
	 *  @throws RhnClientNotFoundException
     *
     */
    public List<RhnClient> getClients() throws RhnConnFault, RhnClientNotFoundException {
        return this.clients_.getRhnClients();
    }

    /**
     *  @return return the list of names of the software channels in the server.
	 *
	 *  @throws RhnConnFault
     *
     */
    public List<String> getChannelNames() throws RhnConnFault {
        return this.channels_.getNames();
    }

    /**
     *  @return return the list of labels of the software channels in the server.
	 *
	 *  @throws RhnConnFault
     *
     */
    public List<String> getChannelLabels() throws RhnConnFault {
        return this.channels_.getLabels();
    }

    /**
     *  @return return the list of systems registered in the server in RhnClient objects.
	 *
	 *  @throws RhnConnFault
	 *  @throws RhnClientNotFoundException
     *
     */
    public List<RhnSwChannel> getChannels() throws RhnConnFault, RhnClientNotFoundException {
        return this.channels_.getChannels();
    }

    /**
     *  @return return the list of names of the config channels in the server.
	 *
	 *  @throws RhnConnFault
	 *  @throws RhnChannelNotFoundException
     *
     */
    public List<String> getConfigChannelNames() throws RhnConnFault, RhnChannelNotFoundException {
        return this.config_channels_.getNames();
    }

    /**
     *  @return return the list of labels of the config channels in the server.
	 *
	 *  @throws RhnConnFault
	 *  @throws RhnChannelNotFoundException
     *
     */
    public List<String> getConfigChannelLabels() throws RhnConnFault, RhnChannelNotFoundException {
        return this.config_channels_.getLabels();
    }

    /**
     *  Load the list of kickstart profiles in the server.
	 *
	 *  @throws RhnConnFault
     *
     */
    public void loadKsProfiles() throws RhnConnFault {
        this.ks_profiles_.clear();
        try {
            List args = new ArrayList();
            args.add(this.connection_.getId());
            XmlRpcClient client_ = new XmlRpcClient(this.connection_.getServer(), false);
            List<XmlRpcStruct> profiles = (List<XmlRpcStruct>) client_.invoke("kickstart.listKickstarts", args);
            for (XmlRpcStruct profile : profiles) {
                this.ks_profiles_.add(profile.get("name").toString());
            }
        } catch (XmlRpcFault ex) {
            throw new RhnConnFault("Error connecting to spacewalk server. Problem found in connection: " + ex.getMessage());
        } catch (XmlRpcException ex) {
            throw new RhnConnFault("Error connecting to spacewalk server. Problem found in connection: " + ex.getMessage());
        } catch (MalformedURLException ex) {
            throw new RhnConnFault("Error connecting to spacewalk. Problem found in server URL: " + ex.getMessage());
        }
    }

    /**
     *  @return return the list of kickstart profiles in the server.
	 *
	 *  @throws RhnConnFault
     *
     */
    public List<String> getKsProfiles() throws RhnConnFault {
        return this.ks_profiles_;
    }

    /**
     *  @return return the list of names of the system groups in the server.
     *
	 *  @throws RhnSystemsGroupNotFoundException
	 *
     */
    public List<String> getGroupNames() throws RhnSystemsGroupNotFoundException {
        return this.system_groups_.getNames();
    }

    /**
     *  @return return the list of system groups in the server.
     *
	 *  @throws RhnSystemsGroupNotFoundException
	 *
     */
    public List<RhnSystemsGroup> getGroups() throws RhnSystemsGroupNotFoundException {
        return this.system_groups_.getGroups();
    }

    /**
     *  @return a RhnCobblerServer object to access cobbler information in spacewalk server.
     *
	 *  @throws RhnConnFault
	 *
     */
    public RhnCobblerServer getCobblerServer() throws RhnConnFault {
        RhnConnCobbler connection = this.connection_.getRhnConnCobbler();
        RhnCobblerServer cobbler_server = new RhnCobblerServer(connection);
        return cobbler_server;
    }
}
