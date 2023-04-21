package com.bitgate.util.services.engine.tags.ext;

import java.util.Vector;
import com.bitgate.util.services.engine.tags.SkeletonTagList;

public class TagList implements SkeletonTagList {

    public TagList() {
    }

    public Vector<String> getTagList() {
        Vector<String> list = new Vector<String>();
        list.add("Chart");
        list.add("Id3");
        list.add("Nntp");
        list.add("Perl");
        list.add("Php");
        return list;
    }
}
