package com.em.janus.model.sorting;

import java.util.Comparator;
import com.em.janus.model.Tag;

public class TagBookCountComparator implements Comparator<Tag> {

    @Override
    public int compare(Tag tag1, Tag tag2) {
        if (tag1 == null && tag2 == null) return 0;
        if (tag1 == null) return 1;
        if (tag2 == null) return -1;
        if (tag1.equals(tag2)) return 0;
        Integer books1 = tag1.getBooks().size();
        Integer books2 = tag2.getBooks().size();
        if (books1 == null && books2 == null) return 0;
        if (books2 == null) return 1;
        if (books1 == null) return -1;
        if (books2.compareTo(books1) == 0) {
            return tag1.getName().compareTo(tag2.getName());
        }
        return books2.compareTo(books1);
    }
}
