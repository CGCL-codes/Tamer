package unbbayes.prs.prm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import unbbayes.prs.prm.cpt.IAggregateFunction;

/**
 * @author Shou Matsumoto
 *
 */
public class DependencyChain implements IDependencyChain {

    private List<IForeignKey> foreignKeys;

    private IAggregateFunction aggregateFunction;

    private IPRMDependency dependencyTo;

    private IPRMDependency dependencyFrom;

    private Map<IForeignKey, Boolean> inverseFKMap = new HashMap<IForeignKey, Boolean>();

    protected DependencyChain() {
        this.foreignKeys = new ArrayList<IForeignKey>();
    }

    public static DependencyChain newInstance() {
        DependencyChain ret = new DependencyChain();
        return ret;
    }

    public IAggregateFunction getAggregateFunction() {
        return this.aggregateFunction;
    }

    public IPRMDependency getDependencyFrom() {
        return this.dependencyFrom;
    }

    public IPRMDependency getDependencyTo() {
        return this.dependencyTo;
    }

    public List<IForeignKey> getForeignKeyChain() {
        return this.foreignKeys;
    }

    public void setAggregateFunction(IAggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    public void setDependencyFrom(IPRMDependency from) {
        this.dependencyFrom = from;
    }

    public void setDependencyTo(IPRMDependency to) {
        this.dependencyTo = to;
    }

    public void setForeignKeyChain(List<IForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public boolean isInverseForeignKey(IForeignKey fk) {
        if (this.getForeignKeyChain().contains(fk)) {
            Boolean ret = this.getInverseFKMap().get(fk);
            return (ret != null) ? ret : false;
        }
        throw new IllegalArgumentException("The dependency chain \"" + this + "\" does not contain FK \"" + fk + "\" in its foreign key chain. Please, check dependency and FF consistency.");
    }

    public void markAsInverseForeignKey(IForeignKey fk, boolean isInverse) {
        if (this.getForeignKeyChain().contains(fk)) {
            this.getInverseFKMap().put(fk, isInverse);
        }
    }

    /**
	 * @return the inverseFKMap
	 */
    public Map<IForeignKey, Boolean> getInverseFKMap() {
        return inverseFKMap;
    }

    /**
	 * @param inverseFKMap the inverseFKMap to set
	 */
    public void setInverseFKMap(Map<IForeignKey, Boolean> inverseFKMap) {
        this.inverseFKMap = inverseFKMap;
    }
}
