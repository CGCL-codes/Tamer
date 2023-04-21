package com.apelon.dtswf.test.interactive;

import com.apelon.apelonserver.client.*;
import com.apelon.dts.client.common.DTSObject;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.attribute.DTSProperty;
import com.apelon.dts.client.attribute.DTSAttribute;
import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.association.Association;
import com.apelon.dts.client.association.Synonym;
import com.apelon.dts.client.association.TermAssociation;
import com.apelon.dts.test.interactive.MenuTest;
import com.apelon.dtswf.client.*;
import com.apelon.dtswf.client.VersionAttributeChangeItem;
import com.apelon.dtswf.data.version.*;
import com.apelon.dtswf.data.User;
import com.apelon.dtswf.data.Assignment;
import com.apelon.dtswf.data.AssignmentStatus;
import com.apelon.dtswf.data.impl.AssignmentImpl;
import com.apelon.dtswf.data.impl.AssignmentUpdateImpl;
import com.apelon.dtswf.data.impl.AssignmentStatusImpl;
import java.text.*;
import java.util.*;

public class VersionMenuTest extends MenuTest {

    private static final String[] mainItems = { "get item", "get versions by date range", "get versions By Namespace", "get versions by id range", "get versions by name", "ger versions by code", "get versions by user", "get versions by assignment name", "get versions by assignment id range", "get edit history", "get latest history", "get workflow users list", "get User", "add Assignment", "delete Assignment", "update Assignment", "get attribute change by version" };

    private static final String[] testMainItems = { "getItem", "getVersionsByDateRange", "getVersionsByNamespace", "getVersionsByIdRange", "getVersionsByName", "getVersionsByCode", "getVersionsByUser", "getVersionsByAssignmentName", "getVersionsByAssignmentIdRange", "getEditHistory", "getLatestHistory", "getUserList", "getUser", "addAssignment", "deleteAssignment", "updateAssignment", "getAttributeChangeByVersion" };

    private static int choice;

    private VersionQuery versionQuery;

    private AssignmentQuery assnQuery;

    public VersionMenuTest() {
        super();
    }

    public VersionMenuTest(ServerConnection conn) {
        super();
        versionQuery = VersionQuery.createInstance(conn);
        assnQuery = new AssignmentQuery(conn);
        assnQuery.setVersion("DWF:002");
        init();
    }

    public String[] getDisplayItems() {
        return mainItems;
    }

    public String[] getTestItems() {
        return testMainItems;
    }

    protected void init() {
    }

    public void getItem() throws Exception {
        String id = getInput("enter the version id");
        DTSObject obj = versionQuery.findItemByVersion(Long.parseLong(id));
        if (obj instanceof DTSConcept) {
            System.out.println("concept: " + obj);
        } else {
            System.out.println("term: " + obj);
        }
    }

    private DateEntry askDate() throws Exception {
        DateEntry entry = new DateEntry();
        DateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy");
        String startDateString = getInput("enter start date (mm/dd/yyyy))");
        entry.start = format.parse(startDateString);
        String endDateString = getInput("enter end date (mm/dd/yyyy))");
        entry.end = format.parse(endDateString);
        return entry;
    }

    private static void printVersions(Version[] vs) {
        for (int i = 0; i < vs.length; i++) {
            System.out.println("id: " + vs[i].getId() + " type: " + vs[i].getType() + " itemId: " + vs[i].getItemId() + " itemCode: " + vs[i].getItemCode() + " itemName: " + vs[i].getItemName() + " itemNamespace: " + vs[i].getItemNamespaceName());
            System.out.println("user: " + vs[i].getUser() + " date: " + vs[i].getDate() + " editType: " + vs[i].getEditType() + " attrType: " + vs[i].getAttributeType());
            System.out.println("assignmentId: " + vs[i].getAssignmentId() + " assignmentName: " + vs[i].getAssignmentName());
            System.out.println("");
        }
    }

