package net.sourceforge.ondex.mapping.go2goslim;

import net.sourceforge.ondex.config.LuceneRegistry;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.searchable.LuceneEnv;
import net.sourceforge.ondex.mapping.AbstractONDEXMappingTest;
import net.sourceforge.ondex.mapping.MappingArguments;

/**
 * 
 * @author hindlem
 *
 */
public class MappingTest extends AbstractONDEXMappingTest {

    private ConceptClass gene;

    private ConceptClass proc;

    private RelationType is_a;

    private RelationType is_p;

    private RelationType part;

    private RelationType func;

    private RelationType locIn;

    private RelationType notFunc;

    private RelationType notPart;

    private RelationType notLoc;

    private CV goCV;

    private CV goslimCV;

    private EvidenceType evi;

    @Override
    public void testMapping() throws Exception {
    }

    @Override
    public void setup() {
        gene = graph.getMetaData().getConceptClass("Gene");
        assertNotNull(gene);
        evi = graph.getMetaData().createEvidenceType("MADE UP", "..", "..");
        assertNotNull(evi);
        goCV = graph.getMetaData().getCV(MetaData.cvGO);
        assertNotNull(goCV);
        goslimCV = graph.getMetaData().getCV(MetaData.cvGOSLIM);
        assertNotNull(goslimCV);
        proc = graph.getMetaData().getConceptClass(MetaData.BioProc);
        assertNotNull(proc);
        is_a = graph.getMetaData().getRelationType(MetaData.is_a);
        assertNotNull(is_a);
        is_p = graph.getMetaData().getRelationType(MetaData.is_p);
        assertNotNull(is_p);
        func = graph.getMetaData().getRelationType(MetaData.hasFunction);
        assertNotNull(func);
        part = graph.getMetaData().getRelationType(MetaData.hasParticipant);
        assertNotNull(part);
        locIn = graph.getMetaData().getRelationType(MetaData.locatedIn);
        assertNotNull(locIn);
        notFunc = graph.getMetaData().getRelationType(MetaData.notFunction);
        assertNotNull(notFunc);
        notPart = graph.getMetaData().getRelationType(MetaData.notParticipant);
        assertNotNull(notPart);
        notLoc = graph.getMetaData().getRelationType(MetaData.notLocatedIn);
        assertNotNull(notLoc);
    }

    /**
	 * see http:&#47&#47geneontology.cvs.sourceforge.net&#47&#42checkout&#42&#47geneontology&#47go-dev&#47go-perl&#47doc&#47map2slim.gif
	 * @throws Exception 
	 */
    public void testMap2SlimExample() throws Exception {
        ONDEXConcept concept1 = graph.createConcept("1", goslimCV, proc, evi);
        ONDEXConcept concept2 = graph.createConcept("2", goslimCV, proc, evi);
        ONDEXConcept concept3 = graph.createConcept("3", goslimCV, proc, evi);
        ONDEXConcept concept4 = graph.createConcept("4", goslimCV, proc, evi);
        graph.createRelation(concept2, concept1, is_a, evi);
        graph.createRelation(concept3, concept1, is_a, evi);
        graph.createRelation(concept4, concept3, is_a, evi);
        ONDEXConcept concept5 = graph.createConcept("5", goCV, proc, evi);
        ONDEXConcept concept6 = graph.createConcept("6", goCV, proc, evi);
        ONDEXConcept concept7 = graph.createConcept("7", goCV, proc, evi);
        ONDEXConcept concept8 = graph.createConcept("8", goCV, proc, evi);
        ONDEXConcept concept9 = graph.createConcept("9", goCV, proc, evi);
        ONDEXConcept concept10 = graph.createConcept("10", goCV, proc, evi);
        graph.createRelation(concept10, concept5, is_a, evi);
        graph.createRelation(concept10, concept8, is_a, evi);
        graph.createRelation(concept9, concept8, is_a, evi);
        graph.createRelation(concept9, concept7, is_a, evi);
        graph.createRelation(concept8, concept6, is_a, evi);
        concept2.createConceptAccession("A", goCV, false);
        concept3.createConceptAccession("A", goCV, false);
        concept5.createConceptAccession("A", goCV, false);
        concept4.createConceptAccession("B", goCV, false);
        concept7.createConceptAccession("B", goCV, false);
        concept3.createConceptAccession("C", goCV, false);
        concept6.createConceptAccession("C", goCV, false);
        ONDEXConcept gene1 = graph.createConcept("Gene1", goCV, gene, evi);
        ONDEXConcept gene2 = graph.createConcept("Gene2", goCV, gene, evi);
        ONDEXConcept gene3 = graph.createConcept("Gene3", goCV, gene, evi);
        ONDEXConcept gene4 = graph.createConcept("Gene4", goCV, gene, evi);
        ONDEXConcept gene5 = graph.createConcept("Gene5", goCV, gene, evi);
        ONDEXConcept gene6 = graph.createConcept("Gene6", goCV, gene, evi);
        graph.createRelation(gene1, concept5, part, evi);
        graph.createRelation(gene2, concept6, part, evi);
        graph.createRelation(gene3, concept7, part, evi);
        graph.createRelation(gene4, concept8, part, evi);
        graph.createRelation(gene5, concept9, part, evi);
        graph.createRelation(gene6, concept10, part, evi);
        LuceneEnv lev = loadLuceneEnv(graph);
        MappingArguments arg = new MappingArguments();
        LuceneRegistry.sid2luceneEnv.put(graph.getSID(), lev);
        Mapping m = new Mapping();
        m.setArguments(arg);
        m.setONDEXGraph(graph);
        m.start();
        writeOutputAndTestTheResult();
    }
}
