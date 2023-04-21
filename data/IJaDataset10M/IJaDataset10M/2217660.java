package fr.gedeon.telnetservice.mbean.jboss.syntaxtree;

import javax.management.MBeanServer;
import org.jboss.mx.util.MBeanServerLocator;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityName;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityNamePartType;
import fr.gedeon.telnetservice.syntaxtree.ast.Node;
import fr.gedeon.telnetservice.syntaxtree.ast.SyntaxTree;
import fr.gedeon.telnetservice.syntaxtree.ast.Transition;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.EntityNameImpl;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.NodeImpl;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.TransitionImpl;

public class JmxNodeImpl extends NodeImpl implements Node {

    SyntaxTree syntaxTree;

    public JmxNodeImpl(SyntaxTree syntaxTree) {
        super(new EntityNameImpl(EntityName.ROOT, "jmx", EntityNamePartType.CONTEXT));
        this.syntaxTree = syntaxTree;
        initializeOutTransitions();
    }

    private void initializeOutTransitions() {
        MBeanServer server = MBeanServerLocator.locateJBoss();
        if (server != null) {
            for (String domainName : server.getDomains()) {
                DomainNodeImpl domainNode = new DomainNodeImpl(this.syntaxTree, this.getName(), domainName);
                this.syntaxTree.registerNode(domainNode);
                Transition domainTr = new TransitionImpl(domainName, EntityNamePartType.CONTEXT, domainNode.getName());
                addOutTransition(domainTr);
            }
        }
    }
}
