package net.sourceforge.jute.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import net.sourceforge.jute.Util;

/**
 * ClassBuilder builds a new in-memory class based on the source that is
 * written to it.
 * 
 * @author david.mcnerney
 */
public class ClassBuilder {

    private String className;

    private StringWriter out;

    public ClassBuilder(String className) {
        this.className = className;
        this.out = new StringWriter();
    }

    public PrintWriter getWriter() {
        return new PrintWriter(out);
    }

    public static Class getPrimitiveWrapper(Class cls) {
        if (!cls.isPrimitive()) return cls;
        if (cls == int.class) return Integer.class;
        if (cls == long.class) return Long.class;
        if (cls == boolean.class) return Boolean.class;
        if (cls == char.class) return Character.class;
        if (cls == float.class) return Float.class;
        if (cls == double.class) return Double.class;
        if (cls == byte.class) return Byte.class;
        if (cls == short.class) return Short.class;
        throw new Error("Uncovertable primitive class: " + cls);
    }

    public Class buildClass() {
        System.out.println("====\n" + out.toString() + "\n====");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null), Util.toURI(className.replace('.', '/')));
        JavaFileObject src = new SimpleJavaFileObject(Util.toURI(className.replace('.', '/') + ".java"), Kind.SOURCE) {

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return out.toString();
            }
        };
        CompilationTask task = compiler.getTask(null, fileManager, null, null, null, Arrays.asList(src));
        if (!task.call()) {
            throw new RuntimeException("Compilation failed");
        }
        ClassLoader ldr = new ClassLoader() {

            @Override
            protected Class<?> findClass(String string) {
                byte[] raw = fileManager.getRawBytes();
                return defineClass(string, raw, 0, raw.length);
            }
        };
        try {
            return Class.forName(className, false, ldr);
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        }
    }

    String getClassName() {
        return className;
    }

    private static class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

        private final JavaFileObject jfo;

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public MemoryFileManager(StandardJavaFileManager m, URI classNameURI) {
            super(m);
            this.jfo = new SimpleJavaFileObject(classNameURI, Kind.CLASS) {

                @Override
                public OutputStream openOutputStream() {
                    return baos;
                }
            };
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location lctn, String string, Kind kind, FileObject fo) {
            return jfo;
        }

        private byte[] getRawBytes() {
            return baos.toByteArray();
        }
    }
}
