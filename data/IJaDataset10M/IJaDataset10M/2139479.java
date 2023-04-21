package com.brrus.icbinabr.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.brrus.icbinabr.domain.*;

public interface EventService {

    @Transactional
    Event createEvent(Event event);

    @Transactional
    Position createPosition(Event event, Position position);

    @Transactional
    TimeSlot createTimeSlot(Position position, TimeSlot timeSlot);

    @Transactional
    Event updateEvent(Event event);

    @Transactional
    Position updatePosition(Position position);

    @Transactional
    TimeSlot updateTimeSlot(TimeSlot timeSlot);

    @Transactional
    void deleteEvent(Event event);

    @Transactional
    void deletePosition(Position position);

    @Transactional
    void deleteTimeSlot(TimeSlot timeSlot);

    Event getEventById(Long id);

    Position getPositionById(Long id);

    TimeSlot getTimeSlotById(Long id);

    List<Event> getFutureEvents();

    List<Event> getEvents();

    List<Position> getPositions();

    List<TimeSlot> getTimeSlots();

    List<Event> getCurrentEvents();

    List<Position> getPositions(Event event);

    List<Position> getFilledPositions(Event event);

    List<Position> getOpenPositions(Event event);

    List<TimeSlot> getTimeSlots(Event event);

    List<TimeSlot> getOpenTimeSlots(Event event);

    List<TimeSlot> getFilledTimeSlots(Event event);

    List<Event> getEventsCoordinated(UserAccount userAccount);

    UserAccount getCoordinatorByEvent(Event event);
}
