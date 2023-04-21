package de.grogra.xl.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.objectweb.asm.Opcodes;
import de.grogra.grammar.RecognitionException;
import de.grogra.grammar.Tokenizer;
import de.grogra.reflect.Field;
import de.grogra.reflect.Member;
import de.grogra.reflect.Reflection;
import de.grogra.reflect.Type;
import de.grogra.util.Utils;
import de.grogra.vfs.FileSystem;
import de.grogra.vfs.LocalFileSystem;
import de.grogra.xl.compiler.scope.ClassPath;
import de.grogra.xl.compiler.scope.CompilationUnitScope;
import de.grogra.xl.compiler.scope.PackageImportOnDemand;
import de.grogra.xl.parser.JavaTokenizer;
import de.grogra.xl.parser.Parser;
import de.grogra.xl.parser.XLParser;
import de.grogra.xl.parser.XLTokenizer;

public final class Main {

    private Main() {
    }

    private static void showHelpAndExit(String msg) {
        if (msg != null) {
            System.err.println(msg);
        }
        System.err.println("Usage: xlc <options> <source files> <exec option>");
        System.err.println("where possible options include:");
        System.err.println("  -g                      Generate all debugging info");
        System.err.println("  -g:none                 Generate no debugging info");
        System.err.println("  -g:{lines,vars,source}  Generate only some debugging info");
        System.err.println("  -nowarn                 Generate no warnings");
        System.err.println("  -verbose                Output messages about what the compiler is doing");
        System.err.println("  -classpath <path>       Specify where to find user class files");
        System.err.println("  -cp <path>              Specify where to find user class files");
        System.err.println("  -bootclasspath <path>   Override location of bootstrap class files");
        System.err.println("  -extdirs <dirs>         Override location of installed extensions");
        System.err.println("  -d <directory>          Specify where to place generated class files");
        System.err.println("  -encoding <encoding>    Specify character encoding used by source files");
        System.err.println("  -source <release>       Provide source compatibility with specified release");
        System.err.println("  -target <release>       Generate class files for specific VM version");
        System.err.println("  -help                   Print a synopsis of standard options");
        System.err.println();
        System.err.println("  -Xeaa {true|false}      Enable Autoconversion Annotation (default: " + CompilerOptions.DEFAULT_ENABLE_AUTOCONVERSION_ANNOTATION + ")");
        System.err.println("  -Xasv {true|false}      Allow Autoconversion with static valueOf (default: " + CompilerOptions.DEFAULT_ALLOW_CONVERSION_WITH_STATIC_VALUEOF + ")");
        System.err.println("  -Xasto {true|false}     Allow Autoconversion with static toX (default: " + CompilerOptions.DEFAULT_ALLOW_CONVERSION_WITH_STATIC_TOX + ")");
        System.err.println("  -Xato {true|false}      Allow Autoconversion with toX (default: " + CompilerOptions.DEFAULT_ALLOW_CONVERSION_WITH_TOX + ")");
        System.err.println("  -Xactor {true|false}    Allow Autoconversion with constructor (default: " + CompilerOptions.DEFAULT_ALLOW_CONVERSION_WITH_CTOR + ")");
        System.err.println();
        System.err.println("  -Xast                   Print abstract syntax tree");
        System.err.println("  -Xmember                List compiled types and their members");
        System.err.println("  -Xexpr                  Print compiled expression trees for methods");
        System.err.println();
        System.err.println("The exec option has to be specified after the source files:");
        System.err.println("  -Xexec <class> <args>   Execute main-method of given class after compilation");
        System.exit(1);
    }

    private static void exit(String msg) {
        System.err.println(msg);
        System.exit(1);
    }

    private static String[] args;

    private static int argIndex;

    private static String param;

    private static String encoding;

    private static boolean verbose;

    private static boolean dumpAST;

    private static ArrayList parsers;

    private static ProblemReporter problems;

    private static boolean ok = true;

    private static boolean parse(String s) throws IOException {
        if (verbose) {
            System.err.print("Parsing " + s + " ... ");
        }
        Tokenizer t = s.toLowerCase().endsWith(".java") ? new JavaTokenizer() : (Tokenizer) new XLTokenizer();
        FileInputStream in = new FileInputStream(s);
        t.setSource(new BufferedReader((encoding == null) ? new InputStreamReader(in) : new InputStreamReader(in, encoding)), s.substring(s.lastIndexOf(File.separatorChar) + 1));
        XLParser p = new XLParser(t);
        p.setDumpTree(dumpAST);
        try {
            p.parse();
            if (verbose) {
                System.err.println("OK");
            }
            parsers.add(p);
            problems.addAll(p.getExceptionList());
            return true;
        } catch (RecognitionException e) {
            if (verbose) {
                System.err.println("Syntax error");
            }
            problems.addAll(p.getExceptionList());
            return false;
        }
    }

