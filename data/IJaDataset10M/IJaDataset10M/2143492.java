package org.eclipse.jdt.internal.compiler.apt.util;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Class used to handle options in the EclipseFileManager and the EclipseCompiler
 */
public final class Options {

    private static final Set<String> ZERO_ARGUMENT_OPTIONS;

    private static final Set<String> ONE_ARGUMENT_OPTIONS;

    private static final Set<String> FILE_MANAGER_OPTIONS;

    static {
        ZERO_ARGUMENT_OPTIONS = new HashSet<String>();
        ZERO_ARGUMENT_OPTIONS.add("-progress");
        ZERO_ARGUMENT_OPTIONS.add("-proceedOnError");
        ZERO_ARGUMENT_OPTIONS.add("-time");
        ZERO_ARGUMENT_OPTIONS.add("-v");
        ZERO_ARGUMENT_OPTIONS.add("-version");
        ZERO_ARGUMENT_OPTIONS.add("-showversion");
        ZERO_ARGUMENT_OPTIONS.add("-deprecation");
        ZERO_ARGUMENT_OPTIONS.add("-help");
        ZERO_ARGUMENT_OPTIONS.add("-?");
        ZERO_ARGUMENT_OPTIONS.add("-help:warn");
        ZERO_ARGUMENT_OPTIONS.add("-?:warn");
        ZERO_ARGUMENT_OPTIONS.add("-noExit");
        ZERO_ARGUMENT_OPTIONS.add("-verbose");
        ZERO_ARGUMENT_OPTIONS.add("-referenceInfo");
        ZERO_ARGUMENT_OPTIONS.add("-inlineJSR");
        ZERO_ARGUMENT_OPTIONS.add("-g");
        ZERO_ARGUMENT_OPTIONS.add("-g:none");
        ZERO_ARGUMENT_OPTIONS.add("-nowarn");
        ZERO_ARGUMENT_OPTIONS.add("-warn:none");
        ZERO_ARGUMENT_OPTIONS.add("-preserveAllLocals");
        ZERO_ARGUMENT_OPTIONS.add("-enableJavadoc");
        ZERO_ARGUMENT_OPTIONS.add("-Xemacs");
        ZERO_ARGUMENT_OPTIONS.add("-X");
        ZERO_ARGUMENT_OPTIONS.add("-O");
        ZERO_ARGUMENT_OPTIONS.add("-1.3");
        ZERO_ARGUMENT_OPTIONS.add("-1.4");
        ZERO_ARGUMENT_OPTIONS.add("-1.5");
        ZERO_ARGUMENT_OPTIONS.add("-5");
        ZERO_ARGUMENT_OPTIONS.add("-5.0");
        ZERO_ARGUMENT_OPTIONS.add("-1.6");
        ZERO_ARGUMENT_OPTIONS.add("-6");
        ZERO_ARGUMENT_OPTIONS.add("-6.0");
        ZERO_ARGUMENT_OPTIONS.add("-proc:only");
        ZERO_ARGUMENT_OPTIONS.add("-proc:none");
        ZERO_ARGUMENT_OPTIONS.add("-XprintProcessorInfo");
        ZERO_ARGUMENT_OPTIONS.add("-XprintRounds");
        FILE_MANAGER_OPTIONS = new HashSet<String>();
        FILE_MANAGER_OPTIONS.add("-bootclasspath");
        FILE_MANAGER_OPTIONS.add("-encoding");
        FILE_MANAGER_OPTIONS.add("-d");
        FILE_MANAGER_OPTIONS.add("-classpath");
        FILE_MANAGER_OPTIONS.add("-cp");
        FILE_MANAGER_OPTIONS.add("-sourcepath");
        FILE_MANAGER_OPTIONS.add("-extdirs");
        FILE_MANAGER_OPTIONS.add("-endorseddirs");
        FILE_MANAGER_OPTIONS.add("-s");
        FILE_MANAGER_OPTIONS.add("-processorpath");
        ONE_ARGUMENT_OPTIONS = new HashSet<String>();
        ONE_ARGUMENT_OPTIONS.addAll(FILE_MANAGER_OPTIONS);
        ONE_ARGUMENT_OPTIONS.add("-log");
        ONE_ARGUMENT_OPTIONS.add("-repeat");
        ONE_ARGUMENT_OPTIONS.add("-maxProblems");
        ONE_ARGUMENT_OPTIONS.add("-source");
        ONE_ARGUMENT_OPTIONS.add("-target");
        ONE_ARGUMENT_OPTIONS.add("-processor");
        ONE_ARGUMENT_OPTIONS.add("-classNames");
    }

