package uk.ac.shef.wit.trex;

import uk.ac.shef.wit.trex.text.annotation.*;
import uk.ac.shef.wit.trex.task.BatchTask;
import uk.ac.shef.wit.trex.task.ClassificationTask;
import uk.ac.shef.wit.trex.task.Task;
import uk.ac.shef.wit.trex.task.observer.TaskObserverBlind;
import uk.ac.shef.wit.trex.task.predefined.BatchTaskDefinitionSimpleTrainOnly;
import uk.ac.shef.wit.trex.task.predefined.BatchTaskDefinitionSimpleTestOnly;
import uk.ac.shef.wit.trex.task.runner.BatchTaskRunnerSpeedOptimizedTrainOnly;
import uk.ac.shef.wit.trex.task.runner.BatchTaskRunner;
import uk.ac.shef.wit.trex.task.runner.BatchTaskRunnerSpeedOptimizedTestOnly;
import uk.ac.shef.wit.trex.dataset.Dataset;
import uk.ac.shef.wit.trex.dataset.LocatorWithURI;
import uk.ac.shef.wit.trex.util.logger.LoggerProfiler;
import uk.ac.shef.wit.trex.util.logger.Logger;
import java.io.*;
import java.util.*;

public class TrexRodrigo {

    File corpus_dir, clean_corpus_dir, tmp_dir;

    public static Annotations annotations = null;

    private static final String[] TAGS_TO_DETECT = new String[] { "person", "location", "date", "organization" };

    private Set<String> trainingSet;

    private String[] nodes;

    private String[] uris;

    /**
     * Instantiates a TRex object.
     *
     * @param corpusDir - The path to the directory where the training files are.
     * @param trainingFilenames - The names of the training files.     
     * @param ontologyNodes - The nodes in the ontology (short name).
     * @param ontologyNodesURI - The URIs of the nodes in the ontology (long name).
     * @throws Exception - An exception is thrown if the corpusDir parameter does not point to a directory.
     */
    public TrexRodrigo(String corpusDir, Set<String> trainingFilenames, String[] ontologyNodes, String[] ontologyNodesURI) throws Exception {
        corpus_dir = new File(corpusDir);
        if (!corpus_dir.isDirectory()) throw new Exception("Param corpusDir must be a directory.");
        if (!corpus_dir.exists()) corpus_dir.mkdirs();
        clean_corpus_dir = new File(corpus_dir, "./.clean");
        if (!clean_corpus_dir.exists()) clean_corpus_dir.mkdirs();
        tmp_dir = new File(corpus_dir, "./.tmp");
        if (!tmp_dir.exists()) tmp_dir.mkdirs();
        trainingSet = trainingFilenames;
        nodes = ontologyNodes;
        uris = ontologyNodesURI;
    }

    /**
     * Performs Named Entity Recognitions on a string.
     *
     * @param text The string to be annotated.
     * @return The input string annotated.
     */
    public String getAnnotatedText(String text) throws IOException, TrexException {
        Collection<String> inputString = new ArrayList<String>();
        inputString.add(text);
        Collection<String> annotatedString = getAnnotatedText(inputString);
        return annotatedString.iterator().next();
    }

