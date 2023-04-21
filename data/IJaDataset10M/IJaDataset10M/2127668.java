package org.objectweb.fractal.fractalizer;

import java.util.Set;
import org.objectweb.fractal.fractalizer.graph.BindingNode;
import org.objectweb.fractal.fractalizer.graph.ComponentGraph;
import org.objectweb.fractal.fractalizer.graph.InterfaceNode;
import org.objectweb.fractal.fractalizer.graph.PrimitiveComponentNode;

/**
 * Default implementation of {@link ADLWriterGraphVisitor}.
 * 
 * @author <a href="mailto:valerio.schiavoni@gmail.com">Valerio Schiavoni</a>
 */
public class ADLWriterGraphVisitorImpl implements ADLWriterGraphVisitor {

    private static final String STANDARD_DTD_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" + "<!DOCTYPE definition PUBLIC \"-//objectweb.org//DTD Fractal ADL 2.0//EN\" \"classpath://org/objectweb/fractal/adl/xml/standard.dtd\">\n";

    /**
   * The string builder.
   */
    private final StringBuilder builder;

    /**
   * The name of the composite component
   */
    String rootDefinitionName;

    public ADLWriterGraphVisitorImpl() {
        this("default-name");
    }

    public ADLWriterGraphVisitorImpl(final String rootDefinitionName) {
        this.rootDefinitionName = rootDefinitionName;
        this.builder = new StringBuilder(STANDARD_DTD_HEADER);
    }

    /**
   * @see org.objectweb.fractal.fractalizer.ADLWriterGraphVisitor#visit(org.objectweb.fractal.fractalizer.graph.ComponentGraph)
   */
    public String visit(final ComponentGraph graph) {
        graph.accept(this);
        return builder.toString();
    }

    /**
   * @see org.objectweb.fractal.fractalizer.ADLWriterGraphVisitor#accept(org.objectweb.fractal.fractalizer.graph.ComponentGraph)
   */
    public void accept(final ComponentGraph componentGraph) {
        builder.append("<definition name='" + rootDefinitionName + "' >\n");
        for (final PrimitiveComponentNode primitive : componentGraph.getPrimitiveComponentNodes()) {
            primitive.accept(this);
        }
        for (final BindingNode bn : componentGraph.getBindingNodes()) bn.accept(this);
        builder.append("</definition>");
    }

    /**
   * @see org.objectweb.fractal.fractalizer.ADLWriterGraphVisitor#accept(org.objectweb.fractal.fractalizer.graph.PrimitiveComponentNode)
   */
    public void accept(final PrimitiveComponentNode primitive) {
        builder.append("<component name='" + primitive.getName() + "'>\n");
        for (final InterfaceNode interfaceNode : primitive.getClientInterfaces()) {
            interfaceNode.accept(this);
        }
        for (final InterfaceNode interfaceNode : primitive.getServerInterfaces()) {
            interfaceNode.accept(this);
        }
        builder.append("<content class='" + primitive.getPrimitiveImplementation() + "'/>\n");
        builder.append("<controller desc='primitive'/>\n");
        builder.append("</component>\n");
    }

    public void accept(final InterfaceNode interfaceNode) {
        final String role = interfaceRole(interfaceNode);
        builder.append("<interface name='" + interfaceNode.getName() + "' role='" + role + "' signature='" + interfaceNode.getSignature() + "' />\n");
    }

    private String interfaceRole(final InterfaceNode interfaceNode) {
        String role = "client";
        if (!interfaceNode.isClient()) {
            role = "server";
        }
        return role;
    }

    /**
   * @see org.objectweb.fractal.fractalizer.ADLWriterGraphVisitor#accept(org.objectweb.fractal.fractalizer.graph.BindingNode)
   */
    public void accept(final BindingNode bindingNodeImpl) {
        final InterfaceNode from = bindingNodeImpl.getFrom();
        final Set<InterfaceNode> possibleTos = bindingNodeImpl.getPossibleTos();
        boolean first = true;
        for (final InterfaceNode to : possibleTos) {
            if (first) {
                builder.append("<binding from='" + from.getOwner().getName() + "." + from.getName() + "' to='" + to.getOwner().getName() + "." + to.getName() + "' />\n");
                first = false;
            } else {
                builder.append("<!-- <binding from='" + from.getOwner().getName() + "." + from.getName() + "' to='" + to.getOwner().getName() + "." + to.getName() + "' /> --> \n");
            }
        }
    }
}
