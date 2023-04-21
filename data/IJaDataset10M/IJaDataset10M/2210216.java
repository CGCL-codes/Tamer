package org.codehaus.jam.internal.elements;

import org.codehaus.jam.JClass;
import org.codehaus.jam.internal.classrefs.DirectJClassRef;
import org.codehaus.jam.internal.classrefs.JClassRef;
import org.codehaus.jam.internal.classrefs.QualifiedJClassRef;
import org.codehaus.jam.internal.classrefs.UnqualifiedJClassRef;
import org.codehaus.jam.mutable.MParameter;
import org.codehaus.jam.visitor.JVisitor;
import org.codehaus.jam.visitor.MVisitor;

/**
 * <p>Implementation of JParameter and MParameter.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class ParameterImpl extends MemberImpl implements MParameter {

    private JClassRef mTypeClassRef;

    ParameterImpl(String simpleName, InvokableImpl containingMember, String typeName) {
        super(containingMember);
        setSimpleName(simpleName);
        setType(typeName);
    }

    public String getQualifiedName() {
        return getSimpleName();
    }

    public void setType(String qcname) {
        if (qcname == null) throw new IllegalArgumentException("null typename");
        mTypeClassRef = QualifiedJClassRef.create(qcname, (ClassImpl) getContainingClass());
    }

    public void setType(JClass qcname) {
        if (qcname == null) throw new IllegalArgumentException("null qcname");
        mTypeClassRef = DirectJClassRef.create(qcname);
    }

    public void setUnqualifiedType(String ucname) {
        if (ucname == null) throw new IllegalArgumentException("null ucname");
        mTypeClassRef = UnqualifiedJClassRef.create(ucname, (ClassImpl) getContainingClass());
    }

    public JClass getType() {
        return mTypeClassRef.getRefClass();
    }

    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }
}
