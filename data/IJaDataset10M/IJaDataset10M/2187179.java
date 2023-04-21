package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.model.templates.siege.SiegeLocationTemplate;

/**
 * @author Sarynth
 *
 */
public class Commander extends SiegeLocation {

    public Commander(SiegeLocationTemplate template) {
        super(template);
        setVulnerable(false);
        setNextState(0);
    }

    @Override
    public int getInfluenceValue() {
        return 0;
    }
}
