package org.magicbox.repository.ibatis;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javolution.util.FastMap;
import org.magicbox.core.model.CenterUser;
import org.magicbox.repository.CenterUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * CenterUserSqlMap Repository, ibatis implementation of CenterUserRepository
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0.1
 */
@Repository("centerUserRepository")
public class CenterUserSqlMap extends SqlMapClientTemplate implements CenterUserRepository {

    private int offset;

    public void setNumberUsersOnPage(int offset) {
        this.offset = offset;
    }

    @Override
    public CenterUser getCenterUserByMail(String mail, String centerId) {
        return null;
    }

    @Override
    public List<String> getGroupsMail(String listeWithSeparator, String separator) {
        return null;
    }

    public boolean deleteCenterUser(String id, String centerId) throws DataAccessException {
        Map<String, String> params = new FastMap<String, String>(2);
        params.put("id", id);
        params.put("centro", centerId);
        return delete("deleteCenterUser", params) == 1;
    }

    public boolean deleteCenterUser(String id) throws DataAccessException {
        return delete("deleteCenterUserById", id) == 1;
    }

    public boolean deleteCenterUsers(String centerId) throws DataAccessException {
        return delete("deleteCenterUsers", centerId) == 1;
    }

    public Collection<CenterUser> getCenterUsersByZipCode(String zipCode, String centerId, int page) throws DataAccessException {
        Map params = new FastMap();
        params.put("zipCode", zipCode);
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserByZipCode", params);
    }

    public Collection<CenterUser> getCenterUsersByCity(String city, String centerId, int page) throws DataAccessException {
        Map params = new FastMap();
        params.put("city", city);
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserByCity", params);
    }

    public Collection<CenterUser> getCenterUsersByNameAndSurname(String name, String surname, String centerId, int page) throws DataAccessException {
        Map params = new FastMap();
        params.put("name", name.toUpperCase());
        params.put("surname", surname.toUpperCase());
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserByNameAndSurname", params);
    }

    public Collection<CenterUser> getCenterUsersByProvince(String province, String centerId, int page) throws DataAccessException {
        Map params = new FastMap();
        params.put("province", province);
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserByProvince", params);
    }

    public Collection<CenterUser> getCenterUsersGroup(String centerId, String groupId, int page) throws DataAccessException {
        Map params = new FastMap<String, String>(4);
        params.put("centerId", centerId);
        params.put("groupId", groupId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserGroup", params);
    }

    public Collection<CenterUser> getCenterUsersNotInGroups(String centerId, int page) throws DataAccessException {
        Map params = new FastMap<String, String>(3);
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUserNotInGroups", centerId);
    }

    public int getCenterUsersOnPage() throws DataAccessException {
        return offset;
    }

    public Collection<CenterUser> getCenterUsers(String centerId, int page) throws DataAccessException {
        Map params = new FastMap();
        params.put("centerId", centerId);
        params.put("limit", offset);
        params.put("offset", page * offset);
        return queryForList("getCenterUsers", params);
    }

    public Collection<CenterUser> getCenterUsersForReport(String centerId) throws DataAccessException {
        return queryForList("getCenterUsersForReport", centerId);
    }

    public Collection<String> getCenterUsersMail(String centerId) throws DataAccessException {
        return queryForList("getMailCenterUsers", centerId);
    }

    public Collection<String> getCenterUsersMobile(String centerId) throws DataAccessException {
        return queryForList("getMobileCenterUsers", centerId);
    }

    public int getCenterUsersNumber(String centerId) throws DataAccessException {
        return (Integer) queryForObject("getNumberCenterUsers", centerId);
    }

    public CenterUser getCenterUser(String id, String centerId) throws DataAccessException {
        Map<String, String> params = new FastMap<String, String>(2);
        params.put("id", id);
        params.put("centerId", centerId);
        return (CenterUser) queryForObject("getCenterUser", params);
    }

    public void saveCenterUser(CenterUser user) {
        if (user.isNew()) {
            insert("insertCenterUser", user);
        } else {
            update("updateCenterUser", user);
        }
    }

    public void insertCenterUsers(final List<CenterUser> users) throws DataAccessException {
        execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executor) {
                int ris = 0;
                try {
                    executor.startBatch();
                    for (CenterUser centerUser : users) {
                        executor.insert("insertUser", centerUser);
                    }
                    ris = executor.executeBatch();
                } catch (SQLException e) {
                    Logger log = LoggerFactory.getLogger(this.getClass());
                    StringBuffer sb = new StringBuffer("InsertCenterUser failed \n").append("num user batch:").append(users.size()).append("\n").append(e.getNextException());
                    log.error(sb.toString());
                }
                return ris;
            }
        });
    }

    public void updateDeleteGroup(String centerId, String groupId) throws DataAccessException {
        Map params = new FastMap(2);
        params.put("centerId", centerId);
        params.put("groupId", groupId);
        update("updateDeleteGroup", params);
    }

    public void updateCenterUsers(final Collection<String> ids, final String groupId, final String centerId) throws DataAccessException {
        execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executor) {
                int ris = 0;
                try {
                    executor.startBatch();
                    Iterator<String> iter = ids.iterator();
                    while (iter.hasNext()) {
                        Map<String, String> params = new FastMap<String, String>(2);
                        params.put("id", iter.next().toString());
                        params.put("groupId", groupId);
                        params.put("centerId", centerId);
                        executor.insert("updateCenterUserGroup", params);
                    }
                    ris = executor.executeBatch();
                } catch (SQLException e) {
                    Logger log = LoggerFactory.getLogger(this.getClass());
                    StringBuffer sb = new StringBuffer("aggiornaUtenti failed \n").append("num ids batch:").append(ids.size()).append("\n").append(" idgruppo:").append(groupId).append("\n").append(e.getNextException());
                    log.error(sb.toString());
                }
                return ris;
            }
        });
    }

    @Autowired
    @Override
    public void setSqlMapClient(@Qualifier("sqlMapClient") SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }
}
