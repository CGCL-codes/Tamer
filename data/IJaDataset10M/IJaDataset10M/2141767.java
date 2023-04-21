package net.sf.campusip.search.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

public interface LuceneDocumentFactory {

    Document createDocument(Object obj);

    String getHandleAttributeName(Object obj);

    String getHandleFieldName(Object obj);

    Analyzer createAnalyzer();
}
