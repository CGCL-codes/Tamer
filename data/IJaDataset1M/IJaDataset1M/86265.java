package com.acv.common.model.selector.impl;

import com.acv.common.model.entity.BookableService;
import com.acv.common.model.selector.BookableServiceSelector;

/**
 * Created by IntelliJ IDEA.
 * User: Guy_acv
 * Date: 7-Nov-2007
 * Time: 11:46:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectedBookableService implements BookableServiceSelector {

    protected BookableService service;

    public SelectedBookableService(BookableService service) {
        this.service = service;
    }

    public BookableService getService() {
        return service;
    }
}
