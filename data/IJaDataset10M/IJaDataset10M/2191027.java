package org.paralit.isf.core.search.indexer;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.paralit.isf.core.search.IndexPolicyInfo;
import org.paralit.isf.core.search.searcher.Search;

/**
 * 索引中文件权限信息修改
  * @author rmfish
 * 
 */
public class ModifyIndex {

    public Document document = null;

    public String policyInfoName[] = new String[] { "PID", "UID", "GID" };

    public String modifier[] = new String[] { null, null, null };

    public final class ModifierType {
    }

    /**
     * 根据文件ID，以及新的文件权限信息更新索引
     * @param id
     * @param policyInfo
     */
    public ModifyIndex(int id, IndexPolicyInfo policyInfo) {
        Term t = new Term("ID", String.valueOf(id));
        Query query = new TermQuery(t);
        try {
            Hits hit = Search.indexsearcher.search(query);
            this.document = hit.doc(0);
            if (policyInfo.policyID != this.document.getValues("PID")[0]) this.modifier[0] = policyInfo.policyID;
            if (policyInfo.userID != this.document.getValues("UID")[0]) this.modifier[1] = policyInfo.userID;
            if (policyInfo.groupID != this.document.getValues("GID")[0]) this.modifier[2] = policyInfo.groupID;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public void modify() {
        for (int i = 0; i < 3; ++i) {
            if (this.modifier[i] != null) {
                this.document.removeField(policyInfoName[i]);
                this.document.add(new Field(policyInfoName[i], modifier[i], Field.Store.YES, Field.Index.UN_TOKENIZED));
            }
        }
    }
}
