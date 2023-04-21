package transversion;

import oracle.jbo.server.*;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.Key;
import oracle.jbo.server.util.*;

public class TvmodificationDetailsImpl extends EntityImpl {

    protected static final int DIFID = 0;

    protected static final int MODCOMMENT = 1;

    protected static final int NEWVALUE = 2;

    protected static final int OLDVALUE = 3;

    protected static final int MODTYPE = 4;

    protected static final int TVDIFFERENCETYPES = 5;

    protected static final int TVDIFFERENCES = 6;

    private static EntityDefImpl mDefinitionObject;

    /**
   * This is the default constructor (do not remove)
   */
    public TvmodificationDetailsImpl() {
    }

    /**
   * Retrieves the definition object for this instance class.
   */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = (EntityDefImpl) EntityDefImpl.findDefObject("transversion.TvmodificationDetails");
        }
        return mDefinitionObject;
    }

    /**
   * Gets the attribute value for DifId, using the alias name DifId
   */
    public Number getDifId() {
        return (Number) getAttributeInternal(DIFID);
    }

    /**
   * Sets <code>value</code> as the attribute value for DifId
   */
    public void setDifId(Number value) {
        setAttributeInternal(DIFID, value);
    }

    /**
   * Gets the attribute value for ModComment, using the alias name ModComment
   */
    public String getModComment() {
        return (String) getAttributeInternal(MODCOMMENT);
    }

    /**
   * Sets <code>value</code> as the attribute value for ModComment
   */
    public void setModComment(String value) {
        setAttributeInternal(MODCOMMENT, value);
    }

    /**
   * Gets the attribute value for NewValue, using the alias name NewValue
   */
    public String getNewValue() {
        return (String) getAttributeInternal(NEWVALUE);
    }

    /**
   * Sets <code>value</code> as the attribute value for NewValue
   */
    public void setNewValue(String value) {
        setAttributeInternal(NEWVALUE, value);
    }

    /**
   * Gets the attribute value for OldValue, using the alias name OldValue
   */
    public String getOldValue() {
        return (String) getAttributeInternal(OLDVALUE);
    }

    /**
   * Sets <code>value</code> as the attribute value for OldValue
   */
    public void setOldValue(String value) {
        setAttributeInternal(OLDVALUE, value);
    }

    /**
   * Gets the attribute value for ModType, using the alias name ModType
   */
    public String getModType() {
        return (String) getAttributeInternal(MODTYPE);
    }

    /**
   * Sets <code>value</code> as the attribute value for ModType
   */
    public void setModType(String value) {
        setAttributeInternal(MODTYPE, value);
    }

    /**
   * Gets the associated entity TvdifferenceTypesImpl
   */
    public TvdifferenceTypesImpl getTvdifferenceTypes() {
        return (TvdifferenceTypesImpl) getAttributeInternal(TVDIFFERENCETYPES);
    }

    /**
   * Sets <code>value</code> as the associated entity TvdifferenceTypesImpl
   */
    public void setTvdifferenceTypes(TvdifferenceTypesImpl value) {
        setAttributeInternal(TVDIFFERENCETYPES, value);
    }

    /**
   * Gets the associated entity TvdifferencesImpl
   */
    public TvdifferencesImpl getTvdifferences() {
        return (TvdifferencesImpl) getAttributeInternal(TVDIFFERENCES);
    }

    /**
   * Sets <code>value</code> as the associated entity TvdifferencesImpl
   */
    public void setTvdifferences(TvdifferencesImpl value) {
        setAttributeInternal(TVDIFFERENCES, value);
    }

    /**
   * Creates a Key object based on given key constituents
   */
    public static Key createPrimaryKey(Number difId, String modType) {
        return new Key(new Object[] { difId, modType });
    }
}
