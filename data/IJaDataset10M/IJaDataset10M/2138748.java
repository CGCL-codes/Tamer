package com.gftech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

public class GFCommon {

    /**
	 * ��һ����������COPY������һ��������ָ����λ�á� ���maxlen����source����ĳ��ȣ�����source�����0���㹻maxlenλ��
	 * ���maxlenС��source����ĳ��ȣ����source����������ļ�λȥ����ʣ��maxlenλ
	 * 
	 * @param d
	 *            destination ����
	 * @param s
	 *            source����
	 * @param from
	 *            destination�Ŀ�ʼλ
	 * @param maxlen
	 *            source�����е����COPY��destination��֮��������ռ�е���󳤶�
	 * @return Ŀ�������ʱ��ռ�õ����һλ
	 */
    public static int bytesCopy(byte d[], byte s[], int from, int maxlen) {
        int end = from;
        if (s != null && d != null) {
            if (from >= 0 && maxlen > 0) {
                if (s.length < maxlen) {
                    for (int i = 0; i < s.length && i + from < d.length; i++) d[i + from] = s[i];
                    end = from + maxlen - 1;
                } else {
                    for (int i = 0; i < maxlen && i + from < d.length; i++) {
                        d[i + from] = s[i];
                        end = i + from;
                    }
                }
            } else if (from < 0 && maxlen > 0) {
                for (int i = d.length + from, j = 0; i > 0 && j < (s.length > maxlen ? maxlen : s.length); i--, j++) {
                    d[i] = s[j];
                    end = i;
                }
            }
        }
        return end;
    }

    /**
	 * ��һ�������е�ָ��λ�ÿ�ʼ�����ȡ��len��ֵ�� ���fromΪ��ֵ�����ָ��λ����ǰȡ��len��ֵ
	 * 
	 * @param src
	 *            Դ����
	 * @param from
	 *            ��ʼλ��
	 * @param len
	 *            COPY�ĳ���
	 * @return Դ�����һ����
	 */
    public static byte[] bytesCopy(byte[] src, int from, int len) {
        byte[] result = null;
        int totalLen = 0;
        if (src != null && src.length > 0 && len > 0) {
            if (from >= 0) {
                totalLen = src.length > from + len ? len : src.length - from;
                result = new byte[Math.abs(len)];
                for (int i = from, j = 0; i < from + totalLen; i++, j++) result[j] = src[i];
            } else {
                int i0 = src.length + from;
                if (i0 >= 0) {
                    if (i0 - len < 0) totalLen = i0 + 1; else totalLen = len;
                    result = new byte[totalLen];
                    for (int i = i0, j = 0; i >= 0 && j < totalLen; i--, j++) result[j] = src[i];
                }
            }
        }
        return result;
    }

    public static byte[] int2bytes(int a, boolean isHighFirst) {
        byte[] result = new byte[4];
        if (isHighFirst) {
            result[0] = (byte) (a >> 24 & 0xff);
            result[1] = (byte) (a >> 16 & 0xff);
            result[2] = (byte) (a >> 8 & 0xff);
            result[3] = (byte) (a & 0xff);
        } else {
            result[3] = (byte) (a >> 24 & 0xff);
            result[2] = (byte) (a >> 16 & 0xff);
            result[1] = (byte) (a >> 8 & 0xff);
            result[0] = (byte) (a & 0xff);
        }
        return result;
    }

    public static byte[] int2bytes(int a) {
        return int2bytes(a, true);
    }

    /**
	 * �õ�һ�����������
	 * 
	 * @param obj
	 *            ����
	 * @return ���������
	 */
    public static String getClassName(Object obj) {
        String name = null;
        if (obj != null) {
            int index = 0;
            String temp = obj.getClass().toString();
            index = temp.lastIndexOf(".");
            if (index > 0 && index < temp.length()) name = temp.substring(index + 1);
        }
        return name;
    }

    public static int bytes2int(byte[] b) {
        return (int) bytes2long(b);
    }

    public static int bytes2int(byte[] b, boolean isHighFirst) {
        return (int) bytes2long(b, isHighFirst);
    }

    /**
	 * �ֽ�����ת�ɳ����Ρ�����λ��ǰ����ת����
	 * 
	 * @param b
	 * @return
	 */
    public static long bytes2long(byte[] b) {
        return bytes2long(b, true);
    }

    /**
	 * �ֽ�����ת�ɳ�����
	 * 
	 * @param b
	 * @param isHighFirst
	 *            �Ƿ��λ��ǰ
	 * @return
	 */
    public static long bytes2long(byte[] b, boolean isHighFirst) {
        long result = 0;
        if (b != null && b.length <= 8) {
            long value;
            if (isHighFirst) {
                for (int i = b.length - 1, j = 0; i >= 0; i--, j++) {
                    value = (long) (b[i] & 0xFF);
                    result += value << (j << 3);
                }
            } else {
                for (int i = 0, j = 0; i < b.length - 1; i++, j++) {
                    value = (long) (b[i] & 0xFF);
                    result += value << (j << 3);
                }
            }
        }
        return result;
    }

