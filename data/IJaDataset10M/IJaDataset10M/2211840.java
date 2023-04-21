package deesel.parser.com.nodes;

import deesel.parser.com.COMNode;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.exceptions.GeneralParserFailureException;
import deesel.parser.types.ReflectionUtil;
import deesel.util.logging.Logger;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class CompiledConstructor extends AbstractMethod implements Constructor {

    private static Logger log = Logger.getLogger(CompiledConstructor.class);

    protected Member member;

    /**
     * Builds a CompiledMethod from the java.lang.reflect.Constructor supplied.
     *
     * @param constructor
     */
    public CompiledConstructor(java.lang.reflect.Constructor constructor) {
        super(null);
        this.member = constructor;
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public DeeselClass getType() {
        return ClassDefFactory.VOID;
    }

    public COMNode[] getAllChildren() {
        return new COMNode[0];
    }

    /**
     * @see Constructor#canBeCalledWith(DeeselClass[])
     */
    public boolean canBeCalledWith(DeeselClass[] types) {
        return false;
    }

    /**
     * @see DeeselMethod#getDeclaringClass()
     * @see Constructor#getDeclaringClass()
     */
    public DeeselClass getDeclaringClass() {
        return new CompiledClass(member.getDeclaringClass());
    }

    /**
     * @see DeeselMethod#getExceptionTypes()
     * @see Constructor#getExceptionTypes()
     */
    public DeeselClass[] getExceptionTypes() {
        Class[] exceptionTypes;
        exceptionTypes = ((java.lang.reflect.Constructor) member).getExceptionTypes();
        return ReflectionUtil.getClassArrayAsClassDefinitionArray(exceptionTypes);
    }

    /**
     * @see DeeselMethod#getParameterTypes()
     * @see Constructor#getParameterTypes()
     */
    public DeeselClass[] getParameterTypes() {
        Class[] parameterTypes;
        parameterTypes = ((java.lang.reflect.Constructor) member).getParameterTypes();
        return ReflectionUtil.getClassArrayAsClassDefinitionArray(parameterTypes);
    }

    /**
     * @see DeeselMethod#getModifiers()
     * @see Constructor#getModifiers()
     */
    public int getModifiers() {
        return member.getModifiers();
    }

    /**
     * @see DeeselMethod#getName()
     */
    public String getName() {
        return member.getName();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(member.getModifiers());
    }

    public boolean isPrivate() {
        return false;
    }

    public boolean isNative() {
        return false;
    }

    public boolean isProtected() {
        return false;
    }

    public boolean isPublic() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }

    public boolean isFinal() {
        return false;
    }

    public boolean isStrict() {
        return false;
    }

    public boolean isSynchronized() {
        return false;
    }

    public Parameter[] getParameters() {
        throw new GeneralParserFailureException("Cannot request parameters from a compiled method or constructor.");
    }

    /**
     * @see DeeselMethod#getReturnType()
     */
    public DeeselClass getReturnType() {
        throw new GeneralParserFailureException("Constructors don't have return types!");
    }

    public String toString() {
        return getDeclaringClass().getFullName() + AbstractMethod.getParamsAsString(getParameterTypes());
    }
}
