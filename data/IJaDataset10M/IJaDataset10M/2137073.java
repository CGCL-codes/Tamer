package gameserver.model.gameobjects.stats.id;

import gameserver.model.gameobjects.stats.StatEffectType;

public class SkillEffectId extends StatEffectId {

    private int effectId;

    private int effectOrder;

    private SkillEffectId(int skillId, int effectId, int effectOrder) {
        super(skillId, StatEffectType.SKILL_EFFECT);
        this.effectId = effectId;
        this.effectOrder = effectOrder;
    }

    public static SkillEffectId getInstance(int skillId, int effectId, int effectOrder) {
        return new SkillEffectId(skillId, effectId, effectOrder);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = super.equals(o);
        result = (result) && (o != null);
        result = (result) && (o instanceof SkillEffectId);
        result = (result) && (((SkillEffectId) o).effectId == effectId);
        result = (result) && (((SkillEffectId) o).effectOrder == effectOrder);
        return result;
    }

    @Override
    public int compareTo(StatEffectId o) {
        int result = 0;
        if (o == null) {
            result = id;
        } else {
            result = type.getValue() - o.type.getValue();
            if (result == 0) {
                if (o instanceof SkillEffectId) {
                    result = effectId - ((SkillEffectId) o).effectId;
                    if (result == 0) result = effectOrder - ((SkillEffectId) o).effectOrder;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final String str = super.toString() + ",effectId:" + effectId + ",effectOrder:" + effectOrder;
        return str;
    }

    /**
	 * @return the effectId
	 */
    public int getEffectId() {
        return effectId;
    }

    /**
	 * @return the effectOrder
	 */
    public int getEffectOrder() {
        return effectOrder;
    }
}
