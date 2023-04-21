package org.osee.indexer.basicDataMining;

import java.util.Date;

public class SohuBlog {

    private static String[] del = { "�Ѻ��", "����", "��" };

    private static boolean isTime(String content) {
        int op = 0;
        if (content.charAt(op) < '1' || content.charAt(op) > '2') {
            return false;
        }
        for (int i = 1; i < 4; i++) {
            if (content.charAt(op + i) < '0' || content.charAt(op + i) > '9') {
                return false;
            }
        }
        op += 5;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (content.charAt(op + j) < '0' || content.charAt(op + j) > '9') {
                    return false;
                }
            }
            op += 3;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public static Date getTime(String content) {
        int a = content.indexOf("<a href=\"http://blog.sohu.com\">�Ѻ��</a>") + "<a href=\"http://blog.sohu.com\">�Ѻ��</a>".length();
        int i = a + content.substring(a).indexOf("<div class=\"item-title\">") + "<div class=\"item-title\">".length();
        for (; i < content.length(); i++) {
            if (isTime(content.substring(i))) {
                break;
            }
        }
        int year = 0;
        for (int j = 0; j < 4; j++) {
            year = year * 10 + content.charAt(i++) - '0';
        }
        i++;
        int month = 0;
        for (int j = 0; j < 2; j++) {
            month = month * 10 + content.charAt(i++) - '0';
        }
        i++;
        int day = 0;
        for (int j = 0; j < 2; j++) {
            day = day * 10 + content.charAt(i++) - '0';
        }
        i++;
        while (content.charAt(i) == ' ') {
            i++;
        }
        Date tmp = new Date(year - 1900, month - 1, day, 0, 0, 0);
        return tmp;
    }

    public static String getID(String content) {
        int a = content.indexOf("<a href=\"http://blog.sohu.com\">�Ѻ��</a>") + "<a href=\"http://blog.sohu.com\">�Ѻ��</a>".length();
        int b = a + content.substring(a).indexOf('>') + 1;
        int c = b + content.substring(b).indexOf('<');
        String tmp = content.substring(b, c);
        int d = -1;
        for (int i = 0; i < del.length; i++) {
            d = tmp.indexOf(del[i]);
            if (d != -1) {
                break;
            }
        }
        if (d != -1) tmp = tmp.substring(0, d);
        return tmp;
    }
}
