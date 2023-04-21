package org.jmlspecs.racwrap;

import java.io.*;
import java.util.*;
import org.multijava.mjc.*;
import org.jmlspecs.ajmlrac.*;
import org.jmlspecs.checker.*;

/**
*   The InterfacePrinter is an object that is used to output the
*   interface definition of a class to a file, given an abstract syntax tree.
*   This class extends JmlAbstractVisitor, so we only need to implement
*   the parts that we need.
**/
public class InterfacePrinter {

    public PrintStream out = null;

    public InterfacePrinter(PrintStream out) {
        this.out = out;
    }

    /**
    *   Parameterless constructor. Defaults to having out as System.out
    */
    public InterfacePrinter() {
        this.out = System.out;
    }

    public void print(JmlCompilationUnit compileUnit) {
        JTypeDeclarationType[] classes = compileUnit.typeDeclarations();
        this.out.println("//-----");
        this.out.println("//Interface file generated for:");
        this.out.println("//" + compileUnit.fileNameIdent());
        this.out.println("//-----");
        if (!compileUnit.packageNameAsString().equals("")) {
            String packageName;
            packageName = compileUnit.packageNameAsString().replace('/', '.');
            packageName = packageName.substring(0, packageName.length() - 1);
            this.out.println();
            this.out.println("package " + packageName + ";");
        }
        this.out.println();
        JClassOrGFImportType[] importClasses = compileUnit.importedClasses();
        for (int i = 0; i < importClasses.length; i++) {
            JClassOrGFImportType importClass = importClasses[i];
            this.out.print("import ");
            this.out.println(importClass.getName().replace('/', '.') + ";");
        }
        JPackageImportType[] importPackages = compileUnit.importedPackages();
        for (int i = 0; i < importPackages.length; i++) {
            JPackageImportType importPackage = importPackages[i];
            this.out.print("import ");
            this.out.println(importPackage.getName().replace('/', '.') + ".*;");
        }
        for (int i = 0; i < classes.length; i++) {
            JmlTypeDeclaration clazz = (JmlTypeDeclaration) classes[i];
            if (clazz.isInterface()) {
                System.out.println(clazz.ident() + " Not a class, ignoring...");
            } else {
                print_class(clazz);
            }
        }
    }

    private void print_class(JmlTypeDeclaration clazz) {
        this.out.println();
        if (clazz.hasFlag(clazz.modifiers(), clazz.ACC_PUBLIC)) {
            this.out.print("public ");
        }
        this.out.print("interface " + clazz.ident());
        String superName = ((JmlClassDeclaration) clazz).superName();
        if (superName != null && !superName.equals("Object") && !superName.equals("java/lang/Object")) {
            this.out.print(" extends " + superName.replace('/', '.'));
        }
        this.out.println(" { ");
        ArrayList methods = clazz.methods();
        for (int i = 0; i < methods.size(); i++) {
            JmlMethodDeclaration method;
            method = (JmlMethodDeclaration) methods.get(i);
            if (!(method.isStatic() || method.isConstructor())) {
                CType type = method.returnType();
                this.out.print("    public ");
                this.out.print(type.toString() + " ");
                this.out.print(method.ident() + "(");
                JFormalParameter[] params = method.parameters();
                Util.printParams(out, params);
                this.out.println(");");
            }
        }
        JFieldDeclarationType[] fields = clazz.fields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getField().isStatic() || fields[i].getField().isPrivate() || fields[i].getField().hasFlag(fields[i].modifiers(), clazz.ACC_MODEL)) {
                continue;
            }
            this.out.println();
            this.out.print("    ");
            this.out.print("public " + fields[i].getType().toString() + " ");
            this.out.println("_chx_get_" + fields[i].ident() + "();");
            this.out.print("    ");
            this.out.print("public void _chx_set_" + fields[i].ident());
            this.out.println("( " + fields[i].getType().toString() + " obj);");
        }
        this.out.println("} //End of interface");
    }
}
