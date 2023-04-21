package cn.lzh.common.string.chinese;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * ����������ַ���ת������.
 * ��Ҫ��ӳ���ļ�:/snt/common/string/chinese/tc2sc.table��
 * 				 /snt/common/string/chinese/sc2tc.table
 * ӳ���ļ����Ը�ݾ����������ӳ�䲻��ȷ����. 
 * @author <a href="mailto:sealinglip@gmail.com">Sealinglip</a>
 */
public class ChineseConvertor {

    private static ChineseConvertor pInstance = null;

    private char[] c_tc2sc = null;

    private char[] c_sc2tc = null;

    static {
        pInstance = getInstance("/snt/common/string/chinese/tc2sc.table", "/snt/common/string/chinese/sc2tc.table");
    }

    /**
	 * ָ����ӳ���ļ������г�ʼ��
	 * @param s_tc2scTable
	 * @param s_sc2tcTable
	 * @throws NullPointerException
	 */
    private ChineseConvertor(String s_tc2scTable, String s_sc2tcTable) throws NullPointerException {
        if (null == c_tc2sc) {
            c_tc2sc = getCharsFromFile(s_tc2scTable);
        }
        if (null == c_tc2sc) {
            throw new NullPointerException("No traditional chinese to simplified chinese map table can be loaded!");
        }
        if (null == c_sc2tc) {
            c_sc2tc = getCharsFromFile(s_sc2tcTable);
        }
        if (null == c_sc2tc) {
            throw new NullPointerException("No traditional chinese to simplified chinese map table can be loaded!");
        }
    }

    /**
	 * ȡ�ü�ת��������Ĭ����?
	 * @return ChineseConvertor
	 */
    public static ChineseConvertor getInstance() {
        return pInstance;
    }

    /**
	 * ��ָ�����ȡ�ü�ת����
	 * @param s_tc2scTable
	 * @param s_sc2tcTable
	 * @return ChineseConvertor
	 */
    public static ChineseConvertor getInstance(String s_tc2scTable, String s_sc2tcTable) {
        try {
            return new ChineseConvertor(s_tc2scTable, s_sc2tcTable);
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * У���嵽�����ӳ��
	 * һ�㵱�����ַ�ӳ�䲻��ȷ��ʱ�����ͨ�����������У��. 
	 * @param src
	 * @param map
	 * @throws Exception
	 */
    public synchronized void resetTc2ScMap(String src, String map) throws Exception {
        if (src == null || map == null) {
            return;
        } else if (src.length() != map.length()) {
            throw new IllegalArgumentException("The two string's length are not equal.");
        }
        char[] cSrc = src.toCharArray();
        char[] cMap = map.toCharArray();
        for (int i = 0; i < cSrc.length; i++) {
            if (cSrc[i] >= 0x4e00 && cSrc[i] <= 0x9fa5) {
                c_tc2sc[cSrc[i] - 0x4e00] = cMap[i];
            }
        }
        BufferedWriter pWriter = new BufferedWriter(new FileWriter("tc2sc.table"));
        pWriter.write(c_tc2sc, 0, c_tc2sc.length);
        pWriter.close();
    }

    /**
	 * У����嵽�����ӳ��
	 * һ�㵱�����ַ�ӳ�䲻��ȷ��ʱ�����ͨ�����������У��.
	 * @param src
	 * @param map
	 * @throws Exception
	 */
    public synchronized void resetSc2TcMap(String src, String map) throws Exception {
        if (src == null || map == null) {
            return;
        } else if (src.length() != map.length()) {
            throw new IllegalArgumentException("The two string's length are not equal.");
        }
        char[] cSrc = src.toCharArray();
        char[] cMap = map.toCharArray();
        for (int i = 0; i < cSrc.length; i++) {
            if (cSrc[i] >= 0x4e00 && cSrc[i] <= 0x9fa5) {
                c_sc2tc[cSrc[i] - 0x4e00] = cMap[i];
            }
        }
        BufferedWriter pWriter = new BufferedWriter(new FileWriter("sc2tc.table"));
        pWriter.write(c_sc2tc, 0, c_sc2tc.length);
        pWriter.close();
    }

    private String convert(String inStr, char[] cTable) {
        if (null == inStr || inStr.length() == 0) {
            return inStr;
        }
        char[] cs = inStr.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            if (c >= 0x4e00 && c <= 0x9fa5) {
                char c1 = cTable[c - 0x4e00];
                if (c1 > 0) cs[i] = c1;
            }
        }
        return new String(cs);
    }

    /**
	 * �Ѽ����ַ�ת���ɷ����ַ�
	 * @param c
	 * @return char
	 */
    public char sc2tc(char c) {
        if (c >= 0x4e00 && c <= 0x9fa5) {
            char c1 = c_sc2tc[c - 0x4e00];
            if (c1 > 0) return c1;
        }
        return c;
    }

    /**
	 * �Ѽ����ַ�ת���ɷ����ַ�
	 * @param inStr
	 * @return String
	 */
    public String sc2tc(String inStr) {
        return convert(inStr, c_sc2tc);
    }

    /**
	 * �Ѽ����ַ�ת���ɷ����ַ�
	 * @param inStr
	 * @param off
	 * @param len
	 * @return String
	 */
    public String sc2tc(String inStr, int off, int len) {
        return convert(inStr.substring(off, off + len), c_sc2tc);
    }

    /**
	 * �Ѽ����ַ�ת���ɷ����ַ�
	 * @param cs
	 */
    public void sc2tc(char[] cs) {
        sc2tc(cs, 0, cs.length);
    }

    /**
	 * �Ѽ����ַ�ת���ɷ����ַ�
	 * @param cs
	 * @param off
	 * @param len
	 */
    public void sc2tc(char[] cs, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i++) {
            char c = cs[i];
            if (c >= 0x4e00 && c <= 0x9fa5) {
                char c1 = c_sc2tc[c - 0x4e00];
                if (c1 > 0) cs[i] = c1;
            }
        }
    }

