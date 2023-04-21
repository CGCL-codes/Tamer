package com.asl.library.executor.services;

import java.util.Map;

/**
 * @author asl
 *
 */
public interface Executor {

    public ExecutionResult execute();

    public ExecutionResult execute(Map<String, String> params);
}
