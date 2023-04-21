package coyousoft.jiuhuabook.dao;

import java.sql.SQLException;
import java.util.List;
import coyousoft.jiuhuabook.entity.UserInfo;
import coyousoft.jiuhuabook.entity.Pagination;

public interface UserInfoDao {

    public UserInfo getUserInfo(String logonName) throws SQLException;

    public UserInfo selectById(Long userId) throws SQLException;

    public Pagination<UserInfo> selectByPagination(Long userClosed, int pageNum, int pageSize) throws SQLException;

    public List<UserInfo> selectAll() throws SQLException;

    public boolean delete(Long userId) throws SQLException;

    public boolean update(UserInfo userInfo) throws SQLException;

    public boolean addNew(UserInfo userInfo) throws SQLException;

    public boolean updateLastLogon(String ip, Long userId) throws SQLException;
}
