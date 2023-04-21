package com.google.code.task.example;

import static com.google.code.task.Style.*;
import com.google.code.task.example.resources.GenesisCoupeTask;
import com.google.code.task.example.resources.LoggingCurrentTimeTask;
import com.google.code.task.example.resources.TiburonCoupeTask;
import com.google.code.task.example.resources.TuscanyCoupeTask;

public class CombinationExample {

    public static void main(String[] args) throws Exception {
        startAsyncerTaskProcessor();
        startTask();
        aspect(beforeTask(new LoggingCurrentTimeTask()), mainTask(chain(new GenesisCoupeTask(), new TuscanyCoupeTask(), new TiburonCoupeTask())), afterTask(chain(new LoggingCurrentTimeTask(), new LoggingCurrentTimeTask()))).asynchronously().execute();
        endTask();
        endAsyncerTaskProcessor();
    }
}
