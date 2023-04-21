package org.hlj.web.lucene.index;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.hlj.web.lucene.need.LuceneNeed;

/**
 * Lucene索引器
 * @author WD
 * @since JDK5
 * @version 1.0 2010-05-25
 */
public interface LuceneIndex {

    /**
	 * 获得Lucene 索引写入器
	 * @return Lucene 索引写入器
	 */
    IndexWriter getIndexWriter();

    /**
	 * 获得Lucene 索引读取器
	 * @return Lucene 索引读取器
	 */
    IndexReader getIndexReader();

    /**
	 * 获得Lucene使用的类包
	 * @return Lucene使用的类包
	 */
    LuceneNeed getLuceneNeed();
}
