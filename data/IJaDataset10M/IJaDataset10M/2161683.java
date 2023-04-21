package com.khotyn.heresy.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.khotyn.heresy.bean.Friend;
import com.khotyn.heresy.dao.FriendDAO;
import com.khotyn.heresy.dao.UserDAO;

/**
 * 好友控制器，用于添加和删除好友 目前只支持单方面添加好友 URI:/addFriend.html（添加好友）
 * URI:/delFriend.html（删除好友）
 * 
 * @author 金灵杰
 * 
 */
@Controller
@SessionAttributes("userID")
public class FriendsController {

    @Autowired
    private FriendDAO friendDAO;

    @Autowired
    private UserDAO userDAO;

    /**
	 * @param userDAO the userDAO to set
	 */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
	 * @param friendDAO the friendDAO to set
	 */
    public void setFriendDAO(FriendDAO friendDAO) {
        this.friendDAO = friendDAO;
    }

    /**
	 * 添加好友
	 * 
	 * @param uid 好友ID
	 * @param myID 用户自己的ID
	 * @param response 服务器相应
	 */
    @RequestMapping("/addFriend.html")
    public void addFriend(@RequestParam(value = "friendID", required = false) String uid, @ModelAttribute("userID") int myID, HttpServletResponse response) {
        if (uid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int UID = 0;
        try {
            UID = Integer.parseInt(uid);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (friendDAO.selectOneFriend(myID, UID) != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        Friend newFriend = new Friend();
        newFriend.setUserID(myID);
        newFriend.setFriend(userDAO.selectUserBriefInfoByUserId(UID));
        friendDAO.insertNewFriend(newFriend);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
	 * 删除好友
	 * 
	 * @param friendIDStr 好友Id
	 * @param myID 用户自己的ID
	 * @param response 服务器响应
	 */
    @RequestMapping("/delFriend.html")
    public void delFriend(@RequestParam(value = "friendID", required = false) String friendIDStr, @ModelAttribute("userID") int myID, HttpServletResponse response) {
        if (friendIDStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int friendID;
        try {
            friendID = Integer.parseInt(friendIDStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Friend friend;
        if ((friend = friendDAO.selectOneFriend(myID, friendID)) == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        friendDAO.deleteFriend(friend.getUserFriendID());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
