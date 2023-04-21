package org.unitils.dataset.annotation.handler.impl;

import org.unitils.dataset.DataSetModule;
import org.unitils.dataset.annotation.InlineAssertDataSet;
import org.unitils.dataset.annotation.handler.DataSetAnnotationHandler;
import org.unitils.dataset.assertstrategy.InlineAssertDataSetStrategyHandler;
import org.unitils.dataset.factory.DataSetStrategyHandlerFactory;
import java.lang.reflect.Method;

/**
 * Handles the execution of the {@link InlineAssertDataSet} annotation.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class InlineAssertDataSetAnnotationHandler implements DataSetAnnotationHandler<InlineAssertDataSet> {

    public void handle(InlineAssertDataSet annotation, Method testMethod, Object testInstance, DataSetModule dataSetModule) {
        String[] dataSetRows = annotation.value();
        boolean logDatabaseContentOnAssertionError = annotation.logDatabaseContentOnAssertionError();
        String databaseName = annotation.databaseName();
        DataSetStrategyHandlerFactory dataSetStrategyHandlerFactory = dataSetModule.getDataSetStrategyHandlerFactory();
        InlineAssertDataSetStrategyHandler inlineAssertDataSetStrategyHandler = dataSetStrategyHandlerFactory.createInlineAssertDataSetStrategyHandler(databaseName);
        inlineAssertDataSetStrategyHandler.assertExpectedDataSet(logDatabaseContentOnAssertionError, dataSetRows);
    }
}
