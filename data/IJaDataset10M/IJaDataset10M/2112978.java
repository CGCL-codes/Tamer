package ac.hiu.j314.elmve;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

public class W {

    public static String sepa = System.getProperty("file.separator");

    public static Class orderClass = new Order(null, null, null, null).getClass();

    public static Class requestClass = new Request(null, null, null, null).getClass();

    public static Message makeMessageFromString(LocalElm elm, String ss) throws ElmException {
        ss = ss.trim();
        if (ss.endsWith("&")) {
            return makeOrderFromString(elm, ss);
        } else {
            return makeRequestFromString(elm, ss);
        }
    }

    public static Request makeRequestFromString(LocalElm elm, String ss) throws ElmException {
        ss = ss.trim();
        if (ss.equals("")) return null;
        Reader rr = new StringReader(ss);
        ElmStreamTokenizer st = new ElmStreamTokenizer(rr);
        Vector v = new Vector();
        try {
            while (st.hasMoreTokens()) v.add(st.nextString());
        } catch (IOException e) {
            System.out.println("W.makeRequestFromString().--???");
            e.printStackTrace();
        }
        String s = (String) v.remove(0);
        Elm receiver;
        if (s.startsWith("##")) {
            receiver = elm.getElm(s.substring(2));
            if (receiver == null) {
                throw new ElmException("No such elms!(" + s.substring(2) + ")");
            }
            if (v.size() == 0) {
                throw new ElmException("No message name.");
            }
            s = (String) v.remove(0);
        } else if (s.startsWith("#")) {
            s = s.substring(1);
            receiver = elm.getParent();
        } else {
            receiver = elm;
        }
        Serializable args[] = new Serializable[v.size()];
        for (int i = 0; i < v.size(); i++) args[i] = (Serializable) W.stringToObject((String) v.get(i));
        return new Request(elm, receiver, s, args);
    }

    public static Order makeOrderFromString(LocalElm elm, String ss) throws ElmException {
        ss = ss.trim();
        if (ss.equals("")) return null;
        Reader rr = new StringReader(ss);
        ElmStreamTokenizer st = new ElmStreamTokenizer(rr);
        Vector v = new Vector();
        try {
            while (st.hasMoreTokens()) v.add(st.nextString());
        } catch (IOException e) {
            System.out.println("W.makeOrderFromString().--???");
            e.printStackTrace();
        }
        String s = (String) v.remove(0);
        Elm receiver;
        if (s.startsWith("##")) {
            receiver = elm.getElm(s.substring(2));
            if (receiver == null) throw new ElmException("No such elms!(" + s.substring(2) + ")");
            if (v.size() == 0) {
                throw new ElmException("No message name.");
            }
            s = (String) v.remove(0);
        } else if (s.startsWith("#")) {
            s = s.substring(1);
            receiver = elm.getParent();
        } else {
            receiver = elm;
        }
        Serializable args[] = new Serializable[v.size()];
        for (int i = 0; i < v.size(); i++) args[i] = (Serializable) W.stringToObject((String) v.get(i));
        return new Order(elm, receiver, s, args);
    }

    public static Integer p(int i) {
        return new Integer(i);
    }

    public static Double p(double d) {
        return new Double(d);
    }

    public static Float p(float f) {
        return new Float(f);
    }

    public static Boolean p(boolean b) {
        return new Boolean(b);
    }

    public static int p(Integer i) {
        return i.intValue();
    }

    public static double p(Double d) {
        return d.doubleValue();
    }

    public static float p(Float f) {
        return f.floatValue();
    }

    public static boolean p(Boolean b) {
        return b.booleanValue();
    }

    public static Serializable[] pp() {
        Serializable o[] = new Serializable[0];
        return o;
    }

    public static Serializable[] pp(Serializable o0) {
        Serializable o[] = new Serializable[1];
        o[0] = o0;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1) {
        Serializable o[] = new Serializable[2];
        o[0] = o0;
        o[1] = o1;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2) {
        Serializable o[] = new Serializable[3];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3) {
        Serializable o[] = new Serializable[4];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4) {
        Serializable o[] = new Serializable[5];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4, Serializable o5) {
        Serializable o[] = new Serializable[6];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        o[5] = o5;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4, Serializable o5, Serializable o6) {
        Serializable o[] = new Serializable[7];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        o[5] = o5;
        o[6] = o6;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4, Serializable o5, Serializable o6, Serializable o7) {
        Serializable o[] = new Serializable[8];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        o[5] = o5;
        o[6] = o6;
        o[7] = o7;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4, Serializable o5, Serializable o6, Serializable o7, Serializable o8) {
        Serializable o[] = new Serializable[9];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        o[5] = o5;
        o[6] = o6;
        o[7] = o7;
        o[8] = o8;
        return o;
    }

