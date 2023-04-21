package net.sf.jcorrect.standard.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintDescriptor;
import javax.validation.ConstraintValidator;
import net.sf.jcorrect.standard.constraintfactory.ConstraintFactorySource;
import org.apache.log4j.Logger;

/**
 * Implementation of the {@link ConstraintDescriptor}. This class has to be
 * constructed with a constraint annotation. This annotation is analyzed when a
 * client wants to obtain some information on it.
 * 
 * @author Eyal Lupu TODO Multithreading
 */
public class ConstraintDescriptorImpl<A extends Annotation> implements ConstraintDescriptor {

    /**
	 * Logger
	 */
    protected Logger logger = Logger.getLogger(getClass());

    /**
	 * The constraint annotation described by this instance
	 */
    private final A constraintAnnotation;

    /**
	 * The constraint validator implementation
	 */
    private Constraint<A> constraintValidator;

    /**
	 * The constraint annotation parameters
	 */
    private Map<String, Object> parameters;

    /**
	 * Validation groups
	 */
    private Set<String> groups;

    public ConstraintDescriptorImpl(A constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        if (null == constraintAnnotation) {
            throw new NullPointerException("The constraintAnnotation argument is madatory.");
        }
        if (!constraintAnnotation.annotationType().isAnnotationPresent(ConstraintValidator.class)) {
            throw new IllegalArgumentException("The constraintAnnotation argument's class must be annotated with ConstraintValidator (JSR 303 - Section 2.1");
        }
    }

    public Annotation getAnnotation() {
        return constraintAnnotation;
    }

    public Constraint getConstraintImplementation() {
        ConstraintValidator constraintValidatorAnnotation = constraintAnnotation.getClass().getAnnotation(ConstraintValidator.class);
        Class<? extends Constraint> constraintValidatorClass = constraintValidatorAnnotation.value();
        constraintValidator = ConstraintFactorySource.getFactory().getInstance(constraintValidatorClass);
        constraintValidator.initialize(constraintAnnotation);
        return constraintValidator;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getGroups() {
        if (null == groups) {
            String[] groupsArr = (String[]) getParameters().get("groups");
            LinkedHashSet<String> tmpSet = new LinkedHashSet<String>(groupsArr.length);
            for (String grp : groupsArr) {
                tmpSet.add(grp);
            }
            groups = Collections.unmodifiableSet(tmpSet);
        }
        return groups;
    }

    public Map<String, Object> getParameters() {
        if (null == parameters) {
            Method[] annotationMethods = constraintAnnotation.annotationType().getDeclaredMethods();
            Map<String, Object> tmpParams = new HashMap<String, Object>(annotationMethods.length);
            Object annotationValue;
            for (Method currParameter : annotationMethods) {
                try {
                    annotationValue = currParameter.invoke(constraintAnnotation);
                    tmpParams.put(currParameter.getName(), annotationValue);
                } catch (Exception e) {
                    String errorMsg = "Failed to process constraint annotation of type " + constraintAnnotation.annotationType().getName() + " problem reading parameter: " + currParameter.getName();
                    logger.error(errorMsg, e);
                    throw new ConstraintAnnotationProcessingException(errorMsg, e);
                }
            }
            parameters = Collections.unmodifiableMap(tmpParams);
        }
        return parameters;
    }
}
