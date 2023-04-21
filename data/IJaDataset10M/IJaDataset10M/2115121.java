package com.sns2Life.user.business.service.impl;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.sns2Life.user.business.service.SpaceService;
import com.sns2Life.user.persistence.dao.SpaceDAO;
import com.sns2Life.user.persistence.model.Space;
import com.sns2Life.user.persistence.vo.MemberBasicInfoVO;

public class SpaceServiceImpl implements SpaceService {

    private SpaceDAO spaceDAO;

    public void setSpaceDAO(SpaceDAO spaceDAO) {
        this.spaceDAO = spaceDAO;
    }

    public void delete(Space persistentInstance) {
        spaceDAO.delete(persistentInstance);
    }

    public List findAll() {
        return spaceDAO.findAll();
    }

    public List<Space> findByAddfriend(Object addfriend) {
        return spaceDAO.findByAddfriend(addfriend);
    }

    public List<Space> findByAddfriendnum(Object addfriendnum) {
        return spaceDAO.findByAddfriendnum(addfriendnum);
    }

    public List<Space> findByAddsize(Object addsize) {
        return spaceDAO.findByAddsize(addsize);
    }

    public List<Space> findByAlbumnum(Object albumnum) {
        return spaceDAO.findByAlbumnum(albumnum);
    }

    public List<Space> findByAttachsize(Object attachsize) {
        return spaceDAO.findByAttachsize(attachsize);
    }

    public List<Space> findByAvatar(Object avatar) {
        return spaceDAO.findByAvatar(avatar);
    }

    public List<Space> findByBlognum(Object blognum) {
        return spaceDAO.findByBlognum(blognum);
    }

    public List<Space> findByCredit(Object credit) {
        return spaceDAO.findByCredit(credit);
    }

    public List<Space> findByDateline(Object dateline) {
        return spaceDAO.findByDateline(dateline);
    }

    public List<Space> findByDoingnum(Object doingnum) {
        return spaceDAO.findByDoingnum(doingnum);
    }

    public List<Space> findByDomain(Object domain) {
        return spaceDAO.findByDomain(domain);
    }

    public List<Space> findByEventinvitenum(Object eventinvitenum) {
        return spaceDAO.findByEventinvitenum(eventinvitenum);
    }

    public List<Space> findByEventnum(Object eventnum) {
        return spaceDAO.findByEventnum(eventnum);
    }

    public List<Space> findByExample(Space instance) {
        return spaceDAO.findByExample(instance);
    }

    public List<Space> findByExperience(Object experience) {
        return spaceDAO.findByExperience(experience);
    }

    public List<Space> findByFlag(Object flag) {
        return spaceDAO.findByFlag(flag);
    }

    public List<Space> findByFriendnum(Object friendnum) {
        return spaceDAO.findByFriendnum(friendnum);
    }

    public List<Space> findByGroupid(Object groupid) {
        return spaceDAO.findByGroupid(groupid);
    }

    public Space findById(Integer id) {
        return spaceDAO.findById(id);
    }

    public List<Space> findByIp(Object ip) {
        return spaceDAO.findByIp(ip);
    }

    public List<Space> findByLastlogin(Object lastlogin) {
        return spaceDAO.findByLastlogin(lastlogin);
    }

    public List<Space> findByLastpost(Object lastpost) {
        return spaceDAO.findByLastpost(lastpost);
    }

    public List<Space> findByLastsearch(Object lastsearch) {
        return spaceDAO.findByLastsearch(lastsearch);
    }

    public List<Space> findByLastsend(Object lastsend) {
        return spaceDAO.findByLastsend(lastsend);
    }

    public List<Space> findByMood(Object mood) {
        return spaceDAO.findByMood(mood);
    }

    public List<Space> findByMtaginvitenum(Object mtaginvitenum) {
        return spaceDAO.findByMtaginvitenum(mtaginvitenum);
    }

    public List<Space> findByMyinvitenum(Object myinvitenum) {
        return spaceDAO.findByMyinvitenum(myinvitenum);
    }

    public List<Space> findByName(Object name) {
        return spaceDAO.findByName(name);
    }

    public List<Space> findByNamestatus(Object namestatus) {
        return spaceDAO.findByNamestatus(namestatus);
    }

    public List<Space> findByNewpm(Object newpm) {
        return spaceDAO.findByNewpm(newpm);
    }

    public List<Space> findByNotenum(Object notenum) {
        return spaceDAO.findByNotenum(notenum);
    }

    public List<Space> findByPokenum(Object pokenum) {
        return spaceDAO.findByPokenum(pokenum);
    }

    public List<Space> findByPollnum(Object pollnum) {
        return spaceDAO.findByPollnum(pollnum);
    }

    public List findByProperty(String propertyName, Object value) {
        return spaceDAO.findByProperty(propertyName, value);
    }

    public List<Space> findByRegip(Object regip) {
        return spaceDAO.findByRegip(regip);
    }

    public List<Space> findBySharenum(Object sharenum) {
        return spaceDAO.findBySharenum(sharenum);
    }

    public List<Space> findByThreadnum(Object threadnum) {
        return spaceDAO.findByThreadnum(threadnum);
    }

    public List<Space> findByUpdatetime(Object updatetime) {
        return spaceDAO.findByUpdatetime(updatetime);
    }

    public List<Space> findByUsername(Object username) {
        return spaceDAO.findByUsername(username);
    }

    public List<Space> findByVideostatus(Object videostatus) {
        return spaceDAO.findByVideostatus(videostatus);
    }

    public List<Space> findByViewnum(Object viewnum) {
        return spaceDAO.findByViewnum(viewnum);
    }

    public void save(Space transientInstance) {
        spaceDAO.save(transientInstance);
    }

    @Override
    public Space findByUid(Integer uid) {
        return spaceDAO.findByUid(uid);
    }

    @Override
    @Transactional
    public void saveByMemberBasicInfoVO(MemberBasicInfoVO bvo) {
        Space space = spaceDAO.findByUid(bvo.getUid());
        space.setName(bvo.getName());
    }
}