    public static Serializable[] pp(Serializable o0, Serializable o1, Serializable o2, Serializable o3, Serializable o4, Serializable o5, Serializable o6, Serializable o7, Serializable o8, Serializable o9) {
        Serializable o[] = new Serializable[10];
        o[0] = o0;
        o[1] = o1;
        o[2] = o2;
        o[3] = o3;
        o[4] = o4;
        o[5] = o5;
        o[6] = o6;
        o[7] = o7;
        o[8] = o8;
        o[9] = o9;
        return o;
    }

    public static String f(double d, int i, int j) {
        if (d == 0.0) return "+0.00000000000000".substring(0, i + 2) + "E+000000000000000".substring(0, j + 2);
        String s = (d < 0.0) ? "-" : "+";
        d = Math.abs(d);
        double k = Math.ceil(Math.log(d) / Math.log(10.0)) - 1;
        d = d / Math.pow(10.0, k);
        if (d == 10.0) {
            d /= 10.0;
            k += 1.0;
        }
        String ss = "" + d;
        ss = ss + "00000000000000";
        s = s + ss.substring(0, i + 1);
        s = s + ((k < 0.0) ? "E-" : "E+");
        k = Math.abs(k);
        ss = "" + (int) k;
        ss = "00000000000000" + ss;
        s = s + ss.substring(ss.length() - j, ss.length());
        return s;
    }

    public static String toSaveString(String s) {
        StringBuffer sb = new StringBuffer(s);
        int i = 0;
        while (sb.length() > i) {
            if (sb.charAt(i) == '\n') {
                sb = sb.replace(i, i + 1, "\\n");
                i++;
                i++;
            } else {
                i++;
            }
        }
        return "\"" + sb.toString() + "\"";
    }

    public static Object stringToObject(String ss) {
        Object o;
        try {
            o = new Integer(ss);
        } catch (Exception e) {
            try {
                if (ss.charAt(ss.length() - 1) == 'f' || ss.charAt(ss.length() - 1) == 'F') o = new Float(ss); else o = new Double(ss);
            } catch (Exception ee) {
                o = ss;
            }
        }
        return o;
    }

    public static ElmStreamTokenizer getEST(String file) throws IOException {
        ElmStreamTokenizer est = null;
        try {
            if (file.startsWith("x-res:")) {
                file = file.substring(6);
                URL url = ElmVE.classLoader.getResource(file);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                est = new ElmStreamTokenizer(isr);
            } else {
                URL url = new URL(file);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                est = new ElmStreamTokenizer(isr);
            }
        } catch (MalformedURLException me) {
            try {
                FileReader reader = new FileReader(file);
                est = new ElmStreamTokenizer(reader);
            } catch (FileNotFoundException fe) {
                throw new IOException();
            }
        }
        return est;
    }

    public static URL getResource(String resource) {
        if (resource.startsWith("http:") || resource.startsWith("file:")) {
            try {
                return new URL(resource);
            } catch (Exception e) {
                return null;
            }
        } else if (resource.startsWith("x-res:")) {
            resource = resource.substring(6);
            return ElmVE.classLoader.getResource(resource);
        } else {
            return null;
        }
    }

    public static String getIPAddress() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (java.net.UnknownHostException ue) {
            ue.printStackTrace();
            return null;
        }
    }

    public static String getLocalPath(String path) {
        path = path.trim();
        if (path.startsWith("//")) {
            path = path.substring(2);
            path = path.substring(path.indexOf("/") + 1);
            if (path.indexOf("/") == -1) path = "/"; else path = path.substring(path.indexOf("/"));
        }
        return path;
    }

    public static String getElmVEPath(String path) {
        path = path.trim();
        if (!path.startsWith("//")) return null;
        int i = path.indexOf("/", 2);
        i = path.indexOf("/", i + 1);
        if (i == -1) i = path.length();
        path = path.substring(0, i);
        return path;
    }
}
