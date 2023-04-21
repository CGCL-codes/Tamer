package org.paninij.asynctype.testproviders;

import org.paninij.asynctype.framework.IAsyncTypeTestAdaptor;
import edu.iastate.cs.designlab.testutilities.ICompilerTest;

/**
 * Basic AsyncType tests.
 * @author Sean Mooney
 * @since Aug 27, 2010
 */
public class SimpleAsyncTypeTests {

    /**
     * An ICompilerTest that does nothing other than create the 
     * AsyncType compiler frontend.
     * @return
     */
    public static ICompilerTest testCreateFrontend() {
        ICompilerTest simpleTest = new IAsyncTypeTestAdaptor();
        return simpleTest;
    }
}
