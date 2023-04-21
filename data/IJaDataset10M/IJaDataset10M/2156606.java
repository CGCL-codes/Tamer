package mw.server.card.gold.ubr;

import java.util.Observable;
import java.util.Observer;
import mw.server.list.CardList;
import mw.server.list.CardListFilter;
import mw.server.model.Ability_Activated;
import mw.server.model.Card;
import mw.server.model.CounterType;
import mw.server.model.Spell;
import mw.server.model.SpellAbility;
import mw.mtgforge.Command;
import mw.server.ChoiceCommand;
import mw.server.GameManager;
import mw.server.pattern.CommandEx;

@SuppressWarnings("serial")
public class Thraximundar {

    public static Card getCard(final GameManager game, final Card card) {
        final ChoiceCommand runtime = new ChoiceCommand() {

            public void execute() {
                int aid = card.getControllerID();
                CardList creatures = game.getBattlefield().getPermanentList(aid);
                creatures = creatures.getType("Creature");
                creatures = creatures.filter(new CardListFilter() {

                    @Override
                    public boolean addCard(Card c) {
                        return c.getTableID() != card.getTableID();
                    }
                });
                setInputChoice(creatures);
            }
        };
        final CommandEx returnBackController = new CommandEx() {

            @Override
            public void execute() {
                card.setController((Integer) getParam());
            }
        };
        final SpellAbility sacrificeAbility = new Spell(card) {

            public void resolve() {
                if (getTargetCard() != null) {
                    game.getManager().sacrifice(getTargetCard());
                }
                returnBackController.execute();
            }
        };
        final SpellAbility ability = new Spell(card) {

            public void resolve() {
                int aid = card.getControllerID();
                int oppid = game.getOpponentID(aid);
                sacrificeAbility.setStackDescription(card + ": sacrifice a creature.");
                sacrificeAbility.setNeedsToChooseCard(true);
                sacrificeAbility.setChoiceCommand(runtime);
                sacrificeAbility.setTargetsAreOptional(false);
                card.setController(oppid);
                returnBackController.setParam(Integer.valueOf(aid));
                game.getStack().add(sacrificeAbility);
            }
        };
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
        final Ability_Activated putCounterAbility = new Ability_Activated(card) {

            public void resolve() {
                card.putCounter(CounterType.P1P1, 1);
            }
        };
        putCounterAbility.setYesNoQuestion(card + ": put a +1/+1 counter?");
        putCounterAbility.setStackDescription(card + ": put a +1/+1 counter.");
        final Observer sacrificeCreatureObserver = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                game.getStack().add(putCounterAbility);
            }
        };
        final Command intoPlay = new Command() {

            public void execute() {
                game.getEventManager().getCreatureAttacksEvent().addObserver(creatureAttacksObserver);
                game.getManager().addSacrificeCreatureObserver(sacrificeCreatureObserver);
            }
        };
        card.setEntersTheBattlefieldCommand(intoPlay);
        final Command leavesPlayCommand = new Command() {

            public void execute() {
                game.getEventManager().getCreatureAttacksEvent().deleteObserver(creatureAttacksObserver);
                game.getManager().removeSacrificeCreatureObserver(sacrificeCreatureObserver);
            }
        };
        card.setLeavesTheBattlefieldCommand(leavesPlayCommand);
        return card;
    }
}
