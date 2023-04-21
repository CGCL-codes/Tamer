package util.basedatatype.collection;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Collection ����
 * @author luo
 *
 */
public class CollectionBase {

    /**
 * ������T[] aת��Ϊָ���ļ�������Collection<T> c;
 * @param <T>
 * @param a
 * @param c
 */
    public static <T> void Array2Collection(T[] a, Collection<T> c) {
        for (T o : a) c.add(o);
    }

    public static <T> Collection<T> fromArray(T[] a, Class cls) {
        Collection<T> c;
        try {
            c = (Collection<T>) cls.newInstance();
            for (T o : a) c.add(o);
            return c;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Collection<T> from2(Collection<T> a, Class cls) {
        Collection<T> c;
        try {
            c = (Collection<T>) cls.newInstance();
            for (T o : a) c.add(o);
            return c;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toStringByDiv(Collection<String> clec, String CDIV) {
        if (clec == null) return null;
        StringBuilder s = new StringBuilder();
        for (String si : clec) {
            s.append(si + CDIV);
        }
        String sh = s.toString();
        if (sh == null) return null;
        return sh.substring(0, sh.length() - CDIV.length());
    }

    public static Object[] toObjArray(Collection<String> clec) {
        if (clec == null) return null;
        return clec.toArray();
    }

    public static String[] toStringArray(Collection clec) {
        if (clec == null) return null;
        String s[] = new String[clec.size()];
        clec.toArray(s);
        return s;
    }

    public static String spString(Collection clec, String CDIV) {
        if (clec == null) return null;
        StringBuilder s = new StringBuilder();
        for (Object obj : clec) {
            String sh;
            if (obj != null) sh = obj.toString(); else sh = "";
            s.append(sh + CDIV);
        }
        return s.substring(0, s.length() - CDIV.length()).toString();
    }

    public static void println(Collection clec) {
        if (clec == null) return;
        for (Object obj : clec) {
            System.out.println(obj.toString());
        }
    }

    public static class Extend {
    }
}
