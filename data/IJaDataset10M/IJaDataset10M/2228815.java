package si.cit.eprojekti.ehelp;

import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.dbobj.Schema;

/**
 * @author gpolancic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HelpSchema extends Schema {

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.ehelp");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.ehelp");

    /**
	 * Constructor
	 */
    public HelpSchema() {
        super();
        try {
            addDBObject(si.cit.eprojekti.ehelp.dbobj.Question.class);
            addDBObject(si.cit.eprojekti.ehelp.dbobj.Category.class);
            addDBObject(si.cit.eprojekti.ehelp.dbobj.ProjectData.class);
            addDBObject(si.cit.eprojekti.ehelp.dbobj.AllowedUser.class);
            addController(si.cit.eprojekti.ehelp.controller.HelpController.class);
            String schemaClass = this.getClass().getName();
            addSetup(schemaClass, "subjectLenght", "Default lenght of question subject", "40");
            addSetup(schemaClass, "displayRecords", "Default number of records beeing displayed", "20");
            addSetup(schemaClass, "UseAdvancedSecurity", "Check individual permission on categories? (Y/N)", "N");
        } catch (Exception e) {
            if (standardLog.isEnabledFor(Priority.WARN)) standardLog.warn(" :: Exception in \"" + this.getClass().getName() + "\" : " + e.toString());
            if (debugLog.isDebugEnabled()) debugLog.debug(" :: Exception in \"" + this.getClass().getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
        }
    }

    public String getMessageBundlePath() {
        return "si/cit/eprojekti/ehelp";
    }

    public String getDefaultDescription() {
        return getString("eHelpDescription");
    }

    public String getDefaultComponentCode() {
        return "ehelp";
    }

    public String getVersion() {
        return getString(" 0.1 ");
    }
}
