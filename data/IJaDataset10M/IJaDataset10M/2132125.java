package au.gov.qld.dnr.dss.event;

/**
 * The adapter which receives <i>alternative</i> events.
 * The methods in this class are empty;  this class is provided as a
 * convenience for easily creating listeners by extending this class
 * and overriding only the methods of interest.
 */
public class AlternativeAdapter implements AlternativeListener {

    public void changedDescription(AlternativeEvent e) {
    }

    public void changedShortDescription(AlternativeEvent e) {
    }

    public void changedComment(AlternativeEvent e) {
    }
}
