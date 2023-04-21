package org.jcvi.assembly.cas.alignment.score;

import org.jcvi.assembly.cas.alignment.CasAlignmentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCasAlignmentType {

    @Test
    public void valueOf() {
        assertSame(CasAlignmentType.LOCAL, CasAlignmentType.valueOf((byte) 0));
        assertSame(CasAlignmentType.SEMI_LOCAL, CasAlignmentType.valueOf((byte) 1));
        assertSame(CasAlignmentType.REVERSE_SEMI_LOCAL, CasAlignmentType.valueOf((byte) 2));
        assertSame(CasAlignmentType.GLOBAL, CasAlignmentType.valueOf((byte) 3));
    }
}
