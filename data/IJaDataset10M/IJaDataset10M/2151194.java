package remote.ejb.visitors;

import remote.entities.nodes.AgentEntity;
import remote.entities.nodes.L2NodeEntity;
import remote.entities.nodes.RouterEntity;
import remote.entities.nodes.SubnetEntity;

/**
 * @author Kasza Miklós
 */
public interface EntityVisitor {

    public void visit(AgentEntity agentEntity);

    public void visit(L2NodeEntity l2NodeEntity);

    public void visit(RouterEntity routerEntity);

    public void visit(SubnetEntity subnetEntity);
}
