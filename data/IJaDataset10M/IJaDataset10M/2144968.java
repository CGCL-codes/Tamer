package castor.test;

import java.io.FileWriter;
import org.exolab.castor.xml.Marshaller;

public class MarshalTester {

    public static void main(String[] args) {
        try {
            CD sessions = new CD("Sessions for Robert J", "Eric Clapton");
            sessions.addTrack("Little Queen of Spades");
            sessions.addTrack("Terraplane Blues");
            FileWriter writer = new FileWriter("cds.xml");
            Marshaller.marshal(sessions, writer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
