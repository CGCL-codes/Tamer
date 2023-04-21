package com.entelience.provider.mim;

import com.entelience.directory.Group;
import com.entelience.directory.Location;
import com.entelience.directory.PeopleFactory;
import com.entelience.objects.Incident;
import com.entelience.objects.directory.GroupInformation;
import com.entelience.objects.directory.LocationInfoLine;
import com.entelience.provider.DbIncident;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.sql.UsesDbObject;
import com.entelience.util.Version;
import com.entelience.util.Config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * organisations and locations in the mim schema
 */
public class ImOrgDb extends UsesDbObject {

    private static final org.apache.log4j.Logger _logger = com.entelience.util.Logs.getProbeLogger();

    private PreparedStatement pstGetGroupId;

    private PreparedStatement pstAddGroupId;

    private PreparedStatement pstSetEGroupId;

    private PreparedStatement pstGetLocationId;

    private PreparedStatement pstAddLocationId;

    private PreparedStatement pstSetELocationId;

    private PreparedStatement pstAddGroupMemberId;

    private PreparedStatement pstGetGroupMemberId;

    private PreparedStatement pstGetLocationMemberId;

    private PreparedStatement pstAddLocationMemberId;

    private PreparedStatement pstGetEsisLocationByName;

    private PreparedStatement pstGetEsisGroupByName;

    /**
     */
    @Override
    protected void prepare() throws SQLException {
        Db db = getDb();
        pstGetGroupId = db.prepareStatement("SELECT t_department_id FROM mim.t_department WHERE lower(name) = lower(?)");
        pstAddGroupId = db.prepareStatement("INSERT INTO mim.t_department (name, e_cie_group_id) VALUES (?, ?) RETURNING t_department_id");
        pstSetEGroupId = db.prepareStatement("UPDATE mim.t_department SET e_cie_group_id = ? WHERE t_department_id = ?");
        pstGetLocationId = db.prepareStatement("SELECT t_location_id FROM mim.t_location WHERE lower(name) = lower(?)");
        pstAddLocationId = db.prepareStatement("INSERT INTO mim.t_location (name, e_location_id) VALUES (?, ?) RETURNING t_location_id");
        pstSetELocationId = db.prepareStatement("UPDATE mim.t_location SET e_location_id = ? WHERE t_location_id = ?");
        pstGetGroupMemberId = db.prepareStatement("SELECT t_app_login_id FROM mim.t_department_user WHERE t_department_id = ? AND t_app_login_id = ?");
        pstAddGroupMemberId = db.prepareStatement("INSERT INTO mim.t_department_user (t_department_id, t_app_login_id) VALUES (?, ?)");
        pstGetLocationMemberId = db.prepareStatement("SELECT t_app_login_id FROM mim.t_location_user WHERE t_location_id = ? AND t_app_login_id = ?");
        pstAddLocationMemberId = db.prepareStatement("INSERT INTO mim.t_location_user (t_location_id, t_app_login_id) VALUES (?, ?)");
        pstGetEsisLocationByName = db.prepareStatement("SELECT e_location_id, location_name, MIN(rank) FROM (" + " SELECT e_location_id, location_name, 1 AS rank FROM e_location where lower(location_name) = lower(?) " + " UNION SELECT e_location_id, alias_name, 1 AS rank FROM e_location_alias WHERE lower(alias_name) = lower(?) " + " UNION SELECT e_location_id, location_name, 10 FROM e_location where lower(location_name || '%') LIKE lower(?) " + " UNION SELECT e_location_id, alias_name, 10 AS rank FROM e_location_alias WHERE lower(alias_name || '%') LIKE lower(?) " + " UNION SELECT e_location_id, location_name, 100 FROM e_location where lower('%' || location_name  || '%') LIKE lower(?) " + " UNION SELECT e_location_id, alias_name, 100 AS rank FROM e_location_alias WHERE lower('%' || alias_name || '%') LIKE lower(?))" + "  AS tmp GROUP BY 1,2 ORDER BY 3,1");
        pstGetEsisGroupByName = db.prepareStatement("SELECT e_group_id, group_name, MIN(rank) FROM ( SELECT e_group_id, group_name, 1 AS rank FROM e_company_group where lower(group_name) = lower(?) UNION SELECT e_group_id, group_name, 10 FROM e_company_group where lower(group_name) = lower(?) UNION SELECT e_group_id, group_name, 100 FROM e_company_group where lower(group_name) = lower(?)) AS tmp GROUP BY 1,2 ORDER BY 3,1");
    }

    private final Map<String, Integer> groupCache = new HashMap<String, Integer>();

    private final Map<String, Integer> locationCache = new HashMap<String, Integer>();

    private final List<String> unmatchedLocations = new ArrayList<String>();

    private final List<String> unmatchedDepartments = new ArrayList<String>();

