package com.google.gdt.eclipse.designer.model.widgets.support;

/**
 * Provider for GWT state.
 * 
 * @author sablin_aa
 * @coverage gwt.model
 */
public interface IGwtStateProvider {

    /**
   * @return the {@link GwtState} for this hierarchy.
   */
    public GwtState getState();
}
