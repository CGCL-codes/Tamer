package com.tardytron.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tardytron.client.dto.EventDto;

@RemoteServiceRelativePath("events")
public interface EventService extends RemoteService {

    EventDto newEvent(int tzOffsetMinutes, int year, int month, int day, boolean twoDayReminder, boolean twoWeekReminder, String name);

    void deleteEvent(long id);

    ArrayList<EventDto> getAllEvents(int tzOffsetMinutes);
}
