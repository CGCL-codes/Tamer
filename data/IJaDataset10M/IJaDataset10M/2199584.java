package net.sourceforge.ondex.export.delimitedfile;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.memory.MemoryONDEXGraph;
import net.sourceforge.ondex.event.ONDEXEventHandler;
import net.sourceforge.ondex.export.ExportArguments;
import net.sourceforge.ondex.logging.ONDEXCoreLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author hindlem
 *
 */
public class ExportTest {

    private MemoryONDEXGraph aog;

    private ConceptClass ccTS;

    private ConceptClass ccProtein;

    private ConceptClass ccGene;

    private ConceptClass ccReaction;

    private ConceptClass ccEnzyme;

    private ConceptClass ccPathway;

    private RelationType rts_is_a;

    private RelationType rts_m_isp;

    private RelationType rts_cats;

    private RelationType rts_h_s_s;

    private EvidenceType et;

    private CV cv;

    private File file;

    @Before
    public void setUp() {
        aog = new MemoryONDEXGraph("testGraph");
        ONDEXEventHandler.getEventHandlerForSID(aog.getSID()).addONDEXGraphListener(new ONDEXCoreLogger());
        ccTS = aog.getMetaData().getFactory().createConceptClass("TARGETSEQUENCE");
        ccProtein = aog.getMetaData().getFactory().createConceptClass("Protein");
        ccGene = aog.getMetaData().getFactory().createConceptClass("Gene");
        ccEnzyme = aog.getMetaData().getFactory().createConceptClass("Enzyme");
        ccReaction = aog.getMetaData().getFactory().createConceptClass("Reaction");
        ccPathway = aog.getMetaData().getFactory().createConceptClass("Pathway");
        rts_is_a = aog.getMetaData().getFactory().createRelationType("is_a");
        rts_m_isp = aog.getMetaData().getFactory().createRelationType("m_isp");
        rts_cats = aog.getMetaData().getFactory().createRelationType("cat_by");
        rts_h_s_s = aog.getMetaData().getFactory().createRelationType("h_s_s");
        et = aog.getMetaData().getFactory().createEvidenceType("I made it up");
        cv = aog.getMetaData().getFactory().createCV("matts db");
        try {
            file = File.createTempFile(getClass().getName(), "tmpfile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        file.delete();
        file = null;
        aog = null;
    }

    @Test
    public void testArtemExample() throws IOException {
        ConceptClass treatment = aog.getMetaData().getFactory().createConceptClass("Treatment");
        ConceptClass pub = aog.getMetaData().getFactory().createConceptClass("Publication");
        RelationType is_r = aog.getMetaData().getFactory().createRelationType("is_r");
        CV uprot = aog.getMetaData().getFactory().createCV("UNIPROTKB");
        CV nlm = aog.getMetaData().getFactory().createCV("NLM");
        AttributeName tm_score = aog.getMetaData().getFactory().createAttributeName("TM_SCORE", Double.class);
        ONDEXConcept prot1 = aog.createConcept("LTI30", cv, ccProtein, et);
        prot1.createConceptAccession("Q41966", uprot, false);
        prot1.createConceptAccession("P42758", uprot, false);
        ONDEXConcept prot2 = aog.createConcept("LTI30", cv, ccProtein, et);
        prot2.createConceptAccession("N45966", uprot, false);
        prot2.createConceptAccession("I33758", uprot, false);
        ONDEXConcept pub1 = aog.createConcept("pub", cv, pub, et);
        pub1.createConceptAccession("18088305", nlm, false);
        ONDEXConcept treat1 = aog.createConcept("Abscisic Acid", cv, treatment, et);
        treat1.createConceptName("Abscisic Acid", true);
        aog.createRelation(pub1, prot2, is_r, et).createRelationGDS(tm_score, Double.valueOf(2.5464654d), false);
        aog.createRelation(pub1, prot1, is_r, et).createRelationGDS(tm_score, Double.valueOf(2.9015989303588867d), false);
        aog.createRelation(pub1, treat1, is_r, et).createRelationGDS(tm_score, Double.valueOf(2.6913013458251953d), false);
        ExportArguments args = new ExportArguments();
        args.setExportFile(file.getAbsolutePath());
        args.addOption(Export.ORDER_ARG, "Protein#is_r,pub_in#Publication#is_r#Treatment");
        args.addOption(Export.FILE_ARG, file.getAbsolutePath());
        args.addOption(Export.LINKS_ARG, false);
        args.addOption(Export.MIN_REPORT_DEPTH_ARG, Integer.MAX_VALUE);
        args.addOption(Export.COMPLETE_ONLY_ARG, false);
        args.addOption(Export.REMOVE_DUPLICATES_ARG, false);
        args.addOption(Export.TRANSLATE_TAXID_ARG, false);
        args.addOption(Export.ZIP_FILE_ARG, false);
        args.addOption(Export.ATTRIB_ARG, "0:accession:UNIPROTKB");
        args.addOption(Export.ATTRIB_ARG, "1:gds:TM_SCORE");
        args.addOption(Export.ATTRIB_ARG, "2:accession:NLM");
        args.addOption(Export.ATTRIB_ARG, "3:gds:TM_SCORE");
        args.addOption(Export.ATTRIB_ARG, "4:name");
        Export export = new Export();
        export.setArguments(args);
        export.setONDEXGraph(aog);
        export.start();
        List<String> lines = new ArrayList<String>();
        lines.add("P42758	2.9015989303588867	18088305	2.6913013458251953	Abscisic Acid");
        lines.add("I33758	2.5464654	18088305	2.6913013458251953	Abscisic Acid");
        int i = 0;
        BufferedReader bis = new BufferedReader(new FileReader(file));
        while (bis.ready()) {
            String line = bis.readLine();
            if (i > 0) {
                assertTrue(lines.contains(line.trim()));
            }
            i++;
        }
    }

    @Test
    public void testSimpleExportWith1Branch() throws IOException {
        ONDEXConcept p1 = aog.getFactory().createConcept("p1", cv, ccProtein, et);
        ONDEXConcept g1 = aog.getFactory().createConcept("g1", cv, ccGene, et);
        ONDEXConcept p2 = aog.getFactory().createConcept("p2", cv, ccProtein, et);
        ONDEXConcept g2 = aog.getFactory().createConcept("g2", cv, ccGene, et);
        ONDEXConcept r1 = aog.getFactory().createConcept("r1", cv, ccReaction, et);
        aog.getFactory().createRelation(p1, g1, rts_is_a, et);
        aog.getFactory().createRelation(p2, g2, rts_is_a, et);
        aog.getFactory().createRelation(r1, p1, rts_cats, et);
        aog.getFactory().createRelation(r1, p2, rts_cats, et);
        ExportArguments args = new ExportArguments();
        args.setExportFile(file.getAbsolutePath());
        args.addOption(Export.ORDER_ARG, "Gene#is_a#Protein#cat_by#Reaction");
        args.addOption(Export.FILE_ARG, file.getAbsolutePath());
        args.addOption(Export.LINKS_ARG, false);
        args.addOption(Export.MIN_REPORT_DEPTH_ARG, Integer.MAX_VALUE);
        args.addOption(Export.COMPLETE_ONLY_ARG, false);
        args.addOption(Export.REMOVE_DUPLICATES_ARG, false);
        args.addOption(Export.TRANSLATE_TAXID_ARG, true);
        args.addOption(Export.ZIP_FILE_ARG, false);
        args.addOption(Export.ATTRIB_ARG, "*:class");
        args.addOption(Export.ATTRIB_ARG, "*:pid");
        Export export = new Export();
        export.setArguments(args);
        export.setONDEXGraph(aog);
        export.start();
        List<String> lines = new ArrayList<String>();
        lines.add("Gene	g1	is_a	1,2,is_a	Protein	p1	cat_by	5,1,cat_by	Reaction	r1");
        lines.add("Gene	g2	is_a	3,4,is_a	Protein	p2	cat_by	5,3,cat_by	Reaction	r1");
        int i = 0;
        BufferedReader bis = new BufferedReader(new FileReader(file));
        while (bis.ready()) {
            String line = bis.readLine();
            if (i > 0) {
                assertTrue(lines.contains(line.trim()));
            }
            i++;
        }
    }

    @Test
    public void testBasicExport() throws IOException {
        ONDEXConcept ts = aog.createConcept("TARGETSEQUENCE_1", cv, ccTS, et);
        ONDEXConcept p = aog.createConcept("Protein_1", cv, ccProtein, et);
        ONDEXConcept e = aog.createConcept("Enzyme_1", cv, ccEnzyme, et);
        ONDEXConcept r = aog.createConcept("Reaction_1", cv, ccReaction, et);
        ONDEXConcept path = aog.createConcept("Pathway_1", cv, ccPathway, et);
        ONDEXRelation hss = aog.createRelation(ts, p, rts_h_s_s, et);
        ONDEXRelation isa = aog.createRelation(p, e, rts_is_a, et);
        ONDEXRelation cat = aog.createRelation(e, r, rts_cats, et);
        ONDEXRelation misp = aog.createRelation(r, path, rts_m_isp, et);
        ExportArguments args = new ExportArguments();
        args.setExportFile(file.getAbsolutePath());
        args.addOption(Export.ORDER_ARG, "TARGETSEQUENCE#h_s_s#Protein#is_a#Enzyme#cat_by#Reaction#m_isp#Pathway");
        args.addOption(Export.FILE_ARG, file.getAbsolutePath());
        args.addOption(Export.LINKS_ARG, true);
        args.addOption(Export.MIN_REPORT_DEPTH_ARG, Integer.MAX_VALUE);
        args.addOption(Export.COMPLETE_ONLY_ARG, true);
        args.addOption(Export.REMOVE_DUPLICATES_ARG, true);
        args.addOption(Export.TRANSLATE_TAXID_ARG, true);
        args.addOption(Export.ZIP_FILE_ARG, false);
        args.addOption(Export.ATTRIB_ARG, "*:class");
        args.addOption(Export.ATTRIB_ARG, "*:pid");
        Export export = new Export();
        export.setArguments(args);
        export.setONDEXGraph(aog);
        export.start();
        int i = 0;
        BufferedReader bis = new BufferedReader(new FileReader(file));
        while (bis.ready()) {
            String line = bis.readLine();
            String[] elements = line.split("\t");
            assertEquals(9 * 2, elements.length);
            if (i == 1) {
                for (int j = 0; j < elements.length; j = j + 2) {
                    String type = elements[j];
                    String parserID = elements[j + 1];
                    if (j == 0) {
                        assertEquals(ts.getOfType().getId(), type);
                        assertEquals(ts.getPID(), parserID);
                    } else if (j == 2) {
                        assertEquals(hss.getOfType().getId(), type);
                        assertEquals(hss.getKey().toString(), parserID);
                    } else if (j == 4) {
                        assertEquals(p.getOfType().getId(), type);
                        assertEquals(p.getPID(), parserID);
                    } else if (j == 6) {
                        assertEquals(isa.getOfType().getId(), type);
                        assertEquals(isa.getKey().toString(), parserID);
                    } else if (j == 8) {
                        assertEquals(e.getOfType().getId(), type);
                        assertEquals(e.getPID(), parserID);
                    } else if (j == 10) {
                        assertEquals(cat.getOfType().getId(), type);
                        assertEquals(cat.getKey().toString(), parserID);
                    } else if (j == 12) {
                        assertEquals(r.getOfType().getId(), type);
                        assertEquals(r.getPID(), parserID);
                    } else if (j == 14) {
                        assertEquals(misp.getOfType().getId(), type);
                        assertEquals(misp.getKey().toString(), parserID);
                    } else if (j == 16) {
                        assertEquals(path.getOfType().getId(), type);
                        assertEquals(path.getPID(), parserID);
                    }
                }
            }
            i++;
        }
        assertEquals(2, i);
    }

    @Test
    public void testBranchedPaths() throws IOException {
        ONDEXConcept ts = aog.createConcept("TARGETSEQUENCE_1", cv, ccTS, et);
        ONDEXConcept p = aog.createConcept("Protein_1", cv, ccProtein, et);
        ONDEXConcept e = aog.createConcept("Enzyme_1", cv, ccEnzyme, et);
        ONDEXConcept r = aog.createConcept("Reaction_1", cv, ccReaction, et);
        ONDEXConcept path = aog.createConcept("Pathway_1", cv, ccPathway, et);
        ONDEXConcept r2 = aog.createConcept("Reaction_2", cv, ccReaction, et);
        ONDEXConcept path2 = aog.createConcept("Pathway_2", cv, ccPathway, et);
        ONDEXRelation hss = aog.createRelation(ts, p, rts_h_s_s, et);
        ONDEXRelation isa = aog.createRelation(p, e, rts_is_a, et);
        ONDEXRelation cat = aog.createRelation(e, r, rts_cats, et);
        ONDEXRelation misp = aog.createRelation(r, path, rts_m_isp, et);
        ONDEXRelation cat2 = aog.createRelation(e, r2, rts_cats, et);
        ONDEXRelation misp2 = aog.createRelation(r2, path2, rts_m_isp, et);
        ExportArguments args = new ExportArguments();
        args.setExportFile(file.getAbsolutePath());
        args.addOption(Export.ORDER_ARG, "TARGETSEQUENCE#h_s_s#Protein#is_a#Enzyme#cat_by#Reaction#m_isp#Pathway");
        args.addOption(Export.FILE_ARG, file.getAbsolutePath());
        args.addOption(Export.ZIP_FILE_ARG, false);
        args.addOption(Export.LINKS_ARG, true);
        args.addOption(Export.MIN_REPORT_DEPTH_ARG, Integer.MAX_VALUE);
        args.addOption(Export.COMPLETE_ONLY_ARG, true);
        args.addOption(Export.REMOVE_DUPLICATES_ARG, true);
        args.addOption(Export.TRANSLATE_TAXID_ARG, true);
        args.addOption(Export.ATTRIB_ARG, "*:class");
        args.addOption(Export.ATTRIB_ARG, "*:pid");
        args.addOption(Export.ATTRIB_ARG, "^:cv");
        args.addOption(Export.ATTRIB_ARG, "$:evidence");
        Export export = new Export();
        export.setArguments(args);
        export.setONDEXGraph(aog);
        export.start();
        int i = 0;
        BufferedReader bis = new BufferedReader(new FileReader(file));
        while (bis.ready()) {
            String line = bis.readLine();
            String[] elements = line.split("\t");
            assertEquals((9 * 2) + 2, elements.length);
            if (i == 1) {
                for (int j = 0; j < elements.length; j = j + 2) {
                    String type = elements[j];
                    String parserID = elements[j + 1];
                    if (j == 0) {
                        assertEquals(ts.getOfType().getId(), type);
                        assertEquals(ts.getPID(), parserID);
                        String cv = elements[j + 2];
                        assertEquals(ts.getElementOf().getId(), cv);
                    } else if (j == 3) {
                        assertEquals(hss.getOfType().getId(), type);
                        assertEquals(hss.getKey().toString(), parserID);
                    } else if (j == 5) {
                        assertEquals(p.getOfType().getId(), type);
                        assertEquals(p.getPID(), parserID);
                    } else if (j == 7) {
                        assertEquals(isa.getOfType().getId(), type);
                        assertEquals(isa.getKey().toString(), parserID);
                    } else if (j == 9) {
                        assertEquals(e.getOfType().getId(), type);
                        assertEquals(e.getPID(), parserID);
                    } else if (j == 11) {
                        assertEquals(cat.getOfType().getId(), type);
                        assertEquals(cat.getKey().toString(), parserID);
                    } else if (j == 13) {
                        assertEquals(r.getOfType().getId(), type);
                        assertEquals(r.getPID(), parserID);
                    } else if (j == 15) {
                        assertEquals(misp.getOfType().getId(), type);
                        assertEquals(misp.getKey().toString(), parserID);
                    } else if (j == 17) {
                        assertEquals(path.getOfType().getId(), type);
                        assertEquals(path.getPID(), parserID);
                        String evidence = elements[j + 2];
                        assertEquals("I made it up", evidence);
                    }
                }
            } else if (i == 2) {
                for (int j = 0; j < elements.length; j = j + 2) {
                    String type = elements[j];
                    String parserID = elements[j + 1];
                    if (j == 0) {
                        assertEquals(ts.getOfType().getId(), type);
                        assertEquals(ts.getPID(), parserID);
                        String cv = elements[j + 2];
                        assertEquals(ts.getElementOf().getId(), cv);
                    } else if (j == 3) {
                        assertEquals(hss.getOfType().getId(), type);
                        assertEquals(hss.getKey().toString(), parserID);
                    } else if (j == 5) {
                        assertEquals(p.getOfType().getId(), type);
                        assertEquals(p.getPID(), parserID);
                    } else if (j == 7) {
                        assertEquals(isa.getOfType().getId(), type);
                        assertEquals(isa.getKey().toString(), parserID);
                    } else if (j == 9) {
                        assertEquals(e.getOfType().getId(), type);
                        assertEquals(e.getPID(), parserID);
                    } else if (j == 11) {
                        assertEquals(cat2.getOfType().getId(), type);
                        assertEquals(cat2.getKey().toString(), parserID);
                    } else if (j == 13) {
                        assertEquals(r2.getOfType().getId(), type);
                        assertEquals(r2.getPID(), parserID);
                    } else if (j == 15) {
                        assertEquals(misp2.getOfType().getId(), type);
                        assertEquals(misp2.getKey().toString(), parserID);
                    } else if (j == 17) {
                        assertEquals(path2.getOfType().getId(), type);
                        assertEquals(path2.getPID(), parserID);
                        String evidence = elements[j + 2];
                        assertEquals("I made it up", evidence);
                    }
                }
            }
            i++;
        }
        assertEquals(3, i);
    }
}
