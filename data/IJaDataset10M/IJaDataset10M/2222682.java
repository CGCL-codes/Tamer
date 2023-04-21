package com.nusino.dql.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Copy Right DynamicQL&copy; Nusino Technologies Inc.
 * If you are authorized to use this code. then you can modify code. However, the copy right marker does not allow to be removed. 
 * @author daping huang, dhuang05@gmail.com
 */
public class ResolverUtil {

    public static final String PARAM_start = "(";

    public static final String COMMENT_start = "/*";

    public static final String COMMENT_end = "*/";

    public static int findLastIndexOf(String content, String symbol) {
        List<int[]> commentRanges = commentRanges(content);
        int max = -2;
        int index = 0;
        int pos = -2;
        while ((pos = content.indexOf(symbol, index)) >= 0) {
            if (pos >= 0) {
                if (max < pos && !inRange(commentRanges, pos)) {
                    max = pos;
                }
                index = pos + 1;
            } else {
                break;
            }
        }
        return max;
    }

    public static int findIndexOf(String content, String symbol) {
        List<int[]> commentRanges = commentRanges(content);
        int index = 0;
        int pos = -2;
        while ((pos = content.indexOf(symbol, index)) >= 0) {
            if (pos >= 0) {
                if (!inRange(commentRanges, pos)) {
                    return pos;
                }
                index = pos + 1;
            } else {
                break;
            }
        }
        return -2;
    }

    public static int findNextCauseIndex(String queryLowercase, int start, List<String> orderKeyList) {
        List<int[]> commentRanges = commentRanges(queryLowercase);
        int position = queryLowercase.length();
        int pos = 0;
        for (int i = 0; i < orderKeyList.size(); i++) {
            pos = queryLowercase.indexOf(orderKeyList.get(i), start);
            while (inRange(commentRanges, pos)) {
                pos = queryLowercase.indexOf(orderKeyList.get(i), pos + 1);
                if (pos < 0) {
                    break;
                }
            }
            if (pos >= 0 && pos != position && position > pos) {
                position = pos;
            }
        }
        if (position == queryLowercase.length()) {
            position = -2;
        }
        return position;
    }

    public static String findNextCause(String queryLowercase, int start, List<String> orderKeyList) {
        List<int[]> commentRanges = commentRanges(queryLowercase);
        int position = queryLowercase.length();
        int pos = 0;
        String causeName = null;
        String temp = null;
        for (int i = 0; i < orderKeyList.size(); i++) {
            temp = orderKeyList.get(i);
            pos = queryLowercase.indexOf(temp, start);
            while (inRange(commentRanges, pos)) {
                pos = queryLowercase.indexOf(orderKeyList.get(i), pos + 1);
                if (pos < 0) {
                    break;
                }
            }
            if (pos >= 0 && pos != position && pos < position) {
                position = pos;
                causeName = temp.trim();
                if (causeName.endsWith(PARAM_start)) {
                    causeName = causeName.substring(0, causeName.length() - 1);
                }
            }
        }
        if (position == queryLowercase.length()) {
            causeName = null;
        }
        return causeName;
    }

    public static int findNextLogicUnitIndex(String queryLowercase, int start, List<String> orderKeyList) {
        List<int[]> commentRanges = commentRanges(queryLowercase);
        int position = queryLowercase.length();
        int pos = 0;
        for (int i = 0; i < orderKeyList.size(); i++) {
            pos = queryLowercase.indexOf(orderKeyList.get(i), start);
            while (inRange(commentRanges, pos)) {
                pos = queryLowercase.indexOf(orderKeyList.get(i), pos + 1);
                if (pos < 0) {
                    break;
                }
            }
            if (pos >= 0 && pos != position && position > pos) {
                position = Math.min(position, pos);
            }
        }
        if (position == queryLowercase.length()) {
            position = -2;
        }
        return position;
    }

    public static String findNextLogicUnit(String queryLowercase, int start, List<String> orderKeyList) {
        List<int[]> commentRanges = commentRanges(queryLowercase);
        int position = queryLowercase.length();
        int pos = 0;
        String causeName = null;
        String temp = null;
        for (int i = 0; i < orderKeyList.size(); i++) {
            temp = orderKeyList.get(i);
            pos = queryLowercase.indexOf(temp, start);
            while (inRange(commentRanges, pos)) {
                pos = queryLowercase.indexOf(orderKeyList.get(i), pos + 1);
                if (pos < 0) {
                    break;
                }
            }
            if (pos >= 0 && pos != position && pos < position) {
                position = pos;
                causeName = temp.trim();
            }
        }
        if (position == queryLowercase.length()) {
            causeName = null;
        }
        return causeName;
    }

    public static boolean inRange(List<int[]> ranges, int index) {
        if (index < 0) {
            return false;
        }
        for (int[] range : ranges) {
            if (index >= range[0] && index < range[1]) {
                return true;
            }
        }
        return false;
    }

    public static List<int[]> commentRanges(String content) {
        List<int[]> commentList = new ArrayList<int[]>();
        int start = 0;
        int end = 0;
        int[] range = null;
        start = content.indexOf(COMMENT_start, 0);
        end = content.indexOf(COMMENT_end, 0);
        if (start >= 0 && end >= 0 && start > end) {
            throw new RuntimeException("end '*/' should be after '/*', Please check source:" + content);
        }
        end = 0;
        do {
            start = content.indexOf(COMMENT_start, end);
            if (start >= 0) {
                end = content.indexOf(COMMENT_end, start + 1);
                if (end >= 0) {
                    range = new int[2];
                    range[0] = start;
                    end = end + COMMENT_end.length();
                    range[1] = end;
                    commentList.add(range);
                } else {
                    throw new RuntimeException("'/*' not end with '*/'");
                }
            } else {
                end = content.indexOf(COMMENT_end, end + 1);
                if (end >= 0) {
                    throw new RuntimeException("end '*/' not start with '/*'");
                }
            }
        } while (start >= 0 && end >= 0);
        return commentList;
    }
}
