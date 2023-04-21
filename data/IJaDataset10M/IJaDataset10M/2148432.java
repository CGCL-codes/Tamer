package backend.parser.tigrricefasta.genome;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractRelationTypeSet;
import backend.core.EvidenceType;
import backend.core.security.Session;
import backend.parser.tigrricefasta.genome.parseSequences.ParseGeneSequences;
import backend.parser.tigrricefasta.genome.parseSequences.ParseProteinSequences;

public class ParseGenome {

    private Session s;

    private AbstractRelationTypeSet rtSetEncodeBy;

    private EvidenceType etIMPD;

    public ParseGenome(Session s) {
        this.s = s;
    }

    public void parse(String inputDir, String[] files, AbstractONDEXGraph graph) {
        Object2ObjectOpenHashMap<String, AbstractConcept> genesMap = null;
        Object2ObjectOpenHashMap<String, AbstractConcept> proteinsMap = null;
        for (String fileName : files) {
            if (fileName.endsWith(".seq") || fileName.endsWith(".cds")) {
                ParseGeneSequences parseGeneSeqs = new ParseGeneSequences(s);
                if (genesMap == null) {
                    genesMap = parseGeneSeqs.Parse(graph, fileName, inputDir);
                } else {
                    genesMap.putAll(parseGeneSeqs.Parse(graph, fileName, inputDir));
                }
            } else if (fileName.endsWith(".pep")) {
                ParseProteinSequences parseProteinSeqs = new ParseProteinSequences(s);
                if (proteinsMap == null) {
                    proteinsMap = parseProteinSeqs.Parse(graph, fileName, inputDir);
                } else {
                    proteinsMap.putAll(parseProteinSeqs.Parse(graph, fileName, inputDir));
                }
            } else {
                System.out.println("File extension unknown, not able to parse file" + "(" + fileName + "). (use: seq/cds = NA & pep = AA))");
            }
        }
        if (proteinsMap != null && genesMap != null) {
            ObjectOpenHashSet<String> keys = new ObjectOpenHashSet<String>();
            keys.addAll(genesMap.keySet());
            keys.addAll(proteinsMap.keySet());
            if (rtSetEncodeBy == null) rtSetEncodeBy = graph.getONDEXGraphData(s).getRelationTypeSet(s, backend.parser.tigrricefasta.MetaData.encodedBy);
            if (etIMPD == null) etIMPD = graph.getONDEXGraphData(s).getEvidenceType(s, backend.parser.tigrricefasta.MetaData.IMPD);
            ObjectIterator<String> keysIt = keys.iterator();
            while (keysIt.hasNext()) {
                String accession = keysIt.next();
                if (genesMap.containsKey(accession) && proteinsMap.containsKey(accession)) {
                    graph.createRelation(s, proteinsMap.get(accession), genesMap.get(accession), rtSetEncodeBy, etIMPD);
                }
            }
        }
    }

    public static String chompVersion(String acc) {
        return acc.substring(0, acc.indexOf("."));
    }
}
