package com.note.dao.impl;

import org.springframework.stereotype.Repository;
import com.note.dao.MemberDao;
import com.note.model.Member;

@Repository
public class MemberDaoImpl extends BaseDaoImpl<Member, String> implements MemberDao {

    public Member getMemberByUsername(String username) {
        String hql = "from Member members where lower(members.userName) = lower(?)";
        return (Member) getSession().createQuery(hql).setParameter(0, username).uniqueResult();
    }
}
