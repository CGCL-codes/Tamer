package com.tardytron.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tardytron.client.dto.EventDto;

public interface EventServiceAsync {

    void newEvent(int tzOffsetMinutes, int year, int month, int day, boolean twoDayReminder, boolean twoWeekReminder, String name, AsyncCallback<EventDto> callback);

    void getAllEvents(int tzOffsetMinutes, AsyncCallback<ArrayList<EventDto>> callback);

    void deleteEvent(long id, AsyncCallback<Void> callback);
}
