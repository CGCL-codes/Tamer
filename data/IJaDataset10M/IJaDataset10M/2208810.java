package purej.util;

import java.util.StringTokenizer;

/**
 * HTML��ƿ
 * 
 * @author leesangboo
 * 
 */
public class HTMLUtils {

    /**
     * 
     * �Է¹��� s�� ��ũ��Ʈ���� ����Ҽ� �ְ� " �� ���͸� '�� <br>
     * �±׷� ��ȯ�Ѵ�.
     * 
     * @param s
     * @return
     */
    public static String parseScriptSafeString(String s) {
        s = enter2brTag(s);
        s = dbQuter2SgQuter(s);
        return s;
    }

    /**
     * 
     * �Է¹��� str�� Enter�� <br>
     * �±׷� ��ȯ�Ͽ� ��ȯ�Ѵ�.
     * 
     * @param str
     * @return
     */
    public static String enter2brTag(String str) {
        StringTokenizer st = new StringTokenizer(str, "\n");
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            while (st.hasMoreTokens()) {
                sb.append(st.nextToken());
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * 
     * �Է¹��� str�� "�� '�� ��ȯ�Ͽ� ��ȯ�Ѵ�.
     * 
     * @param str
     * @return
     */
    public static String dbQuter2SgQuter(String str) {
        if (str == null || str.trim().equals("")) return "";
        return StringUtils.replace(str, "\"", "'");
    }

    /**
     * 
     * SELECT�� s�� s1�� ������, SELECTED�� ��ȯ�Ѵ�.
     * 
     * @param s
     * @param s1
     * @return
     */
    public static String getSelected(String s, String s1) {
        if (s.equals(s1)) return " selected"; else return "";
    }

    /**
     * SELECT�� i�� j�� ������, SELECTED�� ��ȯ�Ѵ�. �� �޼ҵ忡 ���� Ŀ��Ʈ
     * 
     * @param i
     * @param j
     * @return
     */
    public static String getSelected(int i, int j) {
        if (i == j) return " selected"; else return "";
    }
}
