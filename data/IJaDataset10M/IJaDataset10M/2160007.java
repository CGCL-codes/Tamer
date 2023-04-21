package ru.nsu.ccfit.pm.econ.engine.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUGameTime;
import ru.nsu.ccfit.pm.econ.common.engine.events.IUGameEvent;
import ru.nsu.ccfit.pm.econ.engine.data.GameTimeEngine;

/**
 * {@link IUGameEvent} interface implementation for engine
 * 
 * @author pupatenko
 * 
 */
public class GameEventEngine implements IUGameEvent {

    private GameTimeEngine eventTime;

    private ArrayList<Long> recieverIds;

    private long senderId;

    public boolean broadcast;

    public GameEventEngine() {
        eventTime = new GameTimeEngine(new Date(0), 0, true);
        recieverIds = new ArrayList<Long>();
        senderId = -1;
        broadcast = false;
    }

    public GameEventEngine(IUGameTime eventTime, Collection<Long> recieverIds, long senderId, boolean broadcast) {
        this.eventTime = new GameTimeEngine(eventTime);
        this.recieverIds = new ArrayList<Long>(recieverIds);
        this.senderId = senderId;
        this.broadcast = broadcast;
    }

    public void clearRecievers() {
        recieverIds.clear();
    }

    public void addReciever(long id) {
        recieverIds.add(id);
    }

    public int recieversNum() {
        return recieverIds.size();
    }

    public void addRecievers(Collection<Long> ids) {
        recieverIds.addAll(ids);
    }

    public GameEventEngine(IUGameEvent other) {
        eventTime = new GameTimeEngine(other.getEventTime());
        recieverIds = new ArrayList<Long>(other.getReceiverIds());
        senderId = other.getSenderId();
        broadcast = other.isBroadcast();
    }

    @Override
    public IUGameTime getEventTime() {
        return eventTime;
    }

    @Override
    public Collection<Long> getReceiverIds() {
        return Collections.unmodifiableCollection(recieverIds);
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean val) {
        broadcast = val;
    }
}
