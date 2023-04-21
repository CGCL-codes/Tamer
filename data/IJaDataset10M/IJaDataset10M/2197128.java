package games.strategy.triplea.delegate.dataObjects;

import games.strategy.engine.data.TechnologyFrontier;

/**
 * Used to describe a tech roll.
 * advance may be null if the game does not support rolling for
 * specific techs
 */
public class TechRoll {

    private final TechnologyFrontier m_tech;

    private final int m_rolls;

    private int m_newTokens;

    public TechRoll(final TechnologyFrontier advance, final int rolls) {
        m_rolls = rolls;
        m_tech = advance;
    }

    public TechRoll(final TechnologyFrontier advance, final int rolls, final int newTokens) {
        m_rolls = rolls;
        m_tech = advance;
        m_newTokens = newTokens;
    }

    public int getRolls() {
        return m_rolls;
    }

    public TechnologyFrontier getTech() {
        return m_tech;
    }

    public int getNewTokens() {
        return m_newTokens;
    }

    public void setNewTokens(final int tokens) {
        this.m_newTokens = tokens;
    }
}