    /**
     */
    public void sendIncidents(String probeName, boolean incidentOnUnmatchedLocations, boolean incidentOnUnmatchedDepartments) throws Exception {
        boolean incident = false;
        StringBuffer msg = new StringBuffer();
        StringBuffer summ = new StringBuffer();
        Collections.sort(unmatchedLocations);
        if (incidentOnUnmatchedLocations && unmatchedLocations != null && !unmatchedLocations.isEmpty()) {
            incident = true;
            msg.append("List of new MIM locations fetched by probe that could not be resolved against existing locations or their aliases : ");
            summ.append("Number of new MIM locations without ESIS location matching : ").append(unmatchedLocations.size()).append('\n');
            for (Iterator<String> it = unmatchedLocations.iterator(); it.hasNext(); ) {
                msg.append('\n').append(it.next());
            }
            msg.append("\n\n");
        }
        Collections.sort(unmatchedDepartments);
        if (incidentOnUnmatchedDepartments && unmatchedDepartments != null && !unmatchedDepartments.isEmpty()) {
            incident = true;
            msg.append("List of new MIM departments fetched by probe that could not be resolved agains existing departments : ");
            summ.append("Number of new MIM departments without ESIS department matching : ").append(unmatchedDepartments.size()).append('\n');
            for (Iterator<String> it = unmatchedDepartments.iterator(); it.hasNext(); ) {
                msg.append('\n').append(it.next());
            }
        }
        if (!incident) {
            return;
        }
        Incident i = new Incident();
        i.setProbeMessage(msg.toString());
        i.setProbeName(probeName);
        i.setEsisVersion(Version.getVersionString());
        i.setSummary(summ.toString());
        DbIncident.saveIncident(getDb(), i, new ArrayList<Exception>());
    }

    /**
     */
    public List<String> getUnmatchedLocations() {
        return unmatchedLocations;
    }

    /**
     */
    public List<String> getUnmatchedDepartments() {
        return unmatchedDepartments;
    }

    /**
     */
    public int getOrAddGroup(String name, boolean createNewGroups) throws SQLException {
        Db db = getDb();
        try {
            db.enter();
            Integer id = groupCache.get(name);
            if (id != null) return id;
            _logger.debug("Looking for MIM Group (" + name + ")");
            Integer eGroupId = lookupEsisGroupId(name, createNewGroups);
            pstGetGroupId.setString(1, name);
            id = DbHelper.getKey(pstGetGroupId);
            if (id != null) {
                if (eGroupId != null) {
                    pstSetEGroupId.setInt(1, eGroupId);
                    pstSetEGroupId.setInt(2, id);
                    db.executeUpdate(pstSetEGroupId);
                }
                groupCache.put(name, id);
                return id;
            }
            if (eGroupId == null) unmatchedDepartments.add(name);
            _logger.info("Adding new MIM Group (" + name + ")");
            pstAddGroupId.setString(1, name);
            pstAddGroupId.setObject(2, eGroupId);
            id = DbHelper.getIntKey(pstAddGroupId);
            groupCache.put(name, id);
            return id;
        } finally {
            db.exit();
        }
    }

