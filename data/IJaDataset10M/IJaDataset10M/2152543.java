package net.sf.doolin.app.sc.engine;

public interface ManagedClient<T extends ClientState, P extends ClientResponse> {

    ClientID getClientID();

    P play(int turnNumber, T state);
}
