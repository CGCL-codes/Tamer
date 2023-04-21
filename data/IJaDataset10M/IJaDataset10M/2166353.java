package jfun.yan.nuts.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import jfun.yan.util.MemberPredicate;

/**
 * A MemberPredicate implementation that checks the presence of
 * a certain annotation.
 * <p>
 * @author Ben Yu
 * Dec 30, 2005 10:55:50 PM
 */
public class AnnotationMemberPredicate implements MemberPredicate {

    private final Class<? extends Annotation> annotation;

    public boolean isMember(String name, Member m) {
        if (m instanceof Method) {
            return ((Method) m).isAnnotationPresent(annotation);
        } else if (m instanceof Field) {
            return ((Field) m).isAnnotationPresent(annotation);
        } else if (m instanceof Constructor) {
            return ((Constructor) m).isAnnotationPresent(annotation);
        }
        return false;
    }

    public AnnotationMemberPredicate(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AnnotationMemberPredicate) {
            final AnnotationMemberPredicate other = (AnnotationMemberPredicate) obj;
            return annotation.equals(other.annotation);
        } else return false;
    }

    public int hashCode() {
        return annotation.hashCode();
    }

    public String toString() {
        return annotation.toString();
    }
}
