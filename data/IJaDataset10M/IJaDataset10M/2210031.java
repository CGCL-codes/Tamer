package com.entelience.directory;

import org.apache.log4j.Logger;
import com.entelience.sql.DbHelper;
import com.entelience.util.Logs;
import com.entelience.util.DateHelper;
import com.entelience.util.Config;
import com.entelience.provider.DbCommonOrganisation;
import com.entelience.provider.geography.DbGeography;
import com.entelience.objects.AuthorizationException;
import com.entelience.objects.DropDown;
import com.entelience.objects.directory.UnnecessaryLocationException;
import com.entelience.objects.directory.LocationInfoLine;
import com.entelience.objects.directory.LocationDetail;
import com.entelience.objects.directory.LocationNetwork;
import com.entelience.objects.directory.CompanyInfoLine;
import com.entelience.objects.directory.LocationHistory;
import com.entelience.objects.directory.LocationNetworkHistory;
import com.entelience.objects.directory.LocationAliasHistory;
import com.entelience.objects.net.NetIP;
import com.entelience.sql.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * A location is the slowest business geographical unit. It can be a site, a datacenter or even a room or whatever
 * suits the situation. The location table is shared by companies, hence a location name must be unique for a company.
 * A location can be linked to IP ranges, domain name (ie. sub.example.ch), ISO region, etc.
 * The IP and domain linkage is critical as it enables to compute metrics per location, so any change has impact on metrics.
 */
public class Location {

    private Location() {
    }

    protected static final Logger _logger = Logs.getLogger();

