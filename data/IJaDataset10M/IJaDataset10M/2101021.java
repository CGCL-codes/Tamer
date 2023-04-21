package org.cofax.cms;

import org.cofax.*;
import javax.servlet.http.*;
import java.util.*;

/**
* CofaxToolsUser:
* CofaxToolsUser is instantiated as a session object
* for each new user - utilities for permissions, workflow, etc flow through this object.
* @author Charles Harvey
**/
public class CofaxToolsUser {

    /**
     * Contains info from the user table such as name, phone, etc.
     *
     **/
    public HashMap userInfoHash;

    /**
     * Contains Hashes of publications this user is grouped to
     * and group type permissions for each pub.
     *
     **/
    public Vector userPubsVectorOHash;

    /**
     * Contains a list of all pubIDs/ pubDescs this user is associated with.
     *
     **/
    public HashMap userPubDescPubIDHash;

    /**
     * Contains a list of all pubIDs/ pubNames this user is associated with.
     *
     **/
    public HashMap userPubNamePubIDHash;

    /**
     * Contains a list of all pubNames/ pubDescs this user is associated with.
     *
     **/
    public HashMap userPubNamePubDescHash;

    /**
     * Contains a list of all groups this user is associated with.
     *
     **/
    public Vector userGroupVector;

    /**
	* Contains a list of all groups types this user is associated with.
	*
	**/
    public Vector userGroupTypeVector;

    /**
     * Contains a list of all group names and id's this user is associated with.
     *
     **/
    public HashMap userGroupNameGroupIDHash;

    /**
     * Contains the current pubID that this user is grouped to.
     *
     **/
    public String workingPub;

    /**
     * Contains the current pubDesc that this user is grouped to.
     *
     **/
    public String workingPubDesc;

    /**
     * Contains the current pubName that this user is grouped to.
     *
     **/
    public String workingPubName;

    /**
     * Contains the configuration (global variables) for the user's current working publication.
     *
     **/
    public HashMap workingPubConfigElementsHash;

    /**
     * Contains articles previously edited by this user in this session.
     *
     **/
    public HashMap userPreviousArticlesHash;

    /**
     * Show the current contents of the user instance.
     *
     **/
    public void report() {
        CofaxToolsUtil.showHash("userInfoHash", userInfoHash);
        CofaxToolsUtil.showVector("userGroupVector", userGroupVector);
        CofaxToolsUtil.showHash("userPubDescPubIDHash", userPubDescPubIDHash);
        CofaxToolsUtil.showHash("userPubNamePubIDHash", userPubNamePubIDHash);
        CofaxToolsUtil.showHash("workingPubConfigElementsHash", workingPubConfigElementsHash);
        CofaxToolsUtil.showHash("userPreviousArticlesHash", userPreviousArticlesHash);
        CofaxToolsUtil.showHash("userPubNamePubDescHash", userPubNamePubDescHash);
        CofaxToolsUtil.showHash("userGroupNameGroupIDHash", userGroupNameGroupIDHash);
        CofaxToolsUtil.showVectorOfHashes("userPubsVectorOHash", userPubsVectorOHash);
        CofaxToolsUtil.log("Working pub: " + workingPub);
        CofaxToolsUtil.log("Working pub description: " + workingPubDesc);
        CofaxToolsUtil.log("Working pub name: " + workingPubName);
    }

    public boolean hasPublicationRights(String askingPub) {
        Vector vec = userPubsVectorOHash;
        Iterator it = vec.iterator();
        boolean hasPubRights = false;
        while (it.hasNext()) {
            Object hasPub = it.next();
            HashMap hasPubHash = (HashMap) hasPub;
            Iterator en = hasPubHash.keySet().iterator();
            while (en.hasNext()) {
                String pubS = (String) en.next();
                if (pubS.equals(askingPub)) {
                    hasPubRights = true;
                }
            }
        }
        return hasPubRights;
    }

