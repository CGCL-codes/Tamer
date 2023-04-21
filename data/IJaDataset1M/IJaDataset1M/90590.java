package com.bbs.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.bbs.dao.ModeratorDao;
import com.bbs.entity.Moderator;
import com.bbs.util.DateUtil;

public class ModeratorService {

    private ModeratorDao md = new ModeratorDao();

    public int setModerator(int uid, int bid) {
        return md.setModerator(DateUtil.getNow(), uid, bid);
    }

    public void deleteBybid(int bid) {
        md.deleteBybid(bid);
    }

    public List<Integer> findMlistByBid(int bid) {
        return md.findModeratorsBybid(bid);
    }

    public int delete(int uid, int bid) {
        return new ModeratorDao().delete(uid, bid);
    }

    public Map<String, String> getByBlist(List<Moderator> list) {
        Map<String, String> map = new HashMap<String, String>();
        for (Moderator m : list) {
            map.put(String.valueOf(m.getUser().getId()), String.valueOf(m.getBlocks().getId()));
        }
        return map;
    }

    public List<Moderator> findByBid(int id) {
        return md.findByBid(id);
    }

    public Moderator findByUid(int id) {
        return md.findByUid(id);
    }
}
