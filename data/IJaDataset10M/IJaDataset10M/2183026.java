package org.specrunner.context;

import java.util.Map;
import nu.xom.Node;
import org.specrunner.plugins.IPlugin;

/**
 * Block of information on context stack.
 * 
 * @author Thiago Santos
 * 
 */
public interface IBlock {

    /**
     * Indicates if the bloc node has children.
     * 
     * @return true, if is not terminal in the specification, false, otherwise.
     */
    boolean hasChildren();

    /**
     * Indicates if a given block has changed.
     * 
     * @return true, if the content of block has changed, false, otherwise.
     */
    boolean isChanged();

    /**
     * Sets the changed status.
     * 
     * @param changed
     *            The new status.
     */
    void setChanged(boolean changed);

    /**
     * Indicates if block has been created form a specification or not.
     * 
     * @return True, if it has node attached to it, false, otherwise.
     */
    boolean hasNode();

    /**
     * Returns the specification node object related to the block.
     * 
     * @return The block node.
     */
    Node getNode();

    /**
     * Change block node.
     * 
     * @param node
     *            The new node.
     */
    void setNode(Node node);

    /**
     * Indicates if block has a plugin or not.
     * 
     * @return True, if it has a plugin attached to it, false, otherwise.
     */
    boolean hasPlugin();

    /**
     * The plugin related to the block.
     * 
     * @return The block plugin.
     */
    IPlugin getPlugin();

    /**
     * Sets the block plugin.
     * 
     * @param plugin
     *            The new plugin.
     */
    void setPlugin(IPlugin plugin);

    /**
     * Indicates if block has a mapping or not.
     * 
     * @return True, if it has a map attached to it, false, otherwise.
     */
    boolean hasMap();

    /**
     * Mapping of elements related to the block. This mapping is responsible for
     * the storage of variables and elements that are expected to be shared
     * between plugins.
     * 
     * @return An object mapping.
     */
    Map<String, Object> getMap();

    /**
     * Sets the block mapping of object.
     * 
     * @param map
     *            The new mapping.
     */
    void setMap(Map<String, Object> map);
}
