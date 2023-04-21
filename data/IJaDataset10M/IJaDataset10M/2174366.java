package com.narfsnaf.pactstone.view.interview;

import com.narfsnaf.pactstone.model.PactStoneGameState;
import com.narfsnaf.pactstone.view.PactStoneView;

public abstract class InterviewPlayersView implements PactStoneView {

    protected PactStoneGameState gameState;

    public void setGameState(PactStoneGameState gameState) {
        this.gameState = gameState;
    }
}
