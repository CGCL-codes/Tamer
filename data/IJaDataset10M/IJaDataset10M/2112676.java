package jp.go.aist.six.oval.model.unix;

import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.definitions.EntityStateBoolType;
import jp.go.aist.six.oval.model.definitions.EntityStateStringType;
import jp.go.aist.six.oval.model.definitions.StateType;

/**
 * The runlevel state holds information about
 * whether a specific service is scheduled to start or stop at a given runlevel.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: RunlevelState.java 2279 2012-04-04 01:15:16Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class RunlevelState extends StateType {

    private EntityStateStringType service_name;

    private EntityStateStringType runlevel;

    private EntityStateBoolType start;

    private EntityStateBoolType kill;

    /**
     * Constructor.
     */
    public RunlevelState() {
        this(null, 0);
    }

    public RunlevelState(final String id, final int version) {
        this(id, version, null);
    }

    public RunlevelState(final String id, final int version, final String comment) {
        super(id, version, comment);
        _oval_family = Family.UNIX;
        _oval_component = Component.RUNLEVEL;
    }

    /**
     */
    public void setServiceName(final EntityStateStringType service_name) {
        this.service_name = service_name;
    }

    public EntityStateStringType getServiceName() {
        return service_name;
    }

    /**
     */
    public void setRunlevel(final EntityStateStringType runlevel) {
        this.runlevel = runlevel;
    }

    public EntityStateStringType getRunlevel() {
        return runlevel;
    }

    /**
     */
    public void setStart(final EntityStateBoolType start) {
        this.start = start;
    }

    public EntityStateBoolType getStart() {
        return start;
    }

    /**
     */
    public void setKill(final EntityStateBoolType kill) {
        this.kill = kill;
    }

    public EntityStateBoolType getKill() {
        return kill;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RunlevelState)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "runlevel_state[" + super.toString() + ", service_name=" + getServiceName() + ", runlevel=" + getRunlevel() + ", start=" + getStart() + ", kill=" + getKill() + "]";
    }
}
