package at.ofai.aaa.agent.jam;

import java.util.Iterator;
import at.ofai.aaa.agent.jam.types.ExpList;
import at.ofai.aaa.agent.jam.types.Expression;
import at.ofai.aaa.agent.jam.types.Value;

/**
 * An abstract superclass for appraisals of a fact (a WorldModelRelation).
 * @author Stefan Rank
 */
abstract class AbstractFactAppraisal implements Appraisal {

    private final WorldModelRelation relation;

    private final IntentionStructure intentionStructure;

    private final Interpreter interpreter;

    private final float copingPotential;

    private Goal copingGoal = null;

    AbstractFactAppraisal(final WorldModelRelation pRelation, final Interpreter pInterpreter) {
        this.interpreter = pInterpreter;
        this.intentionStructure = pInterpreter.getIntentionStructure();
        this.relation = pRelation;
        this.copingPotential = calcCopingPotential(pInterpreter.getPlanLibrary());
    }

    /**
     * ATTENTION: equals of AbstractFactAppraisal violates the hashCode-equals contract.
     * equals here only considers the type of Appraisal and the appraised relation (no intensity...),
     * do not use Appraisal as Map-keys!
     */
    public boolean equals(final Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return Relation.unify(((AbstractFactAppraisal) o).getRelation(), null, this.getRelation(), null);
    }

    /**
     * ATTENTION: hashCode is simply calling super.hashCode(), i.e. Object.hashCode().
     * This must be changed if Appraisals should be used as keys in maps.
     */
    public int hashCode() {
        return super.hashCode();
    }

    protected final String getOwnerName() {
        return this.interpreter.getName();
    }

    private float calcCopingPotential(final PlanTable pt) {
        return 1f;
    }

    public final void tryToCope() {
        final int numArgsToConsider = 3;
        final float utility = getUtility();
        ExpList explist = new ExpList();
        explist.add(Value.newValue(getImpulseExpression()));
        explist.add(Value.newValue(getIntensity()));
        Relation r = this.getRelation().getSubRelation(null);
        explist.add(Value.newValue(r.getName()));
        Iterator<Expression> iter = r.getArgs().iterator();
        for (int i = 0; i < numArgsToConsider; ++i) {
            if (iter.hasNext()) {
                explist.add(iter.next());
            } else {
                explist.add(Value.newValue(""));
            }
        }
        explist.add(Value.newValue(this.getRelation().getSuccessValue(null)));
        Relation goal = new Relation("tryToCope", explist);
        GoalAction ga = new PerformGoalAction(goal, Value.newValue(utility), (ExpList) null, (ExpList) null);
        this.copingGoal = this.intentionStructure.addUnique(ga);
    }

    public final void removeCoping() {
        if (this.copingGoal != null) {
            this.copingGoal.removeIntention(false);
            this.intentionStructure.removeGoal(this.copingGoal);
            this.copingGoal = null;
        }
    }

    protected float getImpulseThreshold() {
        return IMPULSE_THRESHOLD;
    }

    public final void postImpulse() {
        final float impulseUtility = getUtility() + IMPULSE_BONUS;
        if (impulseUtility > getImpulseThreshold()) {
            ExpList explist = new ExpList();
            explist.add(Value.newValue(getImpulseExpression()));
            explist.add(Value.newValue(getIntensity()));
            explist.add(Value.newValue(this));
            Relation goal = new Relation(Appraisal.IMPULSE_GOAL_STRING, explist);
            GoalAction ga = new PerformGoalAction(goal, Value.newValue(impulseUtility), (ExpList) null, (ExpList) null);
            this.intentionStructure.addUnique(ga);
        }
    }

    public boolean updateEffects() {
        WorldModelRelation r = this.getRelation();
        if (!r.isAsserted()) {
            r.advanceAge();
        }
        if (this.copingGoal != null) {
            float newUtility = this.getUtility();
            Goal g = this.intentionStructure.getToplevelGoals().getFirst();
            if (g == this.copingGoal && g.getSubgoal() != null) {
                return true;
            }
            if (newUtility > 1f) {
                this.copingGoal.getGoalAction().setUtility(Value.newValue(newUtility));
                return true;
            }
        }
        return false;
    }

    public float getIntensity() {
        return getRecency() * this.copingPotential;
    }

    private float getUtility() {
        return getIntensity() * UTILITY_FACTOR;
    }

    private float getRecency() {
        final int age = this.relation.getAge();
        if (age < OLD_WMR_AGE_THRESHOLD) {
            return 1f - (age / OLD_WMR_AGE_THRESHOLD);
        }
        return 0f;
    }

    public final WorldModelRelation getRelation() {
        return this.relation;
    }

    public final String verboseString() {
        String fullname = this.getClass().getName();
        StringBuffer sb = new StringBuffer(fullname.substring(fullname.lastIndexOf('.') + 1) + ": " + this.relation.simpleString(null));
        sb.append(this.valuesString());
        return sb.toString();
    }

    protected StringBuffer valuesString() {
        StringBuffer sb = new StringBuffer();
        sb.append("; recency: " + getRecency() + " copingPotential: " + this.copingPotential);
        return sb;
    }

    public String toString() {
        return this.verboseString();
    }
}
