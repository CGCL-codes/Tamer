package com.cnc.mediaconnect.search;

public class Page {

    private int page = 0;

    private int maxpage = 0;

    private int maxitem = 0;

    public int getMaxItem() {
        return maxitem;
    }

    public void setMaxItem(int item) {
        maxitem = item;
    }

    public int getMaxpage() {
        return maxpage;
    }

    public void setMaxpage(String maxpage) {
        try {
            int index = Integer.parseInt(maxpage);
            maxitem = index;
            this.maxpage = (index / 7) + (index % 7 == 0 ? 0 : 1);
        } catch (Exception e) {
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
