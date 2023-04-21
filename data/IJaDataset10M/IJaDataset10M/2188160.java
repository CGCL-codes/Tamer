package si.cit.eprojekti.eprocess.dbobj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.core.dbobj.DBField;
import com.jcorporate.expresso.core.security.filters.FilterManager;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.8.19 14:54:37
 * 
 * WorkFlow description:
 *	Defining all possible states for workflow activities and workflows
 */
public class ValidState extends SecuredDBObject {

    private static final long serialVersionUID = 23134546488887987L;

    public static String TABLE_NAME = "EPJ_EPCS_VALST";

    public static String TABLE_DESCRIPTION = "ActivityStateTable";

    /**
	 * @throws DBException
	 */
    public ValidState() throws DBException {
        super();
    }

    /**
	 * @see com.jcorporate.expresso.core.dataobjects.DataObject#add()
	 */
    public void add() throws DBException {
        setField("Created", DateTime.getDateTimeForDB());
        super.add();
    }

    /**
	 * @see com.jcorporate.expresso.core.dataobjects.DataObject#update()
	 */
    public void update() throws DBException {
        setField("Updated", DateTime.getDateTimeForDB());
        super.update();
    }

    public void add(int uid) throws DBException {
        setField("CreatedUID", uid);
        add();
    }

    public void update(int uid) throws DBException {
        setField("UpdatedUID", uid);
        update();
    }

    public static final String DEFAULT_MEENING_NOT_STARTED = "NS";

    public static final String DEFAULT_MEENING_STARTED = "ST";

    public static final String DEFAULT_MEENING_IN_PROGREES = "IP";

    public static final String DEFAULT_MEENING_FINISHED = "FI";

    public static final String FLD_CODE = "Code";

    public static final String FLD_NAME = "Name";

    public static final String FLD_DEFAULT_MEENING = "DefaultMeening";

