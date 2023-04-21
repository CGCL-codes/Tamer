package storage.framework;

import org.nodal.model.AnchorNode;
import org.nodal.model.Node;
import org.nodal.model.NodeContent;
import org.nodal.nav.Path;
import org.nodal.type.NodeType;

/**
 * An interface that represents the basic framework for creating Node instances.
 * {@link BasicNodeFactory} is built on top of this interface, and at its most basic
 * it is provided by {@link AbstractRepository.Backend}
 * @see BasicNodeFactory
 * @see AbstractRepository.Backend
 * @author leei
 */
public interface NodeFactoryFramework {

    /**
   * Create a brand new Node with the given NodeType. The
   * <tt>context</tt> Node determines the base metadata for the new
   * Node, including permissions and versioning.
   * 
   * @param type a NodeType describing the Node to be created
   * @param context a Node from which to inherit metadata context
   * @return a new Node of the given NodeType
   */
    NodeContent.Editor createNode(NodeType type, Node context);

    /**
   * Clone a brand new Node copying the content from an existing
   * Node. The <tt>context</tt> Node determines the base metadata for
   * the new Node, including permissions and versioning.
   * @param node a Node from which to inherit type and initial content
   * @param context a Node from which to inherit metadata context
   * @return a new Node of cloned from node
   */
    NodeContent.Editor cloneNode(Node node, Node context);

    /**
   * Create an AnchorNode with that references an absolute Path.
   * @param abs the absolute Path providing the reference
   * @return and AnchorNode that references the abs Path
   */
    AnchorNode createAnchor(Path abs);

    /**
   * Create an AnchorNode with a relative Path reference to a given context
   * in the Repository.
   * @param context an absolute Path that provides context for the rel
   * @param rel a relative Path that is evaluated in context
   * @return an AnchorNode that evaluates the relative Path in context
   */
    AnchorNode createAnchor(Path context, Path rel);
}
