package com.rbnb.api;

abstract class ClientIO extends com.rbnb.api.Rmap implements com.rbnb.api.Client {

    /**
     * the client connection type.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 06/01/2001
     */
    private byte type = CLIENT;

    /**
     * the remote identification.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 06/01/2001
     */
    private String remoteID = null;

    /**
     * the <code>Username</code> associated with this object.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 01/14/2003
     */
    private Username username = null;

    private static final byte PAR_RID = 0;

    private static final byte PAR_TYP = 1;

    private static final String[] PARAMETERS = { "RID", "TYP" };

    private static final String[] TYPES = { "Client", "Mirror", "PlugIn" };

    private static String[] ALL_PARAMETERS = null;

    private static int parametersStart = 0;

    ClientIO() {
        super();
    }

    ClientIO(String nameI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        super(nameI);
    }

    String additionalToString() {
        return (additionalToString(this));
    }

    static String additionalToString(ClientInterface clientI) {
        String stringR = "";
        if (clientI.getType() != CLIENT) {
            stringR += " " + TYPES[clientI.getType()];
        }
        if (clientI.getRemoteID() != null) {
            stringR += " (Remote: " + clientI.getRemoteID() + ")";
        }
        if (clientI.getUsername() != null) {
            stringR += " " + clientI.getUsername();
        }
        return (stringR);
    }

    static String[] addToParameters(String[] parametersI) {
        String[] parametersR = Rmap.addToParameters(null);
        if (parametersR != null) {
            parametersStart = parametersR.length;
        }
        return (addToParameters(parametersR, PARAMETERS));
    }

    void copyFields(Rmap clientI) {
        ClientInterface client = (ClientInterface) clientI;
        setType(client.getType());
        setRemoteID(client.getRemoteID());
    }

    void defaultParameters(Rmap otherI, boolean[] seenI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.IOException, java.lang.InterruptedException {
        defaultClientParameters(this, (ClientInterface) otherI, seenI);
    }

    static final void defaultClientParameters(ClientInterface ciIO, ClientInterface otherI, boolean[] seenI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.IOException, java.lang.InterruptedException {
        if (otherI != null) {
            if ((seenI == null) || !seenI[parametersStart + PAR_RID]) {
                ciIO.setRemoteID(otherI.getRemoteID());
            }
            if ((seenI == null) || !seenI[parametersStart + PAR_TYP]) {
                ciIO.setType(otherI.getType());
            }
            Rmap ci = (Rmap) ciIO, other = (Rmap) otherI;
            ci.defaultStandardParameters(other, seenI);
        }
    }

    public Rmap getRegistered(com.rbnb.api.Rmap requestI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot get registered information for " + requestI);
    }

    public final String getRemoteID() {
        return (remoteID);
    }

    public final byte getType() {
        return (type);
    }

    public final Username getUsername() {
        return (username);
    }

    private static final synchronized void initializeParameters() {
        if (ALL_PARAMETERS == null) {
            ALL_PARAMETERS = addToParameters(null);
        }
    }

    public boolean isSynchronized() {
        return (true);
    }

    public boolean isRunning() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException("Cannot determine if " + this + " is running.");
    }

    Rmap newInstance() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        return (newInstance(this));
    }

    static Rmap newInstance(Rmap rmapI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmapR = null;
        if (rmapI instanceof PlugInInterface) {
            rmapR = new PlugInIO();
        } else if (rmapI instanceof RouterInterface) {
            rmapR = new RouterIO();
        } else if (rmapI instanceof ControllerInterface) {
            rmapR = new ControllerIO();
        } else if (rmapI instanceof SinkInterface) {
            rmapR = new SinkIO();
        } else if (rmapI instanceof SourceInterface) {
            rmapR = new SourceIO();
        }
        ((ClientIO) rmapR).copyFields(rmapI);
        rmapR.setName(rmapI.getName());
        if (rmapI instanceof ClientInterface) {
            ClientInterface ci = (ClientInterface) rmapI, ciR = (ClientInterface) rmapR;
            ciR.setType(ci.getType());
            ciR.setRemoteID(ci.getRemoteID());
        }
        return (rmapR);
    }

