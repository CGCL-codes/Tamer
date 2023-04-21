package gap.jac.api;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.lang.model.SourceVersion;
import gap.jac.tools.*;
import gap.jac.source.util.JavacTask;
import gap.jac.file.JavacFileManager;
import gap.jac.JavacOption.OptionKind;
import gap.jac.JavacOption;
import gap.jac.Main;
import gap.jac.RecognizedOptions.GrumpyHelper;
import gap.jac.RecognizedOptions;
import gap.jac.util.Context;
import gap.jac.util.Log;
import gap.jac.util.Options;
import gap.jac.util.Pair;
import java.nio.charset.Charset;

/**
 *
 * @author Peter von der Ahé
 * @author jdp
 */
public final class JavacTool extends Object implements JavaCompiler {

    public static JavacTool create() {
        return new JavacTool();
    }

    private boolean compilationInProgress = false;

    private final List<Pair<String, String>> options = new ArrayList<Pair<String, String>>();

    private final Context dummyContext = new Context();

    private final PrintWriter silent = new PrintWriter(new OutputStream() {

        public void write(int b) {
        }
    });

    private final Main sharedCompiler;

    public JavacTool() {
        super();
        this.sharedCompiler = new Main("javac", silent);
        this.sharedCompiler.setOptions(Options.instance(dummyContext));
    }

    public void setOption(String name, Object... args) {
        setOption1(name, OptionKind.NORMAL, args);
    }

    public void setExtendedOption(String name, Object... args) {
        setOption1(name, OptionKind.EXTENDED, args);
    }

    public JavacFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset) {
        Context context = new Context();
        if (diagnosticListener != null) context.put(DiagnosticListener.class, diagnosticListener);
        context.put(Log.outKey, new PrintWriter(System.err, true));
        return new JavacFileManager(context, true, charset);
    }

    public JavacTask getTask(Writer out, JavaFileManager fileManager, DiagnosticListener<? super JavaFileObject> diagnosticListener, Iterable<String> options, Iterable<String> classes, Iterable<? extends JavaFileObject> compilationUnits) {
        final String kindMsg = "All compilation units must be of SOURCE kind";
        if (options != null) for (String option : options) option.getClass();
        if (classes != null) {
            for (String cls : classes) if (!SourceVersion.isName(cls)) throw new IllegalArgumentException("Not a valid class name: " + cls);
        }
        if (compilationUnits != null) {
            for (JavaFileObject cu : compilationUnits) {
                if (cu.getKind() != JavaFileObject.Kind.SOURCE) throw new IllegalArgumentException(kindMsg);
            }
        }
        Context context = new Context();
        if (diagnosticListener != null) context.put(DiagnosticListener.class, diagnosticListener);
        if (out == null) context.put(Log.outKey, new PrintWriter(System.err, true)); else context.put(Log.outKey, new PrintWriter(out, true));
        if (fileManager == null) fileManager = getStandardFileManager(diagnosticListener, null, null);
        context.put(JavaFileManager.class, fileManager);
        processOptions(context, fileManager, options);
        Main compiler = new Main("javacTask", context.get(Log.outKey));
        return new JavacTaskImpl(this, compiler, options, context, classes, compilationUnits);
    }

    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        if (err == null) err = System.err;
        for (String argument : arguments) argument.getClass();
        return gap.jac.Main.main(arguments, new PrintWriter(err, true));
    }

    public Set<SourceVersion> getSourceVersions() {
        return Collections.unmodifiableSet(EnumSet.range(SourceVersion.RELEASE_3, SourceVersion.latest()));
    }

    public int isSupportedOption(String option) {
        JavacOption[] recognizedOptions = RecognizedOptions.getJavacToolOptions(new GrumpyHelper());
        for (JavacOption o : recognizedOptions) {
            if (o.matches(option)) return o.hasArg() ? 1 : 0;
        }
        return -1;
    }

    public void destroy() {
    }

    /**
     * Register that a compilation is about to start.
     */
    void beginContext(final Context context) {
        if (compilationInProgress) throw new IllegalStateException("Compilation in progress");
        compilationInProgress = true;
        final JavaFileManager givenFileManager = context.get(JavaFileManager.class);
        context.put(JavaFileManager.class, (JavaFileManager) null);
        context.put(JavaFileManager.class, new Context.Factory<JavaFileManager>() {

            public JavaFileManager make() {
                if (givenFileManager != null) {
                    context.put(JavaFileManager.class, givenFileManager);
                    return givenFileManager;
                } else {
                    return new JavacFileManager(context, true, null);
                }
            }
        });
    }

    /**
     * Register that a compilation is completed.
     */
    void endContext() {
        compilationInProgress = false;
    }

    private String argsToString(Object... args) {
        String newArgs = null;
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            String separator = "";
            for (Object arg : args) {
                sb.append(separator).append(arg.toString());
                separator = File.pathSeparator;
            }
            newArgs = sb.toString();
        }
        return newArgs;
    }

    private void setOption1(String name, OptionKind kind, Object... args) {
        String arg = this.argsToString(args);
        JavacOption option = this.sharedCompiler.getOption(name);
        if (option == null || !match(kind, option.getKind())) throw new IllegalArgumentException(name);
        if ((args.length != 0) != option.hasArg()) throw new IllegalArgumentException(name);
        if (option.hasArg()) {
            if (option.process(null, name, arg)) throw new IllegalArgumentException(name);
        } else {
            if (option.process(null, name)) throw new IllegalArgumentException(name);
        }
        options.add(new Pair<String, String>(name, arg));
    }

    private static boolean match(OptionKind clientKind, OptionKind optionKind) {
        return (clientKind == (optionKind == OptionKind.HIDDEN ? optionKind.EXTENDED : optionKind));
    }

    private static void processOptions(Context context, JavaFileManager fileManager, Iterable<String> options) {
        if (options == null) return;
        Options optionTable = Options.instance(context);
        JavacOption[] recognizedOptions = RecognizedOptions.getJavacToolOptions(new GrumpyHelper());
        Iterator<String> flags = options.iterator();
        while (flags.hasNext()) {
            String flag = flags.next();
            int j;
            for (j = 0; j < recognizedOptions.length; j++) if (recognizedOptions[j].matches(flag)) break;
            if (j == recognizedOptions.length) {
                if (fileManager.handleOption(flag, flags)) {
                    continue;
                } else {
                    String msg = Main.getLocalizedString("err.invalid.flag", flag);
                    throw new IllegalArgumentException(msg);
                }
            }
            JavacOption option = recognizedOptions[j];
            if (option.hasArg()) {
                if (!flags.hasNext()) {
                    String msg = Main.getLocalizedString("err.req.arg", flag);
                    throw new IllegalArgumentException(msg);
                }
                String operand = flags.next();
                if (option.process(optionTable, flag, operand)) throw new IllegalArgumentException(flag + " " + operand);
            } else {
                if (option.process(optionTable, flag)) throw new IllegalArgumentException(flag);
            }
        }
    }
}
