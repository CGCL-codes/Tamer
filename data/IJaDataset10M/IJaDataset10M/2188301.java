package net.sf.antcontrib.cpptasks.sun;

import java.io.File;
import java.util.Vector;
import net.sf.antcontrib.cpptasks.CUtil;
import net.sf.antcontrib.cpptasks.compiler.AbstractCompiler;
import net.sf.antcontrib.cpptasks.compiler.CommandLineCCompiler;
import net.sf.antcontrib.cpptasks.compiler.LinkType;
import net.sf.antcontrib.cpptasks.compiler.Linker;
import net.sf.antcontrib.cpptasks.compiler.Processor;
import net.sf.antcontrib.cpptasks.OptimizationEnum;
import org.apache.tools.ant.types.Environment;

/**
 * Adapter for the Sun C89 C++ Compiler
 * 
 * @author Hiram Chirino (cojonudo14@hotmail.com)
 */
public class C89CCompiler extends CommandLineCCompiler {

    private static final AbstractCompiler instance = new C89CCompiler(false, null);

    public static AbstractCompiler getInstance() {
        return instance;
    }

    private C89CCompiler(boolean newEnvironment, Environment env) {
        super("c89", null, new String[] { ".c", ".cc", ".cpp", ".cxx", ".c++" }, new String[] { ".h", ".hpp" }, ".o", false, null, newEnvironment, env);
    }

    protected void addImpliedArgs(final Vector args, final boolean debug, final boolean multithreaded, final boolean exceptions, final LinkType linkType, final Boolean rtti, final OptimizationEnum optimization) {
        args.addElement("-c");
        if (debug) {
            args.addElement("-g");
            args.addElement("-D_DEBUG");
        } else {
            args.addElement("-DNDEBUG");
        }
    }

    protected void addWarningSwitch(Vector args, int level) {
        C89Processor.addWarningSwitch(args, level);
    }

    public Processor changeEnvironment(boolean newEnvironment, Environment env) {
        if (newEnvironment || env != null) {
            return new C89CCompiler(newEnvironment, env);
        }
        return this;
    }

    protected void getDefineSwitch(StringBuffer buf, String define, String value) {
        C89Processor.getDefineSwitch(buf, define, value);
    }

    protected File[] getEnvironmentIncludePath() {
        return CUtil.getPathFromEnvironment("INCLUDE", ":");
    }

    protected String getIncludeDirSwitch(String includeDir) {
        return C89Processor.getIncludeDirSwitch(includeDir);
    }

    public Linker getLinker(LinkType type) {
        return C89Linker.getInstance().getLinker(type);
    }

    public int getMaximumCommandLength() {
        return Integer.MAX_VALUE;
    }

    protected int getMaximumInputFilesPerCommand() {
        return 1;
    }

    protected void getUndefineSwitch(StringBuffer buf, String define) {
        C89Processor.getUndefineSwitch(buf, define);
    }
}
