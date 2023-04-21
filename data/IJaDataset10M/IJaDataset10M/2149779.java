package util;

import games.AbstractGameCallback;
import games.savedata.DataNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventTimer {

    private AbstractGameCallback<?> callback;

    private javax.swing.Timer timer;

    private int interval;

    private int remainingTime;

    private boolean repeat;

    private String event;

    private String[] eventArguments;

    private String identifier;

    public EventTimer(String identifier, AbstractGameCallback<?> callback, int interval, String event, String... eventArguments) {
        this(identifier, callback, interval, event, false, eventArguments);
    }

    public EventTimer(String identifier, AbstractGameCallback<?> callback, int interval, String event, boolean repeat, String... eventArguments) {
        this.identifier = identifier;
        this.event = event;
        this.callback = callback;
        this.interval = interval;
        this.remainingTime = interval;
        this.repeat = repeat;
        this.eventArguments = eventArguments;
        this.timer = new javax.swing.Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                EventTimer.this.tick();
            }
        });
        this.timer.setRepeats(true);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    private void tick() {
        this.remainingTime--;
        if (this.remainingTime == 0) {
            Object[] args = this.eventArguments;
            this.callback.fireEvent(this.event, args);
            if (this.repeat) this.remainingTime = this.interval; else this.timer.stop();
        }
    }

    public DataNode saveTo(DataNode parentNode) {
        DataNode timerNode = parentNode.createChild("timer");
        timerNode.setAttribute("identifier", this.identifier);
        timerNode.setAttribute("interval", this.interval + "");
        timerNode.setAttribute("repeat", this.repeat + "");
        timerNode.setAttribute("remainingTime", this.remainingTime + "");
        DataNode argumentsNode = timerNode.createChild("arguments");
        for (String argument : this.eventArguments) {
            DataNode argumentNode = argumentsNode.createChild("argument");
            argumentNode.setValue(argument);
        }
        return timerNode;
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }
}
