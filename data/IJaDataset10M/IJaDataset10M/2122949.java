package br.com.arsmachina.tapestrycrud.hibernate.validator;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.services.ValidationConstraintGenerator;

/**
 * Adds <code>required</code> validation to Tapestry based on the <code>optional</code>
 * attribute of the {@link Basic}, {@link ManyToOne}, and {@link OneToOne} annotations.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public final class OptionalAttributeValidationConstraintsGenerator implements ValidationConstraintGenerator {

    private static final List<String> REQUIRED;

    static {
        REQUIRED = new ArrayList<String>();
        REQUIRED.add("required");
    }

    @SuppressWarnings("unchecked")
    public List<String> buildConstraints(Class propertyType, AnnotationProvider annotationProvider) {
        boolean optional = true;
        ManyToOne manyToOne = annotationProvider.getAnnotation(ManyToOne.class);
        if (manyToOne != null) {
            optional = manyToOne.optional();
        } else {
            Basic basic = annotationProvider.getAnnotation(Basic.class);
            if (basic != null) {
                optional = basic.optional();
            } else {
                OneToOne oneToOne = annotationProvider.getAnnotation(OneToOne.class);
                if (oneToOne != null) {
                    optional = oneToOne.optional();
                }
            }
        }
        return optional ? null : REQUIRED;
    }
}
