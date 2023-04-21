package com.rezzix.kowa.actors;

import java.util.ArrayList;
import java.util.Collections;
import com.rezzix.kowa.ai.Ai;
import com.rezzix.kowa.ai.Aiadnane;
import com.rezzix.kowa.ai.CommonAi;
import com.rezzix.kowa.ai.HardAi;
import com.rezzix.kowa.ai.NormalAi;
import com.rezzix.kowa.board.Cell;
import com.rezzix.kowa.game.Game;
import com.rezzix.kowa.market.Merchandise;
import com.rezzix.kowa.util.Constants;

public class AiPlayer extends Player {

    int level;

    Ai ai;

    public AiPlayer(String name, Game game, Cell cell, int level) {
        super(name, game, Constants.defaultLifepoints, cell);
        this.level = level;
        if (level == Constants.iaEasy) {
            ai = new CommonAi(getGame(), this);
        } else if (level == Constants.iaNormal) {
            ai = new NormalAi(getGame(), this);
        } else if (level == Constants.iaHard) {
            ai = new HardAi(getGame(), this);
        } else if (level == Constants.iaadnane) {
            ai = new Aiadnane(getGame(), this);
        }
    }

    @Override
    public void makeMove(Game game) {
        manageEscorts();
        commerceInMarket();
        dealWithMercenary();
        setState(Constants.playerStateWaiting);
    }

    private void dealWithMercenary() {
        if (getGame().getActors().getMercenary(cell) == null) {
            return;
        }
        Player foe = ai.sendMercenaryTo();
        if (foe != null) {
            engage(getGame().getActors().getMercenary(cell), foe);
        }
    }

    private void manageEscorts() {
        int advisedEscortNbr = ai.adviseEscortsToHire();
        if (escorts.size() < advisedEscortNbr) {
            hire(getGame().getActors().getEscort(cell));
        } else if (escorts.size() > advisedEscortNbr) {
            fire(getGame().getActors().getEscort(cell));
        }
    }

    @Override
    public String toString() {
        return "(AI" + level + ") " + super.toString();
    }

    private void commerceInMarket() {
        ArrayList<Merchandise> merchandises = new ArrayList<Merchandise>();
        merchandises.addAll(wareHouse.getMerchandises());
        for (Merchandise merchandise : merchandises) {
            int advised = ai.adviseQuantityToSell(merchandise);
            if (advised > 0) {
                System.out.println(getName() + " sells " + advised + " from " + merchandise.getName());
                sellAdvisedQuantity(merchandise, advised);
            }
        }
        ArrayList<Merchandise> shuffledMerchandise = new ArrayList<Merchandise>();
        shuffledMerchandise.addAll(getGame().getMarket().getMerchandises());
        Collections.shuffle(shuffledMerchandise);
        for (Merchandise merchandise : shuffledMerchandise) {
            int advised = ai.adviseQuantityToBuy(merchandise);
            if (advised > 0) {
                buyAdvisedQuantity(merchandise, advised);
            }
        }
    }

    private void buyAdvisedQuantity(Merchandise merchandise, int advisedQuantityToBuy) {
        buy(merchandise, advisedQuantityToBuy);
    }

    private void sellAdvisedQuantity(Merchandise merchandise, int advisedQuantityToSell) {
        sell(merchandise, advisedQuantityToSell);
    }
}
