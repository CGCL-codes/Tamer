package com.my.util;

public class PageHelp {

    int totalPage;

    int allRow;

    int currentPage;

    int pageSize;

    int offset;

    int length;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void objectList(int allRow, String hql, int page, int pageSize) {
        this.allRow = allRow;
        this.totalPage = PageBean.countTotalPage(pageSize, allRow);
        this.offset = PageBean.countOffset(pageSize, page);
        this.length = pageSize;
        this.currentPage = PageBean.countCurrentPage(page);
        this.pageSize = pageSize;
    }

    public PageBean pageBean() {
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);
        pageBean.setCurrentPage(currentPage);
        pageBean.setAllRow(allRow);
        pageBean.setTotalPage(totalPage);
        pageBean.init();
        return pageBean;
    }
}
