package onepoint.project.modules.project;

/**
 * @author dfreis
 *
 */
public interface OpDependencyIfc {

    public abstract OpActivityIfc getPredecessorActivity();

    public abstract OpActivityIfc getSuccessorActivity();

    public abstract int getDependencyType();

    public abstract void setDependencyType(int type);

    public abstract boolean getAttribute(int key);
}
