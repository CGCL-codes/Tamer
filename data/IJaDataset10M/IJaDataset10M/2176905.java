package si.unimb.isportal07.iiContent.dbobj;

import java.util.Locale;
import java.util.Vector;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBField;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.services.dbobj.MediaDBObject;

public class Content extends MediaDBObject {

    private static final long serialVersionUID = 3690474740435399734L;

    private String thisClass = new String(getClass().getName() + ".");

    public static final String CONTENT_ID = "ContentId";

    public static final String CATEGORY_ID = "CategoryId";

    public static final String CONTENT = "Content";

    public static final String DATE_CREATED = "DateCreated";

    public static final String CREATED_UID = "CreatedUid";

    public static final String DATE_MODIFIED = "DateModified";

    public static final String MODIFIED_UID = "ModifiedUid";

    public static final String AUTHOR = "Author";

    public static final String URL = "URL";

    public Content() throws DBException {
        super();
    }

    public Content(DBConnection myConnection) throws DBException {
        super(myConnection);
    }

    public void delete() throws DBException {
        super.delete();
    }

    public void add() throws DBException {
        String myName = new String(thisClass + "add()");
        setField(DATE_CREATED, DateTime.getDateTimeForDB());
        setField(CREATED_UID, getRequestingUid());
        setField(DATE_MODIFIED, DateTime.getDateTimeForDB());
        setField(MODIFIED_UID, getRequestingUid());
        super.add();
    }

    public void add(int Uid) throws DBException {
        String myName = new String(thisClass + "add()");
        setField(DATE_CREATED, DateTime.getDateTimeForDB());
        setField(CREATED_UID, Uid);
        setField(DATE_MODIFIED, DateTime.getDateTimeForDB());
        setField(MODIFIED_UID, Uid);
        super.add();
    }

    public void update() throws DBException {
        String myName = new String(thisClass + "update()");
        setField(DATE_MODIFIED, DateTime.getDateTimeForDB());
        setField(MODIFIED_UID, getRequestingUid());
        super.update();
    }

    public DBObject getThisDBObj() throws DBException {
        return new Content();
    }

    public synchronized Vector getValidValues(String fieldName) throws DBException {
        Locale locale = this.getLocale();
        String schemaClass = si.unimb.isportal07.iiContent.ContentSchema.class.getName();
        String prefix = this.getClass().getName();
        if (fieldName.equals(Content.CATEGORY_ID)) {
            Vector myValues = new Vector(2);
            myValues.addElement(new ValidValue("1", "Category 1"));
            myValues.addElement(new ValidValue("2", "Category 2"));
            myValues.addElement(new ValidValue("3", "Category 3"));
            myValues.addElement(new ValidValue("4", "Category 4"));
            myValues.addElement(new ValidValue("5", "Category 5"));
            return myValues;
        }
        return super.getValidValues(fieldName);
    }

    public void setupFields() throws DBException {
        setTargetTable("isportal07_iiContent_Content");
        setDescription(this.getClass().getName() + " table");
        setCharset("utf-8");
        addField(Content.CONTENT_ID, DBField.AUTOINC_TYPE, 0, false, "Identificator");
        addField(Content.CATEGORY_ID, DBField.INT_TYPE, 0, false, "Category id");
        addField(Content.CONTENT, DBField.TEXT_TYPE, 0, false, "Content");
        addField(Content.DATE_CREATED, DBField.DATETIME_TYPE, 0, false, "Date_created_on");
        addField(Content.CREATED_UID, DBField.INT_TYPE, 0, false, "Uid_created");
        addField(Content.DATE_MODIFIED, DBField.DATETIME_TYPE, 0, true, "Date_updated_on");
        addField(Content.MODIFIED_UID, DBField.INT_TYPE, 0, true, "Uid_updated");
        addField(Content.AUTHOR, DBField.VARCHAR_TYPE, 100, false, "Author of the content");
        addField(Content.URL, DBField.VARCHAR_TYPE, 100, false, "URL");
        addKey(Content.CONTENT_ID);
        setReadOnly(Content.DATE_CREATED);
        setReadOnly(Content.DATE_MODIFIED);
        setReadOnly(Content.CREATED_UID);
        setReadOnly(Content.MODIFIED_UID);
        setReadOnly(Content.CONTENT_ID);
        setMultiValued(Content.CATEGORY_ID);
        setDefaultValue(Content.CONTENT, "Add content here");
        setDefaultValue(Content.AUTHOR, "Add author here");
        setDefaultValue(Content.URL, "Add url here");
        setStringFilter(Content.CONTENT, "rawFilter");
        setStringFilter(Content.AUTHOR, "rawFilter");
        setStringFilter(Content.URL, "rawFilter");
        addSortKey(Content.CONTENT, false);
    }
}
