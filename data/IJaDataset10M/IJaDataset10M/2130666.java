package pl.ehotelik.portal.report.planner;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Sep 7, 2010
 * Time: 9:28:05 PM
 * This is a representation of RPEmployee object.
 */
public class RPEmployee extends RPObject {

    public RPEmployee(long id) {
        super(id);
    }

    public RPEmployee(long id, String label) {
        super(id);
        super.setLabel(label);
    }
}
