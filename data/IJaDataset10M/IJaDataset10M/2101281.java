package net.sf.jelly.apt.decorations.declaration;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A property, representing the getter/setter pair.  In all cases, the description of the property matches the description of the
 * getter, but the annotations are the union of the getter and the setter, with the intersection preferring the getter.
 *
 * @author Ryan Heaton
 */
public class PropertyDeclaration extends DecoratedMethodDeclaration {

    private final DecoratedMethodDeclaration setter;

    private final DecoratedMethodDeclaration getter;

    private final String propertyName;

    private final TypeMirror propertyType;

    /**
   * A property declaration.
   *
   * @param getter The getter.
   * @param setter The setter.
   * @throws IllegalStateException If the getter and setter don't pair up.
   */
    public PropertyDeclaration(DecoratedMethodDeclaration getter, DecoratedMethodDeclaration setter) {
        super(getter == null ? setter : getter);
        this.getter = getter;
        this.setter = setter;
        this.propertyName = getter != null ? getter.getPropertyName() : setter.getPropertyName();
        TypeMirror propertyType = null;
        if (getter != null) {
            propertyType = getter.getReturnType();
        }
        if (setter != null) {
            Collection<ParameterDeclaration> parameters = setter.getParameters();
            if ((parameters == null) || (parameters.size() != 1)) {
                throw new IllegalStateException(this.setter.getPosition() + ": invalid setter for " + propertyType);
            } else {
                TypeMirror setterType = parameters.iterator().next().getType();
                if (propertyType == null) {
                    propertyType = setterType;
                } else if (!propertyType.equals(setterType)) {
                    throw new IllegalStateException(this.setter.getPosition() + ": invalid setter for getter of type " + propertyType);
                }
            }
        }
        if (propertyType == null) {
            throw new IllegalStateException("Unable to determine property type for property" + this.propertyName + ".");
        }
        this.propertyType = propertyType;
    }

    /**
   * The type of this property.
   *
   * @return The type of this property.
   */
    public TypeMirror getPropertyType() {
        return this.propertyType;
    }

    /**
   * The simple name of the property is the property name.
   *
   * @return The simple name of the property is the property name.
   */
    @Override
    public String getSimpleName() {
        return this.propertyName;
    }

    /**
   * Make sure the property name is calculated correctly.
   * cd
   */
    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
   * The setter, or null if this property is a read-only property.
   *
   * @return The setter.
   */
    public DecoratedMethodDeclaration getSetter() {
        return setter;
    }

    /**
   * The getter.
   *
   * @return The getter.
   */
    public DecoratedMethodDeclaration getGetter() {
        return getter;
    }

    /**
   * Whether this property is read-only.
   *
   * @return Whether this property is read-only.
   */
    public boolean isReadOnly() {
        return getSetter() == null;
    }

    /**
   * Whether this property is write-only.
   *
   * @return Whether this property is write-only.
   */
    public boolean isWriteOnly() {
        return getGetter() == null;
    }

    /**
   * Gets the annotations on the setter and the getter.  If the annotation is on both the setter and the getter, only the one on the getter will
   * be included.
   *
   * @return The union of the annotations on the getter and setter.
   */
    @Override
    public Map<String, AnnotationMirror> getAnnotations() {
        Map<String, AnnotationMirror> annotations = new HashMap<String, AnnotationMirror>();
        if (getGetter() != null) {
            annotations.putAll(getGetter().getAnnotations());
        }
        if (getSetter() != null) {
            annotations.putAll(getSetter().getAnnotations());
        }
        return annotations;
    }

    /**
   * Gets the collection of annotations on the setter and the getter.  If the annotation is on both the setter and the getter, only the one on the getter will
   * be included.
   *
   * @return The union of the annotations on the getter and setter.
   */
    @Override
    public Collection<AnnotationMirror> getAnnotationMirrors() {
        return getAnnotations().values();
    }

    /**
   * Gets the annotation on the getter.  If it doesn't exist, returns the one on the setter.
   *
   * @param annotationType The annotation type.
   * @return The annotation.
   */
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        A annotation = null;
        if (this.getter != null) {
            annotation = this.getter.getAnnotation(annotationType);
        }
        if ((annotation == null) && (this.setter != null)) {
            annotation = this.setter.getAnnotation(annotationType);
        }
        return annotation;
    }

    @Override
    public TypeMirror getReturnType() {
        return getPropertyType();
    }

    @Override
    public boolean isGetter() {
        return false;
    }

    @Override
    public boolean isSetter() {
        return false;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }
}
