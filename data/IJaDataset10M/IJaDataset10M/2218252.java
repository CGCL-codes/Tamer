package bioroot.antibody;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import bioroot.DBUtil;
import bioroot.Util;
import util.gen.HTML;
import util.gen.Misc;
import org.apache.log4j.Logger;

/**
 * @author nix
 * This contains information related to a particular antibody bleed, hybridoma sup collection, or purification
 */
public class AntibodyPrep {

    private static final Logger log = Logger.getLogger(AntibodyPrep.class);

    private int id = 0;

    private int antibodyId = 0;

    private String name = "";

    private String date = "";

    private String recipients = "";

    private String organismName = "";

    private int organismId;

    private UseDilution[] useDilutions;

    private boolean lookedForUseDilutions = false;

    private String purification = "";

    private String notes = "";

    private String remainingAliquots = "";

    private String lastUser = "";

    private String userComments = "";

    private String location = "";

    private String fileName = "";

    private String oldFileName = "";

    public AntibodyPrep(int antibodyId, ResultSet results) {
        this.antibodyId = antibodyId;
        try {
            id = results.getInt("id");
            name = results.getString("text");
            date = results.getString("date");
            recipients = results.getString("recipients");
            organismId = results.getInt("organismId");
            purification = results.getString("purification");
            notes = results.getString("notes");
            remainingAliquots = results.getString("remainingAliquots");
            lastUser = results.getString("lastUser");
            userComments = results.getString("userComments");
            location = results.getString("location");
            fileName = results.getString("fileName");
        } catch (SQLException e) {
            log.error("Problem fetching results to build antibody bean", e);
        }
    }

    public AntibodyPrep() {
        useDilutions = new UseDilution[1];
        useDilutions[0] = new UseDilution();
    }

    /**Makes a new AntibodyPrep table entry.*/
    public boolean submitNew(DBUtil bioRoot) {
        if (Misc.isNotEmpty(organismName)) organismId = bioRoot.getOrganismId(organismName);
        if (Misc.isNotEmpty(fileName)) {
            File tmp = new File(Util.filepath + "Upload/AntibodyFiles/" + fileName);
            File real = new File((Util.filepath + "Upload/AntibodyFiles/" + fileName.substring(4)));
            tmp.renameTo(real);
            fileName = real.getName();
        }
        StringBuffer sb = new StringBuffer("INSERT INTO AntibodyPrep (" + "antibodyId, text, date, recipients, organismId, purification, notes, remainingAliquots," + "lastUser, userComments, location, fileName ) VALUES (");
        sb.append(antibodyId);
        sb.append(",'");
        sb.append(name);
        sb.append("','");
        sb.append(date);
        sb.append("','");
        sb.append(recipients);
        sb.append("',");
        sb.append(organismId);
        sb.append(",'");
        sb.append(purification);
        sb.append("','");
        sb.append(notes);
        sb.append("','");
        sb.append(remainingAliquots);
        sb.append("','");
        sb.append(lastUser);
        sb.append("','");
        sb.append(userComments);
        sb.append("','");
        sb.append(location);
        sb.append("','");
        sb.append(fileName);
        sb.append("')");
        boolean submitted = bioRoot.executeSQLUpdate(sb.toString());
        if (submitted) {
            id = bioRoot.getLastInsertId("AntibodyPrep");
            if (id == 0) return false;
        }
        if (submitted && useDilutions != null) submitted = saveNewUseDilutions(bioRoot);
        return submitted;
    }

    /**Updates a Antibody prep entry*/
    public boolean updateOld(DBUtil bioRoot) {
        if (Misc.isNotEmpty(fileName) && fileName.startsWith("temp")) {
            File tmp = new File(Util.filepath + "Upload/AntibodyFiles/" + fileName);
            File real = new File((Util.filepath + "Upload/AntibodyFiles/" + fileName.substring(4)));
            tmp.renameTo(real);
            fileName = real.getName();
        }
        if (Misc.isNotEmpty(getOrganismName(bioRoot))) {
            setOrganismId(bioRoot.getOrganismId(getOrganismName(bioRoot)));
        } else setOrganismId(0);
        StringBuffer sb = new StringBuffer("UPDATE AntibodyPrep SET ");
        sb.append("text='");
        sb.append(name);
        sb.append("',date='");
        sb.append(date);
        sb.append("',recipients='");
        sb.append(recipients);
        sb.append("',organismId=");
        sb.append(organismId);
        sb.append(",notes='");
        sb.append(notes);
        sb.append("',location='");
        sb.append(location);
        sb.append("',fileName='");
        sb.append(fileName);
        sb.append("',notes='");
        sb.append(notes);
        sb.append("',remainingAliquots='");
        sb.append(remainingAliquots);
        sb.append("',lastUser='");
        sb.append(lastUser);
        sb.append("',userComments='");
        sb.append(userComments);
        sb.append("' WHERE id=");
        sb.append(id);
        return bioRoot.executeSQLUpdate(sb.toString());
    }

    public boolean saveNewUseDilutions(DBUtil bioRoot) {
        boolean saved = true;
        for (int i = 0; i < useDilutions.length; i++) {
            if (saved) saved = useDilutions[i].submitNew(bioRoot, id);
        }
        return saved;
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

    public void setOrganismId(int i) {
        organismId = i;
    }

    public void setOrganismName(String organismName) {
        this.organismName = HTML.filterField(organismName, 250);
    }

    public UseDilution[] getUseDilutions(DBUtil bioRoot) {
        if (lookedForUseDilutions == false && id != 0) {
            try {
                String sql = "SELECT * FROM UseDilution WHERE antibodyPrepId =" + id;
                ResultSet results = bioRoot.executeSQL(sql);
                ArrayList al = new ArrayList();
                while (results.next()) {
                    al.add(new UseDilution(results, id));
                }
                if (al.size() != 0) {
                    useDilutions = new UseDilution[al.size()];
                    al.toArray(useDilutions);
                }
                lookedForUseDilutions = true;
            } catch (Exception e) {
                log.error("Problem fetching UseDilutitons...", e);
            }
        }
        return useDilutions;
    }

    /**Just updates fields that are accessible by all lab group memebers.*/
    public boolean updateCommonAccessFields(DBUtil bioRoot) {
        StringBuffer sb = new StringBuffer("UPDATE AntibodyPrep SET ");
        sb.append("remainingAliquots='");
        sb.append(remainingAliquots);
        sb.append("', lastUser='");
        sb.append(lastUser);
        sb.append("', userComments='");
        sb.append(userComments);
        sb.append("' WHERE id=");
        sb.append(id);
        return bioRoot.executeSQLUpdate(sb.toString());
    }

    public int getAntibodyId() {
        return antibodyId;
    }

    public void setAntibodyId(int antibodyId) {
        this.antibodyId = antibodyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = HTML.filterField(date, 250);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = HTML.filterField(lastUser, 250);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = HTML.filterField(notes, 250);
    }

    public String getPurification() {
        return purification;
    }

    public void setPurification(String purification) {
        this.purification = HTML.filterField(purification, 250);
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = HTML.filterField(recipients, 250);
    }

    public String getRemainingAliquots() {
        return remainingAliquots;
    }

    public void setRemainingAliquots(String remainingAliquots) {
        this.remainingAliquots = HTML.filterField(remainingAliquots, 250);
    }

    public void setUseDilutions(UseDilution[] useDilutions) {
        this.useDilutions = useDilutions;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = HTML.filterField(userComments, 250);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = HTML.filterField(location, 250);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = HTML.filterField(name, 250);
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }
}
