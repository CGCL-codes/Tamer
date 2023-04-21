package auditorium;

import sexpression.*;

/**
 * An instance of this class simply wraps an ID, IP and port together. This
 * class represents a data structure in the auditorium format.
 * 
 * @author kyle
 * 
 */
public class HostPointer {

    public static final ASExpression PATTERN = new ListExpression(StringExpression.makeString("host"), StringWildcard.SINGLETON, StringWildcard.SINGLETON, StringWildcard.SINGLETON);

    private final String _nodeid;

    private final String _ip;

    private final int _port;

    private ListExpression _aseform;

    private String _string;

    /**
     * @param nodeid
     *            This is the id of the host.
     * @param ip
     *            This is the IP of the host in dotted decimal format.
     * @param port
     *            This is the port that the host is listening on.
     */
    public HostPointer(String nodeid, String ip, int port) {
        _nodeid = nodeid;
        _port = port;
        _ip = ip;
    }

    /**
     * Construct a new host address from its s-expression representation.
     * 
     * @param hostexp
     *            This should be of the format (host [id] [ip] [port])
     * @throws IncorrectFormatException
     *             This method throws if the given exp is not correctly
     *             formatted.
     */
    public HostPointer(ASExpression hostexp) throws IncorrectFormatException {
        try {
            ASExpression result = PATTERN.match(hostexp);
            if (result == NoMatch.SINGLETON) throw new IncorrectFormatException(hostexp, new Exception(hostexp + " didn't match the pattern " + PATTERN));
            _nodeid = ((ListExpression) result).get(0).toString();
            _ip = ((ListExpression) result).get(1).toString();
            _port = Integer.parseInt(((ListExpression) result).get(2).toString());
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException(hostexp, e);
        }
    }

    /**
     * Get the id of the node this references.
     * 
     * @return This method returns the node id.
     */
    public String getNodeId() {
        return _nodeid;
    }

    /**
     * Get the IP of this host.
     * 
     * @return This method returns the IP of this host.
     */
    public String getIP() {
        return _ip;
    }

    /**
     * Get the port of this host.
     * 
     * @return This method returns the port of this host.
     */
    public int getPort() {
        return _port;
    }

    /**
     * Return the sexpression representation of this host pointer.
     * 
     * @return This method returns (host _nodeid _ip _port)
     */
    public ListExpression toASE() {
        if (_aseform == null) _aseform = new ListExpression(StringExpression.makeString("host"), StringExpression.makeString(_nodeid), StringExpression.makeString(_ip), StringExpression.makeString(Integer.toString(_port)));
        return _aseform;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HostPointer)) return false;
        HostPointer hpo = (HostPointer) o;
        return _nodeid.equals(hpo._nodeid) && _ip.equals(hpo._ip) && _port == hpo._port;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (_string == null) _string = _nodeid + "@" + _ip + ":" + Integer.toString(_port);
        return _string;
    }
}
