package org.jmlspecs.jml2.checker;

import java.io.File;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.jmlspecs.jml2.util.Util;
import org.jmlspecs.jml4.compiler.DefaultCompilerExtension;
import org.jmlspecs.jml4.compiler.JmlCompilerOptions;
import org.jmlspecs.jml4.esc.util.Utils;
import org.jmlspecs.jml4.util.Logger;

public class JML2CheckerWrapper extends DefaultCompilerExtension {

    public static final boolean DEBUG = false;

    private static final String JML2_CHECKER = "jml";

    private static final String LINESEP = System.getProperty("line.separator");

    public String name() {
        return "JML2CheckerWrapper";
    }

    public void preCodeGeneration(Compiler compiler, CompilationUnitDeclaration unit) {
        if (!compiler.options.jmlEnabled && compiler.options.jml2CheckerEnabled) {
            this.comp(compiler, unit);
        }
    }

    private void comp(Compiler compiler, CompilationUnitDeclaration unit) {
        String fileName = String.valueOf(unit.getFileName());
        String filePath = Util.translatePath(fileName);
        filePath = Utils.win2unixFileName(filePath);
        String[] a = getExecArgs(filePath);
        if (DEBUG) Logger.println(this + " - fullFlNm : " + filePath);
        String output = Util.exec(a);
        if (DEBUG) Logger.println(this + " - output : \n" + output);
        if (output != null) {
            this.parse(compiler, unit, output);
        } else {
            String msg = "problem invoking JML2 checker: ";
            msg += org.eclipse.jdt.internal.core.util.Util.concatWith(a, ' ');
            compiler.problemReporter.jmlEsc2Error(msg, 0, 0);
        }
    }

    private String[] getExecArgs(String filePath) {
        String[] result = { JML2_CHECKER, "-Q", filePath };
        if (!underUnix()) {
            String[] adaptedResult = new String[result.length + 1];
            adaptedResult[0] = "bash";
            System.arraycopy(result, 0, adaptedResult, 1, result.length);
            result = adaptedResult;
        }
        return result;
    }

    private void parse(Compiler compiler, CompilationUnitDeclaration unit, String output) {
        String[] lines = output.split(LINESEP);
        for (int i = 0; i < lines.length; i++) parseLineAndReport(compiler, unit, lines[i]);
    }

    private static final String LINE = ", line ";

    private static final String CHAR = ", character ";

    private static final String ERROR = "error: ";

    private void parseLineAndReport(Compiler compiler, CompilationUnitDeclaration unit, String line) {
        int lineIdx = line.indexOf(LINE);
        if (lineIdx == -1) {
            Logger.println(this + " - Not able to find substring \"" + LINE + "\" in line : \n" + line);
            return;
        }
        int lineNoEnd = line.substring(lineIdx + 7).indexOf(',');
        int lineNo = Integer.parseInt(line.substring(lineIdx + 7, lineIdx + 7 + lineNoEnd));
        int charIdx = line.indexOf(CHAR);
        if (charIdx == -1) {
            Logger.println(this + " - Not able to find substring \"" + CHAR + "\" in line : \n" + line);
            return;
        }
        int errIdx = line.indexOf(ERROR);
        if (lineIdx == -1) {
            Logger.println(this + " - Not able to find substring \"" + ERROR + "\" in line : \n" + line);
            return;
        }
        String errMsg = "JML2 Checker: " + line.substring(errIdx + 7);
        ProblemReporter pr = compiler.problemReporter;
        int ss = Util.getSourceStartOfLine(unit.compilationResult.getLineSeparatorPositions(), lineNo);
        int se = Util.getSourceEndOfLine(unit.compilationResult.getLineSeparatorPositions(), lineNo);
        pr.jmlEsc2Error(errMsg, ss, se);
    }

    public void getOptionsMap(CompilerOptions options, Map optionsMap) {
        optionsMap.put(JmlCompilerOptions.OPTION_EnableJml2Checker, options.jml2CheckerEnabled ? CompilerOptions.ENABLED : CompilerOptions.DISABLED);
    }

    public void setOptionsMap(CompilerOptions options, Map optionsMap) {
        Object optionValue;
        if ((optionValue = optionsMap.get(JmlCompilerOptions.OPTION_EnableJml2Checker)) != null) {
            options.jml2CheckerEnabled = CompilerOptions.ENABLED.equals(optionValue);
        }
    }

    public int configureArgs(String currentArg, String[] args, int index, Map options) {
        if (currentArg.equals("-jml2")) {
            options.put(JmlCompilerOptions.OPTION_EnableJml2Checker, CompilerOptions.ENABLED);
            return index;
        }
        return super.configureArgs(currentArg, args, index, options);
    }

    private static boolean underUnix() {
        return File.separatorChar == '/';
    }

    public void optionsToBuffer(CompilerOptions options, StringBuffer buf) {
        buf.append("\n\t\t- JML2 checker: ").append(options.jml2CheckerEnabled ? CompilerOptions.ENABLED : CompilerOptions.DISABLED);
    }
}
