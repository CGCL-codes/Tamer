package hu.sztaki.lpds.pgportal.services.timer;

/**
 * @author lpds
 */
public class EventTimerTest {

    public EventTimerTest(String configFileName) throws EventTimerException {
        EventTimer eventTimer = new EventTimer(configFileName);
        eventTimer.startEvents();
        try {
            Thread.sleep(3300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventTimer.stopEvents();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            EventTimerTest test = new EventTimerTest("/home/schandras/1/EventTimer.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
