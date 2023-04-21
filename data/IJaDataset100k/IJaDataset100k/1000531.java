package budgeteventplanner.client;

import java.util.List;
import budgeteventplanner.client.entity.Attendee;
import com.google.common.collect.SetMultimap;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("attendee")
public interface AttendeeService extends RemoteService {

    Attendee createAttendee(String eventId, String name, String email);

    List<Attendee> getAttendeeListByOrganizerId(String organizerId);

    List<Attendee> getAttendeeListByEventId(String eventId);

    void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle, String companyName, String address, String phoneNum, Integer status);

    Integer attendeeLogin(String registrationCode);

    Attendee getAttendeeByAttendeeId(String attendeeId);

    void removeAttendeeByAttendeeList(List<Attendee> attendeeList);

    void sendEmail(String attendeeId, Integer status);

    void sendEmailBatch(List<String> attendeeIdList, Integer status);

    void sendEmailBatchByOrganizer(List<String> attendeeIdList, Integer status);

    void sendCustomizedEmail(Attendee attendee, String subject, String msgBody);

    void sendCustomizedEmail(String attendeeId, String subject, String msgBody);

    List<Attendee> fillAttendeesInEvent(String eventId, List<Attendee> attendeeIdList);

    SetMultimap<String, Attendee> getSortedAttendeeList(String organizerId, String excludedEventId);

    SetMultimap<Integer, Attendee> getSortedEventAttendeeByStatus(String eventId);

    List<Attendee> getEventAttendeeByStatus(String eventId, Integer status);
}
