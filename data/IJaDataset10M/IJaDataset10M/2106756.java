package mw.server.card.black;

import mw.mtgforge.Constant;
import mw.server.card.ability.factory.SpellCreateTokenFactory;
import mw.server.model.Ability_Tap;
import mw.server.model.Card;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class KalitasBloodchiefofGhet {

    public static Card getCard(final GameManager game, final Card card) {
        final Ability_Tap ability = new Ability_Tap(card, "B B B") {

            public void resolve() {
                Card target = getTargetCard();
                if (game.getBattlefield().isCardInPlay(target)) {
                    int att = target.getAttack();
                    int def = target.getDefense();
                    if (game.getManager().destroyTarget(target, card)) {
                        Card token = SpellCreateTokenFactory.createToken(card, "Vampire", "Creature&Vampire", "no", Constant.Color.Black, att, def);
                        game.getBattlefield().addPermanent(token);
                    }
                }
            }
        };
        ability.setNeedsTargetCreature(true);
        card.addSpellAbility(ability);
        return card;
    }
}
