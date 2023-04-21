package fw4ex.markingscriptcompiler.compiler;

import fw4ex.markingscriptcompiler.compiler.lexenv.ILexEnv;
import fw4ex.markingscriptcompiler.compiler.lexenv.LexEnvLoopNode;
import fw4ex.markingscriptcompiler.exceptions.CompileException;
import fw4ex.markingscriptcompiler.exceptions.LexEnvException;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.FW4EXDir;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.MarkingScript;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.chdir.ChDir;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.AbstractComponent;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.AssertCommand;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.AssertScript;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.CompareCommand;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.File;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.FileContent;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.GradingScript;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.Input;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.Option;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.loop.FileLoop;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.loop.StringLoop;

/**
 * Compiles a {@link MarkingScript} to bash. <br/>
 * The compiled bash is meant to be ran on a FW4EX server. <br/>
 * Since the {@link MarkingScript} is compiled into bash, the
 * <code>Object</code> returned by all the <code>compile</code> methods
 * can be safely cast to <code>String</code>.
 */
public class BashCompiler extends AbstractCompiler {

    /**
     * Converts the given {@link FW4EXDir} to the suitable bash representation.
     * Appends a final '/' to the returned path if the returned value is
     * actually a path (it's not if we convert <code>FW4EXDIR.PATH</code>).
     * @param dir The {@link FW4EXDir} to convert.
     * @return Suitable bash representation of the {@link FW4EXDir},
     * with a final '/'
     * when the returned value is an actual directory (it's not if we convert
     * FW4EXDIR.PATH).
     */
    public String convertFW4EXDir(FW4EXDir dir) throws CompileException {
        switch(dir) {
            case PATH:
                return "";
            case STUDENT:
                return "$HOME" + DIR_SEPARATOR;
            case TEACHER:
                return "$FW4EX_EXERCISE_DIR" + DIR_SEPARATOR;
            case UNIX:
                return "/";
            default:
                throw new CompileException("Unknown FW4EXDir.");
        }
    }

    /** The directory separator used to write paths. */
    private static String DIR_SEPARATOR = "/";

    /** The "new line" sequence. */
    private static String CARRIAGE_RETURN = "\n";

    /**
	 * Compilation schema <br/>
     * <ul>
     *  <li>Include all resources needed.</li>
     *  <li>Compile first child.</li>
     * </ul>
	 */
    public Object compile(MarkingScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh").append(CARRIAGE_RETURN).append(CARRIAGE_RETURN).append("source $FW4EX_LIB_DIR/basicLib.sh").append(CARRIAGE_RETURN).append("source $FW4EX_LIB_DIR/moreLib.sh").append(CARRIAGE_RETURN).append("source $FW4EX_LIB_DIR/Patterns/comparisonLib.sh").append(CARRIAGE_RETURN).append(CARRIAGE_RETURN);
        return sb.append(compilable.getRootEntity().accept(this, lexenv, visitType)).toString();
    }

    /**
	 * Compilation schema <br/>
	 *
	 * If the chDir is refers to a loop :
	 * <pre>cd "$loopName" || exit 102</pre>
	 * else :
	 * <pre>cd 'path/to/dir/' || exit 102</pre>
	 * If there's a nested entity, we compile it.
	 */
    public Object compile(ChDir compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("cd ");
        if (compilable.isRef()) {
            LexEnvLoopNode loop = (LexEnvLoopNode) lexenv.lookUp(compilable.getNameRef());
            sb.append("\"$").append(loop.getLoopName()).append("\"");
        } else {
            sb.append(convertFW4EXDir(compilable.getFw4exDir())).append("'").append(compilable.getDirName()).append("'");
        }
        sb.append(CARRIAGE_RETURN);
        if (compilable.hasNestedEntity()) {
            sb.append(compilable.getNestedEntity().accept(this, lexenv, visitType));
        }
        return sb.toString();
    }

