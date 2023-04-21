package org.openaion.gameserver.world.container;

import java.util.Map;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastMap;

/**
 * @author Sarynth
 *
 */
public class KiskContainer {

    private final Map<Integer, Kisk> kiskByPlayerObjectId = new FastMap<Integer, Kisk>().shared();

    public void add(Kisk kisk, Player player) {
        if (this.kiskByPlayerObjectId.put(player.getObjectId(), kisk) != null) throw new DuplicateAionObjectException();
    }

    public Kisk get(Player player) {
        return this.kiskByPlayerObjectId.get(player.getObjectId());
    }

    public void remove(Player player) {
        this.kiskByPlayerObjectId.remove(player.getObjectId());
    }

    /**
	 * @return
	 */
    public int getCount() {
        return this.kiskByPlayerObjectId.size();
    }
}
