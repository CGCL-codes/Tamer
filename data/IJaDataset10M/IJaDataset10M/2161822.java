package com.surfacetension.controller;

import com.surfacetension.model.ISTModel;
import com.surfacetension.view.ISTView;

/**
 * @author Dinesh Kumar
 *
 */
public interface ISTController {

    public ISTView getView();

    public ISTModel getModel();
}
