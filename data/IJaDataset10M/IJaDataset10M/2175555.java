package games.strategy.triplea.delegate.remote;

import games.strategy.engine.message.IRemote;
import games.strategy.engine.pbem.PBEMMessagePoster;

/**
 * @author Tony Clayton
 */
public interface IAbstractEndTurnDelegate extends IRemote {

    public boolean postTurnSummary(PBEMMessagePoster poster);

    public void setHasPostedTurnSummary(boolean hasPostedTurnSummary);

    public boolean getHasPostedTurnSummary();
}
