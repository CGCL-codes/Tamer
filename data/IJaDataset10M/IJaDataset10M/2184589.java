package si.cit.eprojekti.eproject.dbobj;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBField;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.8.25 20:18:46
 * 
 * Deliverable description:
 *	Holding core Deliverable data.
 */
public class RDeliverable extends Deliverable {

    private static final long serialVersionUID = 23134546488887987L;

    public static String TABLE_NAME = "epj_eprjct_dlvr_r";

    public static String TABLE_DESCRIPTION = "RDeliverableTable";

    /**
	 * @throws DBException
	 */
    public RDeliverable() throws DBException {
        super();
    }

    public static final String FLD_DATE = "Date";

    public static final String FLD_REALIZED = "Realized";

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#setupFields()
	 */
    public void setupFields() throws DBException {
        super.setupFields();
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        addField(FLD_DATE, DBField.DATETIME_TYPE, 0, false, "eProjectRDeliverableDate");
        addField(FLD_REALIZED, DBField.DOUBLE_TYPE, 0, false, "eProjectRDeliverableRealized");
        setCharset("UTF-8");
    }
}
