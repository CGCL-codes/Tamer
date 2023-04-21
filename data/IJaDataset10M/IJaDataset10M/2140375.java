package com.realtimesearch.index;

import java.io.*;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import com.realtimesearch.core.BBSItem;
import com.realtimesearch.index.BBSItemDocument;
import com.realtimesearch.searchengine.config.PropertyConfiguration;

public class BBSItemIndexer {

    private String indexPath = "";

    private IndexWriter writer = null;

    private Analyzer analyzer = null;

    private String dictionary_file = PropertyConfiguration.getWordDictionary();

    public BBSItemIndexer(String indexPath) throws Exception {
        this.indexPath = indexPath;
        initialize();
    }

    private void initialize() throws Exception {
        Directory d = new SimpleFSDirectory(new File(indexPath));
        analyzer = new IKAnalyzer();
        writer = new IndexWriter(d, analyzer, true, MaxFieldLength.LIMITED);
    }

    public void close() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            writer = null;
        }
    }

    public void addBBSItem(BBSItem bbsitem, int id) throws Exception {
        Thread.sleep(10);
        Document doc = BBSItemDocument.buildBBSItemDocment(bbsitem, id);
        writer.addDocument(doc);
        Thread.sleep(10);
    }

    public void optimizeIndex() throws Exception {
        writer.optimize();
    }
}
