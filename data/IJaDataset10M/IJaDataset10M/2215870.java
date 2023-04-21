package tchoukstats.client.filter;

import tchoukstats.client.ClientAction;

public interface IClientFilter {

    public boolean filter(ClientAction action);

    public void addFilter(ClientFilter nextFilter);

    public boolean isEmpty();
}