    void read(Rmap otherI, InputStream isI, DataInputStream disI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        read(isI, disI, this, otherI);
    }

    static void read(InputStream isI, DataInputStream disI, ClientInterface ciIO, Rmap otherI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        Serialize.readOpenBracket(isI);
        initializeParameters();
        boolean[] seen = new boolean[ALL_PARAMETERS.length];
        int parameter;
        while ((parameter = Serialize.readParameter(ALL_PARAMETERS, isI)) != -1) {
            seen[parameter] = true;
            if (!readClientParameter(parameter, isI, disI, ciIO)) {
                ((Rmap) ciIO).readStandardParameter(otherI, parameter, isI, disI);
            }
        }
        defaultClientParameters(ciIO, (ClientInterface) otherI, seen);
    }

    static final boolean readClientParameter(int parameterI, InputStream isI, DataInputStream disI, ClientInterface ciIO) throws com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        boolean successR = false;
        if (parameterI >= parametersStart) {
            successR = true;
            switch(parameterI - parametersStart) {
                case PAR_RID:
                    ciIO.setRemoteID(isI.readUTF());
                    break;
                case PAR_TYP:
                    ciIO.setType(isI.readByte());
                    break;
                default:
                    successR = false;
                    break;
            }
        }
        return (successR);
    }

    public final void setRemoteID(String remoteIDI) {
        remoteID = remoteIDI;
    }

    public final void setType(byte typeI) {
        type = typeI;
    }

    public final void setUsername(Username usernameI) {
        username = usernameI;
    }

    public void start() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be started.");
    }

    public void start(Client clientI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be used to start " + clientI);
    }

    public void start(Shortcut shortcutI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be used to start " + shortcutI);
    }

    public void stop() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be stopped.");
    }

    public void stop(Client clientI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be used to stop " + clientI);
    }

    public void stop(Server serverI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be used to stop " + serverI);
    }

    public void stop(Shortcut shortcutI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be used to stop " + shortcutI);
    }

    public void synchronizeWserver() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        throw new java.lang.IllegalStateException(this + " cannot be synchronized.");
    }

    void write(Rmap otherI, String[] parametersI, int parameterI, OutputStream osI, DataOutputStream dosI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        write(parametersI, parameterI, osI, dosI, this, (ClientInterface) otherI);
    }

    static void write(String[] parametersI, int parameterI, OutputStream osI, DataOutputStream dosI, ClientInterface ciI, ClientInterface otherI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        long before = osI.getWritten();
        int valid = osI.setStage(true, false);
        osI.addStaged((Serializable) ciI, parametersI, parameterI);
        ((Rmap) ciI).writeStandardParameters((Rmap) otherI, osI, dosI);
        writeClientParameters(osI, dosI, ciI, otherI);
        if ((otherI == null) || (osI.getWritten() > before)) {
            Serialize.writeCloseBracket(osI);
        } else if (valid >= 0) {
            osI.removeStaged(valid);
        }
    }

    static final void writeClientParameters(OutputStream osI, DataOutputStream dosI, ClientInterface ciI, ClientInterface otherI) throws com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        osI.setStage(false, false);
        initializeParameters();
        if ((ciI.getRemoteID() != null) && ((otherI == null) || (otherI.getRemoteID() == null) || !otherI.getRemoteID().equals(ciI.getRemoteID()))) {
            osI.writeParameter(ALL_PARAMETERS, parametersStart + PAR_RID);
            osI.writeUTF(ciI.getRemoteID());
        }
        if ((otherI == null) || ((otherI != null) && (ciI.getType() != otherI.getType()))) {
            osI.writeParameter(ALL_PARAMETERS, parametersStart + PAR_TYP);
            osI.writeByte(ciI.getType());
        }
    }

    public boolean tryReconnect() {
        return (false);
    }

    /** Copies all the fields of the object to the given object
	 */
    protected void cloned(Object o) {
        super.cloned(o);
        ClientIO clonedR = (ClientIO) o;
        clonedR.type = type;
        clonedR.remoteID = remoteID;
        clonedR.username = username;
    }
}
