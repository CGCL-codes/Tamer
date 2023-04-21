package cn.sduo.app.util.lang;

import hk.gov.ogcio.egis.rm.chinesehandling.CodepointSetUtil;
import hk.gov.ogcio.egis.rm.chinesehandling.ValidationUtil;
import hk.gov.ogcio.egis.rm.chinesehandling.ValidationUtil.Mode;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cn.sduo.app.util.IOPUtil;

public class TradSimpChineseUtil {

    private static final Log LOG = LogFactory.getLog(TradSimpChineseUtil.class);

    private static EnumSet<Mode> modeSetTC = EnumSet.noneOf(ValidationUtil.Mode.class);

    private static EnumSet<Mode> modeSetTCNumeric = EnumSet.noneOf(ValidationUtil.Mode.class);

    private static EnumSet<Mode> modeSetTCAlphaNumeric = EnumSet.noneOf(ValidationUtil.Mode.class);

    static {
        modeSetTC.add(ValidationUtil.Mode.CHINESE);
        modeSetTC.add(ValidationUtil.Mode.SPACE);
        modeSetTCNumeric.add(ValidationUtil.Mode.CHINESE);
        modeSetTCNumeric.add(ValidationUtil.Mode.DIGIT);
        modeSetTCNumeric.add(ValidationUtil.Mode.SPACE);
        modeSetTCNumeric.add(ValidationUtil.Mode.PUNCTUATION);
        modeSetTCAlphaNumeric.add(ValidationUtil.Mode.CHINESE);
        modeSetTCAlphaNumeric.add(ValidationUtil.Mode.ALPHABET);
        modeSetTCAlphaNumeric.add(ValidationUtil.Mode.DIGIT);
        modeSetTCAlphaNumeric.add(ValidationUtil.Mode.SPACE);
        modeSetTCAlphaNumeric.add(ValidationUtil.Mode.PUNCTUATION);
        try {
            init();
        } catch (Exception ex) {
            LOG.error("Unable to init ChineseUtil: ", ex);
        }
    }

    private static TradSimpChineseUtil cu = null;

    private static final String CONFIG_PATH_PFX = "/com/pccw/oframework/utility/validation";

    private static final String HKSCS_IN_ISO = CONFIG_PATH_PFX + "/HKSCSSet.txt";

    private static final String BIG5_IN_ISO = CONFIG_PATH_PFX + "/Big5Set.txt";

    private static final String HKSCSCP_SET = CONFIG_PATH_PFX + "/HKSCSCPSet.txt";

    private static final String USER_EXCLLUDE_SET = CONFIG_PATH_PFX + "/UserExcludeSet.txt";

    private static final String ADDITIONAL_SET_RESOURCE = CONFIG_PATH_PFX + "/AdditionalSet.txt";

    private static final String GB2312_SET_RESOURCE = CONFIG_PATH_PFX + "/GB2312Set.txt";

    private static final String GBK_SET_RESOURCE = CONFIG_PATH_PFX + "/GBKSet.txt";

    private ValidationUtil chineseValidUtil;

