package net.sourceforge.ondex.filter.allpairs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.io.File;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.config.Config;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.Unit;
import net.sourceforge.ondex.filter.FilterArguments;
import net.sourceforge.ondex.tools.DirUtils;
import net.sourceforge.ondex.tools.ONDEXGraphCloner;
import net.sourceforge.ondex.workflow.ONDEXWorkflow;
import net.sourceforge.ondex.workflow.Parameters.GraphInit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AllPairsTest {

    private String ondexDir;

    private ONDEXWorkflow ondex;

    private ONDEXGraph og;

    private Unit weightUnit;

    private AttributeName weightAN;

    private ConceptClass someCC;

    private RelationType rt1;

    private RelationType rts1;

    private EvidenceType ev;

    private CV cv;

    private int lastCID = 1;

    ONDEXConcept[] cs;

    ONDEXRelation[] rs;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        ondexDir = System.getProperty("ondex.dir");
        if (ondexDir == null || ondexDir.length() == 0) {
            fail("ondex.dir is not set in -D variables can not continue");
        }
        if (ondexDir == null) {
            fail("ondex.dir is not set in -D variables can not continue");
        }
        File dir = new File(ondexDir + File.separator + "dbs" + File.separator + "EquCollapserTest");
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            DirUtils.deleteTree(dir);
            dir.delete();
            dir.mkdir();
        }
        Config.ondexDir = ondexDir;
        ondex = new ONDEXWorkflow(this.getClass().getName(), true, ondexDir + File.separator + "dbs", GraphInit.BERKELEY);
        og = ondex.getDefaultGraph();
        assertNotNull(og);
        weightUnit = og.getMetaData().createUnit("g", "gramm", "weight unit");
        weightAN = og.getMetaData().getFactory().createAttributeName("weight", "edge weight", "determines the weight of this edge", weightUnit, Double.class);
        someCC = og.getMetaData().getFactory().createConceptClass("otherCC", "other concept class");
        rt1 = og.getMetaData().getFactory().createRelationType("testRT1", "test relation type 1");
        rts1 = og.getMetaData().getFactory().createRelationType("testRTS1", rt1);
        ev = og.getMetaData().getFactory().createEvidenceType("ev");
        cv = og.getMetaData().getFactory().createCV("cv1");
        cs = new ONDEXConcept[5];
        for (int i = 0; i < cs.length; i++) cs[i] = anotherConcept();
        rs = new ONDEXRelation[9];
        rs[0] = anotherRelation(cs[0], cs[1], 0.1);
        rs[1] = anotherRelation(cs[2], cs[1], 0.1);
        rs[2] = anotherRelation(cs[3], cs[2], 1.0);
        rs[3] = anotherRelation(cs[4], cs[3], 0.1);
        rs[4] = anotherRelation(cs[0], cs[4], 0.1);
        rs[5] = anotherRelation(cs[1], cs[4], 0.1);
        rs[6] = anotherRelation(cs[1], cs[3], 0.1);
        rs[7] = anotherRelation(cs[0], cs[2], 0.1);
        rs[8] = anotherRelation(cs[3], cs[0], 0.1);
        assert (og.getConcepts().size() == 5);
        assert (og.getRelations().size() == 9);
    }

    private ONDEXConcept anotherConcept() {
        return og.getFactory().createConcept("node#" + (lastCID++), cv, someCC, ev);
    }

    private ONDEXRelation anotherRelation(ONDEXConcept from, ONDEXConcept to, double weight) {
        ONDEXRelation r = og.getFactory().createRelation(from, to, rts1, ev);
        r.createRelationGDS(weightAN, new Double(weight), true);
        return r;
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        weightUnit = null;
        weightAN = null;
        someCC = null;
        rt1 = null;
        rts1 = null;
        ev = null;
        cv = null;
        cs = null;
        rs = null;
        String ondexDir = System.getProperty("ondex.dir");
        System.out.println("Deleting previous databases under: " + ondexDir + File.separator + this.getClass().getName());
        ondex.closeGraphs();
        og = null;
        DirUtils.deleteTree(ondexDir + File.separator + "dbs" + File.separator + this.getClass().getName());
        ONDEXGraphCloner.clearIndex();
        ondex = null;
        System.gc();
    }

    @Test
    public void testDirected() {
        Filter filter = new Filter();
        assertEquals("All pairs shortest path filter", filter.getName());
        assertNotNull(filter.getVersion());
        ArgumentDefinition<?>[] args = filter.getArgumentDefinitions();
        assert (args.length == 3);
        FilterArguments fa = new FilterArguments();
        String name = null;
        Object value = null;
        for (ArgumentDefinition<?> arg : args) {
            name = arg.getName();
            if (name.equals(ArgumentNames.WEIGHTATTRIBUTENAME_ARG)) value = weightAN.getId(); else if (name.equals(ArgumentNames.ONLYDIRECTED_ARG)) value = new Boolean(true); else continue;
            fa.addOption(name, value);
        }
        assert (fa.getOptions().size() == 2);
        filter.setONDEXGraph(og);
        filter.setArguments(fa);
        filter.start();
        assert (filter.getVisibleConcepts().contains(cs[0]));
        assert (filter.getVisibleConcepts().contains(cs[1]));
        assert (filter.getVisibleConcepts().contains(cs[2]));
        assert (filter.getVisibleConcepts().contains(cs[3]));
        assert (filter.getVisibleConcepts().contains(cs[4]));
        assert (filter.getVisibleConcepts().size() == 5);
        assert (filter.getVisibleRelations().contains(rs[0]));
        assert (filter.getVisibleRelations().contains(rs[1]));
        assert (!filter.getVisibleRelations().contains(rs[2]));
        assert (filter.getVisibleRelations().contains(rs[3]));
        assert (filter.getVisibleRelations().contains(rs[4]));
        assert (filter.getVisibleRelations().contains(rs[5]));
        assert (filter.getVisibleRelations().contains(rs[6]));
        assert (filter.getVisibleRelations().contains(rs[7]));
        assert (filter.getVisibleRelations().contains(rs[8]));
        assert (filter.getVisibleRelations().size() == 8);
    }

    @Test
    public void testUndirected() {
        Filter filter = new Filter();
        assertEquals("All pairs shortest path filter", filter.getName());
        assertNotNull(filter.getVersion());
        ArgumentDefinition<?>[] args = filter.getArgumentDefinitions();
        assert (args.length == 3);
        FilterArguments fa = new FilterArguments();
        String name = null;
        Object value = null;
        for (ArgumentDefinition<?> arg : args) {
            name = arg.getName();
            if (name.equals(ArgumentNames.WEIGHTATTRIBUTENAME_ARG)) value = weightAN.getId(); else if (name.equals(ArgumentNames.ONLYDIRECTED_ARG)) value = new Boolean(false); else continue;
            fa.addOption(name, value);
        }
        assert (fa.getOptions().size() == 2);
        filter.setONDEXGraph(og);
        filter.setArguments(fa);
        filter.start();
        assert (filter.getVisibleConcepts().contains(cs[0]));
        assert (filter.getVisibleConcepts().contains(cs[1]));
        assert (filter.getVisibleConcepts().contains(cs[2]));
        assert (filter.getVisibleConcepts().contains(cs[3]));
        assert (filter.getVisibleConcepts().contains(cs[4]));
        assert (filter.getVisibleConcepts().size() == 5);
        assert (filter.getVisibleRelations().contains(rs[0]));
        assert (filter.getVisibleRelations().contains(rs[1]));
        assert (!filter.getVisibleRelations().contains(rs[2]));
        assert (filter.getVisibleRelations().contains(rs[3]));
        assert (filter.getVisibleRelations().contains(rs[4]));
        assert (filter.getVisibleRelations().contains(rs[5]));
        assert (filter.getVisibleRelations().contains(rs[6]));
        assert (filter.getVisibleRelations().contains(rs[7]));
        assert (filter.getVisibleRelations().contains(rs[8]));
        assert (filter.getVisibleRelations().size() == 8);
    }
}
