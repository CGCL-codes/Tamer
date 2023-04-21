package bioroot.plasmid;

import bioroot.*;
import java.sql.*;
import util.bio.digest.*;
import util.bio.seq.*;
import util.gen.*;
import java.util.*;

/**
 * @author nix
 *
 * Container for holding plasmid information
 */
public class PlasmidBean extends ReagentBean {

    private int geneId = 0;

    private String sequence = "";

    private String fileName = "";

    private String source = "";

    private String concentration = "";

    private String volume = "";

    private String purification = "";

    private String recipients = "";

    private String restrictionCutSites = "";

    private String geneName = "";

    private String[] markers;

    private String[] strainConcats;

    private OligoMatch[] oligoMatches;

    private boolean blasted = false;

    public PlasmidBean() {
    }

    public PlasmidBean(int id, DBUtil bioRoot) {
        String sql = "SELECT * FROM Plasmid WHERE id=" + id;
        ResultSet results = bioRoot.executeSQLAdvance(sql);
        loadPlasmidBean(results);
    }

    public PlasmidBean(ResultSet results) {
        loadPlasmidBean(results);
    }

    public int compareTo(Object obj) {
        PlasmidBean other = (PlasmidBean) obj;
        return getOrderBy().compareTo(other.getOrderBy());
    }

    public int getLength() {
        return sequence.length();
    }

    public String getRestrictionCutSites(Enzyme[] enzymes) {
        if (Misc.isEmpty(restrictionCutSites) && Misc.isNotEmpty(sequence)) {
            restrictionCutSites = REDigest.restrictionMapList(sequence, enzymes);
        }
        return restrictionCutSites;
    }

    public String getGeneName(DBUtil bioRoot) {
        if (Misc.isEmpty(geneName) && geneId != 0) {
            geneName = bioRoot.getGeneName(geneId);
        }
        return geneName;
    }

    public String[] getMarkers(DBUtil bioRoot) {
        if (markers == null && getId() != 0) {
            String sql = "SELECT Marker.name FROM PlasmidMarker, Marker WHERE " + "PlasmidMarker.plasmidId=" + getId() + " AND PlasmidMarker.markerId=Marker.id";
            markers = bioRoot.fetchMultipleCells(sql);
        }
        return markers;
    }

    public void setMarkers(String[] markers) {
        this.markers = markers;
    }

    public boolean saveMarkers(DBUtil bioRoot, boolean deleteOld) {
        boolean saved = true;
        String sql;
        if (deleteOld) {
            sql = "DELETE FROM PlasmidMarker WHERE plasmidId=" + getId();
            saved = bioRoot.executeSQLUpdate(sql);
        }
        if (markers != null && markers.length != 0 && Misc.isNotEmpty(markers[0]) && saved) {
            sql = "SELECT id FROM Marker WHERE text IN ('" + Misc.stringArrayToString(markers, "','") + "')";
            String[] ids = bioRoot.fetchMultipleCells(sql);
            int numIds = ids.length;
            sql = "INSERT INTO PlasmidMarker (plasmidId, markerId) Values (" + getId() + ",";
            for (int i = 0; i < numIds; i++) {
                saved = bioRoot.executeSQLUpdate(sql + ids[i] + ")");
            }
        }
        return saved;
    }

