package com.entelience.servlet.admin;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.objects.directory.Expertise;
import com.entelience.objects.directory.GroupInformation;
import com.entelience.objects.directory.PeopleInfoLine;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapDirectory;
import com.entelience.util.Excel;

/**
 * Export users + XPs as an excel spreadsheat
 *
 */
public final class UsersExcel extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        String filter = getParam(request, "filter");
        addHeader(response, "users.xls");
        buildDocument(x, peopleId, filter);
    }

    /**
     * buildDocument
     */
    public void buildDocument(Excel x, Integer peopleId, String filter) throws Exception {
        x.newWorkBook();
        x.newSheet("Users");
        PeopleInfoLine[] users = getUsers(peopleId, filter);
        Map<String, Expertise[]> xpMap = getExpertises(peopleId, users);
        Map<String, GroupInformation[]> membershipMap = getMemberships(peopleId, users);
        _logger.info("Processing users[" + users.length + "]");
        x.newRow();
        x.newTitleCell("User name", 15);
        x.newTitleCell("First name", 10);
        x.newTitleCell("Last name", 10);
        x.newTitleCell("Email", 20);
        x.newTitleCell("Phone", 15);
        x.newTitleCell("Company", 15);
        x.newTitleCell("Location", 15);
        x.newTitleCell("Timezone", 15);
        x.newTitleCell("Administrator", 10);
        x.newTitleCell("Disabled", 10);
        x.newTitleCell("Creation Date", 15);
        for (int i = 0; i < users.length; i++) {
            x.newRow();
            x.newCell(users[i].getName());
            x.newCell(users[i].getFirst_name());
            x.newCell(users[i].getLast_name());
            x.newCell(users[i].getEmail());
            x.newCell(users[i].getPhone());
            x.newCell(users[i].getCompany());
            x.newCell(users[i].getLocation());
            x.newCell(users[i].getTimezone());
            x.newCell(users[i].isIs_admin() ? "Yes" : "No");
            x.newCell(users[i].isDisabled() ? "Yes" : "No");
            x.newDateCell(users[i].getCreation_date());
        }
        x.newSheet("Users memberships");
        x.newRow();
        x.newTitleCell("Group", 25);
        _logger.info("Processing memberships");
        Iterator<Map.Entry<String, GroupInformation[]>> itMb = membershipMap.entrySet().iterator();
        while (itMb.hasNext()) {
            Map.Entry<String, GroupInformation[]> entry = itMb.next();
            String userName = entry.getKey();
            GroupInformation[] gi = entry.getValue();
            if (gi.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(userName, 25);
                for (int i = 0; i < gi.length; i++) {
                    x.newRow();
                    x.newCell(gi[i].getName());
                }
            }
        }
        x.newSheet("Users expertises");
        x.newRow();
        x.newTitleCell("Vendor", 25);
        x.newTitleCell("Product", 25);
        _logger.info("Processing expertises");
        Iterator<Map.Entry<String, Expertise[]>> itXp = xpMap.entrySet().iterator();
        while (itXp.hasNext()) {
            Map.Entry<String, Expertise[]> entry = itXp.next();
            _logger.info("type : " + entry.getKey().getClass());
            String username = entry.getKey();
            Expertise[] xps = entry.getValue();
            if (xps.length > 0) {
                x.newRow();
                x.newRow();
                x.newTitleCell(username, 25);
                for (int i = 0; i < xps.length; i++) {
                    x.newRow();
                    x.newCell(xps[i].getVendor_name());
                    x.newCell(xps[i].getProduct_name());
                }
            }
        }
        x.writeWorkBook();
    }

    /**
     * get the list of groups to show
     */
    private PeopleInfoLine[] getUsers(Integer peopleId, String filter) throws Exception {
        try {
            soapDirectory sr = new soapDirectory(peopleId);
            PeopleInfoLine[] users;
            if ("disabled".equals(filter)) {
                users = sr.listDisabledUsers();
            } else {
                users = sr.getListPeople(Boolean.FALSE);
            }
            return users;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservice method : " + e.getMessage(), e);
        }
    }

    private Map<String, GroupInformation[]> getMemberships(Integer peopleId, PeopleInfoLine[] users) throws Exception {
        try {
            Map<String, GroupInformation[]> memberships = new TreeMap<String, GroupInformation[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < users.length; i++) {
                memberships.put(users[i].getName(), sr.getGroupsForUser(users[i].getE_people_id().intValue()));
            }
            return memberships;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    /**
     * get the list of expertise mapped per group 
     */
    private Map<String, Expertise[]> getExpertises(Integer peopleId, PeopleInfoLine[] users) throws Exception {
        try {
            Map<String, Expertise[]> xps = new TreeMap<String, Expertise[]>();
            soapDirectory sr = new soapDirectory(peopleId);
            for (int i = 0; i < users.length; i++) {
                xps.put(users[i].getName(), sr.getUserExpertises(users[i].getE_people_id().intValue(), false));
            }
            return xps;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservice method : " + e.getMessage(), e);
        }
    }
}
