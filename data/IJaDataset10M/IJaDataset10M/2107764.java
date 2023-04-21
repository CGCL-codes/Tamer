package org.butu.paged;

import java.util.List;

public class APagedList<E> implements IPagedList<E> {

    private static final long serialVersionUID = 1L;

    private List<E> list;

    private int totalCount;

    public APagedList(List<E> list, int totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<E> getList() {
        return list;
    }
}
