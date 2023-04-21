package de.fleckowarsky.yading.mainwindow;

import java.util.ArrayList;
import java.util.List;

public class SearchTermListModel {

    private static List<String> searchTerms = new ArrayList<String>();

    private static int position = 0;

    private SearchTermListModel() {
    }

    public static void addSearchTerm(String searchTerm) {
        searchTerms.add(searchTerm);
        position = searchTerms.size() - 1;
    }

    public static void clear() {
        searchTerms.clear();
        position = 0;
    }

    public static boolean hasNext() {
        return position < searchTerms.size() - 1;
    }

    public static boolean hasPrevious() {
        return (position > 0);
    }

    public static String getNext() {
        if (hasNext()) {
            position++;
            return searchTerms.get(position);
        } else {
            return null;
        }
    }

    public static String getPrevious() {
        if (hasPrevious()) {
            position--;
            return searchTerms.get(position);
        } else {
            return null;
        }
    }
}