    /** Creates a new instance of Convertor. */
    private TradSimpChineseUtil() throws Exception {
        InputStream is1 = IOPUtil.getResourceAsStream(BIG5_IN_ISO);
        Set<String> set1 = CodepointSetUtil.loadCodepointSetFromInputStream(is1);
        InputStream is2 = IOPUtil.getResourceAsStream(HKSCS_IN_ISO);
        Set<String> set2 = CodepointSetUtil.loadCodepointSetFromInputStream(is2);
        InputStream is3 = IOPUtil.getResourceAsStream(HKSCSCP_SET);
        Set<String> set3 = CodepointSetUtil.loadCodepointSetFromInputStream(is3);
        InputStream is4 = IOPUtil.getResourceAsStream(ADDITIONAL_SET_RESOURCE);
        Set<String> set4 = CodepointSetUtil.loadCodepointSetFromInputStream(is4);
        InputStream is5 = IOPUtil.getResourceAsStream(USER_EXCLLUDE_SET);
        Set<String> set5 = CodepointSetUtil.loadCodepointSetFromInputStream(is5);
        InputStream is6 = IOPUtil.getResourceAsStream(GB2312_SET_RESOURCE);
        Set<String> set6 = CodepointSetUtil.loadCodepointSetFromInputStream(is6);
        InputStream is7 = IOPUtil.getResourceAsStream(GBK_SET_RESOURCE);
        Set<String> set7 = CodepointSetUtil.loadCodepointSetFromInputStream(is7);
        chineseValidUtil = new ValidationUtil();
        chineseValidUtil.setBaseSet(set1);
        chineseValidUtil.setInclusionSet(set2);
        chineseValidUtil.setInclusionSet(set3);
        chineseValidUtil.setInclusionSet(set4);
        chineseValidUtil.setInclusionSet(set6);
        chineseValidUtil.setInclusionSet(set7);
        chineseValidUtil.setExclusionSet(set5);
        chineseValidUtil.setCPSet(set3);
    }

    public static synchronized TradSimpChineseUtil getInstance() throws Exception {
        if (cu == null) init();
        return cu;
    }

    public static synchronized void init() throws Exception {
        cu = null;
        cu = new TradSimpChineseUtil();
    }

    public boolean isChineseAlphaNumeric(String data) {
        if (data == null) return false; else if ("".equals(data)) return false; else LOG.trace("isChineseAlphaNumeric check=" + TradSimpChineseUtil.dumpCodepointHex(data));
        return chineseValidUtil.checkString(data, modeSetTCAlphaNumeric, null, null);
    }

    public boolean isChinese(String data) {
        if (data == null) return false; else if ("".equals(data)) return false; else LOG.trace("isChinese check=" + TradSimpChineseUtil.dumpCodepointHex(data));
        return chineseValidUtil.checkString(data, modeSetTC, null, null);
    }

    public boolean isChineseNumeric(String data) {
        if (data == null) return false; else if ("".equals(data)) return false; else LOG.trace("isChineseNumeric check=" + TradSimpChineseUtil.dumpCodepointHex(data));
        return chineseValidUtil.checkString(data, modeSetTCNumeric, null, null);
    }

    public String convertFullWidthToHalfWidth(String data) {
        if (data == null) return null;
        return chineseValidUtil.convertFullWidthToHalfWidth(data);
    }

    public String replaceInvalidChars(String data) {
        if (data == null) return null;
        return chineseValidUtil.replaceInvalidChars(data, modeSetTCAlphaNumeric, null, null);
    }

    protected String extractPUAChars(String data) {
        if (data == null) return null;
        return chineseValidUtil.extractPUAChars(data);
    }

    protected String extractCPChars(String data) {
        if (data == null) return null;
        return chineseValidUtil.extractCPChars(data);
    }

    public String extractPUACheckChars(String data) {
        if (data == null) return null;
        String puaString = chineseValidUtil.extractPUAChars(data);
        String cpString = chineseValidUtil.extractCPChars(data);
        return unionStrings(puaString, cpString);
    }

    public static String[] toStringArray(String pInputString) {
        Vector<String> strings = new Vector<String>();
        String tmpStr;
        int i = 0;
        while (pInputString != null && i < pInputString.length()) {
            int codePoint = pInputString.codePointAt(i);
            if (Character.isHighSurrogate(pInputString.charAt(i))) {
                i = i + 2;
            } else {
                i++;
            }
            tmpStr = new String(new int[] { codePoint }, 0, 1);
            strings.add(tmpStr);
        }
        if (strings.size() == 0) {
            return null;
        }
        return strings.toArray(new String[] {});
    }

