package myriadempires.pantheon.oldModules;

import java.util.ArrayList;
import java.util.Arrays;
import myriadempires.pantheon.PantheonModule;
import myriadempires.pantheon.AICapability;
import myriadempires.core.interfaces.IContinent;
import myriadempires.core.interfaces.ITerritory;
import myriadempires.core.AIUtils.EngagementResult;
import myriadempires.core.AIUtils.EngagementSimulator;
import myriadempires.core.Game;
import myriadempires.pantheon.oldAIModule;

/**
 * Greek minor god, twin of {@linkplain Alexiares}.<p>
 *
 * Aims to prevent any other player gaining / keeping a continent
 * 
 * @author Richard
 * @todo improve documentaton
 */
@PantheonModule(Garrison = AICapability.ADVISORY, Engage = AICapability.ADVISORY)
public class Anicetus extends oldAIModule {

    @Override
    public int garrison(ITerritory tr, int numTroops) {
        int adv = 0;
        for (ITerritory to : tr.getBorders(true)) {
            IContinent cont = to.getContinent();
            ArrayList<ITerritory> cTers = new ArrayList<ITerritory>(Arrays.asList(cont.getTerritories()));
            cTers.retainAll(Arrays.asList(Game.Map.getTerritories(to.getOwner())));
            EngagementResult er = EngagementSimulator.CalculateResult(tr.getGarrison(), to.getGarrison());
            int a = (int) ((cont.getBonus() * Math.pow(cTers.size(), 2) / cont.size()) * (er.defRemn - er.probability * (er.atkRemn + er.defRemn)));
            adv = Math.max(adv, a);
        }
        return adv;
    }

    @Override
    public int engage(ITerritory from, ITerritory to) {
        IContinent cont = to.getContinent();
        ArrayList<ITerritory> cTers = new ArrayList<ITerritory>(Arrays.asList(cont.getTerritories()));
        cTers.retainAll(Arrays.asList(Game.Map.getTerritories(to.getOwner())));
        EngagementResult er = EngagementSimulator.CalculateResult(from.getGarrison(), to.getGarrison());
        return (int) (Math.pow(er.probability + .5d, 2) * cont.getBonus() * Math.pow(cTers.size(), 2) / cont.size());
    }
}
