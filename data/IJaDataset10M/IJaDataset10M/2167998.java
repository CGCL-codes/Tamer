package net.jadoth.test.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Thomas M�nz
 *
 */
public class MainBla {

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        final List<String> VALUES = Arrays.asList("A", "B", "C");
        final ArrayList<List<String>> lists = new ArrayList<List<String>>();
        final LinkedList<String> linkedList = new LinkedList<String>(VALUES);
        final ArrayList<String> arrayList = new ArrayList<String>(VALUES);
        System.out.println("Adding " + linkedList.getClass().getCanonicalName() + " linkedList " + linkedList + " to lists, index " + lists.size());
        lists.add(linkedList);
        System.out.println("Adding " + arrayList.getClass().getCanonicalName() + "  arrayList  " + arrayList + " to lists, index " + lists.size());
        lists.add(arrayList);
        System.out.println("\nlists.indexOf(arrayList): " + lists.indexOf(arrayList));
        System.out.println("\nlists.remove(arrayList)");
        lists.remove(arrayList);
        System.out.println("\nremaining elements:");
        for (final List<String> list : lists) {
            System.out.println(" - " + list.getClass().getCanonicalName());
        }
    }
}
