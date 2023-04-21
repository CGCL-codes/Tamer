package org.ztemplates.test.actions.urlhandler.repository.simple;

import org.ztemplates.actions.ZMatch;

/**
 */
@ZMatch("/audiobooks2/category/${title}/${categoryId}[/page/${pageNum}[/sortby-${sortBy}]]")
public class Handler2 {

    private String title;

    private String categoryId;

    private String pageNum;

    private String sortBy;

    private String param1;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
