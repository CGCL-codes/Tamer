package info.jonclark.corpus.management.directories;

import info.jonclark.corpus.management.directories.CorpusQuery.Statistic;
import info.jonclark.corpus.management.etc.CorpusManException;
import info.jonclark.corpus.management.etc.CorpusProperties;
import info.jonclark.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParallelCorpusDirectory extends AbstractCorpusDirectory {

    private final String[] targets;

    public ParallelCorpusDirectory(Properties props, String directoryNamespace) throws CorpusManException {
        super(props, directoryNamespace);
        assert this.getType().equals("parallel") : "Incorrect directory type: " + this.getType();
        this.targets = CorpusProperties.getParallelTargets(props, directoryNamespace);
        if (targets.length <= 1) {
            throw new CorpusManException("No targets defined for parallel directory: " + directoryNamespace);
        }
    }

    @Override
    public List<File> getDocuments(CorpusQuery query, File currentDirectory) throws IOException {
        if (query.getNParallel() >= targets.length) throw new RuntimeException("Invalid query for parallel directory: " + query.getNParallel() + " when there are only " + targets.length + " parallel directories.");
        if (query.getNParallel() == CorpusQuery.ALL_PARALLEL_DIRECTORIES) {
            File[] subdirs = FileUtils.getSubdirectories(currentDirectory);
            ArrayList<File> documents = new ArrayList<File>(50000);
            for (final File subdir : subdirs) documents.addAll(getChild().getDocuments(query, subdir));
            return documents;
        } else {
            File subdir = new File(currentDirectory, targets[query.getNParallel()]);
            if (subdir.exists()) return getChild().getDocuments(query, subdir); else return new ArrayList<File>(0);
        }
    }

    @Override
    public File getNextFileForCreation(CorpusQuery query, File currentDirectory) {
        if (query.getNParallel() >= targets.length) throw new RuntimeException("Invalid query for parallel directory: " + query.getNParallel() + " when there are only " + targets.length + " parallel directories.");
        File childFile = new File(currentDirectory, targets[query.getNParallel()]);
        return getChild().getNextFileForCreation(query, childFile);
    }

    @Override
    public double getStatistic(CorpusQuery query, File currentDirectory) throws IOException {
        if (query.getStatistic() == Statistic.PARALLEL_COUNT) {
            return targets.length;
        } else if (query.getStatistic() == Statistic.DOCUMENT_COUNT) {
            double total = 0.0;
            for (int i = 0; i < targets.length; i++) {
                File subdir = new File(currentDirectory, targets[query.getNParallel()]);
                total += getChild().getStatistic(query, subdir);
            }
            return total;
        } else if (query.getStatistic() == Statistic.NONE) {
            return -1;
        } else {
            throw new RuntimeException("Unknown Statistics: " + query.getStatistic());
        }
    }
}