    /**
     *  Sets the globals that we will need when working with the current pub for
     *  http construction, FTP file transfer, etc.
     *
     *@param  db         The new userWorkingPubConfigElements value
     *@param  session    The new userWorkingPubConfigElements value
     *@param  askingPub  The new userWorkingPubConfigElements value
     */
    public boolean setUserWorkingPubConfigElements(DataStore db, HttpSession session, String askingPub) {
        boolean hasRights = false;
        if (hasPublicationRights(askingPub)) {
            workingPubDesc = CofaxToolsUtil.getPubDescFromID(db, askingPub);
            workingPub = askingPub;
            workingPubName = CofaxToolsUtil.getPubNameFromID(db, askingPub);
            HashMap fillReq = new HashMap();
            fillReq.put("PUBID", askingPub);
            String tag = CofaxToolsDbUtils.fillTag(db, "getAllFromConfigByPubID");
            workingPubConfigElementsHash = CofaxToolsDbUtils.getNameValuePackageHash(db, fillReq, tag);
            hasRights = true;
        }
        return hasRights;
    }

    /**
     *  Updates or inserts info into the user table and associated tables as
     *  appropriate.
     *
     *@param  db   The new userInfo value
     *@param  req  The new userInfo value
     *@return      Description of the Return Value
     */
    public static String setUserInfo(DataStore db, HttpServletRequest req) {
        HashMap ht = CofaxToolsUtil.splitPostQuery(req);
        StringBuffer results = new StringBuffer();
        String tag = "";
        String userID = (String) ht.get("USERID");
        String homePub = (String) ht.get("PUBLICATION");
        String firstName = (String) ht.get("FIRSTNAME");
        String lastName = (String) ht.get("LASTNAME");
        ht.put("HOMEPUB", homePub);
        String userName = (String) ht.get("USERNAME");
        String userPassword = (String) ht.get("USERPASSWORD");
        String userPasswordValidation = (String) ht.get("userPasswordValidation");
        String delete = req.getParameter("delete");
        int checkBoxTableLength = Integer.parseInt((String) ht.get("checkBoxTableLength"));
        int checkBoxTableLength2 = 0;
        try {
            checkBoxTableLength2 = Integer.parseInt((String) ht.get("checkBoxTableRightLength"));
        } catch (Exception e) {
            checkBoxTableLength2 = 0;
        }
        if (delete == null) {
            if ((firstName.equals("")) || (lastName.equals("")) || (userName.equals(""))) {
                return ("<FONT COLOR=RED>You must enter a user name, user " + "password, first name, and last name on creation of a user...</FONT>");
            }
            if (!userPassword.equals(userPasswordValidation)) {
                return ("<FONT COLOR=RED>Passwords do not match. Please click " + "back on your browser and try again.</FONT>");
            }
            int groupCheck = 0;
            for (int i = 0; i <= checkBoxTableLength; i++) {
                String temp = String.valueOf(i);
                String test = req.getParameter(temp);
                if (test != null) {
                    groupCheck++;
                }
            }
            if (groupCheck == 0) {
                return ("<FONT COLOR=RED>A user must be joined to at least " + "one group before creation. Please click back on your " + "browser and try again.</FONT>");
            }
        }
        if (delete != null) {
            HashMap htTempor = new HashMap();
            if (userID != null) {
                htTempor.put("USERID", userID);
                tag = CofaxToolsDbUtils.fillTag(db, "deleteUserFromTblUsersAndTblUserToGroups");
                List list = CofaxToolsDbUtils.getPackageData(db, htTempor, tag);
                return ("User " + userID + " (" + ht.get("USERNAME") + ") killed");
            } else {
                return ("<FONT COLOR=RED>You must choose a user before you can " + "delete a user...</FONT>");
            }
        }
        if ((userID == null) || (userID.equals(""))) {
            userID = "0";
        }
        ht.put("USERID", userID);
        tag = CofaxToolsDbUtils.fillTag(db, "updateUserInfo");
        List list = CofaxToolsDbUtils.getPackageData(db, ht, tag);
        HashMap dupHolder = (HashMap) list.get(0);
        String duplicateCheck = (String) dupHolder.get("DUPLICATE");
        if ((duplicateCheck != null) && (!duplicateCheck.equals(""))) {
            return ("<FONT COLOR=RED>You have attempted to add a user with " + "the same user name as a current user. Please click back " + "on your browser, change the user name, and try again.</FONT>");
        }
        HashMap hm = (HashMap) list.get(0);
        userID = (String) hm.get("USERID");
        HashMap htTempp = new HashMap();
        htTempp.put("USERID", userID);
        for (int i = 0; i < checkBoxTableLength2; i++) {
            String temp = String.valueOf(i);
            String Section = req.getParameter("PARAM" + temp);
            htTempp.put("MappingCode", Section);
            String groupID = req.getParameter("s" + temp);
            String Director = req.getParameter("d" + temp);
            if (groupID != null && (Director == null)) {
                htTempp.put("director", "0");
                htTempp.put("action", "1");
            } else if ((groupID != null) && (Director != null)) {
                htTempp.put("director", Director);
                htTempp.put("action", "2");
            } else if ((groupID == null) && (Director != null)) {
                htTempp.put("director", Director);
                htTempp.put("action", "2");
            } else {
                htTempp.put("action", "0");
            }
            tag = CofaxToolsDbUtils.fillTag(db, "updateUserSection");
            List res = CofaxToolsDbUtils.getPackageData(db, htTempp, tag);
        }
        HashMap htTempo = new HashMap();
        htTempo.put("USERID", userID);
        for (int i = 0; i < checkBoxTableLength; i++) {
            String temp = String.valueOf(i);
            String groupIDName = req.getParameter("PARAMNAME" + temp);
            htTempo.put("groupID", groupIDName);
            String groupID = req.getParameter(temp);
            if (groupID != null) {
                htTempo.put("action", "1");
            } else {
                htTempo.put("action", "0");
            }
            tag = CofaxToolsDbUtils.fillTag(db, "updateUserGroups");
            List res = CofaxToolsDbUtils.getPackageData(db, htTempo, tag);
        }
        return ("User " + req.getParameter("USERNAME") + " saved. Please choose another action.");
    }

