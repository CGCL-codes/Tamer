package edu.rice.cs.drjava.model.repl;

import koala.dynamicjava.interpreter.error.*;
import koala.dynamicjava.interpreter.*;
import koala.dynamicjava.interpreter.context.*;
import koala.dynamicjava.tree.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.lang.reflect.*;
import edu.rice.cs.drjava.model.repl.newjvm.ClassPathManager;
import edu.rice.cs.util.UnexpectedException;

/** This class is an extension to DynamicJavaAdapter that allows us to process expressions involving the "this"
 *  keyword correctly in the current debug interpreter context. This allows users to debug outer classes and their
 *  fields using the usual Java syntax of outerclass.this. This is done by holding on to the class name of "this" 
 *  and by translating references to outer instance classes to field accesses in the form "this.this$N.this$N-1...".
 *
 *  This class is loaded in the Interpreter JVM, not the Main JVM.
 *  (Do not use DrJava's config framework here.)
 *
 * @version $Id: JavaDebugInterpreter.java 3737 2006-04-05 02:53:19Z rcartwright $
 */
public class JavaDebugInterpreter extends DynamicJavaAdapter {

    /** This interpreter's name. */
    protected final String _name;

    /** The class name of the "this" object for the currently suspended thread. */
    protected String _thisClassName;

    /** The name of the package containing _this, if any. */
    protected String _thisPackageName;

    /** Extends IdentityVisitor to convert all instances of ThisExpressions in the tree to either QualifiedName or an
   *  ObjectFieldAccess
   */
    protected IdentityVisitor _translationVisitor;

    /** Creates a new debug interpreter.
   *  @param name the name of the interpreter
   *  @param className the class name of the current context of "this"
   */
    public JavaDebugInterpreter(String name, String className) {
        super(new ClassPathManager());
        _name = name;
        setClassName(className);
        _translationVisitor = makeTranslationVisitor();
    }

    /** Processes the tree before evaluating it. The translation visitor visits each node in the tree for the given
   *  statement or expression and converts the necessary nodes.
   *  @param node Tree to process
   */
    public Node processTree(Node node) {
        return node.acceptVisitor(_translationVisitor);
    }

    public GlobalContext makeGlobalContext(TreeInterpreter i) {
        return new GlobalContext(i) {

            public boolean exists(String name) {
                return (super.exists(name)) || (_getObjectFieldAccessForField(name, this) != null) || (_getStaticFieldAccessForField(name, this) != null) || (_getReferenceTypeForField(name, this) != null);
            }
        };
    }

    /**
   * Returns whether the given className corresponds to a class
   * that is anonymous or has an anonymous enclosing class.
   * @param className the className to check
   * @return whether the class is anonymous
   */
    private boolean hasAnonymous(String className) {
        StringTokenizer st = new StringTokenizer(className, "$");
        while (st.hasMoreElements()) {
            String currToken = st.nextToken();
            try {
                Integer.valueOf(currToken);
                return true;
            } catch (NumberFormatException nfe) {
            }
        }
        return false;
    }

    /**
   * Returns the fully qualified class name for "this".
   * It will append the package name onto the class name
   * if there is a package name.
   */
    private String _getFullyQualifiedClassNameForThis() {
        String cName = _thisClassName;
        if (!_thisPackageName.equals("")) {
            cName = _thisPackageName + "." + cName;
        }
        return cName;
    }

    private Class<?> _loadClassForThis(Context context) {
        try {
            return context.lookupClass(_getFullyQualifiedClassNameForThis());
        } catch (ClassNotFoundException e) {
            throw new UnexpectedException(e);
        }
    }

