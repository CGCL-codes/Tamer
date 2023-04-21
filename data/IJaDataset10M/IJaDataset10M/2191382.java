package com.wikiup.romulan.gom.imp.cost;

import com.wikiup.romulan.gom.Model;
import com.wikiup.romulan.gom.Player;
import com.wikiup.romulan.gom.imp.rc.ResourceContainer;
import com.wikiup.romulan.gom.inf.CostInf;
import com.wikiup.romulan.gom.inf.NamedModelInf;
import com.wikiup.romulan.util.GameUtil;

public class UnitCost extends ResourceContainer implements CostInf, NamedModelInf {

    private Player player;

    public UnitCost(Player player, Model model) {
        this.player = player;
    }

    public int getTime() {
        Model planet = GameUtil.getCurrentPlanet(player);
        int starport = GameUtil.getBuildingRank(planet, "starport");
        int naniteFactory = GameUtil.getBuildingRank(planet, "nanite-factory");
        return (getMetal() + getCrystal()) * 36 / 25 / (starport + 1) >> naniteFactory;
    }

    public String getName() {
        return "cost";
    }
}
