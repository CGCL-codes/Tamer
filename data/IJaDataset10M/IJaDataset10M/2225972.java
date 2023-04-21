package com.softwoehr.pigiron.functions;

import com.softwoehr.pigiron.access.Connection;
import com.softwoehr.pigiron.access.ParameterArray;
import com.softwoehr.pigiron.access.SocketConnection;
import com.softwoehr.pigiron.access.SSLSocketConnection;
import com.softwoehr.pigiron.access.VSMException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Abstract base class of all the class embodiments of VSMAPI fuction calls.
 *
 * Private data stored in the base and accessed via accessors in this and
 * the subclasses (the actual VSMAPI function objects) include:
 * <ul>
 * <li>hostname</li>
 * <li>port</li>
 * <li>userid</li>
 * <li>password</li>
 * <li>target identifier</li>
 * </ul>
 * @author jax
 */
public abstract class VSMCall {

    /**
     *
     */
    public VSMCall() {
    }

    private String hostname;

    private int port;

    private String userid;

    private String password;

    private ParameterArray inParams;

    private ParameterArray outParams;

    private Connection connection;

    private String target_identifier;

    /**
     * Compose the array of input params to be transmitted in order to the VSMAPI Host.
     * Subclass are the actual function calls which implement composeInputArray()
     * to their needs.
     * "Input" as in "input to VSMAPI".
     * @return ParameterArray of input params to be transmitted in order to the VSMAPI Host
     * @see #composeOutputArray()
     */
    protected abstract ParameterArray composeInputArray();

    /**
     * Compose the array of output params to be read in order from the VSMAPI Host.
     * Subclass are the actual function calls which implement composeInputArray()
     * to their needs.
     * "output" as in "output from VSMAPI"
     * @return ParameterArray of input params to be read in order from the VSMAPI Host
     * @see #composeInputArray()
     */
    protected abstract ParameterArray composeOutputArray();

    /**
     * Create a new connection to the VSMAPI Host and open it.
     * @throws java.net.UnknownHostException if hostname can't be found
     * @throws java.io.IOException if error in connect.
     * @see com.softwoehr.pigiron.access.Connection
     */
    protected void connect() throws UnknownHostException, IOException {
        connection = new SocketConnection(hostname, port);
        connection.connect();
    }

    /**
     * Create a new connection to the VSMAPI Host and open it in 
     * either SSL mode or plain socket depending on boolean.
     * @param ssl if true, connect in SSL mode, plain socket otherwise
     * @throws java.net.UnknownHostException if hostname can't be found
     * @throws java.io.IOException if error in connect.
     * @see com.softwoehr.pigiron.access.Connection
     */
    protected void connect(boolean ssl) throws UnknownHostException, IOException {
        if (ssl) {
            connection = new SSLSocketConnection(hostname, port);
        } else {
            connection = new SocketConnection(hostname, port);
        }
        connection.connect();
    }

    /**
     * Create a new connection to the VSMAPI Host and open it.
     * @param hostname the name of the Host to which to connect
     * @param port the number of the Host port to which to connect
     * @throws java.net.UnknownHostException if hostname can't be found
     * @throws java.io.IOException if error in connect.
     * @see com.softwoehr.pigiron.access.Connection
     */
    protected void connect(String hostname, int port) throws UnknownHostException, IOException {
        this.hostname = hostname;
        this.port = port;
        connect();
    }

    /**
     * Create a new connection to the VSMAPI Host and open it in either
     * plain socket or SSL socket mode dependingon boolean.
     * @param hostname the name of the Host to which to connect
     * @param port the number of the Host port to which to connect
     * @param ssl if true, connect in SSL mode, plain socket otherwise
     * @throws java.net.UnknownHostException if hostname can't be found
     * @throws java.io.IOException if error in connect.
     * @see com.softwoehr.pigiron.access.Connection
     */
    protected void connect(String hostname, int port, boolean ssl) throws UnknownHostException, IOException {
        this.hostname = hostname;
        this.port = port;
        connect(ssl);
    }

    /**
     * Disconnect from the Host.
     * @see com.softwoehr.pigiron.access.Connection
     */
    protected void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    /**
     * Run the VSMAPI call and return its output parameters. This is the big one
     * after all the instance's private data has been set and the input and output
     * parameters have been composed.
     * @return ParameterArray of output paramters resulting from the VSMAPI call
     * @throws java.io.IOException if there is a communication error
     * @throws com.softwoehr.pigiron.access.VSMException if there is an error in parameter reading/writing
     */
    public ParameterArray doIt() throws IOException, VSMException {
        composeInputArray();
        composeOutputArray();
        connect();
        writeInput(connection.getOutputStream());
        readOutput(connection.getInputStream());
        disconnect();
        return outParams;
    }

