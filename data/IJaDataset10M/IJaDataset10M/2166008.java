package portal.presentation.cafe.web;

import org.w3c.dom.Element;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;
import hambo.app.base.ProtectedPortalPage;
import hambo.util.Device;
import hambo.app.util.Link;
import hambo.app.util.DOMUtil;
import hambo.util.StringUtil;
import hambo.config.ConfigManager;
import hambo.community.SearchFriends;
import hambo.community.FriendsManager;
import hambo.community.ObjectUserCafe;
import hambo.community.ObjectFriend;
import hambo.util.XMLUtil;
import hambo.community.CommunityApplication;
import hambo.app.util.Link;
import hambo.user.HamboUserManager;
import hambo.user.HamboUser;
import hambo.community.ObjectLogin;
import hambo.community.GuestbookManager;
import hambo.positioningapi.data.VerificationData;

/**
 * Page used for Light-Search in Cafe and Display the Results (MAX 30 Results)
 */
public class cffriends extends ProtectedPortalPage {

    private boolean isUserPosActivated;

    private String posresult;

    private String posvictim;

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public cffriends() {
        super("cffriends");
    }

    /**
    * The over-ridden method that is called automatically 
    * by {@link PortalPage}.run().
    */
    public void processPage() {
        cfStatic.fixNavigation(navigation);
        helpComponent = cfStatic.includeMarketMessage(language, comms, getContext().getDevice(), helpComponent);
        if (getContext().getSessionAttributeAsString("camefrom") != null) getContext().removeSessionAttribute("camefrom");
        paintMainArea();
    }

    /**
     * Paint the Main Area
     */
    private void paintMainArea() {
        Element element_tdresult = (Element) getElement("tdresult");
        Element element_noresult = (Element) getElement("noresult");
        Element element_tableresult = (Element) getElement("tableresult");
        Element element_trresultlabel = (Element) getElement("trresultlabel");
        isUserPosActivated = VerificationData.isUserAdminActivated(user_id);
        posresult = getParameter("posresult");
        posvictim = getParameter("posvictim");
        handlePositioning();
        SearchFriends sf = new SearchFriends(user_id);
        FriendsManager fm = cfStatic.isUserRegistered(comms, user_id);
        String user_uid = getContext().getSessionAttributeAsString("user_uid");
        if (CommunityApplication.USE_LIGHT_SEARCH) {
            cfvirtuallightsearch vobjsearch = new cfvirtuallightsearch();
            vobjsearch.run(comms, user_id);
            Element virtuallightsearch = getElement("lightsearch");
            if (virtuallightsearch != null) {
                DOMUtil.setFirstNodeText(virtuallightsearch, vobjsearch.toString());
            }
        } else {
            removeElement("lightsearch");
        }
        Element elem_nbGbEntr = (Element) getElement("nbGbEntr");
        GuestbookManager gm = new GuestbookManager(user_uid);
        int nbGbEntr = gm.countNewGuestbookEntries(user_uid);
        if (elem_nbGbEntr != null && nbGbEntr != 0) {
            DOMUtil.setFirstNodeText(elem_nbGbEntr, Integer.toString(nbGbEntr));
        }
        if (nbGbEntr == 0) {
            removeElement("tableGbEntr");
        }
        fixFriends(fm, user_uid);
    }

    /**
    * Print the left Part of the Page: My Friends
    */
    private void fixFriends(FriendsManager fm, String user_uid) {
        printFriendsList(fm, user_uid);
        printMyRequestList(fm, user_uid);
        printTheirRequestList(fm, user_uid);
    }