    /**
	 * �ѷ����ַ�ת���ɼ����ַ�
	 * @param c
	 * @return char
	 */
    public char tc2sc(char c) {
        if (c >= 0x4e00 && c <= 0x9fa5) {
            char c1 = c_tc2sc[c - 0x4e00];
            if (c1 > 0) return c1;
        }
        return c;
    }

    /**
	 * �ѷ����ַ�ת���ɼ����ַ�
	 * @param inStr
	 * @return String
	 */
    public String tc2sc(String inStr) {
        return convert(inStr, c_tc2sc);
    }

    /**
	 * �ѷ����ַ�ת���ɼ����ַ�
	 * @param inStr
	 * @param off
	 * @param len
	 * @return String
	 */
    public String tc2sc(String inStr, int off, int len) {
        return convert(inStr.substring(off, off + len), c_tc2sc);
    }

    /**
	 * �ѷ����ַ�ת���ɼ����ַ�
	 * @param cs
	 */
    public void tc2sc(char[] cs) {
        tc2sc(cs, 0, cs.length);
    }

    /**
	 * �ѷ����ַ�ת���ɼ����ַ�
	 * @param cs
	 * @param off
	 * @param len
	 */
    public void tc2sc(char[] cs, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i++) {
            char c = cs[i];
            if (c >= 0x4e00 && c <= 0x9fa5) {
                char c1 = c_tc2sc[c - 0x4e00];
                if (c1 > 0) cs[i] = c1;
            }
        }
    }

    private static char[] getCharsFromFile(String inFileName) {
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(ChineseConvertor.class.getResourceAsStream(inFileName)));
            char[] cContent = new char[0x5a16];
            reader.read(cContent, 0, cContent.length);
            return cContent;
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: snt.common.string.chinese.ChineseConvertor [-gb | -big5] inputstring");
            System.exit(1);
            return;
        }
        boolean bIsGB = true;
        String inStr = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-gb")) {
                bIsGB = true;
            } else if (args[i].equalsIgnoreCase("-big5")) {
                bIsGB = false;
            } else {
                inStr = args[i];
            }
        }
        ChineseConvertor pTmp = ChineseConvertor.getInstance();
        String outStr = "";
        if (bIsGB) {
            outStr = pTmp.tc2sc(inStr);
        } else {
            outStr = pTmp.sc2tc(inStr);
        }
        System.out.println("String [" + inStr + "] converted into:\n[" + outStr + "]");
    }
}
