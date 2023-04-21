package com.sefer.dragonfly.client.confige.pojo;

import java.util.LinkedList;
import java.util.List;

public class CmCaches {

    private List<CmCachePojo> lists = new LinkedList<CmCachePojo>();

    public void add(CmCachePojo c) {
        lists.add(c);
    }

    public List<CmCachePojo> getLists() {
        return lists;
    }

    public void setLists(List<CmCachePojo> lists) {
        this.lists = lists;
    }
}
