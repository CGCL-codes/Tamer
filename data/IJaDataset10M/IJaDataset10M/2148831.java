package com.google.code.sapwcrawler.datahandler;

import java.util.*;

public class CompositeDataHandler<T> implements DataHandler<T> {

    private List<DataHandler<T>> list = new ArrayList<DataHandler<T>>();

    public List<DataHandler<T>> getList() {
        return list;
    }

    public void processData(T data) {
        for (DataHandler<T> h : list) h.processData(data);
    }
}