    /**
     *  Updates or inserts info into the user table and associated tables as
     *  appropriate.
     *
     *@param  db   The new userInfo value
     *@param  req  The new userInfo value
     *@return      Description of the Return Value
     */
    public static String setMyCountInfo(DataStore db, HttpServletRequest req) {
        HashMap ht = CofaxToolsUtil.splitPostQuery(req);
        StringBuffer results = new StringBuffer();
        String tag = "";
        String userID = (String) ht.get("USERID");
        String homePub = (String) ht.get("PUBLICATION");
        String firstName = (String) ht.get("FIRSTNAME");
        String lastName = (String) ht.get("LASTNAME");
        ht.put("HOMEPUB", homePub);
        String userName = (String) ht.get("USERNAME");
        String userPassword = (String) ht.get("USERPASSWORD");
        String userPasswordValidation = (String) ht.get("userPasswordValidation");
        if ((firstName.equals("")) || (lastName.equals("")) || (userName.equals(""))) {
            return ("<FONT COLOR=RED>You must enter a user name, user " + ", first name, and last name on creation of a user...</FONT>");
        }
        if (!(userPassword.equals(""))) {
            if (!userPassword.equals(userPasswordValidation)) {
                return ("<FONT COLOR=RED>Passwords do not match. Please click " + "back on your browser and try again.</FONT>");
            }
        }
        if ((userID == null) || (userID.equals(""))) {
            userID = "0";
        }
        ht.put("USERID", userID);
        tag = CofaxToolsDbUtils.fillTag(db, "updateUserInfo");
        List list = CofaxToolsDbUtils.getPackageData(db, ht, tag);
        HashMap dupHolder = (HashMap) list.get(0);
        String duplicateCheck = (String) dupHolder.get("DUPLICATE");
        if ((duplicateCheck != null) && (!duplicateCheck.equals(""))) {
            return ("<FONT COLOR=RED>You have attempted to add a user with " + "the same user name as a current user. Please click back " + "on your browser, change the user name, and try again.</FONT>");
        }
        HashMap hm = (HashMap) list.get(0);
        userID = (String) hm.get("USERID");
        return ("Your count was successfully modified. Please choose another action.");
    }

    /**
    * Delete a saved search 
    *
    **/
    public static String deleteSavedSearch(DataStore db, HttpServletRequest req) {
        String userID = req.getParameter("USERID");
        String searchName = req.getParameter("SEARCHNAME");
        if (((userID != null) && (!(userID.equals("")))) && ((searchName != null) && (!(searchName.equals(""))))) {
            StringBuffer v_del = new StringBuffer();
            v_del.append("delete from tblPermSavedSearches WHERE userID = " + userID + " and searchName='" + searchName + "' ");
            HashMap ht = new HashMap();
            List del = CofaxToolsDbUtils.getPackageData(db, ht, v_del.toString());
            return ("Search " + searchName + " deleted.");
        } else {
            return ("You must select a search");
        }
    }
}
