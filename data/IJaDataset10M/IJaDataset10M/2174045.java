package org.test.cloe.utils;

import org.test.AbstractMockConnectionUnitTests;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAOException;
import org.tigr.cloe.utils.AssemblyDBRange;
import org.tigr.cloe.utils.Range;
import org.tigr.seq.tdb.exceptions.OutOfRangeException;

public class TestAssemblyDBRange extends AbstractMockConnectionUnitTests {

    public TestAssemblyDBRange(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.setUpAssemblyDAOQueries();
    }

    public void test_calculateGappedAsmCoord_noGaps() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int GappedOffset = AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
        assertEquals(baseNumUnGapped - 1, GappedOffset);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAsmCoord_Gaps_afterTheBaseWeWant() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAA------A";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int GappedOffset = AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
        assertEquals(baseNumUnGapped - 1, GappedOffset);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAsmCoord_oneGapBefore() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA-AAGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int GappedOffset = AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
        assertEquals(baseNumUnGapped, GappedOffset);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAsmCoord_twoGapsBefore_Separated() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA-A-AGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int GappedOffset = AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
        assertEquals(6, GappedOffset);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAsmCoord_twoGapsBefore_Consecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA--AAGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int GappedOffset = AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
        assertEquals(6, GappedOffset);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAsmCoord_OutOfRange() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = 50;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAsmCoord_negRange() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int baseNumUnGapped = -50;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAsmCoord_EmptySequence() throws DAOException {
        String gappedConsensus = "";
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAsmCoord_nullSequence() throws OutOfRangeException {
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, null);
        try {
            AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAsmCoord__noSequence() throws OutOfRangeException {
        int assemblyID = 1234;
        int baseNumUnGapped = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus_Empty(assemblyID);
        try {
            AssemblyDBRange.calculateGappedAsmCoord(projectName, assemblyID, baseNumUnGapped);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_noGaps() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int GappedOffset = 4;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int baseNumUnGapped = AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
        assertEquals(baseNumUnGapped, 5);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAsmCoord_GapsAfter() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAA----A";
        int assemblyID = 1234;
        int GappedOffset = 4;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int baseNumUnGapped = AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
        assertEquals(baseNumUnGapped, 5);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAsmCoord_oneGapBefore() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA-AAGAAAA";
        int assemblyID = 1234;
        int GappedOffset = 5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int baseNumUnGapped = AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
        assertEquals(baseNumUnGapped, 5);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAsmCoord_twoGaps_separated() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA-A-AGAAAA";
        int assemblyID = 1234;
        int GappedOffset = 6;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int baseNumUnGapped = AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
        assertEquals(baseNumUnGapped, 5);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAsmCoord_twoGaps_Consecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AA--AAGAAAA";
        int assemblyID = 1234;
        int GappedOffset = 6;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        int baseNumUnGapped = AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
        assertEquals(baseNumUnGapped, 5);
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAsmCoord_outOfRange() throws DAOException {
        String gappedConsensus = "AA--AAGAAAA";
        int assemblyID = 1234;
        int GappedOffset = 60;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_negRange() throws DAOException {
        String gappedConsensus = "AA--AAGAAAA";
        int assemblyID = 1234;
        int GappedOffset = -5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_emptySequence() throws DAOException {
        String gappedConsensus = "";
        int assemblyID = 1234;
        int GappedOffset = -5;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_nullSequence() throws OutOfRangeException {
        int assemblyID = 1234;
        int GappedOffset = -5;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, null);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_emptyResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int GappedOffset = -5;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus_Empty(assemblyID);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAsmCoord_nullResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int GappedOffset = -5;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus_Empty(assemblyID);
        try {
            AssemblyDBRange.calculateUngappedAsmCoord(projectName, assemblyID, GappedOffset);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 8));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_oneGap_insideRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG-AAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_oneGap_afterRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA-T";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 8));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_oneGap_beforeRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "A-AAAGAAAA";
        int assemblyID = 1234;
        int startBase = 2;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(2, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_twoGaps_beforeRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "A--AAAGAAAA";
        int assemblyID = 1234;
        int startBase = 2;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(3, 10));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_twoGaps_afterRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA--T";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 8));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_oneGap_insideRangeSeparated() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG-A-AAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 10));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_oneGap_insideRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG--AAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 10));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_noGaps_StartOutsideOfRange() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 10;
        int endBase = 90;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_StartOutsideOfRange_neg() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = -10;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    /**
 * Looks like if you specify an end that is too far it jsut returns the last base.
 * @throws DAOException
 * @throws OutOfRangeException 
 */
    public void test_calculateGappedAssemblyRange_noGaps_EndOutsideOfRange() throws DAOException, OutOfRangeException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 90;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(0, 8));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateGappedAssemblyRange_noGaps_StartIs0() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 0;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_StartIsGreaterThanEnd() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 6;
        int endBase = 2;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_nullSequence() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, null);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_emptyStringSequence() throws DAOException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, "");
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_emptyResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus_Empty(assemblyID);
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateGappedAssemblyRange_noGaps_nullResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        try {
            AssemblyDBRange.calculateGappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryWholeGappedSize);
            this.verifyPreparedStatementParameter(this.queryWholeGappedSize, 1, assemblyID);
            this.verifySQLStatementNotExecuted(this.queryAssemblyGappedConsensus);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_oneGap_insideRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG-AAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 10;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_oneGap_afterRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA-T";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_oneGap_beforeRange() throws OutOfRangeException, DAOException {
        String gappedConsensus = "A-AAAGAAAA";
        int assemblyID = 1234;
        int startBase = 3;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(2, 8));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_twoGaps_beforeRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "A--AAAGAAAA";
        int assemblyID = 1234;
        int startBase = 4;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(2, 7));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_twoGaps_afterRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAGAAAA--T";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_twoGaps_insideRangeSeparated() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG-A-AAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 7));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_twoGaps_insideRangeConsecutive() throws OutOfRangeException, DAOException {
        String gappedConsensus = "AAAAG--AAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 11;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_noGaps_StartOutsideOfRange() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 10;
        int endBase = 90;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_StartOutsideOfRange_neg() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = -10;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    /**
 * Looks like if you specify an end that is too far it jsut returns the last base.
 * @throws DAOException
 * @throws OutOfRangeException 
 */
    public void test_calculateUngappedAssemblyRange_noGaps_EndOutsideOfRange() throws DAOException, OutOfRangeException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 90;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_noGaps_StartIs0() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 0;
        int endBase = 9;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_StartIsGreaterThanEnd() throws DAOException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 6;
        int endBase = 2;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_StartIsEnd() throws DAOException, OutOfRangeException {
        String gappedConsensus = "AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 6;
        int endBase = 6;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(6, 6));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_StartsOnGap_FirstBase() throws DAOException, OutOfRangeException {
        String gappedConsensus = "-AAAAGAAAA";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 10;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_StartsOnGap() throws DAOException, OutOfRangeException {
        String gappedConsensus = "A-AAAGAAAA";
        int assemblyID = 1234;
        int startBase = 2;
        int endBase = 10;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(2, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_EndsOnGap_lastBase() throws DAOException, OutOfRangeException {
        String gappedConsensus = "AAAAGAAAA-";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 10;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_EndsOnGap() throws DAOException, OutOfRangeException {
        String gappedConsensus = "AAAAGAAAA-T";
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 10;
        this.setUpAssemblyWholeGappedSize(assemblyID, gappedConsensus.length());
        this.setUpAssemblyGappedConsensus(assemblyID, gappedConsensus);
        Range range = AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
        assertEquals(range, new Range(1, 9));
        this.verifyAllResultSetsClosed();
        this.verifyAllStatementsClosed();
        this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
        this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
    }

    public void test_calculateUngappedAssemblyRange_noGaps_nullSequence() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, null);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_emptyStringSequence() throws DAOException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus(assemblyID, "");
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (OutOfRangeException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_emptyResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        this.setUpAssemblyWholeGappedSize(assemblyID, 0);
        this.setUpAssemblyGappedConsensus_Empty(assemblyID);
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryAssemblyGappedConsensus, 1, assemblyID);
        }
    }

    public void test_calculateUngappedAssemblyRange_noGaps_nullResult() throws OutOfRangeException {
        int assemblyID = 1234;
        int startBase = 1;
        int endBase = 8;
        try {
            AssemblyDBRange.calculateUngappedAssemblyRange(projectName, assemblyID, startBase, endBase);
            fail();
        } catch (DAOException e) {
            this.verifyAllResultSetsClosed();
            this.verifyAllStatementsClosed();
            this.verifySQLStatementExecuted(this.queryWholeGappedSize);
            this.verifySQLStatementNotExecuted(this.queryAssemblyGappedConsensus);
            this.verifyPreparedStatementParameter(queryWholeGappedSize, 1, assemblyID);
        }
    }
}
