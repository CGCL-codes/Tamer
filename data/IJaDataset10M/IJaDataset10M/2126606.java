package com.googlecode.jazure.examples.eventdriven.inner;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class AggregatorExample implements Aggregator<EventDrivenJobConfig> {

    private Logger logger = LoggerFactory.getLogger(AggregatorExample.class);

    @Override
    public void aggregate(TaskInvocation result) {
        logger.info("Event drivent result : " + result.toString());
    }

    @Override
    public void aggregateCorrelated(EventDrivenJobConfig jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
        logger.debug("Completed of job " + jobConfig.getId() + " and results " + results);
    }

    @Override
    public boolean aggregateCorrelatedSupported(EventDrivenJobConfig jobConfig) {
        return true;
    }

    @Override
    public boolean aggregateSupported(TaskInvocation result) {
        return true;
    }
}
