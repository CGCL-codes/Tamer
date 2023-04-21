package com.fwk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListHandler<E> {

    public ListHandler() {
        list = new ArrayList<E>();
    }

    public ListHandler(int size) {
        list = new ArrayList<E>(size);
    }

    public ListHandler(List ls) {
        this.list = ls;
    }

    private int totalCount;

    private List<E> list;

    public boolean add(E o) {
        return list.add(o);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public boolean remove(E o) {
        return list.remove(o);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListIterator iterator() {
        return list.listIterator();
    }

    public int size() {
        return list.size();
    }

    public List<E> getList() {
        return this.list;
    }

    public int getTotalPage(int pageSize) {
        return (totalCount % pageSize != 0) ? totalCount / pageSize + 1 : totalCount / pageSize;
    }
}
