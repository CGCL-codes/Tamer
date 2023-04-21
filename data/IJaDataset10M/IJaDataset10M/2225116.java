package com.bluebrim.solitarylayouteditor.test;

import java.util.*;
import com.bluebrim.extensibility.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Test to verify that wanted SPI's are included in the class path that
 * is genererated by default in the launch configuration.
 * 
 * @author G�ran St�ck
 *
 */
public class CoServicesTest {

    public static void main(String[] args) {
        Iterator providers = CoServices.getProviderNames(CoXmlMappingSPI.class);
        while (providers.hasNext()) {
            String className = (String) providers.next();
            System.out.println(className);
            try {
                Class cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }
}
