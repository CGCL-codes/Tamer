package nl.utwente.ewi.hmi.deira.queue;

import java.util.ArrayList;
import nl.utwente.ewi.hmi.deira.iam.riam.Participant;

public class RaceEventTest {

    /**
	 * Run to test class RaceEvent.
	 * 
	 * @param args
	 *            N/A
	 */
    public static void main(String[] args) {
        Event e1;
        Event e2;
        Participant p1 = new Participant("Azure");
        Participant p2 = new Participant("Eben");
        ArrayList<Participant> ps1 = new ArrayList<Participant>();
        ArrayList<Participant> ps2 = new ArrayList<Participant>();
        ArrayList<Participant> ps3 = new ArrayList<Participant>();
        ps1.add(p1);
        ps2.add(p1);
        ps2.add(p2);
        ps3.add(p2);
        e1 = new HREvent(1, 0, "A", "1", null, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "B", "1", null, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1a: FAILED"); else System.out.println("Test1a: PASSED");
        e1 = new HREvent(1, 0, "A", "1", null, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "A", "2", null, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1b: FAILED"); else System.out.println("Test1b: PASSED");
        e1 = new HREvent(1, 0, "A", "1", null, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "A", "1", null, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1c: PASSED"); else System.out.println("Test1c: FAILED. e1 = " + e1 + "; e2 = " + e2);
        e1 = new HREvent(1, 0, "A", "1", ps1, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "A", "1", ps1, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1d: PASSED"); else System.out.println("Test1d: FAILED. e1 = " + e1 + "; e2 = " + e2);
        e1 = new HREvent(1, 0, "A", "1", ps1, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "A", "1", ps2, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1e: FAILED"); else System.out.println("Test1e: PASSED");
        e1 = new HREvent(1, 0, "A", "1", ps1, 5.0f, 0, null);
        e2 = new HREvent(1, 0, "A", "1", ps3, 5.0f, 0, null);
        if (e1.isSimilar(e2)) System.out.println("Test1f: FAILED"); else System.out.println("Test1f: PASSED");
    }
}
