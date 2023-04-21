package ac.hiu.j314.elmve;

import java.io.*;
import java.util.*;
import java.net.*;

public class ElmClassLoader extends ClassLoader {

    protected String elmClassPath[] = { "/elmClasses" };

    protected ElmClassLoader() {
        super();
    }

    protected ElmClassLoader(ClassLoader cl) {
        super(cl);
    }

    public Class findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassData(name);
        if (b == null) throw new ClassNotFoundException();
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        name = name.replace('.', '/');
        try {
            for (int i = 0; i < elmClassPath.length; i++) {
                File f = new File(elmClassPath[i] + "/" + name + ".class");
                System.out.println(f);
                if (!f.isFile()) break;
                long l = f.length();
                byte b[] = new byte[(int) l];
                FileInputStream fis = new FileInputStream(elmClassPath[i] + "/" + name + ".class");
                fis.read(b);
                return b;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected URL findResource(String name) {
        URL ret = getClass().getResource(name);
        if (ret != null) return ret;
        try {
            for (int i = 0; i < elmClassPath.length; i++) {
                File f = new File(elmClassPath[i] + name);
                if (!f.isFile()) break;
                ret = new URL("file:" + elmClassPath[i] + name);
                System.out.println(ret);
                if (ret != null) return ret;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void setElmClassPath(ArrayList cp) {
        String cps[] = new String[cp.size()];
        Iterator i = cp.iterator();
        int j = 0;
        while (i.hasNext()) {
            cps[j] = (String) i.next();
            j++;
        }
        elmClassPath = cps;
    }

    protected ArrayList getElmClassPath() {
        ArrayList ret = new ArrayList();
        for (int i = 0; i < elmClassPath.length; i++) ret.add(elmClassPath[i]);
        return ret;
    }

    protected void loadElmClassPath(String url) throws IOException {
        URL u = W.getResource(url);
        InputStream is = u.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        ElmStreamTokenizer est = new ElmStreamTokenizer(isr);
        String c = est.nextString();
        StringTokenizer st = new StringTokenizer(c);
        ArrayList cp = new ArrayList();
        while (st.hasMoreTokens()) {
            cp.add(st.nextToken());
        }
        setElmClassPath(cp);
    }

    protected void saveElmClassPath(String url) throws IOException {
        if (url.startsWith("file:")) url = url.substring(5);
        FileWriter fw = new FileWriter(url);
        PrintWriter f_out = new PrintWriter(fw);
        f_out.print("\"");
        for (int i = 0; i < elmClassPath.length; i++) {
            f_out.print(elmClassPath[i] + " ");
        }
        f_out.print("\"");
        f_out.close();
    }
}
