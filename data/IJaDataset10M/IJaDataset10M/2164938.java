package de.denkselbst.niffler.search.niffler;

import java.util.BitSet;
import junit.framework.TestCase;
import de.denkselbst.niffler.bottomclause.BottomClause;
import de.denkselbst.niffler.bottomclause.BottomClauseTestHelper;
import de.denkselbst.niffler.refinement.State;

public class AcceptabilityJudgeTest extends TestCase {

    public void testAcceptable() {
        AcceptabilityJudge j = new AcceptabilityJudge(AcceptabilityJudge.NoiseLevel.ONE_PROMILLE);
        assertTrue(j.acceptable(createState(1000, 1)));
        assertFalse(j.acceptable(createState(1000, 2)));
    }

    public void testGetP() {
        AcceptabilityJudge j = new AcceptabilityJudge(AcceptabilityJudge.NoiseLevel.ONE_PROMILLE);
        State s1 = createState(1000, 1);
        assertEquals(1000.0, j.getP(s1), 0.0001);
        State s2 = createState(2000, 4);
        assertEquals(500.0, j.getP(s2), 0.0001);
        assertFalse("State 2 is not acceptable and hence must not yet be pruned.", j.lowerNoise(s1, s2));
        State s3 = createState(2000, 1);
        assertEquals(2000.0, j.getP(s3), 0.0001);
        assertTrue(j.lowerNoise(s3, s1));
        assertFalse(j.lowerNoise(null, s1));
        State s4 = createState(2000, 0);
        assertEquals(2000.0, j.getP(s4), 0.0001);
        assertTrue(j.lowerNoise(s3, s1));
    }

    private State createState(int posCover, int negCover) {
        BitSet pos = new BitSet();
        pos.set(0, posCover);
        BitSet neg = new BitSet();
        neg.set(0, negCover);
        BottomClause bc = BottomClauseTestHelper.createEmptyDummy();
        State s = State.createInitialState(bc, pos, neg);
        return s;
    }
}
