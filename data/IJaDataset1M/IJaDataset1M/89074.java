package com.rbnb.api;

abstract class RCO implements com.rbnb.api.BuildInterface, com.rbnb.api.GetLogInterface, com.rbnb.api.Interruptable, com.rbnb.api.IOMetricsInterface {

    /**
     * the build date.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 12/20/2001
     */
    private java.util.Date buildDate = null;

    /**
     * the build version.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 12/20/2001
     */
    private String buildVersion = null;

    /**
     * the <code>ClientHandler</code>.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/08/2001
     */
    private ClientHandler clientHandler = null;

    /**
     * metrics: final bytes transferred.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 11/19/2002
     */
    long metricsBytes = 0;

    /**
     * the next expected <code>Ping</code> data value.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.1
     * @version 02/26/2003
     */
    private short pingValue = -1;

    /**
     * the <code>ServerHandler</code>.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/08/2001
     */
    private ServerHandler rbnb = null;

    /**
     * the read door.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 12/13/2001
     */
    private Door readDoor = null;

    /**
     * has this connection been reversed?
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 03/11/2002
     */
    private boolean reversed = false;

    /**
     * the server-side communications object.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/14/2001
     */
    private Object serverSide = null;

    /**
     * <code>RCO</code> is stopping itself?
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/09/2001
     */
    private boolean stopMyself = false;

    /**
     * stop this <code>RCO</code>?
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/10/2001
     */
    private boolean terminateRequested = false;

    /**
     * the thread running this <code>RCO</code>.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 05/08/2001
     */
    private Thread thread = null;

    /**
     * the former <code>ClientHandler</code>.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.2
     * @version 01/26/2004
     */
    private ClientHandler wasClientHandler = null;

    /**
     * the write door.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 12/13/2001
     */
    private Door writeDoor = null;

    RCO() {
        super();
        try {
            setReadDoor(new Door(Door.STANDARD));
            setWriteDoor(new Door(Door.STANDARD));
        } catch (java.lang.InterruptedException e) {
        }
    }

    RCO(Object serverSideI, ServerHandler rbnbI) {
        this();
        try {
            setReadDoor(new Door(Door.STANDARD));
            setWriteDoor(new Door(Door.STANDARD));
        } catch (java.lang.InterruptedException e) {
        }
        setServerSide(serverSideI);
        setServerHandler(rbnbI);
    }

    private final void ask(Ask askI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Serializable answer = null;
        try {
            if (askI.getInformationType().equalsIgnoreCase(Ask.REGISTERED)) {
                Rmap rmap = (Rmap) askI.getAdditional().firstElement(), pmap;
                if (!isSupported(IsSupported.FEATURE_REQUEST_LEAF_NODES)) {
                    rmap.markLeaf();
                }
                answer = getServerHandler().getRoutingMapHandler().getRegistered(rmap);
                if (answer == null) {
                    answer = new Rmap();
                }
            } else if (askI.getInformationType().equalsIgnoreCase(Ask.CHILDAT)) {
                int index = ((Integer) askI.getAdditional().firstElement()).intValue();
                answer = getClientHandler().getChildAt(index);
            } else if (askI.getInformationType().equalsIgnoreCase(Ask.ISRUNNING)) {
                Client client = (Client) askI.getAdditional().firstElement();
                ClientHandler handler = (ClientHandler) getServerHandler().findChild((Rmap) client);
                if ((handler == null) || !handler.isRunning() || (handler.getThread() == null) || !handler.getThread().isAlive()) {
                    answer = new Stop(client);
                } else {
                    answer = new Start(client);
                }
            } else if (askI.getInformationType().equalsIgnoreCase(Ask.REQUESTAT)) {
                if (getClientHandler() instanceof SinkHandler) {
                    int index = ((Integer) askI.getAdditional().firstElement()).intValue();
                    ((SinkHandler) getClientHandler()).initiateRequestAt(index);
                } else {
                    answer = Language.exception(new java.lang.IllegalArgumentException("Cannot match request " + askI.getInformationType() + " from " + this + "."));
                }
            } else if (askI.getInformationType().equalsIgnoreCase(Ask.ROUTEFROM)) {
                if (!isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to start " + "routing connections.");
                }
                String type = (String) askI.getAdditional().firstElement();
                Rmap hierarchy = (Rmap) askI.getAdditional().elementAt(1), hierarchy2 = (askI.getAdditional().size() <= 2) ? null : (Rmap) askI.getAdditional().lastElement();
                boolean goodName = true;
                if (hierarchy2 != null) {
                    Rmap h2Bottom = hierarchy2.moveToBottom();
                    if (h2Bottom.getFullName().compareTo(getServerHandler().getFullName()) != 0) {
                        answer = Language.exception(new java.lang.IllegalArgumentException("Cannot accept route from " + type + " " + hierarchy + ".\nMy name (" + getServerHandler().getFullName() + ") does not match the name (" + h2Bottom.getFullName() + ") expected by the remote server."));
                        goodName = false;
                    }
                }
                if (goodName) {
                    if (getClientHandler() instanceof RouterHandler) {
                        answer = ((RouterHandler) getClientHandler()).acceptRouteFrom(type, hierarchy);
                    } else {
                        answer = Language.exception(new java.lang.IllegalArgumentException("Cannot accept route from " + type + " " + hierarchy + "."));
                    }
                }
            } else {
                answer = Language.exception(new java.lang.IllegalArgumentException("Unknown request " + askI.getInformationType() + " sent to " + this + "."));
            }
        } catch (java.lang.RuntimeException e) {
            getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
            answer = Language.exception(e);
        }
        if (answer != null) {
            send(answer);
        }
    }

