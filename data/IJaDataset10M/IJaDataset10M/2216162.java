package com.tysanclan.site.projectewok.imports.tango;

import org.apache.wicket.spring.injection.annot.SpringBean;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class UserRecordHandler implements RecordHandler {

    @SpringBean
    private UserService userService;

    private static final long TIME_FULL = 1000L * 60L * 60L * 24L * 30L * 4;

    private static final long TIME_SENIOR = 1000L * 60L * 60L * 24L * 365L * 2L;

    private static final long TIME_REVERED = 1000L * 60L * 60L * 24L * 365L * 5L;

    /**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
    @Override
    public String getRecordDescriptor() {
        return "U";
    }

    @Override
    public boolean handle(String[] data, TangoImporterCallback callback) {
        String username = data[2], hashedPassword = data[3], rank = data[4], email = data[5], joinDateStamp = data[6], signature = data[7], avatarURL = data[9], customTitle = data[8];
        long joinTime = Long.parseLong(joinDateStamp), currentTime = System.currentTimeMillis(), key = Long.parseLong(data[1]);
        Rank assignedRank = determineAssignedRank(rank, joinTime, currentTime);
        boolean retired = isUserRetired(rank);
        User user = userService.createUser(username, hashedPassword, email);
        if (user != null) {
            if (!userService.setUserImportData(user.getId(), assignedRank, joinTime)) {
                return false;
            }
            if (!userService.setUserRetirement(user.getId(), retired)) {
                return false;
            }
            if (!userService.setUserAvatar(user.getId(), avatarURL)) {
                return false;
            }
            if (!userService.setUserCustomTitle(user.getId(), customTitle)) {
                return false;
            }
            if (!userService.setUserSignature(user.getId(), signature)) {
                return false;
            }
            callback.registerImportedObject(key, user);
            return true;
        }
        return false;
    }

    private boolean isUserRetired(String rank) {
        boolean retired = rank.equals("Honorary");
        return retired;
    }

    private Rank determineAssignedRank(String rank, long joinTime, long currentTime) {
        Rank assignedRank;
        if (rank.equals("CouncilMember") || rank.equals("Prince")) {
            assignedRank = Rank.SENATOR;
        } else if (rank.equals("King")) {
            assignedRank = Rank.CHANCELLOR;
        } else if (rank.equals("TruthSayer")) {
            assignedRank = Rank.TRUTHSAYER;
        } else if (rank.equals("Peasant")) {
            assignedRank = Rank.TRIAL;
        } else {
            assignedRank = determineRankBasedOnMembershipTime(joinTime, currentTime);
        }
        return assignedRank;
    }

    private Rank determineRankBasedOnMembershipTime(long joinTime, long currentTime) {
        Rank assignedRank;
        long membershipLength = currentTime - joinTime;
        if (membershipLength > TIME_REVERED) {
            assignedRank = Rank.REVERED_MEMBER;
        } else if (membershipLength > TIME_SENIOR) {
            assignedRank = Rank.SENIOR_MEMBER;
        } else if (membershipLength > TIME_FULL) {
            assignedRank = Rank.FULL_MEMBER;
        } else {
            assignedRank = Rank.JUNIOR_MEMBER;
        }
        return assignedRank;
    }

    /**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
    @Override
    public void cleanup() {
        userService = null;
    }
}
