package de.grogra.rgg.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import de.grogra.graph.GraphState;
import de.grogra.graph.impl.Extent;
import de.grogra.graph.impl.GraphManager;
import de.grogra.graph.impl.Node;
import de.grogra.graph.impl.Node.NType;
import de.grogra.imp3d.objects.Box;
import de.grogra.numeric.Dummy;
import de.grogra.pf.io.FilterBase;
import de.grogra.pf.io.FilterItem;
import de.grogra.pf.io.FilterSource;
import de.grogra.pf.io.IO;
import de.grogra.pf.io.IOFlavor;
import de.grogra.pf.io.ObjectSource;
import de.grogra.pf.io.ResourceLoader;
import de.grogra.pf.registry.Registry;
import de.grogra.pf.registry.TypeItem;
import de.grogra.pf.ui.Workbench;
import de.grogra.reflect.ClassAdapter;
import de.grogra.reflect.Reflection;
import de.grogra.reflect.Type;
import de.grogra.rgg.Library;
import de.grogra.util.IOWrapException;
import de.grogra.util.MimeType;
import de.grogra.vfs.MemoryFileSystem;
import de.grogra.xl.compiler.BytecodeWriter;
import de.grogra.xl.compiler.CClass;
import de.grogra.xl.compiler.CompilationUnit;
import de.grogra.xl.compiler.Compiler;
import de.grogra.xl.compiler.CompilerBase;
import de.grogra.xl.compiler.CompilerOptions;
import de.grogra.xl.compiler.Main;
import de.grogra.xl.compiler.scope.ClassPath;
import de.grogra.xl.compiler.scope.CompilationUnitScope;
import de.grogra.xl.property.CompiletimeModel;
import de.grogra.xl.property.RuntimeModel;
import de.grogra.xl.property.RuntimeModelFactory;
import de.grogra.xl.util.ObjectList;

public class CompilationFilter extends FilterBase implements ObjectSource, ResourceLoader {

    public static final MimeType COMPILATION_UNIT_TYPE = MimeType.valueOf(CompilationUnit.class);

    public static final IOFlavor CUNIT_FLAVOR = new IOFlavor(COMPILATION_UNIT_TYPE);

    public static final String DEACTIVATION_CATEGORY = "java.lang.Class";

    public static boolean WRITE_CLASSES = true;

    public static boolean DUMP_TYPES = false;

    private CompilationUnitScope compilationUnit;

    private ObjectList sources = new ObjectList();

    public CompilationFilter(FilterItem item, FilterSource source) {
        super(item, source);
        setFlavor(IOFlavor.RESOURCE_LOADER);
    }

    public Object getObject() {
        return this;
    }

    public CompilationUnitScope getCompilationUnit() {
        return compilationUnit;
    }

    private static void add(ObjectList<Type> list, Type type) {
        list.add(type);
        for (int i = type.getDeclaredTypeCount() - 1; i >= 0; i--) {
            add(list, type.getDeclaredType(i));
        }
    }

    public Type[] compile(CClass shell, ClassLoader parentLoader) throws IOException {
        ObjectList list = new ObjectList();
        XLFilter.CLASSPATH.set(XLFilter.createDefaultClassPath(this));
        StringBuffer names;
        try {
            list.add(((ObjectSource) source).getObject());
            names = new StringBuffer(source.getSystemId());
            for (int i = 0; i < sources.size(); i++) {
                list.add(((ObjectSource) sources.get(i)).getObject());
                names.append(", ").append(((ObjectSource) sources.get(i)).getSystemId());
            }
        } finally {
            XLFilter.CLASSPATH.set(null);
        }
        CompilationUnit[] src = (CompilationUnit[]) list.toArray(new CompilationUnit[list.size()]);
        Compiler c = new Compiler();
        CompilationUnitScope[] cus = c.compile(src, shell, null, null, null, true);
        compilationUnit = cus[0];
        int version = org.objectweb.asm.Opcodes.V1_5;
        CompilerOptions opts = src[0].options;
        for (int i = 0; i < src.length; i++) {
            if (!src[i].options.supportsVersion(version)) {
                opts = src[i].options;
                version = opts.javaVersion;
            }
            c.problems.addAll(src[i].problems);
        }
        if (c.problems.containsErrors()) {
            throw new IOWrapException(c.problems);
        } else if (c.problems.containsWarnings()) {
            Registry.current().getLogger().log(Workbench.GUI_INFO, Library.I18N.msg("xl.compilation-warnings", names), c.problems);
        } else if (shell == null) {
            Registry.current().getLogger().log(Workbench.SOFT_GUI_INFO, Library.I18N.msg("xl.compilation-successful", names));
        }
        ObjectList<Type> types = new ObjectList<Type>();
        for (int j = 0; j < cus.length; j++) {
            Type[] a = cus[j].getDeclaredTypes();
            for (int k = 0; k < a.length; k++) {
                add(types, a[k]);
            }
        }
        Type[] a = types.toArray(new Type[types.size()]);
        BytecodeWriter w = new BytecodeWriter(opts);
        MemoryFileSystem fs = w.createFileSystemFor(cus);
        if (parentLoader == null) {
            parentLoader = ClassPath.get(compilationUnit).getClassLoader();
        }
        ClassLoader loader = new ClassAdapter.URLClassLoaderWithPool(new URL[] { fs.toURL(fs.getRoot()) }, parentLoader);
        if (WRITE_CLASSES) {
            File file = new File(System.getProperty("user.home"), "classes.zip");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            fs.writeJar(out);
            out.flush();
            out.close();
        }
        for (int i = 0; i < a.length; i++) {
            if (DUMP_TYPES) {
                Main.dumpType(a[i], true);
            }
            try {
                a[i] = ClassAdapter.wrap(Class.forName(a[i].getBinaryName(), false, loader));
            } catch (ClassNotFoundException e) {
                throw new IOWrapException(e);
            }
        }
        return a;
    }

    public void loadResource(Registry r) throws IOException {
        Type[] a = compile(null, null);
        for (int i = 0; i < a.length; i++) {
            r.getDirectory("/classes", null).add(new TypeItem(a[i]));
        }
    }

    public boolean addResource(FilterSource source) {
        FilterSource cu = IO.createPipeline(source, CUNIT_FLAVOR);
        if (cu == null) {
            return false;
        }
        sources.add(cu);
        return true;
    }

    public String getJoinedDeactivationCategory() {
        return DEACTIVATION_CATEGORY;
    }
}
