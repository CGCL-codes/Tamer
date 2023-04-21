package org.parallelj.internal.kernel.join;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.parallelj.internal.kernel.KProcess;
import org.parallelj.internal.kernel.KProcessor;

public class AndJoinTest extends AbstractJoinTest {

    @Test
    public void testOneOneJoin() {
        KAndJoin join = newAndJoin(1, 1);
        this.procedure.setJoin(join);
        KProcess process = this.program.newProcess(null);
        assertTrue(join.isEnabled(process));
        new KProcessor().execute(process);
        this.verify(process, 0, 0);
        assertFalse(join.isEnabled(process));
    }

    @Test
    public void testZeroOneJoin() {
        KAndJoin join = newAndJoin(0, 1);
        this.procedure.setJoin(join);
        KProcess process = this.program.newProcess(null);
        new KProcessor().execute(process);
        assertFalse(join.isEnabled(process));
    }

    @Test
    public void testZeroZeroJoin() {
        KAndJoin join = newAndJoin(0, 0);
        this.procedure.setJoin(join);
        KProcess process = this.program.newProcess(null);
        new KProcessor().execute(process);
        assertFalse(join.isEnabled(process));
    }

    KAndJoin newAndJoin(int... values) {
        this.prepare(values);
        return new KAndJoin(this.procedure);
    }
}
