package com.google.code.jahath.common.container;

import java.util.concurrent.Future;

public interface ExecutionEnvironment {

    void execute(Task task);

    Future<?> submit(Task task);
}
