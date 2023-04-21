package fr.fg.server.core.battle.abilities;

import fr.fg.server.core.battle.BaseAbility;
import fr.fg.server.core.battle.event.BattleEvent.Priority;
import fr.fg.server.core.battle.event.BattleEvent.Type;
import fr.fg.server.core.battle.event.AfterShotEvent;
import fr.fg.server.core.battle.event.BattleEventManager;
import fr.fg.server.core.battle.event.BeforeShotEvent;
import fr.fg.server.core.battle.event.DamageResolutionEvent;
import fr.fg.server.core.battle.util.BattleContext;
import fr.fg.server.core.battle.util.BattleSlot;
import fr.fg.server.data.Ability;
import fr.fg.server.data.ReportDamage;

public class SublimationAbility extends BaseAbility {

    @Override
    public void registerEvents(BattleEventManager manager) {
        manager.registerEvent(this, Type.DAMAGE_RESOLUTION, Priority.HIGH);
        manager.registerEvent(this, Type.BEFORE_SHOT);
        manager.registerEvent(this, Type.AFTER_SHOT);
    }

    @Override
    public void onBeforeShot(BattleContext context, BeforeShotEvent event) {
        if (context.isSimulation()) return;
        BattleSlot activeSlot = context.getActiveSlot();
        activeSlot.getState().removeCustomBuff(Ability.TYPE_SUBLIMATION);
        if (activeSlot.hasAbility(Ability.TYPE_SUBLIMATION)) {
            Ability sublimation = activeSlot.getAbility(Ability.TYPE_SUBLIMATION);
            if (Math.random() < sublimation.getSublimationChances()) {
                activeSlot.getState().setCustomBuff(Ability.TYPE_SUBLIMATION);
                event.getReportDamage().addModifier(ReportDamage.SUBLIMATION);
                context.getLog().append("Sublimation!\n");
            }
        }
    }

    @Override
    public void onAfterShot(BattleContext context, AfterShotEvent event) {
        if (context.isSimulation()) return;
        BattleSlot activeSlot = context.getActiveSlot();
        activeSlot.getState().removeCustomBuff(Ability.TYPE_SUBLIMATION);
    }

    @Override
    public void onDamageResolution(BattleContext context, DamageResolutionEvent event) {
        if (context.isSimulation()) return;
        BattleSlot activeSlot = context.getActiveSlot();
        if (activeSlot.getState().hasCustomBuff(Ability.TYPE_SUBLIMATION)) {
            event.setProtectionUpperLimit(0);
        }
    }
}
