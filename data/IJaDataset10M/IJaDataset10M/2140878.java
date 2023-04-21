package fw4ex.markingscriptcompiler.compiler;

import java.util.Vector;
import fw4ex.markingscriptcompiler.compiler.lexenv.ILexEnv;
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
 * Compiles a {@link MarkingScript} to an XML string representation.<br/>
 * Since the {@link MarkingScript} is compiled into an XML string, the
 * <code>Object</code> returned by all the <code>compile</code> methods
 * can be safely cast to <code>String</code>. <br/>
 * The <code>visitType</code> argument is <u>never</u> used by the
 * {@code XmlCompiler}. Indeed we don't have to make a difference between
 * teacher and student, we just write all the information we have for each
 * compiled element. <code>null</code> is used everywhere for this argument.
 */
public class XmlCompiler extends AbstractCompiler {

    public String convertFW4EXDir(FW4EXDir dir) throws CompileException {
        switch(dir) {
            case PATH:
                return "PATH";
            case STUDENT:
                return "student";
            case TEACHER:
                return "teacher";
            case UNIX:
                return "/";
            default:
                throw new CompileException("Unknown FW4EXDir.");
        }
    }

    /**
	 * Compiles a {@link MarkingScript} <pre>
	 * &lt;script language="fw4exsh"&gt;
	 *   #nested entity compilation
	 * &lt;/script&gt;
	 * </pre>
	 */
    public Object compile(MarkingScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<script language=\"fw4exsh\">\n");
        sb.append(compilable.getRootEntity().accept(this, lexenv, null)).append("\n</script>");
        return sb.toString();
    }

    /**
	 * Compiles a {@link ChDir} <br/>
	 * If it refers to a loop: <pre>
	 * &lt;chDir nameref="..."&gt;
	 *   &lt;body&gt;
     *     #nested entity compilation
     *   &lt;/body&gt;
	 * &lt;/chDir&gt;
	 * </pre>
	 * If it is raw: <pre>
	 * &lt;chDir fw4exdir="..." dirname="..."&gt;
     *   &lt;body&gt;
     *     #nested entity
     *   &lt;/body&gt;
     * &lt;/chDir&gt;
     * </pre>
	 */
    public Object compile(ChDir compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<chDir ");
        if (compilable.isRef()) {
            sb.append("nameref=\"").append(compilable.getNameRef()).append("\"");
        } else {
            sb.append("fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" dirname=\"").append(compilable.getDirName()).append("\"");
        }
        sb.append(">\n").append("<body>\n").append(compilable.getNestedEntity().accept(this, lexenv, null)).append("\n</body>\n</chDir>");
        return sb.toString();
    }

    /**
     * Compiles an {@link AssertCommand}. <br/>
     * Elements between [ ] are compiled only if the target element is not
     * empty.<br/>
     * For instance here we don't add <code>&lt;component&gt;</code> if the
     * command components list is empty, whereas we always add
     * <code>&lt;assertionScripts&gt;</code> even if the command assert scripts
     * list is empty.<br/>
     * This is meant to respect the RNC grammar of {@link MarkingScript}.
     *  <pre>
     * &lt;assertCommand fw4exdir="..." name="..."&gt;
     *   [&lt;components&gt;#components compilation&lt;/components&gt;]
     *   [&lt;input&gt;#input compilation&lt;/input&gt;]
     *   &lt;assertionScripts&gt;
     *     [#assert scripts compilation. Empty if no assert scripts]
     *   &lt;/assertionScripts&gt;
     *   &lt;gradingScripts&gt;
     *     [#grading scripts compilation. Empty if no grading scripts]
     *   &lt;/gradingScripts&gt
     * &lt;/assertCommand&gt;
     * </pre>
     */
    public Object compile(AssertCommand compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        sb.append("<assertCommand fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getCmdName()).append("\">\n");
        Vector<AbstractComponent> components = compilable.getComponents();
        if (components.isEmpty() == false) {
            sb.append("<components>\n");
            for (int i = 0; i < components.size(); i++) {
                sb.append(components.get(i).accept(this, lexenv, null)).append("\n");
            }
            sb.append("</components>\n");
        }
        if (compilable.hasInput()) {
            sb.append(compilable.getInput().accept(this, lexenv, null)).append("\n");
        }
        sb.append("<assertionScripts>\n");
        Vector<AssertScript> assertScripts = compilable.getAssertScripts();
        for (int i = 0; i < assertScripts.size(); i++) {
            sb.append(assertScripts.get(i).accept(this, lexenv, null)).append("\n");
        }
        sb.append("</assertionScripts>\n");
        sb.append("<gradingScripts>\n");
        Vector<GradingScript> gradingScripts = compilable.getGradingScripts();
        for (int i = 0; i < gradingScripts.size(); i++) {
            sb.append(gradingScripts.get(i).accept(this, lexenv, null)).append("\n");
        }
        sb.append("</gradingScripts>\n");
        sb.append("</assertCommand>");
        return sb.toString();
    }

    /**
	 * Compiles an {@link AssertScript}. <br/>
	 * If the {@link AssertScript} refers to a file : <pre>
	 * &lt;script fw4exdir="..." name="..." /&gt;
	 * </pre>
	 * If the {@link AssertScript} stores its own source code : <pre>
	 * &lt;script&gt;
	 *   &lt;src&gt; # AssertScript source code &lt;/src&gt;
	 * &lt;/script&gt;
	 * </pre>
	 */
    public Object compile(AssertScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        if (compilable.refersToFile()) {
            sb.append("<script fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getScriptName()).append("\" />");
        } else {
            sb.append("<script><src>").append(compilable.getSourceCode()).append("</src></script>");
        }
        return sb.toString();
    }

    /**
	 * Compile a {@link CompareCommand}.<br/>
	 * Elements between [ ] are compiled only if the target element is not
     * empty.<br/>
     * For instance here we don't add <code>&lt;component&gt;</code> if the
     * command components list is empty, whereas we always add
     * <code>&lt;gradingScripts&gt;</code> even if the command grading scripts
     * list is empty.<br/>
     * This is meant to respect the RNC grammar of {@link MarkingScript}.
     * <pre>
	 * &lt;compareCommand fw4exdir="..." name="..." [teacherDir="..." teacherName="..."]&gt;
     *   [&lt;components&gt;#components compilation&lt;/components&gt;]
     *   [&lt;input&gt;#input compilation&lt;/input&gt;]
     *   &lt;gradingScripts&gt;
     *     [#grading scripts compilation. Empty if no grading scripts]
     *   &lt;/gradingScripts&gt
     * &lt;/compareCommand&gt;
     * </pre>
	 */
    public Object compile(CompareCommand compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        sb.append("<compareCommand fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getCmdName()).append("\"");
        if (compilable.hasTeacherValue()) {
            sb.append(" teacherFw4exdir=\"").append(convertFW4EXDir(compilable.getTeacherFw4exDir())).append("\" teacherName=\"").append(compilable.getTeacherCmdName()).append("\"");
        }
        sb.append(">\n");
        Vector<AbstractComponent> components = compilable.getComponents();
        if (components.isEmpty() == false) {
            sb.append("<components>\n");
            for (int i = 0; i < components.size(); i++) {
                sb.append(components.get(i).accept(this, lexenv, null)).append("\n");
            }
            sb.append("</components>\n");
        }
        if (compilable.getInput() != null) {
            sb.append(compilable.getInput().accept(this, lexenv, null) + "\n");
        }
        sb.append("<gradingScripts>\n");
        Vector<GradingScript> gradingScripts = compilable.getGradingScripts();
        for (int i = 0; i < gradingScripts.size(); i++) {
            sb.append(gradingScripts.get(i).accept(this, lexenv, null)).append("\n");
        }
        sb.append("</gradingScripts>\n");
        sb.append("</compareCommand>");
        return sb.toString();
    }

    /**
	 * Compiles a {@link File}.<br/>
	 * If it refers to a loop: <pre>
     * &lt;file nameref="..." /&gt;
     * </pre>
     * If it is raw without teacher values: <pre>
     * &lt;file fw4exdir="..." name="..." /&gt;
     * </pre>
     * If it is raw with teacher values: <pre>
     * &lt;file fw4exdir="..." name="..." teacherFw4exdir="..." teacherName="..." /&gt;
     * </pre>
	 */
    public Object compile(File compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<file ");
        if (compilable.isRef()) {
            sb.append("nameref=\"").append(compilable.getRef());
        } else {
            sb.append("fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getFileName());
            if (compilable.hasTeacherValue()) {
                sb.append("\" teacherFw4exdir=\"").append(convertFW4EXDir(compilable.getTeacherFw4exDir())).append("\" teacherName=\"").append(compilable.getTeacherFileName());
            }
        }
        return sb.append("\"/>").toString();
    }

    /**
     * Compiles a {@link FileContent}.<br/>
     * If it refers to a loop: <pre>
     * &lt;fileContent nameref="..." /&gt;
     * </pre>
     * If it is raw without teacher values: <pre>
     * &lt;fileContent fw4exdir="..." name="..." /&gt;
     * </pre>
     * If it is raw with teacher values: <pre>
     * &lt;fileContent fw4exdir="..." name="..." teacherFw4exdir="..." teacherName="..." /&gt;
     * </pre>
     */
    public Object compile(FileContent compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<fileContent ");
        if (compilable.getRef() != null) {
            sb.append("nameref=\"").append(compilable.getRef());
        } else {
            sb.append("fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getFileName());
            if (compilable.hasTeacherValue()) {
                sb.append("\" teacherFw4exdir=\"").append(convertFW4EXDir(compilable.getTeacherFw4exDir())).append("\" teacherName=\"").append(compilable.getTeacherFileName());
            }
        }
        return sb.append("\"/>").toString();
    }

    /**
     * Compiles an {@link Input}.<br/>
     * If it refers to a loop: <pre>
     * &lt;input nameref="..." /&gt;
     * </pre>
     * If it is raw without teacher values: <pre>
     * &lt;input fw4exdir="..." name="..." /&gt;
     * </pre>
     * If it is raw with teacher values: <pre>
     * &lt;input fw4exdir="..." name="..." teacherFw4exdir="..." teacherName="..." /&gt;
     * </pre>
     */
    public Object compile(Input compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<input ");
        if (compilable.getNameRef() != null) {
            sb.append("nameref=\"").append(compilable.getNameRef());
        } else {
            sb.append("fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getInputName());
            if (compilable.hasTeacherValue()) {
                sb.append("\" teacherFw4exdir=\"").append(convertFW4EXDir(compilable.getTeacherFw4exDir())).append("\" teacherName=\"").append(compilable.getTeacherInputName());
            }
        }
        return sb.append("\"/>").toString();
    }

    /**
     * Compiles an {@link Option}.<br/>
     * If it refers to a loop: <pre>
     * &lt;option nameref="..." /&gt;
     * </pre>
     * If it is raw without teacher values: <pre>
     * &lt;option value"..." /&gt;
     * </pre>
     * If it is raw with teacher values: <pre>
     * &lt;option value"..." teacherValue="..." /&gt;
     * </pre>
     */
    public Object compile(Option compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder("<option ");
        if (compilable.isRef()) {
            sb.append("nameref=\"").append(compilable.getRef());
        } else {
            sb.append("value=\"").append(compilable.getValue());
            if (compilable.hasTeacherValue()) {
                sb.append("\" teacherValue=\"").append(compilable.getTeacherValue());
            }
        }
        return sb.append("\"/>").toString();
    }

    /**
	 * Compiles a {@link FileLoop}.<br/>
	 * If it uses a regular expression : <pre>
	 * &lt;loopOnFiles fw4exdir="..." name="..."&gt;
	 *   &lt;pathregexp&gt;...&lt;/pathregexp&gt;
	 *   &lt;body&gt;
	 *     #nested entity compilation
	 *   &lt;/body&gt;
	 * &lt;/loopOnFiles&gt;
	 * </pre>
	 * If it uses a collection of items: <pre>
	 * &lt;loopOnFiles fw4exdir="..." name="..."&gt;
     *   &lt;items&gt;
     *     &lt;item&gt;...&lt;/item&gt;...
     *   &lt;/items&gt;
     *   &lt;body&gt;
     *     #nested entity compilation
     *   &lt;/body&gt;
     * &lt;/loopOnFiles&gt;
     * </pre>
	 */
    public Object compile(FileLoop compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        sb.append("<loopOnFiles fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getName()).append("\">\n");
        if (compilable.isRegexp()) {
            sb.append("<pathregexp>").append(compilable.getRegexp()).append("</pathregexp>");
        } else {
            sb.append("<items>\n");
            Vector<String> items = compilable.getItems();
            for (int i = 0; i < items.size(); i++) {
                sb.append("<item>").append(items.get(i)).append("</item>\n");
            }
            sb.append("</items>");
        }
        sb.append("\n<body>\n").append(compilable.getNestedEntity().accept(this, lexenv, null)).append("\n</body>\n</loopOnFiles>");
        return sb.toString();
    }

    /**
	 * Compiles a {@link StringLoop} <pre>
     * &lt;loopOnStrings name="..."&gt;
     *   &lt;items&gt;
     *     &lt;item&gt;...&lt;/item&gt;...
     *   &lt;/items&gt;
     *   &lt;body&gt;
     *     #nested entity compilation
     *   &lt;/body&gt;
     * &lt;/loopOnStrings&gt;
     * </pre>
	 */
    public Object compile(StringLoop compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        sb.append("<loopOnStrings name=\"").append(compilable.getName()).append("\">\n<items>\n");
        Vector<String> items = compilable.getItems();
        for (int i = 0; i < items.size(); i++) {
            sb.append("<item>").append(items.get(i)).append("</item>\n");
        }
        sb.append("</items>\n<body>\n").append(compilable.getNestedEntity().accept(this, lexenv, null)).append("\n</body>\n</loopOnStrings>");
        return sb.toString();
    }

    /**
	 * Compiles a {@link GradingScript}. <br/>
	 * If it refers to a file :
	 * <pre>
	 * &lt;script fw4exdir="..." name="..." /&gt;
	 * </pre>
	 * If it stores its own source code ;
	 * <pre>
	 * &lt;script&gt;
	 *   &lt;src&gt;#script source code&lt;/src&gt;
	 * &lt;/script&gt;
	 * </pre>
	 */
    public Object compile(GradingScript compilable, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        StringBuilder sb = new StringBuilder();
        if (compilable.refersToFile()) {
            sb.append("<script fw4exdir=\"").append(convertFW4EXDir(compilable.getFw4exDir())).append("\" name=\"").append(compilable.getScriptName()).append("\" />");
        } else {
            sb.append("<script>\n<src>").append(compilable.getSourceCode()).append("\n</src>\n</script>");
        }
        return sb.toString();
    }
}
