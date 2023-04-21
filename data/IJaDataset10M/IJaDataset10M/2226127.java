package org.minions.stigma.server.npc;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlRootElement;
import org.minions.stigma.game.map.Map;
import org.minions.stigma.game.map.MapType;
import org.minions.stigma.globals.Position;
import org.minions.stigma.server.managers.ActorManager;
import org.minions.stigma.server.managers.WorldManager;
import org.minions.utils.logger.Log;

/**
 * Represents static layer over map. NPC will be put on
 * given position. This layer type is rather not very
 * reusable.
 */
@XmlRootElement(name = "staticNpcLayer")
public class StaticNpcLayer extends NpcLayer {

    private java.util.Map<Position, NpcDescription> npcMap = new HashMap<Position, NpcDescription>();

    /**
     * Returns map of NPCs positions to descriptions. Needed
     * for JAXB.
     * @return map of NPCS
     */
    public java.util.Map<Position, NpcDescription> getNpcMap() {
        return npcMap;
    }

    /**
     * Sets new of NPCs positions to descriptions. Needed
     * for JAXB.
     * @param npcMap
     *            new map
     */
    public void setNpcMap(java.util.Map<Position, NpcDescription> npcMap) {
        this.npcMap = npcMap;
    }

    /**
     * Adds NPC on given position (overwrites existing).
     * @param p
     *            position to put NPC on
     * @param desc
     *            description of NPC
     */
    public void addNpc(Position p, NpcDescription desc) {
        npcMap.put(p, desc);
    }

    /**
     * Returns NPC from given position, or {@code null} if
     * no such.
     * @param p
     *            requested position
     * @return NPC description from given position
     */
    public NpcDescription getNpc(Position p) {
        return npcMap.get(p);
    }

    /**
     * Removes NPC from given position.
     * @param p
     *            requested position
     */
    public void removeNpc(Position p) {
        npcMap.remove(p);
    }

    /** {@inheritDoc} */
    @Override
    public boolean putNpcs(WorldManager worldManager, Map m) {
        MapType t = m.getType();
        if (t == null) {
            Log.logger.error("Not fully initialized map passed to StaticNpcLayer");
            return false;
        }
        for (java.util.Map.Entry<Position, NpcDescription> entry : npcMap.entrySet()) {
            Position p = entry.getKey();
            if (t.getTile(p).isPassable()) {
                ActorManager a = NpcFactory.buildNpc(worldManager, entry.getValue(), t.getId(), m.getInstanceNo(), p);
                worldManager.addActor(a);
            } else Log.logger.warn("Bad position for NPC: " + p + " on map: " + t.getId());
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        String str = "[ StaticNpcLayer:\n";
        for (java.util.Map.Entry<Position, NpcDescription> entry : npcMap.entrySet()) {
            str += entry.getKey();
            str += " -> ";
            str += entry.getValue();
            str += "\n";
        }
        return str + "]";
    }
}
