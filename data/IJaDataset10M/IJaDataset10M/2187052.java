package org.parallelj.mda.rt.controlflow.engine.split;

import org.junit.Test;
import org.parallelj.mda.rt.controlflow.engine.split.OrSplit;

public class OrSplitTest extends AbstractSplitTest {

    @Test
    public void testTrueTrueFlow() {
        OrSplit split = newOrSplit(true, true);
        split.split(this.activity);
        this.verify(1, 1);
    }

    @Test
    public void testTrueFalseFlow() {
        OrSplit split = newOrSplit(true, false);
        split.split(this.activity);
        this.verify(1, 0);
    }

    @Test
    public void testFalseTrueFlow() {
        OrSplit split = newOrSplit(false, true);
        split.split(this.activity);
        this.verify(0, 1);
    }

    @Test
    public void testFalseFalseFlow() {
        OrSplit split = newOrSplit(false, false);
        split.split(this.activity);
        this.verifySum(1);
    }

    @Test
    public void testFalseFalseFalseFlow() {
        OrSplit split = newOrSplit(false, false, false);
        split.split(this.activity);
        this.verifySum(1);
    }

    OrSplit newOrSplit(boolean... guards) {
        this.prepare(guards);
        return new OrSplit(this.activityImpl, this.edges);
    }
}
