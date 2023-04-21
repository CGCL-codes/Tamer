package net.sourceforge.refactor4pdt.core.renameclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.sourceforge.refactor4pdt.core.PhpRefactoringVisitor;
import net.sourceforge.refactor4pdt.log.Log;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.php.internal.core.ast.nodes.ClassDeclaration;
import org.eclipse.php.internal.core.ast.nodes.ClassInstanceCreation;
import org.eclipse.php.internal.core.ast.nodes.Identifier;
import org.eclipse.php.internal.core.ast.nodes.MethodDeclaration;

public class RenameClassVisitor extends PhpRefactoringVisitor {

    public RenameClassVisitor() {
        classInstanceCreations = new ArrayList();
        classDeclarations = new ArrayList();
        methods = new HashMap();
    }

    @Override
    public boolean apply(ASTNode node) {
        Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L4, getClass(), "apply", "Call apply for type", node.getType());
        return false;
    }

    public ArrayList getClassInstanceCreations(String className) {
        ArrayList foundClassInstanceCreations = new ArrayList();
        for (Iterator iterator = classInstanceCreations.iterator(); iterator.hasNext(); ) {
            Identifier classIdentifier = (Identifier) iterator.next();
            if (classIdentifier.getName().equals(className)) {
                foundClassInstanceCreations.add(classIdentifier);
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L3, getClass(), "getInstanceCreations", (new StringBuilder("Class instance creation for ")).append(className).append("found: ").append(classIdentifier).toString());
            }
        }
        return foundClassInstanceCreations;
    }

    public Identifier getClassDeclaration(String className) {
        Identifier foundClassDeclaration = null;
        for (Iterator iterator = classDeclarations.iterator(); iterator.hasNext(); ) {
            Identifier identifier = (Identifier) iterator.next();
            if (identifier.getName().equals(className)) {
                foundClassDeclaration = identifier;
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L3, getClass(), "getClassDeclaration", (new StringBuilder("Class declaration for ")).append(className).append(" found: ").append(identifier).toString());
            }
        }
        return foundClassDeclaration;
    }

    public ArrayList getConstructors(String className) {
        ArrayList foundConstructors = new ArrayList();
        for (Iterator iterator = methods.keySet().iterator(); iterator.hasNext(); ) {
            Identifier identifier = (Identifier) iterator.next();
            if (identifier.getName().equals(className) && ((Identifier) methods.get(identifier)).getName().equals(className)) {
                foundConstructors.add(identifier);
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L3, getClass(), "getConstructors", (new StringBuilder("Constructors for ")).append(className).append(" found: ").append(identifier).toString());
            }
        }
        return foundConstructors;
    }

    public Identifier getMethodWithNewName(String oldClassName, String newClassName) {
        Identifier foundMethod = null;
        for (Iterator iterator = methods.keySet().iterator(); iterator.hasNext(); ) {
            Identifier identifier = (Identifier) iterator.next();
            if (identifier.getName().equals(newClassName) && ((Identifier) methods.get(identifier)).getName().equals(oldClassName)) {
                foundMethod = identifier;
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.ERROR, getClass(), "getMethodWithNewName", (new StringBuilder("Method ")).append(newClassName).append(" found in class ").append(oldClassName).toString());
            }
        }
        return foundMethod;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(MethodDeclaration)", "search for methods started");
        ASTNode currentNode = node;
        Identifier classIdentifier = null;
        if (currentNode.getParent().getType() == 6) currentNode = currentNode.getParent();
        if (currentNode.getParent().getType() == 12) classIdentifier = ((ClassDeclaration) currentNode.getParent()).getName();
        Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(MethodDeclaration)", (new StringBuilder(String.valueOf(classIdentifier.getName().toString()))).append(" -> ").append(node.getFunction().getFunctionName().getName().toString()).toString());
        if (classIdentifier != null && node.getFunction().getFunctionName() != null) {
            methods.put(node.getFunction().getFunctionName(), classIdentifier);
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(MethodDeclaration)", (new StringBuilder("Method : \n\t")).append(node.getFunction().getFunctionName().toString()).append("\n\t  ---->  ").append(classIdentifier.toString()).toString());
        }
        return false;
    }

    @Override
    public boolean visit(ClassDeclaration node) {
        Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(ClassDeclaration)", "look for class declarations started");
        if (node.getName() != null) {
            classDeclarations.add(node.getName());
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(ClassDeclaration)", (new StringBuilder("Class declaration : \n\t")).append(node.getName()).toString());
        }
        return false;
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(ClassInstanceCreation)", "look for class indstance creations started");
        if (node.getClassName().getClassName() != null) {
            classInstanceCreations.add(node.getClassName().getClassName());
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "visit(ClassInstanceCreation)", (new StringBuilder("Class instance creation : \n\t")).append(node.getClassName().getClassName()).toString());
        }
        return false;
    }

    private ArrayList classDeclarations;

    private ArrayList classInstanceCreations;

    private HashMap methods;
}
