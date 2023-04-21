package pl.wcislo.sbql4j.javac.test;

import java.util.ArrayList;
import java.util.List;

public class SimpleClassWithParamMethod {

    public static List<Integer> getIntList() {
        return new ArrayList<Integer>();
    }

    public static List<List<Integer>> getIntNestedList() {
        return new ArrayList<List<Integer>>();
    }
}
