package org.epoline.phoenix.memberdetails.shared;

import java.util.ArrayList;
import java.util.List;
import org.epoline.phoenix.common.shared.DateRange;
import org.epoline.phoenix.common.shared.Item;
import org.epoline.phoenix.common.shared.PhoenixUserException;
import org.epoline.phoenix.common.shared.Util;

/**
 * Data class for DMSMember. Used by Management Tool and Team Manager
 */
public class ItemMember extends Item {

    private String task = "";

    private final String teamUnit;

    private ItemMemberUser user;

    private ItemMemberUser deputy;

    private DateRange partTimeDateRange;

    private boolean partTimeCheck = false;

    private DateRange absenceDateRange;

    private boolean absenceCheck = false;

    private boolean expertiseEN = false;

    private boolean expertiseFR = false;

    private boolean expertiseDE = false;

    private boolean expertiseBestDG1 = false;

    private boolean expertiseBestDG2 = false;

    private boolean defaultTeam = false;

    private int partTimeValueMonday = 0;

    private int partTimeValueTuesday = 0;

    private int partTimeValueWednesday = 0;

    private int partTimeValueThursday = 0;

    private int partTimeValueFriday = 0;

    private int partTimeValueSaturday = 0;

    private ItemMemberProfile profile;

    private final List ranges = new ArrayList();

    private final List documentSets = new ArrayList();

    private boolean deletePmSettings;

    public ItemMember(String teamUnit) {
        this(teamUnit, "");
    }

