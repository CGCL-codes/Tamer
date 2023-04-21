package backend.mapping.inparanoid2;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractRelation;
import backend.core.AbstractRelationTypeSet;
import backend.core.AttributeName;
import backend.core.ConceptClass;
import backend.core.EvidenceType;
import backend.core.ONDEXView;
import backend.core.RelationType;
import backend.core.security.Session;
import backend.event.type.EventType;
import backend.event.type.StatisticalOutput;
import backend.mapping.AbstractONDEXMapping;
import backend.mapping.args.SequenceAlignmentProgramArgumentDefinition;
import backend.mapping.inparanoid2.clustering.Inparalog;
import backend.mapping.inparanoid2.clustering.InparalogParser;
import backend.mapping.inparanoid2.clustering.MergeGroups;
import backend.mapping.inparanoid2.clustering.OndexMatch;
import backend.mapping.inparanoid2.clustering.Ortholog;
import backend.mapping.inparanoid2.clustering.OrthologParser;
import backend.param.args.ArgumentDefinition;
import backend.param.args.generic.FileArgumentDefinition;
import backend.param.args.generic.FloatRangeArgumentDefinition;
import backend.param.args.generic.IntegerRangeArgumentDefinition;
import backend.param.args.generic.SequenceTypeArgumentDefinition;
import backend.query.advanced.AdvancedQueryArguments;
import backend.query.advanced.output.Match;
import backend.query.advanced.util.FastaWriter;
import backend.query.advanced.util.SeqUtil;

/**
 * Implements the INPARANOID algorithm as a mapping 
 * method for the ONDEX system.
 * 
 * @author taubertj
 * 
 */
public class Mapping extends AbstractONDEXMapping {

    private AttributeName conf_an;

    private AbstractRelationTypeSet ipara_typeSet;

    private AbstractRelationTypeSet ortho_typeSet;

    private RelationType ipara_type;

    private RelationType ortho_type;

    private EvidenceType et;

    private ConceptClass ccProtein;

    private int cutoff;

    private float overlap;

    private Float evalue;

    private String sequenceType;

    private AbstractONDEXGraph graph;

    private String outputDir;

    private String program;

    private String programDir;

    public static boolean DEBUG = false;

    private static Mapping instance;

    /**
	 * Constructor with session context.
	 * 
	 * @param s - Session
	 */
    public Mapping(Session s) {
        super(s);
    }

    public String getName() {
        return new String("Inparanoid");
    }

