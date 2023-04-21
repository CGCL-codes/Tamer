package net.teqlo.components.salesforce.salesforceV0_1;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import net.teqlo.TeqloException;
import net.teqlo.components.Activity;
import net.teqlo.components.standard.javascriptV0_01.AbstractScriptComponent;
import net.teqlo.components.standard.microformatsV0_2.MicroformatExecutor;
import net.teqlo.components.standard.xmlDbV2_0.DbExecutor;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.ExecutorLookup;
import net.teqlo.db.User;
import net.teqlo.db.UserDocument;
import net.teqlo.runtime.Context;
import net.teqlo.runtime.Service;
import net.teqlo.xml.TeqloXmlValue;
import net.teqlo.xml.XmlConstantsV10c;

public class SalesforceExecutor extends MicroformatExecutor {

    public static final String SALESFORCE_UID_PREF = "uid";

    public static final String SALESFORCE_PWD_PREF = "pwd";

    private static final String SCRIPT_FOLDER_PREF = "scriptFolder";

    private static final String SALESFORCE_ACTION = "salesforce.action";

    private static final String INSERT_ACTION = "insert";

    private static final String REMOVE_ACTION = "remove";

    private static final String UPDATE_ACTION = "update";

    private static final String SYNC_ALL_ACTION = "syncAll";

    private static final String SYNC_CHANGED_ACTION = "syncChanged";

    private static final String SYNC_ALL_OBSOLETE_ACTION = "syncAllObsolete";

    public static final String SALESFORCE_KEY_PARAM = "salesforceKey";

    public static final String SALESFORCE_SYNC_ID_PARAM = "salesforceSyncId";

    private static final String DECLARE_NS = "declare namespace tq = '" + XmlConstantsV10c.TEQLO_NAMESPACE_URI + "';\n";

    private static final String GET_RECORD = "//tq:*[@uuid][.//tq:userField[@rel eq 'salesforceKey'][text() eq $salesforceKey]]";

    private static final String GET_OBSOLETE_UUIDS = "//tq:*[@uuid][.//tq:userField[@rel eq 'salesforceSyncId'][text() ne $salesforceSyncId]]/@uuid";

    /**
	 * @param component
	 * @param service
	 * @param el
	 * @throws TeqloException
	 */
    public SalesforceExecutor(AbstractScriptComponent component, Service service, ExecutorLookup el) throws TeqloException {
        super(component, service, el);
    }

    @Override
    public Activity createActivity(Context context, ActivityLookup al) throws TeqloException {
        String action = al.getAttributes().getAttributeValue(SALESFORCE_ACTION);
        if (action == null) throw new TeqloException(this, SALESFORCE_ACTION, null, "This activity attribute must be specified");
        User user = context.getUser();
        if (action.equalsIgnoreCase(INSERT_ACTION)) return new SalesforceInsertActivity(user, this, al); else if (action.equalsIgnoreCase(REMOVE_ACTION)) return new SalesforceRemoveActivity(user, this, al); else if (action.equalsIgnoreCase(UPDATE_ACTION)) return new SalesforceUpdateActivity(user, this, al); else if (action.equalsIgnoreCase(SYNC_ALL_ACTION)) return new SalesforceSyncAllActivity(user, this, al); else if (action.equalsIgnoreCase(SYNC_CHANGED_ACTION)) return new SalesforceSyncChangedActivity(user, this, al); else if (action.equalsIgnoreCase(SYNC_ALL_OBSOLETE_ACTION)) return new SalesforceSyncAllObsoleteActivity(user, this, al); else throw new TeqloException(this, action, null, "This action is not recognized");
    }

    /**
	 * Returns the XML string holding the microformat record from our partner database that contains the specified
	 * salesforce key.
	 * @param salesforceKey to be retrieved
	 * @return String XML record
	 * @throws TeqloException
	 */
    public String getMicroformatRecord(User user, String salesforceKey) throws TeqloException {
        String dbExecutorFqn = this.el.getAttributes().getAttributeValue(PARTNER_ATTRIBUTE);
        DbExecutor dbExecutor = (DbExecutor) this.service.getExecutor(dbExecutorFqn);
        UserDocument userDocument = dbExecutor.getDocument(user);
        Map<String, TeqloXmlValue> params = new HashMap<String, TeqloXmlValue>();
        params.put(SALESFORCE_KEY_PARAM, new TeqloXmlValue(salesforceKey));
        Vector<TeqloXmlValue> result = userDocument.xquery(DECLARE_NS + GET_RECORD, params);
        if (result.size() == 0) {
            return null;
        } else if (result.size() == 1) {
            String resultString = result.get(0).getValue().toString();
            return resultString;
        } else throw new TeqloException(this, salesforceKey, null, "This salesforce key matched >1 record");
    }

    /**
	 * Returns all uuids of records whose salesforce sync id is not equal to the supplied sync id.
	 * @param user
	 * @param salesforceSyncId representing the latest (ie up to date) sync
	 * @return Vector of String uuids
	 * @throws TeqloException if any error occurs
	 */
    public Vector<TeqloXmlValue> getObsoleteUUIDs(User user, String salesforceSyncId) throws TeqloException {
        String dbExecutorFqn = this.el.getAttributes().getAttributeValue(PARTNER_ATTRIBUTE);
        DbExecutor dbExecutor = (DbExecutor) this.service.getExecutor(dbExecutorFqn);
        UserDocument userDocument = dbExecutor.getDocument(user);
        Map<String, TeqloXmlValue> params = new HashMap<String, TeqloXmlValue>();
        params.put(SALESFORCE_SYNC_ID_PARAM, new TeqloXmlValue(salesforceSyncId));
        Vector<TeqloXmlValue> result = userDocument.xquery(DECLARE_NS + GET_OBSOLETE_UUIDS, params);
        return result;
    }

    /**
	 * Return the app or owner pref specifying script folder (note we do not supply user, so admin is assumed)
	 * @return String script folder ending in '/'
	 * @throws TeqloException
	 */
    public String getScriptFolder() throws TeqloException {
        return this.getPrefValue(this.getOwner(), SalesforceExecutor.SCRIPT_FOLDER_PREF);
    }

    @Override
    public String queryExecutor(User user, String queryString, Map<String, String> parameters) throws TeqloException {
        return super.queryExecutor(user, queryString, parameters);
    }
}