    public ItemMember(String teamUnit, String task) {
        super();
        if (teamUnit == null || task == null) {
            throw new NullPointerException();
        }
        if (teamUnit.length() == 0 || teamUnit.length() != teamUnit.trim().length()) {
            throw new IllegalArgumentException("teamunit: " + teamUnit);
        }
        this.teamUnit = teamUnit;
        this.task = task;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ItemMember)) {
            return false;
        }
        ItemMember member = (ItemMember) obj;
        if (!isValid() || !member.isValid()) {
            return false;
        }
        return isAbsenceCheck() == member.isAbsenceCheck() && (!isAbsenceCheck() || (getAbsenceDateRange().equals(member.getAbsenceDateRange()))) && ((getDeputy() == null && member.getDeputy() == null) || (getDeputy() != null && member.getDeputy() != null && getDeputy().equals(member.getDeputy()))) && getUser().equals(member.getUser()) && getDocumentSets().size() == member.getDocumentSets().size() && getDocumentSets().containsAll(member.getDocumentSets()) && getRanges().size() == member.getRanges().size() && getRanges().containsAll(member.getRanges()) && getTask().equals(member.getTask()) && getTeamUnit().equals(member.getTeamUnit()) && getProfile().equals(member.getProfile()) && isDefaultTeam() == member.isDefaultTeam() && isExpertiseBestDG1() == member.isExpertiseBestDG1() && isExpertiseBestDG2() == member.isExpertiseBestDG2() && isExpertiseEN() == member.isExpertiseEN() && isExpertiseFR() == member.isExpertiseFR() && isExpertiseDE() == member.isExpertiseDE() && isPartTimeCheck() == member.isPartTimeCheck() && (!isPartTimeCheck() || (getPartTimeValueMonday() == member.getPartTimeValueMonday() && getPartTimeValueTuesday() == member.getPartTimeValueTuesday() && getPartTimeValueWednesday() == member.getPartTimeValueWednesday() && getPartTimeValueThursday() == member.getPartTimeValueThursday() && getPartTimeValueFriday() == member.getPartTimeValueFriday() && getPartTimeValueSaturday() == member.getPartTimeValueSaturday() && getPartTimeDateRange().equals(member.getPartTimeDateRange())));
    }

    public DateRange getAbsenceDateRange() {
        if (absenceDateRange == null) setAbsenceDateRange(new DateRange());
        return absenceDateRange;
    }

    public ItemMemberUser getDeputy() {
        return deputy;
    }

    public List getDocumentSets() {
        return documentSets;
    }

    public DateRange getPartTimeDateRange() {
        if (partTimeDateRange == null) setPartTimeDateRange(new DateRange());
        return partTimeDateRange;
    }

    public int getPartTimeValueFriday() {
        return partTimeValueFriday;
    }

    public int getPartTimeValueMonday() {
        return partTimeValueMonday;
    }

    public int getPartTimeValueSaturday() {
        return partTimeValueSaturday;
    }

    public int getPartTimeValueThursday() {
        return partTimeValueThursday;
    }

    public int getPartTimeValueTuesday() {
        return partTimeValueTuesday;
    }

    public int getPartTimeValueWednesday() {
        return partTimeValueWednesday;
    }

    public ItemMemberProfile getProfile() {
        return profile;
    }

    public List getRanges() {
        return ranges;
    }

    public java.lang.String getTask() {
        return task;
    }

    public java.lang.String getTeamUnit() {
        return teamUnit;
    }

    public ItemMemberUser getUser() {
        return user;
    }

    private static int getValidPartTimeValue(int value) {
        if (value < 0) return 0; else if (value > 100) return 100; else return value;
    }

    public int hashCode() {
        return getUser().hashCode();
    }

    public boolean isAbsenceCheck() {
        return absenceCheck;
    }

    public boolean isDefaultTeam() {
        return defaultTeam;
    }

    public boolean isDeletePmSettings() {
        return deletePmSettings;
    }

    public boolean isExpertiseBestDG1() {
        return expertiseBestDG1;
    }

    public boolean isExpertiseBestDG2() {
        return expertiseBestDG2;
    }

    public boolean isExpertiseDE() {
        return expertiseDE;
    }

    public boolean isExpertiseEN() {
        return expertiseEN;
    }

    public boolean isExpertiseFR() {
        return expertiseFR;
    }

    public boolean isPartTimeCheck() {
        return partTimeCheck;
    }

    public boolean isValid() {
        if (getUser() == null || getUser().isValid() == false) return false;
        if (getProfile() == null || getProfile().isValid() == false) return false;
        if (isPartTimeCheck() && getPartTimeDateRange().isValid() == false) return false;
        if (isAbsenceCheck() && getAbsenceDateRange().isValid() == false) return false;
        return true;
    }

    public void setAbsenceCheck(boolean newAbsenceCheck) {
        boolean oldAbsenceCheck = absenceCheck;
        absenceCheck = newAbsenceCheck;
        firePropertyChange("absenceCheck", new Boolean(oldAbsenceCheck), new Boolean(absenceCheck));
    }

    public void setAbsenceDateRange(DateRange newAbsenceDateRange) {
        if (newAbsenceDateRange == null) throw new NullPointerException("Invalid parameter");
        DateRange oldAbsenceDateRange = absenceDateRange;
        absenceDateRange = newAbsenceDateRange;
        firePropertyChange("absenceDateRange", oldAbsenceDateRange, absenceDateRange);
    }

    public void setDefaultTeam(boolean newDefaultTeam) {
        boolean oldDefaultTeam = defaultTeam;
        defaultTeam = newDefaultTeam;
        firePropertyChange("defaultTeam", new Boolean(oldDefaultTeam), new Boolean(defaultTeam));
    }

    public void setDeletePmSettings(boolean newDeletePmSettings) {
        boolean oldDeletePmSettings = deletePmSettings;
        deletePmSettings = newDeletePmSettings;
        firePropertyChange("deletePmSettings", new Boolean(oldDeletePmSettings), new Boolean(deletePmSettings));
    }

    public void setDeputy(ItemMemberUser newDeputy) {
        if (newDeputy != null && !newDeputy.isValid()) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        ItemMemberUser oldDeputy = deputy;
        deputy = newDeputy;
        firePropertyChange("deputy", oldDeputy, deputy);
    }

    public void setExpertiseBestDG1(boolean newExpertiseBestDG1) {
        boolean oldExpertiseBestDG1 = expertiseBestDG1;
        expertiseBestDG1 = newExpertiseBestDG1;
        firePropertyChange("expertiseBestDG1", new Boolean(oldExpertiseBestDG1), new Boolean(expertiseBestDG1));
    }

    public void setExpertiseBestDG2(boolean newExpertiseBestDG2) {
        boolean oldExpertiseBestDG2 = expertiseBestDG2;
        expertiseBestDG2 = newExpertiseBestDG2;
        firePropertyChange("expertiseBestDG2", new Boolean(oldExpertiseBestDG2), new Boolean(expertiseBestDG2));
    }

    public void setExpertiseDE(boolean newExpertiseDE) {
        boolean oldExpertiseDE = expertiseDE;
        expertiseDE = newExpertiseDE;
        firePropertyChange("expertiseDE", new Boolean(oldExpertiseDE), new Boolean(expertiseDE));
    }

    public void setExpertiseEN(boolean newExpertiseEN) {
        boolean oldExpertiseEN = expertiseEN;
        expertiseEN = newExpertiseEN;
        firePropertyChange("expertiseEN", new Boolean(oldExpertiseEN), new Boolean(expertiseEN));
    }

    public void setExpertiseFR(boolean newExpertiseFR) {
        boolean oldExpertiseFR = expertiseFR;
        expertiseFR = newExpertiseFR;
        firePropertyChange("expertiseFR", new Boolean(oldExpertiseFR), new Boolean(expertiseFR));
    }

    public void setPartTimeCheck(boolean newPartTimeCheck) {
        boolean oldPartTimeCheck = partTimeCheck;
        partTimeCheck = newPartTimeCheck;
        firePropertyChange("partTimeCheck", new Boolean(oldPartTimeCheck), new Boolean(partTimeCheck));
    }

    public void setPartTimeDateRange(DateRange newPartTimeDateRange) {
        if (newPartTimeDateRange == null) throw new NullPointerException("Invalid parameter");
        DateRange oldPartTimeDateRange = partTimeDateRange;
        partTimeDateRange = newPartTimeDateRange;
        firePropertyChange("partTimeDateRange", oldPartTimeDateRange, partTimeDateRange);
    }

    public void setPartTimeValueFriday(int newPartTimeValueFriday) {
        int oldPartTimeValueFriday = partTimeValueFriday;
        partTimeValueFriday = getValidPartTimeValue(newPartTimeValueFriday);
        firePropertyChange("partTimeValueFriday", new Integer(oldPartTimeValueFriday), new Integer(partTimeValueFriday));
    }

    public void setPartTimeValueMonday(int newPartTimeValueMonday) {
        int oldPartTimeValueMonday = partTimeValueMonday;
        partTimeValueMonday = getValidPartTimeValue(newPartTimeValueMonday);
        firePropertyChange("partTimeValueMonday", new Integer(oldPartTimeValueMonday), new Integer(partTimeValueMonday));
    }

    public void setPartTimeValueSaturday(int newPartTimeValueSaturday) {
        int oldPartTimeValueSaturday = partTimeValueSaturday;
        partTimeValueSaturday = getValidPartTimeValue(newPartTimeValueSaturday);
        firePropertyChange("partTimeValueSaturday", new Integer(oldPartTimeValueSaturday), new Integer(partTimeValueSaturday));
    }

    public void setPartTimeValueThursday(int newPartTimeValueThursday) {
        int oldPartTimeValueThursday = partTimeValueThursday;
        partTimeValueThursday = getValidPartTimeValue(newPartTimeValueThursday);
        firePropertyChange("partTimeValueThursday", new Integer(oldPartTimeValueThursday), new Integer(partTimeValueThursday));
    }

    public void setPartTimeValueTuesday(int newPartTimeValueTuesday) {
        int oldPartTimeValueTuesday = partTimeValueTuesday;
        partTimeValueTuesday = getValidPartTimeValue(newPartTimeValueTuesday);
        firePropertyChange("partTimeValueTuesday", new Integer(oldPartTimeValueTuesday), new Integer(partTimeValueTuesday));
    }

    public void setPartTimeValueWednesday(int newPartTimeValueWednesday) {
        int oldPartTimeValueWednesday = partTimeValueWednesday;
        partTimeValueWednesday = getValidPartTimeValue(newPartTimeValueWednesday);
        firePropertyChange("partTimeValueWednesday", new Integer(oldPartTimeValueWednesday), new Integer(partTimeValueWednesday));
    }

    public void setProfile(ItemMemberProfile newProfile) {
        ItemMemberProfile oldProfile = profile;
        profile = newProfile;
        firePropertyChange("profile", oldProfile, profile);
    }

    /**
	 * Sets the member task. Must be a non empty string after leading and
	 * trailing whitespace removed.
	 * 
	 * @param newTask java.lang.String
	 */
    public void setTask(String newTask) throws PhoenixUserException {
        if (newTask == null) throw new NullPointerException();
        if (newTask.trim().length() == 0) throw new PhoenixUserException(newTask);
        String oldTask = task;
        task = Util.truncateTrim(newTask, 24);
        firePropertyChange("task", oldTask, task);
    }

    public void setUser(ItemMemberUser newUser) {
        if (newUser == null) {
            throw new NullPointerException("Invalid parameter");
        }
        if (newUser.isValid() == false) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        ItemMemberUser oldUser = user;
        user = newUser;
        firePropertyChange("user", oldUser, user);
    }
}
