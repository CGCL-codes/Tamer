package org.apache.jdo.impl.model.jdo;

import org.apache.jdo.impl.model.jdo.util.TypeSupport;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOMap;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.util.I18NHelper;

/**
 * An instance of this class represents the JDO relationship metadata 
 * (the treatment of keys and values) of a map relationship field. 
 * This dynamic implementation only stores property values explicitly
 * set by setter method.
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOMapImplDynamic extends JDORelationshipImpl implements JDOMap {

    /** Property embeddedKey. */
    protected Boolean embeddedKey;

    /** Property keyType. No default. */
    protected transient JavaType keyType;

    /** Property keyTypeName. Defaults to java.lang.Object. */
    private String keyTypeName = "java.lang.Object";

    /** Property embeddedValue. */
    protected Boolean embeddedValue;

    /** Property valueType. No default. */
    protected transient JavaType valueType;

    /** Property valueTypeName. Defaults to java.lang.Object. */
    private String valueTypeName = "java.lang.Object";

    /** I18N support */
    private static final I18NHelper msg = I18NHelper.getInstance(JDOMapImplDynamic.class);

    /**
     * Determines whether the keys of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the keys are stored as part of this instance;
     * <code>false</code> otherwise
     */
    public boolean isEmbeddedKey() {
        if (embeddedKey != null) {
            return embeddedKey.booleanValue();
        }
        JavaType type = getKeyType();
        return (type != null) ? TypeSupport.isEmbeddedElementType(type) : false;
    }

    /**
     * Set whether the keys of the map should be stored if possible as part 
     * of the instance instead of as their own instances in the datastore.
     * @param embeddedKey <code>true</code> if the keys are stored as part of
     * this instance; <code>false</code> otherwise
     */
    public void setEmbeddedKey(boolean embeddedKey) {
        this.embeddedKey = (embeddedKey ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Get the type representation of the keys for this JDOMap.
     * @return the type of the keys of this JDOMap  
     */
    public JavaType getKeyType() {
        if (keyType != null) {
            return keyType;
        }
        JavaType type = null;
        if (keyTypeName != null) {
            JDOField jdoField = getDeclaringField();
            JDOClass jdoClass = jdoField.getDeclaringClass();
            JDOModel jdoModel = jdoClass.getDeclaringModel();
            type = TypeSupport.resolveType(jdoModel, keyTypeName, jdoClass.getPackagePrefix());
            if (type == null) {
                throw new ModelFatalException(msg.msg("EXC_CannotResolveKeyType", keyTypeName, jdoField.getName(), jdoClass.getName()));
            }
        }
        return type;
    }

    /**
     * Set the type representation of the keys for this JDOMap.
     * @param keyType the type representation of the keys
     */
    public void setKeyType(JavaType keyType) {
        this.keyType = keyType;
        if (keyType != null) {
            setKeyTypeName(keyType.getName());
        }
    }

    /**
     * Get the string representation of the type of the keys for this JDOMap.
     * @return the key type as string
     */
    public String getKeyTypeName() {
        return keyTypeName;
    }

    /**
     * Set string representation of the type of the keys for this JDOMap.
     * @param keyTypeName the name of the key type
     */
    public void setKeyTypeName(String keyTypeName) {
        this.keyTypeName = keyTypeName;
    }

    /**
     * Determines whether the values of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the values are stored as part of this 
     * instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedValue() {
        if (embeddedValue != null) {
            return embeddedValue.booleanValue();
        }
        JavaType type = getValueType();
        return (type != null) ? TypeSupport.isEmbeddedElementType(type) : false;
    }

    /**
     * Set whether the values of the map should be stored if possible as part 
     * of the instance instead of as their own instances in the datastore.
     * @param embeddedValue <code>true</code> if the values are stored as part 
     * of this instance; <code>false</code> otherwise
     */
    public void setEmbeddedValue(boolean embeddedValue) {
        this.embeddedValue = (embeddedValue ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Get the type representation of the values for this JDOMap.
     * @return the type of the values of this JDOMap  
     */
    public JavaType getValueType() {
        if (valueType != null) {
            return valueType;
        }
        JavaType type = null;
        if (valueTypeName != null) {
            JDOField jdoField = getDeclaringField();
            JDOClass jdoClass = jdoField.getDeclaringClass();
            JDOModel jdoModel = jdoClass.getDeclaringModel();
            type = TypeSupport.resolveType(jdoModel, valueTypeName, jdoClass.getPackagePrefix());
            if (type == null) {
                throw new ModelFatalException(msg.msg("EXC_CannotResolveValueType", valueTypeName, jdoField.getName(), jdoClass.getName()));
            }
        }
        return type;
    }

    /**
     * Set the type representation of the values for this JDOMap.
     * @param valueType the type representation of the values
     */
    public void setValueType(JavaType valueType) {
        this.valueType = valueType;
        if (valueType != null) {
            setKeyTypeName(valueType.getName());
        }
    }

    /**
     * Get the string representation of the type of the values for this JDOMap.
     * @return the key value as string
     */
    public String getValueTypeName() {
        return valueTypeName;
    }

    /**
     * Set string representation of the type of the values for this JDOMap.
     * @param valueTypeName the name of the value type
     */
    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
    }

    /**
     * Determines whether this JDORelationship represents a map 
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOMap instance.
     * @return <code>true</code> if this JDORelationship represents a
     * map relationship; <code>false</code> otherwise.
     */
    public boolean isJDOMap() {
        return true;
    }

    /** 
     * Get the type representation of the relationship. This will be 
     * the JavaType for references, the element type for collections
     * and arrays, and the value type for maps.
     * @return the relationship type
     */
    public JavaType getRelatedJavaType() {
        return getValueType();
    }
}
