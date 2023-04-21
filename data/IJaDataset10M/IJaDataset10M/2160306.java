package org.poker.prophecy.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.poker.prophecy.PropertiesManager;

/**
 * Testet die Klasse PropertiesManager
 * @see PropertiesManager
 * @author bg
 */
public class PropertiesManagerTest extends TestCase {

    @Test
    public void test() {
        System.out.println("PropertiesManager.getLogsDirPath(): " + PropertiesManager.getLogsDirPath());
        System.out.println("PropertiesManager.getCurrentLogFilePath(): " + PropertiesManager.getCurrentLogFilePath());
        System.out.println("PropertiesManager.getCurrentLogFilePath(): " + PropertiesManager.getCurrentErrorLogFilePath());
    }
}
