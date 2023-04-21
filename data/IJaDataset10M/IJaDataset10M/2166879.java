package net.jetrix.listeners.interceptor;

import net.jetrix.Client;
import net.jetrix.ClientRepository;
import net.jetrix.Message;
import net.jetrix.User;
import net.jetrix.clients.QueryClient;
import net.jetrix.messages.NoConnectingMessage;

/**
 * Interceptor checking the validity of the name. The client will be rejected
 * if the name is already in use, empty or equals to "Server".
 *
 * @since 0.3
 *
 * @author Emmanuel Bourg
 * @version $Revision: 856 $, $Date: 2010-05-04 13:00:37 -0400 (Tue, 04 May 2010) $
 */
public class NameCheckInterceptor implements ClientInterceptor {

    public boolean isValidating() {
        return true;
    }

    public void process(Client client) throws ClientValidationException {
        User user = client.getUser();
        if (client instanceof QueryClient) {
            return;
        }
        if (isNameUsed(user.getName())) {
            Message m = new NoConnectingMessage("Nickname already in use!");
            client.send(m);
            client.disconnect();
            throw new ClientValidationException();
        }
        if (!isNamedAccepted(user.getName())) {
            Message m = new NoConnectingMessage("Invalid name!");
            client.send(m);
            client.disconnect();
            throw new ClientValidationException();
        }
    }

    protected boolean isNameUsed(String name) {
        ClientRepository repository = ClientRepository.getInstance();
        return repository.getClient(name) != null;
    }

    protected boolean isNamedAccepted(String name) {
        return name != null && !"server".equalsIgnoreCase(name) && name.indexOf(" ") == -1;
    }
}
