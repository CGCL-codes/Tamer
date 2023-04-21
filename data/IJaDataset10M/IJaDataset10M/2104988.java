package it.unibz.izock.networking;

import it.unibz.izock.WattenListener;
import it.unibz.izock.events.IZockEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The Class SingleClientBinding is a responsible to pass all
 * events that has been delivered to a Player, to it's remote 
 * counterpart, a GameClient.
 */
public class SingleClientBinding implements WattenListener {

    /** The _client. */
    private GameClient _client;

    /**
	 * Instantiates a new single client binding.
	 * 
	 * @param remoteClient the remote client
	 */
    public SingleClientBinding(GameClient remoteClient) {
        _client = remoteClient;
    }

    @SuppressWarnings("unchecked")
    public void handleEvent(IZockEvent anEvent) {
        try {
            Class<?> theClass = Class.forName(anEvent.getID());
            Method mClient = _client.getClass().getMethod("handleEvent", new Class[] { theClass });
            mClient.invoke(_client, theClass.cast(anEvent));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
