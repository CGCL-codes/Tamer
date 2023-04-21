package org.vardb.analysis;

import java.util.Map;
import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.vardb.alignment.CJalview;
import org.vardb.analysis.dao.CSelectonAnalysis;
import org.vardb.sequences.CSequenceFileParser;
import org.vardb.sequences.CSequenceType;
import org.vardb.util.CStringHelper;

public class TestSelectonWrapper extends AbstractAnalysisTest {

    protected String dir = "d:/research/software/selecton/";

    @Test
    @Ignore
    public void testSelecton() {
        String str = getResource("selecton.aln");
        Map<String, String> alignment = CSequenceFileParser.readFastaAlignment(str, CSequenceType.NT);
        CSelectonWrapper.Params params = new CSelectonWrapper.Params();
        params.setAlignment(alignment);
        CSelectonWrapper helper = createWrapper();
        CSelectonAnalysis analysis = helper.selecton(params);
        assertPositions(analysis.getPositions());
    }

    @Test
    @Ignore
    public void testParseSelectonOutput() {
        String str = getResource("selecton.res");
        CSelectonWrapper.Positions positions = CSelectonWrapper.parsePositions(str);
        assertPositions(positions);
    }

    @Test
    public void testJalview() {
        String str = getResource("selecton.res");
        CSelectonWrapper.Positions positions = CSelectonWrapper.parsePositions(str);
        assertPositions(positions);
        str = getResource("selecton.aln");
        Map<String, String> alignment = CSequenceFileParser.readFastaAlignment(str, CSequenceType.NT);
        CJalview jalview = new CJalview(CSequenceType.NT, alignment);
        positions.addFeatures(jalview);
        System.out.println(jalview.getFeatures().toString());
    }

    private void assertPositions(CSelectonWrapper.Positions positions) {
        Assertions.assertThat(positions.getPositions().size()).isPositive();
        for (CSelectonWrapper.Position position : positions.getPositions()) {
            System.out.println("position: " + CStringHelper.toString(position));
        }
    }

    private CSelectonWrapper createWrapper() {
        return new CSelectonWrapper(this.dir, this.tempDir);
    }
}
