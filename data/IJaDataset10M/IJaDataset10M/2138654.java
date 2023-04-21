package com.smartitengineering.tostringgenerator;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.TreeMaker;
import org.netbeans.api.java.source.WorkingCopy;

/**
 *
 * @author imyousuf
 */
public class ToStringGeneratorFactory {

    private static final String TO_STRING = "toString";

    private static final String IMPORT_JAVA_UTIL_QUALIFIER = "java\\.util\\.(\\*|Set|List|Vector|ArrayList|HashSet|TreeSet|LinkedList)";

    private static final String JAVA_UTIL_COLLECTION_QUALIFIER = "(java\\.util\\.)?(Set|List|Vector|ArrayList|HashSet|TreeSet|LinkedList)";

    private static final Logger LOGGER = Logger.getLogger(ToStringGeneratorFactory.class.getName());

    static {
        Handler[] handlers = LOGGER.getHandlers();
        boolean hasConsoleHandler = false;
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                hasConsoleHandler = true;
            }
        }
        if (!hasConsoleHandler) {
            LOGGER.addHandler(new ConsoleHandler());
        }
    }

    public static final void generateToString(WorkingCopy workingCopy) throws IOException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Received Working Copy: " + workingCopy);
        }
        workingCopy.toPhase(Phase.RESOLVED);
        TreeMaker make = workingCopy.getTreeMaker();
        CompilationUnitTree compilationUnitTree = workingCopy.getCompilationUnit();
        for (Tree typeDecl : compilationUnitTree.getTypeDecls()) {
            if (Tree.Kind.CLASS == typeDecl.getKind()) {
                ClassTree clazz = (ClassTree) typeDecl;
                addToStringToClass(make, workingCopy, clazz);
            }
        }
    }

    protected static void addOtherVars(final StringBuilder body, StringBuilder memberName, JavaMemberVariable instanceMemberVar) {
        appendToBuilder(body, "toStringBuilder", memberName);
        appendToBuilder(body, "toStringBuilder", instanceMemberVar.getName());
    }

    protected static void addArrayVars(StringBuilder body, StringBuilder memberNameTitle, JavaMemberVariable instanceMemberVar) {
        appendToBuilder(body, "toStringBuilder", memberNameTitle);
        StringBuilder size = new StringBuilder();
        body.append("if (").append(instanceMemberVar.getName()).append(" != null) {");
        appendToBuilder(body, "toStringBuilder", "\"\\nSize: \"");
        size.append(instanceMemberVar.getName()).append(".length");
        appendToBuilder(body, "toStringBuilder", size);
        body.append("for (int i = 0; i < ").append(instanceMemberVar.getName()).append(".length; ++i) {");
        appendToBuilder(body, "toStringBuilder", "\"\\nIndex \"");
        appendToBuilder(body, "toStringBuilder", "i");
        appendToBuilder(body, "toStringBuilder", "\": \"");
        StringBuilder arrayIndex = new StringBuilder(instanceMemberVar.getName()).append("[i]");
        appendToBuilder(body, "toStringBuilder", arrayIndex);
        body.append("}");
        body.append("} else {");
        appendToBuilder(body, "toStringBuilder", "\"NULL\"");
        body.append("}");
    }

    protected static void addCollectionVars(StringBuilder body, StringBuilder memberNameTitle, JavaMemberVariable instanceMemberVar) {
        appendToBuilder(body, "toStringBuilder", memberNameTitle);
        StringBuilder size = new StringBuilder();
        body.append("if (").append(instanceMemberVar.getName()).append(" != null) {");
        appendToBuilder(body, "toStringBuilder", "\"\\nSize: \"");
        size.append(instanceMemberVar.getName()).append(".size()");
        appendToBuilder(body, "toStringBuilder", size);
        body.append("java.util.Iterator collectionIiterator = ").append(instanceMemberVar.getName()).append(".iterator();");
        body.append("for (int i = 0; collectionIiterator.hasNext(); ++i) {");
        appendToBuilder(body, "toStringBuilder", "\"\\nIndex \"");
        appendToBuilder(body, "toStringBuilder", "i");
        appendToBuilder(body, "toStringBuilder", "\": \"");
        appendToBuilder(body, "toStringBuilder", "collectionIiterator.next()");
        body.append("}");
        body.append("} else {");
        appendToBuilder(body, "toStringBuilder", "\"NULL\"");
        body.append("}");
    }

    protected static void addToStringToClass(TreeMaker make, WorkingCopy workingCopy, ClassTree clazz) {
        ClassTree modifiedClazz = makeNewToString(make, workingCopy, clazz, true, true);
        workingCopy.rewrite(clazz, modifiedClazz);
        List<? extends Tree> members = clazz.getMembers();
        for (Tree member : members) {
            Kind memberKind = member.getKind();
            if (memberKind.equals(Kind.CLASS)) {
                addToStringToClass(make, workingCopy, (ClassTree) member);
            } else {
            }
        }
    }

    protected static ClassTree removeOldToString(final ClassTree clazz, final TreeMaker make) {
        ClassTree modifiedClazz = null;
        List<? extends Tree> members = clazz.getMembers();
        for (Tree member : members) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Kind: " + member.getKind().name());
                LOGGER.finest("Class: " + member.getClass());
            }
            if (member.getKind().equals(Tree.Kind.METHOD)) {
                MethodTree method = (MethodTree) member;
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("Found Method: " + method.getName() + "(" + method.getName().contentEquals(TO_STRING) + ")" + " Params: " + method.getParameters() + "(" + method.getParameters().isEmpty() + ")" + " Vars: " + method.getTypeParameters());
                }
                if (method.getName().contentEquals(TO_STRING) && method.getParameters().isEmpty()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Removed method: " + method.getName());
                    }
                    modifiedClazz = make.removeClassMember(clazz, member);
                }
            }
        }
        return modifiedClazz;
    }

    protected static List<JavaMemberVariable> getInstanceMemberVars(ClassTree clazz, WorkingCopy workingCopy) {
        ArrayList<JavaMemberVariable> instanceMemberVars = new ArrayList<JavaMemberVariable>();
        List<? extends Tree> members = clazz.getMembers();
        List<? extends ImportTree> imports = workingCopy.getCompilationUnit().getImports();
        List<String> collectionQualifiedIdentifiers = new ArrayList<String>();
        if (LOGGER.isLoggable(Level.FINEST)) {
            List<? extends TypeParameterTree> types = clazz.getTypeParameters();
            for (TypeParameterTree paramType : types) {
                LOGGER.finest("Type Name: " + paramType.getName());
            }
            for (ImportTree importTree : imports) {
                LOGGER.finest("Import Q-Id: " + importTree.getQualifiedIdentifier().getKind().name());
                LOGGER.finest("Import Tree: " + importTree.toString() + " Length: " + importTree.toString().length() + '\n');
                MemberSelectTree memberSelectTree = (MemberSelectTree) importTree.getQualifiedIdentifier();
                LOGGER.finest("Member Selected ID: " + memberSelectTree.getIdentifier());
                LOGGER.finest("Member toString: " + memberSelectTree.toString());
                LOGGER.finest("Member Exp toString: " + memberSelectTree.getExpression().toString());
                LOGGER.finest("Util Import Pattern matches: " + Pattern.matches(IMPORT_JAVA_UTIL_QUALIFIER, memberSelectTree.toString()));
            }
        }
        for (ImportTree importTree : imports) {
            MemberSelectTree memberSelectTree = (MemberSelectTree) importTree.getQualifiedIdentifier();
            if (Pattern.matches(IMPORT_JAVA_UTIL_QUALIFIER, memberSelectTree.toString())) {
                collectionQualifiedIdentifiers.add(memberSelectTree.getIdentifier().toString());
            }
        }
        for (Tree member : members) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Kind: " + member.getKind().name());
                LOGGER.finest("Class: " + member.getClass());
            }
            if (member.getKind().equals(Tree.Kind.VARIABLE)) {
                VariableTree variableTree = (VariableTree) member;
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("Variable Name: " + variableTree.getName());
                }
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("Variable Type: " + variableTree.getType().getKind().name());
                }
                Set<javax.lang.model.element.Modifier> modifiers = variableTree.getModifiers().getFlags();
                JavaMemberVariable.Type varType = JavaMemberVariable.Type.OTHER;
                boolean isInstance = true;
                for (javax.lang.model.element.Modifier modifier : modifiers) {
                    if (modifier.compareTo(javax.lang.model.element.Modifier.STATIC) == 0) {
                        isInstance = false;
                    }
                }
                if (isInstance) {
                    Tree typeTree = variableTree.getType();
                    if (typeTree.getKind().equals(Tree.Kind.ARRAY_TYPE)) {
                        varType = JavaMemberVariable.Type.ARRAY;
                    } else if (typeTree.getKind().equals(Tree.Kind.PARAMETERIZED_TYPE)) {
                        ParameterizedTypeTree parameterizedTypeTree = (ParameterizedTypeTree) typeTree;
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest("Var ParamTypeTree Type Kind:" + parameterizedTypeTree.getType().getKind());
                        }
                        if (parameterizedTypeTree.getType().getKind().equals(Tree.Kind.IDENTIFIER)) {
                            varType = handleIdentifierTypeTree(parameterizedTypeTree.getType(), collectionQualifiedIdentifiers);
                        } else if (parameterizedTypeTree.getType().getKind().equals(Tree.Kind.MEMBER_SELECT)) {
                            MemberSelectTree tree = (MemberSelectTree) parameterizedTypeTree.getType();
                            if (Pattern.matches(JAVA_UTIL_COLLECTION_QUALIFIER, tree.toString())) {
                                varType = JavaMemberVariable.Type.COLLECTION;
                            }
                        }
                    } else if (typeTree.getKind().equals(Tree.Kind.IDENTIFIER)) {
                        varType = handleIdentifierTypeTree(typeTree, collectionQualifiedIdentifiers);
                    }
                    final JavaMemberVariable memberVar = new JavaMemberVariable(variableTree.getName().toString(), varType);
                    instanceMemberVars.add(memberVar);
                }
            }
        }
        return instanceMemberVars;
    }

    protected static JavaMemberVariable.Type handleIdentifierTypeTree(Tree typeTree, List<String> collectionQualifiedId) {
        IdentifierTree identifierTree = (IdentifierTree) typeTree;
        final String idName = identifierTree.getName().toString();
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("Var IdTypeTree Name:" + idName);
        }
        if (collectionQualifiedId.contains(idName)) {
            return JavaMemberVariable.Type.COLLECTION;
        } else if (collectionQualifiedId.contains("*") && Pattern.matches(JAVA_UTIL_COLLECTION_QUALIFIER, idName)) {
            return JavaMemberVariable.Type.COLLECTION;
        }
        return JavaMemberVariable.Type.OTHER;
    }

    protected static ClassTree makeNewToString(final TreeMaker make, final WorkingCopy workingCopy, final ClassTree clazz, final boolean replaceOldToString, final boolean includeSuper) {
        ClassTree modifiedClazz = null;
        if (replaceOldToString) {
            modifiedClazz = removeOldToString(clazz, make);
        }
        TypeElement element = workingCopy.getElements().getTypeElement("java.lang.String");
        ExpressionTree returnType = make.QualIdent(element);
        ModifiersTree methodModifier = make.Modifiers(Modifier.PUBLIC, Collections.<AnnotationTree>emptyList());
        final StringBuilder body = new StringBuilder("{StringBuilder toStringBuilder = new StringBuilder();");
        if (includeSuper) {
            appendToBuilder(body, "toStringBuilder", "super.toString()");
            appendToBuilder(body, "toStringBuilder", "\"\\n\"");
        }
        List<JavaMemberVariable> instanceMemberVars = getInstanceMemberVars(clazz, workingCopy);
        for (JavaMemberVariable instanceMemberVar : instanceMemberVars) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Working with variable " + instanceMemberVar.getName() + " of type " + instanceMemberVar.getType().name());
            }
            StringBuilder memberNameTitle = new StringBuilder("\"\\n").append(instanceMemberVar.getName()).append(": \"");
            switch(instanceMemberVar.getType()) {
                case ARRAY:
                    addArrayVars(body, memberNameTitle, instanceMemberVar);
                    break;
                case COLLECTION:
                    addCollectionVars(body, memberNameTitle, instanceMemberVar);
                    break;
                default:
                case OTHER:
                    addOtherVars(body, memberNameTitle, instanceMemberVar);
            }
        }
        body.append("return toStringBuilder.toString()}");
        MethodTree toStringTree = make.Method(methodModifier, TO_STRING, returnType, Collections.<TypeParameterTree>emptyList(), Collections.<VariableTree>emptyList(), Collections.<ExpressionTree>emptyList(), body.toString(), null);
        modifiedClazz = make.addClassMember((modifiedClazz == null) ? clazz : modifiedClazz, toStringTree);
        return modifiedClazz;
    }

    protected static void appendToBuilder(StringBuilder builder, String innerBuilderName, String toAppend) {
        builder.append(innerBuilderName).append(".append(").append(toAppend).append(");");
    }

    protected static void appendToBuilder(StringBuilder builder, String innerBuilderName, StringBuilder toAppend) {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("toAppend: " + toAppend + "\n");
        }
        builder.append(innerBuilderName).append(".append(").append(toAppend).append(");");
    }

    protected static class JavaMemberVariable {

        public enum Type {

            ARRAY, COLLECTION, OTHER
        }

        private String name;

        private JavaMemberVariable.Type type;

        public JavaMemberVariable(String name, JavaMemberVariable.Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public JavaMemberVariable.Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }
}