    abstract void assignConnection(RCO rcoI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    public long bytesTransferred() {
        return (metricsBytes);
    }

    private final void clearCache(ClearCache clearCacheI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (clearCacheI.getObject() instanceof SourceInterface) {
            clearCache((SourceInterface) clearCacheI.getObject());
        } else {
            throw new java.lang.IllegalArgumentException("Cannot clear the cache of " + clearCacheI.getObject() + ".");
        }
    }

    private final void clearCache(SourceInterface sourceInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = getServerHandler().findChild((Rmap) sourceInterfaceI);
        if (rmap == null) {
            throw new com.rbnb.api.AddressException(sourceInterfaceI + " not found.");
        } else if (!(rmap instanceof SourceHandler)) {
            throw new com.rbnb.api.AddressException(sourceInterfaceI + " does not represent a source of this " + "server.");
        }
        SourceHandler sHandler = (SourceHandler) rmap;
        sHandler.update(sourceInterfaceI);
        synchronized (sHandler) {
            sHandler.setPerformClearCache(true);
            while (sHandler.getPerformClearCache()) {
                sHandler.wait(TimerPeriod.NORMAL_WAIT);
            }
        }
        send(Language.ping());
    }

    abstract void close() throws java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    private final synchronized void connectToExistingClient(ClientInterface clientInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = getServerHandler().findChild((Rmap) clientInterfaceI);
        if (rmap == null) {
            throw new com.rbnb.api.AddressException(clientInterfaceI + " not found.");
        } else if (!(rmap instanceof ClientHandler)) {
            throw new com.rbnb.api.AddressException(clientInterfaceI + " does not represent a client of this " + "server.");
        }
        ClientHandler cHandler = (ClientHandler) rmap;
        cHandler.assignConnection(this, clientInterfaceI);
    }

    abstract ACO convertToACO(Client clientI) throws com.rbnb.api.AddressException, java.io.IOException;

    abstract void disconnect() throws java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    public final java.util.Date getBuildDate() {
        return (buildDate);
    }

    public final String getBuildVersion() {
        return (buildVersion);
    }

    final ClientHandler getClientHandler() {
        return (clientHandler);
    }

    public final Log getLog() {
        Log logR = null;
        if (getServerHandler() != null) {
            logR = getServerHandler().getLog();
        } else if (getClientHandler() != null) {
            logR = getClientHandler().getLog();
        }
        return (logR);
    }

    public final long getLogClass() {
        return (Log.CLASS_RCO);
    }

    public final byte getLogLevel() {
        return ((getClientHandler() == null) ? Log.STANDARD : getClientHandler().getLogLevel());
    }

    final Door getReadDoor() {
        return (readDoor);
    }

    final ServerHandler getServerHandler() {
        return (rbnb);
    }

    final Object getServerSide() {
        return (serverSide);
    }

    final boolean getTerminateRequested() {
        return (terminateRequested);
    }

    final Thread getThread() {
        return (thread);
    }

    final Door getWriteDoor() {
        return (writeDoor);
    }

    public final void interrupt() {
        if (getThread() != null) {
            getThread().interrupt();
        }
    }

    boolean isAllowedAccess(Address addressI, int accessI) throws com.rbnb.api.AddressException {
        return (true);
    }

    boolean isSupported(int featureI) {
        return (true);
    }

    abstract boolean isWaiting() throws com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    synchronized void login(Login loginI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        ClientInterface clientInterface = (ClientInterface) loginI.getChildAt(0);
        setBuildDate(loginI.getBuildDate());
        setBuildVersion(loginI.getBuildVersion());
        if (clientInterface.tryReconnect()) {
            Rmap rmap = getServerHandler().findChild((Rmap) clientInterface);
            if (rmap != null) {
                ClientHandler clientHandler = (ClientHandler) rmap;
                if (!clientHandler.allowReconnect(loginI.getUsername())) {
                    throw new java.lang.IllegalStateException("Cannot reconnect to existing client handler " + clientHandler.getFullName() + ".");
                } else if ((clientInterface instanceof RouterInterface) && !isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to reconnect " + "to routing connections.");
                } else if ((clientInterface instanceof ControllerInterface) && !isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.CONTROL)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to reconnect " + "to control connections.");
                } else if ((clientInterface instanceof PlugInInterface) && !isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.PLUGIN)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to reconnect " + "to plugin connections.");
                } else if ((clientInterface instanceof SinkInterface) && !isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.SINK)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to reconnect " + "to sink connections.");
                } else if ((clientInterface instanceof SourceInterface) && !isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.SOURCE)) {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to reconnect " + "to source connections.");
                }
                clientHandler.setUsername(loginI.getUsername());
                clientHandler.setRCO(this);
                setClientHandler(clientHandler);
                clientHandler.reconnect(clientInterface);
            }
        }
        if (getClientHandler() == null) {
            synchronized (getServerHandler()) {
                getServerHandler().uniqueName(clientInterface);
                setClientHandler(getServerHandler().createClientHandler(this, clientInterface));
            }
            getClientHandler().setUsername(loginI.getUsername());
        }
        Server myServer = (Server) ((Server) getServerHandler()).newInstance();
        try {
            Rmap myClient = (Rmap) clientInterface.getClass().newInstance();
            myClient.setName(getClientHandler().getName());
            myServer.addChild(myClient);
            send(myServer);
        } catch (java.lang.IllegalAccessException e) {
            throw new com.rbnb.api.AddressException("Failed to gain access.");
        } catch (java.lang.InstantiationException e) {
            throw new com.rbnb.api.AddressException("Failed to instantiate client handler.");
        }
    }

    private final Rmap makeRequest(Rmap requestI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        ServerHandler sHandler = (ServerHandler) getClientHandler().getParent();
        Server server = Server.newServerHandle(sHandler.getName(), sHandler.getAddress());
        server.setServerSide(sHandler);
        Sink sink = server.createRAMSink("_snk." + getClientHandler().getName());
        sink.setCframes(1);
        sink.start();
        sink.addChild(requestI);
        sink.initiateRequestAt(0);
        Rmap rmapR = sink.fetch(Sink.FOREVER);
        sink.stop();
        return (rmapR);
    }

    static final RCO newRCO(Object serverSideI, ServerHandler rbnbI) throws com.rbnb.api.AddressException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        RCO rcoR = null;
        if (serverSideI instanceof RAMCommunications) {
            rcoR = new RAMRCO(serverSideI, rbnbI);
        } else {
            String strAddr = (String) rbnbI.getAddress();
            int ss = strAddr.indexOf("://");
            if (ss == -1) {
                rcoR = new TCPRCO(serverSideI, rbnbI);
            } else if (strAddr.substring(0, ss).equalsIgnoreCase("INTERNAL")) {
                rcoR = new RAMRCO(serverSideI, rbnbI);
            } else if (strAddr.substring(0, ss).equalsIgnoreCase("TCP")) {
                rcoR = new TCPRCO(serverSideI, rbnbI);
            } else {
                throw new com.rbnb.api.AddressException(strAddr + " is not a valid address.");
            }
        }
        return (rcoR);
    }

    final boolean process(Serializable messageI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Object[] values;
        try {
            if (getTerminateRequested()) {
                return (false);
            } else if (com.rbnb.compat.Utilities.interrupted(getThread())) {
                throw new java.lang.InterruptedException();
            } else if (messageI instanceof Login) {
                login((Login) messageI);
            } else if (messageI instanceof Username) {
                getClientHandler().setUsername((Username) messageI);
                send(Language.ping());
            } else if (messageI instanceof ClientInterface) {
                connectToExistingClient((ClientInterface) messageI);
                return (false);
            } else if (getClientHandler() == null) {
                if (getTerminateRequested()) {
                    return (false);
                }
                throw new java.lang.IllegalStateException(messageI + " cannot be processed without a client " + "handler. Please login first.");
            } else if (messageI instanceof Acknowledge) {
                if (getClientHandler() instanceof SinkHandler) {
                    ((SinkHandler) getClientHandler()).acknowledge((Acknowledge) messageI);
                } else {
                    throw new java.lang.IllegalStateException(messageI + " cannot be added to a non-sink client " + getClientHandler().getFullName());
                }
            } else if (Language.isRegister(messageI)) {
                register((Register) messageI);
            } else if (messageI instanceof RequestOptions) {
                if (getClientHandler() instanceof SinkHandler) {
                    ((SinkHandler) getClientHandler()).setRequestOptions((RequestOptions) messageI);
                } else {
                    throw new com.rbnb.api.SerializeException(messageI + " is only supported by sinks.");
                }
            } else if (messageI instanceof DeleteChannels) {
                Rmap response;
                if (getClientHandler() instanceof SourceHandler) {
                    response = ((SourceHandler) getClientHandler()).deleteChannels((Rmap) ((DeleteChannels) messageI).getObject());
                    send(response);
                } else {
                    throw new com.rbnb.api.SerializeException(messageI + " is only supported by sources.");
                }
            } else if (Language.isRequest(messageI)) {
                ask((Ask) messageI);
            } else if (messageI instanceof ClearCache) {
                clearCache((ClearCache) messageI);
            } else if (messageI instanceof Reset) {
                reset((Reset) messageI);
            } else if (messageI instanceof ReverseRoute) {
                if (getClientHandler() instanceof ControllerHandler) {
                    if (!isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
                        throw new com.rbnb.api.AddressException("Client address is not authorized to " + "reverse routing.");
                    } else if (((ControllerHandler) getClientHandler()).reverseRoute((ReverseRoute) messageI)) {
                        return (false);
                    }
                } else {
                    throw new java.lang.IllegalStateException(messageI + " cannot be executed by non-controller client " + getClientHandler().getFullName());
                }
            } else if (Language.isStart(messageI)) {
                start((Start) messageI);
            } else if (Language.isStop(messageI)) {
                stop((Stop) messageI);
            } else if (!getClientHandler().isRunning()) {
                if (getTerminateRequested()) {
                    return (false);
                }
                throw new com.rbnb.api.SerializeException("Message is out of sequence: " + messageI + "\nThe client handler has not been started yet.");
            } else if (Language.isOK(messageI)) {
                Ping ping = (Ping) messageI;
                if (ping.getHasData()) {
                    if (ping.getData() != pingValue) {
                        throw new com.rbnb.api.SerializeException("Received out-of-order ping. Expected " + pingValue + ", got " + (ping.getHasData() ? (new Short(ping.getData()).toString()) : "no counter") + ".");
                    }
                    ++pingValue;
                }
                getClientHandler().synchronizeWclient();
                send((Ping) messageI);
            } else if (messageI instanceof PeerUpdate) {
                if (getClientHandler() instanceof RouterHandler) {
                    ((RouterHandler) getClientHandler()).updatePeer((PeerUpdate) messageI);
                    send(Language.ping());
                } else {
                    throw new java.lang.IllegalStateException(messageI + " cannot be processed by " + getClientHandler().getFullName());
                }
            } else if (messageI instanceof RoutedMessage) {
                if (getClientHandler() instanceof RouterHandler) {
                    Serializable serializable = ((RouterHandler) getClientHandler()).deliver((RoutedMessage) messageI);
                    if (serializable != null) {
                        send(serializable);
                    }
                } else {
                    throw new java.lang.IllegalStateException(messageI + " cannot be processed by " + getClientHandler().getFullName());
                }
            } else if (messageI instanceof Mirror) {
                if (isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.CONTROL)) {
                    new MirrorController((ServerHandler) getClientHandler().getParent(), (Mirror) messageI);
                    send(Language.ping());
                } else {
                    throw new com.rbnb.api.AddressException("Client address is not authorized to start mirrors.");
                }
            } else if (messageI instanceof Rmap) {
                if ((getClientHandler() instanceof SourceHandler) || (getClientHandler() instanceof PlugInHandler)) {
                    getClientHandler().addChild((Rmap) messageI);
                } else {
                    throw new java.lang.IllegalStateException(messageI + " cannot be added to a non-source/plugin client " + getClientHandler().getFullName());
                }
            } else if (messageI instanceof ExceptionMessage) {
                getClientHandler().exception((ExceptionMessage) messageI);
            } else {
                throw new java.lang.IllegalStateException(messageI + " is not a type that can be " + "processed.");
            }
        } catch (com.rbnb.api.RBNBException e) {
            if (getClientHandler() == null) {
                Language.throwException(e);
            }
            try {
                getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
            } catch (java.lang.Throwable e1) {
            }
            send(Language.exception(e));
        } catch (java.lang.RuntimeException e) {
            if (getClientHandler() == null) {
                Language.throwException(e);
            }
            try {
                getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
            } catch (java.lang.Throwable e1) {
            }
            send(Language.exception(e));
        }
        return (true);
    }

    abstract Serializable receive(long timeOutI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    private final void register(Register registerI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Serializable answer = null;
        try {
            if (getClientHandler() instanceof SourceHandler) {
                if (registerI.getReplace()) {
                    throw new java.lang.IllegalArgumentException("Cannot replace the entire registration for a " + "source connection.\n" + registerI);
                }
                ((SourceHandler) getClientHandler()).register((Rmap) registerI.getObject());
                answer = Language.ping();
            } else if (getClientHandler() instanceof PlugInHandler) {
                if (registerI.getReplace()) {
                    ((PlugInHandler) getClientHandler()).reRegister((Rmap) registerI.getObject());
                } else {
                    ((PlugInHandler) getClientHandler()).register((Rmap) registerI.getObject());
                    answer = Language.ping();
                }
            } else {
                throw new com.rbnb.api.SerializeException("Cannot register with " + this + ", it has no registration map.");
            }
        } catch (java.lang.RuntimeException e) {
            getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
            answer = Language.exception(e);
        }
        if (answer != null) {
            send(answer);
        }
    }

    private final void reset(Reset resetI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (resetI.getObject() instanceof SourceInterface) {
            reset((SourceInterface) resetI.getObject());
        } else {
            throw new java.lang.IllegalArgumentException("Cannot reset " + resetI.getObject() + ".");
        }
    }

    private final void reset(SourceInterface sourceInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = getServerHandler().findChild((Rmap) sourceInterfaceI);
        if (rmap == null) {
            throw new com.rbnb.api.AddressException(sourceInterfaceI + " not found.");
        } else if (!(rmap instanceof SourceHandler)) {
            throw new com.rbnb.api.AddressException(sourceInterfaceI + " does not represent a source of this " + "server.");
        }
        SourceHandler sHandler = (SourceHandler) rmap;
        if ((sHandler == getClientHandler()) || isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.CONTROL)) {
            sHandler.update(sourceInterfaceI);
            synchronized (sHandler) {
                sHandler.setPerformReset(true);
                while (sHandler.getPerformReset()) {
                    sHandler.wait(TimerPeriod.NORMAL_WAIT);
                }
            }
        } else {
            throw new com.rbnb.api.AddressException("Client address is not authorized to reset other clients.");
        }
        send(Language.ping());
    }

    public final void run() {
        Serializable message;
        try {
            while (!getTerminateRequested() && !com.rbnb.compat.Utilities.interrupted(getThread())) {
                getReadDoor().setIdentification(this + "_read");
                getWriteDoor().setIdentification(this + "_write");
                if ((message = receive(TimerPeriod.NORMAL_WAIT)) == null) {
                    continue;
                }
                if (com.rbnb.compat.Utilities.interrupted(getThread())) {
                    throw new java.lang.InterruptedException();
                }
                if (!process(message)) {
                    reversed = (message instanceof ReverseRoute);
                    break;
                }
                if (getThread() != null) {
                    ((ThreadWithLocks) getThread()).ensureLocksCleared(toString(), "RCO.run", getLog(), getLogLevel(), getLogClass());
                }
            }
            if (getTerminateRequested() || stopMyself || com.rbnb.compat.Utilities.interrupted(getThread())) {
                throw new java.lang.InterruptedException();
            }
        } catch (java.lang.InterruptedException e) {
        } catch (java.io.IOException e) {
            try {
                if (getClientHandler() == null) {
                    getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
                }
                if (getClientHandler() instanceof SourceHandler) {
                    if (((SourceHandler) getClientHandler()).stopOnIOException()) {
                        setClientHandler(null);
                    }
                } else if (!stopMyself) {
                    send(Language.exception(e));
                }
            } catch (java.lang.Throwable e1) {
            }
        } catch (java.lang.Exception e) {
            try {
                if (((getClientHandler() == null) || !((RBNBClient) getClientHandler()).getTerminateRequested()) && !(e instanceof java.lang.InterruptedException) && !(e instanceof java.io.InterruptedIOException)) {
                    getLog().addException(Log.STANDARD, getLogClass(), toString(), e);
                }
                if (!stopMyself) {
                    send(Language.exception(e));
                }
            } catch (java.lang.Throwable e1) {
            }
        } catch (java.lang.Error e) {
            try {
                getLog().addError(Log.STANDARD, getLogClass(), toString(), e);
                if (!stopMyself) {
                    send(Language.exception(new java.lang.Exception("A fatal error occured.\n" + e.getClass() + " " + e.getMessage())));
                }
            } catch (java.lang.Throwable e1) {
            }
        } finally {
            if (getThread() != null) {
                ((ThreadWithLocks) getThread()).clearLocks();
            }
        }
        try {
            getLog().addMessage(getLogLevel() + 10, getLogClass(), toString(), "Terminating RCO.");
        } catch (java.lang.Throwable e) {
        }
        if (getClientHandler() != null) {
            try {
                if (reversed) {
                    getClientHandler().setRCO(null);
                }
                getClientHandler().stop(getClientHandler());
            } catch (java.lang.Throwable e) {
            }
            setClientHandler(null);
        }
        if (!reversed) {
            try {
                send(Language.ping());
            } catch (java.lang.Throwable e) {
            }
            try {
                close();
            } catch (java.lang.Throwable e) {
            }
            try {
                disconnect();
            } catch (java.lang.Throwable e) {
            }
        }
        try {
            getLog().addMessage(getLogLevel() + 11, getLogClass(), toString(), "Terminated RCO.");
        } catch (java.lang.Throwable e) {
        }
        setThread(null);
    }

    abstract void send(Serializable serializableI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException;

    public final void setBuildDate(java.util.Date buildDateI) {
        buildDate = buildDateI;
    }

    public final void setBuildVersion(String buildVersionI) {
        buildVersion = buildVersionI;
    }

    final void setClientHandler(ClientHandler clientHandlerI) {
        clientHandler = clientHandlerI;
        if (clientHandlerI != null) {
            wasClientHandler = clientHandlerI;
        }
    }

    private final void setReadDoor(Door readDoorI) {
        readDoor = readDoorI;
    }

    final void setServerHandler(ServerHandler rbnbI) {
        rbnb = rbnbI;
    }

    final void setServerSide(Object serverSideI) {
        serverSide = serverSideI;
    }

    final synchronized void setTerminateRequested(boolean stopI) {
        terminateRequested = stopI;
        notifyAll();
    }

    private final void setThread(Thread threadI) {
        thread = threadI;
    }

    private final void setWriteDoor(Door writeDoorI) {
        writeDoor = writeDoorI;
    }

    final synchronized void start() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (getThread() == null) {
            setThread(new ThreadWithLocks(this));
            getThread().start();
        }
    }

    private final void start(Start startI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (startI.getObject() instanceof ClientInterface) {
            ClientInterface clientInterface = (ClientInterface) startI.getObject();
            start(clientInterface);
        } else if (startI.getObject() instanceof Shortcut) {
            Shortcut shortcut = (Shortcut) startI.getObject();
            start(shortcut);
        }
    }

    private final void start(ClientInterface clientInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = getServerHandler().findChild((Rmap) clientInterfaceI);
        if (rmap == null) {
            throw new com.rbnb.api.AddressException(clientInterfaceI + " not found.");
        } else if (!(rmap instanceof ClientHandler)) {
            throw new com.rbnb.api.AddressException(clientInterfaceI + " does not represent a client of this " + "server.");
        }
        ClientHandler cHandler = (ClientHandler) rmap;
        try {
            cHandler.start(getClientHandler());
        } catch (java.lang.Exception e) {
            if (cHandler == getClientHandler()) {
                stop();
            }
            Language.throwException(e);
        }
        send(Language.ping());
        if (cHandler instanceof PlugInInterface) {
            cHandler.setAmNew(false);
        }
    }

    private final void start(Shortcut shortcutInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (!isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
            throw new com.rbnb.api.AddressException("Client address is not authorized to start shortcuts.");
        }
        Rmap entry = getServerHandler().findDescendant(Rmap.PATHDELIMITER + getServerHandler().getName() + Rmap.PATHDELIMITER + shortcutInterfaceI.getName(), false);
        Address addressR = Address.newAddress(shortcutInterfaceI.getDestinationAddress());
        shortcutInterfaceI.setDestinationAddress(addressR.getAddress());
        if (entry != null) {
            Shortcut shortcut = (Shortcut) entry;
            if (shortcutInterfaceI.getDestinationAddress().compareTo(shortcut.getDestinationAddress()) != 0) {
                throw new com.rbnb.api.AddressException("Cannot start shortcut " + shortcutInterfaceI + "\nIt conflicts with the existing shortcut " + shortcut);
            }
            if (shortcut.getCost() != shortcutInterfaceI.getCost()) {
                shortcut.setCost(shortcutInterfaceI.getCost());
                getServerHandler().findPaths(null);
                ((RBNB) getServerHandler()).setUpdateCounter(((RBNB) getServerHandler()).getUpdateCounter() + 1);
                ((RBNB) getServerHandler()).broadcastUpdate(new PeerUpdate((PeerServer) getServerHandler()));
            }
        } else {
            ShortcutHandler scHandler = new RBNBShortcut();
            scHandler.setName(shortcutInterfaceI.getName());
            scHandler.setDestinationName(shortcutInterfaceI.getDestinationName());
            scHandler.setDestinationAddress(shortcutInterfaceI.getDestinationAddress());
            scHandler.setCost(shortcutInterfaceI.getCost());
            scHandler.setActive(shortcutInterfaceI.getActive());
            getServerHandler().addChild((Rmap) scHandler);
            scHandler.start();
        }
        send(Language.ping());
    }

    final void stop() throws java.lang.InterruptedException {
        boolean localStopMyself;
        setTerminateRequested(true);
        localStopMyself = (Thread.currentThread() == getThread()) || ((wasClientHandler != null) && (Thread.currentThread() == wasClientHandler.getThread()));
        stopMyself = stopMyself || localStopMyself;
        stopMe();
        if (!stopMyself && (getClientHandler() != null)) {
            if (!reversed) {
                try {
                    send(Language.ping());
                } catch (java.lang.Throwable e) {
                }
                try {
                    close();
                } catch (java.lang.Throwable e) {
                }
                try {
                    disconnect();
                } catch (java.lang.Throwable e) {
                }
            }
        }
        if (!localStopMyself) {
            synchronized (this) {
                for (long startAt = System.currentTimeMillis(), nowAt = System.currentTimeMillis(); ((nowAt - startAt < TimerPeriod.SHUTDOWN) && (getThread() != null) && getThread().isAlive()); nowAt = System.currentTimeMillis()) {
                    wait(TimerPeriod.NORMAL_WAIT);
                }
            }
            interrupt();
        }
    }

    void stopMe() {
    }

    private final void stop(Stop stopI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        if (stopI.getObject() instanceof ClientInterface) {
            ClientInterface clientInterface = (ClientInterface) stopI.getObject();
            stop(clientInterface);
        } else if (stopI.getObject() instanceof Server) {
            Server serverInterface = (Server) stopI.getObject();
            stop(serverInterface);
        } else if (stopI.getObject() instanceof Shortcut) {
            Shortcut shortcutInterface = (Shortcut) stopI.getObject();
            stop(shortcutInterface);
        }
    }

    private final void stop(ClientInterface clientInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = getServerHandler().findChild((Rmap) clientInterfaceI);
        if (rmap == null) {
            send(Language.ping());
            return;
        } else if (!(rmap instanceof ClientHandler)) {
            throw new com.rbnb.api.AddressException(clientInterfaceI + " does not represent a client of this " + "server.");
        }
        ClientHandler cHandler = (ClientHandler) rmap;
        if (clientInterfaceI instanceof SourceInterface) {
            SourceHandler sHandler = (SourceHandler) cHandler;
            SourceInterface sInterface = (SourceInterface) clientInterfaceI;
            sHandler.setAkeep(sInterface.getAkeep());
            sHandler.setCkeep(sInterface.getCkeep());
        }
        if (cHandler == getClientHandler()) {
            stop();
        } else {
            if (isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.CONTROL)) {
                cHandler.stop(getClientHandler());
                send(Language.ping());
            } else {
                throw new com.rbnb.api.AddressException("Clien address is not authorized to terminate " + "other clients.");
            }
        }
    }

    private final void stop(Server serverInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Rmap rmap = null, bottom = ((Rmap) serverInterfaceI).moveToBottom();
        if (serverInterfaceI.getName() != null) {
            rmap = getServerHandler().getRoutingMapHandler().findDescendant(bottom.getFullName(), false);
        } else {
            if ((bottom != serverInterfaceI) && (bottom.getName() != null) && bottom.getName().equals(getServerHandler().getName())) {
                rmap = (Rmap) getServerHandler();
            }
            if (rmap == null) {
                throw new java.lang.IllegalStateException("Stopping a peer connection by address " + "is not yet supported.");
            }
        }
        if (rmap == null) {
            throw new com.rbnb.api.AddressException(serverInterfaceI + " not found.");
        } else if (!(rmap instanceof ServerInterface)) {
            throw new com.rbnb.api.AddressException(serverInterfaceI + " does not represent a server known to " + "this server.");
        }
        if (rmap instanceof ServerHandler) {
            ServerHandler sHandler = (ServerHandler) rmap;
            if (isAllowedAccess(((RBNB) sHandler).getAddressHandler(), AddressPermissions.CONTROL)) {
                sHandler.stop(getClientHandler());
            } else {
                throw new com.rbnb.api.AddressException("Client address is not authorized to terminate " + "the server.");
            }
        } else if (rmap instanceof ConnectedServer) {
            if (isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
                ConnectedServer cServer = (ConnectedServer) rmap;
                cServer.lostRouting();
            } else {
                throw new com.rbnb.api.AddressException("Client address is not authorized to terminate routing.");
            }
        } else {
            throw new java.lang.IllegalArgumentException("Cannot stop " + serverInterfaceI.getFullName() + ".");
        }
        send(Language.ping());
    }

    private final void stop(Shortcut shortcutInterfaceI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        ShortcutHandler scHandler = (ShortcutHandler) getServerHandler().findChild((Rmap) shortcutInterfaceI);
        if (scHandler == null) {
            throw new com.rbnb.api.AddressException(shortcutInterfaceI + " not found.");
        } else if (!isAllowedAccess(((RBNB) getServerHandler()).getAddressHandler(), AddressPermissions.ROUTER)) {
            throw new com.rbnb.api.AddressException("Client address is not authorized to terminate shortcuts.");
        }
        scHandler.stop();
        send(Language.ping());
    }

    public final String toString() {
        if (getClientHandler() != null) {
            try {
                return ("RCO " + getClientHandler().getName() + " (" + super.toString() + ")");
            } catch (java.lang.Exception e) {
                return ("RCO " + super.toString());
            }
        } else {
            return ("RCO " + super.toString());
        }
    }
}
