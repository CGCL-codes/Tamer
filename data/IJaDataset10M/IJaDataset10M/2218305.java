package games.strategy.engine.delegate;

import games.strategy.engine.GameOverException;
import games.strategy.engine.data.Change;
import games.strategy.engine.data.CompositeChange;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.framework.IGame;
import games.strategy.engine.framework.ServerGame;
import games.strategy.engine.history.IDelegateHistoryWriter;
import games.strategy.engine.message.IChannelSubscribor;
import games.strategy.engine.message.IRemote;
import games.strategy.engine.message.MessengerException;
import games.strategy.engine.random.IRandomSource;
import games.strategy.engine.random.RandomStats;
import java.util.Properties;

/**
 * 
 * Default implementation of DelegateBridge
 * 
 * @author Sean Bridges
 */
public class DefaultDelegateBridge implements IDelegateBridge {

    private final GameData m_data;

    private final IGame m_game;

    private final IDelegateHistoryWriter m_historyWriter;

    private final RandomStats m_randomStats;

    private final DelegateExecutionManager m_delegateExecutionManager;

    private IRandomSource m_randomSource;

    /** Creates new DefaultDelegateBridge */
    public DefaultDelegateBridge(final GameData data, final IGame game, final IDelegateHistoryWriter historyWriter, final RandomStats randomStats, final DelegateExecutionManager delegateExecutionManager) {
        m_data = data;
        m_game = game;
        m_historyWriter = historyWriter;
        m_randomStats = randomStats;
        m_delegateExecutionManager = delegateExecutionManager;
    }

    public GameData getData() {
        return m_data;
    }

    public PlayerID getPlayerID() {
        return m_data.getSequence().getStep().getPlayerID();
    }

    public void setRandomSource(final IRandomSource randomSource) {
        m_randomSource = randomSource;
    }

    /**
	 * All delegates should use random data that comes from both players so that
	 * neither player cheats.
	 */
    public int getRandom(final int max, final String annotation) {
        final int random = m_randomSource.getRandom(max, annotation);
        m_randomStats.addRandom(random);
        return random;
    }

    /**
	 * Delegates should not use random data that comes from any other source.
	 */
    public int[] getRandom(final int max, final int count, final String annotation) {
        final int[] rVal = m_randomSource.getRandom(max, count, annotation);
        m_randomStats.addRandom(rVal);
        return rVal;
    }

    public void addChange(final Change aChange) {
        if (aChange instanceof CompositeChange) {
            final CompositeChange c = (CompositeChange) aChange;
            if (c.getChanges().size() == 1) {
                addChange(c.getChanges().get(0));
                return;
            }
        }
        if (!aChange.isEmpty()) m_game.addChange(aChange);
    }

    /**
	 * Returns the current step name
	 */
    public String getStepName() {
        return m_data.getSequence().getStep().getName();
    }

    public IDelegateHistoryWriter getHistoryWriter() {
        return m_historyWriter;
    }

    private Object getOutbound(final Object o) {
        final Class<?>[] interfaces = o.getClass().getInterfaces();
        return m_delegateExecutionManager.createOutboundImplementation(o, interfaces);
    }

    public IRemote getRemote() {
        return getRemote(getPlayerID());
    }

    public IRemote getRemote(final PlayerID id) {
        try {
            final Object implementor = m_game.getRemoteMessenger().getRemote(ServerGame.getRemoteName(id, m_data));
            return (IRemote) getOutbound(implementor);
        } catch (final MessengerException me) {
            throw new GameOverException("Game Over");
        }
    }

    public IChannelSubscribor getDisplayChannelBroadcaster() {
        final Object implementor = m_game.getChannelMessenger().getChannelBroadcastor(ServerGame.getDisplayChannel(m_data));
        return (IChannelSubscribor) getOutbound(implementor);
    }

    public Properties getStepProperties() {
        return m_data.getSequence().getStep().getProperties();
    }

    public void leaveDelegateExecution() {
        m_delegateExecutionManager.leaveDelegateExecution();
    }

    public void enterDelegateExecution() {
        m_delegateExecutionManager.enterDelegateExecution();
    }

    public void stopGameSequence() {
        ((ServerGame) m_game).stopGameSequence();
    }
}
