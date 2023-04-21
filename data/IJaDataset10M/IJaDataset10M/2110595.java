package titancommon.messages;

/**
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class ConfigurationSuccessMsg {

    public short configID;

    public short nodeID;

    public ConfigurationSuccessMsg(short configID, short nodeID) {
        this.configID = configID;
        this.nodeID = nodeID;
    }
}
