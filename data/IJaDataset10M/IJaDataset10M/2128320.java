package cn.openlab.game.web;

import cn.openlab.game.dao.MemberDao;
import cn.openlab.game.dao.MemberDaoImpl;
import cn.openlab.game.dao.MemberResourceDao;
import cn.openlab.game.dao.MemberResourceDaoImpl;
import cn.openlab.game.entity.Member;

public class ProfileAction {

    private Member member;

    private MemberDao memberDao;

    private MemberResourceDao memberResourceDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void setMemberResourceDao(MemberResourceDao memberResourceDao) {
        this.memberResourceDao = memberResourceDao;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String execute() {
        String userName = member.getUserName();
        member = memberDao.getMemberByUserName(userName);
        return "success";
    }

    public String register() {
        memberDao.saveMember(member);
        return "success";
    }

    public String loadMemberResource() {
        return null;
    }
}
