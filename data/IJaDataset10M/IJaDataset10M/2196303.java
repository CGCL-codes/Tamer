package si.cit.eprojekti.enews.dbobj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import si.cit.eprojekti.projectvianet.util.link.ICategory;
import si.cit.eprojekti.projectvianet.util.link.IElement;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.ISOValidValue;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.services.dbobj.Setup;
import java.io.*;

/**
 *	
 *	Main DB Object for News storage
 *
 * 	@author taks
 *	@version 1.0
 *
 */
public class News extends SecuredDBObject implements IElement {

    private static final long serialVersionUID = -7595417785258920525L;

    private String thisClass = new String(getClass().getName() + ".");

    private static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(News.class);

    public static String TABLE_NAME = "ePj_enws_news";

    public static String TABLE_DESCRIPTION = "eNewsNewsTable";

    /**
	 * @throws DBException
	 */
    public News() throws DBException {
        super();
    }

    /**
	 * @param myConnection
	 * @throws DBException
	 */
    public News(DBConnection myConnection) throws DBException {
        super(myConnection);
    }

    public void delete() throws DBException {
        String myName = new String(thisClass + "delete()");
        super.delete();
    }

    public void add() throws DBException {
        String myName = new String(thisClass + "add()");
        setField("DateCreated", DateTime.getDateTimeForDB());
        setField("DateUpdated", DateTime.getDateTimeForDB());
        setField("DateApproved", DateTime.getDateTimeForDB());
        setField("NewsApproved", "T");
        super.add();
    }

    public void update() throws DBException {
        String myName = new String(thisClass + "update()");
        setField("DateUpdated", DateTime.getDateTimeForDB());
        super.update();
    }

    public void update(int uid) throws DBException {
        setField("UpdatedUid", uid);
        update();
    }

    public void add(int uid) throws DBException {
        setField("CreatedUid", uid);
        setField("UpdatedUid", uid);
        setField("ApprovedUid", uid);
        add();
    }

    public DBObject getThisDBObj() throws DBException {
        return new News();
    }

