package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class DivineKnight extends L2Transformation {

    private static final int[] SKILLS = { 680, 681, 682, 683, 684, 685, 795, 796, 5491, 619 };

    public DivineKnight() {
        super(252, 16, 30);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 252 || getPlayer().isCursedWeaponEquipped()) return;
        transformedSkills();
    }

    public void transformedSkills() {
        getPlayer().addSkill(SkillTable.getInstance().getInfo(680, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(681, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(682, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(683, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(684, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(685, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(795, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(796, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(680, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(681, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(682, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(683, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(684, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(685, 1), false, false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(795, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(796, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new DivineKnight());
    }
}
