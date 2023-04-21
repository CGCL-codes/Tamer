package meconsea.webcoll.sys.dao;

import meconsea.webcoll.base.dao.BaseDAO;
import meconsea.webcoll.base.dao.IDAO;
import meconsea.webcoll.sys.entity.SysUserPopedom;
import com.google.inject.name.Named;
import com.wideplay.warp.persist.dao.Finder;

public class SysUserPopedomDao extends BaseDAO<SysUserPopedom, String> implements IDAO<SysUserPopedom, String> {

    public SysUserPopedomDao() {
        super(SysUserPopedom.class);
    }

    @Finder(namedQuery = SysUserPopedom.FIND_USER_POPEDOM)
    public SysUserPopedom getSysUserPopedom(@Named("sysUserId") String sysUserId) {
        return null;
    }
}
