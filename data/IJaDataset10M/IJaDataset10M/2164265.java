package net.kortsoft.gameportlet.culandcon.workflow;

import net.kortsoft.gameportlet.culandcon.CulAndConGame;
import net.kortsoft.gameportlet.culandcon.CulAndConTurn;
import net.kortsoft.gameportlet.culandcon.Culture;
import net.kortsoft.gameportlet.culandcon.view.CulAndConView;

public class PopulationPhase extends TurnPhase {

    public PopulationPhase(CulAndConGame culAndConGame) {
        super(culAndConGame);
    }

    @Override
    protected String phaseName() {
        return "Population Phase";
    }

    @Override
    protected CulAndConView doExecuteEvent(CulAndConEvent culAndConEvent) throws EventException {
        CulAndConGame culAndConGame = culAndConEvent.getCulAndConGame();
        CulAndConTurn turn = culAndConGame.getTurn();
        Culture currentCulture = newTurn(culAndConGame, turn);
        switch(culAndConEvent.getType()) {
            default:
                int popAdd = currentCulture.growPopulation();
                currentCulture.getCulAndConGame().getGameLog().log("Added " + popAdd + " agriculture specialists");
                return new ResourcePhase(getCulAndConGame()).executeEvent(culAndConEvent);
        }
    }

    private Culture newTurn(CulAndConGame culAndConGame, CulAndConTurn turn) {
        Culture currentCulture = getCurrentCulture();
        Culture nextCulture = culAndConGame.nextCulture(currentCulture);
        culAndConGame.getGameLog().log("Now is turn(" + culAndConGame.getTurn().getTurnNumber() + ") of " + nextCulture);
        nextCulture.setupTurn();
        culAndConGame.getGameLog().log(nextCulture.cultureBrief());
        turn.setCurrentCulture(nextCulture);
        if (nextCulture.equals(culAndConGame.getCulturesInOrder().get(0))) turn.increaseTurn();
        return nextCulture;
    }
}
