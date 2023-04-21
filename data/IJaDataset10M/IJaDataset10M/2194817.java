package mw.server.card.gold.ubr;

import mw.server.list.CardList;
import mw.server.model.Card;
import mw.server.model.Spell;
import mw.server.model.SpellAbility;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class DrasticRevelation {

    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility spell = new Spell(card) {

            public void resolve() {
                int aid = game.getManager().getPriorityPID();
                CardList hand = game.getPlayerById(aid).getHandList();
                for (Card c : hand) {
                    game.putIntoGraveyardFromHand(aid, c);
                }
                for (int i = 1; i <= 7; i++) {
                    game.getPlayerById(aid).drawCard();
                }
                for (int i = 0; i <= 2; i++) {
                    int randInt = (int) (Math.random() * (7 - i));
                    game.putIntoGraveyardFromHand(aid, game.getPlayerById(aid).getHandList().getCard(randInt));
                }
            }
        };
        spell.setStackDescription("Discard your hand. " + "Draw seven cards, then discard three cards at random.");
        card.clearSpellAbility();
        card.addSpellAbility(spell);
        return card;
    }
}
