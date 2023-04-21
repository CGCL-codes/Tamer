package name.huliqing.qblog.dao;

import name.huliqing.qblog.entity.HelpEn;

/**
 *
 * @author huliqing
 */
public abstract class HelpDa extends BaseDao<HelpEn, String> {

    public HelpDa() {
        super(HelpEn.class);
    }
}
