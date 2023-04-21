package com.google.code.task.example;

import static com.google.code.task.Style.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.code.task.example.resources.GenesisCoupeTask;
import com.google.code.task.example.resources.LoggingFailoverTask;
import com.google.code.task.example.resources.TaskEventHandler;
import com.google.code.task.example.resources.TiburonCoupeTask;
import com.google.code.task.example.resources.TuscanyCoupeTask;

public class MoreComplexDecoratorExample {

    static final Log LOG = LogFactory.getLog(MoreComplexDecoratorExample.class);

    public static void main(String[] args) throws Exception {
        startAsyncerTaskProcessor();
        startTask();
        new GenesisCoupeTask().aspect().before(new TiburonCoupeTask()).after(new TuscanyCoupeTask()).failover(new LoggingFailoverTask()).retryCount(1).asynchronously().event(new TaskEventHandler()).execute();
        endTask();
        endAsyncerTaskProcessor();
    }
}
