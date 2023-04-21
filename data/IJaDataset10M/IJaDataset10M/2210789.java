package joeq.ClassLib.sun15_win32;

import java.util.Iterator;
import joeq.Class.PrimordialClassLoader;
import joeq.Class.jq_Class;
import joeq.Class.jq_InstanceField;
import joeq.Class.jq_StaticField;
import joeq.ClassLib.ClassLibInterface;
import joeq.Main.jq;
import joeq.Runtime.ObjectTraverser;
import jwutil.collections.AppendIterator;

public class Interface extends joeq.ClassLib.sun142_win32.Interface {

    /** Creates new Interface */
    public Interface() {
    }

    public Iterator getImplementationClassDescs(joeq.UTF.Utf8 desc) {
        if (ClassLibInterface.USE_JOEQ_CLASSLIB && (desc.toString().startsWith("Ljava/") || desc.toString().startsWith("Lsun/misc/"))) {
            joeq.UTF.Utf8 u = joeq.UTF.Utf8.get("Ljoeq/ClassLib/sun15_win32/" + desc.toString().substring(1));
            return new AppendIterator(super.getImplementationClassDescs(desc), java.util.Collections.singleton(u).iterator());
        }
        return super.getImplementationClassDescs(desc);
    }

    public ObjectTraverser getObjectTraverser() {
        return sun15_win32ObjectTraverser.INSTANCE;
    }

    public static class sun15_win32ObjectTraverser extends sun142_win32ObjectTraverser {

        public static sun15_win32ObjectTraverser INSTANCE = new sun15_win32ObjectTraverser();

        protected sun15_win32ObjectTraverser() {
        }

        public void initialize() {
            super.initialize();
            jq_Class k;
            k = (jq_Class) PrimordialClassLoader.getJavaLangClass();
            nullInstanceFields.add(k.getOrCreateInstanceField("name", "Ljava/lang/String;"));
            k = (jq_Class) PrimordialClassLoader.getJavaLangReflectField();
            nullInstanceFields.add(k.getOrCreateInstanceField("fieldAccessor", "Lsun/reflect/FieldAccessor;"));
            nullInstanceFields.add(k.getOrCreateInstanceField("overrideFieldAccessor", "Lsun/reflect/FieldAccessor;"));
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Lsun/reflect/UnsafeStaticFieldAccessorImpl;");
            nullInstanceFields.add(k.getOrCreateInstanceField("base", "Ljava/lang/Object;"));
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Lsun/security/jca/Providers;");
            nullStaticFields.add(k.getOrCreateStaticField("threadLists", "Ljava/lang/ThreadLocal;"));
            nullStaticFields.add(k.getOrCreateStaticField("providerList", "Lsun/security/jca/ProviderList;"));
            k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Lsun/net/www/protocol/jar/JarFileFactory;");
            nullStaticFields.add(k.getOrCreateStaticField("fileCache", "Ljava/util/HashMap;"));
            nullStaticFields.add(k.getOrCreateStaticField("urlCache", "Ljava/util/HashMap;"));
            if (jq.on_vm_startup != null) {
                Object[] args = {};
                k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/io/FileDescriptor;");
                joeq.Class.jq_Method init_fd = k.getOrCreateStaticMethod("init", "()V");
                joeq.Bootstrap.MethodInvocation mi = new joeq.Bootstrap.MethodInvocation(init_fd, args);
                jq.on_vm_startup.add(mi);
                System.out.println("Added call to reinitialize in/out/err file descriptors on joeq startup: " + mi);
            }
        }

        public static final jq_StaticField valueOffsetField;

        static {
            jq_Class k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/util/concurrent/atomic/AtomicLong;");
            valueOffsetField = k.getOrCreateStaticField("valueOffset", "J");
        }

        public java.lang.Object mapStaticField(jq_StaticField f) {
            if (f == valueOffsetField) {
                jq_Class k = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljava/util/concurrent/atomic/AtomicLong;");
                k.prepare();
                int offset = ((jq_InstanceField) k.getDeclaredMember("value", "J")).getOffset();
                return new Long(offset);
            }
            return super.mapStaticField(f);
        }
    }
}
