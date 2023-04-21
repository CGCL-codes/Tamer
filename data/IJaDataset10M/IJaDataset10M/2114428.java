package backend.core.persistent.perst.test.advanced;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXIterator;
import backend.core.AbstractRelation;
import backend.core.index.IndexONDEXGraph;
import backend.core.persistent.perst.PerstEnv;
import backend.core.persistent.test.DirUtils;
import backend.core.test.advancedmodel.AbstractXMLConstructOndexExample;
import backend.logging.ONDEXLogger;

public class XmlDerivedGraphPerst extends AbstractXMLConstructOndexExample {

    private PerstEnv penv;

    private static final String ondexDir = System.getProperty("ondex.dir");

    private static final String PERSTDIR = "PerstMemoryONDEXGraphMetaDataPermissionsTest";

    private static final String dbdir = ondexDir + File.separator + "dbs" + File.separator + PERSTDIR;

    private ONDEXLogger log = new ONDEXLogger();

    @Override
    protected void setUp() throws Exception {
        DirUtils.deleteTree(dbdir);
        File dir = new File(dbdir);
        dir.mkdir();
        assertTrue(dir.canRead());
        assertTrue(dir.canWrite());
        penv = new PerstEnv(session, dbdir + File.separator + "ondex.dbs", log);
        assertNotNull(penv);
        assertNotNull(penv.getONDEXGraph());
        og = new IndexONDEXGraph(session, penv.getONDEXGraph());
        assertNotNull(og);
        omd = og.getONDEXGraphData(session);
        assertNotNull(omd);
        penv.getONDEXGraph().addONDEXGraphListener(log);
        og.getPermissions().setAllCreate(session, true);
        og.getPermissions().setAllDelete(session, true);
        og.getPermissions().setAllGet(session, true);
        og.getPermissions().setAllUpdate(session, true);
        super.parseRelationDataFromXML();
        penv.commit();
    }

    protected void tearDown() throws Exception {
        penv.cleanup();
        penv = null;
        DirUtils.deleteTree(dbdir);
    }

    public void debugPrintConcepts() {
        System.out.println("Concepts " + og.getConcepts(session).size());
        Iterator<AbstractConcept> itC = og.getConcepts(session);
        while (itC.hasNext()) {
            AbstractConcept ac = itC.next();
            System.out.println("Names for concept = " + ac.getConceptNames(session).size());
            if (ac.getConceptName(session) != null) System.out.println("Primary ConceptName " + ac.getConceptName(session).getName(session)); else System.out.println("Null ConceptName");
            System.out.println(ac.getOfType(session).getName(session));
        }
        System.out.println("Relations " + og.getRelations(session).size());
        Iterator<AbstractRelation> itR = og.getRelations(session);
        while (itR.hasNext()) {
            AbstractRelation ar = itR.next();
            System.out.println("Abstract Realation" + ar.getId(session));
        }
    }

    public void testPersistanceOfDataOnClose() throws Exception {
        String NAME = "name";
        String ANNO = "anno";
        String DESC = "desc";
        String ACCESS = "access";
        String FROMCONCEPT = "from concept";
        String TOCONCEPT = "from concept";
        String GDSS = "gdss";
        String EVIDENCE = "evidence";
        long attributeNames = omd.getAttributeNames(session).size();
        long conceptClasesNames = omd.getConceptClasses(session).size();
        long CVs = omd.getCVs(session).size();
        long ETs = omd.getEvidenceTypes(session).size();
        long RTs = omd.getRelationTypes(session).size();
        long RTSs = omd.getRelationTypeSets(session).size();
        AbstractONDEXIterator<AbstractConcept> concepts = og.getConcepts(session);
        long noOfConcepts = concepts.size();
        HashMap<AbstractConcept, HashMap<String, Object>> conceptsObjs = new HashMap<AbstractConcept, HashMap<String, Object>>();
        while (concepts.hasNext()) {
            HashMap<String, Object> objs = new HashMap<String, Object>();
            AbstractConcept concept = concepts.next();
            objs.put(GDSS, concept.getConceptGDSs(session));
            objs.put(NAME, concept.getAnnotation(session));
            objs.put(ANNO, concept.getConceptName(session));
            objs.put(EVIDENCE, concept.getEvidence(session));
            objs.put(ACCESS, concept.getConceptAccessions(session));
            objs.put(DESC, concept.getDescription(session));
            conceptsObjs.put(concept, objs);
        }
        AbstractONDEXIterator<AbstractRelation> rels = og.getRelations(session);
        long noOfRelations = rels.size();
        HashMap<AbstractRelation, HashMap<String, Object>> relationsObjs = new HashMap<AbstractRelation, HashMap<String, Object>>();
        while (rels.hasNext()) {
            HashMap<String, Object> objs = new HashMap<String, Object>();
            AbstractRelation rel = rels.next();
            objs.put(FROMCONCEPT, rel.getFromConcept(session));
            objs.put(TOCONCEPT, rel.getToConcept(session));
            objs.put(GDSS, rel.getRelationGDSs(session).toCollection());
            objs.put(EVIDENCE, rel.getEvidence(session));
            relationsObjs.put(rel, objs);
        }
        penv.cleanup();
        penv = null;
        penv = new PerstEnv(session, dbdir + File.separator + "ondex.dbs", log);
        assertNotNull(penv);
        assertNotNull(penv.getONDEXGraph());
        og = new IndexONDEXGraph(session, penv.getONDEXGraph());
        assertNotNull(og);
        omd = og.getONDEXGraphData(session);
        assertNotNull(omd);
        assertEquals(attributeNames, omd.getAttributeNames(session).size());
        assertEquals(conceptClasesNames, omd.getConceptClasses(session).size());
        assertEquals(CVs, omd.getCVs(session).size());
        assertEquals(ETs, omd.getEvidenceTypes(session).size());
        assertEquals(RTs, omd.getRelationTypes(session).size());
        assertEquals(RTSs, omd.getRelationTypeSets(session).size());
        AbstractONDEXIterator<AbstractConcept> conceptsAfter = og.getConcepts(session);
        assertEquals(noOfConcepts, conceptsAfter.size());
        while (conceptsAfter.hasNext()) {
            AbstractConcept rel = conceptsAfter.next();
            HashMap<String, Object> conObjects = relationsObjs.get(rel);
            assertEquals(conObjects.get(EVIDENCE), rel.getEvidence(session));
            assertEquals(conObjects.get(GDSS), rel.getConceptGDSs(session));
            assertEquals(conObjects.get(NAME), rel.getConceptName(session));
            assertEquals(conObjects.get(ANNO), rel.getAnnotation(session));
            assertEquals(conObjects.get(ACCESS), rel.getConceptAccessions(session));
            assertEquals(conObjects.get(DESC), rel.getDescription(session));
        }
        assertEquals(noOfRelations, og.getRelations(session).size());
        AbstractONDEXIterator<AbstractRelation> relsAfter = og.getRelations(session);
        assertEquals(noOfRelations, relsAfter.size());
        while (relsAfter.hasNext()) {
            AbstractRelation rel = relsAfter.next();
            HashMap<String, Object> relObjects = relationsObjs.get(rel);
            assertEquals(relObjects.get(FROMCONCEPT), rel.getFromConcept(session));
            assertEquals(relObjects.get(TOCONCEPT), rel.getToConcept(session));
            assertEquals(relObjects.get(GDSS), rel.getRelationGDSs(session));
            assertEquals(relObjects.get(EVIDENCE), rel.getEvidence(session));
        }
    }
}
