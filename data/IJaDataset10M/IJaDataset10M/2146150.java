package org.openmeetings.axis.services;

public interface RoomServicePortType extends java.rmi.Remote {

    public java.lang.Long deleteRoom(java.lang.String SID, java.lang.Long rooms_id) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.RoomReturn getRoomWithClientObjectsById(java.lang.String SID, java.lang.Long rooms_id) throws java.rmi.RemoteException;

    public java.lang.Long updateRoomWithModerationAndQuestions(java.lang.String SID, java.lang.Long room_id, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.Boolean allowUserQuestions) throws java.rmi.RemoteException;

    public org.openmeetings.app.data.beans.basic.xsd.SearchResult getRoomsWithCurrentUsers(java.lang.String SID, java.lang.Integer start, java.lang.Integer max, java.lang.String orderby, java.lang.Boolean asc) throws java.rmi.RemoteException;

    public java.lang.Boolean deleteFlvRecording(java.lang.String SID, java.lang.Long flvRecordingId) throws java.rmi.RemoteException;

    public java.lang.String getInvitationHash(java.lang.String SID, java.lang.String username, java.lang.Long room_id, java.lang.Boolean isPasswordProtected, java.lang.String invitationpass, java.lang.Integer valid, java.lang.String validFromDate, java.lang.String validFromTime, java.lang.String validToDate, java.lang.String validToTime) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationAndQuestions(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.Boolean allowUserQuestions) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.RoomReturn[] getRoomsWithCurrentUsersByList(java.lang.String SID, java.lang.Integer start, java.lang.Integer max, java.lang.String orderby, java.lang.Boolean asc) throws java.rmi.RemoteException;

    public java.lang.Long addExternalMeetingMemberRemindToRoom(java.lang.String SID, java.lang.Long room_id, java.lang.String firstname, java.lang.String lastname, java.lang.String email, java.lang.String baseUrl, java.lang.Long language_id, java.lang.String jNameTimeZone, java.lang.String invitorName) throws java.rmi.RemoteException;

    public java.lang.Integer closeRoom(java.lang.String SID, java.lang.Long room_id, java.lang.Boolean status) throws java.rmi.RemoteException;

    public java.lang.Long addMeetingMemberRemindToRoom(java.lang.String SID, java.lang.Long room_id, java.lang.String firstname, java.lang.String lastname, java.lang.String email, java.lang.String baseUrl, java.lang.Long language_id) throws java.rmi.RemoteException;

    public java.lang.String sendInvitationHashWithDateObject(java.lang.String SID, java.lang.String username, java.lang.String message, java.lang.String baseurl, java.lang.String email, java.lang.String subject, java.lang.Long room_id, java.lang.String conferencedomain, java.lang.Boolean isPasswordProtected, java.lang.String invitationpass, java.lang.Integer valid, java.util.Date fromDate, java.util.Date toDate, java.lang.Long language_id, java.lang.Boolean sendMail) throws java.rmi.RemoteException;

    public org.openmeetings.app.data.beans.basic.xsd.SearchResult getRooms(java.lang.String SID, java.lang.Integer start, java.lang.Integer max, java.lang.String orderby, java.lang.Boolean asc) throws java.rmi.RemoteException;

    public java.lang.String sendInvitationHash(java.lang.String SID, java.lang.String username, java.lang.String message, java.lang.String baseurl, java.lang.String email, java.lang.String subject, java.lang.Long room_id, java.lang.String conferencedomain, java.lang.Boolean isPasswordProtected, java.lang.String invitationpass, java.lang.Integer valid, java.lang.String validFromDate, java.lang.String validFromTime, java.lang.String validToDate, java.lang.String validToTime, java.lang.Long language_id, java.lang.Boolean sendMail) throws java.rmi.RemoteException;

