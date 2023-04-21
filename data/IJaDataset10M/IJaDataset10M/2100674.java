package com.googlecode.kipler.syntax.concept;

import com.googlecode.kipler.common.Copyable;
import com.googlecode.kipler.syntax.Role;

/**
 * This class represents a qualified number restriction expression.
 * 
 * @author İnanç Seylan
 */
public class QualifiedNoRestriction extends Concept {

    private InequalityConstructor constructor;

    private int no;

    private Role role;

    private Concept concept;

    public QualifiedNoRestriction() {
    }

    public QualifiedNoRestriction(InequalityConstructor constructor, int no, Role role, Concept concept) {
        setConstructor(constructor);
        setNo(no);
        setRole(role);
        setConcept(concept);
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public InequalityConstructor getConstructor() {
        return constructor;
    }

    public void setConstructor(InequalityConstructor constructor) {
        this.constructor = constructor;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
	 * Changes the expression to at least if it's at most restriction or vice
	 * versa. There are three exceptions, though. If this is of the form <=(0,
	 * R, C), it changes to some(R, C). If this is of the form >=(0, R, C), it
	 * changes to bottom concept. If this is of the form >=(1, R, C), it changes
	 * to all(R, ~C).
	 */
    public Concept toggleNegated() {
        Concept result = this;
        if (getConstructor().equals(InequalityConstructor.AT_MOST)) {
            if (getNo() == 0) {
                result = new RoleQuantification(RoleQuantification.Constructor.SOME, getRole(), getConcept());
            } else {
                setConstructor(InequalityConstructor.AT_LEAST);
                no++;
            }
        } else {
            if (getNo() == 0) {
                result = new Bottom();
            } else if (getNo() == 1) {
                result = new RoleQuantification(RoleQuantification.Constructor.ALL, getRole(), getConcept().copy().toggleNegated());
            } else {
                setConstructor(InequalityConstructor.AT_MOST);
                no--;
            }
        }
        return result;
    }

    /**
	 * @return false
	 */
    public boolean isNegated() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj.getClass().equals(this.getClass()))) {
            QualifiedNoRestriction other = (QualifiedNoRestriction) obj;
            return (this.isLeftBiased() == other.isLeftBiased() && this.getConstructor().equals(other.getConstructor()) && this.getNo() == other.getNo() && this.getRole().equals(other.getRole()) && this.getConcept().equals(other.getConcept()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17 * 37 + new Boolean(isLeftBiased()).hashCode() + getConstructor().hashCode() + new Integer(getNo()).hashCode() + getConcept().hashCode() + getRole().hashCode();
    }

    /**
	 * @see Copyable#copy()
	 */
    public QualifiedNoRestriction copy() {
        QualifiedNoRestriction qr = new QualifiedNoRestriction();
        qr.setConstructor(getConstructor());
        qr.setNo(no);
        qr.setRole(getRole().copy());
        qr.setConcept(getConcept().copy());
        qr.setLeftBiased(isLeftBiased());
        return qr;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getConstructor());
        buffer.append("(");
        buffer.append(getNo());
        buffer.append(", ");
        buffer.append(getRole().toString());
        buffer.append(", ");
        buffer.append(getConcept());
        buffer.append(")");
        return buffer.toString();
    }

    /**
	 * Calls
	 * {@link ConceptVisitor#visitQualifiedNoRestriction(QualifiedNoRestriction)}
	 * on itself.
	 */
    public void accept(ConceptVisitor v) {
        v.visitQualifiedNoRestriction(this);
    }

    @Override
    public void setLeftBiased(boolean value) {
        super.setLeftBiased(value);
        getConcept().setLeftBiased(value);
    }
}
