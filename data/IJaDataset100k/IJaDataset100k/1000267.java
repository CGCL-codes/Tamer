package com.phloc.commons.microdom;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.parent.IHasChildrenSorted;
import com.phloc.commons.parent.IHasParent;
import com.phloc.commons.state.EChange;

/**
 * This is the base interface for all kind of nodes in the micro document object
 * model.
 * 
 * @author philip
 */
public interface IMicroNode extends ICloneable<IMicroNode>, IHasChildrenSorted<IMicroNode>, IHasParent<IMicroNode>, Serializable {

    /**
   * @return Just an abstract name that depends on the implementing class.
   */
    @Nonnull
    @Nonempty
    String getNodeName();

    /**
   * @return The value of this node. This depends on the concrete implementation
   *         class. It is currently implemented for {@link IMicroText},
   *         {@link IMicroComment} and {@link IMicroEntityReference}.
   */
    String getNodeValue();

    /**
   * @return <code>true</code> if at least one child is present,
   *         <code>false</code> otherwise
   */
    boolean hasChildren();

    /**
   * Get a list of all direct child nodes.
   * 
   * @return May be <code>null</code> if the node has no children.
   */
    @Nullable
    List<IMicroNode> getChildren();

    /**
   * @return The first child node of this node, or <code>null</code> if this
   *         node has no children.
   */
    @Nullable
    IMicroNode getFirstChild();

    /**
   * @return The last child node of this node, or <code>null</code> if this node
   *         has no children.
   */
    @Nullable
    IMicroNode getLastChild();

    /**
   * @return A list containing all recursively contained child elements. May be
   *         <code>null</code> if this node has no children.
   */
    @Nullable
    List<IMicroNode> getAllChildrenRecursive();

    /**
   * @return The previous node on the same level as this node, or
   *         <code>null</code> if this node has no preceding siblings.
   */
    @Nullable
    IMicroNode getPreviousSibling();

    /**
   * @return The next node on the same level as this node, or <code>null</code>
   *         if this node has no succeeding siblings.
   */
    @Nullable
    IMicroNode getNextSibling();

    /**
   * @return <code>true</code> if this node has a parent node assigned,
   *         <code>false</code> otherwise.
   */
    boolean hasParent();

    /**
   * @return May be <code>null</code>.
   */
    @Nullable
    IMicroNode getParent();

    /**
   * Detach this node from the parent node so it can be inserted into another
   * node without problems. Otherwise you would get an
   * {@link IllegalStateException} if adding this node again to another parent
   * since each node can only have one parent.
   * 
   * @return this
   */
    @Nonnull
    IMicroNode detachFromParent();

    @Nullable
    IMicroElement getParentElementWithName(@Nullable String sTagName);

    @Nullable
    IMicroElement getParentElementWithName(@Nullable String sNamespaceURI, @Nullable String sTagName);

    /**
   * Append any child to the node.
   * 
   * @param <NODETYPE>
   *        Parameter type == return type
   * @param aChildNode
   *        The child node to append. May be <code>null</code>.
   * @return The appended node, or <code>null</code> if the parameter was
   *         <code>null</code>.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nullable
    <NODETYPE extends IMicroNode> NODETYPE appendChild(@Nullable NODETYPE aChildNode) throws MicroException;

    /**
   * Insert an existing node before a certain child node of this.
   * 
   * @param aChildNode
   *        The new child node to be inserted.
   * @param aSuccessor
   *        The node before which the new node will be inserted.
   * @return The newly inserted node
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nullable
    <NODETYPE extends IMicroNode> NODETYPE insertBefore(@Nullable NODETYPE aChildNode, @Nonnull IMicroNode aSuccessor) throws MicroException;

    /**
   * Insert an existing node after a certain child node of this.
   * 
   * @param aChildNode
   *        The new child node to be inserted.
   * @param aPredecessor
   *        The node after which the new node will be inserted.
   * @return The newly inserted node
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nullable
    <NODETYPE extends IMicroNode> NODETYPE insertAfter(@Nullable NODETYPE aChildNode, @Nonnull IMicroNode aPredecessor) throws MicroException;

    /**
   * Insert an existing node as a child at the specified index.
   * 
   * @param nIndex
   *        The index to insert. Must be &ge; 0.
   * @param aChildNode
   *        The new child node to be inserted.
   * @return The newly inserted node
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nullable
    <NODETYPE extends IMicroNode> NODETYPE insertAtIndex(@Nonnegative int nIndex, @Nullable NODETYPE aChildNode) throws MicroException;

    /**
   * Append a text node to this node.
   * 
   * @param sText
   *        text to be added
   * @return The created text node.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroText appendText(@Nullable CharSequence sText) throws MicroException;

    /**
   * Append a text node to this node. If the type of the value is not
   * {@link String}, the {@link com.phloc.commons.typeconvert.TypeConverter} is
   * invoked to convert it to a {@link String} object.
   * 
   * @param aValue
   *        text to be added
   * @return The created text node.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroText appendTextWithConversion(@Nullable Object aValue) throws MicroException;

    /**
   * Append a text node which is ignorable whitespace content to this node.
   * 
   * @param sText
   *        The whitespace content to be added.
   * @return The created text node.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroText appendIgnorableWhitespaceText(@Nullable CharSequence sText) throws MicroException;

    /**
   * Append a CDATA node to this node.
   * 
   * @param sText
   *        CDATA text
   * @return The created comment.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroCDATA appendCDATA(@Nullable CharSequence sText) throws MicroException;

    /**
   * Append a comment node to this node.
   * 
   * @param sText
   *        comment text
   * @return The created comment.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroComment appendComment(@Nullable CharSequence sText) throws MicroException;

    /**
   * Append an entity reference to this node.
   * 
   * @param sName
   *        The name of the entity reference.
   * @return The created entity reference.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroEntityReference appendEntityReference(@Nonnull String sName) throws MicroException;

    /**
   * Append an element without namespace to this node.
   * 
   * @param sTagName
   *        element name to be created
   * @return The created element
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroElement appendElement(@Nonnull @Nonempty String sTagName) throws MicroException;

    /**
   * Append an element with namespace to this node.
   * 
   * @param sNamespaceURI
   *        Namespace URI to use.
   * @param sTagName
   *        element name to be created
   * @return The created element
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroElement appendElement(@Nullable String sNamespaceURI, @Nonnull @Nonempty String sTagName) throws MicroException;

    /**
   * Append a processing instruction to this node.
   * 
   * @param sTarget
   *        The PI target
   * @param sData
   *        The PI data
   * @return The created element
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroProcessingInstruction appendProcessingInstruction(@Nonnull @Nonempty String sTarget, @Nullable String sData) throws MicroException;

    /**
   * Append a new container to this node
   * 
   * @return The created container.
   * @throws MicroException
   *         if this node cannot have children
   */
    @Nonnull
    IMicroContainer appendContainer() throws MicroException;

