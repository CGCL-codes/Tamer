package uk.ac.ncl.instantsoap.webServiceDispatcher.impl;

import junit.framework.TestCase;
import junit.framework.Assert;
import uk.ac.ncl.instantsoap.webServiceDispatcher.impl.WebServiceDispatcherImpl2;
import uk.ac.ncl.instantsoap.serviceDispatcher.AsynchronousServiceDispatcher;
import uk.ac.ncl.instantsoap.JobSpecification;
import uk.ac.ncl.instantsoap.XML;

public class WebServiceDispatcherImpl2Test extends TestCase {

    public void testConstruction() throws Exception {
        WebServiceDispatcherImpl2 w2 = new WebServiceDispatcherImpl2();
    }

    public void testInvokeBlocking() throws Exception {
        JobSpecification js = new JobSpecification() {

            public String getTaskName() {
                return "hello";
            }

            public XML getData() {
                return new XML();
            }
        };
        WebServiceDispatcherImpl2 w2 = new WebServiceDispatcherImpl2();
        w2.invokeAndBlock(js);
    }
}
