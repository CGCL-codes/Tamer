package com.c2b2.ipoint.model;

import java.util.Comparator;

/**
 * This class is compares page objects and orders by page order
 * <p>
 * iPoint Portal
 * Copyright 2007 C2B2 Consulting Limited. All rights reserved.
 * </p>
 */
public class PageOrderComparator implements Comparator<Page> {

    public PageOrderComparator() {
    }

    public int compare(Page o1, Page o2) {
        int result = 0;
        if (o1.getPageOrder() < o2.getPageOrder()) {
            result = -1;
        } else if (o1.getPageOrder() > o2.getPageOrder()) {
            result = 1;
        }
        return result;
    }
}
