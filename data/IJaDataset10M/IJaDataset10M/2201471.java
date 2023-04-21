package tm.javaLang.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.NodeList;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpMemberCall;
import tm.javaLang.ast.ExpEnsureClassInit;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyVoid;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;

public class CGRConstructorCall extends CGRInvocation {

    public CGRConstructorCall(Java_CTSymbolTable st) {
        super(st);
    }

    /** Create a constructor call
     * <P><strong>Precondition:</strong>
     * <ul><li><code>exp.get_list(0)</code> is the argument list,
     *     </li>
     *     <li><code>exp.get(1)</code> is the recipient.
     *     </li>
     *     <li><code>exp.get_type(2)</code> is the TyClass for the class.
     *     </li>
     *     <li><code>exp.id()</code> is the class as a scoped name, and
     *     </li>
     *     <li><code>exp.opid</code> is the name of the class as a scoped name suitable for display.
     *     </li>
     * </ul>
     * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li><code>exp.get()</code> is the constructor call.
     *     </li>
     * </ul>
     * @param exp See above.
     */
    public void apply(ExpressionPtr exp) {
        NodeList arguments = exp.get_list(0);
        ExpressionNode recipient = exp.get(1);
        TyClass tyClass = (TyClass) exp.get_type(2);
        Declaration classDecl = tyClass.getDeclaration();
        ExpressionNode constructorCall = makeConstructorCall(exp.id(), exp.opid.getTerminalId(), recipient, arguments, classDecl, tyClass);
        exp.set(constructorCall);
        exp.set(tyClass, 2);
    }

    /**
     * @param id The name of the class as a scoped name
     * @param constructorName The name of the class as a string
     * @param arguments The arguments to the constructor
     * @param classDecl The declaration of the class
     * @param tyClass The type for the class
     * @return
     */
    private ExpressionNode makeConstructorCall(ScopedName id, String constructorName, ExpressionNode recipient, NodeList arguments, Declaration classDecl, TyClass tyClass) {
        ExpressionNode constructorCall;
        Assert.error(classDecl != null, "Multiple entities found matching id " + id.getName());
        Assert.error(symbolTable.isAccessible(classDecl), classDecl.getName().getName() + " is inaccessible from " + symbolTable.getCurrentScope().toString());
        DeclarationSet constructors = ((SHType) classDecl.getDefinition()).lookup(id.getTerminalId(), Java_LFlags.CONSTRUCTOR_LF);
        if (constructors == null || constructors.isEmpty()) Assert.error("No constructor found matching id " + id.getName());
        DeclarationSet possibles = findClosestMethod(constructors, arguments);
        Assert.error(!possibles.isEmpty(), "No such constructor is accessable or applicable");
        Declaration constructor = possibles.getSingleMember();
        Assert.error(constructor != null, "Ambiguous match for constructors.");
        recipient = new ExpEnsureClassInit(tyClass, recipient);
        constructorCall = new OpMemberCall(TyVoid.get(), constructorName, ".", false, true, constructor.getRuntimeId(), false, recipient, new int[0], arguments);
        return constructorCall;
    }
}
