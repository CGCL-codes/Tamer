package bioroot;

import java.sql.*;
import org.apache.log4j.Logger;
import util.gen.*;

/**
 * @author nix
 *
 * Container for holding comon information about a reagent (oligo, plasmid, strain, antibody...)
 */
public class ReagentBean implements Comparable {

    public static Logger log = Logger.getLogger(ReagentBean.class);

    private int id = 0;

    private int labGroupId = 0;

    private Organization organization = null;

    private int ownerId = 0;

    private int organismId = 0;

    private String editHistory = "";

    private String availability = "Conditional";

    private String visibility = "Lab Mates";

    private String location = "";

    private String name = "";

    private String alias = "";

    private String notes = "";

    private String comments = "";

    private String messages = "";

    boolean complete = false;

    private String ownerName = "";

    private String organismName = "";

    private String labGroupName = "";

    private String orderBy = "";

    private String lastUser = "";

    private String reference = "";

    public int compareTo(Object obj) {
        ReagentBean other = (ReagentBean) obj;
        return this.orderBy.compareTo(other.orderBy);
    }

    public void loadReagentBean(ResultSet results) {
        try {
            id = results.getInt("id");
            labGroupId = results.getInt("labGroupId");
            name = results.getString("text");
            alias = results.getString("alias");
            organismId = results.getInt("organismId");
            ownerId = results.getInt("ownerId");
            editHistory = results.getString("editHistory");
            location = results.getString("location");
            availability = results.getString("availability");
            visibility = results.getString("visibility");
            notes = results.getString("notes");
            comments = results.getString("comments");
            lastUser = results.getString("lastUser");
            reference = results.getString("reference");
        } catch (SQLException e) {
            log.error("Problem fetching results to build reagent bean", e);
        }
    }

    /**Just updates fields that are accessible by all lab group memebers, lastUser, comments given a 
	 * reagent type: Oligo or Plasmid. Strain overrides this method.*/
    public boolean updateCommonAccessFields(DBUtil bioRoot, String reagentType) {
        StringBuffer sb = new StringBuffer("UPDATE SET ");
        sb.append("lastUser='");
        sb.append(lastUser);
        sb.append("',comments='");
        sb.append(comments);
        sb.append("' WHERE id=");
        sb.append(id);
        return bioRoot.executeSQLUpdate(sb.toString());
    }

    /**checks to see if oligo text is unique within labgroup, assumes a connection has been made*/
    public boolean isReagentNameUnique(DBUtil bioRoot, String tableName) {
        String sql = "SELECT text FROM " + tableName + " where text = '" + name + "' AND labGroupId=" + labGroupId;
        bioRoot.executeSQL(sql);
        if (Misc.isNotEmpty(bioRoot.getResultString())) {
            return false;
        }
        return true;
    }

    public boolean canView(UserBean user, DBUtil bioRoot) {
        if (visibility.equals("WWW")) return true;
        if (visibility.equals("Lab mates") && user.getLabGroupId() == labGroupId) return true;
        if (visibility.equals("Private")) {
            if (user.getId() == ownerId) return true;
            if (user.isSuperUser() && user.getLabGroupId() == labGroupId) return true;
        }
        if (organization == null) organization = new Organization(bioRoot, labGroupId);
        if (organization.getId() == user.getOrganization().getId() && organization.getId() != 0) return true;
        return false;
    }

    public boolean canModifyAll(UserBean user) {
        if (user.getId() == ownerId) return true;
        if (user.isSuperUser() && user.getLabGroupId() == labGroupId) return true;
        return false;
    }

    public boolean canModifyCommon(UserBean user) {
        if (canModifyAll(user)) return true;
        if (user.getLabGroupId() == labGroupId) return true;
        return false;
    }

    public String getVisibility(DBUtil bioRoot) {
        if (visibility.equals("Organization")) {
            if (organization == null) organization = new Organization(bioRoot, labGroupId);
            return organization.getName();
        }
        return visibility;
    }

    /**Returns one of the standard visibility settings (WWW, Lab mates, Private) or the generic 'Organization'*/
    public String getGenericVisibility() {
        if (visibility.equals("WWW") || visibility.equals("Lab Mates") || visibility.equals("Private")) return visibility;
        return "Organization";
    }

    public String getLabGroupName(DBUtil bioRoot) {
        if (Misc.isEmpty(labGroupName) && labGroupId != 0) {
            labGroupName = bioRoot.fetchLabGroupName(labGroupId);
        }
        return labGroupName;
    }

    public String getOwnerName(DBUtil bioRoot) {
        if (Misc.isEmpty(ownerName) && ownerId != 0) {
            ownerName = bioRoot.fetchUserName(ownerId, false);
        }
        return ownerName;
    }

    public String getOrganismName(DBUtil bioRoot) {
        if (Misc.isEmpty(organismName) && organismId != 0) {
            organismName = bioRoot.getOrganismName(organismId);
        }
        return organismName;
    }

    public int getOrganismId() {
        return organismId;
    }

    public String getAlias() {
        return alias;
    }

    public String getAvailability() {
        return availability;
    }

    public String getEditHistory() {
        return editHistory;
    }

    public int getId() {
        return id;
    }

    public int getLabGroupId() {
        return labGroupId;
    }

    public String getLocation() {
        return location;
    }

    public String getMessages() {
        if (Misc.isNotEmpty(messages)) return messages + "<p>";
        return "";
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setAlias(String string) {
        alias = HTML.filterField(string, 250);
    }

    public void setAvailability(String string) {
        availability = HTML.filterField(string, 250);
    }

    public void setLocation(String string) {
        location = HTML.filterField(string, 250);
    }

    public void setMessages(String string) {
        messages = string;
    }

    public void appendMessages(String string) {
        if (Misc.isEmpty(messages)) messages = string; else messages = messages + "<br>" + string;
    }

    public void setName(String string) {
        name = HTML.filterField(string, 250);
    }

    public void setNotes(String string) {
        notes = HTML.filterField(string, 10000);
    }

    public void setOrganismId(int i) {
        organismId = i;
    }

    public void setOwnerId(int i) {
        ownerId = i;
    }

    public void setVisibility(String string) {
        visibility = HTML.filterField(string, 250);
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setEditHistory(String editHistory) {
        this.editHistory = editHistory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabGroupId(int labGroupId) {
        this.labGroupId = labGroupId;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setLabGroupName(String labGroupName) {
        this.labGroupName = labGroupName;
    }

    public void setOrganismName(String organismName) {
        this.organismName = HTML.filterField(organismName, 250);
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = HTML.filterField(comments, 10000);
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = HTML.filterField(lastUser, 250);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String string) {
        this.reference = HTML.filterField(string, 250);
    }

    public Organization getOrganization(DBUtil bioRoot) {
        if (organization == null) organization = new Organization(bioRoot, labGroupId);
        return organization;
    }
}
