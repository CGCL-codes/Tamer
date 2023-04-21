package rath.nateon;

import java.util.HashMap;

/**
 * 네이트온 메신져 비밀번호 인코딩 클래스.
 * 
 * @author Jang-Ho Hwang, rath@xrath.com
 * @version 1.0.000, 2005/05/20
 */
public class NateEncode {

    private HashMap map = new HashMap();

    private HashMap padMap = new HashMap();

    private HashMap lenMap = new HashMap();

    public NateEncode() {
        init();
    }

    private void init() {
        for (int i = 0; i <= 7; i++) {
            map.put(String.valueOf(i), (7 - i) + "d");
            padMap.put(String.valueOf(i), "01101000");
        }
        map.put("8", "fd");
        map.put("9", "fd");
        padMap.put("8", "01101000");
        padMap.put("9", "01101000");
        map.put("!", "6c");
        map.put("@", "7a");
        map.put("$", "3c");
        map.put("%", "2c");
        map.put("^", "9b");
        map.put("&", "1c");
        map.put("*", "dc");
        padMap.put("!", "01100000");
        padMap.put("@", "01100000");
        padMap.put("$", "01100000");
        padMap.put("%", "01100000");
        padMap.put("^", "01100000");
        padMap.put("&", "01100000");
        padMap.put("*", "01100000");
        int start = 6;
        for (int i = 'a'; i <= 'g'; i++) {
            map.put(String.valueOf((char) i), (start - (i - 'a')) + "8");
            map.put(String.valueOf((char) (i - 0x20)), (start - (i - 'a')) + "a");
            padMap.put(String.valueOf((char) i), "01000000");
            padMap.put(String.valueOf((char) (i - 0x20)), "01010000");
        }
        start = 0x0f;
        for (int i = 'h'; i <= 'o'; i++) {
            map.put(String.valueOf((char) i), Integer.toHexString((start - (i - 'h'))) + "8");
            map.put(String.valueOf((char) (i - 0x20)), Integer.toHexString((start - (i - 'h'))) + "a");
            padMap.put(String.valueOf((char) i), "01000000");
            padMap.put(String.valueOf((char) (i - 0x20)), "01010000");
        }
        start = 7;
        for (int i = 'p'; i <= 'w'; i++) {
            map.put(String.valueOf((char) i), (start - (i - 'p')) + "9");
            map.put(String.valueOf((char) (i - 0x20)), (start - (i - 'p')) + "b");
            padMap.put(String.valueOf((char) i), "01001000");
            padMap.put(String.valueOf((char) (i - 0x20)), "01011000");
        }
        start = 0x0f;
        for (int i = 'x'; i <= 'z'; i++) {
            map.put(String.valueOf((char) i), Integer.toHexString((start - (i - 'x'))) + "9");
            map.put(String.valueOf((char) (i - 0x20)), Integer.toHexString((start - (i - 'x'))) + "b");
            padMap.put(String.valueOf((char) i), "01001000");
            padMap.put(String.valueOf((char) (i - 0x20)), "01011000");
        }
        start = 3;
        for (int i = 20; i >= 14; i--) lenMap.put(new Integer(i), "f" + (start++));
        start = 0x0a;
        for (int i = 13; i >= 8; i--) lenMap.put(new Integer(i), "e" + Integer.toHexString(start++));
        lenMap.put(new Integer(7), "e0");
        lenMap.put(new Integer(6), "e1");
    }

    public String encode(String password) {
        StringBuffer sb = new StringBuffer();
        sb.append(lenMap.get(new Integer(password.length())));
        for (int i = 0; i < password.length(); i++) {
            String s = password.substring(i, i + 1);
            String value = (String) map.get(s);
            sb.append(value);
        }
        String value = sb.toString();
        String lastChar = password.substring(password.length() - 1, password.length());
        String padRule = (String) padMap.get(lastChar);
        int sum = 0;
        int num = Integer.parseInt(((String) map.get(lastChar)).substring(0, 1), 0x10);
        StringBuffer pad = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int inc = padRule.charAt(i) - 0x30;
            if (sum == 0) {
                pad.append('0');
                if (num == 0) pad.append('1'); else pad.append(Integer.toHexString(num));
                sum = num;
                continue;
            }
            sum = sum * 2 + inc;
            int t = sum & 0xff;
            String toAdd = Integer.toHexString(t);
            if (toAdd.length() == 1) pad.append('0');
            pad.append(toAdd);
        }
        String toPad = pad.toString();
        StringBuffer base = new StringBuffer();
        base.append(toPad);
        base.append(toPad);
        base.append(toPad);
        for (int i = 0; i < value.length(); i++) {
            base.setCharAt(i, value.charAt(i));
        }
        return base.toString();
    }
}
