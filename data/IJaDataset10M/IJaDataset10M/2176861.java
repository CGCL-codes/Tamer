package com.umc.helper.unit_tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Fasst alle Helper Unit Tests zu einer Suite zusammen, um schnell einen Gesamttest machen zu können.
 *  
 * @author DonGyros
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ UpnpTest.class, LanguageTest.class })
public class AllTests {
}
