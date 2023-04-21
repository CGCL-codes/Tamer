package si.cit.eprojekti.ecalendar.dbobj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.dbobj.ValidValue;

/**
 * 
 * @author taks
 * @version 1.0
 * 
 * 2004.8.16
 * 
 */
public class AssetParticipants extends SecuredDBObject {

    private static final long serialVersionUID = 93626059525789332L;

    private String thisClass = getClass().getName() + ".";

    private static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(AssetParticipants.class);

    public static String TABLE_NAME = "ePj_ecal_assetPar";

    public static String TABLE_DESCRIPTION = "eCalendarAssetParticipantsTable";

    /**
	 * Constructor
	 * @throws DBException
	 */
    public AssetParticipants() throws DBException {
        super();
    }

    /**
	 * Constructor
	 * @param  request
	 * @throws DBException
	 */
    public AssetParticipants(ControllerRequest request) throws DBException {
        super(SecuredDBObject.SYSTEM_ACCOUNT);
        setDBName(request.getDBName());
    }

    /**
	 * Constructor
	 * @param int
	 * @throws DBException
	 */
    public AssetParticipants(int uid) throws DBException {
        super(uid);
    }

    /**
	* Override the method getValues to provide specific values for our
	* multi-valued fields
	*
	* @param    fieldName Fielname to retrieve values for
	* @returns    Vector of ValidValue Value/description pairs for this field
	* @throws    DBException If the values cannot be retrieved
	*/
    public synchronized Vector getValidValues(String fieldName) throws DBException {
        String myName = new String(thisClass + "getValues(String)");
        Locale locale;
        locale = this.getLocale();
        if (fieldName.equals("CalAssetId")) {
            Vector myValues = new Vector();
            try {
                Asset asset = new Asset();
                ArrayList allRecords = asset.searchAndRetrieveList();
                Iterator allRecordIterAsset = allRecords.iterator();
                while (allRecordIterAsset.hasNext()) {
                    asset = (Asset) allRecordIterAsset.next();
                    myValues.addElement(new ValidValue(asset.getField("CalAssetId"), asset.getField("CalAssetTitle")));
                }
                return myValues;
            } catch (Exception e) {
                log.warn("Napaka pri pridobivanju zapisov!");
                log.error("Napaka: " + e.getMessage());
            }
        }
        return super.getValidValues(fieldName);
    }

    /**
	 * Setup fields
	 * @throws  DBException
	 */
    protected void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        setCharset("utf-8");
        addField("CalAssetId", "int", 0, false, "eCalendarAssetParticipantsTableAssetId");
        addField("UserId", "int", 0, false, "eCalendarAssetParticipantsTableUserId");
        addKey("CalAssetId");
        addKey("UserId");
        setMultiValued("CalAssetId");
        setMultiValued("UserId");
        setDefaultValue("UserId", String.valueOf(getRequestingUid()));
        setLookupObject("CalAssetId", "si.cit.eprojekti.ecalendar.dbobj.Asset");
        setLookupObject("UserId", "com.jcorporate.expresso.services.dbobj.DefaultUserInfo");
    }

    public boolean isUserMemberOfAsset(int userUid, int calAssetId) {
        try {
            setField("CalAssetId", String.valueOf(calAssetId));
            setField("UserId", String.valueOf(userUid));
            if (count() == 0) return false; else return true;
        } catch (DBException e) {
            return false;
        }
    }

    public static Vector getAllParticipants(String assetId) throws DBException {
        Vector v = new Vector();
        AssetParticipants ap = new AssetParticipants();
        ap.setField("CalAssetId", assetId);
        ArrayList array = ap.searchAndRetrieveList();
        Iterator iter = array.iterator();
        while (iter.hasNext()) {
            ap = (AssetParticipants) iter.next();
            v.addElement(ap.getField("UserId"));
        }
        return v;
    }
}
