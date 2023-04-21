package fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline;

import org.w3c.dom.Element;
import fw4ex.markingscriptcompiler.compiler.FW4EXVisitType;
import fw4ex.markingscriptcompiler.compiler.ICompiler;
import fw4ex.markingscriptcompiler.compiler.lexenv.ILexEnv;
import fw4ex.markingscriptcompiler.exceptions.CompileException;
import fw4ex.markingscriptcompiler.exceptions.LexEnvException;
import fw4ex.markingscriptcompiler.exceptions.ParseException;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.loop.AbstractLoop;
import fw4ex.markingscriptcompiler.xmlparser.XmlParser;

/**
 * Represents a command line option.<br/>
 * It may use a raw value (with a possible teacher value) or it may refer to
 * an {@link AbstractLoop}.
 *
 */
public class Option extends AbstractComponent {

    /** The raw value of the option, when not referring to a loop. */
    protected String value;

    /** The teacher value of the option. */
    protected String teacherValue;

    /**
     * Construct a looped Option.
     * @param nameRef The name of the referred loop.
     */
    public Option(String nameRef) {
        super(nameRef);
    }

    /**
     * Construct a raw Option.
     * @param value The raw value of the option.
     * @param teacherValue The teacher value of the option.
     */
    public Option(String value, String teacherValue) {
        super(null);
        this.value = value;
        this.teacherValue = teacherValue;
    }

    @Override
    public boolean hasTeacherValue() {
        return teacherValue != null;
    }

    /** The raw value of the option, when not referring to a loop. */
    public String getValue() {
        return value;
    }

    /** The teacher value of the option. */
    public String getTeacherValue() {
        return teacherValue;
    }

    public Object accept(ICompiler compiler, ILexEnv lexenv, FW4EXVisitType visitType) throws CompileException, LexEnvException {
        return compiler.compile(this, lexenv, visitType);
    }

    public static Option parse(Element e, XmlParser parser) throws ParseException {
        if (!"".equals(e.getAttribute("nameref"))) {
            return parser.getFactory().newLoopedOption(e.getAttribute("nameref"));
        } else {
            return parser.getFactory().newRawOption(e.getAttribute("value"), e.getAttribute("teacherValue"));
        }
    }
}
