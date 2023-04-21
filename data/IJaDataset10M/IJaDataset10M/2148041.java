package NCParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;

public class NCParser {

    NCParser() {
    }

    public static boolean isToolCall(String str) {
        int mPos = -1;
        int gPos = -1;
        mPos = str.indexOf("M06");
        gPos = str.indexOf("G17");
        return (mPos > -1 || gPos > -1);
    }

    public static boolean isTolerance(String str) {
        if (str.indexOf("G62T") > -1) return true;
        return false;
    }

    public static String getCodeNumber(char pre, char sur, String str) {
        int prePos = -1;
        int surPos = -1;
        prePos = str.indexOf(pre);
        surPos = str.indexOf(sur);
        if (sur == '\n') surPos = str.length();
        if (prePos < 0 || surPos < 0 || prePos >= surPos) {
            System.out.println("Error" + prePos + surPos);
            return null;
        }
        String retStr = new String(str.substring(prePos + 1, surPos));
        return retStr;
    }

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String extractValue(String code, String str) {
        String value = new String("");
        int pos = str.indexOf(code);
        if (pos > -1) {
            for (int i = 0; i < (str.length() - (pos + 1)); i++) {
                String tmp = str.substring((i + (pos + 1)), (i + (pos + 1) + 1));
                if (isNumber(tmp) || tmp.equals(".") || tmp.equals("+") || tmp.equals("-")) {
                    value += tmp;
                } else {
                    break;
                }
            }
        }
        System.out.println("extractValue : " + value);
        System.out.println("extractValue source : " + str);
        return value;
    }

    public static String extractValueWithCode(String code, String str) {
        return (code + extractValue(code, str));
    }

    public static int getNumcntFromCode(String str) {
        int numCnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).equals(".")) break;
            if (NCParser.isNumber(str.substring(i, i + 1))) numCnt++;
        }
        return numCnt;
    }

    public static String trimZero(String str) {
        Double value = new Double(str);
        String pattern = "#####.###";
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(value);
    }

    public static int getDotPos(String value) {
        int dotPos = value.indexOf(".");
        if (dotPos == -1) dotPos = value.length();
        return dotPos;
    }

    public static int getLineNumber(String filename) throws Exception {
        int lineCnt = 0;
        File linf = new File(filename);
        FileInputStream lfis = new FileInputStream(linf);
        InputStreamReader lisr = new InputStreamReader(lfis);
        LineNumberReader lReader = new LineNumberReader(lisr);
        lReader.skip(linf.length());
        lineCnt = lReader.getLineNumber();
        lReader.close();
        lReader = null;
        return lineCnt;
    }

    protected static int getPrePos(char pre, String srcCode) {
        return (srcCode.indexOf(pre));
    }

    protected static int getSurPos(char sur, String srcCode) {
        return (srcCode.indexOf(sur));
    }
}
