package it.polito.semreview.experiment;

import it.polito.semreview.classifiers.Classifier;
import it.polito.semreview.classifiers.KnowledgeBase;
import it.polito.semreview.classifiers.NaiveBayes;
import it.polito.semreview.dataset.PaperId;
import it.polito.semreview.dataset.PapersDirLoadingStrategy;
import it.polito.semreview.utils.filesystem.FileUtils;
import it.polito.softeng.common.Pair;
import it.polito.softeng.common.exceptions.LoadingException;
import it.polito.softeng.common.exceptions.UnknownElementException;
import it.polito.softeng.common.serialization.SerializationStorage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.csvreader.CsvReader;

public class ClassifierBatchStorer {

    private File enrichedDir;

    private File plainPapersDir;

    private static final String JOURNAL_NAME = "TSE";

    private static final String INTERESTING_LABEL = "interesting";

    public KnowledgeBase getModel(List<String> interestingPapers) {
        KnowledgeBase kb = new KnowledgeBase();
        for (String anInterestingPaper : interestingPapers) {
            kb.train(INTERESTING_LABEL, anInterestingPaper);
        }
        return kb;
    }

    private Classifier classifier;

    /**
	 * Create an instance which does not use enriched papers.
	 * 
	 * @param plainPapersDir
	 * @param csvInterestingPapers
	 * @throws IOException
	 */
    public ClassifierBatchStorer(File plainPapersDir, File csvInterestingPapers) throws IOException {
        logger.info("Plain dir " + plainPapersDir.getAbsolutePath());
        if (useEnrichedPapers()) {
            logger.info("Enriched dir " + enrichedDir.getAbsolutePath());
        }
        logger.info("CSV interesting paper " + csvInterestingPapers.getAbsolutePath());
        this.plainPapersDir = plainPapersDir;
        this.enrichedDir = null;
        this.csvInterestingPapers = csvInterestingPapers;
        calcInterestingPapers();
    }

    /**
	 * Create an instance which use enriched papers.
	 * 
	 * @param plainPapersDir
	 * @param csvInterestingPapers
	 * @throws IOException
	 */
    public ClassifierBatchStorer(File plainPapersDir, File enrichedDir, File csvInterestingPapers) throws IOException {
        logger.info("Plain dir " + plainPapersDir.getAbsolutePath());
        logger.info("Enriched dir " + enrichedDir.getAbsolutePath());
        logger.info("CSV interesting paper " + csvInterestingPapers.getAbsolutePath());
        this.plainPapersDir = plainPapersDir;
        this.enrichedDir = enrichedDir;
        this.csvInterestingPapers = csvInterestingPapers;
        calcInterestingPapers();
    }

    private File csvInterestingPapers;

    private void calcInterestingPapers() throws IOException {
        CsvReader reader = CsvReader.parse(FileUtils.readFile(csvInterestingPapers));
        reader.setDelimiter(';');
        reader.skipLine();
        interestingPapers = new LinkedList<PaperId>();
        csvIndex = new HashMap<PaperId, Integer>();
        int index = 0;
        while (reader.readRecord()) {
            String title = reader.get(2);
            int year = Integer.parseInt(reader.get(3));
            int issue = Integer.parseInt(reader.get(4));
            PaperId paperId = new PaperId(JOURNAL_NAME, year, issue, title);
            interestingPapers.add(paperId);
            csvIndex.put(paperId, index++);
        }
    }

    private List<PaperId> interestingPapers;

    private Map<PaperId, Integer> csvIndex;

    private Set<Integer> getI0Indexes(File i0File) throws IOException {
        if (!i0File.exists()) {
            System.err.println("I_zero file " + i0File.getAbsolutePath() + " does not exist");
            System.exit(1);
        }
        Set<Integer> indexes = new HashSet<Integer>();
        String content = FileUtils.readFile(i0File);
        String[] contentParts = content.split(",");
        for (String contentPart : contentParts) {
            int index = Integer.parseInt(contentPart.trim());
            indexes.add(index);
        }
        return indexes;
    }

    private Set<PaperId> getI0Ids(File i0File) throws IOException {
        Set<Integer> indexes = getI0Indexes(i0File);
        Set<PaperId> ids = new HashSet<PaperId>();
        for (int index : indexes) {
            ids.add(interestingPapers.get(index));
        }
        return ids;
    }

    private String getContent(List<Pair<PaperId, String>> papers, PaperId paperId) {
        for (Pair<PaperId, String> paper : papers) {
            if (paper.getFirst().equals(paperId)) {
                return paper.getSecond();
            }
        }
        throw new UnknownElementException(paperId);
    }

    private List<String> getContent(List<Pair<PaperId, String>> papers, Set<PaperId> paperIds) {
        List<String> contents = new LinkedList<String>();
        for (PaperId id : paperIds) {
            String content = getContent(papers, id);
            contents.add(content);
        }
        return contents;
    }

    private void removeIds(List<Pair<PaperId, String>> papers, Set<PaperId> ids) {
        List<Pair<PaperId, String>> papersToRemove = new LinkedList<Pair<PaperId, String>>();
        for (Pair<PaperId, String> paper : papers) {
            if (ids.contains(paper.getFirst())) {
                papersToRemove.add(paper);
            }
        }
        for (Pair<PaperId, String> paper : papersToRemove) {
            papers.remove(paper);
        }
    }

    private static final Logger logger = Logger.getLogger(ClassifierBatchStorer.class);

    public boolean useEnrichedPapers() {
        return enrichedDir != null;
    }

    public void algorithm(float threshold, File resultFile, File i0File) throws IOException, LoadingException {
        List<Pair<PaperId, String>> plainPapers = loadAllPlain();
        List<Pair<PaperId, String>> papersToExamine = useEnrichedPapers() ? loadAllEnriched() : plainPapers;
        algorithm(threshold, resultFile, plainPapers, papersToExamine, i0File);
    }

