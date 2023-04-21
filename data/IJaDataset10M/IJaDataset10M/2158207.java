package nakayo.gameserver.skillengine.effect;

import nakayo.gameserver.controllers.movement.ActionObserver;
import nakayo.gameserver.controllers.movement.ActionObserver.ObserverType;
import nakayo.gameserver.model.gameobjects.Item;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.model.gameobjects.stats.id.SkillEffectId;
import nakayo.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import nakayo.gameserver.model.templates.item.WeaponType;
import nakayo.gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.TreeSet;

/**
 * @author ATracer
 *         <p/>
 *         The code here is duplicated of WeaponStatboost
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeaponStatupEffect")
public class WeaponStatupEffect extends BufEffect {

    @XmlAttribute(name = "weapon")
    private WeaponType weaponType;

    @Override
    public void startEffect(final Effect effect) {
        if (!(effect.getEffector() instanceof Player)) return;
        final Player effected = (Player) effect.getEffected();
        final SkillEffectId skillEffectId = getSkillEffectId(effect);
        final TreeSet<StatModifier> stats = getModifiers(effect);
        if (effected.getEquipment().isWeaponEquipped(weaponType)) effected.getGameStats().addModifiers(skillEffectId, stats);
        ActionObserver aObserver = new ActionObserver(ObserverType.EQUIP) {

            @Override
            public void equip(Item item, Player owner) {
                if (item.getItemTemplate().getWeaponType() == weaponType) effected.getGameStats().addModifiers(skillEffectId, stats);
            }

            @Override
            public void unequip(Item item, Player owner) {
                if (item.getItemTemplate().getWeaponType() == weaponType) effected.getGameStats().endEffect(skillEffectId);
            }
        };
        effected.getObserveController().addEquipObserver(aObserver);
        effect.setActionObserver(aObserver, position);
    }

    @Override
    public void endEffect(Effect effect) {
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null) effect.getEffected().getObserveController().removeEquipObserver(observer);
        final SkillEffectId skillEffectId = getSkillEffectId(effect);
        if (effect.getEffected().getGameStats().effectAlreadyAdded(skillEffectId)) effect.getEffected().getGameStats().endEffect(skillEffectId);
    }

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }
}
