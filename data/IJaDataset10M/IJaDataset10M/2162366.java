package com.ibm.wala.j2ee.client.impl;

import java.util.Properties;
import com.ibm.wala.j2ee.client.CallGraphBuilderFactory;
import com.ibm.wala.util.debug.Assertions;

/**
 *
 * A factory for creating a call graph builder factory
 * 
 * @author Stephen Fink
 */
public class CallGraphBuilderFactoryFactory {

    /**
   * Construct a CallGraphBuilderFactory
   * 
   * @param props  Optionally, influence the construction of the engine.
   * @return A non-null AppAnalysisEngine instance.
   */
    public static CallGraphBuilderFactory getCallGraphBuilderFactory(Properties props) {
        try {
            String klass = "com.ibm.wala.j2ee.client.impl.RTABuilderFactory";
            if (props != null) {
                klass = props.getProperty("analysis", "com.ibm.wala.j2ee.client.impl.RTABuilderFactory");
            }
            return (CallGraphBuilderFactory) Class.forName(klass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.UNREACHABLE();
            return null;
        }
    }
}