    public synchronized Vector getValidValues(String fieldName) throws DBException {
        Locale locale;
        locale = this.getLocale();
        String schemaClass = "si.cit.eprojekti.enews.NewsSchema";
        String prefix = this.getClass().getName();
        if (fieldName.equals("NewsVisible")) {
            Vector myValues = new Vector(5);
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "V", "Visible"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "H", "Hidden"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "N", "Neutral"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "1", "Sticky1"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "2", "Sticky2"));
            return myValues;
        }
        if (fieldName.equals("NewsApproved")) {
            Vector myValues = new Vector(2);
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "T", "Approved"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "F", "Disapproved"));
            return myValues;
        }
        if (fieldName.equals("NewsJobInclude")) {
            Vector myValues = new Vector(2);
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "T", "Included_in_Job"));
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "F", "NOT_included_in_Job"));
            return myValues;
        }
        if (fieldName.equals("CategoryId")) {
            Vector myValues = new Vector();
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "0", "RootCategory"));
            try {
                Category category = new Category();
                ArrayList allRecords = category.searchAndRetrieveList();
                Iterator i = allRecords.iterator();
                while (i.hasNext()) {
                    category = (Category) i.next();
                    myValues.addElement(new ValidValue(category.getField("CategoryId"), category.getField("CategoryName")));
                }
                return myValues;
            } catch (Exception e) {
                log.error("Error: " + e.getMessage());
            }
        }
        if (fieldName.equals("FileId")) {
            Vector myValues = new Vector();
            myValues.addElement(new ISOValidValue(schemaClass, locale, prefix, "0", "Blank"));
            try {
                NewsFiles newsFiles = new NewsFiles();
                newsFiles.addSortKey("Updated", false);
                ArrayList allRecords = newsFiles.searchAndRetrieveList();
                Iterator i = allRecords.iterator();
                String fileDir = Setup.getValue("", "BaseDir") + Setup.getValue("", "si.cit.eprojekti.enews.NewsSchema", "newsFileDir");
                while (i.hasNext()) {
                    newsFiles = (NewsFiles) i.next();
                    if (newsFiles.getField("FileVisible").equalsIgnoreCase("T")) {
                        File f1 = new File(fileDir + newsFiles.getField("ProjectId") + "/" + newsFiles.getField("FileName"));
                        if (f1.canRead()) {
                            myValues.addElement(new ValidValue(newsFiles.getField("FileId"), newsFiles.getField("FileName") + " (" + newsFiles.getField("FileTitle") + ")"));
                        }
                    }
                }
                return myValues;
            } catch (Exception e) {
                log.error("Error: " + e.getMessage());
            }
        }
        return super.getValidValues(fieldName);
    }

    public void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        setCharset("utf-8");
        addField("NewsId", "auto-inc", 0, false, "eNewsNewsTableNewsId");
        addField("CategoryId", "int", 0, false, "eNewsNewsTableCategoryId");
        addField("NewsTitle", "varchar", 130, false, "eNewsNewsTableTitle");
        addField("NewsBody", "text", 0, true, "eNewsNewsTableBody");
        addField("NewsSource", "varchar", 100, true, "eNewsNewsTableSource");
        addField("FileId", "int", 0, false, "eNewsNewsTableFileId");
        addField("NewsPresented", "date", 0, false, "eNewsNewsTablePresented");
        addField("NewsHidden", "date", 0, true, "eNewsNewsTableHidden");
        addField("NewsVisible", "char", 1, true, "eNewsNewsTableVisible");
        addField("NewsJobInclude", "char", 1, false, "eNewsNewsTableJobInclude");
        addField("NewsApproved", "char", 1, false, "eNewsNewsTableApprovedStatus");
        addField("DateCreated", "datetime", 0, true, "Created");
        addField("CreatedUid", "int", 0, true, "CreatedBy");
        addField("DateUpdated", "datetime", 0, true, "Updated");
        addField("UpdatedUid", "int", 0, true, "UpdatedBy");
        addField("DateApproved", "datetime", 0, false, "eNewsNewsTableApproved");
        addField("ApprovedUid", "int", 0, false, "eNewsNewsTableApprovedBy");
        addKey("NewsId");
        setReadOnly("DateCreated");
        setReadOnly("DateUpdated");
        setReadOnly("DateApproved");
        setReadOnly("CreatedUid");
        setReadOnly("UpdatedUid");
        setReadOnly("ApprovedUid");
        setReadOnly("NewsId");
        setReadOnly("NewsApproved");
        setMultiValued("NewsVisible");
        setMultiValued("NewsJobInclude");
        setMultiValued("CategoryId");
        setMultiValued("FileId");
        setMultiValued("NewsApproved");
        setDefaultValue("NewsPresented", DateTime.getDateForDB());
        setDefaultValue("NewsJobInclude", "T");
        setDefaultValue("NewsVisible", "N");
        setDefaultValue("CategoryId", "0");
        setDefaultValue("NewsApproved", "T");
        setDefaultValue("FileId", "0");
        setStringFilter("NewsTitle", "rawFilter");
        setStringFilter("NewsBody", "rawFilter");
        setStringFilter("NewsSource", "rawFilter");
        addSortKey("DateUpdated", false);
        setLookupObject("CategoryId", "si.cit.eprojekti.enews.dbobj.Category");
        setLookupObject("FileId", "si.cit.eprojekti.enews.dbobj.NewsFiles");
    }

    public String getDateInSLOFormat(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR));
    }

    /**
		 * @see com.jcorporate.expresso.core.dbobj.DBObject#getID()
		 */
    public int getElementID() throws Exception {
        return getFieldInt("NewsId");
    }

    /**
		 * @see com.jcorporate.expresso.core.dbobj.DBObject#getName()
		 */
    public String getElementName() throws Exception {
        return getField("NewsTitle");
    }

    /**
		 * @see com.jcorporate.expresso.core.dbobj.DBObject#getDescription()
		 */
    public String getElementDescription() throws Exception {
        return getField("NewsBody");
    }

    /**
		 * @see com.jcorporate.expresso.core.dbobj.DBObject#getCategory()
		 */
    public ICategory getElementCategory() throws Exception {
        if (getField("CategoryId").equals("0")) return null;
        Category ret = new Category();
        ret.setField("CategoryId", getField("CategoryId"));
        if (ret.count() != 0) return (Category) ret.searchAndRetrieveList().get(0); else return null;
    }

    /**
		 * @see com.jcorporate.expresso.core.dbobj.DBObject#matchesPattern()
		 */
    public boolean elementMatchesPattern(String pattern) {
        return false;
    }
}
