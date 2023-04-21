package com.w20e.socrates.rendering;

import java.io.Serializable;
import java.util.Collection;

/**
 * State stores some moment in time. The RenderState should hold a list of
 * RenderItems, that can either be groups, controls, or a combination of both.
 * @author dokter
 */
public interface RenderState extends Serializable {

    /**
   * Get the items in this state.
   * @return the list of items in this state.
   */
    Collection<Renderable> getItems();
}
