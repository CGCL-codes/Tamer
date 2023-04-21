package org.osmius.dao.jdbc;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.osmius.dao.OsmHistinstAvailabilityDao;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OsmHistinstAvailabilityDaoJDBC extends BaseDaoJDBC implements OsmHistinstAvailabilityDao {

    public List getAvailabilities(final String idnInstance, final int days) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getInt(2), rs.getTimestamp(3), rs.getTimestamp(4) };
                return tmp;
            }
        }
        List data;
        String query = new StringBuilder().append("SELECT HIA.IDN_INSTANCE, HIA.IND_AVAILABILITY, greatest( HIA.DTI_INIAVAILABILITY, NOW() - INTERVAL ? DAY), LEAST(HIA.DTI_FINAVAILABILITY,NOW()) ").append("FROM OSM_HISTINST_AVAILABILITIES HIA ").append("WHERE HIA.IDN_INSTANCE=? AND HIA.IDN_INSTANCE IN (SELECT DISTINCT GI.IDN_INSTANCE FROM OSM_GROUP_INSTANCES GI, OSM_USER_GROUPS UG ").append("                     WHERE GI.IDN_GROUP     = UG.IDN_GROUP ").append("                       AND UG.IDN_USER      =  ? ) AND  ").append(" ((HIA.DTI_INIAVAILABILITY >= NOW() - INTERVAL ? DAY AND HIA.DTI_FINAVAILABILITY <= NOW()) OR ").append("  (HIA.DTI_INIAVAILABILITY  < NOW() - INTERVAL ? DAY AND HIA.DTI_FINAVAILABILITY > NOW() - INTERVAL ? DAY) OR ").append("  (HIA.DTI_INIAVAILABILITY >= NOW() - INTERVAL ? DAY AND HIA.DTI_INIAVAILABILITY < NOW() AND HIA.DTI_FINAVAILABILITY > NOW())) ").append("ORDER BY HIA.IDN_INSTANCE, HIA.DTI_INIAVAILABILITY ").toString();
        data = jdbcTemplate.query(query, new Object[] { days, idnInstance, getUser(), days, days, days, days }, new TmpMapper());
        return data;
    }

    public List getAvailabilitiesSecs(String idnInstance, final int days) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getInt(1), rs.getInt(2) };
                return tmp;
            }
        }
        List data;
        String query = new StringBuilder().append("SELECT COALESCE(SUM(IF(IND_AVAILABILITY=1,1,0) * (unix_timestamp(least(A.DTI_FINAVAILABILITY,NOW())) - ").append("                                                  unix_timestamp(greatest(A.DTI_INIAVAILABILITY,NOW()-INTERVAL ? DAY)))),0)  as SegsAVA, ").append("       COALESCE(SUM(IF(A.IND_AVAILABILITY=1,0,1) * (unix_timestamp(least(A.DTI_FINAVAILABILITY,NOW())) - ").append("                                                    unix_timestamp(greatest(A.DTI_INIAVAILABILITY,NOW()-INTERVAL ? DAY)))),0)  as SegsNOTAVA, ").append("A.IDN_INSTANCE as Instance ").append("FROM OSM_HISTINST_AVAILABILITIES A ").append("WHERE  A.IDN_INSTANCE= ? AND A.IDN_INSTANCE IN (SELECT DISTINCT GI.IDN_INSTANCE FROM OSM_GROUP_INSTANCES GI, OSM_USER_GROUPS UG  ").append("                                                WHERE GI.IDN_GROUP     = UG.IDN_GROUP AND UG.IDN_USER      =  ? ) ").append("   AND A.DTI_FINAVAILABILITY >NOW()-INTERVAL ? DAY ").append("   AND A.DTI_INIAVAILABILITY <  NOW() ").append("GROUP BY A.IDN_INSTANCE").toString();
        data = jdbcTemplate.query(query, new Object[] { days, days, idnInstance, getUser(), days }, new TmpMapper());
        return data;
    }

    public void removeByInstance(String instance) {
    }

    public Integer getAvailabilityByDay(final String idnInstance, final String day) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getTimestamp(1), rs.getInt(2) };
                return tmp;
            }
        }
        String query = new StringBuilder().append("SELECT DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d'), HIA.IND_AVAILABILITY ").append("FROM OSM_HISTINST_AVAILABILITIES HIA ").append("WHERE HIA.IDN_INSTANCE = ? AND ").append("DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d') = ? ").append("AND IND_AVAILABILITY=0 ").append("ORDER BY HIA.DTI_INIAVAILABILITY  DESC ").append("LIMIT 1").toString();
        List listData = jdbcTemplate.query(query, new Object[] { idnInstance, day }, new TmpMapper());
        Integer data = null;
        if (listData.size() == 1) data = 0; else data = -1;
        return data;
    }

    public Integer getLastAvailabilityByDay(final String idnInstance, final String day) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getTimestamp(1), rs.getInt(2) };
                return tmp;
            }
        }
        String query = new StringBuilder().append("SELECT DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d'), HIA.IND_AVAILABILITY  ").append("FROM OSM_HISTINST_AVAILABILITIES HIA ").append("WHERE HIA.IDN_INSTANCE=? ").append("AND DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d') < ? ").append("ORDER BY HIA.DTI_INIAVAILABILITY  DESC ").append("LIMIT 1").toString();
        List listData = jdbcTemplate.query(query, new Object[] { idnInstance, day }, new TmpMapper());
        Integer data = null;
        if (listData.size() == 1) data = (Integer) ((Object[]) listData.get(0))[1]; else data = -1;
        return data;
    }

    public Integer getAvailabilityByHour(final String idnInstance, final String hour) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getTimestamp(1), rs.getInt(2) };
                return tmp;
            }
        }
        String query = new StringBuilder().append("SELECT DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d %H:00:00'), HIA.IND_AVAILABILITY ").append("FROM OSM_HISTINST_AVAILABILITIES HIA ").append("WHERE HIA.IDN_INSTANCE = ? AND ").append("DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d %H') = ? ").append("AND IND_AVAILABILITY=0 ").append("ORDER BY HIA.DTI_INIAVAILABILITY  DESC ").append("LIMIT 1").toString();
        List listData = jdbcTemplate.query(query, new Object[] { idnInstance, hour }, new TmpMapper());
        Integer data = null;
        if (listData.size() == 1) data = 0; else data = -1;
        return data;
    }

    public Integer getLastAvailabilityByHour(final String idnInstance, final String hour) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getTimestamp(1), rs.getInt(2) };
                return tmp;
            }
        }
        String query = new StringBuilder().append("SELECT DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d %H:00:00'), HIA.IND_AVAILABILITY  ").append("FROM OSM_HISTINST_AVAILABILITIES HIA ").append("WHERE HIA.IDN_INSTANCE=? ").append("AND DATE_FORMAT(HIA.DTI_INIAVAILABILITY,'%Y-%m-%d %H') < ? ").append("ORDER BY HIA.DTI_INIAVAILABILITY  DESC ").append("LIMIT 1").toString();
        List listData = jdbcTemplate.query(query, new Object[] { idnInstance, hour }, new TmpMapper());
        Integer data = null;
        if (listData.size() == 1) data = (Integer) ((Object[]) listData.get(0))[1]; else data = -1;
        return data;
    }

    public List getInstanceAvailabilityByRange(String idnInstance, int range) {
        class TmpMapper implements ParameterizedRowMapper {

            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object[] tmp = new Object[] { rs.getInt(1), rs.getInt(2) };
                return tmp;
            }
        }
        List data = null;
        String query = "";
        if (range == 1) {
            query = new StringBuilder().append("SELECT PRIN.DIFF, PRIN.CARDINAL, CAR.CARDINAL ").append("FROM  (SELECT COALESCE(SUM(IF(A.IND_AVAILABILITY=0,1,0)),0) DIFF,").append("         DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? HOUR),'%Y%m%d%H0000'),INTERVAL B.CARDINAL HOUR),").append("         B.cardinal ").append("         FROM OSM_HISTINST_AVAILABILITIES A, OSM_CARDINAL B").append("         WHERE A.IDN_INSTANCE = ? ").append("           AND A.DTI_FINAVAILABILITY >= DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? HOUR),'%Y%m%d%H0000'),INTERVAL B.CARDINAL HOUR)").append("           AND A.DTI_INIAVAILABILITY <  DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? HOUR),'%Y%m%d%H0000'),INTERVAL B.CARDINAL+1 HOUR)").append("           AND B.CARDINAL < ? ").append("GROUP BY DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? HOUR),'%Y%m%d%H0000'),INTERVAL B.CARDINAL HOUR)) PRIN RIGHT JOIN OSM_CARDINAL CAR ON PRIN.CARDINAL=CAR.CARDINAL WHERE CAR.CARDINAL<? ").append("ORDER BY CAR.CARDINAL").toString();
            data = jdbcTemplate.query(query, new Object[] { 23, idnInstance, 23, 23, 24, 23, 24 }, new TmpMapper());
        } else if (range == 7) {
            query = new StringBuilder().append("SELECT PRIN.DIFF, PRIN.CARDINAL, CAR.CARDINAL ").append("FROM  (SELECT COALESCE(SUM(IF(A.IND_AVAILABILITY=0,1,0)),0) DIFF,").append("         DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY),").append("         B.cardinal ").append("         FROM OSM_HISTINST_AVAILABILITIES A, OSM_CARDINAL B").append("         WHERE A.IDN_INSTANCE = ? ").append("           AND A.DTI_FINAVAILABILITY >= DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY)").append("           AND A.DTI_INIAVAILABILITY <  DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL+1 DAY)").append("           AND B.CARDINAL < ? ").append("GROUP BY DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY)) PRIN RIGHT JOIN OSM_CARDINAL CAR ON PRIN.CARDINAL=CAR.CARDINAL WHERE CAR.CARDINAL<? ").append("ORDER BY CAR.CARDINAL").toString();
            data = jdbcTemplate.query(query, new Object[] { 6, idnInstance, 6, 6, 7, 6, 7 }, new TmpMapper());
        } else {
            query = new StringBuilder().append("SELECT PRIN.DIFF, PRIN.CARDINAL, CAR.CARDINAL ").append("FROM  (SELECT COALESCE(SUM(IF(A.IND_AVAILABILITY=0,1,0)),0) DIFF,").append("         DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY),").append("         B.cardinal ").append("         FROM OSM_HISTINST_AVAILABILITIES A, OSM_CARDINAL B").append("         WHERE A.IDN_INSTANCE = ? ").append("           AND A.DTI_FINAVAILABILITY >= DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY)").append("           AND A.DTI_INIAVAILABILITY <  DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL+1 DAY)").append("           AND B.CARDINAL < ? ").append("GROUP BY DATE_ADD(DATE_FORMAT(DATE_ADD(now(), INTERVAL - ? DAY),'%Y%m%d'),INTERVAL B.CARDINAL DAY)) PRIN RIGHT JOIN OSM_CARDINAL CAR ON PRIN.CARDINAL=CAR.CARDINAL WHERE CAR.CARDINAL<? ").append("ORDER BY CAR.CARDINAL").toString();
            data = jdbcTemplate.query(query, new Object[] { 30, idnInstance, 30, 30, 31, 30, 31 }, new TmpMapper());
        }
        return data;
    }
}
