package com.mytaobao.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.mytaobao.dao.MemberDao;
import com.mytaobao.domain.LeaveMessage;
import com.mytaobao.domain.Member;

/**
 * @author z3y2
 */
@Deprecated
@Service
@Transactional
public class LeaveMessageService {

    private MemberDao memberDao;

    public void addMessage(LeaveMessage m) {
        Member mm = new Member();
        mm.setId(m.getEmail());
        memberDao.save(mm);
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<Member> findAllMessage() {
        return memberDao.findAll();
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
}
