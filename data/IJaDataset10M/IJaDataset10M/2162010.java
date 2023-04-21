package si.cit.eprojekti.edocs.dbobj;

import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.misc.DateTime;

/**
 * 
 * @author taks
 * @version 1.0
 * 
 * 2004.5.12
 * 
 */
public class DocumentHistory extends SecuredDBObject {

    private static final long serialVersionUID = 2906988561701159433L;

    private String thisClass = new String(getClass().getName() + ".");

    private static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(DocumentHistory.class);

    public static String TABLE_NAME = "ePj_docs_docHis";

    public static String TABLE_DESCRIPTION = "eDocsDocumentHistoryTable";

    /**
	 * Constructor
	 * @throws DBException
	 */
    public DocumentHistory() throws DBException {
        super();
    }

    /**
	 * Constructor
	 * @param myConnection
	 * @throws DBException
	 */
    public DocumentHistory(DBConnection myConnection) throws DBException {
        super(myConnection);
    }

    /**
	 * Override the method add() and add the record
	 * @throws  DBException
	 */
    public void add() throws DBException {
        String myName = new String(thisClass + "add()");
        setField("Created", DateTime.getDateTimeForDB());
        super.add();
    }

    /**
	 * Override the method update() and update the record
	 * @throws  DBException
	 */
    public void update() throws DBException {
        String myName = new String(thisClass + "update()");
        super.update();
    }

    public void add(int uid) throws DBException {
        setField("CreatedUid", uid);
        add();
    }

    /**
	 * Return this DBObject
	 * @return  DBObject
	 * @throws  DBException
	 */
    public DBObject getThisDBObj() throws DBException {
        return new DocumentHistory();
    }

    /**
	 * Setup fields
	 * @throws  DBException
	 */
    public void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        setCharset("utf-8");
        addField("HistoryId", "auto-inc", 0, false, "eDocsDocumentHistoryTableHistoryId");
        addField("DocumentIdOld", "int", 0, false, "eDocsDocumentHistoryTableDocumentIdOld");
        addField("DocumentIdNew", "int", 0, false, "eDocsDocumentHistoryTableDocumentIdNew");
        addField("Created", "datetime", 0, true, "Created");
        addField("CreatedUid", "int", 0, true, "CreatedBy");
        addField("Coment", "text", 0, true, "eDocsDocumentHistoryTableComent");
        addKey("HistoryId");
        addKey("DocumentIdOld");
        addKey("DocumentIdNew");
        setReadOnly("HistoryId");
        setReadOnly("Created");
        setReadOnly("CreatedUid");
        setLookupObject("DocumentIdNew", "si.cit.eprojekti.edocs.dbobj.Document");
        setLookupObject("DocumentIdOld", "si.cit.eprojekti.edocs.dbobj.Document");
    }
}
