package at.proximity.gps;

import ocss.nmea.api.NMEAClient;
import ocss.nmea.api.NMEAEvent;

public class CustomClient extends NMEAClient {

    public CustomClient(String s, String[] sa) {
        super(s, sa);
    }

    public void dataDetectedEvent(NMEAEvent e) {
        System.out.println("Received:" + e.getContent());
    }

    public static void main(String[] args) {
        String prefix = "GP";
        String[] array = { "GGA" };
        CustomClient customClient = new CustomClient(prefix, array);
        customClient.initClient();
        customClient.setReader(new CustomReader(customClient.getListeners()));
        customClient.startWorking();
    }
}
