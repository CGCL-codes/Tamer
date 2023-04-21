package org.kwantu.m2.model.ui;

import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.model.KwantuClass;
import org.kwantu.m2.model.KwantuAttribute;
import org.kwantu.m2.model.KwantuRelationship;
import org.kwantu.m2.model.KwantuMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.Field;

/**
 * Describes a type. Used when checking xpath expressions for type correctness, see
 * {@link org.kwantu.m2.xpath.compiler.Typed}. Instances of TypeDescriptor are expected
 * to be returned by the type resolver ({@link org.kwantu.m2.xpath.compiler.TypeResolver}).
 * 
 * @author chris
 */
public final class TypeDescriptor {

    private static final Log LOG = LogFactory.getLog(TypeDescriptor.class);

    public static final TypeDescriptor DOUBLE = new TypeDescriptor().type(Double.class).primitive(true);

    public static final TypeDescriptor BOOLEAN = new TypeDescriptor().type(Boolean.class).primitive(true);

    public static final TypeDescriptor NULL = new TypeDescriptor().type(null);

    public TypeDescriptor() {
    }

    private Object type;

    private String description;

    private boolean isCollection;

    /** A type is considered primitive if it does not have object identity. An
     * AbstractPersistentObject for example does have object identity, a Double
     * value not.
     */
    private boolean primitive;

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsCollection() {
        return isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public TypeDescriptor type(Object type) {
        setType(type);
        return this;
    }

    public TypeDescriptor description(String description) {
        setDescription(description);
        return this;
    }

    public TypeDescriptor collection(boolean b) {
        this.setIsCollection(b);
        return this;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    public TypeDescriptor primitive(boolean p) {
        setPrimitive(p);
        return this;
    }

    public boolean isAssignableFrom(TypeDescriptor other) {
        if (type.equals(Boolean.class) && other.getType().equals(boolean.class)) {
            return true;
        }
        if (type.equals(Integer.class) && other.getType().equals(int.class)) {
            return true;
        }
        if (type.equals(Double.class) && other.getType().equals(double.class)) {
            return true;
        }
        if (getType() instanceof Class && other.getType() instanceof Class) {
            return ((Class) getType()).isAssignableFrom(((Class) other.getType()));
        }
        if (getType() instanceof KwantuClass && other.getType() instanceof KwantuClass) {
            return isIsCollection() == other.isIsCollection() && ((KwantuClass) getType()).isAssignableFrom(((KwantuClass) other.getType()));
        }
        return isIsCollection() == other.isIsCollection() && getType().equals(other.getType());
    }

    public boolean ofType(Object o) {
        if (getType() instanceof Class) {
            return ((Class) getType()).isInstance(o);
        }
        if (getType() instanceof KwantuClass) {
            return ((KwantuClass) getType()).isInstance(o);
        }
        if (getType() == null) {
            return false;
        }
        throw new KwantuFaultException("unexpected result of getType(): " + getType());
    }

    public boolean isNumber() {
        if (type.equals(Integer.class) || type.equals(int.class) || type.equals(Double.class) || type.equals(double.class) || type.equals(BigDecimal.class)) {
            return true;
        }
        return false;
    }

    /** Extract a list of names of types that are part of this type as attributes, relationships or methods.
     *  Later we will need to parameterize to get 
     *  - only attributes
     *  - only relationships
     *  - only methods
     *  or any combination of the above.
     * @return
     */
    public List<String> getAttributeNames() {
        return getAttributeNames(true, true, true);
    }

    public List<String> getAttributeNames(boolean attributes, boolean relationships, boolean methods) {
        List<String> aNames = new ArrayList();
        LOG.info("In get Attribute names, type '" + getType() + "'.");
        if (getType() instanceof KwantuClass) {
            KwantuClass kwantuClass = (KwantuClass) getType();
            LOG.info("Getting attribute names for kwantu Class '" + kwantuClass.getName() + "'.");
            if (attributes) {
                for (KwantuAttribute a : kwantuClass.getKwantuAttributes()) {
                    aNames.add(a.getName());
                }
            }
            if (relationships) {
                for (KwantuRelationship r : kwantuClass.getKwantuRelationships()) {
                    aNames.add(r.getName() + "/");
                }
            }
            if (methods) {
                for (KwantuMethod m : kwantuClass.getKwantuMethods()) {
                    aNames.add(m.getMethodName() + "*");
                }
            }
            Collections.sort(aNames);
            return aNames;
        } else {
            Object o = getType();
            if (o instanceof Class) {
                Class clazz = (Class) o;
                for (Field f : clazz.getDeclaredFields()) {
                    LOG.info("Getting field name for '" + f.getName() + "'.");
                    aNames.add(f.getName());
                }
            }
        }
        return aNames;
    }

    @Override
    public String toString() {
        return super.toString() + "[type=" + getType() + ", collection=" + isIsCollection() + ", primitive=" + isPrimitive() + "]";
    }
}