    public static int processOptionsFileManager(String option) {
        if (option == null) return -1;
        if (FILE_MANAGER_OPTIONS.contains(option)) {
            return 1;
        }
        return -1;
    }

    public static int processOptions(String option) {
        if (option == null) return -1;
        if (ZERO_ARGUMENT_OPTIONS.contains(option)) {
            return 0;
        }
        if (ONE_ARGUMENT_OPTIONS.contains(option)) {
            return 1;
        }
        if (option.startsWith("-g")) {
            int length = option.length();
            if (length > 3) {
                StringTokenizer tokenizer = new StringTokenizer(option.substring(3, option.length()), ",");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if ("vars".equals(token) || "lines".equals(token) || "source".equals(token)) {
                        continue;
                    }
                    return -1;
                }
                return 0;
            }
            return -1;
        }
        if (option.startsWith("-warn")) {
            int length = option.length();
            if (length <= 6) {
                return -1;
            }
            int warnTokenStart;
            switch(option.charAt(6)) {
                case '+':
                    warnTokenStart = 7;
                    break;
                case '-':
                    warnTokenStart = 7;
                    break;
                default:
                    warnTokenStart = 6;
            }
            StringTokenizer tokenizer = new StringTokenizer(option.substring(warnTokenStart, option.length()), ",");
            int tokenCounter = 0;
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                tokenCounter++;
                if ("constructorName".equals(token) || token.equals("pkgDefaultMethod") || token.equals("packageDefaultMethod") || token.equals("maskedCatchBlock") || token.equals("maskedCatchBlocks") || token.equals("deprecation") || token.equals("allDeprecation") || token.equals("unusedLocal") || token.equals("unusedLocals") || token.equals("unusedArgument") || token.equals("unusedArguments") || token.equals("unusedImport") || token.equals("unusedImports") || token.equals("unusedPrivate") || token.equals("unusedLabel") || token.equals("localHiding") || token.equals("fieldHiding") || token.equals("specialParamHiding") || token.equals("conditionAssign") || token.equals("syntheticAccess") || token.equals("synthetic-access") || token.equals("nls") || token.equals("staticReceiver") || token.equals("indirectStatic") || token.equals("noEffectAssign") || token.equals("intfNonInherited") || token.equals("interfaceNonInherited") || token.equals("charConcat") || token.equals("noImplicitStringConversion") || token.equals("semicolon") || token.equals("serial") || token.equals("emptyBlock") || token.equals("uselessTypeCheck") || token.equals("unchecked") || token.equals("unsafe") || token.equals("raw") || token.equals("finalBound") || token.equals("suppress") || token.equals("warningToken") || token.equals("unnecessaryElse") || token.equals("javadoc") || token.equals("allJavadoc") || token.equals("assertIdentifier") || token.equals("enumIdentifier") || token.equals("finally") || token.equals("unusedThrown") || token.equals("unqualifiedField") || token.equals("unqualified-field-access") || token.equals("typeHiding") || token.equals("varargsCast") || token.equals("null") || token.equals("boxing") || token.equals("over-ann") || token.equals("dep-ann") || token.equals("intfAnnotation") || token.equals("enumSwitch") || token.equals("incomplete-switch") || token.equals("hiding") || token.equals("static-access") || token.equals("unused") || token.equals("paramAssign") || token.equals("discouraged") || token.equals("forbidden") || token.equals("fallthrough")) {
                    continue;
                } else if (token.equals("tasks")) {
                    String taskTags = "";
                    int start = token.indexOf('(');
                    int end = token.indexOf(')');
                    if (start >= 0 && end >= 0 && start < end) {
                        taskTags = token.substring(start + 1, end).trim();
                        taskTags = taskTags.replace('|', ',');
                    }
                    if (taskTags.length() == 0) {
                        return -1;
                    }
                    continue;
                } else {
                    return -1;
                }
            }
            if (tokenCounter == 0) {
                return -1;
            } else {
                return 0;
            }
        }
        if (option.startsWith("-J") || option.startsWith("-X") || option.startsWith("-A")) {
            return 0;
        }
        return -1;
    }
}
