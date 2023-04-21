package net.sourceforge.fluxion.pussycat.renderlets;

import java.util.Set;

/**
 * PussycatContainer should be instantiated with the {@link net.sourceforge.fluxion.pussycat.manager.PussycatSessionManager}
 * it is contained within.
 *
 * @author Tony Burdett
 * @author Rob Davey
 * @version 1.0
 * @date 29-Feb-2008
 */
public interface RenderletContainer {

    /**
   * Add a new renderlet to the framework.  The renderletName represent the fully qualified class name of the renderlet
   * to use.
   *
   * @param idPrefix a prefix to uniquely identify this renderlet
   * @param renderletName renderletName
   */
    void add(String idPrefix, String renderletName);

    /**
   * Look up the renderlet by its id, and lookup the referenced object that is being changed. Then update the renderlet
   * instance with the current linked object
   *
   * @param renderletId renderlet id
   * @param objectId    object id
   * @return the ids of renderlets that need updating
   */
    Set<String> update(String renderletId, String objectId);

    /**
   * Removes a renderlet instance
   *
   * @param renderletId the renderlet to remove
   * @return true is successful, false otherwise
   */
    boolean remove(String renderletId);

    void addRenderletContainerListener(RenderletContainerListener rcl);

    void removeRenderletContainerListener(RenderletContainerListener rcl);
}
