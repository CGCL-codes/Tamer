package com.maptales.mobile.util;

import com.maptales.mobile.bus.Media;

public class Helpers {

    public static String[] parseTags(String tagStr) {
        String[] tags = new String[10];
        int curPos = 0, i = 0;
        int nextPos;
        while ((nextPos = tagStr.indexOf(' ', curPos)) > -1) {
            String tag = tagStr.substring(curPos, nextPos).trim();
            if (!tag.equals("")) {
                tags[i++] = tagStr.substring(curPos, nextPos).trim();
            }
            curPos = nextPos + 1;
        }
        String tag = tagStr.substring(curPos).trim();
        if (!tag.equals("")) {
            tags[i++] = tag;
        }
        String[] realTags = new String[i];
        System.arraycopy(tags, 0, realTags, 0, i);
        return realTags;
    }

    static void sort(Media a[], int lo0, int hi0) throws Exception {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi) {
            return;
        }
        Media mid = a[(lo + hi) / 2];
        while (lo < hi) {
            while (lo < hi && a[lo].getCreationDate().getTime() < mid.getCreationDate().getTime()) {
                lo++;
            }
            while (lo < hi && a[hi].getCreationDate().getTime() > mid.getCreationDate().getTime()) {
                hi--;
            }
            if (lo < hi) {
                Media T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
        }
        if (hi < lo) {
            int T = hi;
            hi = lo;
            lo = T;
        }
        sort(a, lo0, lo);
        sort(a, lo == lo0 ? lo + 1 : lo, hi0);
    }

    public static void sortMediaByTimestamp(Media a[]) throws Exception {
        sort(a, 0, a.length - 1);
    }
}
