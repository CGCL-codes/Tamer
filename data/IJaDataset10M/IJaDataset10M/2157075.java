package org.hibernate.search.test.batchindexing;

public interface TitleAble {

    public String getTitle();

    public void setTitle(String title);

    public void setFirstPublishedIn(Nation firstPublishedIn);

    public Nation getFirstPublishedIn();
}
