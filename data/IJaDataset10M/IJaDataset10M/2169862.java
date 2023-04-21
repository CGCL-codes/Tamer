package mw.server.card.gold.brg;

import java.util.Observable;
import java.util.Observer;
import mw.server.model.Ability_Activated;
import mw.server.model.Card;
import mw.mtgforge.Command;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class DragonAppeasement {

    public static Card getCard(final GameManager game, final Card card) {
        final Ability_Activated drawCardAbility = new Ability_Activated(card) {

            public void resolve() {
                game.getPlayerById(card.getControllerID()).drawCard();
            }
        };
        drawCardAbility.setYesNoQuestion(card + ": draw a card?");
        drawCardAbility.setStackDescription(card + ": draw a card.");
        final Observer sacrificeCreatureObserver = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg instanceof Card) {
                    if (((Card) arg).getControllerID() == card.getControllerID()) {
                        game.getStack().add(drawCardAbility);
                    }
                }
            }
        };
        final Command intoPlay = new Command() {

            public void execute() {
                game.getManager().addSacrificeCreatureObserver(sacrificeCreatureObserver);
                game.getPlayerById(card.getControllerID()).getGameStatistics().addSkipYourDrawStep();
            }
        };
        card.setEntersTheBattlefieldCommand(intoPlay);
        final Command leavesPlayCommand = new Command() {

            public void execute() {
                game.getManager().removeSacrificeCreatureObserver(sacrificeCreatureObserver);
                game.getPlayerById(card.getControllerID()).getGameStatistics().subSkipYourDrawStep();
            }
        };
        card.setLeavesTheBattlefieldCommand(leavesPlayCommand);
        return card;
    }
}
