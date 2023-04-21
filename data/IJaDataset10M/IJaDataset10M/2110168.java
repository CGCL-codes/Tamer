package com.example.list;

import com.advancedpwr.record.BaseFactory;
import com.advancedpwr.record.examples.ListExample;
import java.util.List;
import java.util.Vector;

public class ListExampleVectorFactory extends BaseFactory {

    protected ListExample listexample;

    public ListExample buildListExample() {
        listexample = (ListExample) newInstance(ListExample.class);
        listexample.setList(buildVector_1_1());
        return listexample;
    }

    protected Vector vector_1_1;

    protected List buildVector_1_1() {
        vector_1_1 = (Vector) newInstance(Vector.class);
        vector_1_1.add("entry 1");
        vector_1_1.add("entry 2");
        return vector_1_1;
    }
}
