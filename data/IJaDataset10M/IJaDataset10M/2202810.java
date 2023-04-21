package mw.server.card.gold.ur;

import java.util.Observable;
import java.util.Observer;
import mw.server.model.Ability;
import mw.server.model.Ability_Activated;
import mw.server.model.Card;
import mw.server.model.SpellAbility;
import mw.server.model.cost.ManaCost;
import mw.mtgforge.Command;
import mw.server.GameManager;

public class SpellboundDragon {

    @SuppressWarnings("serial")
    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility discardAbility = new Ability(card, "0") {

            public void resolve() {
                if (getTargetCard() != null) {
                    int pid = getTargetPlayerID();
                    game.discardCard(pid, getTargetCard());
                    final ManaCost convertedManaCost = new ManaCost(getTargetCard().getManaCost());
                    final int iConvertedManaCost = convertedManaCost.getConverted();
                    card.addAttack(iConvertedManaCost);
                    final Command untilEOT = new Command() {

                        public void execute() {
                            card.subAttack(iConvertedManaCost);
                        }
                    };
                    this.setCommand(untilEOT);
                    game.getEndOfTurn().addUntil(getCommand());
                }
            }
        };
        final Ability_Activated ability = new Ability_Activated(card) {

            public void resolve() {
                int cid = card.getControllerID();
                game.getPlayerById(cid).drawCard();
                discardAbility.setNeedsDiscardCard(true);
                discardAbility.setTargetPlayerID(cid);
                discardAbility.setInvisible(true);
                game.getStack().add(discardAbility);
            }
        };
        ability.setStackDescription(card + ": Whenever " + card + " attacks, draw a card," + " then discard a card. Spellbound Dragon gets +X/+0 until end of turn," + " where X is the discarded card's converted mana cost.");
        final Observer creatureAttacksObserver = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg instanceof Card) {
                    Card c = (Card) arg;
                    if (c.getTableID() == card.getTableID()) {
                        game.getStack().add(ability);
                    }
                }
            }
        };
        final Command intoPlay = new Command() {

            public void execute() {
                game.getEventManager().getCreatureAttacksEvent().addObserver(creatureAttacksObserver);
            }
        };
        card.setEntersTheBattlefieldCommand(intoPlay);
        final Command leavesPlayCommand = new Command() {

            public void execute() {
                game.getEventManager().getCreatureAttacksEvent().deleteObserver(creatureAttacksObserver);
            }
        };
        card.setLeavesTheBattlefieldCommand(leavesPlayCommand);
        return card;
    }
}