    /**
     * Performs Named Entity Recognition on a batch of strings.
     *
     * @param batchText A String Collection containing the lines to be annotated.
     * @return The input String annotated
     * @throws TrexException
     * @throws IOException
     */
    public Collection<String> getAnnotatedText(Collection<String> batchText) throws TrexException, IOException {
        File outputFile = new File(tmp_dir, Long.toString(Calendar.getInstance().getTimeInMillis()) + ".tmp");
        outputFile.createNewFile();
        FileWriter out = new FileWriter(outputFile);
        for (String text : batchText) out.append(" " + text + "\n");
        out.flush();
        out.close();
        Set<String> testSet = new HashSet<String>();
        testSet.add(outputFile.getName());
        if (annotations == null) {
            Set<File> taggedFiles = new TreeSet<File>();
            for (String filename : trainingSet) taggedFiles.add(new File(corpus_dir, filename));
            annotations = loadAnnotationsFromTrainingSet(taggedFiles, TAGS_TO_DETECT);
        }
        BatchTask trainingTask = (BatchTask) new BatchTaskDefinitionSimpleTrainOnly(new String[] { corpus_dir.getAbsolutePath() }, trainingSet, nodes, uris).getResult();
        final Map<AnnotationOfEntity, Integer> suggestedAnnotations = new HashMap<AnnotationOfEntity, Integer>();
        final BatchTaskRunner runner = new BatchTaskRunnerSpeedOptimizedTrainOnly(1);
        runner.addObserver(new TaskObserverBlind() {

            public boolean wantsTrainDatasets() {
                return true;
            }

            public void handleTrainDataset(final Task task, final ClassificationTask subTask, final Object theClass, final Dataset dataset) {
                super.handleTrainDataset(task, subTask, theClass, dataset);
            }
        });
        runner.run(trainingTask);
        BatchTask testingTask = (BatchTask) new BatchTaskDefinitionSimpleTestOnly(new String[] { tmp_dir.getAbsolutePath() }, testSet, nodes, uris).getResult();
        BatchTaskRunner runner2 = new BatchTaskRunnerSpeedOptimizedTestOnly(1);
        runner2.addObserver(new TaskObserverBlind() {

            public boolean wantsPredictedAnnotations() {
                return true;
            }

            public void handlePredictedAnnotations(Task task, Annotations annotations) {
                AnnotationOfEntityList annotationOfEntityList = null;
                try {
                    annotationOfEntityList = annotations.listAnnotationsOfEntity();
                } catch (TrexException e) {
                    e.printStackTrace();
                }
                for (Iterator it = annotationOfEntityList.iterator(); it.hasNext(); ) System.out.println((AnnotationOfEntity) it.next());
            }
        });
        runner2.run(testingTask);
        System.out.println("Found " + suggestedAnnotations.size() + " suggested annotations.");
        Vector<Map.Entry<AnnotationOfEntity, Integer>> suggestedAnnotationsOrdered = new Vector<Map.Entry<AnnotationOfEntity, Integer>>(suggestedAnnotations.entrySet());
        Collections.sort(suggestedAnnotationsOrdered, new ReverseOrderByInteger());
        StringBuffer plainText = new StringBuffer();
        for (String s : batchText) plainText.append(" " + s + "\n");
        String annotatedText = plainText.toString().replaceAll("\n", "\t");
        Set<String> entitiesAnnotated = new HashSet<String>();
        for (Map.Entry<AnnotationOfEntity, Integer> entry : suggestedAnnotationsOrdered) {
            String entity = plainText.substring(entry.getKey().getStart().getOffset(), entry.getKey().getEnd().getOffset());
            if (entitiesAnnotated.add(entity)) {
                String annotation = "<" + entry.getKey().getAboutEntity() + ">" + entity + "</" + entry.getKey().getAboutEntity() + ">";
                annotatedText = annotatedText.replaceAll(entity, annotation);
            }
        }
        batchText.clear();
        String[] annotatedTextArray = annotatedText.split("\t");
        for (String s : annotatedTextArray) batchText.add(s);
        plainText.delete(0, plainText.length());
        entitiesAnnotated.clear();
        suggestedAnnotations.clear();
        suggestedAnnotationsOrdered.clear();
        outputFile.delete();
        return batchText;
    }