    /**
     * try to get a esis group id that match this mim group name
     */
    public Integer lookupEsisGroupId(String name, boolean createNewGroups) throws SQLException {
        Db db = getDb();
        try {
            db.enter();
            pstGetEsisGroupByName.setString(1, name);
            pstGetEsisGroupByName.setString(2, name + "%");
            pstGetEsisGroupByName.setString(3, "%" + name + "%");
            ResultSet rs = db.executeQuery(pstGetEsisGroupByName);
            List<Integer> groupIds = new ArrayList<Integer>();
            List<String> groupNames = new ArrayList<String>();
            Integer previousRank = null;
            if (rs.next()) {
                do {
                    int grpId = rs.getInt(1);
                    String grpName = rs.getString(2);
                    int rank = rs.getInt(3);
                    if (previousRank == null) {
                        previousRank = rank;
                        groupIds.add(grpId);
                        groupNames.add(grpName);
                    } else {
                        if (previousRank == rank) {
                            groupIds.add(grpId);
                            groupNames.add(grpName);
                        } else {
                            break;
                        }
                    }
                } while (rs.next());
            } else {
                _logger.debug("ImOrgDb - lookupEsisGroupId : no row found");
            }
            if (groupIds.isEmpty()) {
                _logger.warn("MIM group (" + name + ") doesnt resolve to any known ESIS group");
                if (createNewGroups) {
                    GroupInformation gi = new GroupInformation();
                    gi.setCompanyId(db.getCompanyId());
                    gi.setName(name);
                    return Group.createCompanyGroup(db, gi);
                } else {
                    return null;
                }
            }
            if (groupIds.size() > 1) {
                _logger.error("MIM group (" + name + ")resolve to several ESIS groups " + groupNames + ": none will be used");
                return null;
            }
            _logger.info("MIM group (" + name + ") resolve to ESIS group (" + groupNames.get(0) + ", " + groupIds.get(0) + ")");
            return groupIds.get(0);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public int getOrAddLocation(String name, boolean createNewLocations) throws Exception {
        Db db = getDb();
        try {
            db.enter();
            Integer id = locationCache.get(name);
            if (id != null) return id;
            _logger.debug("Looking up for MIM Location (" + name + ")");
            Integer eLocationId = lookupEsisLocationId(name, createNewLocations);
            pstGetLocationId.setString(1, name);
            id = DbHelper.getKey(pstGetLocationId);
            if (id != null) {
                if (eLocationId != null) {
                    pstSetELocationId.setInt(1, eLocationId);
                    pstSetELocationId.setInt(2, id);
                    db.executeUpdate(pstSetELocationId);
                }
                locationCache.put(name, id);
                return id;
            }
            if (eLocationId == null) unmatchedLocations.add(name);
            _logger.info("Adding new MIM Location (" + name + ")");
            pstAddLocationId.setString(1, name);
            pstAddLocationId.setObject(2, eLocationId);
            id = DbHelper.getIntKey(pstAddLocationId);
            locationCache.put(name, id);
            return id;
        } finally {
            db.exit();
        }
    }

    /**
     * try to get a esis location id that match this mim location name
     */
    public Integer lookupEsisLocationId(String name, boolean createNewLocations) throws Exception {
        Db db = getDb();
        try {
            _logger.debug("Looking up for ESIS location (" + name + ")");
            db.enter();
            pstGetEsisLocationByName.setString(1, name);
            pstGetEsisLocationByName.setString(2, name);
            pstGetEsisLocationByName.setString(3, name + "%");
            pstGetEsisLocationByName.setString(4, name + "%");
            pstGetEsisLocationByName.setString(5, "%" + name + "%");
            pstGetEsisLocationByName.setString(6, "%" + name + "%");
            ResultSet rs = db.executeQuery(pstGetEsisLocationByName);
            List<Integer> locIds = new ArrayList<Integer>();
            List<String> locNames = new ArrayList<String>();
            Integer previousRank = null;
            if (rs.next()) {
                do {
                    int locId = rs.getInt(1);
                    String locName = rs.getString(2);
                    int rank = rs.getInt(3);
                    if (previousRank == null) {
                        previousRank = rank;
                        locIds.add(locId);
                        locNames.add(locName);
                    } else {
                        if (previousRank == rank) {
                            locIds.add(locId);
                            locNames.add(locName);
                        } else {
                            break;
                        }
                    }
                } while (rs.next());
            } else {
                _logger.debug("ImOrgDb - lookupEsisLocationId : no row found");
            }
            if (locIds.isEmpty()) {
                _logger.warn("MIM Location (" + name + ") doesn't resolve to any known ESIS location");
                if (createNewLocations) {
                    LocationInfoLine loc = new LocationInfoLine();
                    loc.setCompanyId(db.getCompanyId());
                    loc.setLocationName(name);
                    loc.setCountryIsoCode(Config.getProperty(db, "com.entelience.esis.mim.locationDefaultCountry", 756));
                    return Location.createLocation(db, loc, PeopleFactory.anonymousId);
                } else {
                    return null;
                }
            }
            if (locIds.size() > 1) {
                _logger.warn("MIM Location (" + name + ") resolve to several ESIS locations " + locNames + ": none will be used");
                return null;
            }
            _logger.info("MIM Location (" + name + ") resolve to ESIS location (" + locNames.get(0) + ")");
            return locIds.get(0);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public void addUserToGroup(int userId, int groupId) throws SQLException {
        Db db = getDb();
        try {
            _logger.debug("Adding MIM user " + userId + " to Group " + groupId);
            db.enter();
            pstGetGroupMemberId.setInt(1, groupId);
            pstGetGroupMemberId.setInt(2, userId);
            if (DbHelper.noRows(pstGetGroupMemberId)) {
                pstAddGroupMemberId.setInt(1, groupId);
                pstAddGroupMemberId.setInt(2, userId);
                db.executeUpdate(pstAddGroupMemberId);
            }
        } finally {
            db.exit();
        }
    }

    /**
     */
    public void addUserToLocation(int userId, int locId) throws SQLException {
        Db db = getDb();
        try {
            _logger.debug("Adding MIM user " + userId + " to Location " + locId);
            db.enter();
            pstGetLocationMemberId.setInt(1, locId);
            pstGetLocationMemberId.setInt(2, userId);
            if (DbHelper.noRows(pstGetLocationMemberId)) {
                pstAddLocationMemberId.setInt(1, locId);
                pstAddLocationMemberId.setInt(2, userId);
                db.executeUpdate(pstAddLocationMemberId);
            }
        } finally {
            db.exit();
        }
    }
}
