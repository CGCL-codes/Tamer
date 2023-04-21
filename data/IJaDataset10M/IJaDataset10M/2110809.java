package com.msimone.mobreader.text;

public interface TextProvider {

    int DEFAULT_PAGE_SIZE = 220;

    void setPageSize(final int pageSize);

    String getCurrentPage();

    String getNextLine();

    String getPreviousLine();

    String getNextPage();

    String getPreviousPage();

    void mark();

    void reset();

    boolean hasMore();

    int pageNumber();

    int numberOfPages();
}
