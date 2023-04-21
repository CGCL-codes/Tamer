package ces.coffice.meetmanage.vo;

import ces.coffice.common.base.BaseVo;

public class RoomAssign extends BaseVo {

    private java.sql.Timestamp beginTime;

    private java.sql.Timestamp endTime;

    /**
     * @clientCardinality 0..*
     * @supplierCardinality 1 
     */
    private Rooms lnkAttribute1;

    private long roomId;

    private String roomName;

    private String meetName;

    private long meetId;

    private String useBeginTime;

    private String useEndTime;

    public RoomAssign() {
    }

    public RoomAssign(long roomId) {
        this.roomId = roomId;
    }

    /**
	 * @return
	 */
    public long getMeetId() {
        return this.meetId;
    }

    /**
	 * @return
	 */
    public String getMeetName() {
        return this.meetName;
    }

    /**
	 * @return
	 */
    public long getRoomId() {
        return this.roomId;
    }

    /**
	 * @return
	 */
    public String getRoomName() {
        return this.roomName;
    }

    /**
	 * @return
	 */
    public String getUseBeginTime() {
        return this.useBeginTime;
    }

    /**
	 * @return
	 */
    public String getUseEndTime() {
        return this.useEndTime;
    }

    /**
	 * @param i
	 */
    public void setMeetId(long meetId) {
        this.meetId = meetId;
    }

    /**
	 * @param string
	 */
    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    /**
	 * @param i
	 */
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    /**
	 * @param string
	 */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
	 * @param string
	 */
    public void setUseBeginTime(String useBeginTime) {
        this.useBeginTime = useBeginTime;
    }

    /**
	 * @param string
	 */
    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }
}