    public static final String FLD_IS_WF_STATE = "IsWfState";

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#setupFields()
	 */
    public void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        addField(FLD_CODE, DBField.CHAR_TYPE, 8, false, "ActivityCode");
        addField(FLD_NAME, DBField.VARCHAR_TYPE, 30, false, "ActivityStateName");
        addField(FLD_DEFAULT_MEENING, DBField.CHAR_TYPE, 2, true, "ActivityStateDefaultMeening");
        addField(FLD_IS_WF_STATE, DBField.BOOLEAN_TYPE, 0, false, "ActivityStateWorkflowState");
        addField("Created", DBField.DATETIME_TYPE, 0, true, "Created");
        addField("CreatedUID", DBField.INT_TYPE, 0, true, "CreatedBy");
        addField("Updated", DBField.DATETIME_TYPE, 0, true, "Updated");
        addField("UpdatedUID", DBField.INT_TYPE, 0, true, "UpdatedBy");
        addKey(FLD_CODE);
        setSortKey(FLD_NAME);
        setMultiValued(FLD_DEFAULT_MEENING);
        setReadOnly("Created");
        setReadOnly("CreatedUID");
        setReadOnly("Updated");
        setReadOnly("UpdatedUID");
        setStringFiltersOnAll(FilterManager.STANDARD_FILTER);
        setCharset("UTF-8");
    }

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#populateDefaultValues()
	 */
    public synchronized void populateDefaultValues() throws DBException {
        ArrayList nms = new ArrayList();
        ArrayList cds = new ArrayList();
        getDefaultData(nms, cds);
        for (int i = 0; i < nms.size(); i++) {
            clear();
            String name = (String) nms.get(i);
            String defMen = null;
            if (name.startsWith(DEFAULT_MEENING_NOT_STARTED)) defMen = DEFAULT_MEENING_NOT_STARTED;
            if (name.startsWith(DEFAULT_MEENING_STARTED)) defMen = DEFAULT_MEENING_STARTED;
            if (name.startsWith(DEFAULT_MEENING_IN_PROGREES)) defMen = DEFAULT_MEENING_IN_PROGREES;
            if (name.startsWith(DEFAULT_MEENING_FINISHED)) defMen = DEFAULT_MEENING_FINISHED;
            name = name.substring(3);
            setField(FLD_CODE, (String) cds.get(i));
            if (!find()) {
                setField(FLD_NAME, name);
                setField(FLD_DEFAULT_MEENING, defMen);
                setField(FLD_IS_WF_STATE, ((String) cds.get(i)).startsWith("WF"));
                add();
            }
        }
    }

    /**
	 * Get default known states
	 * @param names Arraylist to be filled with names
	 */
    private void getDefaultData(ArrayList names, ArrayList codes) {
        names.add("FI COMPLETD");
        codes.add("COMPLETD");
        names.add("NS NOTSTART");
        codes.add("NOTSTART");
        names.add("ST INITIATD");
        codes.add("INITIATD");
        names.add("IP ACTIVEST");
        codes.add("ACTIVEST");
        names.add("NS WF_NTSTA");
        codes.add("WF_NTSTA");
        names.add("ST WF_START");
        codes.add("WF_START");
        names.add("IP WF_ACTIV");
        codes.add("WF_ACTIV");
        names.add("   WF_PAUSD");
        codes.add("WF_PAUSD");
        names.add("FI WF_CMPLT");
        codes.add("WF_CMPLT");
    }

    public static Vector getAllValidStates(boolean workflow) throws DBException {
        Vector ret = new Vector();
        ValidState c = new ValidState();
        ArrayList allRecs = c.searchAndRetrieveList();
        Iterator allRecsIterator = allRecs.iterator();
        while (allRecsIterator.hasNext()) {
            c = (ValidState) allRecsIterator.next();
            if (workflow == c.getFieldBoolean(FLD_IS_WF_STATE)) {
                ValidValue vv = new ValidValue(c.getField(FLD_CODE), c.getField(FLD_NAME));
                ret.add(vv);
            }
        }
        return ret;
    }

    public static Vector getAllValidActivityStates() throws DBException {
        return getAllValidStates(false);
    }

    public static Vector getAllValidWorkflowStates() throws DBException {
        return getAllValidStates(true);
    }

    public synchronized Vector getValidValues(String fieldName) throws DBException {
        if (fieldName.equals(FLD_DEFAULT_MEENING)) {
            Vector ret = new Vector();
            ret.add(new ValidValue(DEFAULT_MEENING_NOT_STARTED, "Not started"));
            ret.add(new ValidValue(DEFAULT_MEENING_STARTED, "Started"));
            ret.add(new ValidValue(DEFAULT_MEENING_IN_PROGREES, "In progress"));
            ret.add(new ValidValue(DEFAULT_MEENING_FINISHED, "Finished"));
            return ret;
        }
        return super.getValidValues(fieldName);
    }

    public static boolean hasBeenStarted(String stateCode) throws DBException {
        Vector ret = new Vector();
        ValidState c = new ValidState();
        ArrayList allRecs = c.searchAndRetrieveList();
        Iterator allRecsIterator = allRecs.iterator();
        while (allRecsIterator.hasNext()) {
            c = (ValidState) allRecsIterator.next();
            if (c.getField(FLD_CODE).equals(stateCode)) {
                if (!c.isFieldNull(FLD_DEFAULT_MEENING)) if (c.getField(FLD_DEFAULT_MEENING).equals(DEFAULT_MEENING_NOT_STARTED)) return false;
                return true;
            }
        }
        return false;
    }

    public static String getStateDefaultMeening(String stateCode) throws DBException {
        Vector ret = new Vector();
        ValidState c = new ValidState();
        c.setField(FLD_CODE, stateCode);
        if (!c.find()) return null;
        return c.getField(FLD_DEFAULT_MEENING);
    }

    public static String getDefaultWorkflowStateCode(String defMean) throws DBException {
        String ret = "";
        Vector temp = getAllValidWorkflowStates();
        for (Iterator iter = temp.iterator(); iter.hasNext(); ) {
            ValidValue el = (ValidValue) iter.next();
            ret = el.getValue();
            if (getStateDefaultMeening(ret).equals(defMean)) return ret;
        }
        return ret;
    }

    public static String getDefaultActivityStateCode(String defMean) throws DBException {
        String ret = "";
        Vector temp = getAllValidActivityStates();
        for (Iterator iter = temp.iterator(); iter.hasNext(); ) {
            ValidValue el = (ValidValue) iter.next();
            ret = el.getValue();
            if (getStateDefaultMeening(ret).equals(defMean)) return ret;
        }
        return ret;
    }

    public static ValidState getValidState(String code) throws DBException {
        ValidState vv = new ValidState();
        vv.setField(FLD_CODE, code);
        if (vv.find()) return vv;
        return null;
    }

    public static String getNameByCode(String code) throws DBException {
        ValidState vs = getValidState(code);
        if (vs != null) return vs.getField(FLD_NAME);
        return "";
    }
}
