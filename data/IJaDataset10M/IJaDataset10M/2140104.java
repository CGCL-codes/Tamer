package com.fxbank.netbeans.plugins.utils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alexey
 */
public class CommonUtils {

    /**
     * Быстрая разбивка строки по символу - аналог String.split, но быстрее
     * @param s строка для разбивки
     * @param splitChar символ-разделитель
     * @return
     */
    public static List<String> fastSplit(String s, char splitChar) {
        List<String> res = new LinkedList<String>();
        if (s != null) {
            int start = 0;
            for (int end = 0; end <= s.length(); end++) {
                if (end == s.length() || s.charAt(end) == splitChar) {
                    if (start != end) {
                        res.add(s.substring(start, end));
                    }
                    start = end + 1;
                }
            }
        }
        return res;
    }
}
