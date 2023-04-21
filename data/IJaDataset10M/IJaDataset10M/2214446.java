package ds.graph.problem;

import ds.graph.Edge;
import ds.mapping.IdentifiableIntegerMapping;
import ds.graph.network.AbstractNetwork;
import ds.graph.Node;

/**
 *
 * @author Martin Groß
 */
public class MinimumCostFlowProblem {

    private AbstractNetwork network;

    private IdentifiableIntegerMapping<Node> balances;

    private IdentifiableIntegerMapping<Edge> capacities;

    private IdentifiableIntegerMapping<Edge> costs;

    public MinimumCostFlowProblem(AbstractNetwork network, IdentifiableIntegerMapping<Edge> capacities, IdentifiableIntegerMapping<Edge> costs, IdentifiableIntegerMapping<Node> balances) {
        this.network = network;
        this.balances = balances;
        this.capacities = capacities;
        this.costs = costs;
    }

    public IdentifiableIntegerMapping<Node> getBalances() {
        return balances;
    }

    public void setBalances(IdentifiableIntegerMapping<Node> balances) {
        this.balances = balances;
    }

    public IdentifiableIntegerMapping<Edge> getCapacities() {
        return capacities;
    }

    public void setCapacities(IdentifiableIntegerMapping<Edge> capacities) {
        this.capacities = capacities;
    }

    public IdentifiableIntegerMapping<Edge> getCosts() {
        return costs;
    }

    public void setCosts(IdentifiableIntegerMapping<Edge> costs) {
        this.costs = costs;
    }

    public AbstractNetwork getNetwork() {
        return network;
    }

    public void setNetwork(AbstractNetwork network) {
        this.network = network;
    }

    @Override
    public String toString() {
        return "MinimumCostFlowProblem{" + "network=" + network + ", balances=" + balances + ", capacities=" + capacities + ", costs=" + costs + '}';
    }
}
