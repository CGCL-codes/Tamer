package org.streets.extention.lucene.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.slf4j.Logger;
import org.streets.extention.lucene.LuceneConstants;
import org.streets.extention.lucene.LuceneRuntimeException;
import org.streets.extention.lucene.services.IndexSource;

/**
 *
 *
 * @version $Id: IndexSourceImpl.java 315 2008-11-11 16:05:58Z mlusetti $
 */
public class IndexSourceImpl implements IndexSource, RegistryShutdownListener {

    private final Logger logger;

    private final Directory directory;

    private final Analyzer analyzer;

    private final IndexWriter indexWriter;

    /**
	 * 
	 * @param logger
	 * @param configResource
	 */
    public IndexSourceImpl(final Logger logger, final Resource configResource) {
        Defense.notNull(configResource, "configResource");
        this.logger = logger;
        if (!configResource.exists()) throw new LuceneRuntimeException(String.format("config resource '%s' not found!", configResource.toURL().toString()));
        try {
            Properties prop = new Properties();
            prop.load(configResource.toURL().openStream());
            File indexFolderFile = new File(prop.getProperty(LuceneConstants.PROPERTIES_KEY_IF));
            boolean createFolder = Boolean.valueOf(prop.getProperty(LuceneConstants.PROPERTIES_KEY_OIF, "false"));
            boolean enableLuceneOutput = Boolean.valueOf(prop.getProperty(LuceneConstants.PROPERTIES_KEY_ELO, "false"));
            String analyzerClassName = prop.getProperty(LuceneConstants.PROPERTIES_KEY_ACN, StandardAnalyzer.class.getName());
            int maxFieldLength = Integer.valueOf(prop.getProperty(LuceneConstants.PROPERTIES_KEY_MFL, "250000"));
            this.directory = FSDirectory.getDirectory(indexFolderFile);
            Class analyzerClass = getClass().getClassLoader().loadClass(analyzerClassName);
            this.analyzer = (Analyzer) analyzerClass.newInstance();
            if (enableLuceneOutput) IndexWriter.setDefaultInfoStream(System.out);
            this.indexWriter = new IndexWriter(this.directory, this.analyzer, createFolder, new IndexWriter.MaxFieldLength(maxFieldLength));
        } catch (IOException ioe) {
            throw new LuceneRuntimeException(ioe);
        } catch (IllegalAccessException iae) {
            throw new LuceneRuntimeException(iae);
        } catch (InstantiationException ie) {
            throw new LuceneRuntimeException(ie);
        } catch (ClassNotFoundException cnfe) {
            throw new LuceneRuntimeException(cnfe);
        }
    }

    public void registryDidShutdown() {
        try {
            if (this.indexWriter != null) this.indexWriter.close();
            if (this.directory != null) this.directory.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IndexSearcher createIndexSearcher() {
        try {
            return new IndexSearcher(this.directory);
        } catch (CorruptIndexException cie) {
            this.logger.error(String.format("The index result corrupted: '%s'", cie.getMessage()), cie);
            throw new LuceneRuntimeException(cie);
        } catch (IOException ioe) {
            this.logger.error(String.format("Unable to access the index for building new reader, reason: '%s'", ioe.getMessage()), ioe);
            throw new LuceneRuntimeException(ioe);
        }
    }

    public Analyzer getAnalyzer() {
        return this.analyzer;
    }

    public IndexWriter getIndexWriter() {
        return this.indexWriter;
    }
}
