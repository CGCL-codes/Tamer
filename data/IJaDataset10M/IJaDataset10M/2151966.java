package net.sourceforge.freejava.loader;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.freejava.jdk6compat.jdk7emul.Jdk7Reflect;
import net.sourceforge.freejava.jdk6compat.jdk7emul.ReflectiveOperationException;
import net.sourceforge.freejava.jvm.stack.Caller;
import net.sourceforge.freejava.sio.BCharOut;
import net.sourceforge.freejava.sio.IPrintOut;
import net.sourceforge.freejava.util.exception.UnexpectedException;

public class UCL {

    static Method URLClassLoader_addURL;

    static {
        Class<?> clazz = URLClassLoader.class;
        try {
            URLClassLoader_addURL = clazz.getDeclaredMethod("addURL", URL.class);
            URLClassLoader_addURL.setAccessible(true);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    /**
     * @return {@link URLClassLoader} which contains the specified url, or <code>null</code> if not
     *         found.
     */
    public static URLClassLoader exists(ClassLoader cl, URL url, boolean findParents) {
        if (url == null) throw new NullPointerException();
        while (cl != null) {
            if (cl instanceof URLClassLoader) {
                URLClassLoader ucl = (URLClassLoader) cl;
                for (URL u : ucl.getURLs()) if (url.equals(u)) return ucl;
            }
            if (findParents) cl = cl.getParent(); else break;
        }
        return null;
    }

    /**
     * try to not add duplicated urls.
     * 
     * @return <code>false</code> if specified url is already existed in ucl or its parents.
     */
    public static int addURL(URLClassLoader ucl, URL... urls) {
        if (URLClassLoader_addURL == null) throw new Error("can\'t access URLClassLoader.addURL()");
        int added = 0;
        try {
            for (URL url : urls) {
                URLClassLoader exists = exists(ucl, url, true);
                if (exists != null) continue;
                Jdk7Reflect.invoke(URLClassLoader_addURL, ucl, url);
                added++;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return added;
    }

    public static URLClassLoader addOrCreate(ClassLoader loader, URL... urls) {
        if (loader instanceof URLClassLoader) {
            URLClassLoader ucl = (URLClassLoader) loader;
            for (URL url : urls) addURL(ucl, url);
            return ucl;
        }
        return new URLClassLoader(urls, loader);
    }

    private static class Collect extends Iter {

        private List<URL> list;

        public Collect() {
            this.list = new ArrayList<URL>();
        }

        public URL[] get() {
            return list.toArray(new URL[0]);
        }

        @Override
        public boolean loader(ClassLoader loader) {
            return filter(loader);
        }

        @Override
        public boolean entry(URL classpath) {
            list.add(classpath);
            return true;
        }
    }

    public static void findURLs(ClassLoader loader, Iter it) {
        if (!it.loader(loader)) return;
        if (!(loader instanceof URLClassLoader)) return;
        URLClassLoader uloader = (URLClassLoader) loader;
        URL[] urls = uloader.getURLs();
        for (URL url : urls) if (!it.entry(url)) break;
        ClassLoader parent = loader.getParent();
        if (parent != null) findURLs(parent, it);
    }

    public static URL[] findURLs(ClassLoader loader) {
        Collect collect = new Collect();
        findURLs(loader, collect);
        return collect.get();
    }

    public static void findURLs(Iter it) {
        findURLs(Caller.getCallerClassLoader(0), it);
    }

    public static URL[] findURLs() {
        return findURLs(Caller.getCallerClassLoader(0));
    }

    public abstract static class Iter {

        public abstract boolean loader(ClassLoader loader);

        public abstract boolean entry(URL classpath);

        public static final Set<String> stops;

        static {
            stops = new HashSet<String>();
            stops.add("sun.misc.Launcher$ExtClassLoader");
        }

        protected boolean filter(ClassLoader loader) {
            return !stops.contains(loader);
        }
    }

    private static class Dumper extends Iter {

        private final IPrintOut out;

        private boolean cont;

        public Dumper(IPrintOut out) {
            this.out = out;
        }

        @Override
        public boolean entry(URL classpath) {
            out.println(classpath);
            return true;
        }

        @Override
        public boolean loader(ClassLoader loader) {
            if (cont) out.println();
            cont = true;
            out.println("; loader " + loader);
            if (filter(loader)) {
                if (!(loader instanceof URLClassLoader)) out.println("; (no url info)");
                return true;
            } else {
                out.println("; (stopped)");
                return false;
            }
        }
    }

    public static void dump(ClassLoader loader, IPrintOut out) {
        findURLs(loader, new Dumper(out));
    }

    public static String dump(ClassLoader loader) {
        BCharOut buf = new BCharOut();
        dump(loader, buf);
        return buf.toString();
    }
}
