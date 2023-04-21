package com.newsbeef.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import com.newsbeef.model.FeedInfo;

public class FeedInfoDao {

    private static class FeedInfoRowMapper implements ParameterizedRowMapper<FeedInfo> {

        public FeedInfo mapRow(ResultSet rs, int row) throws SQLException {
            FeedInfo feedInfo = new FeedInfo(rs.getInt("id"));
            feedInfo.setUrl(rs.getString("url"));
            feedInfo.setScheduleDate(rs.getDate("scheduledDate"));
            feedInfo.setTotalUsers(rs.getInt("totalUsers"));
            feedInfo.setStatus(rs.getByte("status"));
            return feedInfo;
        }
    }

    private static class UserFeedInfoRowMapper extends FeedInfoRowMapper {

        public FeedInfo mapRow(ResultSet rs, int row) throws SQLException {
            FeedInfo feedInfo = super.mapRow(rs, row);
            feedInfo.setUserTitle(rs.getString("title"));
            return feedInfo;
        }
    }

    private SimpleJdbcTemplate simpleJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    public List<FeedInfo> findSchedulableFeedInfos() {
        String sql = "select * from feed where scheduledDate < ? and (status = ? or status = ?)";
        ParameterizedRowMapper<FeedInfo> mapper = new FeedInfoRowMapper();
        Date currentDate = new Date();
        return this.simpleJdbcTemplate.query(sql, mapper, currentDate, FeedInfo.STATUS_AVAILABLE, FeedInfo.STATUS_ERROR);
    }

    public List<FeedInfo> findFeedInfos(String userId) {
        String sql = "select * from feeduserinfo as fui inner join feed as f on fui.feedId=f.id where fui.userId=?";
        ParameterizedRowMapper<FeedInfo> mapper = new UserFeedInfoRowMapper();
        return this.simpleJdbcTemplate.query(sql, mapper, userId);
    }

    public FeedInfo addFeed(String userId, String url, String title) {
        FeedInfo feedInfo = addFeed(url);
        createUserFeed(url, title, feedInfo);
        String sql = "update feed set totalUsers=totalUsers+1 where id=?";
        this.simpleJdbcTemplate.update(sql, feedInfo.getId());
        feedInfo.setTotalUsers(feedInfo.getTotalUsers() + 1);
        return feedInfo;
    }

    public void createUserFeed(String userId, String title, FeedInfo feedInfo) {
        String sql = "insert into feeduserinfo (feedId,userId,title) values (?,?;?)";
        this.simpleJdbcTemplate.update(sql, feedInfo.getId(), userId, title);
    }

    public FeedInfo addFeed(String url) {
        FeedInfo feedInfo;
        try {
            feedInfo = findFeed(url);
        } catch (EmptyResultDataAccessException e) {
            createFeed(url);
            feedInfo = findFeed(url);
        }
        return feedInfo;
    }

    public void createFeed(String url) {
        String sql = "insert into feed (url,scheduledDate,totalUsers,status) values (?,?,?,?)";
        Date scheduledDate = new Date();
        this.simpleJdbcTemplate.update(sql, url, scheduledDate, 0, FeedInfo.STATUS_AVAILABLE);
    }

    private FeedInfo findFeed(String url) {
        String sql = "select * from feed where url=?";
        ParameterizedRowMapper<FeedInfo> mapper = new FeedInfoRowMapper();
        return this.simpleJdbcTemplate.queryForObject(sql, mapper, url);
    }

    public void updateStatus(FeedInfo fi, short status) {
        String sql = "update feed set status=?, scheduledDate=? where id=?";
        Date scheduledDate = new Date();
        fi.setStatus(status);
        fi.setScheduleDate(scheduledDate);
        this.simpleJdbcTemplate.update(sql, status, scheduledDate, fi.getId());
    }
}
