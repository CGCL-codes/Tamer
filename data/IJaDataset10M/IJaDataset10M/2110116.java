package org.osmius.dao.jdbc;

import org.osmius.dao.OsmDDiscoveryTypinstancesDao;
import org.osmius.model.OsmDDiscoveryTypinstances;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OsmDDiscoveryTypinstancesDaoJDBC extends BaseDaoJDBC implements OsmDDiscoveryTypinstancesDao {

    public List getOsmDDiscoveryTypinstances() {
        class TmpMapper implements ParameterizedRowMapper {

            public OsmDDiscoveryTypinstances mapRow(ResultSet rs, int rowNum) throws SQLException {
                OsmDDiscoveryTypinstances tmp = new OsmDDiscoveryTypinstances();
                tmp.setTypInstance(rs.getString(1));
                tmp.setPrtRanges(rs.getString(2));
                tmp.setIndState(rs.getInt(3));
                tmp.setDtiUpdated(rs.getTimestamp(4) != null ? new Date(rs.getTimestamp(4).getTime()) : null);
                return tmp;
            }
        }
        return jdbcTemplate.query("SELECT TYP_INSTANCE,PRT_RANGES,IND_STATE,DTI_UPDATE FROM OSM_D_DISCOVERY_TYPINSTANCES_D", new TmpMapper());
    }

    public OsmDDiscoveryTypinstances getOsmDDiscoveryTypinstance(String typInstance) {
        return null;
    }

    public void updateTypinstance(String typInstance, String ports, int state) {
        jdbcTemplate.update("UPDATE OSM_D_DISCOVERY_TYPINSTANCES_D SET PRT_RANGES=?, IND_STATE=? WHERE TYP_INSTANCE=?", new Object[] { ports, state, typInstance });
    }

    public void updateTypinstancesState(int state) {
        jdbcTemplate.update("UPDATE OSM_D_DISCOVERY_TYPINSTANCES_D SET IND_STATE=? ", new Object[] { state });
    }
}
