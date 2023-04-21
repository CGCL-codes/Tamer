package edu.rice.cs.astgen;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import edu.rice.cs.plt.lambda.Lambda;
import edu.rice.cs.plt.iter.IterUtil;

public abstract class CodeGenerator {

    protected final ASTModel ast;

    protected final Options options;

    /** All concrete code generators must have a public constructor accepting a single ASTGen argument. */
    public CodeGenerator(ASTModel ast) {
        this.ast = ast;
        this.options = ast.options();
    }

    /** Return a list of any other CodeGenerators producing code that this depends on. */
    public abstract Iterable<Class<? extends CodeGenerator>> dependencies();

    /**
   * Generate code to appear inside the given interface's declaration.  By convention, any output
   * should typically be followed by a blank line.
   */
    public abstract void generateInterfaceMembers(TabPrintWriter writer, NodeInterface i);

    /**
   * Generate code to appear inside the given interface's declaration.  By convention, any output
   * should typically be followed by a blank line.
   */
    public abstract void generateClassMembers(TabPrintWriter writer, NodeClass c);

    /**
   * Generate any necessary support code.
   * @see Options#createFileInOutDir, Options#createJavaSourceInOutDir, #copyFromResource
   */
    public abstract void generateAdditionalCode();

    /** Convenience method to create a singleton list.  (Generic options infer a too-tight type variable.) */
    public static Iterable<Class<? extends CodeGenerator>> singleDependency(Class<? extends CodeGenerator> c) {
        return IterUtil.<Class<? extends CodeGenerator>>singleton(c);
    }

    /** A factory producing CodeGenerators from a fixed AST, parameterized by a CodeGenerator class. */
    public static Lambda<Class<? extends CodeGenerator>, CodeGenerator> factory(final ASTModel ast) {
        return new Lambda<Class<? extends CodeGenerator>, CodeGenerator>() {

            public CodeGenerator value(Class<? extends CodeGenerator> c) {
                try {
                    return c.getConstructor(ASTModel.class).newInstance(ast);
                } catch (NoSuchMethodException e) {
                    throw error(c, e);
                } catch (SecurityException e) {
                    throw error(c, e);
                } catch (IllegalAccessException e) {
                    throw error(c, e);
                } catch (IllegalArgumentException e) {
                    throw error(c, e);
                } catch (InstantiationException e) {
                    throw error(c, e);
                } catch (InvocationTargetException e) {
                    throw error(c, e.getCause());
                } catch (ExceptionInInitializerError e) {
                    throw error(c, e.getCause());
                }
            }

            private ASTGenException error(Class<? extends CodeGenerator> c, Throwable e) {
                return new ASTGenException("Unable to instantiate CodeGenerator " + c.getName(), e);
            }
        };
    }

    /** Copy the contents of a resource to the writer with proper indentation; optionally, ignore
    * the file contents up to a line starting with {@code skipPast}.
    * @param skipPast  Text at the beginning of a line signalling that only subsequent
    *                  lines should be copied; may be {@code null}, which turns off any skipping
    */
    public static void copyFromResource(TabPrintWriter writer, String resource, String skipPast) {
        InputStream source = ASTGen.class.getResourceAsStream(resource);
        if (source == null) {
            throw new RuntimeException("Cannot find " + resource);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(source));
        try {
            String line = in.readLine();
            if (skipPast != null) {
                while (line != null && !line.startsWith(skipPast)) {
                    line = in.readLine();
                }
                if (line == null) {
                    throw new ASTGenException("Unexpected eof in " + resource);
                }
                line = in.readLine();
            }
            while (line != null) {
                writer.startLine(line);
                line = in.readLine();
            }
        } catch (IOException e) {
            throw new ASTGenException("Unexpected IOException from " + resource, e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /** Convert the first character in a string to upper case. */
    public static String upperCaseFirst(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
