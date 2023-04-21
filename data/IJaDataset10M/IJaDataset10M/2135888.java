package org.pubcurator.core.model;

import java.util.List;
import org.pubcurator.model.document.PubDocument;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class DocumentsQueryResult {

    private List<PubDocument> documents;

    private int page;

    private int pageSize;

    private int totalCount;

    public DocumentsQueryResult(List<PubDocument> documents, int page, int pageSize, int totalCount) {
        this.documents = documents;
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public List<PubDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PubDocument> documents) {
        this.documents = documents;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
