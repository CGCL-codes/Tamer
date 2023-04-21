package net.sf.jgmail.datapack;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jgmail.DataPackException;
import net.sf.jgmail.GmailSession;

/**
 * @author todd
 */
public class CategoriesType implements Type {

    GmailSession session;

    /**
    * 
    */
    public CategoriesType(GmailSession session) {
        this.session = session;
    }

    public void parse(List array) throws DataPackException {
        Map sl = session.getCategoryCounts();
        for (Iterator it = ((List) array.get(0)).iterator(); it.hasNext(); ) {
            List sub = (List) it.next();
            sl.put((String) sub.get(0), new Integer((String) sub.get(1)));
        }
    }

    public String getSettingCode() {
        return "ct";
    }
}
