package games.strategy.engine.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Kevin Comcowich
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RepairFrontier extends DefaultNamed implements Iterable<RepairRule> {

    private final List<RepairRule> m_rules = new ArrayList<RepairRule>();

    private List<RepairRule> m_cachedRules;

    /**
	 * Creates new RepairFrontier
	 * 
	 * @param name
	 *            name of new repair frontier
	 * @param data
	 *            game data
	 */
    public RepairFrontier(final String name, final GameData data) {
        super(name, data);
    }

    public void addRule(final RepairRule rule) {
        if (m_rules.contains(rule)) throw new IllegalStateException("Rule already added:" + rule);
        m_rules.add(rule);
        m_cachedRules = null;
    }

    public void removeRule(final RepairRule rule) {
        if (!m_rules.contains(rule)) throw new IllegalStateException("Rule not present:" + rule);
        m_rules.remove(rule);
        m_cachedRules = null;
    }

    public List<RepairRule> getRules() {
        if (m_cachedRules == null) m_cachedRules = Collections.unmodifiableList(m_rules);
        return m_cachedRules;
    }

    public Iterator<RepairRule> iterator() {
        return getRules().iterator();
    }
}
