package org.uc3m.verbus.bpel2bpa;

import java.util.Iterator;
import java.util.List;
import org.uc3m.verbus.VerbusException;
import org.uc3m.verbus.bpa.Activity;
import org.uc3m.verbus.bpa.BPAProcess;
import org.uc3m.verbus.bpa.Entity;
import org.uc3m.verbus.bpa.ExprEqualityOp;
import org.uc3m.verbus.bpa.ExprOpAnd;
import org.uc3m.verbus.bpa.ExprOpNot;
import org.uc3m.verbus.bpa.ExprOpOr;
import org.uc3m.verbus.bpa.Transition;
import org.uc3m.verbus.bpa.VGoal;
import org.uc3m.verbus.bpa.VInvariant;
import org.uc3m.verbus.bpa.VPreRequirement;
import org.uc3m.verbus.bpa.util.BPAUtil;
import org.uc3m.verbus.bpel.model.BPWSProcess;

public class PropertyCreator {

    protected BPAProcess bpa;

    protected BPWSProcess bpel;

    protected BPAUtil util;

    protected BPELToBPAUtil b2bUtil;

    public PropertyCreator(BPWSProcess bpel, BPAProcess bpa) {
        this.bpa = bpa;
        this.bpel = bpel;
        util = new BPAUtil(bpa);
        b2bUtil = new BPELToBPAUtil(bpel, bpa);
    }

    /**
     * Create a "notRunningConcurrently" invariant: activities A and
     * B can not be concurrentlu running.
     *
     * Property: !(life1.state=running & life2.state=running)
     *
     * @param life1 Life-cycle entity of the first activity
     * @param life2 Life-cycle entity of the second activity
     * @return the invariant
     *
     */
    public VInvariant createNotRunningConcurrently(Entity life1, Entity life2) throws VerbusException {
        VInvariant inv = bpa.createVInvariant();
        ExprOpNot not = bpa.createExprOpNot();
        ExprOpAnd and = bpa.createExprOpAnd();
        inv.setInvariant(not);
        not.setOperand(and);
        and.addOperand(util.createEnumEquality(life1, "state", "running"));
        and.addOperand(util.createEnumEquality(life2, "state", "running"));
        inv.setName("notRunningConcurrently(" + life1.getName() + "," + life2.getName() + ")");
        return inv;
    }

    /**
     * Create a "runningAWhileB" invariant: activities (or process) A 
     * must be running while activity B is running.
     *
     * Property: lifeA.state=running | !lifeB.state=running
     *
     * @param lifeA Life-cycle entity of the activity A 
     * @param lifeB Life-cycle entity of the activity B
     * @return the invariant
     *
     */
    public VInvariant createRunningAWhileB(Entity lifeA, Entity lifeB) throws VerbusException {
        VInvariant inv = bpa.createVInvariant();
        ExprOpOr or = bpa.createExprOpOr();
        inv.setInvariant(or);
        or.addOperand(util.createEnumEquality(lifeA, "state", "running"));
        or.addOperand(util.getNegated(util.createEnumEquality(lifeB, "state", "running")));
        inv.setName("runningAWhileB(" + lifeA.getName() + "," + lifeB.getName() + ")");
        return inv;
    }

    /**
     * Create the "noneRunning" goal: no activity can be running
     *
     * Property: !life1.state=running & !life2.state=running &...
     *
     */
    public VGoal createNoneRunning() throws VerbusException {
        ExprOpAnd and = bpa.createExprOpAnd();
        List entities = bpa.getEntities();
        Iterator i = entities.iterator();
        while (i.hasNext()) {
            Entity ent = (Entity) i.next();
            if (b2bUtil.isLifeCycleBasic(ent) || b2bUtil.isLifeCycleGeneral(ent)) and.addOperand(util.getNegated(util.createEnumEquality(ent, "state", "running")));
        }
        if (and.getOperands().size() == 0) throw new VerbusException("No life-cycle entities found");
        VGoal goal = bpa.createVGoal();
        goal.setGoal(and);
        goal.setName("noneRunning");
        return goal;
    }

    /**
     * Generates the general requirements RG-1,...
     * 
     * @throws VerbusException if any error occurs
     */
    protected void generateGeneralRequirements() throws VerbusException {
        ExprOpOr or;
        VGoal rg1 = bpa.createVGoal();
        rg1.setComment("requirement general 1");
        rg1.setName("rg1");
        bpa.addGoal(rg1);
        Entity pLife = b2bUtil.getProcessLifeCycleEntity();
        or = bpa.createExprOpOr();
        or.addOperand(util.createEnumEquality(pLife, "state", "completed"));
        or.addOperand(util.createEnumEquality(pLife, "state", "cancelled"));
        or.addOperand(util.createEnumEquality(pLife, "state", "compensated"));
        rg1.setGoal(or);
        VGoal rg3 = bpa.createVGoal();
        rg3.setComment("requirement general 3");
        rg3.setName("rg3");
        bpa.addGoal(rg3);
        ExprOpAnd rg3Pred = bpa.createExprOpAnd();
        rg3.setGoal(rg3Pred);
        List entities = b2bUtil.getLifeCycleEntities();
        for (Iterator iter = entities.iterator(); iter.hasNext(); ) {
            Entity life = (Entity) iter.next();
            if (life != pLife) {
                or = bpa.createExprOpOr();
                or.addOperand(util.createEnumEquality(life, "state", "completed"));
                or.addOperand(util.createEnumEquality(life, "state", "not_started"));
                or.addOperand(util.createEnumEquality(life, "state", "cancelled"));
                if (b2bUtil.isLifeCycleGeneral(life)) {
                    or.addOperand(util.createEnumEquality(life, "state", "compensated"));
                }
                rg3Pred.addOperand(or);
            }
        }
        List activities = bpa.getActivities();
        ExprOpAnd and = bpa.createExprOpAnd();
        ExprEqualityOp eq = util.createEnumEquality(pLife, "state", "cancelled");
        eq.setNegated(true);
        and.addOperand(eq);
        eq = util.createEnumEquality(pLife, "state", "compensated");
        eq.setNegated(true);
        and.addOperand(eq);
        eq = util.createEnumEquality(pLife, "state", "completed");
        eq.setNegated(true);
        and.addOperand(eq);
        for (Iterator iter = activities.iterator(); iter.hasNext(); ) {
            Activity activity = (Activity) iter.next();
            for (Iterator iterator = activity.getTransitions().iterator(); iterator.hasNext(); ) {
                Transition t = (Transition) iterator.next();
                if (t.isBeginTransition()) {
                    VPreRequirement pre = bpa.createVPreRequirement();
                    pre.setComment("requirement general 2");
                    pre.setName("gr2_" + activity.getName() + "_" + t.getName());
                    pre.setExpression(and);
                    pre.setTransition(t);
                    pre.setActivity(activity);
                    bpa.addPreRequirement(pre);
                }
            }
        }
    }
}
