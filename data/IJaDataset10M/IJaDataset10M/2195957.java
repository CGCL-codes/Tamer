package mw.server.card.gold.ugw;

import mw.server.list.CardList;
import mw.server.model.Card;
import mw.server.model.Spell;
import mw.server.model.SpellAbility;
import mw.mtgforge.Constant;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class FlurryofWings {

    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility spell = new Spell(card) {

            public void resolve() {
                CardList permanents = game.getBattlefield().getAllPermanents();
                permanents = permanents.getType("Creature");
                int attackingCreatures = 0;
                for (Card c : permanents) {
                    if (c.isAttacking()) {
                        attackingCreatures++;
                    }
                }
                int aid = card.getControllerID();
                for (int i = 1; i <= attackingCreatures; i++) {
                    Card c = new Card();
                    c.setOwner(aid);
                    c.setController(aid);
                    c.setName("Bird Soldier");
                    c.setManaCost("0");
                    c.setToken(true);
                    c.setColor(Constant.Color.White);
                    c.setAttack(1);
                    c.setDefense(1);
                    c.addType("Creature");
                    c.addType("Bird");
                    c.addType("Soldier");
                    c.addKeyword("Flying");
                    game.getBattlefield().addPermanent(c);
                }
            }
        };
        card.clearSpellAbility();
        card.addSpellAbility(spell);
        return card;
    }
}