    /**
   * Remove the passed child.
   * 
   * @param aChild
   *        The child to be removed. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the child was successfully removed,
   *         {@link EChange#UNCHANGED} otherwise.
   */
    @Nonnull
    EChange removeChild(@Nonnull IMicroNode aChild);

    /**
   * Remove the child not at the specified index.
   * 
   * @param nIndex
   *        The 0-based index of the item to be removed.
   * @return {@link EChange#CHANGED} if the node was successfully removed,
   *         {@link EChange#UNCHANGED} otherwise.
   */
    @Nonnull
    EChange removeChildAtIndex(@Nonnegative int nIndex);

    /**
   * Remove all children from this node.
   * 
   * @return {@link EChange#CHANGED} if at least one child was present, and was
   *         successfully removed, {@link EChange#UNCHANGED} otherwise.
   */
    @Nonnull
    EChange removeAllChildren();

    /**
   * Replace the passed old child with the new child.
   * 
   * @param aOldChild
   *        The child to be removed. May not be <code>null</code>.
   * @param aNewChild
   *        The child to be inserted instead. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the child was successfully replaced,
   *         {@link EChange#UNCHANGED} if old child and new child are identical.
   */
    @Nonnull
    EChange replaceChild(@Nonnull IMicroNode aOldChild, @Nonnull IMicroNode aNewChild);

    /**
   * @return The node type. Never <code>null</code>.
   */
    @Nonnull
    EMicroNodeType getType();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroDocument}.
   */
    boolean isDocument();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroDocumentType}.
   */
    boolean isDocumentType();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroText}.
   */
    boolean isText();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroCDATA}.
   */
    boolean isCDATA();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroComment}.
   */
    boolean isComment();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroEntityReference}.
   */
    boolean isEntityReference();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroElement}.
   */
    boolean isElement();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroProcessingInstruction}.
   */
    boolean isProcessingInstruction();

    /**
   * @return <code>true</code> if this node can safely be casted to
   *         {@link IMicroContainer}.
   */
    boolean isContainer();

    /**
   * Register a specific MicroDOM event listener. One event listener can only be
   * attached once to an event!
   * 
   * @param eEventType
   *        The event type. May not be <code>null</code>.
   * @param aTarget
   *        The event target to be added. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the event listener was registered,
   *         {@link EChange#UNCHANGED} otherwise.
   */
    @Nonnull
    EChange registerEventTarget(@Nonnull EMicroEvent eEventType, @Nonnull IMicroEventTarget aTarget);

    /**
   * Unregister a specific MicroDOM event listener.
   * 
   * @param eEventType
   *        The event type. May not be <code>null</code>.
   * @param aTarget
   *        The event target to be added. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the event listener was unregistered,
   *         {@link EChange#UNCHANGED} otherwise.
   */
    @Nonnull
    EChange unregisterEventTarget(@Nonnull EMicroEvent eEventType, @Nonnull IMicroEventTarget aTarget);

    /**
   * As instances of this class may not implement equals/hashCode we need a way
   * to determine, if 2 nodes are equal by content.
   * 
   * @param aNode
   *        The node to compare to this.
   * @return <code>true</code> if the nodes are of the same type and the same
   *         content, <code>false</code> otherwise.
   */
    boolean isEqualContent(@Nullable IMicroNode aNode);
}
