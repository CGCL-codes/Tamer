package org.jmlspecs.openjml.jmldoc;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.comp.JmlMemberEnter;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javadoc.AnnotationTypeDocImpl;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.DocEnv;

public class DocEnvJml extends DocEnv {

    protected DocEnvJml(Context context) {
        super(context);
    }

    public static void preRegister(final Context context) {
        context.put(docEnvKey, new Context.Factory<DocEnv>() {

            public DocEnv make(Context context) {
                return new DocEnvJml(context);
            }
        });
    }

    protected ClassDocImpl getClassDoc(ClassSymbol clazz) {
        ClassDocImpl result = classMap.get(clazz);
        if (result != null) return result;
        if (isAnnotationType(clazz)) {
            result = super.getClassDoc(clazz);
        } else {
            result = new ClassDocJml(this, clazz);
        }
        classMap.put(clazz, result);
        return result;
    }

    protected void makeClassDoc(ClassSymbol clazz, String docComment, JCClassDecl tree, Position.LineMap lineMap) {
        ClassDocImpl result = classMap.get(clazz);
        if (result != null) {
            super.makeClassDoc(clazz, docComment, tree, lineMap);
            return;
        }
        if (isAnnotationType(tree)) {
            super.makeClassDoc(clazz, docComment, tree, lineMap);
        } else {
            result = new ClassDocJml(this, clazz, docComment, tree, lineMap);
            classMap.put(clazz, result);
        }
    }
}
