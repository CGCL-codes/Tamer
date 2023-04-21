package commands.controller;

/**
 * Message containing info about number of free connections in MC.
 * @author KUBA
 *
 */
public class CC_NC_FreeConnections extends ControllerCommand {

    private Integer numConn;

    /**
	 * @param numConn Number of free connections
	 */
    public CC_NC_FreeConnections(Integer numConn) {
        super(ControllerCommandType.NC_FREE_CONNECTIONS);
        this.numConn = numConn;
    }

    /**
	 * @return Number of free connections
	 */
    public Integer getNumConn() {
        return numConn;
    }
}
