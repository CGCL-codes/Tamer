package org.osmius.dao.jdbc;

import org.osmius.dao.OsmGroupInstanceDao;
import org.osmius.model.OsmGroup;
import org.osmius.model.OsmInstance;
import org.osmius.service.exceptions.OsmGroupInstanceException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OsmGroupInstanceDaoJDBC extends BaseDaoJDBC implements OsmGroupInstanceDao {

    public List getOsmGroupInstances(String idnGroup) {
        class TmpMapper implements ParameterizedRowMapper {

            public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                String[] tmp = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) };
                return tmp;
            }
        }
        List data;
        StringBuffer queryString = new StringBuffer().append("SELECT INS.IDN_INSTANCE, INS.DES_INSTANCE, INS.TYP_INSTANCE,GI.IDN_GROUP, INS.DTI_LASTNEWS ").append("FROM OSM_INSTANCES INS, OSM_GROUP_INSTANCES GI ").append("WHERE GI.IDN_GROUP=? AND GI.IDN_INSTANCE=INS.IDN_INSTANCE ").append("ORDER BY INS.IDN_INSTANCE");
        data = jdbcTemplate.query(queryString.toString(), new Object[] { idnGroup }, new TmpMapper());
        return data;
    }

    public List getAvailableInstances(String idnGroup) {
        return null;
    }

    public List getAvailableInstancesIdAndDes(String idnGroup) {
        class TmpMapper implements ParameterizedRowMapper {

            public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                String[] tmp = new String[] { rs.getString(1), rs.getString(2) };
                return tmp;
            }
        }
        List data;
        StringBuilder queryString = new StringBuilder().append("SELECT INS.IDN_INSTANCE, INS.DES_INSTANCE FROM OSM_INSTANCES INS WHERE ").append("NOT EXISTS (SELECT 1 FROM OSM_GROUP_INSTANCES GINS WHERE GINS.IDN_GROUP = ? AND GINS.IDN_INSTANCE=INS.IDN_INSTANCE)");
        data = jdbcTemplate.query(queryString.toString(), new Object[] { idnGroup }, new TmpMapper());
        return data;
    }

    public void saveOsmGroupInstances(OsmGroup osmGroup, OsmInstance[] osmInstances) {
    }

    public void removeOsmGroupInstances(String idnGroup, String[] idnInstances) {
    }

    public void removeOsmGroupInstances(String idnGroup) {
    }

    public void removeOsmGroupInstancesByInstance(final String idnInstance) {
    }

    public List getAvailableGroups(String idnInstance) {
        return null;
    }

    public List getOsmGroupInstancesByInstance(String idnInstance) {
        return null;
    }

    public void saveOsmGroupInstances(OsmInstance osmInstance, OsmGroup[] osmGroups) {
    }

    public void removeOsmGroupInstances(OsmInstance osmInstance, String[] avaGroups) {
    }

    public List getAssignedInstances(String idnGroup) {
        return null;
    }

    public void saveOsmGroupInstances(OsmGroup osmGroup, OsmInstance[] arrInsntances, String[] delSubs) throws OsmGroupInstanceException {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getString(1), rs.getLong(2) };
                return tmp;
            }
        }
        class TmpMapperII implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getString(1), rs.getString(2), rs.getLong(3) };
                return tmp;
            }
        }
        jdbcTemplate.update("DELETE FROM OSM_GROUP_INSTANCES WHERE IDN_GROUP=?", new Object[] { osmGroup.getIdnGroup() });
        for (int i = 0; i < arrInsntances.length; i++) {
            OsmInstance arrInsntance = arrInsntances[i];
            jdbcTemplate.update("INSERT INTO OSM_GROUP_INSTANCES VALUES(?,?)", new Object[] { osmGroup.getIdnGroup(), arrInsntance.getIdnInstance() });
        }
        String query = null;
        List userSubs;
        long flag;
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT DISTINCT(NUS.IDN_USER), NUS.IDN_SUBSCRIPTION FROM ").append("OSM_N_USER_SUBSCRIPTION NUS, OSM_N_INSTANCE_SUBSCRIPTION NIS ").append("WHERE NIS.IDN_INSTANCE=? AND NIS.IDN_SUBSCRIPTION=NUS.IDN_SUBSCRIPTION AND ").append("NIS.IDN_INSTANCE NOT IN (SELECT DISTINCT(GI.IDN_INSTANCE) FROM OSM_GROUP_INSTANCES GI, OSM_USER_GROUPS UG WHERE GI.IDN_GROUP=UG.IDN_GROUP AND UG.IDN_USER=NUS.IDN_USER) ").append("ORDER BY NUS.IDN_USER, NUS.IDN_SUBSCRIPTION").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapper());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] userSub = (Object[]) userSubs.get(j);
                jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { userSub[0], userSub[1] });
                flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                if (flag == 0) {
                    jdbcTemplate.update("DELETE FROM OSM_N_INSTANCE_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                    jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                }
            }
        }
        userSubs = null;
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT US.IDN_USER, SS.IDN_SERVICE, US.IDN_SUBSCRIPTION ").append("FROM OSM_N_SERVICE_SUBSCRIPTION SS, OSM_N_USER_SUBSCRIPTION US, OSM_SERVICE_INSTANCES SI ").append("WHERE SS.IDN_SUBSCRIPTION=US.IDN_SUBSCRIPTION ").append("AND SS.IDN_SERVICE = SI.IDN_SERVICE ").append("AND SI.IDN_INSTANCE= ? ").append("GROUP BY US.IDN_SUBSCRIPTION, US.IDN_USER, SS.IDN_SERVICE ").append("ORDER BY US.IDN_SUBSCRIPTION, US.IDN_USER, SS.IDN_SERVICE").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapperII());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] objects = (Object[]) userSubs.get(j);
                if (!testServiceSecurity((String) objects[0], (String) objects[1])) {
                    jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { objects[0], objects[2] });
                    flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    if (flag == 0) {
                        jdbcTemplate.update("DELETE FROM OSM_N_SERVICE_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                        jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    }
                }
            }
        }
        userSubs = null;
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT US.IDN_USER, SLASUB.IDN_SLA, US.IDN_SUBSCRIPTION ").append("FROM OSM_N_SLA_SUBSCRIPTION SLASUB, OSM_N_USER_SUBSCRIPTION US, OSM_SERVICE_SLAS SSLA, OSM_SERVICE_INSTANCES SI ").append("WHERE SLASUB.IDN_SUBSCRIPTION=US.IDN_SUBSCRIPTION ").append("AND SLASUB.IDN_SLA = SSLA.IDN_SLA ").append("AND SSLA.IDN_SERVICE = SI.IDN_SERVICE ").append("AND SI.IDN_INSTANCE= ? ").append("GROUP BY US.IDN_SUBSCRIPTION, US.IDN_USER, SLASUB.IDN_SLA ").append("ORDER BY US.IDN_SUBSCRIPTION, US.IDN_USER, SLASUB.IDN_SLA").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapperII());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] objects = (Object[]) userSubs.get(j);
                if (!testSLASecurity((String) objects[0], (String) objects[1])) {
                    jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { objects[0], objects[2] });
                    flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    if (flag == 0) {
                        jdbcTemplate.update("DELETE FROM OSM_N_SLA_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                        jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    }
                }
            }
        }
    }

    public void removeOsmGroupInstancesAndDependences(String idnGroup, String[] delSubs) throws OsmGroupInstanceException {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getString(1), rs.getLong(2) };
                return tmp;
            }
        }
        class TmpMapperII implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getString(1), rs.getString(2), rs.getLong(3) };
                return tmp;
            }
        }
        String query;
        List userSubs;
        long flag;
        jdbcTemplate.update("DELETE FROM OSM_GROUP_INSTANCES WHERE IDN_GROUP=?", new Object[] { idnGroup });
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT DISTINCT(NUS.IDN_USER), NUS.IDN_SUBSCRIPTION FROM ").append("OSM_N_USER_SUBSCRIPTION NUS, OSM_N_INSTANCE_SUBSCRIPTION NIS ").append("WHERE NIS.IDN_INSTANCE=? AND NIS.IDN_SUBSCRIPTION=NUS.IDN_SUBSCRIPTION AND ").append("NIS.IDN_INSTANCE IN (SELECT DISTINCT(GI.IDN_INSTANCE) FROM OSM_GROUP_INSTANCES GI, OSM_USER_GROUPS UG WHERE GI.IDN_GROUP=UG.IDN_GROUP AND UG.IDN_USER=NUS.IDN_USER) ").append("ORDER BY NUS.IDN_USER, NUS.IDN_SUBSCRIPTION").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapper());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] userSub = (Object[]) userSubs.get(j);
                jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { userSub[0], userSub[1] });
                flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                if (flag == 0) {
                    jdbcTemplate.update("DELETE FROM OSM_N_INSTANCE_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                    jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { userSub[1] });
                }
            }
        }
        userSubs = null;
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT US.IDN_USER, SS.IDN_SERVICE, US.IDN_SUBSCRIPTION ").append("FROM OSM_N_SERVICE_SUBSCRIPTION SS, OSM_N_USER_SUBSCRIPTION US, OSM_SERVICE_INSTANCES SI ").append("WHERE SS.IDN_SUBSCRIPTION=US.IDN_SUBSCRIPTION ").append("AND SS.IDN_SERVICE = SI.IDN_SERVICE ").append("AND SI.IDN_INSTANCE= ? ").append("GROUP BY US.IDN_SUBSCRIPTION, US.IDN_USER, SS.IDN_SERVICE ").append("ORDER BY US.IDN_SUBSCRIPTION, US.IDN_USER, SS.IDN_SERVICE").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapperII());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] objects = (Object[]) userSubs.get(j);
                if (!testServiceSecurity((String) objects[0], (String) objects[1])) {
                    jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { objects[0], objects[2] });
                    flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    if (flag == 0) {
                        jdbcTemplate.update("DELETE FROM OSM_N_SERVICE_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                        jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    }
                }
            }
        }
        userSubs = null;
        for (int i = 0; i < delSubs.length; i++) {
            String instance = delSubs[i];
            query = new StringBuilder().append("SELECT US.IDN_USER, SLASUB.IDN_SLA, US.IDN_SUBSCRIPTION ").append("FROM OSM_N_SLA_SUBSCRIPTION SLASUB, OSM_N_USER_SUBSCRIPTION US, OSM_SERVICE_SLAS SSLA, OSM_SERVICE_INSTANCES SI ").append("WHERE SLASUB.IDN_SUBSCRIPTION=US.IDN_SUBSCRIPTION ").append("AND SLASUB.IDN_SLA = SSLA.IDN_SLA ").append("AND SSLA.IDN_SERVICE = SI.IDN_SERVICE ").append("AND SI.IDN_INSTANCE= ? ").append("GROUP BY US.IDN_SUBSCRIPTION, US.IDN_USER, SLASUB.IDN_SLA ").append("ORDER BY US.IDN_SUBSCRIPTION, US.IDN_USER, SLASUB.IDN_SLA").toString();
            userSubs = jdbcTemplate.query(query, new Object[] { instance }, new TmpMapperII());
            for (int j = 0; j < userSubs.size(); j++) {
                Object[] objects = (Object[]) userSubs.get(j);
                if (!testSLASecurity((String) objects[0], (String) objects[1])) {
                    jdbcTemplate.update("DELETE FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_USER=? AND IDN_SUBSCRIPTION=?", new Object[] { objects[0], objects[2] });
                    flag = jdbcTemplate.queryForLong("SELECT COUNT(*) FROM OSM_N_USER_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    if (flag == 0) {
                        jdbcTemplate.update("DELETE FROM OSM_N_SLA_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                        jdbcTemplate.update("DELETE FROM OSM_N_SUBSCRIPTION WHERE IDN_SUBSCRIPTION=?", new Object[] { objects[2] });
                    }
                }
            }
        }
    }

    public List getAvailableInstances(String idnGroup, String instance) {
        return null;
    }

    private boolean testServiceSecurity(String idnUser, String idnService) {
        String query = new StringBuilder().append("SELECT COUNT(*) ").append("FROM OSM_USER_GROUPS UG,  ").append("OSM_GROUP_INSTANCES GI, ").append("OSM_SERVICE_INSTANCES SI  ").append("WHERE SI.IDN_SERVICE= ? ").append("  AND SI.IDN_INSTANCE=GI.IDN_INSTANCE  ").append("  AND GI.IDN_GROUP = UG.IDN_GROUP  ").append("  AND UG.IDN_USER= ?").toString();
        return jdbcTemplate.queryForLong(query, new Object[] { idnService, idnUser }) == 0 ? false : true;
    }

    private boolean testSLASecurity(String idnUser, String idnSLA) {
        String query = new StringBuilder().append("SELECT COUNT(*) ").append("FROM OSM_SERVICE_SLAS SSLA  ").append("WHERE SSLA.IDN_SLA= ? ").append("  AND EXISTS (SELECT 1  ").append("              FROM OSM_USER_GROUPS UG,  ").append("                   OSM_GROUP_INSTANCES GI, ").append("                   OSM_SERVICE_INSTANCES SI ").append("              WHERE SSLA.IDN_SERVICE=SI.IDN_SERVICE ").append("                AND SI.IDN_INSTANCE=GI.IDN_INSTANCE  ").append("                AND GI.IDN_GROUP = UG.IDN_GROUP  ").append("                AND UG.IDN_USER= ? )").toString();
        return jdbcTemplate.queryForLong(query, new Object[] { idnSLA, idnUser }) == 0 ? false : true;
    }
}
