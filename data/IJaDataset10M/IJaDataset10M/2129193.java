package myriadempires.core.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import myriadempires.core.Game;
import myriadempires.core.Map;
import myriadempires.core.interfaces.ITerritory;
import myriadempires.core.AIUtils.TerritoryAnalysis;
import myriadempires.core.AIUtils.Comparators.GarrisonComparator;
import myriadempires.core.annotation.Player;
import myriadempires.core.annotation.PlayerType;

/**
 *
 * @author Richard
 */
@Player(PlayerType.AI)
public class Miser extends DefaultPlayer {

    private static int PlayerCount = 1;

    public Miser() {
        super("Miser " + PlayerCount, new Color((int) Math.floor(Math.random() * 0xFFFFFF)));
        PlayerCount++;
    }

    public void placeRecruits() {
        ITerritory[] ters = Game.Map.getTerritories(Game.getReference(this));
        ArrayList<ITerritory> alTers = new ArrayList<ITerritory>();
        int foreign = 0;
        for (ITerritory tr : ters) {
            if (TerritoryAnalysis.hasForeignBorder(tr, this)) {
                alTers.add(tr);
            }
        }
        ters = alTers.toArray(new ITerritory[0]);
        alTers.clear();
        while (reinforcements > 0) {
            Arrays.sort(ters, new GarrisonComparator(true));
            ters[0].addToGarrison(1);
            reinforcements--;
        }
    }

    public void doTurn() {
        if (getRecruits() > 0) placeRecruits();
        return;
    }
}
