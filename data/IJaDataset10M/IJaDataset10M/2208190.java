package symore.dissemination;

import java.util.Properties;

/**
 * Factory that creates the synchronization and dissemination component used to spread
 * messages/events in the replication group. A concrete class implementing this interface
 * has to be configured with the property "Synchronizer".
 * 
 * The synchronization service takes care that all participants get all events, even if some are 
 * temporarily unavailable. The dissemination component simply broadcasts messages to all currently
 * available participants.  
 * 
 * @author Frank Bregulla, Manuel Scholz
 */
public abstract class AbstractMsgSpreadFactory {

    public AbstractMsgSpreadFactory() {
    }

    public abstract ISymoreSynchronizer getSynchronizer(Properties properties) throws DisseminationException;

    public abstract void stop();

    public abstract ISymoreDisseminator getDisseminator(Properties properties) throws DisseminationException;
}