    public static String byte2bin(byte b) {
        String result = "";
        for (int i = 0; i < 8; i++) if (((b >>> (7 - i)) & 1) == 0) result += "0"; else result += "1";
        return result;
    }

    public static String int2bin(int value) {
        String result = "";
        for (int i = 0; i < 32; i++) if (((value >>> (31 - i)) & 1) == 0) {
            if (result.length() != 0) result += "0";
        } else result += "1";
        if (result.length() == 0) result = "0";
        return result;
    }

    public static String long2bin(long value) {
        String result = "";
        for (int i = 0; i < 64; i++) if (((value >>> (63 - i)) & 1) == 0) result += "0"; else result += "1";
        return result;
    }

    public static byte[] long2bytes(long value) {
        return long2bytes(value, true);
    }

    public static byte[] long2bytes(long value, boolean isHighFirst) {
        byte[] b = new byte[8];
        if (isHighFirst) {
            for (int i = 0; i < 8; i++) {
                b[i] = (byte) (value >> (8 * (7 - i)) & 0xFF);
            }
        } else {
            for (int i = 0, j = 7; i < 8; i++, j--) b[j] = (byte) (value >> (8 * (7 - i)) & 0xFF);
        }
        return b;
    }

    /**
	 * ��ʽ��IP��ַ,��219.11.33.44ת����������ʽ:219011033044
	 * 
	 * @param ip
	 * @return
	 */
    public static String formatIP(String ip) {
        String result = null;
        if (ip != null) {
            String[] p = new String[4];
            int index = ip.indexOf(".");
            if (index > 0 && index < ip.length() - 1) p[0] = ip.substring(0, index); else return null;
            ip = ip.substring(index + 1);
            index = ip.indexOf(".");
            if (index > 0 && index < ip.length() - 1) p[1] = ip.substring(0, index); else return null;
            ip = ip.substring(index + 1);
            index = ip.indexOf(".");
            if (index > 0 && index < ip.length() - 1) p[2] = ip.substring(0, index); else return null;
            p[3] = ip.substring(index + 1);
            if (p != null && p.length == 4) {
                result = GFString.getFixedLenStr(p[0], 3, '0');
                result += GFString.getFixedLenStr(p[1], 3, '0');
                result += GFString.getFixedLenStr(p[2], 3, '0');
                result += GFString.getFixedLenStr(p[3], 3, '0');
            }
        }
        return result;
    }

    public static boolean isActiveThread(ThreadGroup group, String threadName) {
        if (group != null && threadName != null) {
            Thread[] thd = new Thread[group.activeCount()];
            group.enumerate(thd);
            String name = null;
            for (int i = 0; i < thd.length && thd[i] != null; i++) {
                name = thd[i].getName();
                if (name != null && name.equals(threadName)) return true;
            }
        }
        return false;
    }

    /**
	 * �õ�ϵͳ��Ϣ��
	 * <p>
	 * ���磺����ϵͳ��JAVA������
	 * 
	 * @return
	 */
    public static String getSystemInfo() {
        String result = "os.name:" + System.getProperty("os.name") + "\n" + "os.arch:" + System.getProperty("os.arch") + "\n\n" + "java.vendor:" + System.getProperty("java.vendor") + "\n" + "java.home:" + System.getProperty("java.home") + "\n" + "java.version:" + System.getProperty("java.version") + "\n" + "java.vm.version:" + System.getProperty("java.vm.version") + "\n\n" + "user.name:" + System.getProperty("user.name") + "\n" + "user.dir:" + System.getProperty("user.dir");
        return result;
    }

