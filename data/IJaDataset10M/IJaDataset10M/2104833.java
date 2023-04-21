package com.entelience.servlet.admin;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.objects.directory.BusinessZone;
import com.entelience.objects.directory.LocationInfoLine;
import com.entelience.objects.directory.GroupInformation;
import com.entelience.objects.directory.PeopleInfoLine;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapDirectory;
import com.entelience.util.Excel;

/**
 * Export zones as an excel spreadsheat
 *
 */
public final class BusinessZonesExcel extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        Boolean showDisabled = getParamBoolean(request, "showDisabled");
        addHeader(response, "business_zones.xls");
        buildDocument(x, peopleId, showDisabled);
    }

    /**
     * buildDocument
     */
    public void buildDocument(Excel x, Integer peopleId, Boolean showDisabled) throws Exception {
        x.newWorkBook();
        BusinessZone[] bzs = getZones(peopleId, showDisabled);
        Map<String, LocationInfoLine[]> locMap = getIncludedLocations(peopleId, bzs);
        Map<String, BusinessZone[]> bzMap = getIncludedZones(peopleId, bzs);
        Map<String, GroupInformation[]> deptMap = getIncludedDepartments(peopleId, bzs);
        Map<String, PeopleInfoLine[]> userMap = getIncludedUsers(peopleId, bzs);
        _logger.info("Processing business zones[" + bzs.length + "]");
        x.newSheet("Continents");
        x.newRow();
        x.newTitleCell("Continent", 25);
        _logger.info("Processing bz continents");
        for (int i = 0; i < bzs.length; i++) {
            String bzName = bzs[i].getName();
            if (bzs[i].getContinents() != null && !bzs[i].getContinents().isEmpty()) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (Iterator<String> it = bzs[i].getContinents().iterator(); it.hasNext(); ) {
                    x.newRow();
                    x.newCell(it.next());
                }
            }
        }
        x.newSheet("Subcontinents");
        x.newRow();
        x.newTitleCell("Subcontinent", 25);
        _logger.info("Processing bz subcontinents");
        for (int i = 0; i < bzs.length; i++) {
            String bzName = bzs[i].getName();
            if (bzs[i].getSubcontinents() != null && !bzs[i].getSubcontinents().isEmpty()) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (Iterator<String> it = bzs[i].getSubcontinents().iterator(); it.hasNext(); ) {
                    x.newRow();
                    x.newCell(it.next());
                }
            }
        }
        x.newSheet("Locations");
        x.newRow();
        x.newTitleCell("Location", 25);
        _logger.info("Processing  bz locations");
        Iterator<Map.Entry<String, LocationInfoLine[]>> itL = locMap.entrySet().iterator();
        while (itL.hasNext()) {
            Map.Entry<String, LocationInfoLine[]> entry = itL.next();
            String bzName = entry.getKey();
            LocationInfoLine[] locs = entry.getValue();
            if (locs.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (int i = 0; i < locs.length; i++) {
                    x.newRow();
                    x.newCell(locs[i].getLocationName());
                }
            }
        }
        x.newSheet("Departments");
        x.newRow();
        x.newTitleCell("Department", 25);
        _logger.info("Processing bz depatments");
        Iterator<Map.Entry<String, GroupInformation[]>> itDept = deptMap.entrySet().iterator();
        while (itDept.hasNext()) {
            Map.Entry<String, GroupInformation[]> entry = itDept.next();
            String bzName = entry.getKey();
            GroupInformation[] depts = entry.getValue();
            if (depts.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (int i = 0; i < depts.length; i++) {
                    x.newRow();
                    x.newCell(depts[i].getName());
                }
            }
        }
        x.newSheet("Included zones");
        x.newRow();
        x.newTitleCell("Included Zone", 25);
        _logger.info("Processing bz included zones");
        Iterator<Map.Entry<String, BusinessZone[]>> itBz = bzMap.entrySet().iterator();
        while (itBz.hasNext()) {
            Map.Entry<String, BusinessZone[]> entry = itBz.next();
            String bzName = entry.getKey();
            BusinessZone[] incBzs = entry.getValue();
            if (incBzs.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (int i = 0; i < incBzs.length; i++) {
                    x.newRow();
                    x.newCell(incBzs[i].getName());
                }
            }
        }
        x.newSheet("Users");
        x.newRow();
        x.newTitleCell("User", 25);
        _logger.info("Processing bz users");
        Iterator<Map.Entry<String, PeopleInfoLine[]>> itUser = userMap.entrySet().iterator();
        while (itUser.hasNext()) {
            Map.Entry<String, PeopleInfoLine[]> entry = itUser.next();
            String bzName = entry.getKey();
            PeopleInfoLine[] users = entry.getValue();
            if (users.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(bzName, 25);
                for (int i = 0; i < users.length; i++) {
                    x.newRow();
                    x.newCell(users[i].getDisplay_name());
                }
            }
        }
        x.writeWorkBook();
    }

    /**
     * get the list of zones to show
     */
    private BusinessZone[] getZones(Integer peopleId, Boolean showDisabled) throws Exception {
        try {
            return new soapDirectory(peopleId).listBusinessZones(null, null, null, null, null, null, null, null, null, showDisabled);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    private Map<String, LocationInfoLine[]> getIncludedLocations(Integer peopleId, BusinessZone[] bzs) throws Exception {
        try {
            Map<String, LocationInfoLine[]> locs = new TreeMap<String, LocationInfoLine[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < bzs.length; i++) {
                locs.put(bzs[i].getName(), sr.listLocationsInBusinessZone(bzs[i].getBusinessZoneId().intValue()));
            }
            return locs;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    private Map<String, GroupInformation[]> getIncludedDepartments(Integer peopleId, BusinessZone[] bzs) throws Exception {
        try {
            Map<String, GroupInformation[]> depts = new TreeMap<String, GroupInformation[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < bzs.length; i++) {
                depts.put(bzs[i].getName(), sr.listGroupsInBusinessZone(bzs[i].getBusinessZoneId().intValue()));
            }
            return depts;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    private Map<String, PeopleInfoLine[]> getIncludedUsers(Integer peopleId, BusinessZone[] bzs) throws Exception {
        try {
            Map<String, PeopleInfoLine[]> users = new TreeMap<String, PeopleInfoLine[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < bzs.length; i++) {
                users.put(bzs[i].getName(), sr.listUsersInBusinessZone(bzs[i].getBusinessZoneId().intValue()));
            }
            return users;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    private Map<String, BusinessZone[]> getIncludedZones(Integer peopleId, BusinessZone[] bzs) throws Exception {
        try {
            Map<String, BusinessZone[]> zones = new TreeMap<String, BusinessZone[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < bzs.length; i++) {
                zones.put(bzs[i].getName(), sr.listBusinessZonesIncludedIn(bzs[i].getBusinessZoneId().intValue()));
            }
            return zones;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }
}
