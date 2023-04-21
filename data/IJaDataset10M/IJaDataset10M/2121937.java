package vqwiki.db;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;

public class OracleClobHelper {

    /**
     *
     */
    public static String getClobValue(Clob clob) throws SQLException, IOException {
        if (clob == null) return "";
        StringBuffer sb = new StringBuffer();
        char buffer[] = new char[4096];
        Reader r = clob.getCharacterStream();
        while (true) {
            int n = r.read(buffer);
            if (n == -1) break;
            sb.append(buffer, 0, n);
        }
        r.close();
        return new String(sb);
    }

    /**
     *
     */
    public static void setClobValue(Clob clob, String value) throws SQLException, IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class oracleClobClass = Class.forName("oracle.sql.CLOB");
        Method m = oracleClobClass.getMethod("getCharacterOutputStream", new Class[] {});
        Writer w = (Writer) m.invoke(clob, new Object[] {});
        w.write(value);
        w.close();
    }
}
