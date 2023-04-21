package ibus.Devices;

import ibus.BaseClasses.*;
import Control.EventListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author brelandmiley
 */
public class CDEmulator extends TimerTask implements EventListener {

    private Device id;

    private boolean radioPolledYet;

    private MessageConstructor mc;

    private Message announce;

    private Message responseToPoll;

    private Message radioPoll;

    private Message playingMessage;

    private Timer announceTimer;

    private Message AMPEmulate;

    public CDEmulator() {
        super();
        id = new Device("18");
        this.announceTimer = new Timer();
        this.radioPoll = new Message(new Device("68"), id, new MData("01"));
        this.responseToPoll = new Message(id, new Device("FF"), new MData("02 00"));
        this.announce = new Message(id, new Device("FF"), new MData("02 01"));
        this.playingMessage = new Message(id, new Device("FF"), new MData("39 02 09 00 3F 00 01 00"));
        this.AMPEmulate = new Message(new Device("C0"), new Device("68"), new MData("01 AA"));
        this.mc = new MessageConstructor();
        this.radioPolledYet = false;
        this.announceTimer.scheduleAtFixedRate(this, 0, 10000);
    }

    @Override
    public void run() {
        this.announce.send();
        this.responseToPoll.send();
        this.AMPEmulate.send();
    }

    public void update(String eventName, Object source, Object data) {
        Message full = (Message) source;
        if (full.equals(radioPoll)) {
            this.radioPolledYet = true;
            this.responseToPoll.send();
            this.AMPEmulate.send();
        }
    }
}
