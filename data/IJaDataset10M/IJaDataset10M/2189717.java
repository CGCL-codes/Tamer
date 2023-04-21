package mw.server.card.gold.ugw;

import mw.server.model.Ability;
import mw.server.model.Card;
import mw.server.model.SpellAbility;
import mw.mtgforge.Command;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class MessengerFalcons {

    public static Card getCard(final GameManager game, final Card card) {
        final Command intoPlay = new Command() {

            final SpellAbility ability = new Ability(card, "0") {

                public void resolve() {
                    Card c = getSourceCard();
                    game.getPlayerById(c.getControllerID()).drawCard();
                }
            };

            public void execute() {
                ability.setStackDescription(card + ": Draw a card.");
                game.getStack().add(ability);
            }
        };
        card.setEntersTheBattlefieldCommand(intoPlay);
        return card;
    }
}
