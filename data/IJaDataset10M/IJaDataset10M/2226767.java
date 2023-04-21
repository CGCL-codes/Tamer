package gameserver.skill.effect;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.skill.model.ProvokeTarget;
import gameserver.skill.model.SkillTemplate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import commons.utils.Rnd;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvokerEffect")
public class ProvokerEffect extends ShieldEffect {

    @XmlAttribute(name = "provoke_target")
    protected ProvokeTarget provokeTarget;

    @XmlAttribute(name = "skill_id")
    protected int skillId;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void startEffect(Effect effect) {
        ActionObserver observer = null;
        final Creature effector = effect.getEffector();
        if (attacktype == null) return;
        switch(attacktype) {
            case ATTACK:
                observer = new ActionObserver(ObserverType.ATTACK) {

                    @Override
                    public void attack(Creature creature) {
                        if (Rnd.get(0, 100) <= probability) {
                            Creature target = getProvokeTarget(provokeTarget, effector, creature);
                            createProvokedEffect(effector, target);
                        }
                    }
                };
                break;
            case ATTACKED:
                observer = new ActionObserver(ObserverType.ATTACKED) {

                    @Override
                    public void attacked(Creature creature) {
                        if (Rnd.get(0, 100) <= probability) {
                            Creature target = getProvokeTarget(provokeTarget, effector, creature);
                            createProvokedEffect(effector, target);
                        }
                    }
                };
                break;
            case MAGICAL_SKILL:
                observer = new ActionObserver(ObserverType.HITTED) {

                    @Override
                    public void hitted(Creature creature, DamageType type) {
                        if (type == DamageType.MAGICAL) {
                            if (Rnd.get(0, 100) <= probability) {
                                Creature target = getProvokeTarget(provokeTarget, effector, creature);
                                createProvokedEffect(effector, target);
                            }
                        }
                    }
                };
                break;
            case PHYSICAL_SKILL:
                observer = new ActionObserver(ObserverType.HITTED) {

                    @Override
                    public void hitted(Creature creature, DamageType type) {
                        if (type == DamageType.PHYSICAL) {
                            if (Rnd.get(0, 100) <= probability) {
                                Creature target = getProvokeTarget(provokeTarget, effector, creature);
                                createProvokedEffect(effector, target);
                            }
                        }
                    }
                };
                break;
        }
        if (observer == null) return;
        effect.setActionObserver(observer, position);
        effect.getEffected().getObserveController().addObserver(observer);
    }

    /**
	 * 
	 * @param effector
	 * @param target
	 */
    private void createProvokedEffect(final Creature effector, Creature target) {
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
        Effect e = new Effect(effector, target, template, template.getLvl(), template.getEffectsDuration());
        e.initialize();
        e.applyEffect();
    }

    /**
	 * 
	 * @param provokeTarget
	 * @param effector
	 * @param target
	 * @return
	 */
    private Creature getProvokeTarget(ProvokeTarget provokeTarget, Creature effector, Creature target) {
        switch(provokeTarget) {
            case ME:
                return effector;
            case OPPONENT:
                return target;
        }
        throw new IllegalArgumentException("Provoker target is invalid " + provokeTarget);
    }

    @Override
    public void endEffect(Effect effect) {
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null) effect.getEffected().getObserveController().removeObserver(observer);
    }
}