    /**
     * Print the List of the Friends of the current User
     * Print a message "No friends" if this user doesnt have any friends yet
     **/
    private void printFriendsList(FriendsManager fm, String user_uid) {
        Vector vMyFriends = fm.getAllMyFriends(user_uid);
        HamboUserManager man = new HamboUserManager();
        Element element_trfriend = (Element) getElement("trfriend");
        if (element_trfriend != null) {
            if (vMyFriends != null && vMyFriends.size() > 0) {
                removeElement("nofriend");
                boolean grey = true;
                for (Enumeration e = vMyFriends.elements(); e.hasMoreElements(); ) {
                    if (grey == true) {
                        element_trfriend.setAttribute("class", "boxodd");
                        grey = false;
                    } else {
                        element_trfriend.setAttribute("class", "boxeven");
                        grey = true;
                    }
                    ObjectFriend friend = (ObjectFriend) e.nextElement();
                    String fuid = friend.getuId();
                    String nickname = friend.getNick();
                    String status = friend.getStatus();
                    String editedFriendSettings = friend.getSettingsEdited();
                    String friendID = friend.getId();
                    int nbmsg = friend.getNbMessage();
                    Element newrow = (Element) element_trfriend.cloneNode(true);
                    Element friend_nick = (Element) getElement(newrow, "friendnick");
                    DOMUtil.setFirstNodeText(friend_nick, nickname);
                    friend_nick.removeAttribute("HREF");
                    Element element_friendsetting = (Element) getElement(newrow, "nonedited");
                    if (!(element_friendsetting != null && editedFriendSettings.equals("N"))) {
                        element_friendsetting.getParentNode().removeChild(element_friendsetting);
                    }
                    Link linksendmsg = new Link("cfprofile");
                    linksendmsg.addParam("fuid", "" + fuid);
                    friend_nick.setAttribute("href", linksendmsg.toString());
                    Element element_editfriend = (Element) getElement(newrow, "editfriend");
                    element_editfriend.removeAttribute("HREF");
                    Link linkprofilefriend = new Link("cfsendmsg");
                    linkprofilefriend.addParam("fuid", "" + fuid);
                    element_editfriend.setAttribute("href", linkprofilefriend.toString());
                    Element element_login = (Element) getElement(newrow, "login");
                    if (element_login != null) {
                        Vector vLogins = fm.getLogins(fuid, 1);
                        if (vLogins.size() > 0) {
                            for (Enumeration e1 = vLogins.elements(); e1.hasMoreElements(); ) {
                                ObjectLogin aLogin = (ObjectLogin) e1.nextElement();
                                String theLoginDate = fm.getLocalDate(aLogin.getLoginDate(), (Locale) getContext().getSessionAttribute("locale"));
                                String theLoginTime = fm.getLocalTime(aLogin.getLoginTime(), getContext().getSessionAttributeAsString("timezone"), (Locale) getContext().getSessionAttribute("locale"), getContext().getSessionAttributeAsString("timezone"), language);
                                DOMUtil.setFirstNodeText(element_login, theLoginDate + " " + theLoginTime);
                            }
                        } else {
                            element_login.getParentNode().removeChild(element_login);
                        }
                    }
                    Element trpos = getElement(newrow, "trposlink");
                    if (isUserPosActivated) {
                        if (VerificationData.isUserAdminActivated(friendID)) {
                            if (VerificationData.checkAllowPositioningByUser(friendID, user_id)) {
                                Link poslink = new Link("cfpositionfriend");
                                poslink.addParam("friendid", friendID);
                                poslink.addParam("nick", nickname);
                                poslink.addParam("fuid", fuid);
                                Element myPoslink = (Element) getElement(newrow, "poslink");
                                myPoslink.setAttribute("href", poslink.toString());
                            } else {
                                removeElement(newrow, "trposlink");
                            }
                        } else removeElement(newrow, "trposlink");
                    }
                    Link linkdel = new Link("cfredirectordelete");
                    linkdel.addParam("fuid", fuid);
                    linkdel.addParam("ac", "f");
                    Element elementDeleteLink = DOMUtil.getElementById(newrow, "deleteFriendship");
                    elementDeleteLink.removeAttribute("href");
                    elementDeleteLink.setAttribute("href", linkdel.toString());
                    Element friend_status = (Element) getElement(newrow, "friendstatus");
                    if (status != null && status.equals("online") && friend_status != null) {
                        int device = friend.getDevice();
                        friend_status.setAttribute("src", ConfigManager.getConfig("server").getProperty("themes_base") + "/images/web/icons/cafe_online_" + device + ".gif");
                    }
                    if (nbmsg == 0) {
                        Element element_msgyes = (Element) getElement(newrow, "msgyes");
                        if (element_msgyes != null) element_msgyes.getParentNode().removeChild(element_msgyes);
                    } else {
                        Element element_msgno = (Element) getElement(newrow, "msgno");
                        if (element_msgno != null) element_msgno.getParentNode().removeChild(element_msgno);
                        Element friend_nbmsg = (Element) getElement(newrow, "nbmsg");
                        Element friend_nbmsg2 = (Element) getElement(newrow, "nbmsg2");
                        if (friend_nbmsg != null && friend_nbmsg2 != null) {
                            DOMUtil.setFirstNodeText(friend_nbmsg, nbmsg + "");
                            friend_nbmsg.removeAttribute("HREF");
                            Link linkreadmsg = new Link("cfreadmsg");
                            linkreadmsg.addParam("fuid", "" + fuid);
                            int[] figures = fm.getSpaceInfo(user_uid);
                            int msglimit = CommunityApplication.ARCHIVE_RESTRICTION;
                            if ((figures[1] / msglimit) > 0.9) {
                                linkreadmsg.addParam("err", "(@cfmyarchfull@)");
                            }
                            friend_nbmsg.setAttribute("href", linkreadmsg.toString());
                            friend_nbmsg2.removeAttribute("HREF");
                            friend_nbmsg2.setAttribute("href", linkreadmsg.toString());
                        }
                    }
                    element_trfriend.getParentNode().insertBefore(newrow, element_trfriend);
                }
                element_trfriend.getParentNode().removeChild(element_trfriend);
            } else {
                element_trfriend.getParentNode().removeChild(element_trfriend);
            }
        }
    }

