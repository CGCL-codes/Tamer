package examples;

import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.vfs.FileObject;
import pl.otros.logview.LogData;
import pl.otros.logview.batch.BatchProcessingContext;
import pl.otros.logview.batch.BatchProcessingListener;
import pl.otros.logview.batch.LogDataParsedListener;
import pl.otros.logview.batch.SingleFileBatchProcessingListener;
import java.util.Iterator;
import java.util.logging.Level;

public class ExampleLogDataParsedListener implements BatchProcessingListener, SingleFileBatchProcessingListener, LogDataParsedListener {

    int totalCount = 0;

    int singleFileCount = 0;

    @Override
    public void logDataParsed(LogData logData, BatchProcessingContext context) {
        if (logData.getLevel() != null && logData.getLevel().intValue() >= Level.WARNING.intValue()) {
            System.out.printf("Event with level %s at %2$tH:%2$tM:%2$tS %2$te-%2$tm-%2$tY: %3$s\n", logData.getLevel(), logData.getDate(), logData.getMessage());
            totalCount++;
            singleFileCount++;
        }
    }

    @Override
    public void processingStarted(BatchProcessingContext batchProcessingContext) {
        System.out.println("Batch processing started");
        DataConfiguration configuration = batchProcessingContext.getConfiguration();
        System.out.println("Dumping batch processing configuration.");
        Iterator<String> keys = configuration.getKeys();
        while (keys.hasNext()) {
            String next = keys.next();
            System.out.printf("%s=%s%n", next, configuration.getString(next));
        }
    }

    @Override
    public void processingFinished(BatchProcessingContext batchProcessingContext) {
        System.out.printf("Finished parsing all files, found %d event at level warning or higher\n", totalCount);
    }

    @Override
    public void processingFileStarted(BatchProcessingContext batchProcessingContext) {
        String baseName = batchProcessingContext.getCurrentFile().getName().getBaseName();
        System.out.printf("Processing file %s have started\n", baseName);
        singleFileCount = 0;
    }

    @Override
    public void processingFileFinished(BatchProcessingContext batchProcessingContext) {
        FileObject currentFile = batchProcessingContext.getCurrentFile();
        System.out.printf("Finished parsing file %s, found %d event at level warning or higher\n", currentFile.getName().getBaseName(), singleFileCount);
    }
}