    public void getVersionsByDateRange() throws Exception {
        VersionSearchConfig config = new VersionSearchConfig();
        config.setSearchType(VersionSearchConfig.DATE_RANGE_SEARCH);
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByNamespace() throws Exception {
        NamespaceSearchConfig config = new NamespaceSearchConfig();
        config.setSearchType(VersionSearchConfig.NAMESPACE_SEARCH);
        String namespace = getInput("enter the namespace");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setNamespaceName(namespace);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByAssignmentName() throws Exception {
        AssignmentSearchConfig config = new AssignmentSearchConfig();
        config.setSearchType(VersionSearchConfig.ASSIGNMENT_NAME_SEARCH);
        String assignment = getInput("enter the assignment name");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setAssignmentName(assignment);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByIdRange() throws Exception {
        IDRangeSearchConfig config = new IDRangeSearchConfig();
        String itemType = getInput("enter the type (c/C for concept or t/T for term");
        itemType = itemType.toUpperCase();
        if (itemType.equals(Version.CONCEPT_TYPE)) {
            config.setSearchType(VersionSearchConfig.CONCEPT_ID_RANGE_SEARCH);
        } else {
            config.setSearchType(VersionSearchConfig.TERM_ID_RANGE_SEARCH);
        }
        String startIdStr = getInput("enter start id");
        String endIdStr = getInput("enter end id");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setStartItemId(Integer.parseInt(startIdStr));
        config.setEndItemId(Integer.parseInt(endIdStr));
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByAssignmentIdRange() throws Exception {
        AssignmentIDRangeSearchConfig config = new AssignmentIDRangeSearchConfig();
        config.setSearchType(VersionSearchConfig.ASSIGNMENT_ID_SEARCH);
        String startIdStr = getInput("enter start id");
        String endIdStr = getInput("enter end id");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setStartAssignmentId(Integer.parseInt(startIdStr));
        config.setEndAssignmentId(Integer.parseInt(endIdStr));
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByName() throws Exception {
        NameSearchConfig config = new NameSearchConfig();
        String itemType = getInput("enter the type (c/C for concept or t/T for term");
        itemType = itemType.toUpperCase();
        if (itemType.equals(Version.CONCEPT_TYPE)) {
            config.setSearchType(VersionSearchConfig.CONCEPT_NAME_SEARCH);
        } else {
            config.setSearchType(VersionSearchConfig.TERM_NAME_SEARCH);
        }
        String name = getInput("enter name");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setItemName(name);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByCode() throws Exception {
        CodeSearchConfig config = new CodeSearchConfig();
        String itemType = getInput("enter the type (c/C for concept or t/T for term");
        itemType = itemType.toUpperCase();
        if (itemType.equals(Version.CONCEPT_TYPE)) {
            config.setSearchType(VersionSearchConfig.CONCEPT_CODE_SEARCH);
        } else {
            config.setSearchType(VersionSearchConfig.TERM_CODE_SEARCH);
        }
        String code = getInput("enter code");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setItemCode(code);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getVersionsByUser() throws Exception {
        UserSearchConfig config = new UserSearchConfig();
        config.setSearchType(VersionSearchConfig.USER_SEARCH);
        String user = getInput("enter username");
        DateEntry entry = askDate();
        config.setStartDate(entry.start);
        config.setEndDate(entry.end);
        config.setUser(user);
        Version[] vs = versionQuery.findVersions(config);
        printVersions(vs);
    }

    public void getLatestHistory() throws Exception {
        String itemType = getInput("enter the type (c/C for concept or t/T for term");
        itemType = itemType.toUpperCase();
        String itemIdStr = getInput("enter item id");
        String nspIdStr = getInput("enter item namespace id");
        String numVer = getInput("enter number of previous versions to get");
        System.out.println("Edit history:");
        Version[] vers = versionQuery.getEditHistory(itemType, Integer.parseInt(itemIdStr), Integer.parseInt(nspIdStr), Version.CURRENT_VERSION, Integer.parseInt(numVer));
        printVersions(vers);
    }

    public void getEditHistory() throws Exception {
        String itemType = getInput("enter the type (c/C for concept or t/T for term");
        itemType = itemType.toUpperCase();
        String itemIdStr = getInput("enter item id");
        String nspIdStr = getInput("enter item namespace id");
        String startVerStr = getInput("enter start version (or c for current)");
        if (startVerStr.equalsIgnoreCase("c")) {
            startVerStr = String.valueOf(Version.CURRENT_VERSION);
        }
        String numVer = getInput("enter number of previous versions to get (or a for all)");
        if (numVer.equalsIgnoreCase("a")) {
            numVer = String.valueOf(Version.ALL_VERSIONS);
        }
        System.out.println("Edit history:");
        Version[] vers = versionQuery.getEditHistory(itemType, Integer.parseInt(itemIdStr), Integer.parseInt(nspIdStr), Integer.parseInt(startVerStr), Integer.parseInt(numVer));
        if (vers != null && vers.length > 1) {
            Version last = vers[0];
            int secondLastVersionId = versionQuery.findPreviousVersionId(last);
            System.out.println("Previous Version Id = [" + secondLastVersionId + "] for version [" + last.getId() + "]");
        }
        printVersions(vers);
    }

    public void getUserList() throws Exception {
        System.out.println("workflow users list:");
        User[] users = assnQuery.getUserList();
        for (int i = 0; i < users.length; i++) {
            User u = users[i];
            System.out.println("\tUser [" + u.getUserName() + "]");
        }
    }

    public void getUser() throws Exception {
        String userName = getInput("enter user name");
        System.out.println("Workflow user [" + userName + "] info:");
        User u = assnQuery.getUser(userName);
        System.out.println("\tUser [" + u.getUserName() + "], Type[" + u.getUserType() + "], ID[" + u.getId() + "]");
    }

    public void addAssignment() throws Exception {
        String assnName = getInput("Enter new assignment name: ");
        Assignment a = addNewAssignment(assnName);
        System.out.println("Adding assignment [" + a.getName() + "]");
        Assignment newA = assnQuery.addAssignment(a);
        if (newA != null) {
            System.out.println("Successfully added assignment [" + newA.getName() + "]");
            System.out.println("Searching for  assignment [" + newA.getName() + "]");
            Assignment fetchA = assnQuery.getAssignment(newA.getId());
            System.out.println("Found assignment [" + fetchA.getName() + "]");
        }
    }

    private Assignment addNewAssignment(String assnName) throws Exception {
        AssignmentImpl a = new AssignmentImpl();
        a.setName(assnName);
        a.setCreatedDate(new Date());
        long timeSecs = System.currentTimeMillis();
        Date tomDate = new Date(timeSecs + 86400 * 1000);
        a.setDueDate(tomDate);
        a.setStatusDate(tomDate);
        a.setAssignedDate(tomDate);
        a.setStatus(AssignmentStatus.STATUS_UNASSIGNED);
        User[] users = assnQuery.getUserList();
        if (users != null && users.length > 0) {
            a.setAssignedBy(users[0].getUserName());
            a.setAssignedTo(users[0].getUserName());
            a.setCreatedBy(users[0].getUserName());
        }
        return a;
    }

    public void deleteAssignment() throws Exception {
        String assnName = getInput("Enter new assignment name: ");
        Assignment a = addNewAssignment(assnName);
        System.out.println("Adding assignment [" + a.getName() + "]");
        Assignment newA = assnQuery.addAssignment(a);
        if (newA != null) {
            System.out.println("Successfully added assignment [" + newA.getName() + "]");
            System.out.println("Searching for  assignment [" + newA.getName() + "]");
            Assignment fetchA = assnQuery.getAssignment(newA.getId());
            System.out.println("Found assignment [" + fetchA.getName() + "]");
            assnQuery.deleteAssignment(fetchA.getId(), fetchA.getAssignedBy());
            System.out.println("Searching for assignment [" + fetchA.getName() + "] in database");
            Assignment fetchAd = assnQuery.getAssignment(fetchA.getId());
            if (fetchAd == null) {
                System.out.println("No match found for  assignment [" + fetchA.getName() + "]");
                System.out.println("Successfully Deleted assignment [" + fetchA.getName() + "]");
            } else {
                System.out.println("Found assignment [" + fetchA.getName() + "]");
            }
        }
    }

    public void updateAssignment() throws Exception {
        String assnName = getInput("Enter new assignment name: ");
        Assignment a = addNewAssignment(assnName);
        System.out.println("Adding assignment [" + a.getName() + "]");
        Assignment newA = assnQuery.addAssignment(a);
        if (newA != null) {
            System.out.println("Successfully added assignment [" + newA.getName() + "]");
            System.out.println("Searching for  assignment [" + newA.getName() + "]");
            Assignment fetchA = assnQuery.getAssignment(newA.getId());
            System.out.println("Found assignment [" + fetchA.getName() + "]");
            System.out.println("Updating assignment [" + fetchA.getName() + "] status to " + AssignmentStatus.STATUS_APPROVED);
            AssignmentUpdateImpl au = new AssignmentUpdateImpl(fetchA.getId());
            au.setStatus(new AssignmentStatusImpl(AssignmentStatus.STATUS_APPROVED, fetchA.getCreatedBy(), fetchA.getCreatedDate()));
            assnQuery.updateAssignment(au);
            System.out.println("Searching for assignment [" + fetchA.getName() + "] in database");
            Assignment fetchAd = assnQuery.getAssignment(fetchA.getId());
            if (fetchAd != null && fetchAd.getStatus().equalsIgnoreCase(AssignmentStatus.STATUS_APPROVED)) {
                System.out.println("Successfully updated assignment [" + fetchA.getName() + "]");
            } else {
                System.out.println("Unsuccessful in updating assignment [" + fetchA.getName() + "]");
            }
        }
    }

    public void getAttributeChangeByVersion() throws Exception {
        String itemTypeStr = getInput("enter item type (c for concept, t for term)");
        String itemIdStr = getInput("enter item id");
        String nspIdStr = getInput("enter item namespace id");
        String verIdStr = getInput("enter version id (c for current)");
        int verId = Version.CURRENT_VERSION;
        if (!verIdStr.equalsIgnoreCase("c")) {
            verId = Integer.parseInt(verIdStr);
        }
        if (itemTypeStr.toUpperCase().startsWith("C")) {
            itemTypeStr = Version.CONCEPT_TYPE;
        } else {
            itemTypeStr = Version.TERM_TYPE;
        }
        Version[] vers = versionQuery.getEditHistory(itemTypeStr, Integer.parseInt(itemIdStr), Integer.parseInt(nspIdStr), verId, 1);
        if (vers != null && vers.length == 1) {
            VersionAttributeChangeItem changeItem = versionQuery.getAttributeChangeByVersion(vers[0]);
            System.out.println("\tAttribute Changes between [" + vers[0] + "] are:");
            System.out.println("\tItem Type=" + changeItem.getItemType());
            System.out.println("\tItem Edit Type=" + changeItem.getItemEditType());
            System.out.println("\tEdit Type=" + changeItem.getEditType());
            System.out.println("\tAttribute Type=" + changeItem.getAttributeType());
            System.out.println("\tOld Attribute=" + getObjectString(changeItem.getOldAttributeObject()));
            System.out.println("\tNew Attribute=" + getObjectString(changeItem.getNewAttributeObject()));
            System.out.println("\tNew Version=" + changeItem.getNewVersion());
        } else {
            System.out.println("No version found for item[" + itemIdStr + "]!");
        }
    }

    private String getObjectString(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof DTSProperty) {
            return LS + "\t\tProperty Name=" + ((DTSAttribute) o).getName() + LS + "\t\tValue=" + ((DTSAttribute) o).getValue();
        } else if (o instanceof ConceptAssociation) {
            return LS + "\t\tAssociation Name=" + ((Association) o).getName() + LS + "\t\tFrom Concept=" + ((Association) o).getFromItem() + LS + "\t\tTo Concept=" + ((Association) o).getValue();
        } else if (o instanceof Synonym) {
            return LS + "\t\tSynonym Name=" + ((Synonym) o).getName() + LS + "\t\tFrom Concept=" + ((Synonym) o).getFromItem() + LS + "\t\tTo Term=" + ((Synonym) o).getValue() + LS + "\t\tPreferred=" + ((Synonym) o).isPreferred();
        } else if (o instanceof TermAssociation) {
            return LS + "\t\tAssociation Name=" + ((Association) o).getName() + LS + "\t\tFrom Term=" + ((Association) o).getFromItem() + LS + "\t\tTo Term=" + ((Association) o).getValue();
        } else {
            return o.toString();
        }
    }

    public static final String LS = System.getProperty("line.separator");

    private static class DateEntry {

        private Date start;

        private Date end;
    }
}
