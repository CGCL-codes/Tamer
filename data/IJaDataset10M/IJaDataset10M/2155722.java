package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class DivineEnchanter extends L2Transformation {

    private static final int[] SKILLS = { 704, 705, 706, 707, 708, 709, 5779, 619 };

    public DivineEnchanter() {
        super(257, 8, 18.25);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 257 || getPlayer().isCursedWeaponEquipped()) return;
        transformedSkills();
    }

    public void transformedSkills() {
        getPlayer().addSkill(SkillTable.getInstance().getInfo(704, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(705, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(706, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(707, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(708, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(709, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(704, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(705, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(706, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(707, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(708, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(709, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new DivineEnchanter());
    }
}
