package com.wikiup.romulan.gom.imp.mobility;

import com.wikiup.romulan.gom.Model;
import com.wikiup.romulan.gom.Player;
import com.wikiup.romulan.gom.inf.RankInf;
import com.wikiup.romulan.util.GameUtil;

public class ImpulseDrive extends Mobility implements RankInf {

    private Player player;

    public ImpulseDrive(Player player, Model model) {
        this.player = player;
    }

    @Override
    public int getSpeed() {
        int rank = getRank();
        return (int) (super.getSpeed() * (1 + rank * 0.2));
    }

    public int getRank() {
        return GameUtil.getTechnologyRank(player, "impulse-drive");
    }

    public void upgrade() {
    }
}
