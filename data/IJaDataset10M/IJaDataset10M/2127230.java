package net.sf.katta.index.indexer.merge;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class IndexDuplicateReducer implements Reducer<Text, DocumentInformation, Text, DocumentInformation> {

    public static enum DuplicateCounter {

        INVALID_DOCUMENTS, DUPLICATE_DOCUMENTS, REMOVED_DOCUMENTS
    }

    public void reduce(Text text, Iterator<DocumentInformation> iterator, OutputCollector<Text, DocumentInformation> outputCollector, Reporter reporter) throws IOException {
        if (text.toString().equals(DfsIndexRecordReader.INVALID)) {
            reporter.incrCounter(DuplicateCounter.INVALID_DOCUMENTS, 1);
            return;
        }
        DocumentInformation newestInformation = new DocumentInformation();
        Text sortValue = new Text();
        int docCount = 0;
        while (iterator.hasNext()) {
            docCount++;
            DocumentInformation documentInformation = iterator.next();
            Text tmpSortValue = documentInformation.getSortValue();
            if (sortValue.getLength() == 0 || tmpSortValue.compareTo(sortValue) > 0) {
                sortValue.set(tmpSortValue.getBytes());
                newestInformation.setDocId(documentInformation.getDocId().get());
                newestInformation.setIndexPath(documentInformation.getIndexPath().toString());
                newestInformation.setSortValue(documentInformation.getSortValue().toString());
            }
        }
        if (docCount > 1) {
            reporter.incrCounter(DuplicateCounter.DUPLICATE_DOCUMENTS, 1);
            reporter.incrCounter(DuplicateCounter.REMOVED_DOCUMENTS, docCount - 1);
        }
        outputCollector.collect(text, newestInformation);
    }

    public void configure(JobConf jobConf) {
    }

    public void close() throws IOException {
    }
}
