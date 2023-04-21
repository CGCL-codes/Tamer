package net.sf.doolin.app.sc.engine.delegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.doolin.app.sc.engine.InstanceID;
import net.sf.doolin.app.sc.engine.InstanceState;
import net.sf.doolin.app.sc.engine.InstanceStateFactory;
import net.sf.doolin.app.sc.engine.client.Client;
import net.sf.doolin.app.sc.engine.client.ClientID;
import net.sf.doolin.app.sc.engine.common.Message;
import net.sf.doolin.app.sc.engine.common.MessageType;
import net.sf.doolin.app.sc.engine.instance.ClientRegistration;
import net.sf.doolin.app.sc.engine.instance.DefaultInstance;
import net.sf.doolin.app.sc.engine.turn.TurnExecutor;
import net.sf.doolin.util.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultDelegateInstance<S extends DefaultDelegateInstance<S, T, P>, T extends InstanceState, P extends ClientDelegateResponse> extends DefaultInstance<S, T, P> implements DelegateInstance<S, T, P> {

    private static final Log log = LogFactory.getLog(DefaultDelegateInstance.class);

    public static final String DELEGATE_INSTANCE_DISCONNECTION = "DelegateInstance.Disconnection";

    private final ClientDelegateFactory<T, P> clientDelegateFactory;

    private final InstanceStateFactory<S, T, P> instanceStateFactory;

    private final List<ClientDelegate<T, P>> delegates = new ArrayList<ClientDelegate<T, P>>();

    public DefaultDelegateInstance(TurnExecutor<S, T, P> turnExecutor, InstanceID instanceID, String name, int connectionMax, String masterClientName, Map<String, String> metaInformation, Date timestamp, int turnNumber, ClientDelegateFactory<T, P> clientDelegateFactory, InstanceStateFactory<S, T, P> instanceStateFactory) {
        super(turnExecutor, instanceID, name, connectionMax, masterClientName, metaInformation, timestamp, turnNumber);
        this.clientDelegateFactory = clientDelegateFactory;
        this.instanceStateFactory = instanceStateFactory;
    }

    @Override
    protected ClientRegistration<T, P> createClientRegistration(ClientID clientID, Client<T, P> client) {
        ClientRegistration<T, P> registration = super.createClientRegistration(clientID, client);
        ClientDelegate<T, P> delegate = this.clientDelegateFactory.createDelegate(registration);
        registration.setProperty(ClientDelegate.class, delegate);
        this.delegates.add(delegate);
        return registration;
    }

    /**
	 * Notifies all delegates that <code>cid</code> client has disconnected.
	 */
    @Override
    public void disconnect(ClientID cid) {
        super.disconnect(cid);
        sendMessages(new Message(MessageType.WARNING, getString(cid, DELEGATE_INSTANCE_DISCONNECTION, cid.getClientName())));
    }

    @Override
    public List<ClientDelegate<T, P>> getClientDelegates() {
        return this.delegates;
    }

    @Override
    public T getClientState(ClientID cid) {
        @SuppressWarnings("unchecked") S thisInstance = (S) this;
        return this.instanceStateFactory.extractState(cid, thisInstance);
    }

    protected String getString(ClientID cid, String code, Object... parameters) {
        return Strings.get(cid.getLocale(), code, parameters);
    }

    protected synchronized void sendMessages(Message... messages) {
        String logMessages = StringUtils.join(messages, "\n");
        for (ClientDelegate<T, P> delegate : this.delegates) {
            ClientID cid = delegate.getClientID();
            if (isConnected(cid)) {
                log.debug(String.format("[Instance][%s] Sending messages to %s%n%s", getName(), cid.getClientName(), logMessages));
                delegate.sendMessages(messages);
            }
        }
    }
}
