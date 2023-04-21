package com.william.javacodelibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class New {

    public static <K, V> Map<K, V> map() {
        return new HashMap<K, V>();
    }

    public static <T> List<T> list() {
        return new ArrayList<T>();
    }

    public static <T> LinkedList<T> linkedList() {
        return new LinkedList<T>();
    }

    public static <T> Set<T> set() {
        return new HashSet<T>();
    }

    public static <T> Queue<T> queue() {
        return new LinkedList<T>();
    }

    public static void f(Map<String, List<? extends List<String>>> map) {
    }

    public void f2(Map<String, List<? extends List<String>>> map) {
    }

    public void testF2() {
        f2(this.<String, List<? extends List<String>>>map());
    }

    public static void main(String[] args) {
        Map<String, List<String>> sls = New.map();
        List<String> l = New.list();
        LinkedList<String> ll = New.linkedList();
        Set<String> s = New.set();
        Queue<String> q = New.queue();
        GenericMethods gm = new GenericMethods();
        f(New.<String, List<? extends List<String>>>map());
    }
}
