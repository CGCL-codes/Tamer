package joeq.ClassLib.sun142_win32;

import java.util.Collections;
import java.util.Iterator;
import joeq.Class.PrimordialClassLoader;
import joeq.Class.jq_Class;
import joeq.ClassLib.ClassLibInterface;
import joeq.Runtime.ObjectTraverser;
import jwutil.collections.AppendIterator;

public class Interface extends joeq.ClassLib.sun14_win32.Interface {

    /** Creates new Interface */
    public Interface() {
    }

    public Iterator getImplementationClassDescs(joeq.UTF.Utf8 desc) {
        if (ClassLibInterface.USE_JOEQ_CLASSLIB && desc.toString().startsWith("Ljava/")) {
            joeq.UTF.Utf8 u = joeq.UTF.Utf8.get("Ljoeq/ClassLib/sun142_win32/" + desc.toString().substring(1));
            return new AppendIterator(super.getImplementationClassDescs(desc), Collections.singleton(u).iterator());
        }
        return super.getImplementationClassDescs(desc);
    }

    public ObjectTraverser getObjectTraverser() {
        return sun142_win32ObjectTraverser.INSTANCE;
    }

    public static class sun142_win32ObjectTraverser extends sun14_win32ObjectTraverser {

        public static sun142_win32ObjectTraverser INSTANCE = new sun142_win32ObjectTraverser();

        protected sun142_win32ObjectTraverser() {
        }

        public void initialize() {
            super.initialize();
            jq_Class k;
            try {
                k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljoeq/ClassLib/Common/java/util/zip/DeflaterHuffman;");
                k.load();
            } catch (NoClassDefFoundError _) {
                System.err.println("Error preloading DeflaterHuffman class");
            }
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/io/ObjectInputStream$GetFieldImpl;");
            k.load();
            k = (jq_Class) PrimordialClassLoader.getJavaLangClass();
            nullInstanceFields.add(k.getOrCreateInstanceField("name", "Ljava/lang/String;"));
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/io/Win32FileSystem;");
            nullInstanceFields.add(k.getOrCreateInstanceField("cache", "Ljava/io/ExpiringCache;"));
            nullInstanceFields.add(k.getOrCreateInstanceField("prefixCache", "Ljava/io/ExpiringCache;"));
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/io/ExpiringCache;");
            k.load();
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/io/ExpiringCache$Entry;");
        }
    }
}
