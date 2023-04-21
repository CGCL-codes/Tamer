package com.fantosoft.admin.meetingroom;

import java.util.ArrayList;
import java.util.List;
import com.fantosoft.admin.AdminException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MeetingRoom implements IMeetingRoom {

    private Long meetingRoomId;

    private String roomName = "";

    private int roomContainNum = 0;

    private String roomDes = "";

    private List reserveRules = new ArrayList();

    private List reserveList = new ArrayList();

    public MeetingRoom() {
    }

    public MeetingRoom(List reserveList) {
        this.reserveList = reserveList;
    }

    public void reserve(ReserveInfo reserveInfo) throws AdminException {
        if (checkRule(reserveInfo)) {
            reserveList.add(reserveInfo);
        }
    }

    public void addReserveRules(IReserveRule reserveRule) {
        reserveRules.add(reserveRule);
    }

    public void delReserveRules(IReserveRule reserveRule) {
        int i = reserveRules.indexOf(reserveRule);
        if (i >= 0) {
            reserveRules.remove(reserveRule);
        }
    }

    public List getReserveRules() {
        return this.reserveRules;
    }

    /**
     * ����Ƿ��ܹ��������е�Լ������
     * @param reserveInfo
     * @return
     */
    public boolean checkRule(IReserveInfo reserveInfo) throws AdminException {
        boolean result = true;
        for (int i = 0; i < reserveRules.size(); i++) {
            IReserveRule reserveRule = (IReserveRule) reserveRules.get(i);
            result = reserveRule.isValid(this, reserveInfo);
            if (result == false) return false;
        }
        return result;
    }

    public void delete(ReserveInfo reserveInfo) {
        int reserveIndex = reserveList.indexOf(reserveInfo);
        if (reserveIndex >= 0) reserveList.remove(reserveIndex); else {
        }
    }

    public List getReserveList() {
        return this.reserveList;
    }

    /**
     * @return Returns the roomContainNum.
     */
    public int getRoomContainNum() {
        return roomContainNum;
    }

    /**
     * @param roomContainNum The roomContainNum to set.
     */
    public void setRoomContainNum(int roomContainNum) {
        this.roomContainNum = roomContainNum;
    }

    /**
     * @return Returns the roomDes.
     */
    public String getRoomDes() {
        return roomDes;
    }

    /**
     * @param roomDes The roomDes to set.
     */
    public void setRoomDes(String roomDes) {
        this.roomDes = roomDes;
    }

    /**
     * @return Returns the roomName.
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName The roomName to set.
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * @return Returns the meetingRoomId.
     */
    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    /**
     * @param meetingRoomId The meetingRoomId to set.
     */
    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }
}
