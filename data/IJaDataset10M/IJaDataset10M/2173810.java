package skillpurchase;

import java.util.Vector;
import skillpurchase.SkillPrerequisite.LOGIC;

public class PrerequisiteBuilder {

    private Vector<SkillTree> internal;

    private int use;

    private LOGIC operand;

    /**
	 * creates a new, empty prerequisite builder
	 */
    public PrerequisiteBuilder() {
        use = 0;
        operand = LOGIC.AND;
        internal = new Vector<SkillTree>();
    }

    /**
	 * @param e SkillTree object to be added (could be a skill, or a nested prerequisite!)
	 */
    public void addPreq(SkillTree e) {
        if (internal.contains(e)) return;
        internal.add(e);
        use++;
    }

    public void setLogic(LOGIC op) {
        operand = op;
    }

    /**
	 * @return an immutable prerequisite object that represents the current state of this prerequisite builder
	 */
    public SkillTree toPrerequisite() {
        if (use == 0) return null;
        if (use == 1) return internal.get(0);
        SkillPrerequisite sp = new SkillPrerequisite(internal, operand);
        for (int jj = 0; jj < internal.size(); jj++) {
            try {
                ((SkillBuilder) (internal.get(jj))).prereqRef(sp);
            } catch (ClassCastException e) {
            }
        }
        return sp;
    }

    public SkillTree getfirst() {
        if (internal.size() > 0) return internal.get(0);
        return null;
    }

    public int getSize() {
        return use;
    }
}
