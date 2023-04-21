package mw.server.card.red;

import mw.server.model.Ability_Tap;
import mw.server.model.Card;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class KamahlPitFighter {

    public static Card getCard(final GameManager game, final Card card) {
        final Ability_Tap ability = new Ability_Tap(card) {

            int damage = 3;

            public void resolve() {
                if (getTargetCard() != null) {
                    game.addDamage(getTargetCard().getTableID(), damage, card);
                } else {
                    game.dealDamageToThePlayer(getTargetPlayerID(), damage, card);
                }
            }
        };
        ability.setDescription("tap: " + card.getName() + " deals 3 damage to target creature or player.");
        ability.setNeedsTargetCreatureOrPlayer(true);
        card.addSpellAbility(ability);
        return card;
    }
}