    /**
	 * Compilation schema
	 * <pre>
	 * fw4ex_run_student_command 'path/to/command' {components} {input}
	 * {assertScript}
	 * {gradingScripts}
	 * </pre>
	 */
    public Object compile(AssertCommand compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("fw4ex_run_student_command ");
        sb.append(convertFW4EXDir(compilable.getFw4exDir())).append("'").append(compilable.getCmdName()).append("' ");
        if (compilable.hasComponents()) {
            for (AbstractComponent c : compilable.getComponents()) {
                sb.append(c.accept(this, lexenv, visitType)).append(" ");
            }
        }
        if (compilable.hasInput()) {
            sb.append(" ").append(compilable.getInput().accept(this, lexenv, visitType));
        }
        sb.append(CARRIAGE_RETURN);
        for (AssertScript s : compilable.getAssertScripts()) {
            sb.append(s.accept(this, lexenv, visitType)).append(CARRIAGE_RETURN);
        }
        for (GradingScript g : compilable.getGradingScripts()) {
            sb.append(g.accept(this, lexenv, visitType)).append(CARRIAGE_RETURN);
        }
        return sb.toString();
    }

    /**
	 * Compilation schema <br/>
	 * If the {@link AssertScript} refers to a file :
	 * <pre>/bin/sh 'path/to/assertScript'</pre>
	 * If the {@link AssertScript} stores its own source code :
	 * <pre> # Source code stored in the AssertScript </pre>
	 *
	 */
    public Object compile(AssertScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        if (compilable.refersToFile()) {
            return "/bin/sh " + convertFW4EXDir(compilable.getFw4exDir()) + "'" + compilable.getScriptName() + "'";
        } else {
            return compilable.getSourceCode();
        }
    }

    /**
     * Compilation schema <br/>
     * If we're compiling the student version :
     * <pre>
     * fw4ex_run_student_command 'path/to/command' {components} {input}
     * </pre>
     * If it's the teacher version :
     * <pre>
     * fw4ex_run_teacher_command 'path/to/command' {components} {input} \
     * {gradingScripts}
     * </pre>
     */
    public Object compile(CompareCommand compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        if (FW4EXVisitType.REGULAR.equals(visitType)) {
            sb.append("fw4ex_run_student_command ").append(convertFW4EXDir(compilable.getFw4exDir())).append("'").append(compilable.getCmdName()).append("' ");
        } else if (FW4EXVisitType.TEACHER.equals(visitType)) {
            sb.append("fw4ex_run_teacher_command ");
            if (compilable.hasTeacherValue()) {
                sb.append(convertFW4EXDir(compilable.getTeacherFw4exDir())).append("'").append(compilable.getTeacherCmdName()).append("' ");
            } else {
                sb.append(convertFW4EXDir(compilable.getFw4exDir())).append("'").append(compilable.getCmdName()).append("' ");
            }
        }
        if (compilable.hasComponents()) {
            for (AbstractComponent c : compilable.getComponents()) {
                sb.append(c.accept(this, lexenv, visitType)).append(" ");
            }
        }
        if (compilable.hasInput()) {
            sb.append(" ").append(compilable.getInput().accept(this, lexenv, visitType));
        }
        if (FW4EXVisitType.TEACHER.equals(visitType)) {
            sb.append(CARRIAGE_RETURN);
            for (GradingScript gs : compilable.getGradingScripts()) {
                sb.append(gs.accept(this, lexenv, visitType)).append(CARRIAGE_RETURN);
            }
        }
        if (FW4EXVisitType.REGULAR.equals(visitType)) {
            sb.append(CARRIAGE_RETURN);
            sb.append(compilable.accept(this, lexenv, FW4EXVisitType.TEACHER));
        }
        return sb.toString();
    }

