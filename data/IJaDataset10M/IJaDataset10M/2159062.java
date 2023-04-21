package com.hk.svr.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import com.hk.bean.LabaTag;
import com.hk.bean.Tag;
import com.hk.bean.TagClick;
import com.hk.bean.UserTag;
import com.hk.frame.dao.query.Query;
import com.hk.frame.dao.query.QueryManager;
import com.hk.svr.TagService;

public class TagServiceImpl implements TagService {

    @Autowired
    private QueryManager queryManager;

    public Tag createTag(String tagName) {
        Tag o = this.getTagByName(tagName);
        if (o == null) {
            Query query = this.queryManager.createQuery();
            query.addField("name", tagName);
            query.addField("labacount", 0);
            query.addField("usercount", 0);
            query.addField("hot", 0);
            query.addField("updatetime", new Date());
            long tagId = query.insert(Tag.class).longValue();
            Tag tag = new Tag();
            tag.setTagId(tagId);
            tag.setName(tagName);
            return tag;
        }
        return o;
    }

    public Tag getTag(long tagId) {
        Query query = this.queryManager.createQuery();
        return query.getObjectById(Tag.class, tagId);
    }

    public Tag getTagByName(String name) {
        Query query = this.queryManager.createQuery();
        query.setTable(Tag.class);
        query.where("name=?").setParam(name);
        return query.getObject(Tag.class);
    }

    public void addTagClick(long tagId, long userId, int add) {
        Query query = this.queryManager.createQuery();
        query.setTable(TagClick.class);
        query.where("tagid=? and userid=?").setParam(tagId).setParam(userId);
        if (query.count() == 0) {
            query.addField("tagid", tagId);
            query.addField("userid", userId);
            query.addField("pcount", 1);
            query.insert(TagClick.class);
        } else {
            query.setTable(TagClick.class);
            query.addField("pcount", "add", add);
            query.where("tagid=? and userid=?").setParam(tagId).setParam(userId);
            query.update();
        }
    }

    public List<UserTag> getUserTagList(long userId, int begin, int size) {
        Query query = this.queryManager.createQuery();
        query.setTable(UserTag.class);
        query.where("userid=?").setParam(userId);
        query.orderByDesc("tagid");
        return query.list(begin, size, UserTag.class);
    }

    public List<Tag> getTagListByUserId(long userId, int begin, int size) {
        Query query = this.queryManager.createQuery();
        String sql = "select t.* from tag t,usertag ut where ut.userid=? and t.tagid=ut.tagid order by ut.tagid desc";
        return query.listBySql("ds1", sql, begin, size, Tag.class, userId);
    }

    public List<Tag> getTagListByUserIdOrderByLabaCount(long userId, int begin, int size) {
        Query query = this.queryManager.createQuery();
        String sql = "select t.* from tag t,usertag ut where ut.userid=? and t.tagid=ut.tagid order by t.labacount desc,t.tagid desc";
        return query.listBySql("ds1", sql, begin, size, Tag.class, userId);
    }

    public List<Tag> getTagListByUserIdOrderByHot(long userId, int begin, int size) {
        Query query = this.queryManager.createQuery();
        String sql = "select t.* from tag t,usertag ut where ut.userid=? and t.tagid=ut.tagid order by t.hot desc";
        return query.listBySql("ds1", sql, begin, size, Tag.class, userId);
    }

    public List<Tag> getTagListOrderByLabaCount(int begin, int size) {
        Query query = this.queryManager.createQuery();
        query.setTable(Tag.class);
        query.orderByDesc("labacount");
        return query.list(begin, size, Tag.class);
    }

    public List<Tag> getTagListOrderByHot(int begin, int size) {
        Query query = this.queryManager.createQuery();
        query.setTable(Tag.class);
        query.orderByDesc("hot");
        return query.list(begin, size, Tag.class);
    }

    public void addUserTag(long userId, long tagId) {
        Query query = this.queryManager.createQuery();
        if (query.count(UserTag.class, "userid=? and tagid=?", new Object[] { userId, tagId }) == 0) {
            UserTag userTag = new UserTag();
            userTag.setUserId(userId);
            userTag.setTagId(tagId);
            query.insertObject(userTag);
        }
    }

    public void updateTagHot(Tag tag) {
        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        b.setTime(tag.getUpdateTime());
        b.set(Calendar.HOUR_OF_DAY, 0);
        b.set(Calendar.MINUTE, 0);
        b.set(Calendar.SECOND, 0);
        e.setTime(tag.getUpdateTime());
        e.set(Calendar.HOUR_OF_DAY, 23);
        e.set(Calendar.MINUTE, 59);
        e.set(Calendar.SECOND, 59);
        Date begin = b.getTime();
        Date end = e.getTime();
        Query query = this.queryManager.createQuery();
        String sql = "select l.createtime from laba l,labatag lt where l.labaid=lt.labaid and lt.tagid=? and l.createtime>=? and l.createtime<=?";
        List<Date> list = query.listBySql("ds1", sql, new ParameterizedRowMapper<Date>() {

            public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getTimestamp("createtime");
            }
        }, tag.getTagId(), begin, end);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyMMdd");
        int arg = Integer.parseInt(sdf.format(tag.getUpdateTime()));
        int arg0 = arg * list.size() / 100;
        int sum = 0;
        sdf.applyPattern("mmss");
        for (Date d : list) {
            sum += Integer.parseInt(sdf.format(d));
        }
        int arg1 = arg + sum / 20;
        int hot = arg0 + arg1;
        this.updateHot(tag.getTagId(), hot);
    }

    public void updateUpdateTime(long tagId, Date updateTime) {
        Query query = this.queryManager.createQuery();
        query.addField("updatetime", updateTime);
        query.updateById(Tag.class, tagId);
    }

    public List<Tag> getTagListByUpdateTimeRange(Date beginTime, Date endTime) {
        Query query = this.queryManager.createQuery();
        return query.listEx(Tag.class, "updatetime>=? and updatetime<=?", new Object[] { beginTime, endTime });
    }

    public void updateHot(long tagId, int hot) {
        Query query = this.queryManager.createQuery();
        query.setTable(Tag.class);
        query.addField("hot", hot);
        query.where("tagid=?").setParam(tagId);
        query.update();
    }

    public int countLabaTagByLabaIdAndAccessional(long labaId, byte accessional) {
        Query query = this.queryManager.createQuery();
        return query.count(LabaTag.class, "labaid=? and accessional=?", new Object[] { labaId, accessional });
    }
}