    public static String unionStrings(String data1, String data2) {
        String pInputString = "";
        if (data1 != null) {
            pInputString = pInputString + data1;
        }
        if (data2 != null) pInputString = pInputString + data2;
        Set<String> foundSet = new TreeSet<String>();
        String tmpStr;
        int i = 0;
        while (pInputString != null && i < pInputString.length()) {
            int codePoint = pInputString.codePointAt(i);
            if (Character.isHighSurrogate(pInputString.charAt(i))) {
                i = i + 2;
            } else {
                i++;
            }
            tmpStr = new String(new int[] { codePoint }, 0, 1);
            if (!foundSet.contains(tmpStr)) {
                foundSet.add(tmpStr);
            }
        }
        if (foundSet.size() == 0) {
            return null;
        }
        StringBuffer retStrBuf = new StringBuffer(foundSet.size());
        for (String str : foundSet) {
            retStrBuf.append(str);
        }
        return retStrBuf.toString();
    }

    public void printAllPUAChars() {
        int counts = 0xF8FF - 0xE000 + 1;
        int[] codePoints = new int[counts];
        for (int i = 0; i < counts; i++) {
            codePoints[i] = i + 0xE000;
        }
        String testString = new String(codePoints, 0, codePoints.length);
        System.out.println("PUA strings:");
        System.out.println(testString);
    }

    /**
     * Converts String in UTF-8 to Hex String.
     * <p>
     * 
     * @param pInStr
     *            String to convert.
     * @return String converted.
     */
    public static String dumpUtf8Hex(String pInStr) throws UnsupportedEncodingException {
        int i = 0;
        byte[] utfBytes = null;
        utfBytes = pInStr.getBytes("UTF8");
        StringBuffer retStrBuf = new StringBuffer(utfBytes.length * 5);
        for (i = 0; i < utfBytes.length; i++) {
            retStrBuf.append(" 0x");
            retStrBuf.append(Integer.toHexString((0xFF & utfBytes[i])));
        }
        return retStrBuf.toString();
    }

    /**
     * Converts String in UTF-16 to Hex String.
     * <p>
     * 
     * @param pInStr
     *            String to convert.
     * @return String converted.
     */
    public static String dumpUtf16Hex(String pInStr) {
        int i = 0;
        char aChar = 0;
        StringBuffer retStrBuf = new StringBuffer(pInStr.length() * 6);
        while (i < pInStr.length()) {
            aChar = pInStr.charAt(i);
            if (Character.isHighSurrogate(aChar)) {
                retStrBuf.append(" {");
            }
            retStrBuf.append("0x");
            retStrBuf.append(Integer.toHexString(aChar));
            if (Character.isLowSurrogate(aChar)) {
                retStrBuf.append('}');
            }
            retStrBuf.append(' ');
            i++;
        }
        return retStrBuf.toString();
    }

    public static String dumpCodepointHex(String pInStr) {
        if (pInStr == null) return null;
        int i = 0;
        StringBuffer retStrBuf = new StringBuffer(pInStr.length() * 6);
        while (i < pInStr.length()) {
            retStrBuf.append(" 0x");
            retStrBuf.append(Integer.toHexString(pInStr.codePointAt(i)));
            if (Character.isHighSurrogate(pInStr.charAt(i))) {
                i = i + 2;
            } else {
                i++;
            }
        }
        return retStrBuf.toString();
    }

    public static String dumpByteArrayToHex(byte[] pInByteArr) {
        int i = 0;
        StringBuffer retStrBuf = new StringBuffer(pInByteArr.length * 5);
        for (i = 0; i < pInByteArr.length; i++) {
            retStrBuf.append(" 0x");
            retStrBuf.append(Integer.toHexString((0xFF & pInByteArr[i])));
        }
        return retStrBuf.toString();
    }

    public static byte[] getByteArrFromHexString(String pInStr) {
        int i = 0;
        int j = 0;
        byte[] tmpByteArr = new byte[pInStr.length()];
        StringTokenizer tokenizer = new StringTokenizer(pInStr);
        String strToken;
        while (tokenizer.hasMoreTokens()) {
            strToken = tokenizer.nextToken();
            j = 0;
            while (j < strToken.length()) {
                tmpByteArr[i++] = (byte) Integer.parseInt(strToken.substring(j, j + 2 > strToken.length() ? strToken.length() : j + 2), 16);
                j = j + 2;
            }
        }
        byte[] retByteArr = new byte[i];
        System.arraycopy(tmpByteArr, 0, retByteArr, 0, i);
        return retByteArr;
    }

