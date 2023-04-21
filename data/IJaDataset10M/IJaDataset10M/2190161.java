package games.strategy.triplea.delegate;

import games.strategy.common.delegate.BaseDelegate;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.delegate.IDelegateBridge;
import games.strategy.engine.message.IRemote;
import games.strategy.triplea.attatchments.ICondition;
import games.strategy.triplea.attatchments.TriggerAttachment;
import games.strategy.util.CompositeMatchAnd;
import games.strategy.util.CompositeMatchOr;
import games.strategy.util.Match;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Logic for activating tech rolls. This delegate requires the
 * TechnologyDelegate to run correctly.
 * 
 * @author Ali Ibrahim
 * @version 1.0
 */
public class TechActivationDelegate extends BaseDelegate {

    private boolean m_needToInitialize = true;

    /** Creates new TechActivationDelegate */
    public TechActivationDelegate() {
    }

    /**
	 * Called before the delegate will run. In this class, this does all the
	 * work.
	 */
    @Override
    public void start(final IDelegateBridge aBridge) {
        super.start(aBridge);
        final GameData data = getData();
        if (!m_needToInitialize) return;
        final Map<PlayerID, Collection<TechAdvance>> techMap = DelegateFinder.techDelegate(data).getAdvances();
        final Collection<TechAdvance> advances = techMap.get(m_player);
        if ((advances != null) && (advances.size() > 0)) {
            m_bridge.getHistoryWriter().startEvent(m_player.getName() + " activating " + advancesAsString(advances));
            for (final TechAdvance advance : advances) {
                TechTracker.addAdvance(m_player, m_bridge, advance);
            }
        }
        techMap.put(m_player, null);
        if (games.strategy.triplea.Properties.getTriggers(data)) {
            final Match<TriggerAttachment> techActivationDelegateTriggerMatch = new CompositeMatchAnd<TriggerAttachment>(TriggerAttachment.availableUses, TriggerAttachment.whenOrDefaultMatch(null, null), new CompositeMatchOr<TriggerAttachment>(TriggerAttachment.unitPropertyMatch(), TriggerAttachment.techMatch(), TriggerAttachment.supportMatch()));
            final HashSet<TriggerAttachment> toFirePossible = TriggerAttachment.collectForAllTriggersMatching(new HashSet<PlayerID>(Collections.singleton(m_player)), techActivationDelegateTriggerMatch, m_bridge);
            if (!toFirePossible.isEmpty()) {
                final HashMap<ICondition, Boolean> testedConditions = TriggerAttachment.collectTestsForAllTriggers(toFirePossible, m_bridge);
                final Set<TriggerAttachment> toFireTestedAndSatisfied = new HashSet<TriggerAttachment>(Match.getMatches(toFirePossible, TriggerAttachment.isSatisfiedMatch(testedConditions)));
                TriggerAttachment.triggerUnitPropertyChange(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
                TriggerAttachment.triggerTechChange(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
                TriggerAttachment.triggerSupportChange(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
            }
        }
        m_needToInitialize = false;
    }

    @Override
    public void end() {
        super.end();
        m_needToInitialize = true;
    }

    @Override
    public Serializable saveState() {
        final TechActivationExtendedDelegateState state = new TechActivationExtendedDelegateState();
        state.superState = super.saveState();
        state.m_needToInitialize = m_needToInitialize;
        return state;
    }

    @Override
    public void loadState(final Serializable state) {
        final TechActivationExtendedDelegateState s = (TechActivationExtendedDelegateState) state;
        super.loadState(s.superState);
        m_needToInitialize = s.m_needToInitialize;
    }

    private String advancesAsString(final Collection<TechAdvance> advances) {
        final Iterator<TechAdvance> iter = advances.iterator();
        int count = advances.size();
        final StringBuilder text = new StringBuilder();
        while (iter.hasNext()) {
            final TechAdvance advance = iter.next();
            text.append(advance.getName());
            count--;
            if (count > 1) text.append(", ");
            if (count == 1) text.append(" and ");
        }
        return text.toString();
    }

    @Override
    public Class<? extends IRemote> getRemoteType() {
        return null;
    }
}

@SuppressWarnings("serial")
class TechActivationExtendedDelegateState implements Serializable {

    Serializable superState;

    public boolean m_needToInitialize;
}
