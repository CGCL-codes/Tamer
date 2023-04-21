package cz.sevcik.jn2n;

/**
 * Tunnel connector
 *
 * @author Jaroslav Sevcik
 *
 * @version $Rev: 5 $
 */
public class Connector {

    /** Tunel data class */
    private N2N tunnel;

    /** Thread external command */
    private CommandThread cmdThread;

    public Connector(N2N tunnel) {
        this.tunnel = tunnel;
        cmdThread = new CommandThread(createCommand());
    }

    /**
     * Start command thread
     */
    public void connect() {
        cmdThread.start();
    }

    /**
     * Exit command thread
     */
    public void disconnect() {
        cmdThread.exit();
        cmdThread.interrupt();
    }

    /**
     * Get the value of tunnel
     *
     * @return the value of tunnel
     */
    public N2N getTunnel() {
        return tunnel;
    }

    /**
     * Set the value of tunnel
     *
     * @param tunnel new value of tunnel
     */
    public void setTunnel(N2N tunnel) {
        this.tunnel = tunnel;
    }

    /**
     * Create connection command
     */
    private String createCommand() {
        return "edge -a " + tunnel.getEdgeNode() + " -c " + tunnel.getCommunityName() + " -k " + tunnel.getEncryptionKey() + " -l " + tunnel.getSuperNode();
    }
}