    /** Given a field, looks at enclosing classes until it finds one that contains the field. It returns the 
   *  ObjectFieldAccess that represents the field.
   *  @param field the name of the field
   *  @param context the context
   *  @return the ObjectFieldAccess that represents the field or null if it cannot find the field in any enclosing
   *  class.
   */
    protected ObjectFieldAccess _getObjectFieldAccessForField(String field, Context context) {
        AbstractTypeChecker tc = makeTypeChecker(context);
        int numDollars = _getNumDollars(_thisClassName);
        if (hasAnonymous(_thisClassName)) {
            Class<?> c = _loadClassForThis(context);
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().startsWith("this$")) {
                    String fieldName = fields[i].getName();
                    int lastIndex = fieldName.lastIndexOf("$");
                    numDollars = Integer.valueOf(fieldName.substring(lastIndex + 1, fieldName.length())).intValue() + 1;
                    break;
                }
            }
        }
        for (int i = 0; i <= numDollars; i++) {
            Expression expr = _buildObjectFieldAccess(i, numDollars);
            Expression newExpr = new ObjectFieldAccess(expr, field);
            try {
                tc.visit((ObjectFieldAccess) newExpr);
                return (ObjectFieldAccess) newExpr;
            } catch (ExecutionError e) {
                newExpr = new ObjectFieldAccess(expr, "val$" + field);
                try {
                    tc.visit((ObjectFieldAccess) newExpr);
                    return (ObjectFieldAccess) newExpr;
                } catch (ExecutionError e2) {
                }
            }
        }
        return null;
    }

    /**
   * Given a method, looks at enclosing classes until it finds
   * one that contains the method. It returns the ObjectMethodCall
   * that represents the method.
   * @param method the method
   * @param context the context
   * @return the ObjectMethodCall that represents the method or null
   * if it cannot find the method in any enclosing class.
   */
    protected ObjectMethodCall _getObjectMethodCallForFunction(MethodCall method, Context context) {
        AbstractTypeChecker tc = makeTypeChecker(context);
        int numDollars = _getNumDollars(_thisClassName);
        String methodName = method.getMethodName();
        List<Expression> args = method.getArguments();
        if (hasAnonymous(_thisClassName)) {
            Class<?> c = _loadClassForThis(context);
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().startsWith("this$")) {
                    String fieldName = fields[i].getName();
                    int lastIndex = fieldName.lastIndexOf("$");
                    numDollars = Integer.valueOf(fieldName.substring(lastIndex + 1, fieldName.length())).intValue() + 1;
                    break;
                }
            }
        }
        for (int i = 0; i <= numDollars; i++) {
            Expression expr = _buildObjectFieldAccess(i, numDollars);
            expr = new ObjectMethodCall(expr, methodName, args, null, 0, 0, 0, 0);
            try {
                tc.visit((ObjectMethodCall) expr);
                return (ObjectMethodCall) expr;
            } catch (ExecutionError e2) {
            }
        }
        return null;
    }

    /**
   * Given a field in a static context, looks at enclosing classes until it
   * finds one that contains the field. It returns the StaticFieldAccess
   * that represents the field.
   * @param field the name of the field
   * @param context the context
   * @return the StaticFieldAccess that represents the field or null
   * if it cannot find the field in any enclosing class.
   */
    protected StaticFieldAccess _getStaticFieldAccessForField(String field, Context context) {
        AbstractTypeChecker tc = makeTypeChecker(context);
        int numDollars = _getNumDollars(_thisClassName);
        String currClass = _getFullyQualifiedClassNameForThis();
        int index = currClass.length();
        for (int i = 0; i <= numDollars; i++) {
            currClass = currClass.substring(0, index);
            ReferenceType rt = new ReferenceType(currClass);
            StaticFieldAccess expr = new StaticFieldAccess(rt, field);
            try {
                tc.visit(expr);
                return expr;
            } catch (ExecutionError e2) {
                index = currClass.lastIndexOf("$");
            }
        }
        return null;
    }

    /**
   * Given a method in a static context, looks at enclosing classes until it
   * finds one that contains the method. It returns the StaticMethodCall
   * that represents the method.
   * @param method the method
   * @param context the context
   * @return the StaticMethodCall that represents the method or null
   * if it cannot find the method in any enclosing class.
   */
    protected StaticMethodCall _getStaticMethodCallForFunction(MethodCall method, Context context) {
        AbstractTypeChecker tc = makeTypeChecker(context);
        int numDollars = _getNumDollars(_thisClassName);
        String methodName = method.getMethodName();
        List<Expression> args = method.getArguments();
        String currClass = _getFullyQualifiedClassNameForThis();
        int index = currClass.length();
        for (int i = 0; i <= numDollars; i++) {
            currClass = currClass.substring(0, index);
            ReferenceType rt = new ReferenceType(currClass);
            StaticMethodCall expr = new StaticMethodCall(rt, methodName, args);
            try {
                tc.visit(expr);
                return expr;
            } catch (ExecutionError e2) {
                index = currClass.lastIndexOf("$");
            }
        }
        return null;
    }

    /**
   * Given the name of an not fully qualified outer class, return the fully qualified
   * ReferenceType that corresponds to that class. This is called when the user
   * references a static field of an outer class.
   * @param field the name of the not fully qualified outer class
   * @param context the context
   * @return the ReferenceType that represents the field(in this case,
   * really a class) or null if it cannot load the corresponding class in the
   * class loader.
   */
    protected ReferenceType _getReferenceTypeForField(String field, Context context) {
        AbstractTypeChecker tc = makeTypeChecker(context);
        int index = _indexOfWithinBoundaries(_getFullyQualifiedClassNameForThis(), field);
        if (index != -1) {
            int lastDollar = field.lastIndexOf("$");
            int lastDot = field.lastIndexOf(".");
            if (lastDollar != -1) {
                field = field.substring(lastDollar + 1, field.length());
            } else {
                if (lastDot != -1) {
                    field = field.substring(lastDot + 1, field.length());
                }
            }
            LinkedList<IdentifierToken> list = new LinkedList<IdentifierToken>();
            StringTokenizer st = new StringTokenizer(_getFullyQualifiedClassNameForThis(), "$.");
            String currString = st.nextToken();
            while (!currString.equals(field)) {
                list.add(new Identifier(currString));
                currString = st.nextToken();
            }
            list.add(new Identifier(field));
            ReferenceType rt = new ReferenceType(list);
            try {
                tc.visit(rt);
                return rt;
            } catch (ExecutionError e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
   * Sets the class name of "this", parsing out the package name.
   */
    protected void setClassName(String className) {
        int indexLastDot = className.lastIndexOf(".");
        if (indexLastDot == -1) {
            _thisPackageName = "";
        } else {
            _thisPackageName = className.substring(0, indexLastDot);
        }
        _thisClassName = className.substring(indexLastDot + 1, className.length());
    }

    /**
   * Helper method to convert a ThisExpression to a QualifiedName.
   * Allows us to redefine "this" in a debug interpreter.
   * @param node ThisExpression
   * @return corresponding QualifiedName
   */
    protected QualifiedName _convertThisToName(ThisExpression node) {
        List<IdentifierToken> ids = new LinkedList<IdentifierToken>();
        ids.add(new Identifier("this", node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn()));
        return new QualifiedName(ids, node.getFilename(), node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
    }

    /**
   * Helper method to convert a ThisExpression to a FieldAccess.
   * Allows us to access fields of outer classes in a debug interpreter.
   * @param node ThisExpression
   * @return corresponding FieldAccess
   */
    protected Expression _convertThisToObjectFieldAccess(ThisExpression node) {
        String className = node.getClassName();
        int numToWalk = verifyClassName(className);
        int numDollars = _getNumDollars(_thisClassName);
        if (numToWalk == -1) {
            throw new ExecutionError("malformed.expression", node);
        } else {
            return _buildObjectFieldAccess(numToWalk, numDollars);
        }
    }

    /**
   * Builds a ThisExpression that has no class name.
   * @return an unqualified ThisExpression
   */
    protected ThisExpression buildUnqualifiedThis() {
        LinkedList<IdentifierToken> ids = new LinkedList<IdentifierToken>();
        return new ThisExpression(ids, "", 0, 0, 0, 0);
    }

    /**
   * Helper method to build an ObjectFieldAccess for a ThisExpression
   * given the number of classes to walk and the number of dollars.
   * @param numToWalk number of outer classes to walk through
   * @param numDollars numer of dollars in _thisClassName
   * @return a QualifiedName is numtoWalk is zero or an ObjectFieldAccess
   */
    private Expression _buildObjectFieldAccess(int numToWalk, int numDollars) {
        if (numToWalk == 0) {
            return _convertThisToName(buildUnqualifiedThis());
        } else {
            return new ObjectFieldAccess(_buildObjectFieldAccess(numToWalk - 1, numDollars), "this$" + (numDollars - numToWalk));
        }
    }

    /**
   * Returns the index of subString within string if the substring is
   * either bounded by the ends of string or by $'s.
   * @param string the super string
   * @param subString the subString
   * @return the index of string that subString begins at or -1
   * if subString is not in string or is not validly bounded
   */
    private int _indexOfWithinBoundaries(String string, String subString) {
        int index = string.indexOf(subString);
        if (index == -1) {
            return index;
        } else {
            if (((string.length() == subString.length() + index) || (string.charAt(subString.length() + index) == '$')) && ((index == 0) || (string.charAt(index - 1) == '$') || (string.charAt(index - 1) == '.'))) {
                return index;
            } else {
                return -1;
            }
        }
    }

    /**
   * Returns the number of dollar characters in
   * a given String.
   * @param className the string to be examined
   * @return the number of dollars in the string
   */
    private int _getNumDollars(String className) {
        int numDollars = 0;
        int index = className.indexOf("$");
        while (index != -1) {
            numDollars++;
            index = className.indexOf("$", index + 1);
        }
        return numDollars;
    }

    /**
   * Checks if the className passed in is a valid className.
   * @param className the className of the ThisExpression
   * @return the number of outer classes to walk out to
   */
    protected int verifyClassName(String className) {
        boolean hasPackage = false;
        if (!_thisPackageName.equals("")) {
            int index = className.indexOf(_thisPackageName);
            if (index == 0) {
                hasPackage = true;
                index = _thisPackageName.length() + 1;
                if (index >= className.length()) {
                    return -1;
                }
                className = className.substring(index, className.length());
            }
        }
        className = className.replace('.', '$');
        int indexWithBoundaries = _indexOfWithinBoundaries(_thisClassName, className);
        if ((hasPackage && indexWithBoundaries != 0) || (indexWithBoundaries == -1)) {
            return -1;
        } else {
            return _getNumDollars(_thisClassName.substring(indexWithBoundaries + className.length()));
        }
    }

    /**
   * Converts the ThisExpression to a QualifiedName
   * if it has no class name or an ObjectFieldAccess
   * if it does.
   * @param node the expression to visit
   * @return the converted form of the node
   */
    protected Expression visitThis(ThisExpression node) {
        if (node.getClassName().equals("")) {
            return _convertThisToName(node);
        } else {
            return _convertThisToObjectFieldAccess(node);
        }
    }

    /**
   * Makes an anonymous IdentityVisitor that overrides
   * visit for a ThisExpresssion to convert it to
   * either a QualifiedName or an ObjectFieldAccess
   */
    public IdentityVisitor makeTranslationVisitor() {
        return new IdentityVisitor() {

            public Node visit(ThisExpression node) {
                Expression e = visitThis(node);
                if (e instanceof QualifiedName) {
                    return visit((QualifiedName) e);
                } else if (e instanceof ObjectFieldAccess) {
                    return visit((ObjectFieldAccess) e);
                } else {
                    throw new UnexpectedException(new IllegalArgumentException("Illegal type of Expression"));
                }
            }
        };
    }

    /**
   * Factory method to make a new NameChecker that tries to find the
   * right scope for QualifiedNames.
   * @param nameContext Context for the NameVisitor
   * @return the visitor
   */
    public NameVisitor makeNameVisitor(final Context nameContext) {
        return new NameVisitor(nameContext) {

            public Node visit(QualifiedName node) {
                try {
                    return super.visit(node);
                } catch (ExecutionError e) {
                    List<IdentifierToken> ids = node.getIdentifiers();
                    Iterator<IdentifierToken> iter = ids.iterator();
                    StringBuffer fieldBuf = new StringBuffer(iter.next().image());
                    while (iter.hasNext()) {
                        IdentifierToken t = iter.next();
                        fieldBuf.append('$').append(t.image());
                    }
                    String field = fieldBuf.toString();
                    if (nameContext.isDefined("this")) {
                        ObjectFieldAccess ofa = _getObjectFieldAccessForField(field, nameContext);
                        if (ofa != null) return ofa;
                    } else {
                        StaticFieldAccess sfa = _getStaticFieldAccessForField(field, nameContext);
                        if (sfa != null) return sfa; else {
                            ReferenceType rt = _getReferenceTypeForField(field, nameContext);
                            if (rt != null) return rt;
                        }
                    }
                    throw e;
                }
            }

            public Node visit(ObjectMethodCall node) {
                MethodCall method = (MethodCall) super.visit(node);
                if (method != null) {
                    if (method instanceof StaticMethodCall) {
                        return method;
                    } else if (nameContext.isDefined("this")) {
                        ObjectMethodCall omc = _getObjectMethodCallForFunction(method, nameContext);
                        if (omc != null) {
                            return omc;
                        } else {
                            return method;
                        }
                    } else {
                        StaticMethodCall smc = _getStaticMethodCallForFunction(method, nameContext);
                        if (smc != null) {
                            return smc;
                        } else {
                            return method;
                        }
                    }
                } else {
                    return null;
                }
            }
        };
    }

    /**
   * Factory method to make a new TypeChecker that treats "this" as a variable.
   * @param context the context
   * @return visitor the visitor
   */
    public AbstractTypeChecker makeTypeChecker(final Context context) {
        if (Float.valueOf(System.getProperty("java.specification.version")) < 1.5) {
            return new TypeChecker14(context) {

                /**
       * Visits a QualifiedName, returning our class if it is "this"
       * @param node the node to visit
       */
                public Class<?> visit(QualifiedName node) {
                    String var = node.getRepresentation();
                    if ("this".equals(var)) {
                        Class<?> c = _loadClassForThis(context);
                        node.setProperty(NodeProperties.TYPE, c);
                        node.setProperty(NodeProperties.MODIFIER, context.getModifier(node));
                        return c;
                    } else return super.visit(node);
                }
            };
        } else {
            return new TypeChecker15(context) {

                /**
       * Visits a QualifiedName, returning our class if it is "this"
       * @param node the node to visit
       */
                public Class<?> visit(QualifiedName node) {
                    String var = node.getRepresentation();
                    if ("this".equals(var)) {
                        Class<?> c = _loadClassForThis(context);
                        node.setProperty(NodeProperties.TYPE, c);
                        node.setProperty(NodeProperties.MODIFIER, context.getModifier(node));
                        return c;
                    } else return super.visit(node);
                }
            };
        }
    }
}