    /**
	 * Checks if the current argument <code>arg[argIndex]</code>
	 * equals <code>name</code>. If so, the argument is consumed
	 * by incrementing <code>argIndex</code>, and the
	 * value of the next argument is stored in {@link #param}. If no next
	 * argument exists, program execution terminates. 
	 * 
	 * @param name a name of an argument having a parameter
	 * @return <code>true</code> iff the current argument equals <code>name</code>
	 */
    private static boolean checkArgWithParam(String name) {
        if (args[argIndex].equals(name)) {
            argIndex++;
            if (argIndex == args.length) {
                showHelpAndExit("Missing argument to " + name);
            }
            param = args[argIndex];
            return true;
        }
        return false;
    }

    private static int parseVersion(String s) {
        if ("1.4".equals(s)) {
            return Opcodes.V1_4;
        } else if ("1.5".equals(s) || "5".equals(s)) {
            return Opcodes.V1_5;
        } else {
            showHelpAndExit("Unsupported version " + s);
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {
        java.lang.reflect.Method m = run(args);
        if (m != null) {
            parsers = null;
            problems = null;
            String[] a = new String[args.length - argIndex];
            System.arraycopy(args, argIndex, a, 0, a.length);
            m.invoke(null, new Object[] { a });
        }
        System.exit(ok ? 0 : 1);
    }

    private static java.lang.reflect.Method run(String[] args) throws Exception {
        Main.args = args;
        boolean dumpExpr = false, dumpMembers = false, warnings = true;
        File output = new File(System.getProperty("user.dir"));
        String exec = null;
        String classPath = System.getProperty("user.dir");
        String extensionDirs = System.getProperty("java.ext.dirs", null);
        String bootClassPath = null;
        CompilerOptions opts = new CompilerOptions();
        for (argIndex = 0; argIndex < args.length; argIndex++) {
            String s = args[argIndex];
            if (!s.startsWith("-")) {
                break;
            }
            if (s.equals("-help")) {
                showHelpAndExit(null);
            } else if (s.equals("-g")) {
                opts.sourceInfo = true;
                opts.lineNumberInfo = true;
                opts.localInfo = true;
            } else if (s.startsWith("-g:")) {
                opts.sourceInfo = false;
                opts.lineNumberInfo = false;
                opts.localInfo = false;
                StringTokenizer t = new StringTokenizer(s.substring(3), ",");
                while (t.hasMoreTokens()) {
                    String o = t.nextToken();
                    if (o.equals("lines")) {
                        opts.lineNumberInfo = true;
                    } else if (o.equals("vars")) {
                        opts.localInfo = true;
                    } else if (o.equals("source")) {
                        opts.sourceInfo = true;
                    } else if (!o.equals("none")) {
                        showHelpAndExit("Unknown debugging info " + o);
                    }
                }
            } else if (s.equals("-nowarn")) {
                warnings = false;
            } else if (s.equals("-verbose")) {
                verbose = true;
            } else if (s.equals("-Xast")) {
                dumpAST = true;
            } else if (s.equals("-Xmember")) {
                dumpMembers = true;
            } else if (s.equals("-Xexpr")) {
                dumpExpr = true;
            } else if (checkArgWithParam("-d")) {
                output = new File(param);
            } else if (checkArgWithParam("-classpath") || checkArgWithParam("-cp")) {
                classPath = param;
            } else if (checkArgWithParam("-sourcepath")) {
                System.err.println("The -sourcepath option has not yet been implemented, it is ignored");
            } else if (checkArgWithParam("-bootclasspath")) {
                bootClassPath = param;
            } else if (checkArgWithParam("-extdirs")) {
                extensionDirs = param;
            } else if (checkArgWithParam("-source")) {
                parseVersion(param);
            } else if (checkArgWithParam("-target")) {
                opts.javaVersion = parseVersion(param);
            } else if (checkArgWithParam("-Xeaa")) {
                opts.enableAutoconversionAnnotation = Boolean.valueOf(param).booleanValue();
            } else if (checkArgWithParam("-Xasv")) {
                opts.allowConversionWithStaticValueOf = Boolean.valueOf(param).booleanValue();
            } else if (checkArgWithParam("-Xasto")) {
                opts.allowConversionWithStaticToX = Boolean.valueOf(param).booleanValue();
            } else if (checkArgWithParam("-Xato")) {
                opts.allowConversionWithToX = Boolean.valueOf(param).booleanValue();
            } else if (checkArgWithParam("-Xactor")) {
                opts.allowConversionWithCtor = Boolean.valueOf(param).booleanValue();
            } else if (checkArgWithParam("-encoding")) {
                encoding = param;
            } else {
                showHelpAndExit("Unknown option " + s);
            }
        }
        if (verbose) {
            for (Iterator i = System.getProperties().entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                System.err.println(e.getKey() + " = " + e.getValue());
            }
        }
        if (argIndex == args.length) {
            showHelpAndExit(null);
        }
        if (!output.isDirectory()) {
            exit("The output directory " + output + " does not exist.");
        }
        Utils.resetTime();
        parsers = new ArrayList();
        problems = new ProblemReporter(warnings ? -1L : 0, 0);
        while (argIndex < args.length) {
            String s = args[argIndex++];
            if (s.equals("-Xexec")) {
                if (argIndex == args.length) {
                    showHelpAndExit("Missing argument to -Xexec");
                }
                exec = args[argIndex++];
                break;
            }
            if (s.startsWith("@")) {
                s = s.substring(1).trim();
                FileReader files = new FileReader(s);
                StringBuffer b = new StringBuffer(1000);
                char[] buf = new char[1024];
                int n;
                while ((n = files.read(buf)) >= 0) {
                    b.append(buf, 0, n);
                }
                files.close();
                for (StringTokenizer t = new StringTokenizer(b.toString()); t.hasMoreTokens(); ) {
                    ok &= parse(t.nextToken());
                }
            } else {
                ok &= parse(s);
            }
        }
        if (verbose) {
            Utils.printTime("Parsing done.");
        }
        CompilationUnitScope[] results;
        ASMTypeLoader loader = new ASMTypeLoader(null, Main.class.getClassLoader());
        FileSystem fs = new LocalFileSystem("output", output);
        if (parsers.isEmpty()) {
            results = null;
            ok = false;
        } else {
            CompilationUnit[] units = new CompilationUnit[parsers.size()];
            File[] ext = (extensionDirs != null) ? ASMTypeLoader.getExtensionClassPath(extensionDirs) : new File[0];
            File[] boot = (bootClassPath != null) ? ASMTypeLoader.getClassPath(bootClassPath) : ASMTypeLoader.getBootClassPath(ext);
            File[] cpath = ASMTypeLoader.getClassPath(classPath);
            loader.addFiles(LocalFileSystem.FILE_ADAPTER, boot, false);
            loader.addFiles(LocalFileSystem.FILE_ADAPTER, ext, false);
            loader.addFiles(LocalFileSystem.FILE_ADAPTER, cpath, true);
            if (verbose) {
                System.err.println("Bootpath    = " + Arrays.toString(boot));
                System.err.println("Extensions  = " + Arrays.toString(ext));
                System.err.println("Classpath   = " + Arrays.toString(cpath));
                Utils.printTime("Scanning of classpath done.");
            }
            ClassPath root = new ClassPath(loader);
            BytecodeWriter writer = new BytecodeWriter(opts);
            for (int i = 0; i < parsers.size(); i++) {
                Parser p = (Parser) parsers.get(i);
                units[i] = new CompilationUnit(root, p.getAST(), p.getFilename(), p.getExceptionList(), new PackageImportOnDemand(null, root.getPackage("java.lang", false)), opts, null);
            }
            Compiler comp = new Compiler();
            comp.problems = problems;
            PrintWriter out = null;
            if (verbose) {
                out = new PrintWriter(System.err);
                comp.setVerbose(out);
            }
            results = comp.compile(units, null, writer, fs, output, dumpExpr);
            if (verbose) {
                out.flush();
                Utils.printTime("Compilation done.");
            }
        }
        if (!problems.isEmpty()) {
            System.err.println(problems.getDetailedMessage(false));
        }
        if (problems.containsErrors()) {
            ok = false;
        }
        if (ok) {
            for (int i = 0; i < results.length; i++) {
                if (dumpExpr || dumpMembers) {
                    Type[] a = results[i].getDeclaredTypes();
                    for (int j = 0; j < a.length; j++) {
                        dumpType(a[j], dumpExpr);
                    }
                    a = results[i].getLocalClasses();
                    for (int j = 0; j < a.length; j++) {
                        dumpType(a[j], dumpExpr);
                    }
                    System.out.println();
                }
            }
            if (verbose) {
                Utils.printTime("Writing of bytecode done.");
            }
            if (exec != null) {
                URL u = fs.toURL(fs.getRoot());
                ClassLoader cloader = new URLClassLoader(new URL[] { u }, loader.getClassLoader());
                return Class.forName(exec, false, cloader).getMethod("main", new Class[] { args.getClass() });
            }
        }
        return null;
    }

    public static void dumpType(Type t, boolean dumpExpr) {
        System.out.print("\n" + t.getBinaryName() + " extends " + t.getSupertype().getBinaryName());
        int n = t.getDeclaredInterfaceCount();
        if (n > 0) {
            System.out.print(" implements ");
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(t.getDeclaredInterface(i).getBinaryName());
            }
        }
        System.out.println();
        Member[] m;
        m = Reflection.getDeclaredFields(t);
        for (int i = 0; i < m.length; i++) {
            Field f = (Field) m[i];
            System.out.print(f.getDescriptor());
            if ((f.getModifiers() & Member.CONSTANT) != 0) {
                System.out.print(" = ");
                try {
                    System.out.print(Reflection.get(null, f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        }
        m = Reflection.getDeclaredMethods(t);
        for (int i = 0; i < m.length; i++) {
            System.out.println(m[i].getDescriptor());
            if (dumpExpr) {
                ((XMethod) m[i]).dumpTree();
            }
        }
        for (int i = 0; i < t.getDeclaredTypeCount(); i++) {
            dumpType(t.getDeclaredType(i), dumpExpr);
        }
    }
}
