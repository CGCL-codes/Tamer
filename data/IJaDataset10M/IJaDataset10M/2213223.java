package org.ddth.txbb.board.dblayer;

import java.sql.SQLException;
import java.util.List;
import org.ddth.daf.Group;
import org.ddth.txbb.board.bo.Box;
import org.ddth.txbb.board.bo.Post;
import org.ddth.txbb.board.bo.Topic;
import org.ddth.txbb.board.bo.Zone;

public interface IDBLayerZoneBox extends org.ddth.txbb.base.dblayer.IDBLayerZoneBox {

    /**
	 * Creates a new box and returns its newly generated box id.
	 * @param isHidden boolean
	 * @param parentBoxId int
	 * @param type int
	 * @param linkedURL String
	 * @param title String
	 * @param outerDesc String
	 * @param innerDesc String
	 * @return int
	 * @throws SQLException
	 */
    public int createBox(boolean isHidden, int parentBoxId, int type, String linkedURL, String title, String outerDesc, String innerDesc) throws SQLException;

    /**
	 * Deletes a box.
	 * @param boxId int
	 * @throws SQLException
	 */
    public void deleteBox(int boxId) throws SQLException;

    /**
	 * Deletes a box.
	 * @param box Box
	 * @throws SQLException
	 */
    public void deleteBox(Box box) throws SQLException;

    /**
	 * Updates box's number of topics, posts and last post information.
	 * @param box Box
	 * @throws SQLException
	 */
    public void resyncBox(Box box) throws SQLException;

    /**
	 * Updates box's number of topics, posts and last post information.
	 * @param boxId int
	 * @throws SQLException
	 */
    public void resyncBox(int boxId) throws SQLException;

    /**
	 * Updates all boxes' number of topics, posts and last post information.
	 * @throws SQLException
	 */
    public void resyncAllBoxes() throws SQLException;

    /**
	 * Sets box's permissions.
	 * @param boxId int
	 * @param viewTopicGroups List<Group> list of Id of groups that has
	 * permisson to view topics.
	 * @param startTopicGroups List<Group> list of Id of groups that has
	 * permisson to start new topics.
	 * @param replyTopicGroups List<Group> list of Id of groups that has
	 * permisson to reply to existing topics.
	 * @param attachFileGroups List<Group> list of Id of groups that has
	 * permisson to attach files.
	 * @param makePollGroups List<Group> list of Id of groups that has
	 * permisson to create polls.
	 * @throws SQLException
	 */
    public void setBoxPermissions(int boxId, List<Group> viewTopicGroups, List<Group> startTopicGroups, List<Group> replyTopicGroups, List<Group> attachFileGroups, List<Group> makePollTopicGroups) throws SQLException;

    /**
	 * Sets box's permissions.
	 * @param box Box
	 * @param viewTopicGroups List<Group> list of Id of groups that has
	 * permisson to view topics.
	 * @param startTopicGroups List<Group> list of Id of groups that has
	 * permisson to start new topics.
	 * @param replyTopicGroups List<Group> list of Id of groups that has
	 * permisson to reply to existing topics.
	 * @param attachFileGroups List<Group> list of Id of groups that has
	 * permisson to attach files.
	 * @param makePollGroups List<Group> list of Id of groups that has
	 * permisson to create polls.
	 * @throws SQLException
	 */
    public void setBoxPermissions(Box box, List<Group> viewTopicGroups, List<Group> startTopicGroups, List<Group> replyTopicGroups, List<Group> attachFileGroups, List<Group> makePollTopicGroups) throws SQLException;

    /**
	 * Updates box data.
	 * @param box Box
	 * @throws SQLException
	 */
    public void updateBox(Box box) throws SQLException;

    /**
	 * Update box's stats data
	 * @param box Box
	 * @param lastPost Box
	 * @throws SQLException
	 */
    public void updateBoxStats(Box box, Post lastPost) throws SQLException;

    /**
	 * Update box's stats data
	 * @param box Box
	 * @param lastTopic Topic
	 * @throws SQLException
	 */
    public void updateBoxStats(Box box, Topic lastTopic) throws SQLException;

    /**
	 * Adds a box to a zone
	 * @param zoneId int
	 * @param boxId int
	 * @throws SQLException
	 */
    public void addBoxToZone(int zoneId, int boxId) throws SQLException;

    /**
	 * Adds a box to a zone
	 * @param zone Zone
	 * @param box Box
	 * @throws SQLException
	 */
    public void addBoxToZone(Zone zone, Box box) throws SQLException;

    /**
	 * Creates a new zone.
	 * @param zoneTitle String
	 * @param zoneDesc String
	 * @param visible boolean
	 * @param boxes List<Number> IDs of boxes that belong to this zone.
	 * @return int ID of the newly created zone.
	 * @throws SQLException
	 */
    public int createZone(String zoneTitle, String zoneDesc, boolean visible, List<Number> boxes) throws SQLException;

    /**
	 * Deletes a zone.
	 * @param zoneId int Id of the zone to be deleted.
	 * @throws SQLException
	 */
    public void deleteZone(int zoneId) throws SQLException;

    /**
	 * Deletes a zone.
	 * @param zone Zone
	 * @throws SQLException
	 */
    public void deleteZone(Zone zone) throws SQLException;

    /**
	 * Reordering zones: moves a zone down
	 * @param zone Zone
	 * @throws SQLException
	 */
    public void moveZoneDown(Zone zone) throws SQLException;

    /**
	 * Reordering zones: moves a zone up
	 * @param zone Zone
	 * @throws SQLException
	 */
    public void moveZoneUp(Zone zone) throws SQLException;

    /**
	 * Reordering zones: moves a zone down
	 * @param zoneId int
	 * @throws SQLException
	 */
    public void moveZoneDown(int zoneId) throws SQLException;

    /**
	 * Reordering zones: moves a zone up
	 * @param zoneId int
	 * @throws SQLException
	 */
    public void moveZoneUp(int zoneId) throws SQLException;

    /**
	 * Updates an existing zone.
	 * @param zone Zone
	 * @throws SQLException
	 */
    public void updateZone(Zone zone) throws SQLException;
}
