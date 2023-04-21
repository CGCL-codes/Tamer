package com.antilia.web.grid;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IGridService {

    IGridService registerWidgetModel(WidgetModel model);

    IGridService deregisterWidgetModel(WidgetModel model);

    Iterable<WidgetModel> getWidgetModels();
}
