package com.sylli.oeuf.server.game.logic.invocation;

import com.sylli.oeuf.server.game.logic.CastingTarget;

public class InvocationParameter {

    private int rank;

    private int priority;

    private CastingTarget target;

    public InvocationParameter(int rank, int priority, CastingTarget target) {
        this.rank = rank;
        this.priority = priority;
        this.target = target;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public CastingTarget getTarget() {
        return target;
    }

    public void setTarget(CastingTarget target) {
        this.target = target;
    }
}
