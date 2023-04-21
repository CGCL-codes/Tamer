package mw.server.card.gold.rw;

import mw.mtgforge.Command;
import mw.server.GameManager;
import mw.server.list.CardList;
import mw.server.model.Ability;
import mw.server.model.Card;
import mw.server.model.MagicWarsModel;
import mw.server.model.SpellAbility;
import mw.server.model.MagicWarsModel.PhaseName;

@SuppressWarnings("serial")
public class AjaniVengeant {

    public static Card getCard(final GameManager game, final Card card) {
        Command dealtDamageCommand = new Command() {

            public void execute() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.subtractLoyaltyCounter(c.getLatestDealtDamage());
                    game.getManager().checkStateEffects();
                }
            }
        };
        card.setDealtDamageCommand(dealtDamageCommand);
        card.addSpellAbility(getAbility1(game, card));
        card.addSpellAbility(getAbility2(game, card));
        card.addSpellAbility(getAbility3(game, card));
        return card;
    }

    protected static SpellAbility getAbility1(final GameManager game, final Card card) {
        final SpellAbility ability1 = new Ability(card, "0") {

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.addLoyaltyCounter(1);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                Card target = getTargetCards().get(0);
                if (game.getBattlefield().isCardInPlay(target.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(target.getTableID());
                    c.addAspect(MagicWarsModel.ASPECT_DOESNT_UNTAP_ON_NEXT_TURN);
                }
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return ((phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != game.getManager().getTurnNumber() && game.getStack().size() == 0 && super.canPlay());
            }
        };
        ability1.setNeedsTargetPermanent(true);
        ability1.setDescription("+1: Target permanent doesn't untap during its controller's next untap step.");
        return ability1;
    }

    protected static SpellAbility getAbility2(final GameManager game, final Card card) {
        final SpellAbility ability2 = new Ability(card, "0") {

            int damage = 3;

            int gain = 3;

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.subtractLoyaltyCounter(2);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                if (getTargetCard() != null) {
                    game.addDamage(getTargetCard().getTableID(), damage, card);
                } else {
                    game.dealDamageToThePlayer(getTargetPlayerID(), damage, card);
                }
                game.getPlayerById(card.getControllerID()).gainLife(gain);
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return ((phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != game.getManager().getTurnNumber() && super.canPlay() && game.getStack().size() == 0 && card.getLoyaltyCounters() > 1);
            }
        };
        ability2.setDescription("-2: Ajani Vengeant deals 3 damage to target creature or player and you gain 3 life.");
        ability2.setStackDescription(card + ": deals 3 damage to target creature or player and you gain 3 life.");
        ability2.setNeedsTargetCreatureOrPlayer(true);
        return ability2;
    }

    protected static SpellAbility getAbility3(final GameManager game, final Card card) {
        final SpellAbility ability3 = new Ability(card, "0") {

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.subtractLoyaltyCounter(7);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                CardList lands = game.getBattlefield().getPermanentList(getTargetPlayerID());
                lands = lands.getType("Land");
                for (int i = 0; i < lands.size(); i++) {
                    if (game.getBattlefield().isCardInPlay(lands.get(i).getTableID())) {
                        Card c = game.getBattlefield().getPermanent(lands.get(i).getTableID());
                        game.getManager().destroy(c);
                    }
                }
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return (phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != game.getManager().getTurnNumber() && game.getStack().size() == 0 && card.getLoyaltyCounters() > 6 && super.canPlay();
            }
        };
        ability3.setNeedsTargetPlayer(true);
        ability3.setDescription("-7: Destroy all lands target player controls.");
        return ability3;
    }
}