    /**Returns HTML for of oligoMatches for display.*/
    public String getHotLinkedOligos() {
        if (oligoMatches != null) {
            int num = oligoMatches.length;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < num; i++) {
                sb.append("<a href='Oligo?id=");
                sb.append(oligoMatches[i].getId());
                sb.append("'>");
                sb.append(oligoMatches[i].getName());
                sb.append("</a> (");
                sb.append(oligoMatches[i].getBpMatches());
                sb.append(") ");
            }
            return sb.toString();
        } else return "";
    }

    /**Returns plain text of oligoMatches for archive.*/
    public String getPlainTextOligos() {
        if (oligoMatches != null) {
            int num = oligoMatches.length;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < num; i++) {
                sb.append("ID: ");
                sb.append(oligoMatches[i].getId());
                sb.append(" ");
                sb.append(oligoMatches[i].getName());
                sb.append(" position(s) ");
                sb.append(oligoMatches[i].getBpMatches());
                sb.append(" ");
            }
            return sb.toString();
        } else return "";
    }

    public void setStrainConcats(String[] strainConcats) {
        this.strainConcats = HTML.filterFields(strainConcats, 250);
    }

    /**Returns an array of id: strainName, or empty String[]{""} if there are none.*/
    public String[] getStrainConcats(DBUtil bioRoot) {
        if (strainConcats == null) {
            if (getId() != 0) {
                String sql = "SELECT Strain.id, Strain.name FROM Strain, PlasmidStrain WHERE " + "Strain.id = PlasmidStrain.strainId AND PlasmidStrain.plasmidId = " + getId();
                strainConcats = bioRoot.fetchResultConcat(sql, 2, ": ", 100);
            } else strainConcats = new String[] { "" };
        }
        return strainConcats;
    }

    /**Can delete old strainIds from PlasmidStrain table, and add new entries if any are
	 * present in the strainConcats.*/
    public boolean saveStrainIds(DBUtil bioRoot, boolean deleteOld) {
        boolean saved = true;
        String sql;
        if (deleteOld) {
            sql = "DELETE FROM PlasmidStrain WHERE plasmidId=" + getId();
            saved = bioRoot.executeSQLUpdate(sql);
        }
        if (strainConcats != null && strainConcats.length != 0 && Misc.isNotEmpty(strainConcats[0]) && saved) {
            String[] ids = Misc.extractIDsFromConcats(strainConcats);
            int numIds = ids.length;
            sql = "INSERT INTO PlasmidStrain (plasmidId, strainId) VALUES (" + getId() + ",";
            for (int i = 0; i < numIds; i++) {
                saved = bioRoot.executeSQLUpdate(sql + ids[i] + ")");
            }
        }
        return saved;
    }

    /**Returns HTML strain names for display.*/
    public String getHotLinkedStrains(DBUtil bioRoot) {
        if (strainConcats == null) strainConcats = getStrainConcats(bioRoot);
        if (Misc.isNotEmpty(strainConcats[0])) {
            int num = strainConcats.length;
            StringBuffer sb = new StringBuffer();
            String[] idName;
            for (int i = 0; i < num; i++) {
                idName = Misc.splitOnFirstColon(strainConcats[i]);
                sb.append("<a href='Strain?strainId=");
                sb.append(idName[0]);
                sb.append("'>");
                sb.append(idName[1]);
                sb.append("</a> ");
            }
            return sb.toString();
        } else return "";
    }

    /**Returns plain text of strain ids for archive.*/
    public String getPlainTextStrain() {
        return Misc.stringArrayToString(strainConcats, ", ");
    }

    public void loadPlasmidBean(ResultSet results) {
        loadReagentBean(results);
        try {
            geneId = results.getInt("geneId");
            sequence = results.getString("sequence");
            fileName = results.getString("fileName");
            source = results.getString("source");
            recipients = results.getString("recipients");
            concentration = results.getString("concentration");
            volume = results.getString("volume");
            purification = results.getString("purification");
        } catch (SQLException e) {
            log.error("Problem fetching results to build plasmid bean", e);
            e.printStackTrace();
        }
    }

    public boolean submitNew(DBUtil bioRoot) {
        StringBuffer sb = new StringBuffer("INSERT INTO Plasmid (" + "labGroupId, organizationId, ownerId, editHistory, availability, visibility,location, text, alias, organismId," + "geneId, sequence, fileName, source, concentration, volume, purification, lastUser, recipients, comments, reference, notes) VALUES (");
        sb.append(getLabGroupId());
        sb.append(",");
        sb.append(getOrganization(bioRoot).getId());
        sb.append(",");
        sb.append(getOwnerId());
        sb.append(",'");
        sb.append(getEditHistory());
        sb.append("','");
        sb.append(getAvailability());
        sb.append("','");
        sb.append(getGenericVisibility());
        sb.append("','");
        sb.append(getLocation());
        sb.append("','");
        sb.append(getName());
        sb.append("','");
        sb.append(getAlias());
        sb.append("',");
        sb.append(getOrganismId());
        sb.append(",");
        sb.append(geneId);
        sb.append(",'");
        sb.append(sequence);
        sb.append("','");
        sb.append(fileName);
        sb.append("','");
        sb.append(source);
        sb.append("','");
        sb.append(concentration);
        sb.append("','");
        sb.append(volume);
        sb.append("','");
        sb.append(purification);
        sb.append("','");
        sb.append(getLastUser());
        sb.append("','");
        sb.append(recipients);
        sb.append("','");
        sb.append(getComments());
        sb.append("','");
        sb.append(getReference());
        sb.append("','");
        sb.append(getNotes());
        sb.append("')");
        return bioRoot.executeSQLUpdate(sb.toString());
    }

    public boolean updateOld(DBUtil bioRoot, UserBean submitter) {
        setEditHistory(Util.makeEditHistory(submitter.getFirstName(), submitter.getLastName(), getEditHistory()));
        StringBuffer sb = new StringBuffer("UPDATE Plasmid SET ");
        sb.append("labGroupId=");
        sb.append(getLabGroupId());
        sb.append(",ownerId=");
        sb.append(getOwnerId());
        sb.append(",editHistory='");
        sb.append(getEditHistory());
        sb.append("',availability='");
        sb.append(getAvailability());
        sb.append("',visibility='");
        sb.append(getGenericVisibility());
        sb.append("',location='");
        sb.append(getLocation());
        sb.append("',text='");
        sb.append(getName());
        sb.append("',alias='");
        sb.append(getAlias());
        sb.append("',organismId=");
        sb.append(getOrganismId());
        sb.append(",geneId=");
        sb.append(geneId);
        sb.append(",sequence='");
        sb.append(sequence);
        sb.append("',fileName='");
        sb.append(fileName);
        sb.append("',source='");
        sb.append(source);
        sb.append("',concentration='");
        sb.append(concentration);
        sb.append("',volume='");
        sb.append(volume);
        sb.append("',purification='");
        sb.append(purification);
        sb.append("',lastUser='");
        sb.append(getLastUser());
        sb.append("',recipients='");
        sb.append(recipients);
        sb.append("',comments='");
        sb.append(getComments());
        sb.append("',reference='");
        sb.append(getReference());
        sb.append("',notes='");
        sb.append(getNotes());
        sb.append("' WHERE id=");
        sb.append(getId());
        return bioRoot.executeSQLUpdate(sb.toString());
    }

    /**checks to see if plasmid text is unique within labgroup, assumes a connection has been made*/
    public boolean isPlasmidNameUnique(DBUtil bioRoot) {
        return isReagentNameUnique(bioRoot, "Plasmid");
    }

    public String getPurification() {
        return purification;
    }

    public String getSequence() {
        return sequence;
    }

    public String getVolume() {
        return volume;
    }

    public void setGeneName(String string) {
        geneName = HTML.filterField(string, 250);
    }

    public void setGeneId(int i) {
        geneId = i;
    }

    public void setPurification(String string) {
        purification = HTML.filterField(string, 250);
    }

    public void setSequence(String string) {
        String newSequence = Seq.filterDNASequence(string);
        if (newSequence.equals(sequence) == false) {
            restrictionCutSites = "";
            blasted = false;
            oligoMatches = null;
            sequence = newSequence;
        }
    }

    public void setVolume(String d) {
        volume = d;
    }

    /**For bulk file upload should be 16 cells	
	 0	1	   2	    3	   4	       5	       6	         7	        8	     9	     10	           11	     12	     13	    14	15							16				
	 Name Alias Source Recipients Reference Gene Organism Markers StrainIds Location Concentration Volume Purification Availability VisibilityBean Notes Sequence    
	 */
    public PlasmidBean(String[] cells, IdNameMatch[] genes, IdNameMatch[] organisms, IdNameMatch[] markerIDMatch, UserBean userBean, DBUtil bioRoot) {
        if (Misc.isEmpty(cells[0])) {
            setMessages("<br>A text appears to be missing. Parsing aborted!");
            return;
        }
        setName(HTML.filterField(cells[0], 200));
        StringBuffer sb = new StringBuffer();
        if (bioRoot.isNameUnique("Plasmid", getName(), userBean.getLabGroupId()) == false) {
            String rand = Passwords.createRandomWords(Passwords.numberLetters, 5, 1)[0];
            setName(getName() + " (RW:" + rand + ")");
            sb.append("<br>The Plasmid Name was not unique.  It has been modified with a random word.");
        }
        setAlias(HTML.filterField(Misc.trimQuotes(cells[1]), 250));
        source = HTML.filterField(Misc.trimQuotes(cells[2]), 250);
        setReference(HTML.filterField(Misc.trimQuotes(cells[3]), 250));
        recipients = HTML.filterField(Misc.trimQuotes(cells[4]), 250);
        if (Misc.isNotEmpty(cells[5])) {
            String gene = cells[5].toLowerCase();
            IdNameMatch x = IdNameMatch.findMatch(genes, gene);
            if (x != null) {
                geneName = x.getName();
                geneId = x.getId();
            } else sb.append("<br>An unknown gene was indicated. ('" + cells[5] + "'?) Add it?\n ");
        }
        if (Misc.isNotEmpty(cells[6])) {
            String org = cells[6].toLowerCase();
            IdNameMatch x = IdNameMatch.findMatch(organisms, org);
            if (x != null) {
                setOrganismName(x.getName());
                setOrganismId(x.getId());
            } else sb.append("<br>An unknown Organism was indicated. ('" + cells[6] + "'?) Consider adding it before uploading.\n ");
        }
        if (Misc.isNotEmpty(cells[7])) {
            String[] mars = Misc.trimQuotes(cells[7]).split("\\s*,\\s*");
            ArrayList al = new ArrayList();
            IdNameMatch x;
            for (int i = 0; i < mars.length; i++) {
                x = IdNameMatch.findMatch(markerIDMatch, mars[i].toLowerCase().trim());
                if (x != null) {
                    al.add(x.getName());
                } else sb.append("<br>An unknown marker/ feature was indicated. ('" + mars[i] + "'?) Consider adding it before uploading.\n ");
            }
            if (al.size() != 0) markers = Misc.stringArrayListToStringArray(al);
        }
        if (Misc.isNotEmpty(cells[8])) {
            String ids = cells[8].replaceAll("[^\\d,]", "");
            if (Misc.isNotEmpty(ids)) {
                String[] sepIds = ids.split(",");
                String[] viewIds = bioRoot.canView(userBean, "Strain", sepIds);
                if (viewIds.length == 0) sb.append("<br>The strains(s) you wish to associate with this plasmid don't exist or cannot be viewed! Check your strain ID numbers: '" + ids + "'\n "); else {
                    if (sepIds.length != viewIds.length) sb.append("<br>Some of the strain(s) you wish to associate with this plasmid don't exist or cannot be viewed! Check your strain ID numbers: '" + ids + "'\n ");
                    String sql = "SELECT id, text FROM Strain WHERE id IN (" + Misc.stringArrayToString(viewIds, ",") + ")";
                    strainConcats = bioRoot.fetchResultConcat(sql, 2, ": ", 200);
                }
            } else sb.append("<br>No ID numbers were found associated with this strain ID request ('" + cells[8] + "')?!\n ");
        }
        setLocation(HTML.filterField(Misc.trimQuotes(cells[9]), 250));
        concentration = HTML.filterField(cells[10], 250);
        volume = HTML.filterField(cells[11], 250);
        if (Misc.isNotEmpty(cells[12])) {
            String pur = cells[12].toLowerCase();
            if (pur.indexOf("crude") != -1) purification = "Enzyme digest grade- crude"; else if (pur.indexOf("clean") != -1) purification = "Sequencing grade- clean"; else if (pur.indexOf("pure") != -1) purification = "Injection grade- ultra pure"; else sb.append("<br>An unknown purification method was indicated. ('" + cells[12] + "'?)  Choose from Enzyme digest grade- crude, Sequencing grade- clean, Injection grade- " + "ultra pure, or leave it blank.");
        }
        setAvailability("No Restrictions");
        if (Misc.isNotEmpty(cells[13])) {
            String avi = cells[13].toLowerCase();
            if (avi.indexOf("restricted") != -1) setAvailability("Restricted"); else if (avi.indexOf("conditional") != -1) setAvailability("Conditional"); else if (avi.indexOf("no restrictions") != -1) ; else sb.append("<br>An unknown Availibility catagory was indicated. ('" + cells[13] + "'?) Choose from No Restrictions, Restricted, Conditional, or leave it blank. " + "Entry set to No Restrictions.");
        }
        setVisibility("Lab Mates");
        if (Misc.isNotEmpty(cells[14])) {
            String vis = cells[14].toLowerCase();
            if (vis.indexOf("private") != -1) setVisibility("Private"); else if (vis.indexOf("lab mates") != -1) setVisibility("Lab Mates"); else if (vis.indexOf("organization") != -1) {
                if (userBean.getOrganization().getId() != 0) setVisibility("Organization"); else sb.append("<br>You have indicated an organization visibility setting yet you don't belong to an organization?! " + "Setting visibility to 'Lab Mates'. ");
            } else if (vis.indexOf("www") != -1) setVisibility("WWW"); else sb.append("<br>An unknown VisibilityBean catagory was indicated. ('" + cells[14] + "'?) Choose from WWW, Organization, Labmates, or Private. Entry set to WWW. ");
        }
        setNotes(HTML.filterField(Misc.trimQuotes(cells[15]), 10000));
        sequence = Seq.filterDNASequence(cells[16]);
        if (sequence.length() != cells[16].length()) sb.append("<br>Non-IUP characters were removed from the plasmid Sequence.");
        setMessages(sb.toString());
        setOwnerId(userBean.getId());
        setLabGroupId(userBean.getLabGroupId());
        setEditHistory(Util.makeEditHistory(userBean.getFirstName(), userBean.getLastName(), null));
        setComplete(true);
    }

    public int getGeneId() {
        return geneId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String ngUl) {
        this.concentration = ngUl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setOligoMatches(OligoMatch[] oligoMatches) {
        this.oligoMatches = oligoMatches;
    }

    public OligoMatch[] getOligoMatches() {
        return oligoMatches;
    }

    public boolean isBlasted() {
        return blasted;
    }

    public void setBlasted(boolean blasted) {
        this.blasted = blasted;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String string) {
        recipients = HTML.filterField(string, 250);
    }
}
