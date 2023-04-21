package deesel.parser.com.nodes;

import deesel.parser.ASTMethodCallExpression;
import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.exceptions.CannotAddChildOfThisTypeException;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.exceptions.AmbiguousMethodCallException;
import deesel.parser.exceptions.GeneralParserFailureException;
import deesel.parser.exceptions.NoSuchMethodParseException;
import deesel.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a member call language construct. for the rules applied
 * please see: <a href="http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#289905">Java
 * Language Specification Entry for Method Calls</a>
 *
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class MethodCall extends ConstructorCall implements PrimaryExpressionComponent {

    private static Logger log = Logger.getLogger(MethodCall.class);

    private String name;

    private DeeselMethod methodUsed;

    public MethodCall(ASTNode node, DeeselClass owner, String name) {
        super(node, owner);
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeeselMethod getMethodUsed() {
        return methodUsed;
    }

    public boolean isUsesExtension() {
        return usesExtension;
    }

    public void setUsesExtension(boolean usesExtension) {
        this.usesExtension = usesExtension;
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public void addChild(Expression node) throws CannotAddChildOfThisTypeException {
        parameters.add(node);
    }

    private DeeselMethod findMostSpecific(DeeselMethod reference, ArrayList methods) throws NoSuchMethodException, AmbiguousMethodCallException {
        if (log.isDebugEnabled()) log.debug("Potential matches " + methods.size());
        for (int i = 0; i < methods.size(); ) {
            DeeselMethod methodDefinition = (DeeselMethod) methods.get(i);
            log.debug("ref " + reference.getDeclaringClass() + " method " + methodDefinition.getDeclaringClass());
            if (reference != methodDefinition) {
                if (reference.getDeclaringClass().isInstance(methodDefinition.getDeclaringClass()) && canCall(reference, methodDefinition)) {
                    if (log.isDebugEnabled()) log.debug(reference.getDeclaringClass() + " is an instance of " + methodDefinition.getDeclaringClass());
                    methods.remove(i);
                    if (i == methods.size()) {
                        break;
                    }
                } else if (canCall(methodDefinition, reference) && !canCall(reference, methodDefinition) && methodDefinition.getDeclaringClass().isInstance(reference.getDeclaringClass())) {
                    if (log.isDebugEnabled()) log.debug(methodDefinition.getDeclaringClass() + " is an instance of " + reference.getDeclaringClass());
                    return findMostSpecific(methodDefinition, methods);
                } else {
                    if (reference.hasImplementation() && !methodDefinition.hasImplementation()) {
                        if (log.isDebugEnabled()) log.debug(methodDefinition.getDeclaringClass() + " is abstract but " + reference.getDeclaringClass() + " isn't.");
                        methods.remove(i);
                        if (i == methods.size()) {
                            break;
                        }
                    } else if (!reference.hasImplementation() && methodDefinition.hasImplementation()) {
                        if (log.isDebugEnabled()) log.debug(methodDefinition.getDeclaringClass() + " is not abstract but " + reference.getDeclaringClass() + " is.");
                        return findMostSpecific(methodDefinition, methods);
                    } else {
                        log.fatal("Both are abstract.");
                        methods.remove(i);
                    }
                    i++;
                }
            } else {
                i++;
            }
        }
        if (methods.size() == 1) {
            return reference;
        } else if (methods.size() == 0) {
            throw new NoSuchMethodException(name);
        } else {
            if (areAllMethodsAbstract(methods)) {
                return (DeeselMethod) methods.get(0);
            }
            throw new AmbiguousMethodCallException((ASTMethodCallExpression) getASTNode(), name, methods);
        }
    }

    private boolean areAllMethodsAbstract(ArrayList methods) {
        for (int i = 0; i < methods.size(); i++) {
            DeeselMethod methodDefinition = (DeeselMethod) methods.get(i);
            if (methodDefinition.hasImplementation()) {
                return false;
            }
        }
        return true;
    }

    public DeeselClass[] getExceptionTypes() {
        return methodUsed.getExceptionTypes();
    }

    public String getName() {
        return methodUsed.getName();
    }

    public DeeselClass getType() {
        return getReturnType();
    }

    public DeeselClass getReturnType() {
        if (isResolved()) {
            return methodUsed.getReturnType();
        } else {
            throw new GeneralParserFailureException("You cannot know the return type of the method before the method has been resolved.");
        }
    }

    public void resolve() {
        if (owner instanceof AdaptiveType) {
            ((AdaptiveType) owner).setConcreteRecursively(true);
        }
        identifyMethodCall(owner);
        fixAdapativeTypes(methodUsed);
        setResolved(true);
    }

    public void identifyMethodCall(DeeselClass clazz) {
        DeeselClass curClass = clazz;
        ArrayList methods = new ArrayList();
        if (log.isDebugEnabled()) {
            log.debug("Looking for member to match " + name + AbstractMethod.getParamsAsString(getParameterTypes()));
        }
        buildMethodList(parameters, curClass, methods, false);
        if (methods.size() == 0) {
            buildMethodList(parameters, curClass, methods, true);
        }
        List extensionParameters = new ArrayList();
        extensionParameters.add(owner);
        extensionParameters.addAll(parameters);
        if (methods.size() == 0) {
            if (log.isDebugEnabled()) {
                log.debug("Looking for extension member to match " + name + AbstractMethod.getParamsAsString(getParameterTypes()));
            }
            DeeselClass[] extensionClasses = getParentClass().getExtensionClassesForClass(owner);
            for (int i = 0; i < extensionClasses.length; i++) {
                DeeselClass extensionClass = extensionClasses[i];
                if (log.isDebugEnabled()) log.debug("Trying extension class" + extensionClass.getFullName());
                buildMethodList(extensionParameters, extensionClass, methods, false);
            }
            if (methods.size() != 0) {
                usesExtension = true;
            }
        }
        if (methods.size() == 0) {
            List newParameters = new ArrayList();
            newParameters.add(owner);
            newParameters.addAll(parameters);
            if (log.isDebugEnabled()) {
                log.debug("Looking for extension member to match " + name + AbstractMethod.getParamsAsString(getParameterTypes()));
            }
            DeeselClass[] extensionClasses = getParentClass().getExtensionClassesForClass(owner);
            for (int i = 0; i < extensionClasses.length; i++) {
                DeeselClass extensionClass = extensionClasses[i];
                if (log.isDebugEnabled()) log.debug("Trying extension class" + extensionClass.getFullName());
                buildMethodList(newParameters, extensionClass, methods, true);
            }
            if (methods.size() != 0) {
                usesExtension = true;
            }
        }
        if (methods.size() == 0) {
            throw new NoSuchMethodParseException(new NoSuchMethodException(name), getASTNode(), owner, name, getParameterTypes());
        }
        try {
            methodUsed = findMostSpecific(methods);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodParseException(e, getASTNode(), owner, name, getParameterTypes());
        }
    }

    private void buildMethodList(List parameters, DeeselClass curClass, ArrayList methods, boolean canUseAutoCast) {
        assert parameters != null && curClass != null && methods != null;
        DeeselClass[] paramTypes = new DeeselClass[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            COMNode expression = (COMNode) parameters.get(i);
            paramTypes[i] = expression.getType();
        }
        while (curClass != null) {
            DeeselMethod[] methodDefinitions;
            assert !curClass.isAdaptable();
            methodDefinitions = curClass.getDeclaredMethods();
            if (log.isDebugEnabled()) log.debug("Looking for methods in " + curClass.getFullName() + " found " + methodDefinitions.length);
            for (int i = 0; i < methodDefinitions.length; i++) {
                DeeselMethod methodDefinition = methodDefinitions[i];
                if (log.isDebugEnabled()) log.debug("Comparing member " + methodDefinition.toString() + " from " + curClass.getFullName());
                if (methodDefinition.getName().equals(name)) {
                    if (canAdaptTo(paramTypes, methodDefinition.getParameterTypes()) || (canUseAutoCast && canAutoCast(paramTypes, methodDefinition.getParameterTypes()))) {
                        methods.add(methodDefinition);
                    }
                }
            }
            DeeselClass[] interfaces = curClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                DeeselClass anInterface = interfaces[i];
                buildMethodList(parameters, anInterface, methods, canUseAutoCast);
            }
            curClass = curClass.getSuperClass();
        }
    }

    private DeeselMethod findMostSpecific(ArrayList methods) throws NoSuchMethodException {
        return findMostSpecific((DeeselMethod) methods.get(0), methods);
    }
}