    public ChineseCheckResult checkChinese(String input) {
        boolean result = isChinese(input);
        String puaString = null;
        puaString = this.extractPUACheckChars(input);
        return new ChineseCheckResult(result, input, puaString);
    }

    public static class ChineseCheckResult {

        String inputString;

        String puaCheckString;

        boolean valid;

        boolean havePUA;

        protected ChineseCheckResult(boolean result, String inputString, String puaCheckString) {
            this.inputString = inputString;
            this.puaCheckString = puaCheckString;
            valid = result;
            if (puaCheckString != null && puaCheckString.length() > 0) havePUA = true; else havePUA = false;
        }

        public boolean isValid() {
            return valid;
        }

        public boolean havePUA() {
            return havePUA;
        }

        public String getInputString() {
            return inputString;
        }

        public String getPUAString() {
            return puaCheckString;
        }
    }

    public static void main(String[] args) throws Exception {
        TradSimpChineseUtil cu = TradSimpChineseUtil.getInstance();
        int[] codePoints = new int[] { 0x66fe, 0x8efb, 0x9B43, 0x8BFB, 0x8B80 };
        String testString = new String(codePoints, 0, codePoints.length);
        System.out.println("testString codepoints=" + TradSimpChineseUtil.dumpCodepointHex(testString));
        byte[] haha = testString.getBytes("GB2312");
        String convertedString = new String(haha, "GB2312");
        System.out.println("converted codepoints=" + TradSimpChineseUtil.dumpCodepointHex(convertedString));
        System.out.println("testString=" + testString);
        System.out.println("testString codepoints=" + TradSimpChineseUtil.dumpCodepointHex(testString));
        System.out.println("isChinese=" + cu.isChinese(testString));
        System.out.println("isChineseNumeric=" + cu.isChineseNumeric(testString));
        System.out.println("isChineseAlphaNumeric=" + cu.isChineseAlphaNumeric(testString));
        System.out.println("extractPUAChars=" + TradSimpChineseUtil.dumpCodepointHex(cu.extractPUAChars(testString)));
        System.out.println("extractCPChars=" + TradSimpChineseUtil.dumpCodepointHex(cu.extractCPChars(testString)));
        System.out.println("extractPUACheckChar=" + TradSimpChineseUtil.dumpCodepointHex(cu.extractPUACheckChars(testString)));
        InputStream is1 = IOPUtil.getResourceAsStream(BIG5_IN_ISO);
        Set<String> set1 = CodepointSetUtil.loadCodepointSetFromInputStream(is1);
        InputStream is2 = IOPUtil.getResourceAsStream(HKSCS_IN_ISO);
        Set<String> set2 = CodepointSetUtil.loadCodepointSetFromInputStream(is2);
        InputStream is3 = IOPUtil.getResourceAsStream(GB2312_SET_RESOURCE);
        Set<String> set3 = CodepointSetUtil.loadCodepointSetFromInputStream(is3);
        int i = 0;
        for (String s : set1) {
            String x = ChineseTranslator.translateCht2Chs(s);
            System.out.print(s + "->" + x);
            System.out.print(ChineseUtil.isChinese(s));
            System.out.print("\n");
            i++;
        }
        i = 0;
        for (String s : set2) {
            String x = ChineseTranslator.translateCht2Chs(s);
            System.out.print(s + "->" + x);
            System.out.print(ChineseUtil.isChinese(s));
            System.out.print("\n");
            i++;
        }
        i = 0;
        for (String s : set3) {
            String x = ChineseTranslator.translateCht2Chs(s);
            System.out.print(s + "->" + x);
            System.out.print(ChineseUtil.isChinese(s));
            System.out.print("\n");
            i++;
        }
    }
}
