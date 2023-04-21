package alice.tuprolog;

/**
 * @author Alex Benini
 */
public class StateGoalEvaluation extends State {

    public StateGoalEvaluation(EngineManager c) {
        this.c = c;
        stateName = "Eval";
    }

    void doJob(Engine e) {
        if (e.currentContext.currentGoal.isPrimitive()) {
            PrimitiveInfo primitive = e.currentContext.currentGoal.getPrimitive();
            try {
                e.nextState = (primitive.evalAsPredicate(e.currentContext.currentGoal)) ? c.GOAL_SELECTION : c.BACKTRACK;
            } catch (HaltException he) {
                e.nextState = c.END_HALT;
            } catch (Throwable t) {
                t.printStackTrace();
                e.nextState = c.END_HALT;
            }
            e.nDemoSteps++;
        } else e.nextState = c.RULE_SELECTION;
    }
}
