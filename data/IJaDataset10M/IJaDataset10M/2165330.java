package educate.lcms;

import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class VetStringAnalyzer {

    public static void main(String[] args) {
        String s = args[0];
        Vector list = new Vector();
        if (doProcess(s, list) == 1) {
            System.out.println("TRUE");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((String) list.elementAt(i));
            }
        } else {
            System.out.println("FALSE");
        }
    }

    public static int doProcess(String s, Vector list) {
        int group_num = 0;
        StringBuffer temp = new StringBuffer("");
        StringBuffer r = new StringBuffer();
        boolean in = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                in = true;
                temp = new StringBuffer("");
                continue;
            }
            if (c == ')') {
                group_num++;
                if (temp.toString().indexOf("-") > 0) {
                    String s1 = temp.substring(0, temp.toString().indexOf("-"));
                    String s2 = temp.substring(temp.toString().indexOf("-") + 1);
                    int q = getQualified(s1, s2);
                    r.append(Integer.toString(q));
                    if (q == 1) {
                        list.addElement(Integer.toString(group_num));
                    }
                } else {
                    r.append(temp);
                    if (Integer.parseInt(temp.toString().trim()) == 1) {
                        list.addElement(Integer.toString(group_num));
                    }
                }
                in = false;
                continue;
            }
            if (in) {
                temp.append(c);
                continue;
            }
            r.append(c);
        }
        int result = 0;
        if (r.toString().indexOf("-") > 0) {
            String s1 = r.substring(0, r.toString().indexOf("-"));
            String s2 = r.substring(r.toString().indexOf("-") + 1);
            result = getQualified(s1, s2);
        } else {
            result = Integer.parseInt(r.toString().trim());
        }
        return result;
    }

    static int getQualified(String s1, String s2) {
        int count = s1.length();
        int[] data = new int[count], condition = new int[count - 1];
        for (int i = 0; i < s1.length(); i++) {
            data[i] = Integer.parseInt(new Character(s1.charAt(i)).toString());
        }
        for (int i = 0; i < s2.length(); i++) {
            condition[i] = Integer.parseInt(new Character(s2.charAt(i)).toString());
        }
        return getQualified(data, condition);
    }

    static int getQualified(int[] data, int[] condition) {
        int x = data[0];
        for (int i = 1; i < data.length; i++) {
            if (condition[i - 1] == 1) {
                x = x & data[i];
            } else {
                x = x | data[i];
            }
        }
        return x;
    }
}
