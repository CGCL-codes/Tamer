package mw.server.card.gold.ub;

import mw.mtgforge.Command;
import mw.server.ChoiceCommand;
import mw.server.GameManager;
import mw.server.list.CardList;
import mw.server.model.Ability;
import mw.server.model.Card;
import mw.server.model.Library;
import mw.server.model.SpellAbility;

@SuppressWarnings("serial")
public class ArchitectsofWill {

    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility threeCardsAbility = new Ability(card, "0") {

            public void resolve() {
                if (getTargetCards().size() != 3) {
                    return;
                }
                CardList list = getTargetCards();
                for (int i = 0; i < 3; i++) {
                    Card c = list.get(i);
                    if (c != null) {
                        int pid = card.getOwnerID();
                        game.removeCardFromLibrary(pid, c);
                    }
                }
                for (int i = 0; i < 3; i++) {
                    Card c = list.get(i);
                    if (c != null) {
                        int pid = card.getOwnerID();
                        game.putRemovedCardOnTop(pid, c);
                    }
                }
            }
        };
        final ChoiceCommand runtime = new ChoiceCommand() {

            public void execute() {
                int pid = getTargetPlayerID();
                Library library = game.getPlayerById(pid).getLibrary();
                CardList top3 = new CardList();
                for (int i = 0; i < 3 && i < library.size(); i++) {
                    top3.add(library.get(i));
                }
                setInputChoice(top3);
            }
        };
        final SpellAbility choiceAbility = new Ability(card, "0") {

            public void resolve() {
                threeCardsAbility.setChoiceCommand(runtime);
                threeCardsAbility.setNeedsToChooseCard(true);
                threeCardsAbility.setTargetCount(3);
                threeCardsAbility.setInvisible(true);
                runtime.setTargetPlayerID(getTargetPlayerID());
                game.getStack().add(threeCardsAbility);
            }
        };
        final Command comesIntoPlay = new Command() {

            public void execute() {
                choiceAbility.setChoiceCommand(runtime);
                choiceAbility.setNeedsTargetPlayer(true);
                game.getStack().add(choiceAbility);
            }
        };
        card.setEntersTheBattlefieldCommand(comesIntoPlay);
        return card;
    }
}
