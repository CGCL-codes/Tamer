package com.sri.emo.dbobj;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.DBField;

/**
 * encapsulate an association between two attributes,
 * usually used, in PADI, between Additional KSAs and Variable Features
 *
 * @author larry hamel
 */
public class AttributeAssociation extends DBObject {

    public static final String SRC_ID = "SRC_ID";

    public static final String DEST_ID = "DEST_ID";

    public static final String COMMENT = "COMMENT";

    public static final String TABLE_NAME = "attribute_assoc";

    /**
     * Default constructor for <code>Attribute</code>
     * creates a new object of this type with no connection
     * yet allocated.
     *
     * @throws com.jcorporate.expresso.core.db.DBException
     *          If the new object cannot be
     *          created
     */
    public AttributeAssociation() throws DBException {
    }

    public AttributeAssociation(String srcId, String destId, String comment) throws DBException {
        setField(SRC_ID, srcId);
        setField(DEST_ID, destId);
        setField(COMMENT, comment);
    }

    /**
     * Defines the database table name and fields for this DB object
     *
     * @throws com.jcorporate.expresso.core.db.DBException
     *          if the operation cannot be performed
     */
    protected synchronized void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription("Association between attributes within a node.");
        addField(SRC_ID, "int", 0, false, "foreign key to association table");
        addField(DEST_ID, "int", 0, false, "foreign key to association table");
        addField(COMMENT, DBField.LONGVARCHAR_TYPE, 0, true, "Comment");
        addKey(SRC_ID);
        addKey(DEST_ID);
    }

    public String getSrcId() throws DBException {
        return getField(SRC_ID);
    }

    public String getDestId() throws DBException {
        return getField(DEST_ID);
    }

    public String getComment() throws DBException {
        return getField(COMMENT);
    }

    public void setComment(String cmt) throws DBException {
        setField(COMMENT, cmt);
    }

    public void setSrcId(String attribId) throws DBException {
        setField(SRC_ID, attribId);
    }

    public void updateReflexive() throws DBException {
        AttributeAssociation refl = new AttributeAssociation(getDestId(), getSrcId(), getComment());
        refl.addOrUpdate();
    }

    public void deleteReflexive() throws DBException {
        AttributeAssociation refl = new AttributeAssociation(getDestId(), getSrcId(), null);
        refl.delete();
    }

    public void setDestId(String attribId) throws DBException {
        setField(DEST_ID, attribId);
    }

    public Attribute getDestAttribute() throws DBException {
        Attribute attrib = new Attribute(getDestId());
        attrib.retrieve();
        return attrib;
    }
}
