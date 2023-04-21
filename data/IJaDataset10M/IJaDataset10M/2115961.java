package transversion;

import oracle.jbo.server.*;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.Key;
import oracle.jbo.server.util.*;

public class TvupgradePhasesImpl extends EntityImpl {

    protected static final int SEQNO = 0;

    protected static final int DESCRIPTION = 1;

    protected static final int FILENAME = 2;

    protected static final int TVSTEPTYPES = 3;

    protected static final int TVUPGRADESTEPS = 4;

    private static EntityDefImpl mDefinitionObject;

    /**
   * This is the default constructor (do not remove)
   */
    public TvupgradePhasesImpl() {
    }

    /**
   * Retrieves the definition object for this instance class.
   */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = (EntityDefImpl) EntityDefImpl.findDefObject("transversion.TvupgradePhases");
        }
        return mDefinitionObject;
    }

    /**
   * Gets the attribute value for Seqno, using the alias name Seqno
   */
    public Number getSeqno() {
        return (Number) getAttributeInternal(SEQNO);
    }

    /**
   * Sets <code>value</code> as the attribute value for Seqno
   */
    public void setSeqno(Number value) {
        setAttributeInternal(SEQNO, value);
    }

    /**
   * Gets the attribute value for Description, using the alias name Description
   */
    public String getDescription() {
        return (String) getAttributeInternal(DESCRIPTION);
    }

    /**
   * Sets <code>value</code> as the attribute value for Description
   */
    public void setDescription(String value) {
        setAttributeInternal(DESCRIPTION, value);
    }

    /**
   * Gets the attribute value for FileName, using the alias name FileName
   */
    public String getFileName() {
        return (String) getAttributeInternal(FILENAME);
    }

    /**
   * Sets <code>value</code> as the attribute value for FileName
   */
    public void setFileName(String value) {
        setAttributeInternal(FILENAME, value);
    }

    /**
   * Gets the associated entity oracle.jbo.RowIterator
   */
    public RowIterator getTvstepTypes() {
        return (oracle.jbo.RowIterator) getAttributeInternal(TVSTEPTYPES);
    }

    /**
   * Gets the associated entity oracle.jbo.RowIterator
   */
    public RowIterator getTvupgradeSteps() {
        return (oracle.jbo.RowIterator) getAttributeInternal(TVUPGRADESTEPS);
    }

    /**
   * Creates a Key object based on given key constituents
   */
    public static Key createPrimaryKey(Number seqno) {
        return new Key(new Object[] { seqno });
    }
}
