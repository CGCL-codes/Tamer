package net.mystrobe.client;

import net.mystrobe.client.connector.DAOSchemaTest;
import net.mystrobe.client.connector.DSSchemaTest;
import net.mystrobe.client.connector.DataObjectTest;
import net.mystrobe.client.connector.DataRequestTest;
import net.mystrobe.client.connector.FilterDataTest;
import net.mystrobe.client.connector.SortDataTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author TVH Group NV
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestStringToJavaNativeUtil.class, DataRequestTest.class, DSSchemaTest.class, DAOSchemaTest.class, DataRequestTest.class, DataObjectTest.class, FilterDataTest.class, SortDataTest.class })
public class RunTests {
}
