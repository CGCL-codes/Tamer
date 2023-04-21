package com.google.inject.testing.guiceberry.junit3;

import com.google.guiceberry.DeprecatedGuiceBerryModule;
import com.google.inject.testing.guiceberry.GuiceBerryEnv;
import com.google.inject.testing.guiceberry.TestId;
import com.google.inject.testing.guiceberry.TestScoped;
import junit.framework.TestCase;

/**
 * This Module provides the three basic bindings required by 
 * {@link GuiceBerryJunit3}, namely {@link TestId}, 
 * {@link TestCase} and the {@link TestScoped} scope. Without these three
 * bindinds, {@link GuiceBerryJunit3#setUp(TestCase)} will fail.

 * <p>Thus, this module is all but required to be installed by all 
 * {@link GuiceBerryEnv}s using JUnit3. 
 * 
 * <p>The only alternative would be to provide the same bindings through some
 * other means.
 * 
 * @see GuiceBerryEnv
 * 
 * @author Luiz-Otavio Zorzella
 * @author Danka Karwanska
 */
public class BasicJunit3Module extends DeprecatedGuiceBerryModule {
}
