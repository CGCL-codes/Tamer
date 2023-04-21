package com.gnizr.core.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import com.gnizr.db.dao.GnizrDao;
import com.gnizr.db.dao.Tag;
import com.gnizr.db.dao.tag.TagDBDao;

public class SearchSuggestIndexer implements Serializable {

    private static final String POP_TAGS_IDX_DIR = "poptags-idx";

    /**
	 * 
	 */
    private static final long serialVersionUID = 3081443635218877501L;

    private static final Logger logger = Logger.getLogger(SearchSuggestIndexer.class);

    private SearchIndexProfile searchIndexProfile;

    private FSDirectory popularTagsIndexDirectory;

    private RAMDirectory customDataIndexDirectory;

    private GnizrDao gnizrDao;

    public void init() {
        if (searchIndexProfile == null) {
            throw new NullPointerException("SearchIndexProfile is not defined");
        }
        customDataIndexDirectory = new RAMDirectory();
        String dataFile = searchIndexProfile.getSearchSuggestDataFile();
        if (dataFile != null) {
            try {
                initCustomDataIndex(dataFile, customDataIndexDirectory);
            } catch (Exception e) {
                logger.error("Error creating RAM-based user-defined suggest index. ", e);
            }
        }
        if (searchIndexProfile.isSuggestPopularTagsEnabled() == true) {
            String searchIndexDir = searchIndexProfile.getSearchIndexDirectory();
            File f = new File(searchIndexDir, POP_TAGS_IDX_DIR);
            try {
                popularTagsIndexDirectory = FSDirectory.getDirectory(f);
                List<Tag> tags = gnizrDao.getTagDao().findTag(5000, TagDBDao.SORT_FREQ);
                initPopularTagDataIndex(tags, popularTagsIndexDirectory);
            } catch (IOException e) {
                logger.error("Error creating FS-based suggest index based on popular tags. ", e);
            }
        }
    }

    public MultiReader openSuggestIndexReader() throws CorruptIndexException, IOException {
        List<IndexReader> idxReaders = new ArrayList<IndexReader>();
        if (customDataIndexDirectory != null) {
            try {
                idxReaders.add(IndexReader.open(customDataIndexDirectory));
            } catch (Exception e) {
                logger.debug("Unable to open RAM-based suggest index. Probably no custom data file is defined.");
            }
        }
        if (popularTagsIndexDirectory != null) {
            try {
                idxReaders.add(IndexReader.open(popularTagsIndexDirectory));
            } catch (Exception e) {
                logger.debug("Unable to open FS-based suggest index based on popular tags. Probably it hasn't been created yet.");
            }
        }
        if (idxReaders != null && idxReaders.size() > 0) {
            IndexReader[] readers = idxReaders.toArray(new IndexReader[idxReaders.size()]);
            return new MultiReader(readers);
        }
        return null;
    }

    private void initPopularTagDataIndex(List<Tag> tags, Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException {
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(directory, new KeywordAnalyzer(), true);
            for (Tag tag : tags) {
                String t = tag.getLabel();
                if (t.length() >= 3) {
                    Document doc = createDocument(t);
                    indexWriter.addDocument(doc);
                }
            }
            indexWriter.optimize();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }

    private BufferedReader createDataBufferedReader(String dataFile) throws FileNotFoundException {
        InputStream is = null;
        File file = new File(dataFile);
        if (file.exists() == true) {
            logger.debug("Attempt to read data from " + file.toString());
            is = new FileInputStream(file);
        } else {
            logger.debug("Attempt to read data from a resource in the class path");
            is = SearchSuggestIndexer.class.getResourceAsStream(dataFile);
        }
        if (is != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader;
        }
        return null;
    }

    private Document createDocument(String suggestTerm) {
        Document doc = new Document();
        doc.add(new Field("t", suggestTerm, Field.Store.YES, Field.Index.UN_TOKENIZED));
        return doc;
    }

    private void initCustomDataIndex(String dataFile, Directory directory) throws CorruptIndexException, LockObtainFailedException, IOException {
        BufferedReader dataReader = createDataBufferedReader(dataFile);
        if (dataReader != null) {
            IndexWriter indexWriter = new IndexWriter(directory, new KeywordAnalyzer(), true);
            try {
                String aline = dataReader.readLine();
                while (aline != null) {
                    StringTokenizer tokenizer = new StringTokenizer(aline, "\n\r\f", false);
                    while (tokenizer.hasMoreTokens()) {
                        String token = tokenizer.nextToken().trim();
                        if (token != null) {
                            Document doc = createDocument(token);
                            indexWriter.addDocument(doc);
                        }
                    }
                    aline = dataReader.readLine();
                }
                indexWriter.optimize();
            } finally {
                if (indexWriter != null) {
                    try {
                        indexWriter.close();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
                if (dataReader != null) {
                    try {
                        dataReader.close();
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }
            }
        }
    }

    public SearchIndexProfile getSearchIndexProfile() {
        return searchIndexProfile;
    }

    public void setSearchIndexProfile(SearchIndexProfile searchIndexProfile) {
        this.searchIndexProfile = searchIndexProfile;
    }

    public GnizrDao getGnizrDao() {
        return gnizrDao;
    }

    public void setGnizrDao(GnizrDao gnizrDao) {
        this.gnizrDao = gnizrDao;
    }
}