    /**
	 * Compilation schema <br/>
	 * If the file refers to a loop :
	 * <pre>"$loopName"</pre>
	 * else :
	 * <pre>'path/to/file'</pre>
	 */
    public Object compile(File compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        if (compilable.isRef()) {
            LexEnvLoopNode loopNode = (LexEnvLoopNode) lexenv.lookUp(compilable.getRef());
            sb.append("\"$").append(loopNode.getLoopName()).append("\"");
        } else {
            String dir = null;
            String file = null;
            if (FW4EXVisitType.REGULAR.equals(visitType)) {
                dir = convertFW4EXDir(compilable.getFw4exDir());
                file = compilable.getFileName();
            } else if (FW4EXVisitType.TEACHER.equals(visitType)) {
                if (compilable.hasTeacherValue()) {
                    dir = convertFW4EXDir(compilable.getTeacherFw4exDir());
                    file = compilable.getTeacherFileName();
                } else {
                    dir = convertFW4EXDir(compilable.getFw4exDir());
                    file = compilable.getFileName();
                }
            }
            sb.append(dir).append("'").append(file).append("'");
        }
        return sb.toString();
    }

    /**
	 * Compilation schema <br/>
	 * If the file refers to a loop :
	 * <pre>$(cat "$loopName")</pre>
	 * else :
	 * <pre>$(cat 'path/to/file')</pre>
	 */
    public Object compile(FileContent compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("$(cat ");
        if (compilable.isRef()) {
            LexEnvLoopNode loopNode = (LexEnvLoopNode) lexenv.lookUp(compilable.getRef());
            sb.append("\"$").append(loopNode.getLoopName()).append("\"");
        } else {
            String dir = null;
            String file = null;
            if (FW4EXVisitType.REGULAR.equals(visitType)) {
                dir = convertFW4EXDir(compilable.getFw4exDir());
                file = compilable.getFileName();
            } else if (FW4EXVisitType.TEACHER.equals(visitType)) {
                if (compilable.hasTeacherValue()) {
                    dir = convertFW4EXDir(compilable.getTeacherFw4exDir());
                    file = compilable.getTeacherFileName();
                } else {
                    dir = convertFW4EXDir(compilable.getFw4exDir());
                    file = compilable.getFileName();
                }
            }
            sb.append(dir).append("'").append(file).append("'");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
	 * Compilation schema <br/>
	 * If the input refers to a loop :
	 * <pre>< "$loopName"</pre>
	 * else :
	 * <pre>< 'path/to/file'</pre>
	 */
    public Object compile(Input compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder(" < ");
        if (compilable.isRef()) {
            LexEnvLoopNode loopLexenv = (LexEnvLoopNode) lexenv.lookUp(compilable.getNameRef());
            sb.append("\"$").append(loopLexenv.getLoopName()).append("\"");
        } else {
            String dir = null;
            String file = null;
            if (FW4EXVisitType.REGULAR.equals(visitType)) {
                dir = convertFW4EXDir(compilable.getFw4exDir());
                file = compilable.getInputName();
            } else if (FW4EXVisitType.TEACHER.equals(visitType)) {
                if (compilable.hasTeacherValue()) {
                    dir = convertFW4EXDir(compilable.getTeacherFw4exDir());
                    file = compilable.getTeacherInputName();
                } else {
                    dir = convertFW4EXDir(compilable.getFw4exDir());
                    file = compilable.getInputName();
                }
            }
            sb.append(dir).append("'").append(file).append("'");
        }
        return sb.toString();
    }

    /**
	 * Compilation schema <br/>
	 * If the option refers to a loop :
	 * <pre>$loopName</pre>
	 * else :
	 * <pre>optionValue</pre>
	 */
    public Object compile(Option compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        if (compilable.isRef()) {
            LexEnvLoopNode loopLexenv = (LexEnvLoopNode) lexenv.lookUp(compilable.getRef());
            sb.append("$").append(loopLexenv.getLoopName());
        } else {
            if (FW4EXVisitType.REGULAR.equals(visitType)) {
                sb.append(compilable.getValue());
            } else if (FW4EXVisitType.TEACHER.equals(visitType)) {
                if (compilable.hasTeacherValue()) {
                    sb.append(compilable.getTeacherValue());
                } else {
                    sb.append(compilable.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Compiles the given {@link FileLoop} and adds it to the given
     * <code>lexenv</code> (using a {@link LexEnvLoopNode}) before compiling
     * the loop nested entity. <br/>
	 * Compilation schema :<br/>
	 * If the loop uses a regexp :
	 * <pre>for loopName in $(ls path/to/fw4exdir/ | grep -E 'regexp')</pre>
	 * else :
	 * <pre>for loopName in 'item1' 'item2' ... 'itemN'</pre>
	 * Then regexp or not share the same compilation schema :
	 * <pre>do
	 *  (
	 *    #we use absolute paths so we need to recreate the absolute
	 *    loopName=path/to/fw4exdir/"$loopName"
	 *    trap 'echo "&lt;/section&gt;"' 0
	 *    echo "&lt;section&gt;"
	 *    #nested entity compilation
	 *  )
	 *done</pre>
	 */
    public Object compile(FileLoop compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        String loopId = compilable.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("for ").append(loopId).append(" in ");
        if (compilable.isRegexp()) {
            sb.append("$(ls ").append(convertFW4EXDir(compilable.getFw4exDir())).append(" | grep -E '").append(compilable.getRegexp()).append("')");
        } else {
            for (String item : compilable.getItems()) {
                sb.append("'").append(item).append("' ");
            }
        }
        sb.append(CARRIAGE_RETURN).append("do").append(CARRIAGE_RETURN).append("(").append(CARRIAGE_RETURN).append(loopId).append("=").append(convertFW4EXDir(compilable.getFw4exDir())).append("\"$").append(loopId).append("\"").append(CARRIAGE_RETURN).append("trap 'echo \"</section>\"' 0").append(CARRIAGE_RETURN).append("echo \"<section>\"").append(CARRIAGE_RETURN).append(compilable.getNestedEntity().accept(this, lexenv.extend(new LexEnvLoopNode(loopId, compilable.getName(), compilable)), visitType)).append(CARRIAGE_RETURN).append(")").append(CARRIAGE_RETURN).append("done");
        return sb.toString();
    }

    /**
	 * Compiles the given {@link StringLoop} and adds it to the given
	 * <code>lexenv</code> (using a {@link LexEnvLoopNode}) before compiling
	 * the loop nested entity. <br/>
	 * Compilation schema :<br/>
	 * <pre>for loopName in 'item1' 'item2' ... 'itemN'
	 *  do
     *    (
     *      trap 'echo "&lt;/section&gt;"' 0
     *      echo "&lt;section&gt;"
     *      #nested entity compilation
     *    )
     *  done</pre>
	 */
    public Object compile(StringLoop compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        String loopId = compilable.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("for ").append(loopId).append(" in ");
        for (String item : compilable.getItems()) {
            sb.append("'").append(item).append("' ");
        }
        sb.append(CARRIAGE_RETURN).append("do").append(CARRIAGE_RETURN).append("(").append(CARRIAGE_RETURN).append("trap 'echo \"</section>\"' 0").append(CARRIAGE_RETURN).append("echo \"<section>\"").append(CARRIAGE_RETURN).append(compilable.getNestedEntity().accept(this, lexenv.extend(new LexEnvLoopNode(loopId, compilable.getName(), compilable)), visitType)).append(CARRIAGE_RETURN).append(")").append(CARRIAGE_RETURN).append("done");
        return sb.toString();
    }

    /**
     * Compilation schema : <br/>
     * If the {@link GradingScript} refers to a file :
     * <pre>/bin/sh 'path/to/gradingScript'</pre>
     * If the {@link GradingScript} stores its own source code :
     * <pre> # Source code stored in the GradingScript </pre>
     */
    public Object compile(GradingScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        if (compilable.refersToFile()) {
            return "/bin/sh " + convertFW4EXDir(compilable.getFw4exDir()) + "'" + compilable.getScriptName() + "'";
        } else {
            return compilable.getSourceCode();
        }
    }
}
