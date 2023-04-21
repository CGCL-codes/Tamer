package fr.fg.server.core.battle.abilities;

import fr.fg.server.core.battle.BaseAbility;
import fr.fg.server.core.battle.event.BattleEvent.Type;
import fr.fg.server.core.battle.event.AbilityActivationEvent;
import fr.fg.server.core.battle.event.BattleEventManager;
import fr.fg.server.core.battle.event.BeforeActionEvent;
import fr.fg.server.core.battle.util.BattleContext;
import fr.fg.server.core.battle.util.BattleSlot;
import fr.fg.server.data.Ability;

public class IncohesionAbility extends BaseAbility {

    @Override
    public void registerEvents(BattleEventManager manager) {
        manager.registerEvent(this, Type.BEFORE_ACTION);
        manager.registerEvent(this, Type.ABILITY_ACTIVATION);
    }

    @Override
    public void onBeforeAction(BattleContext context, BeforeActionEvent event) {
        for (BattleSlot slot : context.getActiveFleet().getSlots()) updateSlot(slot, context.getCurrentRound());
        for (BattleSlot slot : context.getPassiveFleet().getSlots()) updateSlot(slot, context.getCurrentRound());
    }

    @Override
    public void onAbilityActivation(BattleContext context, AbilityActivationEvent event) {
        if (event.getAbility().getType() == Ability.TYPE_RIFT) {
            for (BattleSlot slot : context.getActiveFleet().getSlots()) revertSlot(slot, context.getCurrentRound());
            for (BattleSlot slot : context.getPassiveFleet().getSlots()) revertSlot(slot, context.getCurrentRound());
        }
    }

    private void updateSlot(BattleSlot slot, int currentRound) {
        if (slot.getCount() > 0 && slot.hasAbility(Ability.TYPE_INCOHESION)) {
            Ability incohesion = slot.getAbility(Ability.TYPE_INCOHESION);
            slot.getState().multiplyDamage(currentRound % 2 == 0 ? incohesion.getIncohesionEvenRoundDamageModifier() : incohesion.getIncohesionOddRoundDamageModifier());
            slot.getState().addProtectionModifier(currentRound % 2 == 0 ? incohesion.getIncohesionEvenRoundProtectionModifier() : incohesion.getIncohesionOddRoundProtectionModifier());
            if (currentRound > 1) {
                slot.getState().multiplyDamage(currentRound % 2 == 1 ? 1 / incohesion.getIncohesionEvenRoundDamageModifier() : 1 / incohesion.getIncohesionOddRoundDamageModifier());
                slot.getState().addProtectionModifier(currentRound % 2 == 1 ? 1 / incohesion.getIncohesionEvenRoundProtectionModifier() : 1 / incohesion.getIncohesionOddRoundProtectionModifier());
            }
        }
    }

    private void revertSlot(BattleSlot slot, int currentRound) {
        if (slot.getCount() > 0 && slot.hasAbility(Ability.TYPE_INCOHESION)) {
            Ability incohesion = slot.getAbility(Ability.TYPE_INCOHESION);
            slot.getState().multiplyDamage(currentRound % 2 == 0 ? incohesion.getIncohesionEvenRoundDamageModifier() : incohesion.getIncohesionOddRoundDamageModifier());
            slot.getState().addProtectionModifier(currentRound % 2 == 0 ? incohesion.getIncohesionEvenRoundProtectionModifier() : incohesion.getIncohesionOddRoundProtectionModifier());
        }
    }
}
