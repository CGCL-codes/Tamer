package at.ofai.aaa.agent.jam;

import java.io.Serializable;
import at.ofai.aaa.agent.jam.types.Binding;

/**
 * A built-in JAM primitive action for modifying existing entries on the JAM world model.
 * @author Marc Huber
 * @author Jaeho Lee
 */
class UpdateAction extends WorldModelAction implements Serializable {

    private Relation newRelation;

    /** Constructor w/ relation to update in the World Model as an argument in addition to the interpreter. */
    UpdateAction(final Relation pOldRelation, final Relation pNewRelation, final WorldModelTable pWorldModel) {
        super(pOldRelation, pWorldModel);
        this.newRelation = pNewRelation;
    }

    public boolean isExecutableAction() {
        return true;
    }

    public Relation getOldRelation() {
        return this.relation;
    }

    public Relation getNewRelation() {
        return this.newRelation;
    }

    /** Update the relation on the World Model. */
    public Result execute(final Binding b, final Goal currentGoal) {
        this.worldModel.update(getOldRelation(), getNewRelation(), b);
        return Result.SUCCEEDED;
    }

    /** Output information to the stream in an in-line manner. */
    public String formattedString(final Binding b) {
        return "UPDATE " + "(" + getOldRelation().formattedString(b) + ") (" + getNewRelation().formattedString(b) + ");";
    }
}