    public java.lang.Long addRoom(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Integer videoPodWidth, java.lang.Integer videoPodHeight, java.lang.Integer videoPodXPosition, java.lang.Integer videoPodYPosition, java.lang.Integer moderationPanelXPosition, java.lang.Boolean showWhiteBoard, java.lang.Integer whiteBoardPanelXPosition, java.lang.Integer whiteBoardPanelYPosition, java.lang.Integer whiteBoardPanelHeight, java.lang.Integer whiteBoardPanelWidth, java.lang.Boolean showFilesPanel, java.lang.Integer filesPanelXPosition, java.lang.Integer filesPanelYPosition, java.lang.Integer filesPanelHeight, java.lang.Integer filesPanelWidth) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationQuestionsAndAudioType(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.Boolean allowUserQuestions, java.lang.Boolean isAudioOnly) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.RoomReturn[] getRoomsWithCurrentUsersByListAndType(java.lang.String SID, java.lang.Integer start, java.lang.Integer max, java.lang.String orderby, java.lang.Boolean asc, java.lang.String externalRoomType) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.FLVRecordingReturn[] getFlvRecordingByExternalRoomTypeAndCreator(java.lang.String SID, java.lang.String externalRoomType, java.lang.Long insertedBy) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationExternalTypeAndTopBarOption(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType, java.lang.Boolean allowUserQuestions, java.lang.Boolean isAudioOnly, java.lang.Boolean waitForRecording, java.lang.Boolean allowRecording, java.lang.Boolean hideTopBar) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationAndExternalTypeAndStartEnd(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType, java.lang.String validFromDate, java.lang.String validFromTime, java.lang.String validToDate, java.lang.String validToTime, java.lang.Boolean isPasswordProtected, java.lang.String password, java.lang.Long reminderTypeId, java.lang.String redirectURL) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.flvrecord.xsd.FlvRecording[] getFlvRecordingByRoomId(java.lang.String SID, java.lang.Long roomId) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.FLVRecordingReturn[] getFlvRecordingByExternalUserId(java.lang.String SID, java.lang.String externalUserId) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.rooms.xsd.Rooms getRoomWithCurrentUsersById(java.lang.String SID, java.lang.Long rooms_id) throws java.rmi.RemoteException;

    public java.lang.Long updateRoom(java.lang.String SID, java.lang.Long rooms_id, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Integer videoPodWidth, java.lang.Integer videoPodHeight, java.lang.Integer videoPodXPosition, java.lang.Integer videoPodYPosition, java.lang.Integer moderationPanelXPosition, java.lang.Boolean showWhiteBoard, java.lang.Integer whiteBoardPanelXPosition, java.lang.Integer whiteBoardPanelYPosition, java.lang.Integer whiteBoardPanelHeight, java.lang.Integer whiteBoardPanelWidth, java.lang.Boolean showFilesPanel, java.lang.Integer filesPanelXPosition, java.lang.Integer filesPanelYPosition, java.lang.Integer filesPanelHeight, java.lang.Integer filesPanelWidth, java.lang.Boolean appointment) throws java.rmi.RemoteException;

    public java.lang.Long getRoomIdByExternalId(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.Long externalRoomId, java.lang.String externalRoomType) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModeration(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.rooms.xsd.Rooms getRoomById(java.lang.String SID, java.lang.Long rooms_id) throws java.rmi.RemoteException;

    public java.lang.Boolean kickUser(java.lang.String SID_Admin, java.lang.Long room_id) throws java.rmi.RemoteException;

    public org.openmeetings.axis.services.xsd.RoomCountBean[] getRoomCounters(java.lang.String SID, java.lang.Integer roomId1, java.lang.Integer roomId2, java.lang.Integer roomId3, java.lang.Integer roomId4, java.lang.Integer roomId5, java.lang.Integer roomId6, java.lang.Integer roomId7, java.lang.Integer roomId8, java.lang.Integer roomId9, java.lang.Integer roomId10) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationAndRecordingFlags(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType, java.lang.Boolean allowUserQuestions, java.lang.Boolean isAudioOnly, java.lang.Boolean waitForRecording, java.lang.Boolean allowRecording) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationAndExternalType(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.rooms.xsd.RoomTypes[] getRoomTypes(java.lang.String SID) throws java.rmi.RemoteException;

    public java.lang.Long updateRoomWithModeration(java.lang.String SID, java.lang.Long room_id, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.rooms.xsd.Rooms[] getRoomsPublic(java.lang.String SID, java.lang.Long roomtypes_id) throws java.rmi.RemoteException;

    public java.lang.Long addRoomWithModerationExternalTypeAndAudioType(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType, java.lang.Boolean allowUserQuestions, java.lang.Boolean isAudioOnly) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.flvrecord.xsd.FlvRecording[] getFlvRecordingByExternalRoomType(java.lang.String SID, java.lang.String externalRoomType) throws java.rmi.RemoteException;

    public org.openmeetings.app.persistence.beans.flvrecord.xsd.FlvRecording[] getFlvRecordingByExternalRoomTypeByList(java.lang.String SID, java.lang.String externalRoomType) throws java.rmi.RemoteException;
}
