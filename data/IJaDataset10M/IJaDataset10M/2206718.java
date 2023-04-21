package com.jme3.gde.core.filters.impl;

import com.jme3.gde.core.filters.actions.AbstractNewFilterAction;
import com.jme3.gde.core.filters.actions.NewFilterAction;
import com.jme3.post.Filter;
import com.jme3.post.filters.DepthOfFieldFilter;

/**
 *
 * @author normenhansen
 */
@org.openide.util.lookup.ServiceProvider(service = NewFilterAction.class)
public class NewDepthOfFieldFilterAction extends AbstractNewFilterAction {

    public NewDepthOfFieldFilterAction() {
        name = "DepthOfField";
    }

    @Override
    protected Filter doCreateFilter() {
        return new DepthOfFieldFilter();
    }
}
