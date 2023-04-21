package protoj.core.internal;

import java.util.List;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import protoj.core.Command;
import protoj.core.CommandStore;

/**
 * Compiles the project code using the javac compiler. Typically this command is
 * invoked from inside a shell script as an alternative to invoking the javac
 * compiler in an os-specific way. For example the logic that detects whether or
 * not the source directory exists would be hard to replicate across batch files
 * and bash scripts.
 * <p>
 * In order to configure the underlying ant task, beans introspection is used on
 * the protoj.core.JavacCompileTask parent. First an example of a javac command
 * that configures the ant source property and includes filter using the protoj
 * -opt option that accepts name=value pairs:
 * 
 * <pre>
 * &quot;javac -opt compileTask.source=1.5 -opt dirSet.includes=org/foo/core&quot;
 * </pre>
 * 
 * That parent contains two properties called compileTask and dirSet that are
 * instances of org.apache.tools.ant.taskdefs.Javac and
 * org.apache.tools.ant.types.DirSet respectively and so consult the ant
 * reference to determine the available set of properties that are available for
 * configuration.
 * 
 * @author Ashley Williams
 * 
 */
public final class JavacCommand {

    private final class Body implements Runnable {

        public void run() {
            OptionSet options = delegate.getOptions();
            if (options.has(optOption)) {
                List<String> compilerOptions = options.valuesOf(optOption);
                parent.getCompileFeature().compileJavac(compilerOptions);
            }
        }
    }

    /**
	 * Provides the basic command functionality.
	 */
    private Command delegate;

    /**
	 * The parent of this command.
	 */
    private final CoreProject parent;

    private OptionSpec<String> optOption;

    public JavacCommand(CoreProject core) {
        this.parent = core;
        CommandStore store = core.getCommandStore();
        StringBuilder description = new StringBuilder();
        description.append("\nCompiles the project code using the javac compiler. Typically this command is");
        description.append("\ninvoked from inside a shell script as an alternative to invoking the javac");
        description.append("\ncompiler in an os-specific way. For example the logic that detects whether or");
        description.append("\nnot the source directory exists would be hard to replicate across batch files");
        description.append("\nand bash scripts.");
        description.append("\n");
        description.append("\nIn order to configure the underlying ant task, beans introspection is used on");
        description.append("\nthe protoj.core.JavacCompileTask parent. First an example of a javac command");
        description.append("\nthat configures the ant source property and includes filter using the protoj");
        description.append("\n-opt option that accepts name=value pairs:");
        description.append("\n");
        description.append("\n\"javac -opt compileTask.source=1.5 -opt dirSet.includes=org/foo/core\"");
        description.append("\n");
        description.append("\nThat parent contains two properties called compileTask and dirSet that are");
        description.append("\ninstances of org.apache.tools.ant.taskdefs.Javac and");
        description.append("\norg.apache.tools.ant.types.DirSet respectively and so consult the ant");
        description.append("\nreference to determine the available set of properties that are available for");
        description.append("\nconfiguration.");
        delegate = store.addCommand("javac", description.toString(), "16m", new Body());
        delegate.initBootstrapCurrentVm();
        optOption = delegate.getParser().accepts("opt").withRequiredArg();
    }

    public Command getDelegate() {
        return delegate;
    }
}
