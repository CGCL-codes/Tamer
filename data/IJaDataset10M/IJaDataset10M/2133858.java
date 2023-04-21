package si.cit.eprojekti.usermanagement.dbobj;

import com.jcorporate.expresso.core.db.DBException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.8.16 13:23:37
 * 
 * SubjectAttribute description:
 *	Holding Natural Subject Attribute's Values
 */
public class NaturalSubjectAttribute extends SubjectAttribute {

    private static final long serialVersionUID = 23134546488887987L;

    public static String TABLE_NAME = "EPJ_UM_NSUB_ATT";

    public static String TABLE_DESCRIPTION = "NSubjectAttributeTable";

    /**
	 * @throws DBException
	 */
    public NaturalSubjectAttribute() throws DBException {
        super();
    }

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#getValidValues(java.lang.String)
	 */
    public synchronized Vector getValidValues(String fieldName) throws DBException {
        if (fieldName.equals(FLD_ATTRIBUTE)) return ValidNaturalSubjectAttribute.getAllValidAttributes();
        return super.getValidValues(fieldName);
    }

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#setupFields()
	 */
    public void setupFields() throws DBException {
        super.setupFields();
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        setCharset("UTF-8");
    }

    /**
	 * Get Full attribute name
	 * @return This attributes attribute name
	 * @throws DBException
	 */
    public String getAttributFullName() throws DBException {
        ValidNaturalSubjectAttribute v = new ValidNaturalSubjectAttribute();
        v.setField(ValidNaturalSubjectAttribute.FLD_SHORT_NAME, getField(FLD_ATTRIBUTE));
        ArrayList temp = v.searchAndRetrieveList();
        if (temp.size() > 0) return ((ValidNaturalSubjectAttribute) temp.get(0)).getField(ValidNaturalSubjectAttribute.FLD_NAME);
        return "";
    }

    /**
	 * Get value
	 * @return Value of this attribute
	 * @throws DBException
	 */
    public String getValue() throws DBException {
        return getField(FLD_VALUE);
    }
}
