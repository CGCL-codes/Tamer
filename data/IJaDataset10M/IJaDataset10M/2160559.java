package mw.server.card.gold.ub;

import mw.server.list.CardList;
import mw.server.model.Ability;
import mw.server.model.Card;
import mw.server.model.Spell;
import mw.server.model.SpellAbility;
import mw.server.ChoiceCommand;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class Brainbite {

    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility discardAbility = new Ability(card, "0") {

            public void resolve() {
                int pid = card.getControllerID();
                if (getTargetCard() != null) {
                    game.discardCard(game.getOpponentById(pid).getPlayerId(), getTargetCard());
                    game.getManager().revealCard(getTargetCard());
                }
                game.getPlayerById(pid).drawCard();
            }
        };
        final ChoiceCommand runtime = new ChoiceCommand() {

            public void execute() {
                int pid = card.getControllerID();
                Card[] hand = game.getOpponentById(pid).getHandCards();
                CardList choice = new CardList(hand);
                setInputChoice(choice);
            }
        };
        final SpellAbility spell = new Spell(card) {

            public void resolve() {
                discardAbility.setChoiceCommand(runtime);
                discardAbility.setNeedsToChooseCard(true);
                game.getStack().add(discardAbility);
            }
        };
        card.clearSpellAbility();
        card.addSpellAbility(spell);
        return card;
    }
}
