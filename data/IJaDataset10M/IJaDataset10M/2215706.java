package bioinfo.comaWebServer.dataServices;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import bioinfo.comaWebServer.entities.AlignmentBibliography;
import bioinfo.comaWebServer.entities.ComaResults;
import bioinfo.comaWebServer.entities.ResultsAlignment;
import bioinfo.comaWebServer.entities.RAlignmentFooter;
import bioinfo.comaWebServer.entities.RAlignmentHeader;
import bioinfo.comaWebServer.entities.ResultsFooter;
import bioinfo.comaWebServer.entities.ResultsHit;
import bioinfo.comaWebServer.entities.ResultsQuery;

public class ComaResultsParser {

    private static final String ALIGNMENT_BEGIN = "^>.*";

    private static final String END_OF_ALIGNMENTS = "^Scoring method:.+";

    private static final String HITS_BEGIN = "^\\sProfiles found below the e-value.*";

    private static final String NO_PROFILES_FOUND = "^\\sNo profiles found below the e-value.*";

    private static final int PRIORITY_BEGIN = 1;

    public static void main(String[] args) throws IOException {
        ComaResultsParser.parseIDS("tests/comaParser_003.ids");
    }

    public static int numberOfSeq(String path) throws IOException {
        ArrayList<String> data = readFile(path);
        Pattern pattern = Pattern.compile("^>.*");
        int counter = 0;
        for (String line : data) {
            Matcher m = pattern.matcher(line);
            if (m.matches()) {
                counter++;
            }
        }
        return counter;
    }

    public static void parse(ComaResults comaResults, String path) throws IOException {
        ArrayList<String> data = readFile(path);
        for (int i = 0; comaResults.getNoProfilesFound() == null && i < data.size(); i++) {
            if (data.get(i).matches(NO_PROFILES_FOUND)) {
                comaResults.setNoProfilesFound(data.get(i));
            }
        }
        parseHeader(data);
        if (comaResults.getNoProfilesFound() == null) {
            comaResults.setHits(parseHits(data));
            comaResults.setAlignments(parseAlignment(data));
        }
        comaResults.setResultsFooter(parseFooter(data));
    }

    private static Set<ResultsAlignment> parseAlignment(ArrayList<String> data) {
        Set<ResultsAlignment> alignments = new HashSet<ResultsAlignment>();
        int priority = PRIORITY_BEGIN;
        List<String> rawAlignment = getRawAlignment(data);
        while (rawAlignment != null) {
            ResultsAlignment resultsAlignment = new ResultsAlignment();
            resultsAlignment.setPriority(priority);
            resultsAlignment.setRheader(parseAlignmentHeader(rawAlignment));
            resultsAlignment.setRfooter(parseAlignmentFooter(rawAlignment));
            Pattern entropyPattern = Pattern.compile(".*Expected score per position is non-negative,\\s*([\\w/\\.\\-\\+]+).*");
            resultsAlignment.setNonNegativeScore(getValue(rawAlignment, entropyPattern));
            resultsAlignment.setQueries(parseResultsQuery(rawAlignment));
            alignments.add(resultsAlignment);
            priority++;
            rawAlignment = getRawAlignment(data);
        }
        if (alignments.size() == 0) {
            return null;
        }
        return alignments;
    }

    private static List<String> getRawAlignment(List<String> data) {
        List<String> rawAlignment = new ArrayList<String>();
        if (data.size() > 0 && data.get(0).matches(ALIGNMENT_BEGIN)) {
            rawAlignment.add(data.get(0));
            data.remove(0);
        } else {
            return null;
        }
        while (data.size() > 0 && !data.get(0).matches(ALIGNMENT_BEGIN) && !data.get(0).matches(END_OF_ALIGNMENTS)) {
            rawAlignment.add(data.get(0));
            data.remove(0);
        }
        return rawAlignment;
    }

