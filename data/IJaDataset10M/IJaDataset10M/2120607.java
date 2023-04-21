package kr.or.javacafe.member.bo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.or.javacafe.member.dao.*;
import kr.or.javacafe.member.domain.*;

@Service
public class MemberBOImpl implements MemberBO {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    @Transactional(readOnly = true)
    public int getUserListCount(User param) {
        return memberDAO.selectUserListCount(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserList(User param) {
        return memberDAO.selectUserList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(User param) {
        return memberDAO.selectUser(param);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOverlapUser(String strUser) {
        int intRet = memberDAO.selectOverlapUserCount(strUser);
        if (intRet == 0) return false; else return true;
    }

    @Override
    @Transactional
    public void addUser(User param) {
        memberDAO.insertUser(param);
    }

    @Override
    @Transactional
    public void editUser(User param) {
        memberDAO.updateUser(param);
    }

    @Override
    @Transactional
    public void editUserRole(User param) {
        memberDAO.updateUserRoleGrp(param);
    }

    @Override
    @Transactional(readOnly = true)
    public User viewUser(User param) {
        return memberDAO.selectUser(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserHistory> getUserHistory(UserHistory param) {
        return memberDAO.selectUserHistory(param);
    }

    @Override
    @Transactional
    public void addUserHistory(UserHistory param) {
        memberDAO.insertUserHistory(param);
    }

    @Override
    @Transactional
    public void removeUserHistory(UserHistory param) {
        memberDAO.deleteUserHistory(param);
    }

    @Override
    @Transactional
    public void editUserTeam(UserTeam param) {
        memberDAO.updateUserTeam(param);
    }

    @Override
    @Transactional
    public void editUserTeamIntroduction(UserTeam param) {
        memberDAO.updateUserTeamIntroduction(param);
    }
}
