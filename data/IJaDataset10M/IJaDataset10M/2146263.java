package cc;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class apsta {

    public static String s_forum = "C:\\_2_Jigsaw\\wwwroot\\forum\\2\\";

    public static String s3 = "";

    public static int k = 1, j = 1, j_max = 5;

    public static String set_google_cookie(String s) {
        String sid = s.substring(s.indexOf("SID="));
        sid = sid.substring(0, sid.indexOf(";") + 1);
        String lsid = s.substring(s.lastIndexOf("LSID="));
        lsid = lsid.substring(0, lsid.indexOf(";") + 1);
        return sid + " " + lsid;
    }

    public static String rep(String line, String old_s, String new_s) {
        int index = 0;
        while ((index = line.indexOf(old_s, index)) >= 0) {
            line = line.substring(0, index) + new_s + line.substring(index + old_s.length());
            index += new_s.length();
        }
        return line;
    }

    public static String rt(String s) {
        int i = s.indexOf("<");
        int j = s.indexOf(">");
        while (i >= 0 && j > i) {
            s = s.substring(0, i) + s.substring(j + 1);
            i = s.indexOf("<");
            j = s.indexOf(">");
        }
        return s;
    }

    public static byte[] hs2b(String hex) {
        java.util.Vector<Object> res = new java.util.Vector<Object>();
        String part;
        int pos = 0;
        final int len = 2;
        while (pos < hex.length()) {
            part = hex.substring(pos, pos + len);
            pos += len;
            int byteVal = Integer.parseInt(part, 16);
            res.add(new Byte((byte) byteVal));
        }
        if (res.size() > 0) {
            byte[] b = new byte[res.size()];
            for (int i = 0; i < res.size(); i++) {
                Byte a = (Byte) res.elementAt(i);
                b[i] = a.byteValue();
            }
            return b;
        } else {
            return null;
        }
    }

    public static String b2hs(byte[] buf) {
        StringBuffer sbuff = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            int b = buf[i];
            if (b < 0) b = b & 0xFF;
            if (b < 16) sbuff.append("0");
            sbuff.append(Integer.toHexString(b).toUpperCase());
        }
        return sbuff.toString();
    }

    public static String rfu2(String url, String enc) throws Exception {
        String s = "", str = "";
        URL u = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream(), enc));
        while ((str = in.readLine()) != null) {
            s = s + str + "\r\n";
        }
        in.close();
        return s;
    }

    public static String rff(String s) throws Exception {
        return rfu2("file:///" + System.getProperties().getProperty("user.dir") + "/" + s, "UTF8");
    }

    public static String rffw(String s) throws Exception {
        return rfu("file:///" + System.getProperties().getProperty("user.dir") + "/" + s);
    }

    public static String rfu(String url) {
        StringBuffer s = new StringBuffer();
        try {
            URL u = new URL(url);
            InputStream in = u.openConnection().getInputStream();
            for (int ch = in.read(); ch > 0; ch = in.read()) {
                s.append((char) ch);
            }
            in.close();
        } catch (IOException e) {
            return e.toString();
        }
        return s.toString();
    }

    public static void tokenize(String s) {
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }

    public static String rfu_win(String url) throws Exception {
        String s = "", str = "";
        try {
            URL u = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream(), "CP1251"));
            while ((str = in.readLine()) != null) {
                s = s + str;
            }
            in.close();
        } catch (IOException e) {
        }
        return s;
    }

    public static String rfu_utf(String url) throws Exception {
        String s = "", str = "";
        try {
            URL u = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
            while ((str = in.readLine()) != null) {
                s = s + str;
            }
            in.close();
        } catch (IOException e) {
        }
        return s;
    }

    public static String rsp(String s) {
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll(" ");
    }

    public static String zir(String zips) throws Exception {
        String s = "", ss = "", sz = "";
        int n = 0;
        File f = new File(zips);
        for (int i = 0; i < f.list().length; i++) {
            s = f.list()[i];
            s = s.substring(s.indexOf("-") + 1, s.indexOf("."));
            n = n + Integer.parseInt(s);
        }
        int j = (int) (n * Math.random());
        n = 0;
        for (int i = 0; i < f.list().length; i++) {
            s = f.list()[i];
            sz = s;
            s = s.substring(s.indexOf("-") + 1, s.indexOf("."));
            n = n + Integer.parseInt(s);
            if (j < n) {
                break;
            }
        }
        s = "";
        int i = Integer.parseInt(sz.substring(sz.indexOf("-") + 1, sz.indexOf(".")));
        int k = (int) (i * Math.random()) + 1;
        ZipFile zipFile = new ZipFile(new File(zips + "/" + sz));
        ZipEntry entry = zipFile.getEntry(k + ".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), "UTF-8"));
        while ((ss = br.readLine()) != null) {
            s = s + ss;
        }
        br.close();
        zipFile.close();
        return s;
    }
}