    private static Set<ResultsQuery> parseResultsQuery(List<String> rawAlignment) {
        Set<ResultsQuery> list = new HashSet<ResultsQuery>();
        int priority = PRIORITY_BEGIN;
        ResultsQuery resultsQuery = parseQuery(rawAlignment);
        while (resultsQuery != null) {
            resultsQuery.setPriority(priority);
            list.add(resultsQuery);
            resultsQuery = parseQuery(rawAlignment);
            priority++;
        }
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    private static ResultsQuery parseQuery(List<String> rawAlignment) {
        ResultsQuery resultsQuery = null;
        while (rawAlignment.size() > 0 && !rawAlignment.get(0).matches("^\\s*Query:.*")) {
            rawAlignment.remove(0);
        }
        if (rawAlignment.size() > 0) {
            resultsQuery = new ResultsQuery();
            Pattern queryPattern = Pattern.compile(".*Query:\\s+(\\d+)\\s+([\\w\\-]+)\\s+(\\d+).*");
            Matcher queryMatcher = queryPattern.matcher(rawAlignment.get(0));
            if (queryMatcher.matches()) {
                resultsQuery.setQueryBegin(queryMatcher.group(1));
                resultsQuery.setQuery(queryMatcher.group(2));
                resultsQuery.setQueryEnd(queryMatcher.group(3));
                rawAlignment.remove(0);
                String inf = rawAlignment.get(0).substring(queryMatcher.start(2), queryMatcher.end(2));
                rawAlignment.remove(0);
                resultsQuery.setInfo(inf);
                Pattern subjectPattern = Pattern.compile(".*Sbjct:\\s+(\\d+)\\s+([\\w\\-]+)\\s+(\\d+).*");
                Matcher subjectMatcher = subjectPattern.matcher(rawAlignment.get(0));
                if (subjectMatcher.matches()) {
                    resultsQuery.setSubjectBegin(subjectMatcher.group(1));
                    resultsQuery.setSubject(subjectMatcher.group(2));
                    resultsQuery.setSubjectEnd(subjectMatcher.group(3));
                    rawAlignment.remove(0);
                }
            }
        }
        return resultsQuery;
    }

    private static RAlignmentFooter parseAlignmentFooter(List<String> rawAlignment) {
        boolean found = false;
        RAlignmentFooter rfooter = new RAlignmentFooter();
        Pattern computedPattern = Pattern.compile(".*Computed\\s+ungapped,\\s*([0-9\\.eE\\-\\+]+)\\s*([0-9\\.eE\\-\\+]+).*");
        for (String data : rawAlignment) {
            Matcher m = computedPattern.matcher(data);
            if (m.matches()) {
                found = true;
                rfooter.setUngappedK(m.group(1));
                rfooter.setUngappedLambda(m.group(2));
            }
        }
        Pattern estimatedPattern = Pattern.compile(".*Estimated\\s+gapped,\\s*([0-9\\.eE\\-\\+]+)\\s*([0-9\\.eE\\-\\+]+).*");
        for (String data : rawAlignment) {
            Matcher m = estimatedPattern.matcher(data);
            if (m.matches()) {
                found = true;
                rfooter.setGappedK(m.group(1));
                rfooter.setGappedLambda(m.group(2));
            }
        }
        Pattern entropyPattern = Pattern.compile(".*Entropy,\\s*([\\w/\\.\\-\\+]+).*");
        rfooter.setEntropy(getValue(rawAlignment, entropyPattern));
        Pattern expectedPattern = Pattern.compile(".*Expected,\\s*([\\w/\\.\\-\\+]+).*");
        rfooter.setExpected(getValue(rawAlignment, expectedPattern));
        Pattern minmaxPattern = Pattern.compile(".*Min/Max,\\s*([\\w/\\.\\-\\+]+).*");
        rfooter.setMinMax(getValue(rawAlignment, minmaxPattern));
        if (!found) return null;
        return rfooter;
    }

    private static RAlignmentHeader parseAlignmentHeader(List<String> rawAlignment) {
        RAlignmentHeader rheader = null;
        if (rawAlignment.get(0).matches(ALIGNMENT_BEGIN)) {
            rheader = new RAlignmentHeader();
            StringBuffer header = new StringBuffer();
            while (rawAlignment.size() > 0 && !rawAlignment.get(0).matches("^\\s*Query length.*")) {
                header.append(rawAlignment.get(0));
                rawAlignment.remove(0);
            }
            rheader.setHeader(header.toString());
            Pattern queryLengthPattern = Pattern.compile(".*Query\\s*length\\s*=\\s*(\\d+).*");
            rheader.setQueryLength(getValue(rawAlignment, queryLengthPattern));
            Pattern sbjctLengthPattern = Pattern.compile(".*Sbjct\\s*length\\s*=\\s*(\\d+).*");
            rheader.setSubjectLength(getValue(rawAlignment, sbjctLengthPattern));
            Pattern scorePattern = Pattern.compile(".*Score\\s*=\\s*([\\w/\\-\\+\\.]+\\s*\\([0-9\\.eE\\-\\+]+\\s+bits\\)).*");
            rheader.setScore(getValue(rawAlignment, scorePattern));
            Pattern expectPattern = Pattern.compile(".*Expect\\s*=\\s*([\\w/\\-\\+\\.]+).*");
            rheader.setExpect(getValue(rawAlignment, expectPattern));
            Pattern pvaluePattern = Pattern.compile(".*P\\-value\\s*=\\s*([\\w/\\-\\+\\.]+).*");
            rheader.setPvalue(getValue(rawAlignment, pvaluePattern));
            Pattern identitiesPattern = Pattern.compile(".*Identities\\s*=\\s*([\\w/]+\\s*\\(\\d+%\\)).*");
            rheader.setIdentities(getValue(rawAlignment, identitiesPattern));
            Pattern positivesPattern = Pattern.compile(".*Positives\\s*=\\s*([\\w/]+\\s*\\(\\d+%\\)).*");
            rheader.setPositives(getValue(rawAlignment, positivesPattern));
            Pattern gapsPattern = Pattern.compile(".*Gaps\\s*=\\s*([\\w/]+\\s*\\(\\d+%\\)).*");
            rheader.setGaps(getValue(rawAlignment, gapsPattern));
        }
        return rheader;
    }

    private static String getValue(List<String> rawAlignment, Pattern pattern) {
        String value = null;
        for (String data : rawAlignment) {
            Matcher m = pattern.matcher(data);
            if (m.matches()) {
                value = m.group(1);
            }
        }
        return value;
    }

    private static Set<ResultsHit> parseHits(ArrayList<String> data) {
        Set<ResultsHit> hits = new HashSet<ResultsHit>();
        int priority = PRIORITY_BEGIN;
        while (data.size() > 0 && !data.get(0).matches(ALIGNMENT_BEGIN)) {
            ResultsHit hit = new ResultsHit();
            String hitInfo = data.get(0);
            data.remove(0);
            hit.setName(hitInfo.substring(0, 76));
            hit.setScore(hitInfo.substring(76, 82).replaceFirst("^\\s*", ""));
            hit.setEvalue(hitInfo.substring(82, hitInfo.length()).replaceFirst("^\\s*", ""));
            hit.setPriority(priority);
            hits.add(hit);
            priority++;
        }
        return hits;
    }

    private static void parseHeader(ArrayList<String> data) {
        while (data.size() > 0 && !data.get(0).matches(HITS_BEGIN) && !data.get(0).matches(NO_PROFILES_FOUND)) {
            data.remove(0);
        }
        if (data.size() > 0) {
            data.remove(0);
        }
    }

    private static ResultsFooter parseFooter(ArrayList<String> data) {
        ResultsFooter resultsFooter = null;
        while (data.size() > 0 && !data.get(0).matches(END_OF_ALIGNMENTS)) {
            data.remove(0);
        }
        if (data.size() > 0) {
            resultsFooter = new ResultsFooter();
            Pattern scoringmethodPattern = Pattern.compile(".*Scoring method:\\s*(.*)$");
            resultsFooter.setScoringMethod(getValue(data, scoringmethodPattern));
            Pattern gapOpenCostPattern = Pattern.compile(".*Gap open cost,\\s*(.*)$");
            resultsFooter.setGapOpenCost(getValue(data, gapOpenCostPattern));
            Pattern initialGapExtentionPattern = Pattern.compile(".*Initial gap extension cost,\\s*(.*)$");
            resultsFooter.setInitialGapExtensionCost(getValue(data, initialGapExtentionPattern));
            Pattern deletionProbabilityPattern = Pattern.compile(".*Deletion probability weight,\\s*(.*)$");
            resultsFooter.setDeletionProbabilityWeight(getValue(data, deletionProbabilityPattern));
            Pattern ungappedPattern = Pattern.compile(".*Ungapped\\s*([\\d\\.]+)\\s*([\\d\\.]+).*");
            for (String str : data) {
                Matcher m = ungappedPattern.matcher(str);
                if (m.matches()) {
                    resultsFooter.setUngappedK(m.group(1));
                    resultsFooter.setUngappedLambda(m.group(2));
                }
            }
            Pattern gappedPattern = Pattern.compile(".*Ungapped\\s*([\\d\\.]+)\\s*([\\d\\.]+).*");
            for (String str : data) {
                Matcher m = gappedPattern.matcher(str);
                if (m.matches()) {
                    resultsFooter.setGappedK(m.group(1));
                    resultsFooter.setGappedLambda(m.group(2));
                }
            }
            Pattern queryLengthPattern = Pattern.compile(".*Length of query,\\s*([\\d]+).*$");
            resultsFooter.setQueryLength(getValue(data, queryLengthPattern));
            Pattern databaseLengthPattern = Pattern.compile(".*Length of database,\\s*(.*)$");
            resultsFooter.setDatabaseLength(getValue(data, databaseLengthPattern));
            Pattern queryEfectiveLengthPattern = Pattern.compile(".*Effective length of query,\\s*(.*)$");
            resultsFooter.setQueryEffectiveLength(getValue(data, queryEfectiveLengthPattern));
            Pattern databaseEfectiveLengthPattern = Pattern.compile(".*Effective length of database,\\s*(.*)$");
            resultsFooter.setDatabaseEffectiveLength(getValue(data, databaseEfectiveLengthPattern));
            Pattern efectiveSearchSpacePattern = Pattern.compile(".*Effective search space,\\s*(.*)$");
            resultsFooter.setEffectiveSearchSpace(getValue(data, efectiveSearchSpacePattern));
        }
        return resultsFooter;
    }

    public static ArrayList<String> readFile(String path) throws IOException {
        ArrayList<String> data = new ArrayList<String>();
        FileInputStream fstream = new FileInputStream(path);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            if (!strLine.equals("")) {
                data.add(strLine);
            }
        }
        in.close();
        br.close();
        return data;
    }

