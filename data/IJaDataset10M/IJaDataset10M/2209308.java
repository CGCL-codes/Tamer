package org.agile.dfs.core.common;

import java.util.HashSet;
import java.util.Set;

public class FileNameHashHelper {

    public static final int DEFAULT_MAX_HASH_CODE = 20;

    private int maxHashCode;

    public FileNameHashHelper() {
        this(DEFAULT_MAX_HASH_CODE);
    }

    public FileNameHashHelper(int maxHashCode) {
        this.maxHashCode = maxHashCode;
    }

    public int hash(String fileName) {
        int tm = Math.abs(fileName.hashCode());
        int n = tm % maxHashCode;
        if (n == 0) {
            n = maxHashCode;
        }
        return n;
    }

    public static void main(String[] args) {
        Set set = new HashSet();
        FileNameHashHelper helper = new FileNameHashHelper();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            String fn = "/one" + i + "/two" + i + "/three" + i;
            Integer code = new Integer(helper.hash(fn));
            if (set.contains(code)) {
            } else {
            }
            System.out.println(code);
        }
        long t2 = System.currentTimeMillis();
        System.out.print(t2 - t1);
    }
}
