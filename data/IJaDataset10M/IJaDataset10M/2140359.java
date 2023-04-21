package scheduler.fixtures;

/**
 * Convenience class for storing a User entity built of native datatypes.
 * @author Caroline
 * @version 25/03/07
 */
public class ExclusionRangeEntityParticipantShell {

    public String participant;

    public String startDateAndTime;

    public String endDateAndTime;

    public ExclusionRangeEntityParticipantShell(String participant, String startDateAndTime, String endDateAndTime) {
        this.participant = participant;
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
    }
}