    public static Map<String, AlignmentBibliography> parseIDS(String path) throws IOException {
        ArrayList<String> data = readFile(path);
        Map<String, AlignmentBibliography> info = new HashMap<String, AlignmentBibliography>();
        String delimiter = "\\s*GETIDS_EOID\\s*";
        Pattern descPattern = Pattern.compile("^\\s*desc:(.*)");
        Pattern pdbPattern = Pattern.compile("^\\s*pdb:(.*)");
        Pattern pfamPattern = Pattern.compile("^\\s*pfam:(.*)");
        Pattern scopPattern = Pattern.compile("^\\s*scop:(\\S+)\\s+(.*)");
        Pattern pubmedPattern = Pattern.compile("^\\s*pubmed:(.*)");
        for (String line : data) {
            String pdb = null;
            String scop = null;
            String pfam = null;
            String scopinfo = null;
            String pubmed = null;
            String description = null;
            String[] ids = line.split(delimiter);
            Matcher m = null;
            for (String id : ids) {
                m = descPattern.matcher(id);
                if (m.matches()) {
                    if (!isEmpty(m.group(1))) {
                        description = m.group(1);
                    }
                }
                m = pdbPattern.matcher(id);
                if (m.matches()) {
                    if (!isEmpty(m.group(1))) {
                        pdb = m.group(1);
                    }
                }
                m = pfamPattern.matcher(id);
                if (m.matches()) {
                    if (!isEmpty(m.group(1))) {
                        pfam = m.group(1);
                    }
                }
                m = scopPattern.matcher(id);
                if (m.matches()) {
                    if (!isEmpty(m.group(1))) {
                        scop = m.group(1);
                    }
                    scopinfo = m.group(2);
                }
                m = pubmedPattern.matcher(id);
                if (m.matches()) {
                    if (!isEmpty(m.group(1))) {
                        pubmed = m.group(1);
                    }
                }
            }
            info.put(description, new AlignmentBibliography(pdb, scop, scopinfo, pubmed, pfam));
        }
        return info;
    }

    private static boolean isEmpty(String line) {
        if (line.matches("^.*n/a.*$")) {
            return true;
        }
        return false;
    }
}
