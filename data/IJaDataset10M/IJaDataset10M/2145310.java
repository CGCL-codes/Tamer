package org.codehaus.jam.visitor;

import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JComment;
import org.codehaus.jam.JConstructor;
import org.codehaus.jam.JField;
import org.codehaus.jam.JInvokable;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JPackage;
import org.codehaus.jam.JParameter;

/**
 * <p>An adaptor which helps another MVisitor visit a JElement and its
 * children, recursively.  Note that inherited class or annotations members
 * are never visited, nor are referenced classes (e.g. referenced via member
 * types).  The following table lists each element and the child types
 * which are traversed.</p>
 *
 * <table border='1'>
 * <tr><td><b>Element</b></td><td><b>Sub-elements traversal</b></td></tr>
 * <tr><td>Package       </td><td>Classes, Annotations, Comments</td></tr>
 * <tr><td>Class         </td><td>Fields, Constructors, Methods, Annotations, Comments</td></tr>
 * <tr><td>Field         </td><td>Annotations, Comments</td></tr>
 * <tr><td>Constructor   </td><td>Parameters, Annotations, Comments</td></tr>
 * <tr><td>Method        </td><td>Parameters, Annotations, Comments</td></tr>
 * <tr><td>Parameter     </td><td>Annotations, Comments</td></tr>
 * <tr><td>Annotation    </td><td>[none]</td></tr>
 * <tr><td>Comment       </td><td>[none]</td></tr>
 * </table>
 *
 * </p>
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class TraversingJVisitor extends JVisitor {

    private JVisitor mDelegate;

    public TraversingJVisitor(JVisitor jv) {
        if (jv == null) throw new IllegalArgumentException("null jv");
        mDelegate = jv;
    }

    public void visit(JPackage pkg) {
        pkg.accept(mDelegate);
        JClass[] c = pkg.getClasses();
        for (int i = 0; i < c.length; i++) visit(c[i]);
        visitAnnotations(pkg);
        visitComment(pkg);
    }

    public void visit(JClass clazz) {
        clazz.accept(mDelegate);
        {
            JField[] f = clazz.getDeclaredFields();
            for (int i = 0; i < f.length; i++) visit(f[i]);
        }
        {
            JConstructor[] c = clazz.getConstructors();
            for (int i = 0; i < c.length; i++) visit(c[i]);
        }
        {
            JMethod[] m = clazz.getMethods();
            for (int i = 0; i < m.length; i++) visit(m[i]);
        }
        visitAnnotations(clazz);
        visitComment(clazz);
    }

    public void visit(JField field) {
        field.accept(mDelegate);
        visitAnnotations(field);
        visitComment(field);
    }

    public void visit(JConstructor ctor) {
        ctor.accept(mDelegate);
        visitParameters(ctor);
        visitAnnotations(ctor);
        visitComment(ctor);
    }

    public void visit(JMethod method) {
        method.accept(mDelegate);
        visitParameters(method);
        visitAnnotations(method);
        visitComment(method);
    }

    public void visit(JParameter param) {
        param.accept(mDelegate);
        visitAnnotations(param);
        visitComment(param);
    }

    public void visit(JAnnotation ann) {
        ann.accept(mDelegate);
    }

    public void visit(JComment comment) {
        comment.accept(mDelegate);
    }

    private void visitParameters(JInvokable iv) {
        JParameter[] p = iv.getParameters();
        for (int i = 0; i < p.length; i++) visit(p[i]);
    }

    private void visitAnnotations(JAnnotatedElement ae) {
        JAnnotation[] anns = ae.getAnnotations();
        for (int i = 0; i < anns.length; i++) visit(anns[i]);
    }

    private void visitComment(JAnnotatedElement e) {
        JComment c = e.getComment();
        if (c != null) visit(c);
    }
}
