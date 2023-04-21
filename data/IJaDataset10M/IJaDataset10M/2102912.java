package org.fb4j.impl;

import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.fb4j.Session;
import org.fb4j.users.UserInfo;
import org.fb4j.users.UserStatus;
import org.fb4j.users.UserInfo.EducationInfo;
import org.fb4j.users.UserInfo.NetworkAffiliation;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mino Togna
 * 
 */
public class UserInfoTest extends SessionTestBase {

    @Test
    public void testUserInfoInSession() {
        Session session = getSession();
        UserInfo userInfo = session.getUserInfo(session.getLoggedInUser());
        printUserInfo(userInfo);
        Assert.assertNotNull(userInfo);
    }

    @Test(expected = UninitializedFieldError.class)
    public void testUserInfoQueryWithException() {
        Session session = getSession();
        Long[] uids = new Long[] { 591307764L, 752141196L, 733192756L };
        UserInfo.Field[] fields = new UserInfo.Field[] { UserInfo.Field.UID, UserInfo.Field.FIRST_NAME, UserInfo.Field.LAST_NAME };
        Set<UserInfo> set = session.getUserInfo(uids, fields);
        for (UserInfo userInfo : set) {
            log.info(userInfo.getFirstName());
            log.info(userInfo.getName());
        }
    }

    @Test(expected = UninitializedFieldError.class)
    public void testUserInfoQueryWithException2() {
        Session session = getSession();
        Long[] uids = new Long[] { 591307764L, 752141196L, 733192756L };
        UserInfo.Field[] fields = new UserInfo.Field[] { UserInfo.Field.UID, UserInfo.Field.FIRST_NAME, UserInfo.Field.LAST_NAME, UserInfo.Field.PIC };
        Set<UserInfo> set = session.getUserInfo(uids, fields);
        for (UserInfo userInfo : set) {
            log.info(userInfo.getFirstName());
            log.info(userInfo.getProfilePicture().getDefault());
            log.info(userInfo.getProfilePicture().getDefaultWithLogo());
        }
    }

    @Test
    public void testUserInfoQuery() {
        Session session = getSession();
        Long[] uids = new Long[] { 591307764L, 752141196L, 733192756L };
        Set<UserInfo> set = session.getUserInfo(uids);
        for (UserInfo userInfo : set) {
            printUserInfo(userInfo);
        }
    }

    public void testGetUserStatusError() {
        Session session = getSession();
        Set<UserStatus> userStatuses = session.getUserStatuses(752141196L);
        for (UserStatus userStatus : userStatuses) {
            log.debug("User status: " + userStatus);
        }
        Assert.assertNotNull(userStatuses);
        Assert.assertTrue(userStatuses.size() > 0);
    }

    @Test
    public void testGetUserStatus() {
        Session session = getSession();
        Set<UserStatus> userStatuses = session.getUserStatuses();
        for (UserStatus userStatus : userStatuses) {
            log.debug("User status: " + userStatus);
        }
        Assert.assertNotNull(userStatuses);
        Assert.assertTrue(userStatuses.size() > 0);
    }

    @Test
    public void testSetStatus() {
        Session session = getSession();
        boolean b = session.setStatus("is still setting his status via fb4j, but with status.set method call");
        log.debug(b);
        Assert.assertTrue(b);
    }

    @Test
    public void testIsVerifiedUser() {
        Session session = getSession();
        boolean b = session.isVerifiedUser();
        log.debug("is verified user? " + b);
        Assert.assertFalse(b);
    }

    private void printUserInfo(UserInfo userInfo) {
        log.debug("====================" + userInfo.getFirstName() + " " + userInfo.getLastName() + "========================================");
        log.debug(userInfo.getAboutMe());
        log.debug(userInfo.getActivities());
        log.debug(userInfo.getBirthday());
        log.debug(userInfo.getBooks());
        log.debug(userInfo.getFirstName());
        log.debug(userInfo.getInterests());
        log.debug(userInfo.getLastName());
        log.debug(userInfo.getMovies());
        log.debug(userInfo.getMusic());
        log.debug(userInfo.getName());
        log.debug(userInfo.getProfilePicture().getDefault());
        log.debug(userInfo.getProfilePicture().getBig());
        log.debug(userInfo.getProfilePicture().getBigWithLogo());
        log.debug(userInfo.getProfilePicture().getSmall());
        log.debug(userInfo.getProfilePicture().getSmallWithLogo());
        log.debug(userInfo.getProfilePicture().getSquare());
        log.debug(userInfo.getProfilePicture().getSquareWithLogo());
        log.debug(userInfo.getProfilePicture().getDefaultWithLogo());
        log.debug(userInfo.getPoliticalViews());
        log.debug(userInfo.getProfileUrl());
        if (userInfo.getEmailHashes() != null) log.debug("Email hashes: " + StringUtils.join(userInfo.getEmailHashes(), ','));
        log.debug(userInfo.getProxiedEmail());
        log.debug(userInfo.getQuotes());
        log.debug(userInfo.getRelationshipStatus());
        log.debug(userInfo.getReligion());
        log.debug(userInfo.getSex());
        log.debug(userInfo.getSignificantOtherId());
        log.debug(userInfo.getTv());
        log.debug("User id: " + userInfo.getUserId());
        log.debug("Is app user: " + userInfo.isAppUser());
        log.debug(userInfo.getWallCount());
        for (NetworkAffiliation affiliation : userInfo.getAffiliations()) {
            log.debug("affiliation: " + affiliation.toString());
        }
        log.debug("user location: " + userInfo.getCurrentLocation());
        if (userInfo.getEducationHistory() != null) {
            for (EducationInfo edInfo : userInfo.getEducationHistory()) {
                log.debug("Education info: " + edInfo);
            }
        }
        log.debug("High school: " + userInfo.getHighSchoolInfo());
        log.debug("Home town location: " + userInfo.getHometownLocation());
        log.debug("Locale: " + userInfo.getLocale());
        if (userInfo.getMeetingFor() != null) {
            log.debug("meeting for: " + StringUtils.join(userInfo.getMeetingFor(), ","));
        }
        if (userInfo.getMeetingSex() != null) {
            log.debug("meeting sex: " + StringUtils.join(userInfo.getMeetingSex(), ","));
        }
        log.debug("Notes count: " + userInfo.getNotesCount());
        if (userInfo.getWorkHistory() != null) {
            log.debug("Work History:" + StringUtils.join(userInfo.getWorkHistory(), ","));
        }
        log.debug("User status:" + userInfo.getStatus());
        log.debug("====================" + userInfo.getFirstName() + " " + userInfo.getLastName() + "========================================");
        log.debug("\n");
        log.debug("\n");
    }
}