    /**
	 * ��ȡ��ݿ�����ӡ�
	 * 
	 * @param driver
	 *            ��ݿ������
	 * @param url
	 *            ��ݿ�URL��ַ
	 * @param userName
	 *            ��ݿ��½�û���
	 * @param pwd
	 *            ��½����
	 * @param conn
	 *            ��ݿ�����
	 * @return ��ݿ�����
	 */
    public static Connection getConn(String driver, String url, String userName, String pwd) {
        Connection conn = null;
        if (driver != null && url != null && userName != null && pwd != null) {
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, userName, pwd);
                if (conn != null) {
                    String str = "������Զ����ݿ������!";
                    System.out.println(str);
                }
            } catch (ClassNotFoundException e) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    /**
	 * ȡ��Class�ļ����ڵ�·����
	 * 
	 * @param className
	 *            ����
	 * @return Class�ļ����ڵ�·����������Class�ļ�����İ�·�������磺com.gftech.web.Test,���ص�·����ʽ����/E:/gftech/project/web/bin/
	 * 
	 */
    public static String getClassPath(String className) {
        try {
            return Class.forName(className).getClassLoader().getResource("").getPath();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * ȡ��Class�ļ����ڵ�·����
	 * 
	 * @param objName
	 *            �������
	 * @return Class�ļ����ڵ�·����������Class�ļ�����İ�·�������磺com.gftech.web.Test,���ص�·����ʽ����/E:/gftech/project/web/bin/
	 * 
	 */
    public static String getClassPath(Object objName) {
        return objName.getClass().getClassLoader().getResource("").getPath();
    }

    /**
	 * ����JspӦ�ó�����WEB-INF��·����
	 * 
	 * @param classPath
	 *            WEB-INF/classes��·������ʽΪ/E:/web/myjsp/WEB-INF/classes/
	 * @return WEB-INF��·������ʽΪE:\web\myjsp\WEB-INF\
	 */
    public static String getWebinfPath(String classPath) {
        String path = null;
        if (classPath != null) {
            String[] strs = classPath.split("/");
            path = "";
            for (int i = 1; i < strs.length - 1; i++) {
                if (strs[i] != null) path += strs[i] + System.getProperty("file.separator");
            }
        }
        return path;
    }

    /**
	 * ����һ��������seed�������
	 * 
	 * @param seed
	 *            ��������������
	 * @return �����
	 */
    public static int random(int seed) {
        long result = 0;
        if (seed != 0) {
            double d = Math.random();
            String temp = d + "";
            int len = temp.length() - 2;
            d = d * Math.pow(10, len);
            result = (long) d % seed;
        }
        return (int) result;
    }

    /**
	 * ���һ����min��max֮��������
	 * 
	 * @param min
	 *            ��Сֵ
	 * @param max
	 *            ���ֵ
	 * @return
	 */
    public static int random(int min, int max) {
        int rd = random(max);
        if (rd >= min) return rd; else return random(min, max);
    }

    /**
	 * �õ�������0��һ������b�г��ֵ�λ��
	 * 
	 * @param b
	 * @return
	 */
    public static int getZeroIndex(byte[] b) {
        if (b != null) {
            for (int i = 0; i < b.length; i++) {
                if (b[i] == 0) return i;
            }
        }
        return -1;
    }

    /**
	 * �������Ƿ���0ֵ
	 * 
	 * @param b
	 * @return
	 */
    public static boolean isHasZero(byte[] b) {
        if (b == null) return true;
        for (byte b1 : b) if (b1 == 0) return true;
        return false;
    }

    /**
	 * ��һ������ת�ɶ�Ӧ�ĺ���,����:1--һ,5--��
	 * 
	 * @param num
	 * @return
	 */
    public static String num2cnum(int num) {
        if (num > -1 && num < 10) {
            return GFFinal.CHINA_NUMBER[num];
        }
        return null;
    }

    /**
	 * ��һ������ת�ɶ�Ӧ�Ļ��ר������,����:1--Ҽ,5--��
	 * 
	 * @param num
	 * @return
	 */
    public static String num2anum(int num) {
        if (num > -1 && num < 10) {
            return GFFinal.ACCOUNT_NUMBER[num];
        }
        return null;
    }

    public static int getUnsigned(byte b) {
        if (b > 0) return (int) b; else return (b & 0x7F + 128);
    }

    public static void appendInterpunction(StringBuffer sb, String interpunction) {
        if (sb != null) {
            int size = sb.toString().length();
            if (size > 0) {
                String last = sb.substring(size - 1, size);
                if (GFString.isInterpunction(last)) return;
            }
            sb.append(interpunction);
        }
    }

    /**
	 * ͨ�ÿ��������㷨(��С����
	 * 
	 * @param list
	 *            �ؼ�ʱ�
	 * @param start
	 *            ��ʼ�±�
	 * @param end
	 *            �����±�
	 */
    public static void quickSort(ArrayList<Comparable> list, int start, int end) {
        if (list != null && list.size() > 1) {
            Stack<Integer> s = new Stack<Integer>();
            s.push(start);
            s.push(end);
            while (s.size() > 0) {
                int j = s.pop();
                int i = s.pop();
                while (i < j) {
                    int p = quickSort0(list, i, j);
                    if (p - i < j - p) {
                        s.push(p + 1);
                        s.push(j);
                        j = p - 1;
                    } else {
                        s.push(i);
                        s.push(p - 1);
                        i = p + 1;
                    }
                }
            }
        }
    }

    private static int quickSort0(ArrayList<Comparable> list, int start, int end) {
        if (list != null) {
            int i = start;
            int j = end;
            Comparable pivot = list.get(start);
            while (i < j) {
                while (i < j && pivot.compareTo(list.get(j)) < 0) j--;
                if (i < j) list.set(i++, list.get(j));
                while (i < j && pivot.compareTo(list.get(i)) >= 0) i++;
                if (i < j) list.set(j--, list.get(i));
            }
            list.set(i, pivot);
            return i;
        }
        return -1;
    }
}
