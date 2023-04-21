package org.paralit.isf.core.search.indexer;

import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.paralit.isf.core.search.IndexPolicyInfo;
import org.paralit.isf.core.search.analyzer.ISFAnalyzer;
import org.paralit.isf.core.search.workflow.MergeMemTask;
import org.paralit.isf.core.search.workflow.WorkFlow;
import org.paralit.isf.exceptions.SearchEngineException;

/**
 * 为Reader类型建立索引
 * 
 * @author rmfish
 * 
 */
public class IndexReader extends Indexer {

    private IndexWriter ramwriter;

    private Reader reader;

    IndexReader(IndexFiles indexFile) {
        super(indexFile);
        reader = indexFile.reader;
    }

    public void index() throws SearchEngineException {
        try {
            ramwriter = new IndexWriter(ramDir, new ISFAnalyzer(), true);
            ramwriter.setUseCompoundFile(false);
            indexFile(id, reader, port, policyInfo, boost, ramwriter);
            ramwriter.close();
            WorkFlow.addWork(new MergeMemTask(ramDir));
        } catch (IOException e) {
            e.printStackTrace();
            throw new SearchEngineException("建立索引时出现数据访问错误");
        }
    }

    /**
	 * 
	 * @param id
	 *            文件ID
	 * @param reader
	 *            Reader类型的文件流
	 * @param boost
	 *            文件的增强因子
	 * @param ramwriter
	 *            IndexWriter对象
	 * @throws IOException
	 */
    private void indexFile(int id, Reader reader, int port, IndexPolicyInfo policyInfo, float boost, IndexWriter ramwriter) throws IOException {
        if (reader == null) return;
        Document doc = new Document();
        doc.add(new Field("TIME", String.valueOf(System.currentTimeMillis()), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("ID", String.valueOf(id), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("content", reader, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("PORT", String.valueOf(port), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("PID", policyInfo.policyID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("UID", policyInfo.userID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("GID", policyInfo.groupID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.setBoost(boost);
        ramwriter.addDocument(doc);
    }
}
