package mw.server.card.gold.ub;

import mw.server.GameManager;
import mw.server.model.Card;
import mw.server.model.Spell;
import mw.server.model.SpellAbility;
import mw.server.model.MagicWarsModel.GameZone;

@SuppressWarnings("serial")
public class DenyReality {

    public static Card getCard(final GameManager game, final Card card) {
        final SpellAbility spell = new Spell(card) {

            public void resolve() {
                game.moveToZone(GameZone.Battlefield, GameZone.Hand, getTargetCard());
            }
        };
        spell.setNeedsTargetPermanent(true);
        card.clearSpellAbility();
        card.addSpellAbility(spell);
        return card;
    }
}
