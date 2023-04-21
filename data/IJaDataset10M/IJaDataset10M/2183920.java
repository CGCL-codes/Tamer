package com.hk.svr.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.taobao.Tb_Follow;
import com.hk.bean.taobao.Tb_Followed;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.svr.Tb_FollowService;

public class Tb_FollowServiceImpl implements Tb_FollowService {

    @Autowired
    private QueryManager manager;

    public void createTb_Follow(Tb_Follow tbFollow, boolean follow_to_sina) {
        if (tbFollow.getUserid() == tbFollow.getFriendid()) {
            return;
        }
        Query query = this.manager.createQuery();
        if (query.getObjectEx(Tb_Follow.class, "userid=? and friendid=?", new Object[] { tbFollow.getUserid(), tbFollow.getFriendid() }) == null) {
            long oid = query.insertObject(tbFollow).longValue();
            tbFollow.setOid(oid);
        }
    }

    public void createTb_Followed(Tb_Followed tbFollowed) {
        if (tbFollowed.getUserid() == tbFollowed.getFansid()) {
            return;
        }
        Query query = this.manager.createQuery();
        if (query.getObjectEx(Tb_Followed.class, "userid=? and fansid=?", new Object[] { tbFollowed.getUserid(), tbFollowed.getFansid() }) == null) {
            long oid = query.insertObject(tbFollowed).longValue();
            tbFollowed.setOid(oid);
        }
    }

    public void deleteTb_Follow(Tb_Follow tbFollow) {
        this.manager.createQuery().deleteById(Tb_Follow.class, tbFollow.getOid());
    }

    public void deleteTb_Followed(Tb_Followed tbFollowed) {
        this.manager.createQuery().deleteById(Tb_Followed.class, tbFollowed.getOid());
    }

    public Tb_Follow getTb_FollowByUseridAndFriendid(long userid, long friendid) {
        Query query = this.manager.createQuery();
        return query.getObjectEx(Tb_Follow.class, "userid=? and friendid=?", new Object[] { userid, friendid });
    }

    public List<Tb_Follow> getTb_FollowListByUserid(long userid, boolean buildFollow, int begin, int size) {
        Query query = this.manager.createQuery();
        return query.listEx(Tb_Follow.class, "userid=?", new Object[] { userid }, "oid desc", begin, size);
    }

    public Tb_Followed getTb_FollowedByUseridAndFriendid(long userid, long fansid) {
        Query query = this.manager.createQuery();
        return query.getObjectEx(Tb_Followed.class, "userid=? and fansid=?", new Object[] { userid, fansid });
    }

    public List<Tb_Followed> getTb_FollowedListByUserid(long userid, boolean buildFans, int begin, int size) {
        Query query = this.manager.createQuery();
        return query.listEx(Tb_Followed.class, "userid=?", new Object[] { userid }, "oid desc", begin, size);
    }

    public int countTb_FollowByUserid(long userid) {
        Query query = this.manager.createQuery();
        return query.count(Tb_Follow.class, "userid=?", new Object[] { userid });
    }

    public int countTb_FollowedByUserid(long userid) {
        Query query = this.manager.createQuery();
        return query.count(Tb_Followed.class, "userid=?", new Object[] { userid });
    }

    @Override
    public List<Long> getTb_FollowedFansidListByUserid(long userid) {
        List<Tb_Followed> list = this.getTb_FollowedListByUserid(userid, false, 0, -1);
        List<Long> idList = new ArrayList<Long>();
        for (Tb_Followed o : list) {
            idList.add(o.getFansid());
        }
        return idList;
    }
}
