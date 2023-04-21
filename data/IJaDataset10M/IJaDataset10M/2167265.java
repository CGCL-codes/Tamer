package com.jxva.dao;

import com.jxva.util.Assert;

/**
 * 
 * @author The Jxva Framework Foundation
 * @since 1.0
 * @version 2009-01-03 21:09:38 by Jxva
 */
public class PageBean {

    public static final int DEFAULT_PAGESIZE = 20;

    private int pagesize = 20;

    private int pageno;

    private long totalPage;

    private long totalCount;

    public PageBean() {
        throw new UnsupportedOperationException("please using other construction function");
    }

    public PageBean(int pageno) {
        Assert.isTrue(pageno > 0, "pageno must be greater than zero");
        this.pageno = pageno;
    }

    public PageBean(int pageno, int pagesize) {
        Assert.isTrue(pageno > 0, "pageno must be greater than zero");
        Assert.isTrue(pagesize > 0, "pagesize must be greater than zero");
        this.pageno = pageno;
        this.pagesize = pagesize;
    }

    public int getPageno() {
        return pageno;
    }

    public PageBean setPageno(int pageno) {
        this.pageno = pageno;
        return this;
    }

    public PageBean setPagesize(int pagesize) {
        this.pagesize = pagesize;
        return this;
    }

    public int getPagesize() {
        return pagesize;
    }

    public long getTotalPage() {
        if (totalCount % pagesize == 0) {
            return totalCount / pagesize;
        } else {
            return (totalCount - totalCount % pagesize) / pagesize + 1;
        }
    }

    public int getNextPage() {
        if (hasNextPage()) {
            return pageno + 1;
        }
        return pageno;
    }

    public int getPrevPage() {
        if (hasPrevPage()) {
            return pageno - 1;
        }
        return pageno;
    }

    public boolean hasPrevPage() {
        return pageno > 1;
    }

    public boolean hasNextPage() {
        return pageno < totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getStartIndex() {
        return pagesize * (pageno - 1);
    }

    public int getEndIndex() {
        return pageno * pagesize;
    }
}
