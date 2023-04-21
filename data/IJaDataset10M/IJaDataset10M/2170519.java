package br.ufmg.ubicomp.decs.server.eventservice.utils;

import java.util.Collection;

public class CollectionUtils {

    public static void copyArrayToCollection(String[] array, Collection<String> col) {
        for (String s : array) {
            col.add(s);
        }
    }
}