    /**
     * Search for a location given a location name and a company. Search on the location name
	 * and the aliases.
     *
     * @return	the location id or null if it doesn't exists
     */
    public static Integer lookupLocationName(Db db, String locationName, int companyId) throws Exception {
        if (locationName == null) return null;
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT l.e_location_id FROM e_location l LEFT JOIN e_location_alias la ON la.e_location_id = l.e_location_id " + " WHERE (lower(l.location_name) = lower(?) OR lower(la.alias_name) = lower(?) ) AND e_company_id = ?");
            pst.setString(1, DbHelper.nullify(locationName));
            pst.setString(2, DbHelper.nullify(locationName));
            pst.setInt(3, companyId);
            return DbHelper.getKey(pst);
        } finally {
            db.exit();
        }
    }

    public static Integer lookupLocationName(Db db, String locationName) throws Exception {
        return lookupLocationName(db, locationName, db.getCompanyId());
    }

    /**
     * Ignore aliases
     */
    public static Integer lookupLocationNameOnly(Db db, String locationName) throws Exception {
        if (locationName == null) return null;
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT l.e_location_id FROM e_location l " + " WHERE lower(l.location_name) = lower(?) AND e_company_id = ?");
            pst.setString(1, DbHelper.nullify(locationName));
            pst.setInt(2, db.getCompanyId());
            return DbHelper.getKey(pst);
        } finally {
            db.exit();
        }
    }

    /**
     * Returns the name of a location of a company by looking for its location id.
     * 
     *  @throws Exception if the location cannot be found
     **/
    public static String lookupLocationId(Db db, Integer id, Integer companyId) throws Exception {
        if (id == null || companyId == null) return null;
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT location_name FROM e_location WHERE e_location_id = ? AND e_company_id = ?");
            pst.setInt(1, id.intValue());
            pst.setInt(2, companyId);
            String ret = DbHelper.getString(pst);
            if (ret == null) throw new Exception("Location (" + id + ") not found for company (" + companyId + ")");
            return ret;
        } finally {
            db.exit();
        }
    }

    /**
	 * Checks that a country code is valid
	 */
    public static void checkCountryIsoValid(Db db, int countryIso) throws Exception {
        if (DbGeography.getCountry(db, countryIso) == null) throw new Exception("The given country ISO code (" + countryIso + ") does not exist");
    }

    /**
     * check the timezone can be used in the country.
     */
    public static void checkTimezoneCompatibleWithCountry(Db db, int countryIso, String tzName) throws Exception {
        if (!DbGeography.checkTimezoneCompatibleWithCountry(db, countryIso, tzName)) throw new Exception("Timezone (" + tzName + ") cannot be used in a location in country (" + countryIso + ")");
    }

    /**
     * check the region can be used with this country.
     */
    public static void checkRegionMatchCountry(Db db, Integer regionId, int countryIso) throws Exception {
        if (!DbGeography.checkRegionMatchCountry(db, regionId, countryIso)) throw new Exception("Region (" + regionId + ") doesn't belong to country (" + countryIso + ")");
    }

    /**
	 * check the region is a valid one
	 */
    public static void checkRegionIdValid(Db db, Integer rId) throws Exception {
        if (!DbGeography.isRegionIdValid(db, rId)) throw new Exception("The given region id (" + rId + ") does not exist ");
    }

    /**
	 * Returns the number of business zone linked to the location
	 */
    public static int countBusinessZonesForLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(bzl.e_business_zone_id) FROM e_business_zone_location bzl " + " INNER JOIN e_business_zone bz ON bz.e_business_zone_id = bzl.e_business_zone_id " + " WHERE e_location_id = ? AND NOT bz.deleted");
            pst.setInt(1, locationId);
            Integer count = DbHelper.getKey(pst);
            if (count == null) throw new Exception("Error when getting group counter for a location");
            return count.intValue();
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of groups linked to the location
	 */
    public static int countGroupsForLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(e_group_id) FROM e_group_location WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            Integer count = DbHelper.getKey(pst);
            if (count == null) throw new Exception("Error when getting group counter for a location");
            return count.intValue();
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of active users for the location
	 */
    public static int countUsersForLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(e_people_id) FROM e_people WHERE e_location_id = ? AND NOT disabled");
            pst.setInt(1, locationId);
            Integer count = DbHelper.getKey(pst);
            if (count == null) throw new Exception("Error when getting user counter for location (" + locationId + ")");
            return count.intValue();
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of assets link to this location via the location inet ranges 
	 * and the asset IP.
	 */
    public static int countAssetsForLocation(Db db, int locationId, boolean active) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(e_asset_id) FROM e_asset ast IN");
            pst.setInt(1, locationId);
            Integer count = DbHelper.getKey(pst);
            if (count == null) throw new Exception("Error when getting user counter for a location");
            return count.intValue();
        } finally {
            db.exit();
        }
    }

    /**
	 * Update the net.t_ip_location which keeps track of linkage in between IP
	 * and locations
	 */
    public static int updateIpLinkedToLocation(Db db, Integer locationId) throws Exception {
        _logger.info("Updating IP to Location linkage for location (" + locationId + ")");
        try {
            db.enter();
            PreparedStatement pstDrop = db.prepareStatement("DELETE FROM net.t_ip_location " + (locationId == null ? "" : " WHERE e_location_id = ?"));
            if (locationId != null) pstDrop.setInt(1, locationId);
            int before = db.executeUpdate(pstDrop);
            PreparedStatement pst = db.prepareStatement(" SELECT hi.t_ip_id, l.e_location_id FROM net.t_ip hi " + " INNER JOIN e_location_network l ON hi.ip << l.net_range " + (locationId == null ? "" : " WHERE l.e_location_id = ?"));
            if (locationId != null) pst.setInt(1, locationId);
            int res = 0;
            ResultSet rs = db.executeQuery(pst);
            if (rs != null && rs.next()) {
                PreparedStatement pstIns = db.prepareStatement(" INSERT INTO net.t_ip_location (t_ip_id, e_location_id) VALUES (?, ?)");
                PreparedStatement pstDel = db.prepareStatement(" DELETE FROM net.t_ip_location WHERE t_ip_id = ?");
                do {
                    if (locationId != null) {
                        pstDel.setInt(1, rs.getInt(1));
                        db.executeUpdate(pstDel);
                    }
                    pstIns.setInt(1, rs.getInt(1));
                    pstIns.setInt(2, rs.getInt(2));
                    res = db.executeUpdate(pstIns);
                    if (res < 0) throw new IllegalStateException("Error during the update of IP linked to location (" + locationId + ")");
                } while (rs.next());
            }
            if (locationId != null) {
                int after = countIPsForLocation(db, locationId);
                _logger.info("Location (" + locationId + ") had before update (" + before + ") IP addresses linked and after (" + after + ")");
            }
            return res;
        } finally {
            db.exit();
        }
    }

    /**
	 * Removes IP to location that don't match any more either the location
	 *  the ip range is not assigned anymore to this location
	 */
    public static int cleanupIpLinkedToLocation(Db db) throws Exception {
        _logger.info("Removing deprecated IP to Location linkage");
        try {
            db.enter();
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM net.t_ip_location WHERE (t_ip_id, e_location_id)  NOT IN " + " ( SELECT il.t_ip_id, il.e_location_id FROM net.t_ip_location il " + " INNER JOIN net.t_ip hi ON hi.t_ip_id = il.t_ip_id " + " INNER JOIN e_location_network ln ON ln.e_location_id = il.e_location_id WHERE hi.ip << ln.net_range )");
            int res = db.executeUpdate(pstDel);
            if (res < 0) throw new IllegalStateException("Error during the removal of the IP not anymore linked to location");
            _logger.debug("IP-to-location records (" + res + ") have been removed");
            return res;
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns true if the IP match the range of at least one location
	 */
    public static boolean isIPLinkedToLocation(Db db, NetIP ip, Integer locationId) throws Exception {
        if (ip == null) throw new IllegalArgumentException("The IP cannot be null");
        if (ip.getIpId() == null && ip.getIp() == null) throw new IllegalArgumentException("The IP id and ip cannot be null");
        try {
            db.enter();
            int inLoc = 0;
            PreparedStatement pstIpInLocation;
            if (ip.getIpId() == null) {
                pstIpInLocation = db.prepareStatement("SELECT COUNT(*) FROM e_location_network WHERE cidr(?) << net_range " + (locationId == null ? "" : " AND e_location_id = ? "));
                pstIpInLocation.setString(1, ip.getIp());
            } else {
                pstIpInLocation = db.prepareStatement("SELECT COUNT(t_ip_id) FROM net.t_ip_location WHERE t_ip_id = ? " + (locationId == null ? "" : " AND e_location_id = ? "));
                pstIpInLocation.setInt(1, ip.getIpId());
            }
            if (locationId != null) pstIpInLocation.setInt(2, locationId);
            inLoc = DbHelper.getIntKey(pstIpInLocation);
            return (inLoc == 1);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of assets link to this location via the location inet ranges 
	 * and the asset IP.
	 */
    public static int countIPsForLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(t_ip_id) FROM net.t_ip_location WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            Integer count = DbHelper.getKey(pst);
            _logger.debug("As of today location (" + locationId + ") has (" + count.toString() + ") linked IP addresses");
            return (count == null ? 0 : count.intValue());
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of IPs linked to at least one location
	 */
    public static int countIPsForAllLocations(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(t_ip_id) FROM net.t_ip_location");
            Integer count = DbHelper.getKey(pst);
            _logger.debug("As of today location there are (" + count + ") IP to locations records");
            return (count == null ? 0 : count.intValue());
        } finally {
            db.exit();
        }
    }

    /**
     * Returns the list of IP without locations
     */
    public static List<NetIP> getIpWithoutLocation(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT host(t.ip), t.t_ip_id, t.creation_date FROM net.t_ip t " + " WHERE t.t_ip_id NOT IN (SELECT t_ip_id FROM net.t_ip_location) AND rfc1918 IS TRUE");
            ResultSet rs = db.executeQuery(pst);
            List<NetIP> l = new ArrayList<NetIP>();
            if (rs.next()) {
                do {
                    NetIP nip = new NetIP();
                    nip.setIp(rs.getString(1));
                    nip.setIpId(rs.getInt(2));
                    nip.setRfc1918(true);
                    nip.setCreationDate(rs.getDate(3));
                    l.add(nip);
                } while (rs.next());
            } else {
                _logger.info("All rfc1918 IPs are linked to a location !");
            }
            return l;
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of unique IPs linked to at least one location
	 */
    public static int countUniqueIPsForAllLocations(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT t_ip_id) FROM net.t_ip_location");
            Integer count = DbHelper.getKey(pst);
            _logger.debug("As of today location there are (" + count + ") unique IP addresses linked to locations");
            return (count == null ? 0 : count.intValue());
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the number of unique locations linked to at least one ip
	 */
    public static int countLocationsWithLinkedIP(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT e_location_id) FROM net.t_ip_location");
            Integer count = DbHelper.getKey(pst);
            _logger.debug("As of today location there are (" + count + ") locations with linked IP addresses");
            return (count == null ? 0 : count.intValue());
        } finally {
            db.exit();
        }
    }

    public static LocationInfoLine getLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, " + " cy.country_iso, cy.country_shortname, zipcode, l.e_region_id, r.name, l.timezone, l.deleted, " + " cast(null as boolean), l.e_raci_obj FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " WHERE l.e_location_id = ? ORDER BY l.location_name");
            pst.setInt(1, locationId);
            ResultSet rs = db.executeQuery(pst);
            List<LocationInfoLine> res = toLocationInfoLine(rs);
            if (res == null || res.size() == 0) {
                return null;
            } else {
                return res.get(0);
            }
        } finally {
            db.exit();
        }
    }

    public static LocationDetail getLocationDetail(Db db, int locationId) throws Exception {
        try {
            db.enter();
            LocationInfoLine lil = getLocation(db, locationId);
            if (lil == null) return null;
            LocationDetail ld = new LocationDetail();
            ld.setLocation(lil);
            if (lil.getRegionId() == null) {
                ld.setCountry(DbGeography.getCountry(db, lil.getCountryIsoCode()));
            } else {
                ld.setRegion(DbGeography.getRegion(db, lil.getRegionId().intValue()));
            }
            if (lil.getTimezone() != null) ld.setTimezone(DbGeography.getTimezone(lil.getTimezone()));
            ld.setNetworks(listInetRanges(db, locationId));
            PreparedStatement pst = db.prepareStatement("SELECT (SELECT COUNT(*) FROM e_people p WHERE NOT deleted AND p.e_location_id = l.e_location_id), " + " (SELECT COUNT(*) FROM e_group_location gl WHERE gl.e_location_id = l.e_location_id) " + " FROM e_location l WHERE l.e_location_id = ?");
            pst.setInt(1, locationId);
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                ld.setNbUsers(rs.getInt(1));
                ld.setNbGroups(rs.getInt(2));
            } else {
                _logger.warn("Couldn't find details of location (" + locationId + ")");
            }
            DropDown[] doms = listDomains(db, locationId);
            List<String> domains = new ArrayList<String>();
            if (doms != null) {
                for (int i = 0; i < doms.length; i++) domains.add(doms[i].getLabel());
            }
            ld.setDomains(domains);
            ld.setAliases(listAliases(db, locationId));
            return ld;
        } finally {
            db.exit();
        }
    }

    /**
     * mainDb : create the location
     * slaveDb : get geographic informations
     */
    public static int createLocation(Db db, LocationInfoLine loc, int modifier) throws Exception {
        try {
            db.enter();
            if (loc == null) throw new IllegalArgumentException("Cannot create a null location");
            if (loc.getCompanyId() == null) loc.setCompanyId(Integer.valueOf(PeopleFactory.getPeopleInfoLine(db, modifier).getE_company_id()));
            if (!Company.isCompany(loc.getCompanyId())) throw new IllegalArgumentException("The provided company Id (" + loc.getCompanyId() + ") could not be matched as an existing company");
            String locName = DbHelper.nullify(loc.getLocationName());
            if (locName == null) throw new Exception("Cannot create a location without a name");
            Integer existingId = lookupLocationName(db, locName, loc.getCompanyId().intValue());
            if (existingId != null) throw new IllegalArgumentException("The location (" + loc.getLocationName() + ") already exists for the company (" + loc.getCompanyId() + ")");
            if (loc.getCountryIsoCode() > 0) {
                checkCountryIsoValid(db, loc.getCountryIsoCode());
                if (loc.getRegionId() != null) {
                    checkRegionIdValid(db, loc.getRegionId());
                    checkRegionMatchCountry(db, loc.getRegionId(), loc.getCountryIsoCode());
                }
            }
            String tz = DbHelper.nullify(loc.getTimezone());
            if (tz != null) checkTimezoneCompatibleWithCountry(db, loc.getCountryIsoCode(), tz);
            PreparedStatement pst = db.prepareStatement("INSERT INTO e_location (e_company_id, location_name, building, site, city, country_iso, zipcode, e_region_id, timezone, deleted) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, false) RETURNING e_location_id");
            pst.setInt(1, loc.getCompanyId().intValue());
            pst.setString(2, locName);
            pst.setString(3, DbHelper.nullify(loc.getBuilding()));
            pst.setString(4, DbHelper.nullify(loc.getSite()));
            pst.setString(5, DbHelper.nullify(loc.getCity()));
            pst.setInt(6, loc.getCountryIsoCode());
            pst.setString(7, DbHelper.nullify(loc.getZipCode()));
            pst.setObject(8, loc.getRegionId());
            pst.setString(9, DbHelper.nullify(tz));
            Integer id = DbHelper.getKey(pst);
            if (id == null) throw new Exception("Error when getting Id of last inserted location (" + loc.toString() + ")");
            addLocationHistory(db, id, modifier);
            _logger.info("Location " + loc.toString() + " has been created");
            return id.intValue();
        } finally {
            db.exit();
        }
    }

    public static List<LocationInfoLine> toLocationInfoLine(ResultSet rs) throws Exception {
        List<LocationInfoLine> ret = new ArrayList<LocationInfoLine>();
        if (rs != null && rs.next()) {
            do {
                LocationInfoLine lil = new LocationInfoLine();
                lil.setLocationId(Integer.valueOf(rs.getInt(1)));
                lil.setCompanyId(Integer.valueOf(rs.getInt(2)));
                lil.setCompanyName(rs.getString(3));
                lil.setLocationName(rs.getString(4));
                lil.setBuilding(rs.getString(5));
                lil.setSite(rs.getString(6));
                lil.setCity(rs.getString(7));
                lil.setCountryIsoCode(rs.getInt(8));
                lil.setCountryName(rs.getString(9));
                lil.setZipCode(rs.getString(10));
                lil.setRegionId((Integer) rs.getObject(11));
                lil.setRegionName(rs.getString(12));
                lil.setTimezone(rs.getString(13));
                lil.setDeleted(rs.getBoolean(14));
                lil.setRemovable((Boolean) rs.getObject(15));
                lil.setRaciObjectId((Integer) rs.getObject(16));
                ret.add(lil);
            } while (rs.next());
        }
        return ret;
    }

    /**
     * Locations for a selected group
     */
    public static List<LocationInfoLine> getLocationsInDepartment(Db db, int departmentId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode," + " l.e_region_id, r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " LEFT JOIN e_group_location gl ON gl.e_location_id = l.e_location_id " + " WHERE gl.e_group_id =? AND NOT l.deleted ORDER BY l.location_name");
            pst.setInt(1, departmentId);
            ResultSet rs = db.executeQuery(pst);
            return toLocationInfoLine(rs);
        } finally {
            db.exit();
        }
    }

    /**
     * Locations Id for a selected group or null if there's none
     */
    protected static List<Integer> getLocationsIdInDepartment(Db db, int departmentId) throws Exception {
        List<Integer> lid = null;
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT e_location_id FROM e_group_location  WHERE e_group_id =?");
            pst.setInt(1, departmentId);
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                lid = new ArrayList<Integer>();
                do {
                    lid.add(rs.getInt(1));
                } while (rs.next());
            }
            return lid;
        } finally {
            db.exit();
        }
    }

    /**
     * Locations for a selected people
     */
    public static LocationInfoLine getLocationOfPeople(Db db, int peopleId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode," + " l.e_region_id, r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj  FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " LEFT JOIN e_people pl ON pl.e_location_id = l.e_location_id " + " WHERE pl.e_people_id =? AND NOT l.deleted ORDER BY l.location_name");
            pst.setInt(1, peopleId);
            ResultSet rs = db.executeQuery(pst);
            List<LocationInfoLine> lil = toLocationInfoLine(rs);
            return (lil.size() > 0 ? lil.get(0) : null);
        } finally {
            db.exit();
        }
    }

    /**
     * Locations that are not linked to this groups/departments
     */
    public static List<LocationInfoLine> getLocationsNotInDepartment(Db db, int departmentId, int companyId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode, " + " l.e_region_id, r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj  FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " WHERE c.e_company_id = ? AND l.e_location_id NOT IN (SELECT e_location_id FROM e_group_location WHERE e_group_id = ?) " + " AND NOT l.deleted ORDER BY l.location_name");
            pst.setInt(2, departmentId);
            pst.setInt(1, companyId);
            ResultSet rs = db.executeQuery(pst);
            return toLocationInfoLine(rs);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the list of location directly linked to the business zone
	 */
    public static List<LocationInfoLine> getLocationsForBusinessZone(Db db, int businessZoneId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode, " + " l.e_region_id, r.name, l.timezone, l.deleted, (bzl.e_main_zone_id IS NULL), l.e_raci_obj  FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " LEFT JOIN e_business_zone_location bzl ON bzl.e_location_id = l.e_location_id " + " WHERE bzl.e_business_zone_id =? AND NOT l.deleted ORDER BY l.location_name");
            pst.setInt(1, businessZoneId);
            ResultSet rs = db.executeQuery(pst);
            return toLocationInfoLine(rs);
        } finally {
            db.exit();
        }
    }

    /**
     * return a list of the locations that are not in any business zone, but could be inserted in
     */
    public static List<LocationInfoLine> getLocationsNotInBusinessZone(Db db, int companyId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT DISTINCT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode," + " l.e_region_id, r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj  FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " WHERE l.e_location_id NOT IN (SELECT e_location_id FROM e_business_zone_location ) " + " AND NOT l.deleted AND l.e_company_id = ? ORDER BY l.location_name");
            pst.setInt(1, companyId);
            ResultSet rs = db.executeQuery(pst);
            return toLocationInfoLine(rs);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the list of locations for the company
	 */
    public static List<LocationInfoLine> getLocationsForCompany(Db db, int companyId, Boolean showDisabled) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, " + " cy.country_shortname, zipcode, l.e_region_id, r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj  " + " FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " WHERE l.e_company_id =? ");
            if (showDisabled != null) sql.append((showDisabled.booleanValue() ? " AND l.deleted " : " AND NOT l.deleted "));
            sql.append(" ORDER BY l.location_name ASC");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            pst.setInt(1, companyId);
            ResultSet rs = db.executeQuery(pst);
            return toLocationInfoLine(rs);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the list of locations (details) for the company
	 */
    public static List<LocationDetail> getLocationsDetailForCompany(Db db, int companyId, Boolean showDisabled) throws Exception {
        try {
            db.enter();
            List<LocationDetail> lld = new ArrayList<LocationDetail>();
            StringBuffer sql = new StringBuffer("SELECT l.e_location_id FROM e_location l WHERE l.e_company_id = ?");
            if (showDisabled != null) sql.append((showDisabled.booleanValue() ? " AND l.deleted " : " AND NOT l.deleted "));
            PreparedStatement pst = db.prepareStatement(sql.toString());
            pst.setInt(1, companyId);
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    LocationDetail ld = getLocationDetail(db, rs.getInt(1));
                    lld.add(ld);
                } while (rs.next());
            }
            return lld;
        } finally {
            db.exit();
        }
    }

    /**
     * the possible locations for a user depend of the departments locations he is member :
     * - if he is not a member of a dept : all of its company locations
     * - if its depts have no locations : all of its company locations
     * - else : all locations of its depts (UNION, not INTER)
     *
     *
     */
    public static List<LocationInfoLine> getPossibleLocationsForUser(Db db, int userId, int companyId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT l.e_location_id, c.e_company_id, c.name, l.location_name, l.building, l.site, l.city, cy.country_iso, cy.country_shortname, zipcode, l.e_region_id, " + " r.name, l.timezone, l.deleted, cast(null as boolean), l.e_raci_obj  FROM e_location l " + " INNER JOIN e_company c ON c.e_company_id = l.e_company_id " + " LEFT JOIN e_country cy ON cy.country_iso = l.country_iso " + " LEFT JOIN e_region r ON r.e_region_id = l.e_region_id " + " WHERE l.e_location_id IN (SELECT egl.e_location_id FROM e_group_location egl " + " INNER JOIN e_company_group dept ON dept.e_group_id = egl.e_group_id " + " INNER JOIN e_group_to_people gtp ON gtp.e_group_id = dept.e_group_id " + " INNER JOIN e_people p ON p.e_people_id = gtp.e_people_id " + " WHERE p.e_people_id = ? AND dept.is_company_dept) ");
            pst.setInt(1, userId);
            ResultSet rs = db.executeQuery(pst);
            List<LocationInfoLine> ret = toLocationInfoLine(rs);
            return (ret.isEmpty() ? getLocationsForCompany(db, companyId, Boolean.FALSE) : ret);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns true if the location is at least linked to one business zone either directly or via a group
	 * @param	db				database handler
	 * @param	locationId		the location unique id
	 * @parama	viaGroup		set to true for linkage via group or false for direct linkage
	 */
    public static boolean isLocationInBusinessZone(Db db, int locationId, Boolean viaGroup) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(e_business_zone_id) FROM e_business_zone_location WHERE e_location_id = ? ");
        if (viaGroup != null) sb.append(" AND via_groups = ?");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement(sb.toString());
            pst.setInt(1, locationId);
            if (viaGroup != null) pst.setBoolean(2, viaGroup);
            int zid = DbHelper.getIntKey(pst);
            return (zid == 0 ? false : true);
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns true if the location is at least linked to one business zone either directly or via a group
	 * @param	db				database handler
	 * @param	locationId		the location unique id
	 * @parama	viaGroup		set to true for linkage via group or false for direct linkage
	 */
    public static boolean isLocationInBusinessZone(Db db, int locationId, int businessZoneId, Boolean viaGroup) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(e_business_zone_id) FROM e_business_zone_location WHERE e_location_id = ? AND e_business_zone_id = ? ");
        if (viaGroup != null) sb.append(" AND via_groups = ?");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement(sb.toString());
            pst.setInt(1, locationId);
            pst.setInt(2, businessZoneId);
            if (viaGroup != null) pst.setBoolean(3, viaGroup);
            int zid = DbHelper.getIntKey(pst);
            return (zid == 0 ? false : true);
        } finally {
            db.exit();
        }
    }

    /**
	 * Sets a location as disabled. Location are not deleted from the database
	 * but marked as disabled. This is needed as data
	 * may still refer to it.
	 */
    private static void deleteLocation(Db db, int locationId, int modifier) throws Exception {
        try {
            db.enter();
            int nbUsers = countUsersForLocation(db, locationId);
            if (nbUsers > 0) throw new AuthorizationException("Cannot delete location " + locationId + ". There are (" + nbUsers + ") users in it");
            int nbGroups = countGroupsForLocation(db, locationId);
            if (nbGroups > 0) throw new AuthorizationException("Cannot delete location " + locationId + ". There are (" + nbGroups + ") groups in it");
            int nbBusinessZones = countBusinessZonesForLocation(db, locationId);
            if (nbBusinessZones > 0) throw new AuthorizationException("Cannot delete location " + locationId + ". It is included in (" + nbBusinessZones + ") business zones");
            PreparedStatement pstChk = db.prepareStatement("SELECT org.e_organisation_id FROM e_organisation org " + " LEFT JOIN audit.e_audit_organisation ao ON org.e_organisation_id = ao.e_organisation_id " + " LEFT JOIN audit.e_audit_rec_organisation aro ON org.e_organisation_id = aro.e_organisation_id " + " LEFT JOIN audit.e_audit_action_organisation aao ON org.e_organisation_id = aao.e_organisation_id " + " WHERE (e_audit_id IS NOT NULL OR e_audit_rec_id IS NOT NULL OR e_audit_action_id IS NOT NULL) AND org.e_location_id = ?");
            pstChk.setInt(1, locationId);
            if (!DbHelper.noRows(pstChk)) throw new AuthorizationException("Cannot delete location (" + locationId + ") as it is linked with opened audits");
            PreparedStatement pst = db.prepareStatement("UPDATE e_location SET deleted = true WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error (" + res + ") when disabling location (" + locationId + ")");
            if (Config.getProperty(db, "com.entelience.esis.directory.Location.purgeNetworkOnDelete", true)) {
                PreparedStatement pstClearNetwork = db.prepareStatement("SELECT e_location_network_id FROM e_location_network WHERE e_location_id = ?");
                pstClearNetwork.setInt(1, locationId);
                ResultSet rs = db.executeQuery(pstClearNetwork);
                if (rs.next()) {
                    do {
                        Location.removeInetRange(db, rs.getInt(1), modifier);
                    } while (rs.next());
                }
            }
            if (Config.getProperty(db, "com.entelience.esis.directory.Location.purgeAliasOnDelete", true)) {
                PreparedStatement pstClearAlias = db.prepareStatement("SELECT alias_name FROM e_location_alias WHERE e_location_id = ?");
                pstClearAlias.setInt(1, locationId);
                ResultSet rs = db.executeQuery(pstClearAlias);
                if (rs.next()) {
                    do {
                        Location.removeAlias(db, locationId, rs.getString(1), modifier);
                    } while (rs.next());
                }
            }
            if (Config.getProperty(db, "com.entelience.esis.directory.Location.purgeDomainOnDelete", true)) {
                Integer orgId = DbCommonOrganisation.getOrganisationId(db, null, null, Integer.valueOf(locationId));
                if (orgId != null) {
                    DropDown[] dp = DbCommonOrganisation.listDomains(db, orgId.intValue());
                    for (int i = 0; i < dp.length; ++i) DbCommonOrganisation.removeDomain(db, orgId.intValue(), dp[i].getData(), modifier);
                }
            }
            _logger.warn("Location " + locationId + " has been disabled");
        } finally {
            db.exit();
        }
    }

    /**
	 * Enable a location. Note that it's network ranges and potentially aliases have been
	 * deleted, however they are in the history.
	 */
    private static void enableLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("UPDATE e_location SET deleted = false WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error (" + res + ") when enabling location (" + locationId + ")");
            _logger.warn("Location " + locationId + " has been enabled");
        } finally {
            db.exit();
        }
    }

    public static int dropAllUsersFromLocation(Db mainDb, Db slaveDb, int locationId, Boolean removeUnnecessaryLocations, int modifier) throws Exception {
        try {
            mainDb.enter();
            slaveDb.enter();
            List<Integer> users = new ArrayList<Integer>();
            PreparedStatement pst1 = mainDb.prepareStatement("SELECT e_people_id FROM e_people WHERE e_location_id = ?");
            pst1.setInt(1, locationId);
            ResultSet rs = mainDb.executeQuery(pst1);
            if (rs.next()) {
                do {
                    users.add(rs.getInt(1));
                } while (rs.next());
            } else {
                _logger.debug("No users to remove from location");
            }
            PreparedStatement pst = mainDb.prepareStatement("UPDATE e_people SET e_location_id = null WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            int res = mainDb.executeUpdate(pst);
            _logger.warn(res + " users have been removed from location " + locationId);
            for (Iterator<Integer> it = users.iterator(); it.hasNext(); ) {
                PeopleFactory.addPeopleHistory(mainDb, it.next(), modifier);
            }
            PreparedStatement pstCheck = slaveDb.prepareStatement("SELECT e_group_id FROM e_group_location WHERE e_location_id = ?");
            pstCheck.setInt(1, locationId);
            if (!DbHelper.noRows(pstCheck)) {
                if (removeUnnecessaryLocations == null) throw new UnnecessaryLocationException("Some locations associated with departments are no longer used by departments members ");
                if (removeUnnecessaryLocations.booleanValue()) dropAllGroupsFromLocation(slaveDb, locationId);
            }
            return res;
        } finally {
            mainDb.exit();
            slaveDb.exit();
        }
    }

    public static int dropAllGroupsFromLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("DELETE FROM e_group_location WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            int res = db.executeUpdate(pst);
            _logger.warn(res + " groups have been removed from location " + locationId);
            return res;
        } finally {
            db.exit();
        }
    }

    public static int dropAllBusinessZonesFromLocation(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("DELETE FROM e_business_zone_location WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            int res = db.executeUpdate(pst);
            _logger.warn(res + " business zones have been removed from location " + locationId);
            return res;
        } finally {
            db.exit();
        }
    }

    /**
	 * Update a location
	 */
    public static void updateLocation(Db db, LocationInfoLine loc, int modifier) throws Exception {
        if (loc == null) throw new IllegalArgumentException("Cannot update a null location");
        try {
            db.enter();
            LocationInfoLine oldLoc = getLocation(db, loc.getLocationId().intValue());
            if (oldLoc == null) throw new Exception("Cannot update a non existent location (" + loc.getLocationId() + ")");
            if (oldLoc.equals(loc)) {
                _logger.warn("Skipping location update for (" + oldLoc.getLocationName() + ") has no changes were detected");
                return;
            }
            String locName = DbHelper.nullify(loc.getLocationName());
            if (locName == null) throw new Exception("Cannot update a location (" + loc.getLocationId() + ") without a name");
            Integer existingId = lookupLocationName(db, locName, loc.getCompanyId().intValue());
            if (existingId != null && existingId.intValue() != loc.getLocationId().intValue()) throw new Exception("Cannot rename the location (" + loc.getLocationId() + ") to (" + locName + ") as it already exists");
            if (loc.getCountryIsoCode() > 0) {
                checkCountryIsoValid(db, loc.getCountryIsoCode());
                if (loc.getRegionId() != null) {
                    checkRegionIdValid(db, loc.getRegionId());
                    checkRegionMatchCountry(db, loc.getRegionId(), loc.getCountryIsoCode());
                }
            } else {
                _logger.warn("The location (" + locName + ") has no country code defined");
            }
            String tz = DbHelper.nullify(loc.getTimezone());
            if (tz != null) checkTimezoneCompatibleWithCountry(db, loc.getCountryIsoCode(), tz);
            PreparedStatement pst = db.prepareStatement("UPDATE e_location SET location_name = ?, building = ?, site = ?, city = ?, country_iso = ?, zipcode = ?," + " e_region_id = ?, timezone = ? WHERE e_location_id = ?");
            pst.setString(1, locName);
            pst.setString(2, DbHelper.nullify(loc.getBuilding()));
            pst.setString(3, DbHelper.nullify(loc.getSite()));
            pst.setString(4, DbHelper.nullify(loc.getCity()));
            pst.setInt(5, (loc.getCountryIsoCode() > 0 ? loc.getCountryIsoCode() : 0));
            pst.setString(6, DbHelper.nullify(loc.getZipCode()));
            pst.setObject(7, loc.getRegionId());
            pst.setString(8, DbHelper.nullify(tz));
            pst.setInt(9, loc.getLocationId().intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error (" + res + ") during the update of location (" + locName + ")");
            _logger.info("Location (" + loc.getLocationId() + ") has been updated");
            if (loc.isDeleted() && !oldLoc.isDeleted()) {
                deleteLocation(db, loc.getLocationId().intValue(), modifier);
            } else if (!loc.isDeleted() && oldLoc.isDeleted()) {
                enableLocation(db, loc.getLocationId().intValue());
            }
            addLocationHistory(db, loc.getLocationId(), modifier);
        } finally {
            db.exit();
        }
    }

    /**
	 * Generates an history record for the location
	 */
    protected static void addLocationHistory(Db db, int locationId, int modifier) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("INSERT INTO e_location_history (modifier, e_location_id, location_name, building, site, city, country_iso, zipcode, e_region_id, timezone, deleted) " + " SELECT ?, e_location_id, location_name, building, site, city, country_iso, zipcode, e_region_id, timezone, deleted " + " FROM e_location WHERE e_location_id = ?");
            pst.setInt(1, modifier);
            pst.setInt(2, locationId);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error (" + res + ") when adding a location history to location (" + locationId + ")");
        } finally {
            db.exit();
        }
    }

    /**
     * Returns the history for location, most recent first. 
	 * If lastN is set it will returns the last nth history records only, and from offset if set. See PageHelper.
     */
    public static List<LocationHistory> getLocationHistory(Db db, int locationId, Integer lastN, Integer offsetN) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT l.e_location_id, l.location_name, l.building, l.site, l.city, l.country_iso, l.zipcode, l.e_region_id, ");
            sql.append("l.timezone, l.deleted, l.modifier, l.change_date, user_name as modifierName  FROM e_location_history l");
            sql.append(" LEFT JOIN e_people ON e_people_id=modifier");
            sql.append(" WHERE l.e_location_id = ?");
            sql.append(" ORDER BY change_date DESC");
            if (lastN != null) sql.append(" LIMIT " + lastN.toString());
            if (offsetN != null) sql.append(" OFFSET " + offsetN.toString());
            PreparedStatement pst = db.prepareStatement(sql.toString());
            pst.setInt(1, locationId);
            ResultSet rs = db.executeQuery(pst);
            List<LocationHistory> res = new ArrayList<LocationHistory>();
            if (rs == null || !rs.next()) {
                _logger.info("No history records exists for location (" + locationId + ")");
            } else {
                LocationHistory lh;
                do {
                    lh = new LocationHistory();
                    lh.setLocationId(Integer.valueOf(rs.getInt(1)));
                    lh.setLocationName(rs.getString(2));
                    lh.setBuilding(rs.getString(3));
                    lh.setSite(rs.getString(4));
                    lh.setCity(rs.getString(5));
                    lh.setCountryIso(rs.getString(6));
                    lh.setZipcode(rs.getString(7));
                    lh.setRegionId(rs.getInt(8));
                    lh.setTimezone(rs.getString(9));
                    lh.setDeleted(rs.getBoolean(10));
                    lh.setModifierId(rs.getInt(11));
                    lh.setChangeDate(DateHelper.toDateOrNull(rs.getTimestamp(12)));
                    lh.setModifierName(rs.getString(13));
                    res.add(lh);
                } while (rs.next());
            }
            return res;
        } finally {
            db.exit();
        }
    }

    /**
     * Ensure that the network range is not in conflict with some from others location. 
     * For the location adding a "larger" network range is allowed (expanding).
     *
     * @throws AuthorizationException if the network range is not available
     */
    public static void checkNetworkIsAvailable(Db db, int locationId, String inet) throws Exception {
        PreparedStatement pst1 = db.prepareStatement("SELECT e_location_network_id FROM e_location_network WHERE net_range <<= cidr(?) AND e_location_id <> ?");
        pst1.setString(1, DbHelper.nullify(inet));
        pst1.setInt(2, locationId);
        ResultSet rs = db.executeQuery(pst1);
        if (rs != null && rs.next()) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            do {
                sb.append((i > 0 ? ", " : ""));
                sb.append(rs.getInt(1));
            } while (rs.next());
            throw new AuthorizationException("Cannot assign network (" + inet + ") to location (" + locationId + ") as it is already assigned or contained by others locations (" + sb.toString() + ")");
        }
        PreparedStatement pst2 = db.prepareStatement("SELECT e_location_network_id FROM e_location_network WHERE net_range >> cidr(?) AND e_location_id <> ?");
        pst2.setString(1, DbHelper.nullify(inet));
        pst2.setInt(2, locationId);
        rs = db.executeQuery(pst2);
        if (rs != null && rs.next()) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            do {
                sb.append((i > 0 ? ", " : ""));
                sb.append(rs.getInt(1));
            } while (rs.next());
            throw new AuthorizationException("Cannot assign network (" + inet + ") to location (" + locationId + ") as it overlaps networks from others locations (" + sb.toString() + ")");
        }
        PreparedStatement pst3 = db.prepareStatement("SELECT e_location_network_id FROM e_location_network WHERE cidr(?) <<= net_range AND e_location_id = ?");
        pst3.setString(1, DbHelper.nullify(inet));
        pst3.setInt(2, locationId);
        rs = db.executeQuery(pst3);
        if (rs != null && rs.next()) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            do {
                sb.append((i > 0 ? ", " : ""));
                sb.append(rs.getInt(1));
            } while (rs.next());
            throw new AuthorizationException("Cannot assign network (" + inet + ") to location (" + locationId + ") as it overlaps networks in this locations (" + sb.toString() + ")");
        }
    }

    /**
	 * Associate a network range (VLAN) with a location. When network range are associated to a location, then assets
	 * with IPs in the range are automatically linked to this location.
	 */
    public static int addInetRange(Db db, int locationId, String inet, String name, int modifier) throws Exception {
        _logger.info("Adding network range (inet :" + inet + ", name :" + name + ") to location (" + locationId + ")");
        try {
            db.enter();
            checkNetworkValidity(db, inet);
            LocationInfoLine loc = getLocation(db, locationId);
            if (loc == null) throw new IllegalArgumentException("Attempting to add network range (" + inet + ") to a non existent location (" + locationId + ")");
            if (loc.isDeleted()) throw new Exception("Cannot add a network range (" + inet + ") to a disabled location (" + locationId + ")");
            checkNetworkIsAvailable(db, locationId, inet);
            PreparedStatement pstAdd = db.prepareStatement("INSERT INTO e_location_network (net_range, e_location_id, net_name) VALUES (cidr(?), ?, ?) RETURNING e_location_network_id");
            pstAdd.setString(1, DbHelper.nullify(inet));
            pstAdd.setInt(2, locationId);
            pstAdd.setString(3, DbHelper.nullify(name));
            int id = DbHelper.getIntKey(pstAdd);
            _logger.warn("Network range (" + inet + ") has been added to location (" + locationId + ") with id (" + id + ")");
            Location.addNetworkLocationHistory(db, id, false, modifier);
            Location.cleanOverlapNetworks(db, id, locationId, inet, modifier);
            updateIpLinkedToLocation(db, locationId);
            return id;
        } finally {
            db.exit();
        }
    }

    /**
	* remove old network included in the new network
	*/
    public static void cleanOverlapNetworks(Db db, Integer locationNetworkId, int locationId, String inet, int modifier) throws Exception {
        _logger.debug("Cleaning overlapped networks for (network id :" + locationNetworkId + ", location id :" + locationId + ", inet :" + inet + ")");
        PreparedStatement pst = db.prepareStatement("SELECT e_location_network_id FROM e_location_network WHERE " + " e_location_id = ? AND net_range << cidr(?) AND e_location_network_id <> ?");
        pst.setInt(1, locationId);
        pst.setString(2, DbHelper.nullify(inet));
        pst.setInt(3, locationNetworkId.intValue());
        ResultSet rs = db.executeQuery(pst);
        if (rs.next()) {
            _logger.warn("Fixing the fact that network (location network id : " + locationNetworkId + ", inet :" + inet + ") is overlapping with other networks for location (" + locationId + ")");
            do {
                _logger.debug("Found that network location (" + rs.getInt(1) + ") overlaps with (location network id : " + locationNetworkId + ", inet :" + inet + ") or location (" + locationId + ")");
                removeInetRange(db, rs.getInt(1), modifier);
            } while (rs.next());
        }
    }

    /**
	 * Adds an history record for this (network, location) to keep track of changes. 
	 */
    private static void addNetworkLocationHistory(Db db, int locationNetworkId, boolean delete, int modifier) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("INSERT INTO e_location_network_history (modifier, e_location_id, net_range, deleted) " + " SELECT ?, e_location_id, net_range, ? " + " FROM e_location_network WHERE e_location_network_id = ?");
            pst.setInt(1, modifier);
            pst.setBoolean(2, delete);
            pst.setInt(3, locationNetworkId);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error (" + res + ") when adding a location network history to location/network (" + locationNetworkId + ")");
        } finally {
            db.exit();
        }
    }

    /**
	* Checks if a network range is valid. It use the result of the Postgresql cidr function
	* 
	* @throws IllegalArgumentException if the network range is invalid
	*/
    public static void checkNetworkValidity(Db db, String networkAddr) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT cidr(?)");
            pst.setString(1, DbHelper.nullify(networkAddr));
            db.executeQuery(pst);
        } catch (Exception e) {
            throw new IllegalArgumentException("The provided network address/range (" + networkAddr + ") is invalid (" + e.getMessage() + ")");
        } finally {
            db.exit();
        }
    }

    /**
	 * Removes an inet range of the location. This has metric impacts. Net ranges are used
	 * to link ip address to location hence this will impact metrics on next probe run as 
	 * well has immediate impact for assets.
	 */
    public static void removeInetRange(Db db, int locationNetworkId, int modifier) throws Exception {
        _logger.trace("Removing inet range (" + locationNetworkId + ")");
        try {
            db.enter();
            LocationNetwork ln = getInetRange(db, locationNetworkId);
            if (ln == null) throw new IllegalArgumentException("Cannot remove a non existing Location/Network (" + locationNetworkId + ")");
            Location.addNetworkLocationHistory(db, locationNetworkId, true, modifier);
            PreparedStatement pst = db.prepareStatement("DELETE FROM e_location_network WHERE e_location_network_id = ?");
            pst.setInt(1, locationNetworkId);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error when removing a network range (" + locationNetworkId + ") for a location ");
            cleanOverlapNetworks(db, locationNetworkId, ln.getLocationId(), ln.getNetwork(), modifier);
            updateIpLinkedToLocation(db, ln.getLocationId());
            _logger.warn("Network range (" + ln.getNetwork() + ") has been removed of location (" + ln.getLocationId() + ")");
        } finally {
            db.exit();
        }
    }

    /**
	 * Update a network/location
     *
     * @throws IllegalArgumentException if the LocationNetwork is invalid
     * @throws Exception if the network range is invalid or overlapps another network range
	 */
    public static void updateInetRange(Db db, LocationNetwork ln, int modifier) throws Exception {
        _logger.debug("Updating inet range (" + ln.getLocationNetworkId() + ", " + ln.getNetwork() + ") for location (" + ln.getLocationId() + ")");
        try {
            db.enter();
            if (ln == null) throw new IllegalArgumentException("Cannot update a null LocationNetwork");
            if (ln.getNetwork() == null) throw new IllegalArgumentException("Cannot update location (" + ln.getLocationId() + ") with a null network range");
            checkNetworkValidity(db, ln.getNetwork());
            checkNetworkIsAvailable(db, ln.getLocationId(), ln.getNetwork());
            PreparedStatement pstUpd = db.prepareStatement("UPDATE e_location_network SET net_range = cidr(?), net_name = ? WHERE e_location_network_id = ?");
            pstUpd.setString(1, DbHelper.nullify(ln.getNetwork()));
            pstUpd.setString(2, DbHelper.nullify(ln.getName()));
            pstUpd.setInt(3, ln.getLocationNetworkId());
            int res = db.executeUpdate(pstUpd);
            if (res != 1) throw new IllegalStateException("Error when updating a network range for a location");
            _logger.info("Network range (" + ln.getLocationNetworkId() + ") for location (" + ln.getLocationId() + ") has been changed to (" + ln.getNetwork() + ")");
            Location.addNetworkLocationHistory(db, ln.getLocationNetworkId(), false, modifier);
            Location.cleanOverlapNetworks(db, ln.getLocationNetworkId(), ln.getLocationId(), ln.getNetwork(), modifier);
            updateIpLinkedToLocation(db, ln.getLocationId());
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns a specific LocationNetwork record or null if it doesn't exist
	 */
    public static LocationNetwork getInetRange(Db db, int locationNetworkId) throws Exception {
        LocationNetwork ln = null;
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT text(net_range), e_location_id, net_name FROM e_location_network WHERE e_location_network_id = ?");
            pst.setInt(1, locationNetworkId);
            ResultSet rs = db.executeQuery(pst);
            if (rs != null && rs.next()) {
                ln = new LocationNetwork();
                ln.setLocationNetworkId(locationNetworkId);
                ln.setNetwork(rs.getString(1));
                ln.setLocationId(rs.getInt(2));
                ln.setName(rs.getString(3));
            }
            return ln;
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the location of network ranges for a given location
	 */
    public static List<LocationNetwork> listInetRanges(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT e_location_network_id, text(net_range), e_location_id, net_name FROM e_location_network WHERE e_location_id = ?");
            pst.setInt(1, locationId);
            List<LocationNetwork> l = new ArrayList<LocationNetwork>();
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    LocationNetwork ln = new LocationNetwork();
                    ln.setLocationNetworkId(rs.getInt(1));
                    ln.setNetwork(rs.getString(2));
                    ln.setLocationId(rs.getInt(3));
                    ln.setName(rs.getString(4));
                    l.add(ln);
                } while (rs.next());
            } else {
                _logger.debug("listInetRanges - no network found for location (" + locationId + ")");
            }
            return l;
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the network range history for a location
	 */
    public static List<LocationNetworkHistory> getInetRangeHistory(Db db, int locationId, Integer lastN, Integer offsetN) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT text(ln.net_range), ln.change_date, ln.deleted, ln.modifier, ln.e_location_id, l.location_name, user_name as modifierName, ln.net_name" + " FROM e_location_network_history ln " + " LEFT JOIN e_location l ON ln.e_location_id = l.e_location_id" + " LEFT JOIN e_people ON e_people_id = modifier" + " WHERE ln.e_location_id = ? ORDER BY ln.change_date DESC");
            if (lastN != null) sql.append(" LIMIT " + lastN.toString());
            if (offsetN != null) sql.append(" OFFSET " + offsetN.toString());
            PreparedStatement pst = db.prepareStatement(sql.toString());
            pst.setInt(1, locationId);
            List<LocationNetworkHistory> l = new ArrayList<LocationNetworkHistory>();
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    LocationNetworkHistory lnh = new LocationNetworkHistory();
                    lnh.setNetRange(rs.getString(1));
                    lnh.setChangeDate(DateHelper.toDateOrNull(rs.getTimestamp(2)));
                    lnh.setDeleted(rs.getBoolean(3));
                    lnh.setModifierId(rs.getInt(4));
                    lnh.setLocationId(rs.getInt(5));
                    lnh.setLocationName(rs.getString(6));
                    lnh.setModifierName(rs.getString(7));
                    lnh.setNetworkName(rs.getString(8));
                    l.add(lnh);
                } while (rs.next());
            } else {
                _logger.debug("No inet range history record found for location (" + locationId + ")");
            }
            return l;
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the list of domains for this location in a list suitable for a DropDown menu
	 */
    public static DropDown[] listDomains(Db db, int locationId) throws Exception {
        Integer orgId = DbCommonOrganisation.getOrganisationId(db, null, null, locationId);
        if (orgId == null) return new DropDown[0];
        return DbCommonOrganisation.listDomains(db, orgId);
    }

    public static boolean addDomain(Db db, int locationId, String domain, int modifier) throws Exception {
        int crossDomainId = Company.addOrGetDomainId(db, domain);
        int orgId = DbCommonOrganisation.getOrCreateOrganisation(db, null, null, locationId);
        return DbCommonOrganisation.addDomain(db, orgId, crossDomainId, modifier);
    }

    public static boolean addDomain(Db db, int locationId, int crossDomainId, int modifier) throws Exception {
        int orgId = DbCommonOrganisation.getOrCreateOrganisation(db, null, null, locationId);
        return DbCommonOrganisation.addDomain(db, orgId, crossDomainId, modifier);
    }

    public static boolean removeDomain(Db db, int locationId, int crossDomainId, int modifier) throws Exception {
        int orgId = DbCommonOrganisation.getOrCreateOrganisation(db, null, null, locationId);
        return DbCommonOrganisation.removeDomain(db, orgId, crossDomainId, modifier);
    }

    public static boolean removeDomain(Db db, int locationId, String domain, int modifier) throws Exception {
        int crossDomainId = Company.addOrGetDomainId(db, domain);
        int orgId = DbCommonOrganisation.getOrCreateOrganisation(db, null, null, locationId);
        return DbCommonOrganisation.removeDomain(db, orgId, crossDomainId, modifier);
    }

    /**
	 * Returns the list of aliases for a given location
	 */
    public static List<String> listAliases(Db db, int locationId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT alias_name FROM e_location_alias WHERE e_location_id = ? ORDER BY alias_name ASC");
            pst.setInt(1, locationId);
            List<String> l = new ArrayList<String>();
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    l.add(rs.getString(1));
                } while (rs.next());
            } else {
                _logger.debug("No aliases found for location (" + locationId + ")");
            }
            return l;
        } finally {
            db.exit();
        }
    }

    /**
	 * Adds an alias to a location.
	 * 
	 * @return	true if the alias was successfully added
	 */
    public static boolean addAlias(Db db, int locationId, String _alias, int modifier) throws Exception {
        String alias = DbHelper.nullify(_alias);
        if (alias == null) throw new IllegalArgumentException("Cannot add a null alias to location " + locationId);
        try {
            db.enter();
            LocationInfoLine loc = getLocation(db, locationId);
            if (loc == null) throw new IllegalArgumentException("Cannot add alias (" + alias + ") to a non-existing location (" + locationId + ")");
            if (loc.isDeleted()) throw new Exception("Cannot add an alias (" + alias + ") to a disabled location (" + loc.getLocationName() + ")");
            Integer existingId = lookupLocationName(db, alias, loc.getCompanyId());
            if (existingId != null) throw new AuthorizationException("Cannot add alias (" + alias + ") to location (" + locationId + ") as it is already an alias of location (" + existingId + ")");
            PreparedStatement pstGetAlias = db.prepareStatement("SELECT location_name FROM e_location_alias la " + "INNER JOIN e_location l ON l.e_location_id = la.e_location_id WHERE lower(alias_name) = lower(?)");
            pstGetAlias.setString(1, alias);
            String existingLocName = DbHelper.getString(pstGetAlias);
            if (existingLocName != null) throw new AuthorizationException("Cannot add (" + alias + ") is already an alias for another company location [" + existingLocName + "]");
            PreparedStatement pstAdd = db.prepareStatement("INSERT INTO e_location_alias (alias_name, e_location_id) VALUES (?, ?)");
            pstAdd.setString(1, alias);
            pstAdd.setInt(2, locationId);
            int res = db.executeUpdate(pstAdd);
            if (res != 1) throw new IllegalStateException("Unexpected return code (" + res + ") from db.executeUpdate when adding alias (" + alias + ") to location (" + loc.getLocationName() + ")");
            Location.addAliasHistory(db, locationId, alias, false, modifier);
            _logger.info("Alias (" + alias + ") added to location (" + loc.getLocationName() + ")");
            return true;
        } finally {
            db.exit();
        }
    }

    /**
	 * Generates an history record for the alias
	 */
    private static void addAliasHistory(Db db, int locationId, String alias, boolean delete, int modifier) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("INSERT INTO e_location_alias_history (alias_name, e_location_id, deleted, modifier) VALUES (?, ?, ?, ?)");
            pst.setString(1, DbHelper.nullify(alias));
            pst.setInt(2, locationId);
            pst.setBoolean(3, delete);
            pst.setInt(4, modifier);
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Unexpected return code (" + res + ") from db.executeUpdate when adding history record for alias (" + alias + ") of location (" + locationId + ")");
        } finally {
            db.exit();
        }
    }

    /**
	 * Returns the aliases history of a location
	 */
    public static List<LocationAliasHistory> getLocationAliasHistory(Db db, int locationId, Integer lastN, Integer offsetN) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT la.change_date, la.e_location_id, la.alias_name, la.deleted, la.modifier, l.location_name, user_name as modifierName" + " FROM e_location_alias_history la " + " LEFT JOIN e_location l ON la.e_location_id = l.e_location_id" + " LEFT JOIN e_people ON e_people_id = modifier" + " WHERE la.e_location_id = ? ORDER BY la.change_date DESC");
            if (lastN != null) sql.append(" LIMIT " + lastN.toString());
            if (offsetN != null) sql.append(" OFFSET " + offsetN.toString());
            PreparedStatement pst = db.prepareStatement(sql.toString());
            pst.setInt(1, locationId);
            List<LocationAliasHistory> l = new ArrayList<LocationAliasHistory>();
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    LocationAliasHistory lah = new LocationAliasHistory();
                    lah.setChangeDate(DateHelper.toDateOrNull(rs.getTimestamp(1)));
                    lah.setLocationId(rs.getInt(2));
                    lah.setAlias(rs.getString(3));
                    lah.setDeleted(rs.getBoolean(4));
                    lah.setModifierId(rs.getInt(5));
                    lah.setLocationName(rs.getString(6));
                    lah.setModifierName(rs.getString(7));
                    l.add(lah);
                } while (rs.next());
            } else {
                _logger.debug("No aliases history record found for location (" + locationId + ")");
            }
            return l;
        } finally {
            db.exit();
        }
    }

    /**
	 * Update an alias. The checks are case insensitive of course, and alias must be unique.
	 */
    public static boolean updateAlias(Db db, int locationId, String _aliasOld, String _aliasNew, int modifier) throws Exception {
        try {
            db.enter();
            String aliasOld = DbHelper.nullify(_aliasOld);
            if (aliasOld == null) throw new IllegalArgumentException("Cannot update a null alias");
            String aliasNew = DbHelper.nullify(_aliasNew);
            if (aliasNew == null) throw new IllegalArgumentException("Cannot update alias (" + aliasOld + ") to null");
            if (aliasNew.equalsIgnoreCase(aliasOld)) {
                _logger.info("Attempting a no change on alias (" + aliasNew + ")");
                return true;
            }
            LocationInfoLine loc = getLocation(db, locationId);
            if (loc == null) throw new IllegalArgumentException("Cannot update alias (" + aliasOld + ") to (" + aliasNew + ") for an unknown location (" + locationId + ")");
            if (loc.isDeleted()) throw new IllegalArgumentException("Cannot update alias (" + aliasOld + ") to (" + aliasNew + ") for a disabled location (" + locationId + ")");
            Integer existingId = lookupLocationName(db, aliasNew, loc.getCompanyId());
            if (existingId == null) {
                PreparedStatement pstGetAlias = db.prepareStatement("SELECT location_name FROM e_location_alias la INNER JOIN e_location l ON l.e_location_id = la.e_location_id WHERE lower(alias_name) = lower(?)");
                pstGetAlias.setString(1, aliasNew);
                String existingLocName = DbHelper.getString(pstGetAlias);
                if (existingLocName != null) throw new AuthorizationException("Cannot use alias (" + aliasNew + ") to update alias (" + aliasOld + ") as it is already an alias of [" + existingLocName + "]");
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE e_location_alias SET alias_name=? WHERE alias_name=? AND e_location_id=?");
                pstUpdate.setString(1, aliasNew);
                pstUpdate.setString(2, aliasOld);
                pstUpdate.setInt(3, locationId);
                int res = db.executeUpdate(pstUpdate);
                if (res != 1) throw new IllegalStateException("Unexpected return code (" + res + ") from db.executeUpdate when updating alias (" + aliasOld + ") to (" + aliasNew + ") for location (" + locationId + "; " + loc.getLocationName() + ")");
                Location.addAliasHistory(db, locationId, aliasNew, false, modifier);
                _logger.warn("Alias (" + aliasOld + ") has been updated to alias (" + aliasNew + ") for location (" + locationId + "; " + loc.getLocationName() + ")");
            } else throw new AuthorizationException("Cannot update alias (" + aliasOld + ") to alias (" + aliasNew + ") for location (" + loc.getLocationName() + ") as it is already an alias of location");
            return true;
        } finally {
            db.exit();
        }
    }

    /**
	 * Delete an alias from a location.
	 *
	 * @throws IllegalArgumentException  if the alias or location is null
	 */
    public static boolean removeAlias(Db db, int locationId, String _alias, int modifier) throws Exception {
        try {
            db.enter();
            String alias = DbHelper.nullify(_alias);
            if (alias == null) throw new IllegalArgumentException("Cannot remove a null alias from location (" + locationId + ")");
            LocationInfoLine loc = getLocation(db, locationId);
            if (loc == null) throw new IllegalArgumentException("Cannot remove alias (" + alias + ") of an unknow location (" + locationId + ")");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM e_location_alias WHERE lower(alias_name) = lower(?) AND e_location_id = ?");
            pstDel.setString(1, alias);
            pstDel.setInt(2, locationId);
            int res = db.executeUpdate(pstDel);
            if (res == 1) {
                Location.addAliasHistory(db, locationId, alias, true, modifier);
                _logger.warn("Alias (" + alias + ") removed from location (" + locationId + "; " + loc.getLocationName() + ")");
                return true;
            } else if (res == 0) {
                _logger.error("Alias (" + alias + ") couldn't be removed from location (" + locationId + "; " + loc.getLocationName() + ")");
                return false;
            }
            throw new IllegalStateException("Unexpected return code [" + res + "] from db.executeUpdate when removing alias (" + alias + ") from location (" + loc.getLocationName() + ")");
        } finally {
            db.exit();
        }
    }
}
