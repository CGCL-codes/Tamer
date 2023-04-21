package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class GrizzlyBear extends L2Transformation {

    private static final int[] SKILLS = { 5491, 619 };

    public GrizzlyBear() {
        super(320, 21, 40);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 320 || getPlayer().isCursedWeaponEquipped()) return;
        transformedSkills();
    }

    public void transformedSkills() {
        getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new GrizzlyBear());
    }
}