    /**
     * Print the list of people that the current user want be friend with: 
     * The Current User is waiting for their agreement
     **/
    private void printMyRequestList(FriendsManager fm, String user_uid) {
        Vector vMyRequest = fm.getAllMyRequest(user_uid);
        Element element_tdrequest = (Element) getElement("tdrequest");
        if (element_tdrequest != null) {
            if (vMyRequest != null && vMyRequest.size() > 0) {
                removeElement("tdnonew");
                for (Enumeration e = vMyRequest.elements(); e.hasMoreElements(); ) {
                    ObjectUserCafe friend = (ObjectUserCafe) e.nextElement();
                    String nickname = friend.getNick();
                    String fuid = friend.getuId();
                    Element newrow = (Element) element_tdrequest.cloneNode(true);
                    Link link2 = new Link("cfredirectordelete");
                    link2.addParam("fuid", fuid);
                    link2.addParam("ac", "w");
                    Element elementDeleteLink = DOMUtil.getElementById(newrow, "deleteLink");
                    elementDeleteLink.removeAttribute("href");
                    elementDeleteLink.setAttribute("href", link2.toString());
                    Element friend_nick = (Element) getElement(newrow, "requestnick");
                    Link link = new Link("cfprofile");
                    link.addParam("fuid", "" + fuid);
                    friend_nick.removeAttribute("href");
                    friend_nick.setAttribute("href", link.toString());
                    DOMUtil.setFirstNodeText(friend_nick, nickname);
                    element_tdrequest.getParentNode().insertBefore(newrow, element_tdrequest);
                }
                element_tdrequest.getParentNode().removeChild(element_tdrequest);
            } else {
                element_tdrequest.getParentNode().removeChild(element_tdrequest);
                removeElement("tdrequesthead");
            }
        }
    }

    /**
     * Print the list of people that want be friend with the current user
     **/
    private void printTheirRequestList(FriendsManager fm, String user_uid) {
        Vector vTheirRequest = fm.getAllTheirRequest(user_uid);
        Element element_tdTheyWait = (Element) getElement("tdTheyWait");
        if (element_tdTheyWait != null) {
            if (vTheirRequest != null && vTheirRequest.size() > 0) {
                removeElement("tdnonew");
                for (Enumeration e = vTheirRequest.elements(); e.hasMoreElements(); ) {
                    ObjectUserCafe friend = (ObjectUserCafe) e.nextElement();
                    String nickname = friend.getNick();
                    String fuid = friend.getuId();
                    Element newrow = (Element) element_tdTheyWait.cloneNode(true);
                    Link link2 = new Link("cfredirectordelete");
                    link2.addParam("fuid", fuid);
                    link2.addParam("ac", "r");
                    Element elementDeleteLink = DOMUtil.getElementById(newrow, "deleteLink2");
                    elementDeleteLink.removeAttribute("href");
                    elementDeleteLink.setAttribute("href", link2.toString());
                    Element friend_nick = (Element) getElement(newrow, "theirRequestnick");
                    friend_nick.removeAttribute("HREF");
                    Link link = new Link("cfapprove");
                    link.addParam("fuid", "" + fuid);
                    friend_nick.setAttribute("href", link.toString());
                    DOMUtil.setFirstNodeText(friend_nick, nickname);
                    element_tdTheyWait.getParentNode().insertBefore(newrow, element_tdTheyWait);
                }
                element_tdTheyWait.getParentNode().removeChild(element_tdTheyWait);
            } else {
                element_tdTheyWait.getParentNode().removeChild(element_tdTheyWait);
                removeElement("tdwaithead");
            }
        }
    }

    private void handlePositioning() {
        if (!isUserPosActivated) {
            removeElement("trpositioningresult");
            removeElement("trposlink");
        } else {
            if (posresult != null && !posresult.equals("")) {
                Element positioningresult = getElement("positioningresult");
                if (posresult.equals("-1")) {
                    removeElement("trposresult");
                } else {
                    DOMUtil.setFirstNodeText(positioningresult, posresult);
                    removeElement("trnoposresult");
                }
                Element positionvictim = getElement("positionvictim");
                if (posvictim != null) DOMUtil.setFirstNodeText(positionvictim, XMLUtil.decode("" + posvictim));
            } else {
                removeElement("trpositioningresult");
            }
        }
    }
}
