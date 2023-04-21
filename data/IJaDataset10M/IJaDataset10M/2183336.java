package org.eyrene.jothi.core.ai;

import org.eyrene.jgames.core.GameRules;

/**
 * <p>Title: JOthiEvaluator_NewHope.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class JOthiEvaluator_NoHope extends JOthiEvaluator {

    public JOthiEvaluator_NoHope(GameRules gameRules) {
        super(gameRules);
    }

    protected void init() {
        addEvaluation(new Score(), -0.40f, 0, 15);
        addEvaluation(new Score(), -0.25f, 15, 25);
        addEvaluation(new Score(), -0.09f, 25, 29);
        addEvaluation(new Score(), -0.04f, 29, 32);
        addEvaluation(new Score(), 0.1f, 57, 100);
        addEvaluation(new Corners(), 0.5f, 0, 100);
        addEvaluation(new CXSquare(), -0.7f, 0, 100);
        addEvaluation(new Edges(), 0.55f, 0, 19);
        addEvaluation(new Edges(), 0.45f, 19, 29);
        addEvaluation(new Edges(), 0.38f, 29, 100);
        addEvaluation(new Stables(), 0.5f, 0, 100);
        addEvaluation(new Wedges(), 0.5f, 0, 100);
        addEvaluation(new Parity(), 0.1f, 0, 100);
        addEvaluation(new Mobility(), 0.12f, 0, 19);
        addEvaluation(new Mobility(), 0.12f, 19, 25);
        addEvaluation(new Mobility(), 0.17f, 25, 35);
        addEvaluation(new Mobility(), 0.20f, 35, 45);
        addEvaluation(new Mobility(), 0.30f, 45, 100);
        addEvaluation(new Empties(), -0.10f, 0, 19);
        addEvaluation(new Empties(), -0.06f, 19, 25);
        addEvaluation(new Frontiers(), -0.017f, 0, 25);
        addEvaluation(new Frontiers(), -0.025f, 25, 29);
        addEvaluation(new Frontiers(), -0.04f, 29, 35);
        addEvaluation(new Frontiers(), -0.06f, 35, 100);
    }
}
