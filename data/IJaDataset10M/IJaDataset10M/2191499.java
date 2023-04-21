package de.grogra.xl.compiler;

import antlr.collections.AST;
import de.grogra.reflect.*;
import de.grogra.xl.expr.*;
import de.grogra.xl.compiler.scope.*;

class FieldInitializer {

    CompilerOptions options;

    boolean compiled;

    XField field;

    AST ast;

    MethodScope scope;

    Expression expr;
}
