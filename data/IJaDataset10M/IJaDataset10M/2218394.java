package org.powermock.tests.utils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * A test chunk consists of a list of methods that should be executed by a
 * particular classloader.
 * */
public interface TestChunk {

    ClassLoader getClassLoader();

    List<Method> getTestMethodsToBeExecutedByThisClassloader();
}
