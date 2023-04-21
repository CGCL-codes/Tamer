package com.objectwave.utility;

/**
* Description: Class for a single node in a SkipList
*/
public class SkipListElement {

    public SkipListElement(int level, Object key, Object value) {
        this.key = key;
        this.value = value;
        forward = new SkipListElement[level + 1];
    }

    int getLevel() {
        return forward.length - 1;
    }

    Object key;

    Object value;

    SkipListElement forward[];
}
