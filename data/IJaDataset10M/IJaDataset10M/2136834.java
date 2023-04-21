package fr.jussieu.gla.wasa.samples.party;

import fr.jussieu.gla.wasa.core.Constraint;
import fr.jussieu.gla.wasa.core.Problem;

/**
 * Expresses that a Crew should meet another Crew only once.
 *
 * @author Laurent Caillette
 * @version $Revision: 1.1 $ $Date: 2002/04/08 17:52:37 $
 */
public class MeetingOnlyOnceConstraint extends Constraint {

    private final ProgressivePartyProblem ppp;

    public MeetingOnlyOnceConstraint(ProgressivePartyProblem ppp) {
        super(ppp);
        this.ppp = ppp;
        setName("Meet1");
    }

    public void evaluate() {
        int guestCount = ppp.getSchedule().getGuestCount();
        int periodCount = ppp.getSchedule().getPeriodCount();
        for (int guestIndex1 = 0; guestIndex1 < guestCount; guestIndex1++) {
            Boat guest1 = ppp.getSchedule().getAllBoats()[guestIndex1];
            for (int guestIndex2 = guestIndex1 + 1; guestIndex2 < guestCount; guestIndex2++) {
                Boat guest2 = ppp.getSchedule().getAllBoats()[guestIndex2];
                int meetingCount = 0;
                int[] meetingIndexArray = new int[ppp.getSchedule().getPeriodCount()];
                for (int periodIndex = 0; periodIndex < periodCount; periodIndex++) {
                    Boat host1 = ppp.getSchedule().getHost(guest1, periodIndex);
                    Boat host2 = ppp.getSchedule().getHost(guest2, periodIndex);
                    if (host1 == host2) {
                        meetingIndexArray[meetingCount] = periodIndex;
                        meetingCount++;
                    }
                }
                if (meetingCount > 1) {
                    for (int metIndex = 0; metIndex < meetingCount; metIndex++) {
                        ppp.getScheduleVars()[guestIndex1][meetingIndexArray[metIndex]].incError(meetingCount);
                        ppp.getScheduleVars()[guestIndex2][meetingIndexArray[metIndex]].incError(meetingCount);
                    }
                }
            }
        }
    }
}