    /**
     * Given a training file that is tagged with <Tag>entity</Tag>, this method produces a clean
     * version of the file given (i.e., with no tags) and fetches the offset values (start and end)
     * of the <i>entity</i> within this clean file so that these can be passed to T-Rex.
     *
     * Please note that the names of the tagged files given initially as the training set will be
     * subsituted by the filenames of the clean files generated by this method. These clean files
     * are located in a hidden .clean folder inside the corpus directory given in the constructor. 
     *
     * @param taggedFiles - A list of files tagged with <Tag>entity</Tag>.
     * @param tagsToDetect - A list of tag names to be detected within the files (without '<' '<' '/').
     * @return Annotations as AnnotationOfEntity objects.
     */
    private Annotations loadAnnotationsFromTrainingSet(Set<File> taggedFiles, String[] tagsToDetect) {
        AnnotationsBuilder builder = new AnnotationsBuilder();
        BufferedReader in;
        String line;
        StringBuffer taggedFile;
        FileWriter out;
        File outputFile;
        for (File file : taggedFiles) {
            try {
                outputFile = new File(clean_corpus_dir, file.getName());
                if (!outputFile.exists()) {
                    outputFile.setReadOnly();
                    outputFile.createNewFile();
                }
                in = new BufferedReader(new FileReader(file));
                line = in.readLine();
                taggedFile = new StringBuffer();
                while (line != null) {
                    taggedFile.append(line + "\n");
                    line = in.readLine();
                }
                int currentIndex = 0;
                int startOffset = taggedFile.indexOf("<", currentIndex);
                int endOffset = taggedFile.indexOf(">", startOffset);
                boolean foundEntity = false;
                while (startOffset > -1 && endOffset > -1) {
                    String tag = taggedFile.substring(startOffset, endOffset).toLowerCase();
                    for (int i = 0; i < tagsToDetect.length && !foundEntity; i++) if (tag.startsWith("<" + tagsToDetect[i].toLowerCase())) foundEntity = true;
                    if (foundEntity) {
                        foundEntity = false;
                        taggedFile.replace(startOffset, startOffset + tag.length() + 1, "");
                        endOffset = taggedFile.indexOf("</", startOffset);
                        AnnotationOfEntity annotation = new AnnotationOfEntityBuilder().setAboutEntity(tag.substring(1)).setStart(new LocatorWithURI(outputFile.getName(), startOffset)).setEnd(new LocatorWithURI(outputFile.getName(), endOffset)).getResult();
                        builder.add(annotation);
                        taggedFile.replace(endOffset, endOffset + tag.length() + 2, "");
                    } else {
                        taggedFile.replace(startOffset, startOffset + tag.length() + 1, "");
                    }
                    startOffset = taggedFile.indexOf("<", startOffset);
                    endOffset = taggedFile.indexOf(">", startOffset);
                }
                out = new FileWriter(outputFile);
                out.append(taggedFile.toString());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TrexException e) {
                e.printStackTrace();
            }
        }
        return builder.getResult();
    }

    public static void main(String[] args) throws Exception {
        Logger.getInstance().addObserver(new LoggerProfiler(System.out));
        Set<String> trainingFilenames = new HashSet<String>();
        String[] ontologyNodes = new String[] { "person", "location", "date", "organization" };
        String[] nodesURI = new String[] { "http://fakeontology.com#Person", "http://fakeontology.com#Location", "http://fakeontology.com#Date", "http://fakeontology.com#Organization" };
        File annotated_file_dir = new File("/home/rodrigo/Data/Work/IntelliJProjects/wit/kodak/corpus/annotated_corpus");
        for (File f : annotated_file_dir.listFiles()) {
            if (f.isFile() && !f.isHidden()) trainingFilenames.add(f.getName());
        }
        TrexRodrigo trex = new TrexRodrigo("/home/rodrigo/Data/Work/IntelliJProjects/wit/kodak/corpus/annotated_corpus", trainingFilenames, ontologyNodes, nodesURI);
        boolean quit = false;
        int numberOfDescriptions = 172;
        BufferedReader in = new BufferedReader(new FileReader("/home/rodrigo/Data/Work/IntelliJProjects/wit/kodak/corpus/unnanotated_corpus/1_testing_photo.descriptions"));
        Collection<String> descriptions = new ArrayList<String>();
        String line = in.readLine();
        while (line != null && descriptions.size() <= numberOfDescriptions) {
            descriptions.add(line);
            line = in.readLine();
        }
        Collection<String> annotatedStuff = trex.getAnnotatedText(descriptions);
        for (String s : annotatedStuff) System.out.println(s);
    }

    private class ReverseOrderByInteger implements Comparator<Map.Entry<AnnotationOfEntity, Integer>> {

        public int compare(Map.Entry<AnnotationOfEntity, Integer> o1, Map.Entry<AnnotationOfEntity, Integer> o2) {
            return (int) Math.signum(o2.getValue() - o1.getValue());
        }
    }
}
