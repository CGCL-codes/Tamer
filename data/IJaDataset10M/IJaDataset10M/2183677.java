package org.jbjf.xml.junit;

import java.io.File;
import org.jbjf.xml.*;
import org.jdom.Element;
import junit.framework.TestCase;

/**
 * Tests the JBJFFTPDefinition class.
 * <p>
 * <pre>
 * -------------- 
 * <b>History </b>: Begin 
 * -------------- 
 * &lt;history&gt;
 * &nbsp;&nbsp;&lt;change&gt; 
 *     1.0.0; ASL; Jul 24, 2006
 *     Initial version created and customized for the ...
 *     Naming Conventions 
 *     ------------------
 *     Scope Conventions
 *       >> g - global
 *       >> m - module/class
 *       >> l - local/method/function
 *     Variable Conventions
 *       >> str - string, text, character
 *       >> lng - integer, long, numeric
 *       >> flt - real, floating point
 *       >> the - object, class, module
 *     Examples
 *       >> lstrName - local string to contain name
 *       >> glngVerbose - global integer indicator for verbose mode
 *       >> mtheScanner - class/module for a document scanner
 * &nbsp;&nbsp;&lt;/change&gt; 
 * &lt;/history&gt; 
 * -------------- 
 * <b>History </b>: End 
 * -------------- 
 * </pre>
 * @author  Adym S. Lincoln<br>
 * Copyright (C) 2007. Adym S. Lincoln  All rights reserved.
 * @version 1.0.0
 * @since   1.0.0
 * <p>
 */
public class TestJBJFSQLParameter extends TestCase {

    private String mstrXMLFile = "jbjf-base-definition.xml";

    private JBJFSQLParameter mtestItem;

    /** 
     * Property that stores the XML Definition object that will house
     * the XML file we parse.
     * @since 1.0.0
     */
    private JBJFBaseDefinition mtheDef;

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = TestJBJFSQLParameter.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "TestJBJFSQLParameter()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * 
     */
    public TestJBJFSQLParameter() {
        super();
    }

    /**
     * @param pArg0
     */
    public TestJBJFSQLParameter(String pArg0) {
        super(pArg0);
    }

    /**
     * Test all the primary getter methods for the XML Connnection
     * Definition.  Extended methods need not be tested here.
     */
    public void testGetters() {
        Element ltheElement = this.mtheDef.findElementByName("sql-parameters", this.mtheDef.getRootElement());
        try {
            this.mtestItem = new JBJFSQLParameter((Element) ltheElement.getChildren().get(0));
        } catch (Exception ltheXcp) {
            ltheXcp.printStackTrace();
        }
        TestCase.assertEquals(mtestItem.getName(), "p002");
        TestCase.assertEquals(mtestItem.getOrder(), 2);
        TestCase.assertEquals(mtestItem.getType(), "string");
        TestCase.assertEquals(mtestItem.getValue(), "my_parameter");
    }

    /**
     * Test all the primary setter methods for the XML Connnection
     * Definition.  Extended methods need not be tested here.
     */
    public void testSetters() {
        Element ltheElement = this.mtheDef.findElementByName("sql-parameters", this.mtheDef.getRootElement());
        try {
            this.mtestItem = new JBJFSQLParameter((Element) ltheElement.getChildren().get(0));
        } catch (Exception ltheXcp) {
            ltheXcp.printStackTrace();
        }
        this.mtestItem.setName("p001");
        this.mtestItem.setOrder(3);
        this.mtestItem.setType("adym");
        this.mtestItem.setValue("value-added");
        TestCase.assertEquals(mtestItem.getName(), "p001");
        TestCase.assertEquals(mtestItem.getOrder(), 3);
        TestCase.assertEquals(mtestItem.getType(), "adym");
        TestCase.assertEquals(mtestItem.getValue(), "value-added");
    }

    /**
     * Run setUp() to perform pre-test functions.
     */
    public void setUp() {
        try {
            this.mtheDef = new JBJFBaseDefinition("." + File.separator + "etc" + File.separator + this.mstrXMLFile);
        } catch (Exception ltheXCP) {
            ltheXCP.printStackTrace();
        }
    }
}
