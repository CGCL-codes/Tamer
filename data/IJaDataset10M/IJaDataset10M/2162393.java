package beantools;

import org.junit.Test;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ClassFinderTest {

    @Test
    public void regressionTestForFind() throws Exception {
        ClassFinder finder = new ClassFinder(".*", "", new Class[] { Test.class }, false, asList("./src/test/data/classes"));
        assertThat(finder.find().size(), equalTo(2));
    }
}
