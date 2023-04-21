package org.openspp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.openspp.dao.DestinationGroupDAO;
import org.openspp.dto.DestinationGroup;
import org.openspp.dto.Organization;
import org.openspp.util.SpringAppContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class DestinationGroupDAOImpl extends BaseDAOImpl implements DestinationGroupDAO {

    public static final short DEST_GRP_MAX_LEN = 255;

    private static Logger log = Logger.getLogger(DestinationGroupDAOImpl.class.getName());

    public DestinationGroupDAOImpl() {
        super();
    }

    public void addOrUpdateDestinationGroup(DestinationGroup destGrp) {
        log.entering("DestinationGroupDAOImpl", "addOrUpdateDestinationGroup");
        if (destGrp.getDestGrpName() == null) {
            throw new RuntimeException("DestinationGroup name is null.");
        } else if (destGrp.getDestGrpName().length() == 0 || destGrp.getDestGrpName().length() > DEST_GRP_MAX_LEN) {
            throw new RuntimeException("Length of DestinationGroup name should be > 0 and <= " + DEST_GRP_MAX_LEN);
        }
        updateOrganizationId(destGrp);
        try {
            jdbcTemplate.update("INSERT INTO DestinationGroup(DestGrpName, RantId, CDate) VALUES(?,?,?)", new Object[] { destGrp.getDestGrpName(), destGrp.getOrganizationId(), new java.sql.Timestamp(System.currentTimeMillis()) });
        } catch (DuplicateKeyException e) {
            jdbcTemplate.update("UPDATE DestinationGroup Set MDate=? WHERE DestGrpName=? AND RantId=? ;", new Object[] { new java.sql.Timestamp(System.currentTimeMillis()), destGrp.getDestGrpName(), destGrp.getOrganizationId() });
        } catch (RuntimeException e) {
            log.throwing(this.getClass().getName(), "addOrUpdateDestinationGroup", e);
            throw e;
        }
    }

    @Override
    public DestinationGroup getDestinationGroup(DestinationGroup destGrp) {
        log.entering("DestinationGroupDAOImpl", "getDestinationGroup");
        updateOrganizationId(destGrp);
        try {
            DestinationGroup destGrp1 = jdbcTemplate.queryForObject("SELECT DestGrpId, DestGrpName, RantId, CDate, MDate FROM DestinationGroup WHERE  DestGrpName=? AND RantId=? ", new Object[] { destGrp.getDestGrpName(), destGrp.getOrganizationId() }, new RowMapper<DestinationGroup>() {

                public DestinationGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
                    DestinationGroup dg = new DestinationGroup();
                    dg.setDestGrpId(rs.getInt(1));
                    dg.setDestGrpName(rs.getString(2));
                    dg.setOrganizationId(rs.getInt(3));
                    dg.setCreatedDateTime(rs.getDate(4));
                    dg.setModifiedDateTime(rs.getDate(5));
                    return dg;
                }
            });
            return destGrp1;
        } catch (EmptyResultDataAccessException e) {
            log.info("Given DestinationGroup " + destGrp.getDestGrpName() + " does not exist.");
            return null;
        } catch (RuntimeException e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    @Override
    public DestinationGroup getDestinationGroup(String dgName, int rantId) {
        DestinationGroup dg = new DestinationGroup();
        dg.setDestGrpName(dgName);
        dg.setOrganizationId(rantId);
        return getDestinationGroup(dg);
    }

    @Override
    public void deleteDestinationGroup(DestinationGroup destGrp) {
        log.entering("DestinationGroupDAOImpl", "deleteDestinationGroup");
        updateOrganizationId(destGrp);
        jdbcTemplate.update("DELETE FROM DestinationGroup WHERE DestGrpName=? AND RantId=? ", new Object[] { destGrp.getDestGrpName(), destGrp.getOrganizationId() });
    }

    /**
	 * This method find OrganizationId corresponding to OrganizationName.
	 * @param destGrp
	 */
    private void updateOrganizationId(DestinationGroup destGrp) {
        log.entering("DestinationGroupDAOImpl", "updateOrganizationId");
        if (destGrp.getOrganizationId() <= 0) {
            OrganizationDAOImpl dao = (OrganizationDAOImpl) SpringAppContext.getBean("OrganizationDAO");
            Organization org = dao.getOrganizationByName(destGrp.getOrganizationName());
            if (org != null) {
                destGrp.setOrganizationId(org.getOrganizationId());
            } else {
                throw new RuntimeException("Registrant " + destGrp.getOrganizationName() + " does not exist.");
            }
        }
    }
}
