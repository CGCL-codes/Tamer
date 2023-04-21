package com.explosion.expfmodules.search;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.utilities.FileIterator;
import com.explosion.utilities.process.StackableSimpleProcess;
import com.explosion.utilities.process.threads.Finishable;

/**
 * @author Stephen
 * Created on May 7, 2004
 */
public class ClassSearchProcess extends StackableSimpleProcess {

    private static Logger log = LogManager.getLogger(ClassSearchProcess.class);

    FileIterator iterator;

    ClassSearcher searcher = null;

    Vector results = new Vector();

    /**
     * 
     * @param finishable
     * @param className
     * @param caseSensitive
     * @param directoryPath
     * @param filenamePattern
     * @param recursive
     * @throws IOException
     */
    public ClassSearchProcess(Finishable finishable, String className, boolean caseSensitive, String directoryPath, String filenamePattern, boolean includeClassesOfSameType, boolean recursive) throws IOException {
        super(finishable, null);
        searcher = new ClassSearcher(className, caseSensitive, includeClassesOfSameType);
        iterator = new FileIterator(directoryPath, filenamePattern, false, recursive, true);
        this.setIsUserProcess(true);
    }

    /**
     * Processes a file
     */
    public void process() throws Exception {
        try {
            int count = 1;
            this.setStatusText("Starting search");
            while (iterator.hasNext()) {
                if (isStopped()) return;
                File file = iterator.next();
                this.setStatusText("Searching file " + file.getAbsolutePath());
                searcher.processFile(count, file.getAbsolutePath());
                if (searcher.found()) {
                    results.add(searcher.getResults());
                }
                this.setPercentComplete(iterator.getPercentComplete());
                count++;
            }
            this.setPercentComplete(100);
            this.setStatusText("Finished, found " + count + " matches.");
        } finally {
            this.setPercentComplete(100);
        }
    }

    /**
     * @return
     */
    public Vector getFileSearchResults() {
        return results;
    }
}
