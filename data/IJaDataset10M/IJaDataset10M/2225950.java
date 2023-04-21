package net.naijatek.myalumni.modules.common.persistence.hibernate;

import java.util.List;
import net.naijatek.myalumni.framework.exceptions.MyAlumniException;
import net.naijatek.myalumni.modules.common.domain.XlatDetailVO;
import net.naijatek.myalumni.modules.common.domain.XlatGroupVO;
import net.naijatek.myalumni.modules.common.persistence.BaseHibernateDao;
import net.naijatek.myalumni.modules.common.persistence.iface.XlatDao;
import net.naijatek.myalumni.util.BaseConstants;

public class XlatHibernateDao extends BaseHibernateDao implements XlatDao {

    @SuppressWarnings("unchecked")
    public XlatDetailVO getDetail(String groupId, String detailId) {
        List<XlatDetailVO> list = getHibernateTemplate().findByNamedQueryAndNamedParam("xlatdetail.byorgAndgroupAndid", new String[] { "id", "groupId" }, new Object[] { detailId, groupId });
        if (list != null && !list.isEmpty()) return list.get(0);
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<XlatDetailVO> getGroupDetails(String groupId) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam("xlatdetail.byorgAndgroup", "groupId", groupId);
    }

    @SuppressWarnings("unchecked")
    public List<XlatDetailVO> getActiveGroupDetails(String groupId) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam("xlatdetail.active.byorgAndgroup", new String[] { "groupId", "status" }, new Object[] { groupId, BaseConstants.ACTIVE });
    }

    public void updateXlatDetail(String userName, String groupId, String lookupCodeId, String status, String label) throws MyAlumniException {
        XlatDetailVO detail;
        detail = (XlatDetailVO) getHibernateTemplate().load(XlatDetailVO.class, lookupCodeId);
        detail.setGroup(new XlatGroupVO(groupId));
        detail.setStatus(status);
        detail.setLastModifiedBy(userName);
        detail.setLabel(label);
        update(detail);
    }

    public void addXlatDetail(XlatDetailVO detail) {
        detail.setGroup(new XlatGroupVO(detail.getLookupGroupId()));
        add(detail);
    }
}
