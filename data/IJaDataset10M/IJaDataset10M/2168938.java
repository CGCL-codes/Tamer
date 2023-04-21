package com.onehao.collection.strategyrefine;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByAge implements ISort {

    public void sort(List<Person> list) {
        Collections.sort(list, new SortByAgeComparator());
    }
}

class SortByAgeComparator implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        int age1 = p1.getAge();
        int age2 = p2.getAge();
        if (age1 == age2) {
            return p1.getName().compareTo(p2.getName());
        }
        return age1 - age2;
    }
}
