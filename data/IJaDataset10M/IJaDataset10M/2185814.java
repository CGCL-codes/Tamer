package reddantcore.modules.example;

import peer.Peer;
import peer.example.HelloWorldClient;
import peer.example.HelloWorldProvider;
import peer.service.client.ServiceClient;
import peer.service.client.ServiceClientFactory;
import peer.service.provider.ServiceProvider;
import peer.service.provider.ServiceProviderFactory;
import reddantcore.modules.api.IPeerClient;
import reddantcore.modules.api.IPeerProvider;
import reddantcore.modules.api.Module;

/**
 *
 * @author tpasquie
 */
public class ModuleHelloWorld extends Module implements IPeerClient, IPeerProvider {

    private static final String MODULE_NAME = "Hello World";

    private ServiceClient client;

    private ServiceProvider provider;

    @Override
    public void run() {
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initClient() throws Exception {
        client = ServiceClientFactory.newClient("test", HelloWorldClient.class, null);
    }

    @Override
    public ServiceClient getClient() {
        return client;
    }

    @Override
    public void initProvider() throws Exception {
        provider = ServiceProviderFactory.newServer("test:helloworld:" + Peer.getConfiguration().getString("user.name"), HelloWorldProvider.class);
    }

    @Override
    public ServiceProvider getProvider() {
        return provider;
    }
}
