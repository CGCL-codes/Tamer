package net.livebookstore.web.service;

/**
 * A search result contains books and page information.
 * 
 * @author Xuefeng
 */
public class SearchResult {

    private int totalCount;

    private int pageIndex;

    private int pageCount;

    private int pageSize;

    private BookResult[] books;

    public SearchResult(BookResult[] books, int totalCount, int pageCount, int pageIndex, int pageSize) {
        this.books = books;
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public BookResult[] getBooks() {
        return books;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }
}