    public void algorithm(float threshold, File resultFile, List<Pair<PaperId, String>> plainPapers, List<Pair<PaperId, String>> enrichedPapers, File i0File) throws IOException, LoadingException {
        List<Pair<PaperId, String>> papersToExamine = new LinkedList<Pair<PaperId, String>>();
        papersToExamine.addAll(enrichedPapers);
        logger.info("All papers " + papersToExamine.size());
        KnowledgeBase model = getModel(getContent(plainPapers, getI0Ids(i0File)));
        StringBuffer results = new StringBuffer();
        removeIds(papersToExamine, getI0Ids(i0File));
        logger.info("Papers to examine (I0 removed): " + papersToExamine.size());
        int iteration = 0;
        while (papersToExamine.size() > 0) {
            List<Pair<PaperId, String>> papersFiltered = filterAutomatically(papersToExamine, threshold, model);
            results.append("iteration:" + (++iteration) + "\n");
            results.append("filtered:" + papersFiltered.size() + "\n");
            if (papersFiltered.size() == 0) {
                logger.info("NO MORE INTERESTING FOUND");
                break;
            }
            List<Pair<PaperId, String>> truePositives = getInterestingIn(papersFiltered);
            boolean first = true;
            for (Pair<PaperId, String> aTruePositive : truePositives) {
                PaperId paperId = aTruePositive.getFirst();
                if (!first) {
                    results.append(",");
                } else {
                    first = false;
                }
                results.append(getCsvIndex(paperId));
            }
            results.append("\n");
            for (Pair<PaperId, String> interestinPaper : truePositives) {
                String plainPaper = getContent(plainPapers, interestinPaper.getFirst());
                model.train("interesting", plainPaper);
            }
            papersToExamine.removeAll(papersFiltered);
        }
        FileUtils.saveFile(resultFile, results.toString());
    }

    private int getCsvIndex(PaperId paperId) {
        if (!csvIndex.containsKey(paperId)) {
            throw new UnknownElementException(paperId);
        }
        return csvIndex.get(paperId);
    }

    public static void main(String[] args) throws IOException, LoadingException {
        if (args.length != 6) {
            System.err.println("Args required: <plain papers dir> <enriched papers dir> <I_zero file> <interesting papers csv> <threshold> <result file>");
            return;
        }
        File plainPapersDir = new File(args[0]);
        File enrichedDir = new File(args[1]);
        File i0File = new File(args[2]);
        File csvInterestingPapers = new File(args[3]);
        float threshold = Float.parseFloat(args[4]);
        File resultFile = new File(args[5]);
        ClassifierBatchStorer instance = new ClassifierBatchStorer(plainPapersDir, enrichedDir, csvInterestingPapers);
        instance.algorithm(threshold, resultFile, i0File);
    }

    public List<Pair<PaperId, String>> getInterestingIn(List<Pair<PaperId, String>> papers) {
        List<Pair<PaperId, String>> interesting = new LinkedList<Pair<PaperId, String>>();
        for (Pair<PaperId, String> paper : papers) {
            PaperId paperId = paper.getFirst();
            if (interestingPapers.contains(paperId)) {
                interesting.add(paper);
            }
        }
        return interesting;
    }

    List<Pair<PaperId, String>> loadAllEnriched() throws IOException, LoadingException {
        if (enrichedDir == null) {
            throw new IllegalStateException("This instance works not on enriched papers");
        }
        return loadAll(enrichedDir, "enriched", true);
    }

    List<Pair<PaperId, String>> loadAllPlain() throws IOException, LoadingException {
        return loadAll(plainPapersDir, "txt", false);
    }

    private List<Pair<PaperId, String>> loadAll(File papersDir, String extension, final boolean serialized) throws IOException, LoadingException {
        final Map<PaperId, Boolean> interestingPapersFound = new HashMap<PaperId, Boolean>();
        for (PaperId interestingPaper : interestingPapers) {
            interestingPapersFound.put(interestingPaper, false);
        }
        PapersDirLoadingStrategy<String> pdls = new PapersDirLoadingStrategy<String>(papersDir, JOURNAL_NAME, extension) {

            @Override
            protected String load(PaperId paperId, File file) throws LoadingException {
                interestingPapersFound.put(paperId, true);
                if (serialized) {
                    return SerializationStorage.load(file, String.class);
                } else {
                    try {
                        return FileUtils.readFile(file);
                    } catch (IOException e) {
                        throw new LoadingException(e);
                    }
                }
            }
        };
        Map<PaperId, String> allPapers = pdls.getAll();
        for (PaperId paperId : interestingPapersFound.keySet()) {
            if (!interestingPapersFound.get(paperId)) {
                throw new RuntimeException("Interesting paper not found " + paperId);
            }
        }
        List<Pair<PaperId, String>> list = new LinkedList<Pair<PaperId, String>>();
        for (PaperId paperId : allPapers.keySet()) {
            list.add(new Pair<PaperId, String>(paperId, allPapers.get(paperId)));
        }
        return list;
    }

    public List<Pair<PaperId, String>> filterAutomatically(final List<Pair<PaperId, String>> papers, final float threshold, KnowledgeBase knowledgeBase) throws IOException, LoadingException {
        final List<Pair<PaperId, String>> classifiedPosivitely = new LinkedList<Pair<PaperId, String>>();
        classifier = new NaiveBayes("interesting");
        for (Pair<PaperId, String> paper : papers) {
            float affinity = classifier.getAffinity(knowledgeBase, paper.getSecond());
            if (affinity >= threshold) {
                classifiedPosivitely.add(paper);
            }
        }
        return classifiedPosivitely;
    }
}
