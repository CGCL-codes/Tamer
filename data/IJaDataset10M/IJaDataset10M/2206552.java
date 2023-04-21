package coolsite.base.biz.impl;

import javax.annotation.Resource;
import coolsite.base.biz.MemberBiz;
import coolsite.base.dao.MemberDao;

public class MemberBizImpl implements MemberBiz {

    @Resource
    private MemberDao MemberDao;

    public MemberDao getMemberDao() {
        return MemberDao;
    }

    public void setMemberDao(MemberDao memberDao) {
        MemberDao = memberDao;
    }
}
