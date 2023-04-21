package com.kenstevens.stratinit.server.ws.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.kenstevens.stratinit.model.Relation;
import com.kenstevens.stratinit.server.ws.TwoPlayerBase;
import com.kenstevens.stratinit.type.RelationType;
import com.kenstevens.stratinit.ws.Result;
import com.kenstevens.stratinit.ws.model.SIRelation;

public class RelationChangeTest extends TwoPlayerBase {

    @Autowired
    EventQueue eventQueue;

    @Test
    public void neutralToWar() {
        changedTo(RelationType.WAR);
    }

    @Test
    public void neutralToAllied() {
        changedTo(RelationType.ALLIED);
    }

    @Test
    public void neutralToNAP() {
        changedTo(RelationType.NAP);
    }

    @Test
    public void neutralToFriendly() {
        changedTo(RelationType.FRIENDLY);
    }

    @Test
    public void neutralToMe() {
        Result<SIRelation> result = stratInit.setRelation(GAME_ID, NATION_THEM_ID, RelationType.ME);
        assertFalseResult(result);
    }

    public void changedTo(RelationType nextType) {
        Result<SIRelation> result = stratInit.setRelation(GAME_ID, NATION_THEM_ID, nextType);
        assertResult(result);
        Relation relation = gameDao.getRelation(NATION_ME, NATION_THEM);
        assertEquals(result.toString(), nextType, relation.getType());
        assertNull(relation.getNextType());
        assertNull(relation.getSwitchTime());
        assertFalse(eventQueue.cancel(relation));
    }

    public void changedToDelayed(RelationType nextType) {
        Relation relation = gameDao.getRelation(NATION_ME, NATION_THEM);
        RelationType pre = relation.getType();
        Result<SIRelation> result = stratInit.setRelation(GAME_ID, NATION_THEM_ID, nextType);
        assertResult(result);
        relation = gameDao.getRelation(NATION_ME, NATION_THEM);
        assertEquals(result.toString(), pre, relation.getType());
        assertEquals(result.toString(), nextType, relation.getNextType());
        assertNotNull(relation.getSwitchTime());
        assertTrue(eventQueue.cancel(relation));
    }

    @Test
    public void allyToFriendly() {
        neutralToAllied();
        changedTo(RelationType.FRIENDLY);
    }

    @Test
    public void allyToNAP() {
        neutralToAllied();
        changedToDelayed(RelationType.NAP);
    }

    @Test
    public void allyToNeutral() {
        neutralToAllied();
        changedToDelayed(RelationType.NEUTRAL);
    }

    @Test
    public void allyToWar() {
        neutralToAllied();
        changedToDelayed(RelationType.WAR);
    }

    @Test
    public void friendlyToAllied() {
        neutralToFriendly();
        changedTo(RelationType.ALLIED);
    }

    @Test
    public void friendlyToNAP() {
        neutralToFriendly();
        changedToDelayed(RelationType.NAP);
    }

    @Test
    public void friendlyToNeutral() {
        neutralToFriendly();
        changedToDelayed(RelationType.NEUTRAL);
    }

    @Test
    public void friendlyToWar() {
        neutralToFriendly();
        changedToDelayed(RelationType.WAR);
    }

    @Test
    public void NAPToAllied() {
        neutralToNAP();
        changedTo(RelationType.ALLIED);
    }

    @Test
    public void NAPToFriendly() {
        neutralToNAP();
        changedTo(RelationType.FRIENDLY);
    }

    @Test
    public void NAPToNeutral() {
        neutralToNAP();
        changedToDelayed(RelationType.NEUTRAL);
    }

    @Test
    public void NAPToWar() {
        neutralToNAP();
        changedToDelayed(RelationType.WAR);
    }
}