    /**
     * Run the VSMAPI call and return its output parameters. This is the big one
     * after all the instance's private data has been set and the input and output
     * parameters have been composed.     
     * @param ssl if true, connect in SSL mode, plain socket otherwise
     * @return ParameterArray of output paramters resulting from the VSMAPI call
     * @throws java.io.IOException if there is a communication error
     * @throws com.softwoehr.pigiron.access.VSMException if there is an error in parameter reading/writing
     */
    public ParameterArray doIt(boolean ssl) throws IOException, VSMException {
        composeInputArray();
        composeOutputArray();
        connect(ssl);
        writeInput(connection.getOutputStream());
        readOutput(connection.getInputStream());
        disconnect();
        return outParams;
    }

    /**
     * Get the Connection object currently assigned to the VSMCall
     * @return the Connection object currently assigned to the VSMCall or <tt>null</tt>
     * @see com.softwoehr.pigiron.access.Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get the string name of the VSMAPI Function exactly
     * as it will be sent to call that function. This name
     * is implemented static public in each class extending
     * VSMCall.
     * @return the string name of the VSMAPI Function
     */
    public abstract String getFunctionName();

    /**
     * Return the name of the Host which will be
     * connected to by the instance of the function call.
     * @return the name of the Host which will be connected to by the instance
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the array of input parameters to the VSMAPI function. These
     * are instanced in <tt>composeInputArray</tt>.
     * @return the array of input parameters to the VSMAPI function
     * @see #composeInputArray()
     */
    public ParameterArray getInParams() {
        return inParams;
    }

    /**
     * Get the array of input parameters to the VSMAPI function.
     * @return the array of output parameters from the VSMAPI function
     * @see #composeOutputArray()
     */
    public ParameterArray getOutParams() {
        return outParams;
    }

    /**
     * Get password to be used to connect to the VSMAPI Host.
     * @return password to be used to connect to the VSMAPI Host
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the number of the Host port to which the call instance will connect on.
     * @return the number of the Host port to which the call instance will connect on
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the target identifier, an input param representing the object of
     * the function for most VSMAPI fuction calls.
     * @return the target identifier representing the object of  the function call
     */
    public String getTarget_identifier() {
        return target_identifier;
    }

    /**
     * Get the userid making the function call.
     * @return userid making the function call
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Read in all the output of the VSMAPI function call into the individual
     * params as ordered in the output params array composed by the function instance.
     * @param in the DataInputStream to read
     * @throws java.io.IOException on communication error
     * @throws com.softwoehr.pigiron.access.VSMException on parameter composition error
     * @see #composeOutputArray()
     * @see #writeInput(java.io.DataOutputStream)
     */
    protected void readOutput(DataInputStream in) throws IOException, VSMException {
        outParams.readAll(in);
    }

    /**
     * Instance the Connection object used to communicate to the VSMAPI Host.
     * @param connection the Connection object used to communicate to the VSMAPI Host
     * @see com.softwoehr.pigiron.access.Connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Set the name of the Host which will be
     * connected to by the instance of the function call.
     * @param hostname the name of the Host which will be connected to by the instance
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Instance the input parameter array which holds the
     * params to be sent to VSMAPI Host.
     * @param inParams the input parameter array which holds the
     * params to be sent to VSMAPI Host
     * @see #composeInputArray()
     */
    public void setInParams(ParameterArray inParams) {
        this.inParams = inParams;
    }

    /**
     * Instance the output parameter array which holds the
     * params to be read, or which have been read, from the VSMAPI Host.
     * @param outParams output parameter array which holds the
     * params to be read, or which have been read, from the VSMAPI Host
     */
    public void setOutParams(ParameterArray outParams) {
        this.outParams = outParams;
    }

    /**
     * Set password to be used to connect to the VSMAPI Host.
     * @param password password to be used to connect to the VSMAPI Host
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the number of the Host port to which the call instance will connect on.
     * @param port the number of the Host port to which the call instance will connect on
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get the target identifier, an input param representing the object of
     * the function for most VSMAPI fuction calls.
     * @param target_identifier an input param representing the object of
     * the function for most VSMAPI fuction calls
     */
    public void setTarget_identifier(String target_identifier) {
        this.target_identifier = target_identifier;
    }

    /**
     * Set the userid making the function call.
     * @param userid the userid making the function call.
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * Write out all the input params to the VSMAPI function call
     * as ordered in the inpput params array composed by the function instance.
     * @param out the DataOutputStream to write
     * @throws java.io.IOException on communication error
     * @see #composeInputArray()
     * @see #readOutput(java.io.DataInputStream)
     */
    protected void writeInput(DataOutputStream out) throws IOException {
        inParams.writeAll(out);
    }
}
