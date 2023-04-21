package se392.ateam2006.resourcemgmt;

import java.util.Calendar;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.Remote;
import java.util.Date;
import se392.ateam2006.exceptions.EquipmentFoundException;
import se392.ateam2006.meetingmgmt.meeting.MeetingEntity;
import se392.ateam2006.resourcemgmt.equipment.EquipmentEntity;
import se392.ateam2006.resourcemgmt.participant.ParticipationEntity;
import se392.ateam2006.resourcemgmt.participant.exclusionset.ExclusionRangeEntity;
import se392.ateam2006.resourcemgmt.room.RoomEntity;
import se392.ateam2006.user.UserEntity;

/**
 * Remote interface for the ResourceMgmtBean - allows access from a different JVM
 * @author Ateam (Matthew Bennett, Claire Melton, Shingai Manyiwa, John Adderley)
 * @version 25/03/07
 */
@Remote
public interface ResourceMgmtRemote {

    Collection getExclusionSetsForMeetingId(String meetingId);

    ;

    RoomEntity createRoom(String roomID, int capacity, String location, String owner);

    int getEquipmentCount();

    int getExclusionSetsCount();

    Collection getLocations();

    int getRoomCount();

    Collection getRoomsByLocation(String location);

    RoomEntity findRoomByID(String roomID);

    void participantResponse(ParticipationEntity part, Collection<ExclusionRangeEntity> coll);

    void removeAllDefaultStartTimes();

    void removeAllEquipment();

    void removeAllExclusionSets();

    void removeExclusionSetsByMeetingId(String meetingID);

    void removeAllRooms();

    Collection getAllRooms();

    void removePreAndExSet(ExclusionRangeEntity set);

    void setEquipmentOwner(String serialID, String owner, String roomId);

    void setParticipantEquipment(String userId, String meetingId, String[] equipment);

    void setRoomOwner(String roomId, String owner);

    ParticipationEntity createParticipation(MeetingEntity me, UserEntity ue) throws CreateException;

    ParticipationEntity createParticipation(MeetingEntity me, UserEntity ue, boolean isActive) throws CreateException;

    ParticipationEntity getParticipant(UserEntity ue, MeetingEntity me);

    void removeParticipation(ParticipationEntity pe) throws IllegalArgumentException;

    void setTestingMode(boolean areWeJustTesting);

    Collection getExclusionSetsForParticipantAndMeetings(UserEntity user, MeetingEntity meeting);

    ExclusionRangeEntity createExclusionRangeEntity(UserEntity ue, MeetingEntity me, Calendar start, Calendar end, boolean pref) throws CreateException;

    ExclusionRangeEntity createExclusionRangeEntity(UserEntity ue, MeetingEntity me, boolean pref) throws CreateException;

    Collection getExclusionSetsForParticipant(UserEntity user);

    ExclusionRangeEntity createExclusionRangeEntity(UserEntity user, Calendar start, Calendar end, boolean pref);

    void setTestDate(Date d);

    int getActiveParticipantCount(MeetingEntity meetingEntity);

    int getMeetingParticipantCount(MeetingEntity meetingEntity);

    Collection getMeetingParticipants(MeetingEntity meetingEntity);

    void removeAllParticipations();

    Collection findParticipationsByUser(UserEntity u);

    void deleteExclusionRange(ExclusionRangeEntity p);

    void deleteRoom(RoomEntity room);

    Collection getAllExclusionsAndPreferences();

    Collection getEquipmentByRoomId(RoomEntity theRoom);

    void setRoomEquipment(String serialNo, String type, String location, boolean status) throws EquipmentFoundException;

    EquipmentEntity findEquipmentBySerialID(String serialID, String roomId);

    EquipmentEntity findEquipmentBySerialID(String serialID);

    Collection<RoomEntity> getAllRoomsAllowed(UserEntity user);

    void createRoom(RoomEntity room);
}
