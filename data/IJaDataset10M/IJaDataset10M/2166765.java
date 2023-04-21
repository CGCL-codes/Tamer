package si.unimb.isportal07.iiWiki;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.Schema;

public class WikiSchema extends Schema {

    private static final long serialVersionUID = 1L;

    private String thisClass = new String(this.getClass().getName() + ".");

    public WikiSchema() throws DBException {
        super();
        addDBObject(si.unimb.isportal07.iiWiki.dbobj.Kategorije.class);
        addDBObject(si.unimb.isportal07.iiWiki.dbobj.Clanek.class);
        addController(si.unimb.isportal07.iiWiki.controller.WikiController.class);
    }

    public String getDefaultDescription() {
        return this.thisClass;
    }

    public String getVersion() {
        return "0.1alpha";
    }

    public String getMessageBundlePath() {
        return "si/unimb/isportal07/iiWiki/i18n";
    }
}
