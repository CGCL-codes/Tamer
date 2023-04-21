package org.myrobotlab.test;

import org.apache.log4j.Level;
import org.myrobotlab.framework.Inbox;
import org.myrobotlab.framework.Outbox;
import org.myrobotlab.framework.Service;
import org.myrobotlab.service.Speech;

public class SpeechTest {

    public static void main(String[] args) throws InterruptedException {
        Outbox.LOG.setLevel(Level.ERROR);
        Inbox.LOG.setLevel(Level.ERROR);
        Service.LOG.setLevel(Level.ERROR);
        Speech speech = new Speech("speech01");
        speech.speak("Hello Henry, are you going to make ice cream?");
    }
}
