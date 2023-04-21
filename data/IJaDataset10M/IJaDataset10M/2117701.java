package common.devbot.util.db.description;

import java.lang.reflect.Method;
import java.util.Map;
import common.devbot.descriptor.description.Description;
import common.devbot.descriptor.description.DescriptionComposite;
import common.devbot.descriptor.description.DescriptionCompositeType;
import common.devbot.util.description.TypeSQL;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class FieldDescription implements Description {

    /** DOCUMENT ME! */
    private String javaFieldName;

    /** DOCUMENT ME! */
    private String dbFieldName;

    /** DOCUMENT ME! */
    private Class<?> javaFieldType;

    /** DOCUMENT ME! */
    private TypeSQL dbFieldType;

    /** DOCUMENT ME! */
    private Method setter;

    /** DOCUMENT ME! */
    private Method getter;

    /** DOCUMENT ME! */
    private boolean primaryKey = false;

    /**
     * 
     */
    private boolean persistanceFlag = false;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final TypeSQL getRealDbFieldType() {
        if (this.dbFieldType == TypeSQL.DEFAULT) {
            return TypeSQL.fromJavaType(javaFieldType);
        }
        return this.dbFieldType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getJavaFieldName() {
        return this.javaFieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aJavaFieldName DOCUMENT ME!
     */
    public final void setJavaFieldName(final String aJavaFieldName) {
        this.javaFieldName = aJavaFieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getDbFieldName() {
        return this.dbFieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDbFieldName DOCUMENT ME!
     */
    public final void setDbFieldName(final String aDbFieldName) {
        this.dbFieldName = aDbFieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Class<?> getJavaFieldType() {
        return this.javaFieldType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aJavaFieldType DOCUMENT ME!
     */
    public final void setJavaFieldType(final Class<?> aJavaFieldType) {
        this.javaFieldType = aJavaFieldType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final TypeSQL getDbFieldType() {
        return this.dbFieldType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDbFieldType DOCUMENT ME!
     */
    public final void setDbFieldType(final TypeSQL aDbFieldType) {
        this.dbFieldType = aDbFieldType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Method getSetter() {
        return this.setter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aSetter DOCUMENT ME!
     */
    public final void setSetter(final Method aSetter) {
        this.setter = aSetter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Method getGetter() {
        return this.getter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aGetter getter
     */
    public final void setGetter(final Method aGetter) {
        this.getter = aGetter;
    }

    /**
	 * @return the primaryKey
	 */
    public final boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
	 * @param aPrimaryKey the primaryKey to set
	 */
    public final void setPrimaryKey(final boolean aPrimaryKey) {
        this.primaryKey = aPrimaryKey;
    }

    /**
	 * @return the persistanceFlag
	 */
    public final boolean isPersistanceFlag() {
        return this.persistanceFlag;
    }

    /**
	 * @param aPersistanceFlag the persistanceFlag to set
	 */
    public final void setPersistanceFlag(final boolean aPersistanceFlag) {
        this.persistanceFlag = aPersistanceFlag;
    }

    /**
	 * @return
	 */
    public Map<DescriptionCompositeType, DescriptionComposite> getDescComposites() {
        return null;
    }

    /**
	 * @return
	 */
    public String getName() {
        return null;
    }

    /**
	 * @param type
	 * @param comp
	 */
    public void addComposite(DescriptionCompositeType type, DescriptionComposite comp) {
    }

    /**
	 * @param type
	 * @return
	 */
    public DescriptionComposite getComposite(DescriptionCompositeType type) {
        return null;
    }
}
