package mw.server.card.gold.rg;

import mw.mtgforge.Command;
import mw.mtgforge.Constant;
import mw.server.GameManager;
import mw.server.list.CardList;
import mw.server.model.Ability;
import mw.server.model.Card;
import mw.server.model.SpellAbility;
import mw.server.model.MagicWarsModel.PhaseName;
import mw.server.model.effect.GlobalEffect;

@SuppressWarnings("serial")
public class SarkhanVol {

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
        card.setHybrid(true);
        return card;
    }

    protected static SpellAbility getAbility1(final GameManager game, final Card card) {
        final GlobalEffect sarkhanBoostEffect = new GlobalEffect() {

            public void applyEffect() {
                CardList creatures = game.getBattlefield().getPermanentList(card.getControllerID());
                creatures = creatures.getType("Creature");
                for (int j = 0; j < creatures.size(); j++) {
                    applyForNewPermanent(creatures.get(j));
                }
            }

            public void applyForNewPermanent(Card c) {
                if (c.isCreature() && c.getControllerID() == card.getControllerID()) {
                    c.addAttack(1);
                    c.addDefense(1);
                    c.addKeyword("Haste");
                }
            }

            public void discardFromPermanent(Card c) {
                if (c.isCreature() && c.getControllerID() == card.getControllerID()) {
                    c.subAttack(1);
                    c.subDefense(1);
                    c.removeKeyword("Haste");
                }
            }

            public void discardEffect() {
                CardList creatures = game.getBattlefield().getPermanentList(card.getControllerID());
                creatures = creatures.getType("Creature");
                for (int j = 0; j < creatures.size(); j++) {
                    discardFromPermanent(creatures.get(j));
                }
            }

            public Card getEffectOwnerCard() {
                return card;
            }
        };
        final Command untilEOTRemoveEffect = new Command() {

            public void execute() {
                game.getManager().removeGlobalEffect(sarkhanBoostEffect);
            }
        };
        final SpellAbility ability1 = new Ability(card, "0") {

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.addLoyaltyCounter(1);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                game.getManager().addGlobalEffect(sarkhanBoostEffect);
                game.getEndOfTurn().addUntil(untilEOTRemoveEffect);
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return ((phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != game.getManager().getTurnNumber() && game.getStack().size() == 0 && super.canPlay());
            }
        };
        ability1.setDescription("+1: Creatures you control get +1/+1 and gain haste until end of turn.");
        ability1.setStackDescription(card + ": creatures you control get +1/+1 and gain haste until end of turn.");
        return ability1;
    }

    protected static SpellAbility getAbility2(final GameManager game, final Card card) {
        final Command untilEOT = new Command() {

            public void execute() {
                int tableID = card.getSpellAbility()[2].getTargetCard().getTableID();
                if (game.getBattlefield().isCardInPlay(tableID)) {
                    Card c = game.getBattlefield().getPermanent(tableID);
                    game.changePermanentControllerTo(c.getOwnerID(), c);
                    c.removeKeyword("Haste");
                }
            }
        };
        final SpellAbility ability2 = new Ability(card, "0") {

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.subtractLoyaltyCounter(2);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                if (getTargetCard() != null) {
                    Card target = getTargetCard();
                    if (game.getBattlefield().isCardInPlay(target.getTableID())) {
                        Card c = game.getBattlefield().getPermanent(target.getTableID());
                        game.changePermanentControllerTo(card.getControllerID(), c);
                        c.untap();
                        c.addKeyword("Haste");
                        game.getEndOfTurn().addUntil(untilEOT);
                    }
                }
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return ((phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != card.getGame().getTurnNumber() && super.canPlay() && game.getStack().size() == 0 && card.getLoyaltyCounters() > 1);
            }
        };
        ability2.setDescription("-2: Gain control of target creature until end of turn. Untap that creature. It gains haste until of turn.");
        ability2.setStackDescription(card + ": gain control of target creature until end of turn. Untap that creature. It gains haste until of turn.");
        ability2.setNeedsTargetCreature(true);
        ability2.setCommand(untilEOT);
        return ability2;
    }

    protected static SpellAbility getAbility3(final GameManager game, final Card card) {
        final SpellAbility ability3 = new Ability(card, "0") {

            public void resolve() {
                if (game.getBattlefield().isCardInPlay(card.getTableID())) {
                    Card c = game.getBattlefield().getPermanent(card.getTableID());
                    c.subtractLoyaltyCounter(6);
                    c.setAbilityLastPlayedTurn(game.getManager().getTurnNumber());
                }
                for (int i = 0; i < 5; i++) {
                    addDragonToken(this.getSourceCard().getControllerID());
                }
            }

            protected void addDragonToken(int aid) {
                Card c = new Card();
                c.setOwner(aid);
                c.setController(aid);
                c.setName("Dragon");
                c.setManaCost("0");
                c.setToken(true);
                c.setColor(Constant.Color.Red);
                c.addType("Creature");
                c.addType("Dragon");
                c.setAttack(4);
                c.setDefense(4);
                c.addKeyword("Flying");
                game.getBattlefield().addPermanent(c);
            }

            public boolean canPlay() {
                PhaseName phase = game.getManager().getCurrentPhaseName();
                return (phase.equals(PhaseName.main1) || phase.equals(PhaseName.main2)) && card.getAbilityLastPlayedTurn() != card.getGame().getTurnNumber() && game.getStack().size() == 0 && card.getLoyaltyCounters() > 5 && super.canPlay();
            }
        };
        ability3.setDescription("-6: Put five 4/4 red Dragon creature tokens with flying into play.");
        ability3.setStackDescription(card + ": put five 4/4 red Dragon creature tokens with flying into play.");
        return ability3;
    }
}
