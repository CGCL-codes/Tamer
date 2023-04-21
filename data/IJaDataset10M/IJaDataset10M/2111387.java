package evolaris.platform.admin.web.action;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import evolaris.framework.async.business.SmsDbManager;
import evolaris.framework.async.datamodel.AllowedInvocation;
import evolaris.framework.sys.web.action.EnterEditDuplicateAction;

/**
 * Class handling allowed invocation entries
 * @author richard.hable
 * @date 2008-02-21
 */
public class AllowedInvocationEnterOrEditAction extends EnterEditDuplicateAction<AllowedInvocation> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(AllowedInvocationEnterOrEditAction.class);

    /**
	 *  
	 * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
	 */
    @SuppressWarnings("unchecked")
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("admin.Create", "create");
        map.put("admin.Modify", "modify");
        map.put("admin.Delete", "delete");
        return map;
    }

    /**
	 * Reads the allowedInvocation entry according to the ID from the database
	 * @param id  allowedInvocation ID
	 * @return allowedInvocation entry; null if not found
	 */
    @Override
    protected AllowedInvocation entryFromDatabase(long id) {
        SmsDbManager smsDbManager = new SmsDbManager(locale, session);
        return smsDbManager.getAllowedInvocation(id);
    }
}
