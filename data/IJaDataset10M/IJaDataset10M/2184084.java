package de.fzi.herakles.util.configuration;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * The Inteface of the reasoner configuration
 * @author Xu
 *
 */
public interface ReasonerConfiguration {

    /**
	 * load the configuration file of remote reasoner
	 * @param filename xml configuration file name
	 */
    public void loadRemoteReasonerConfiguration(String filename);

    /**
	 * load the configuration file of remote reasoner
	 * @param file file object of the configuration
	 */
    public void loadRemoteReasonerConfiguration(File file);

    /**
	 * load the configuration file of distribute reasoner factory
	 */
    public void loadRemoteReasonerConfiguration();

    /**
	 * add a new remote reasoner
	 * @param type reasoner type
	 * @param host host 
	 * @param name name
	 * @return index of the configuartion
	 */
    public int addRemoteReasoner(String type, String host, String name);

    /**
	 * update the existing reasoner configuration
	 * @param type the type of the remote reasoner
	 * @param index the index of the remote reasoner 
	 * @param host new host name
	 * @param name new name
	 */
    public void updateRemoteReasoner(String type, int index, String host, String name);

    /**
	 * remove the remote reasoner configuration of the input reasoner type and index
	 * @param type reasoner type
	 * @param index index of configuration
	 */
    public void removeRemoteReasoner(String type, int index);

    /**
	 * remove all remote reasoner configuration
	 */
    public void remoteAllRemoteReasoner();

    /**
	 * get the host of a reasoner according to the input reasoner type and index
	 * @param type reasoner type
	 * @param index reasoner index
	 * @return host
	 */
    public String getRemoteReasonerHost(String type, int index);

    /**
	 * get the name of  a remote reasoner according to the input reasoner type and index
	 * @param type reasoner type
	 * @param index reasoner index
	 * @return name
	 */
    public String getRemoteReasonerName(String type, int index);

    /**
	 * get the remote reasoner hosts of the input reasoner type
	 * @param type reasoner type
	 * @return map of the hosts
	 */
    public Map<Integer, String> getRemoteReasonerHosts(String type);

    /**
	 * get the remote reasoner names of the input reasoner type
	 * @param type reasoner type
	 * @return map of the names
	 */
    public Map<Integer, String> getRemoteReasonerNames(String type);

    /**
	 * get the types of the remote reasoners
	 * @return reasoner type set
	 */
    public Set<String> getRemoteReasonerTypes();

    /**
	 * get the index list of the remote reasoners according to the input reasoner type
	 * @param type reasoner type
	 * @return reasoner index set
	 */
    public Set<Integer> getRemoteReasonerIndex(String type);

    /**
	 * save the remote reasoner configuration in the input file
	 * @param file the configuration file that will be saved
	 */
    public void saveRemoteReasonerConfiguration(File file);

    /**
	 * reload the remote reasoner configuration
	 * @param file configuration file
	 */
    public void reloadRemoteReasonerConfiguration(File file);

    /**
	 * reload the remote reasoner configuration from default configuration file
	 */
    public void reloadRemoteReasonerConfiguration();

    /**
	 * set the positive sound reasoner
	 * @param reasonerID the reasoner ID of the positive sound reasoner
	 */
    public void setPositiveSoundReasoner(String reasonerID);

    /**
	 * get the reasoner ID of the positive sound reasoner
	 * @return the reasoner Id of the positive sound reasoner
	 */
    public String getPositiveSoundReasoner();

    /**
	 * set the negative sound reasoner
	 * @param reasonerID thh reasoner ID of the negative sound reasoner
	 */
    public void setNegativeSoundReasoner(String reasonerID);

    /**
	 * get the reasoner ID of the negative sound reasoner
	 * @return the reasoner ID of the negative sound reasoner
	 */
    public String getNegativeSoundReasoner();

    /**
	 * set the complete reasoner
	 * @param reasonerID the reasoner ID of the complete reasoner
	 */
    public void setCompleteReasoner(String reasonerID);

    /**
	 * get the reasoner ID of the complete reasoner
	 * @return the reasoner ID of the complete
	 */
    public String getCompleteReasoner();

    /**
	 * set the sound and complete reasoner
	 * @param reasonerID reasoner ID of the sound and complete reasoner
	 */
    public void setSoundCompleteReasoner(String reasonerID);

    /**
	 * get the reasoner Id of sound and complete reasoner 
	 * @return the reasoner ID of the sound and complete reasoner
	 */
    public String getSoundCompleteReasoner();

    /**
	 * set the unsound and incomplete reasoner
	 * @param reasonerID reasoner ID of the unsound and incomplete reasoner
	 */
    public void setUnsoundIncompleteReasoner(String reasonerID);

    /**
	 * get the reasoner Id of unsound and incomplete reasoner
	 * @return the reasoner ID of the unsound and incomplete reasoner
	 */
    public String getUnsoundIncompleteReasoner();
}