    public String getVersion() {
        return new String("10.08.2006");
    }

    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        ArrayList<ArgumentDefinition<?>> args = new ArrayList<ArgumentDefinition<?>>();
        args.add(new FloatRangeArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.E_VALUE_ARG, ArgumentNames.E_VALUE_DESC, true, 0.000001F, Float.MIN_VALUE, Float.MAX_VALUE));
        args.add(new SequenceTypeArgumentDefinition(ArgumentNames.SEQ_TYPES_ARG, true, MetaData.atAA));
        args.add(new FileArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.PROGRAM_DIR_ARG, backend.mapping.genericblast.args.ArgumentNames.PROGRAM_DIR_DESC, true, true, true));
        args.add(new FloatRangeArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.OVERLAP_ARG, backend.mapping.genericblast.args.ArgumentNames.OVERLAP_DESC, false, 0.5F, 0, 1));
        args.add(new IntegerRangeArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.CUTOFF_ARG, false, 30, 0, Integer.MAX_VALUE));
        args.add(new IntegerRangeArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.PROCESSORS_ARG, false, 1, 1, Integer.MAX_VALUE));
        args.add(new SequenceAlignmentProgramArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.SEQ_ALIGNMENT_PROG_ARG, true, SequenceAlignmentProgramArgumentDefinition.BLAST));
        args.add(new IntegerRangeArgumentDefinition(backend.mapping.genericblast.args.ArgumentNames.PHMEMORY_ARG, backend.mapping.genericblast.args.ArgumentNames.PHMEMORY_ARG_DESC, false, 500, 0, Integer.MAX_VALUE));
        args.add(new IntegerRangeArgumentDefinition(ArgumentNames.MIN_SEQUENCE_LENGTH_QUERY_SEQUENCES, ArgumentNames.MIN_SEQUENCE_LENGTH_QUERY_SEQUENCES_DESC, false, 6, 0, Integer.MAX_VALUE));
        IntegerRangeArgumentDefinition taxids = new IntegerRangeArgumentDefinition(ArgumentNames.TAXIDS_ARG, ArgumentNames.TAXIDS_ARG_DESC, false, null, 0, Integer.MAX_VALUE);
        taxids.setCanHaveMultipleInstances(true);
        args.add(taxids);
        return args.toArray(new ArgumentDefinition<?>[args.size()]);
    }

    /**
	 * initializes global variables
	 */
    private void init() {
        evalue = (Float) ma.getUniqueValue(ArgumentNames.E_VALUE_ARG);
        cutoff = ((Integer) ma.getUniqueValue(backend.mapping.genericblast.args.ArgumentNames.CUTOFF_ARG)).intValue();
        overlap = ((Float) ma.getUniqueValue(backend.mapping.genericblast.args.ArgumentNames.OVERLAP_ARG)).floatValue();
        sequenceType = (String) ma.getUniqueValue(ArgumentNames.SEQ_TYPES_ARG);
        et = graph.getONDEXGraphData(s).getEvidenceType(s, MetaData.etINPARANOID);
        if (et == null) {
            et = graph.getONDEXGraphData(s).createEvidenceType(s, MetaData.etINPARANOID);
        }
        ortho_type = graph.getONDEXGraphData(s).getRelationType(s, MetaData.rtOrtolog);
        if (ortho_type == null) {
            ortho_type = graph.getONDEXGraphData(s).createRelationType(s, MetaData.rtOrtolog);
        }
        ipara_type = graph.getONDEXGraphData(s).getRelationType(s, MetaData.rtIPara);
        if (ipara_type == null) {
            ipara_type = graph.getONDEXGraphData(s).createRelationType(s, MetaData.rtIPara);
        }
        ortho_typeSet = graph.getONDEXGraphData(s).getRelationTypeSet(s, MetaData.rtsOrtolog);
        if (ortho_typeSet == null) {
            ortho_typeSet = graph.getONDEXGraphData(s).createRelationTypeSet(s, MetaData.rtsOrtolog, ortho_type);
        }
        ipara_typeSet = graph.getONDEXGraphData(s).getRelationTypeSet(s, MetaData.rtsIPara);
        if (ipara_typeSet == null) {
            ipara_typeSet = graph.getONDEXGraphData(s).createRelationTypeSet(s, MetaData.rtsIPara, ipara_type);
        }
        conf_an = graph.getONDEXGraphData(s).getAttributeName(s, MetaData.atCONF);
        if (conf_an == null) {
            conf_an = graph.getONDEXGraphData(s).createAttributeName(s, MetaData.atCONF, Double.class);
        }
        ccProtein = graph.getONDEXGraphData(s).getConceptClass(s, MetaData.ccProtein);
        outputDir = System.getProperty("ondex.dir") + File.separator + "seqs" + File.separator;
        program = (String) ma.getUniqueValue(backend.mapping.genericblast.args.ArgumentNames.SEQ_ALIGNMENT_PROG_ARG);
        programDir = (String) ma.getUniqueValue(backend.mapping.genericblast.args.ArgumentNames.PROGRAM_DIR_ARG);
        instance = this;
    }

    @Override
    public void setONDEXGraph(AbstractONDEXGraph graph) {
        this.graph = graph;
        init();
        String[] taxIds = getTaxIds();
        final HashMap<String, Vector<String[]>> taxIdToSeq = getTaxIDToSeq(taxIds);
        HashSet<String> foundInParalogs = new HashSet<String>();
        HashSet<String> foundOrthologs = new HashSet<String>();
        String[] allTaxIdsSizeSorted = taxIdToSeq.keySet().toArray(new String[taxIdToSeq.keySet().size()]);
        Arrays.sort(allTaxIdsSizeSorted, new Comparator<String>() {

            public int compare(String o1, String o2) {
                int thisVal = taxIdToSeq.get(o1).size();
                int anotherVal = taxIdToSeq.get(o2).size();
                return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
            }
        });
        System.out.println("Finding self matches");
        HashMap<String, Map<String, ArrayList<Match>>> selfMatches = new HashMap<String, Map<String, ArrayList<Match>>>();
        for (int i = 0; i < allTaxIdsSizeSorted.length; i++) {
            String taxIdSource = allTaxIdsSizeSorted[i];
            Vector<String[]> sourceSeq = taxIdToSeq.get(taxIdSource);
            if (sourceSeq.size() > 1) {
                selfMatches.put(taxIdSource, getMatches(sourceSeq, 1000));
            }
        }
        System.out.println("Finding cross matches");
        for (int i = 0; i < allTaxIdsSizeSorted.length; i++) {
            String taxIdSource = allTaxIdsSizeSorted[i];
            Vector<String[]> sourceSeq = taxIdToSeq.get(taxIdSource);
            for (int j = allTaxIdsSizeSorted.length - 1; j >= 0; j--) {
                String taxIdTarget = allTaxIdsSizeSorted[j];
                if (j < i) continue;
                Vector<String[]> targetSeq = taxIdToSeq.get(taxIdTarget);
                Vector<String[]> dbseq = new Vector<String[]>();
                dbseq.addAll(sourceSeq);
                dbseq.addAll(targetSeq);
                Map<String, ArrayList<Match>> matches = getMatches(dbseq, 1000);
                Map<String, ArrayList<Match>> sourceSelf = selfMatches.get(taxIdSource);
                if (sourceSelf != null) {
                    appendMatches(matches, sourceSelf);
                }
                Map<String, ArrayList<Match>> targetSelf = selfMatches.get(taxIdTarget);
                if (targetSelf != null) {
                    appendMatches(matches, targetSelf);
                }
                Int2ObjectOpenHashMap<ArrayList<OndexMatch>> processedMatches = convertMatches(matches, cutoff);
                System.out.println("Queries with hits passed to inparanoid " + processedMatches.size());
                Int2ObjectOpenHashMap<ArrayList<Ortholog>> cidToOrtho = processResults(processedMatches);
                IntIterator it2 = cidToOrtho.keySet().iterator();
                while (it2.hasNext()) {
                    int aIParacid = it2.next();
                    AbstractConcept conceptA = graph.getConcept(s, aIParacid);
                    Iterator<Ortholog> orthosIt = cidToOrtho.get(aIParacid).iterator();
                    while (orthosIt.hasNext()) {
                        Ortholog ortho = orthosIt.next();
                        Integer mainB = ortho.getMainB().getConceptId();
                        AbstractConcept conceptB = graph.getConcept(s, mainB);
                        String orthoKey = conceptA.getId(s) + "_" + conceptB.getId(s);
                        if (foundOrthologs.contains(orthoKey)) {
                            foundOrthologs.add(orthoKey);
                            foundOrthologs.add(conceptB.getId(s) + "_" + conceptA.getId(s));
                            AbstractRelation r = graph.createRelation(s, conceptA, conceptB, ortho_typeSet, et);
                            r.createRelationGDS(s, conf_an, Double.valueOf(ortho.getScore()), false);
                            r = graph.createRelation(s, conceptB, conceptA, ortho_typeSet, et);
                            r.createRelationGDS(s, conf_an, Double.valueOf(ortho.getScore()), false);
                        }
                        Iterator<Inparalog> it3 = ortho.getInA().iterator();
                        AbstractRelation r;
                        while (it3.hasNext()) {
                            Inparalog ipara = it3.next();
                            Integer cid = ipara.getConceptId();
                            String iparalogKey = cid + "_" + conceptA.getId(s);
                            if (!foundInParalogs.contains(iparalogKey)) {
                                foundInParalogs.add(iparalogKey);
                                AbstractConcept c = graph.getConcept(s, cid);
                                r = graph.createRelation(s, c, conceptA, ipara_typeSet, et);
                                r.createRelationGDS(s, conf_an, Double.valueOf(ipara.getConfidence()), false);
                            }
                        }
                        it3 = ortho.getInB().iterator();
                        while (it3.hasNext()) {
                            Inparalog ipara = it3.next();
                            Integer cid = ipara.getConceptId();
                            String iparalogKey = cid + "_" + conceptB.getId(s);
                            if (!foundInParalogs.contains(iparalogKey)) {
                                foundInParalogs.add(iparalogKey);
                                AbstractConcept c = graph.getConcept(s, cid);
                                r = graph.createRelation(s, c, conceptB, ipara_typeSet, et);
                                r.createRelationGDS(s, conf_an, Double.valueOf(ipara.getConfidence()), false);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * merges all items in match2 into match1
	 * @param match1 the target matches
	 * @param match2 the matches to add to the target
	 */
    public void appendMatches(Map<String, ArrayList<Match>> match1, Map<String, ArrayList<Match>> match2) {
        Iterator<String> matchIt = match2.keySet().iterator();
        while (matchIt.hasNext()) {
            String match = matchIt.next();
            ArrayList<Match> targetArray = match1.get(match);
            if (targetArray == null) {
                match1.put(match, match2.get(match));
            } else {
                targetArray.addAll(match2.get(match));
            }
        }
    }

    /**
	 * converts matches to a slimed down OndexMatch to use less space and adds any missing self matches that may have occured as a result blasting
	 * @param matches the original matches returned from an AdvancedQuery
	 * @return the slimed down hash set with reverse matches added
	 */
    private static Int2ObjectOpenHashMap<ArrayList<OndexMatch>> convertMatches(Map<String, ArrayList<Match>> matches, int cutoff) {
        Int2ObjectOpenHashMap<ArrayList<OndexMatch>> unsortedOndexMatch = new Int2ObjectOpenHashMap<ArrayList<OndexMatch>>(matches.size());
        Iterator<String> strIdIt = matches.keySet().iterator();
        while (strIdIt.hasNext()) {
            String id = strIdIt.next();
            Iterator<Match> matchIt = matches.get(id).iterator();
            while (matchIt.hasNext()) {
                Match m = matchIt.next();
                if (m.getQueryTaxId() == null) {
                    System.err.println("TAX ID NULL FOR QUERY " + m.getQueryId());
                    continue;
                }
                int qid = Integer.parseInt(m.getQueryId());
                OndexMatch om = new OndexMatch(qid, m.getQueryTaxId(), m.getTargetId(), m.getTargetTaxId(), m.getScore());
                ArrayList<OndexMatch> array = unsortedOndexMatch.get(qid);
                if (array == null) {
                    array = new ArrayList<OndexMatch>(100);
                    unsortedOndexMatch.put(qid, array);
                }
                array.add(om);
            }
        }
        HashMap<Integer, Boolean> selfMatches = new HashMap<Integer, Boolean>(1000);
        HashMap<Integer, Integer> idToTaxId = new HashMap<Integer, Integer>(unsortedOndexMatch.size());
        Iterator<ArrayList<OndexMatch>> matIt = unsortedOndexMatch.values().iterator();
        while (matIt.hasNext()) {
            Iterator<OndexMatch> matchs = matIt.next().iterator();
            while (matchs.hasNext()) {
                OndexMatch match = matchs.next();
                Boolean selfmatch = false;
                if (match.getQuery() == match.getTarget()) {
                    selfmatch = true;
                }
                Boolean alreadySelfMatchQuery = selfMatches.get(match.getQuery());
                if (alreadySelfMatchQuery == null) {
                    selfMatches.put(match.getQuery(), selfmatch);
                    idToTaxId.put(match.getQuery(), match.getQueryTaxId());
                } else if (!alreadySelfMatchQuery && selfmatch) {
                    selfMatches.put(match.getQuery(), selfmatch);
                }
                Boolean alreadySelfMatchTarget = selfMatches.get(match.getTarget());
                if (alreadySelfMatchTarget == null) {
                    selfMatches.put(match.getTarget(), selfmatch);
                    idToTaxId.put(match.getTarget(), match.getTargetTaxId());
                } else if (!alreadySelfMatchTarget && selfmatch) {
                    selfMatches.put(match.getTarget(), selfmatch);
                }
            }
        }
        int selfMatchesAdded = 0;
        Iterator<Integer> selfMatchesIt = selfMatches.keySet().iterator();
        while (selfMatchesIt.hasNext()) {
            Integer match = selfMatchesIt.next();
            if (!selfMatches.get(match)) {
                Integer taxId = idToTaxId.get(match);
                OndexMatch newMatch = new OndexMatch(match, taxId, match, taxId, cutoff);
                ArrayList<OndexMatch> array = unsortedOndexMatch.get(match);
                if (array == null) {
                    array = new ArrayList<OndexMatch>(100);
                    unsortedOndexMatch.put(match, array);
                }
                array.add(newMatch);
                selfMatchesAdded++;
            }
        }
        selfMatches = null;
        idToTaxId = null;
        return unsortedOndexMatch;
    }

    /**
	 * Converts unsorted matches to ortholog results based on the INPARANOUID algoritm
	 * @param unsortedOndexMatches the slimed down ondex matches 
	 * @return a map of conceptids to orthologs
	 * @see #convertMatches(Map<String, ArrayList<Match>> matches)
	 * @see Ortholog
	 */
    private Int2ObjectOpenHashMap<ArrayList<Ortholog>> processResults(Int2ObjectOpenHashMap<ArrayList<OndexMatch>> unsortedOndexMatches) {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>>> taxidToMatches = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>>>(2);
        int forwardMatchesAdded = 0;
        int backwardMatchesAdded = 0;
        ObjectIterator<ArrayList<OndexMatch>> it = unsortedOndexMatches.values().iterator();
        while (it.hasNext()) {
            Iterator<OndexMatch> matchs = it.next().iterator();
            while (matchs.hasNext()) {
                OndexMatch match = matchs.next();
                if (!taxidToMatches.containsKey(match.getQueryTaxId())) {
                    taxidToMatches.put(match.getQueryTaxId(), new HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>>());
                }
                HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>> queryTaxids = taxidToMatches.get(match.getQueryTaxId());
                if (!queryTaxids.containsKey(match.getTargetTaxId())) {
                    queryTaxids.put(match.getTargetTaxId(), new HashMap<Integer, HashMap<Integer, OndexMatch>>());
                }
                HashMap<Integer, HashMap<Integer, OndexMatch>> targetTaxids = queryTaxids.get(match.getTargetTaxId());
                if (!targetTaxids.containsKey(match.getQuery())) {
                    targetTaxids.put(match.getQuery(), new HashMap<Integer, OndexMatch>());
                }
                HashMap<Integer, OndexMatch> matchQuery = targetTaxids.get(match.getQuery());
                forwardMatchesAdded++;
                matchQuery.put(match.getTarget(), match);
                if (!taxidToMatches.containsKey(match.getTargetTaxId())) {
                    taxidToMatches.put(match.getTargetTaxId(), new HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>>());
                }
                HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>> taxidTarget = taxidToMatches.get(match.getTargetTaxId());
                if (!taxidTarget.containsKey(match.getQueryTaxId())) {
                    taxidTarget.put(match.getQueryTaxId(), new HashMap<Integer, HashMap<Integer, OndexMatch>>());
                }
                HashMap<Integer, HashMap<Integer, OndexMatch>> taxidQuery = taxidTarget.get(match.getQueryTaxId());
                if (!taxidQuery.containsKey(match.getTarget())) {
                    taxidQuery.put(match.getTarget(), new HashMap<Integer, OndexMatch>());
                }
                HashMap<Integer, OndexMatch> matchTarget = taxidQuery.get(match.getTarget());
                if (!matchTarget.containsKey(match.getQuery())) {
                    OndexMatch newMatch = new OndexMatch(match.getTarget(), match.getTargetTaxId(), match.getQuery(), match.getQueryTaxId(), cutoff);
                    matchTarget.put(match.getQuery(), newMatch);
                    backwardMatchesAdded++;
                }
            }
            it.remove();
        }
        unsortedOndexMatches = null;
        fireEventOccurred(new StatisticalOutput("Taxids found: " + taxidToMatches.keySet().size()));
        fireEventOccurred(new StatisticalOutput("Forward Matches Indexed: " + forwardMatchesAdded));
        fireEventOccurred(new StatisticalOutput("Backward Matches Indexed: " + backwardMatchesAdded));
        return runINPARANOID(taxidToMatches, graph, cutoff);
    }

    /**
	 * Gets the taxid restrictions for this run
	 * @return taxid restrictions for this run
	 */
    private String[] getTaxIds() {
        List<Object> taxids = ma.getObjectValueList(ArgumentNames.TAXIDS_ARG);
        if (taxids != null) {
            String[] taxIds = new String[taxids.size()];
            for (int i = 0; i < taxids.size(); i++) {
                Object object = taxids.get(i);
                taxIds[i] = String.valueOf((Integer) object);
            }
            return taxIds;
        }
        return null;
    }

    /**
	 * Gets a map of taxid to sequences via the SeqUtil class
	 * @param taxIds the taxids to get null object here gets all possible
	 * @return taxid to sequences
	 * @see SeqUtil#getSequencesWithTaxIDs(Session, AbstractONDEXGraph, ONDEXView, String, int, boolean, String[])
	 */
    private HashMap<String, Vector<String[]>> getTaxIDToSeq(String[] taxIds) {
        AttributeName aname = graph.getONDEXGraphData(s).getAttributeName(s, sequenceType);
        ONDEXView<AbstractConcept> aaConcepts = graph.getConceptsOfAttributeName(s, aname);
        ONDEXView<AbstractConcept> proteinConcepts = graph.getConceptsOfConceptClass(s, ccProtein);
        ONDEXView<AbstractConcept> concepts = ONDEXView.and(aaConcepts, proteinConcepts);
        aaConcepts.close();
        proteinConcepts.close();
        HashMap<String, Vector<String[]>> taxIdToSeq = new HashMap<String, Vector<String[]>>();
        Vector<String[]> dbSeqs = null;
        int minimalQSeqLength = ((Integer) ma.getUniqueValue(ArgumentNames.MIN_SEQUENCE_LENGTH_QUERY_SEQUENCES)).intValue();
        if (taxIds == null || taxIds.length == 0) {
            dbSeqs = SeqUtil.getSequencesWithTaxID(s, graph, concepts, sequenceType, minimalQSeqLength, true);
        } else {
            dbSeqs = SeqUtil.getSequencesWithTaxIDs(s, graph, concepts, sequenceType, minimalQSeqLength, true, taxIds);
        }
        Iterator<String[]> seqIt = dbSeqs.iterator();
        while (seqIt.hasNext()) {
            String[] seq = seqIt.next();
            String taxId = seq[SeqUtil.TAXID_LOC];
            Vector<String[]> list = taxIdToSeq.get(taxId);
            if (list == null) {
                list = new Vector<String[]>();
                taxIdToSeq.put(taxId, list);
            }
            list.add(seq);
        }
        return taxIdToSeq;
    }

    private int orthologGroups = 0;

    /**
	 * The original INPARANOID algorithm
	 * @param taxidToMatches 
	 * @param graph
	 * @param cutoff
	 * @return conceptid to orthologs
	 * @see Ortholog
	 */
    private Int2ObjectOpenHashMap<ArrayList<Ortholog>> runINPARANOID(HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, OndexMatch>>>> taxidToMatches, AbstractONDEXGraph graph, int cutoff) {
        Int2ObjectOpenHashMap<ArrayList<Ortholog>> cidToOrtho = new Int2ObjectOpenHashMap<ArrayList<Ortholog>>();
        System.out.println("Ortholog Parser");
        OrthologParser oparser = new OrthologParser();
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Ortholog>>>> orthologs = oparser.parse(taxidToMatches);
        System.out.println("InParalog Parser");
        InparalogParser iparser = new InparalogParser();
        ArrayList<Ortholog> orthos = iparser.parse(orthologs, taxidToMatches);
        iparser = null;
        orthologs = null;
        System.out.println("Merge Groups");
        MergeGroups mgroups = new MergeGroups(cutoff);
        orthos = mgroups.merge(orthos, taxidToMatches);
        mgroups = null;
        orthologGroups = orthologGroups + orthos.size();
        Iterator<Ortholog> it2 = orthos.iterator();
        while (it2.hasNext()) {
            Ortholog ortho = it2.next();
            ArrayList<Ortholog> orthologsOutOfConcept = cidToOrtho.get(ortho.getMainA().getConceptId());
            if (orthologsOutOfConcept == null) {
                orthologsOutOfConcept = new ArrayList<Ortholog>();
                cidToOrtho.put(ortho.getMainA().getConceptId(), orthologsOutOfConcept);
            }
            orthologsOutOfConcept.add(ortho);
        }
        return cidToOrtho;
    }

    public static void propgateEvent(EventType et) {
        if (instance != null) instance.fireEventOccurred(et);
    }

    public boolean requiresIndexedGraph() {
        return false;
    }

    /**
	 * runs a blast job in the 2 taxIds
	 * @param fromTaxid
	 * @param toTaxID
	 * @param taxIdToSeq
	 * @param matchesToReturnPerQuery
	 * @return
	 */
    private Map<String, ArrayList<Match>> getMatches(Vector<String[]> seqs, int matchesToReturnPerQuery) {
        Map<String, ArrayList<Match>> matches = null;
        if (program.equalsIgnoreCase(SequenceAlignmentProgramArgumentDefinition.DECYPHER)) {
            String time = new SimpleDateFormat("yyMMdd_HHmmss_SSS").format(new Date(System.currentTimeMillis()));
            File sequences = FastaWriter.writeDecypherWorkaroundFile(seqs, System.getProperty("ondex.dir") + File.separator + "seqs" + File.separator + "INPARA_SEQ_" + time);
            AdvancedQueryArguments sma = new AdvancedQueryArguments();
            sma.setOutputDir(outputDir);
            sma.setSearchObject(sequences);
            sma.setProgramDir(programDir);
            sma.addOption(backend.query.advanced.decypher.Args.BLAST_TYPE, backend.query.advanced.decypher.Args.blastp_arg);
            sma.addOption(backend.query.advanced.decypher.Args.EVALUE_ARG, evalue);
            sma.addOption(backend.query.advanced.decypher.Args.CUT_OFF_ARG, Integer.valueOf(cutoff));
            sma.addOption(backend.query.advanced.decypher.Args.OVERLAP_ARG, Float.valueOf(overlap));
            sma.addOption(backend.query.advanced.decypher.Args.QUERY_FILE, sequences);
            sma.addOption(backend.query.advanced.decypher.Args.TARGET_FILE, sequences);
            sma.addOption(backend.query.advanced.decypher.Args.MAX_RESULTS_PER_QUERY, Integer.valueOf(matchesToReturnPerQuery));
            backend.query.advanced.decypher.AdvancedQuery advancedQuery = new backend.query.advanced.decypher.AdvancedQuery(Session.NONE);
            advancedQuery.setAdvancedQueryArguments(sma);
            advancedQuery.setONDEXGraph(null, "", "");
            sequences.delete();
            matches = advancedQuery.getMatches();
        } else {
            throw new RuntimeException("Test implementation other programs not implemented yet");
        }
        return matches;
    }
}
