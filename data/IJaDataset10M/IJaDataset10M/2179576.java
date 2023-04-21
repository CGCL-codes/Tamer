package magicstudio.util;

import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.ParseException;

/**
 * Date: 2004-1-11
 * Time: 12:23:33
 * Desc: Utilities to handle chinese number system, e.g. ��ʮ�塱���İ���ʮ����
 *       charactor includes �����㣩һ�����������߰˾�ʮ��ǧ��
 *       Note: Only support integer
 * Progress: ready to run, does not support ǧ����
 */
public class ChineseNumber {

    private static interface ConvertStrategy {

        int convert(String chNumber);
    }

    private static Map<Pattern, ConvertStrategy> convertMap = new HashMap<Pattern, ConvertStrategy>();

    private static Map<Character, Integer> digitMap = new HashMap<Character, Integer>(20);

    private static void register(String chNumPattern, ConvertStrategy conStr) {
        Pattern p = null;
        try {
            p = Pattern.compile(chNumPattern);
        } catch (PatternSyntaxException e) {
            assert false;
        }
        convertMap.put(p, conStr);
    }

    static {
        buildDigitMap();
        buildConvertMap();
    }

    private static void buildDigitMap() {
        digitMap.put('?', 0);
        digitMap.put('?', 0);
        digitMap.put('?', 1);
        digitMap.put('?', 2);
        digitMap.put('?', 3);
        digitMap.put('?', 4);
        digitMap.put('?', 5);
        digitMap.put('?', 6);
        digitMap.put('?', 7);
        digitMap.put('?', 8);
        digitMap.put('?', 9);
    }

    private static void buildConvertMap() {
        register("[һ�����������߰˾ũ���]+", new ConvertStrategy() {

            public int convert(String chNumber) {
                return convertRaw(chNumber);
            }
        });
        register("ʮ[һ�����������߰˾�]?", new ConvertStrategy() {

            public int convert(String chNumber) {
                return 10 + convertRaw(chNumber.substring(1));
            }
        });
        register("[һ�����������߰˾�]ʮ[һ�����������߰˾�]?", new ConvertStrategy() {

            public int convert(String chNumber) {
                return 10 * convertRaw(chNumber.charAt(0)) + convertRaw(chNumber.substring(2));
            }
        });
        register("[һ�����������߰˾�]��[����][һ�����������߰˾�]", new ConvertStrategy() {

            public int convert(String chNumber) {
                return 100 * convertRaw(chNumber.charAt(0)) + convertRaw(chNumber.charAt(3));
            }
        });
        register("[һ�����������߰˾�]��([һ�����������߰˾�]ʮ[һ�����������߰˾�]?)?", new ConvertStrategy() {

            public int convert(String chNumber) {
                return 100 * convertRaw(chNumber.charAt(0)) + recursiveParse(chNumber.substring(2));
            }
        });
    }

    private static int recursiveParse(String chNumber) {
        try {
            return parse(chNumber);
        } catch (ParseException e) {
            assert false;
            return 0;
        }
    }

    private static int convertRaw(String chNumber) {
        if (chNumber == null || chNumber.equals("")) return 0;
        int value = 0;
        for (int i = 0; i < chNumber.length(); i++) {
            value = value * 10 + convertRaw(chNumber.charAt(i));
        }
        return value;
    }

    private static int convertRaw(char chDigit) {
        assert digitMap.containsKey(chDigit);
        return digitMap.get(chDigit);
    }

    public static int parse(String chNumber) throws ParseException {
        for (Map.Entry<Pattern, ConvertStrategy> entry : convertMap.entrySet()) {
            if (entry.getKey().matcher(chNumber).matches()) return entry.getValue().convert(chNumber);
        }
        throw new ParseException("Can not parse " + chNumber, 0);
    }

    public static class TheTest extends TestCase {

        public void testParse() {
            try {
                assertEquals(8, ChineseNumber.parse("��"));
                assertEquals(79, ChineseNumber.parse("�߾�"));
                assertEquals(12, ChineseNumber.parse("ʮ��"));
                assertEquals(31, ChineseNumber.parse("��ʮһ"));
                assertEquals(503, ChineseNumber.parse("�婖��"));
                assertEquals(499, ChineseNumber.parse("�ľž�"));
                assertEquals(200, ChineseNumber.parse("����"));
                assertEquals(405, ChineseNumber.parse("�İ٩���"));
                assertEquals(20, ChineseNumber.parse("��ʮ"));
                assertEquals(10, ChineseNumber.parse("ʮ"));
                assertEquals(10, ChineseNumber.parse("һʮ"));
                assertEquals(100, ChineseNumber.parse("һ��"));
                assertEquals(7, ChineseNumber.parse("������"));
                assertEquals(0, ChineseNumber.parse("��"));
                assertEquals(231, ChineseNumber.parse("������ʮһ"));
                assertEquals(630, ChineseNumber.parse("�����ʮ"));
            } catch (java.text.ParseException e) {
                System.out.println(e.toString());
                fail("should not throw exception");
            }
        }
    }
}
