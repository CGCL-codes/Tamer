package uips.communication.server;

import uips.clients.IClientFactory;
import uips.clients.IGenericClient;
import uips.communication.uip.interfaces.IUipClient;
import uips.tree.convertors.OuterToInnerConv;
import uips.tree.inner.factories.impl.UipTreeInnFactory;

public class ServerClientFactory implements IClientFactory {

    public static final ServerClientFactory SERVER_CLIENT_FACTORY = new ServerClientFactory();

    @Override
    public IGenericClient createClient(IUipClient uipClient) {
        return new ServerClient(uipClient, OuterToInnerConv.OUTER_TO_INNER_CONV, UipTreeInnFactory.UIP_TREE_INN_FACTORY);
    }
}
