package mw.server.card.red;

import mw.server.list.CardList;
import mw.server.model.Ability_Tap;
import mw.server.model.Card;
import mw.server.GameManager;

@SuppressWarnings("serial")
public class GrimLavamancer {

    public static Card getCard(final GameManager game, final Card card) {
        final Ability_Tap ability = new Ability_Tap(card, "R") {

            int damage = 2;

            public void resolve() {
                if (getTargetCard() != null) {
                    int id = getTargetCard().getTableID();
                    if (game.getBattlefield().isCardInPlay(id)) {
                        game.addDamage(id, damage, card);
                    }
                } else {
                    game.dealDamageToThePlayer(getTargetPlayerID(), damage, card);
                }
                int pid = getSourceCard().getControllerID();
                CardList list = game.getGraveyard().getPersonalCards(pid);
                if (list.size() > 1) {
                    game.getGraveyard().remove(list.get(1));
                }
                if (list.size() > 0) {
                    game.getGraveyard().remove(list.get(0));
                }
            }

            public boolean canPlay() {
                int pid = getSourceCard().getControllerID();
                int count = game.getGraveyard().getPersonalCards(pid).size();
                return super.canPlay() && count > 1;
            }
        };
        ability.setDescription("tap, R, remove 2 cards in your graveyard from the game: " + card.getName() + " deals 2 damage to target creature or player.");
        ability.setStackDescription("tap, R, remove 2 cards in your graveyard from the game: " + card.getName() + " deals 2 damage to target creature or player.");
        ability.setNeedsTargetCreatureOrPlayer(true);
        card.addSpellAbility(ability);
        return card;
    }
}
