package com.firescrum.dailymeeting.dao;

import java.util.Date;
import java.util.List;
import com.firescrum.core.model.Product;
import com.firescrum.core.model.UserApp;
import com.firescrum.dailymeeting.model.MemberRecord;
import com.firescrum.infrastructure.dao.BaseDao;

public class DaoMemberRecord extends BaseDao<MemberRecord> implements IDaoMemberRecord {

    public List<MemberRecord> getAllMemberRecordByDate(Product product, Date dailyMeetingDate) {
        final Object[] values = { product, dailyMeetingDate };
        return super.getHibernateTemplate().find("FROM MemberRecord WHERE product = ? and dailyMeetingDate = ?", values);
    }

    public MemberRecord getMemberRecord(UserApp user, Product product, Date dailyMeetingDate) {
        final Object[] values = { user, product, dailyMeetingDate };
        MemberRecord memberRecord = null;
        List<MemberRecord> list = super.getHibernateTemplate().find("FROM MemberRecord WHERE user = ? and product = ? and dailyMeetingDate = ?", values);
        ;
        if (list != null && !list.isEmpty()) {
            memberRecord = list.get(0);
        }
        return memberRecord;
    }
}
