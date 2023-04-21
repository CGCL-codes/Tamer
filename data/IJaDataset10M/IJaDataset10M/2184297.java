package mw.server.card;

import javax.script.SimpleBindings;
import mw.server.model.Card;
import mw.server.model.ability.CanPlayAsSorcery;
import mw.server.model.cost.LoyaltyCost;
import mw.server.model.cost.PayLifeCost;

/**
 * This class replaces ScriptEngine SimpleBindings that is widely used in card scripts.
 * 
 * Values like "$target" are changed to "bindings.target" in runtime. 
 * That allows not to touch scripted cards while moving to groovy compilation and keep using short bindings.
 */
public class GroovyScriptBindingsImpl implements GroovyScriptBindings {

    private SimpleBindings bindings = new SimpleBindings();

    public SimpleBindings getBindingsRef() {
        return this.bindings;
    }

    public void setProperty(String property, Object value) {
        bindings.put(property, value);
    }

    public Object getTarget() {
        return bindings.get("$target");
    }

    public Object getChosen() {
        return bindings.get("$chosen");
    }

    public Object getTargetPlayer() {
        return bindings.get("$targetPlayer");
    }

    public Object getWasKicked() {
        return bindings.get("$wasKicked");
    }

    public Object getFirstKicker() {
        return bindings.get("$firstKicker");
    }

    public Object getSecondKicker() {
        return bindings.get("$secondKicker");
    }

    public Object getMultikicker() {
        return bindings.get("$multikicker");
    }

    public Object getTargets() {
        return bindings.get("$targets");
    }

    public Object getChosenCards() {
        return bindings.get("$chosenCards");
    }

    public Object getTargetAbility() {
        return bindings.get("$targetAbility");
    }

    public Object getTargetSpell() {
        return bindings.get("$targetSpell");
    }

    public Object getTargetAbilities() {
        return bindings.get("$targetAbilities");
    }

    public Object getTargetSpells() {
        return bindings.get("$targetSpells");
    }

    public Object getxValue() {
        return bindings.get("$xValue");
    }

    public Object getEquipped() {
        return bindings.get("$equipped");
    }

    public Object getAllCreatures() {
        return bindings.get("$allCreatures");
    }

    public Object getMyCreatures() {
        return bindings.get("$myCreatures");
    }

    public Object getAllPermanents() {
        return bindings.get("$allPermanents");
    }

    public Object getMyPermanents() {
        return bindings.get("$myPermanents");
    }

    public Object getOppPermanents() {
        return bindings.get("$oppPermanents");
    }

    public Object getOppCreatures() {
        return bindings.get("$oppCreatures");
    }

    public Object getThis() {
        return bindings.get("$this");
    }

    public Object getOppLife() {
        return bindings.get("$oppLife");
    }

    public Object getPlayer() {
        return bindings.get("$player");
    }

    public Object getController() {
        return bindings.get("$controller");
    }

    public Object getOpponent() {
        return bindings.get("$opponent");
    }

    public Object getPlayerId() {
        return bindings.get("$playerId");
    }

    public Object getOpponentId() {
        return bindings.get("$opponentId");
    }

    public Object getActivePlayerId() {
        return bindings.get("$activePlayerId");
    }

    public Object getTurnOwnerId() {
        return bindings.get("$turnOwnerId");
    }

    public Object getParam() {
        return bindings.get("$param");
    }

    public Object getPermanent() {
        return bindings.get("$permanent");
    }

    public Object getCard() {
        return bindings.get("$card");
    }

    public Object getSource() {
        return bindings.get("$source");
    }

    public Object getDamage() {
        return bindings.get("$damage");
    }

    public Object getSa() {
        return bindings.get("$sa");
    }

    public Object getMana() {
        return bindings.get("$mana");
    }

    public Object getAllPlayers() {
        return bindings.get("$allPlayers");
    }

    public Object loyaltyCost(int toPay) {
        return new LoyaltyCost(toPay);
    }

    public Object loyaltyCost(String cost) {
        return new LoyaltyCost(cost);
    }

    public Object lifeCost(int toPay) {
        return new PayLifeCost(toPay);
    }

    public Object getEnchanted() {
        Object obj = getThis();
        if (obj != null && obj instanceof Card) {
            Card enchanted = ((Card) obj).getSpellAbility()[0].getTargetCard();
            return enchanted;
        }
        return null;
    }

    public Object getAttackers() {
        return bindings.get("$attackers");
    }

    public Object getBlockers() {
        return bindings.get("$blockers");
    }

    public Object getPlayAsSorcery() {
        Object obj = getThis();
        if (obj != null && obj instanceof Card) {
            return new CanPlayAsSorcery((Card) obj);
        }
        return null;
    }

    public Object getDamagedPlayer() {
        return bindings.get("$damagedPlayer");
    }
}
