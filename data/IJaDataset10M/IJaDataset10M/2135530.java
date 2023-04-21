package org.identifylife.descriptlet.store.web.jersey.resource.result;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.identifylife.descriptlet.store.model.oxm.AbstractMarshallerTest;
import org.identifylife.descriptlet.store.web.jersey.response.ResultSet;
import org.junit.Test;

/**
 * @author dbarnier
 *
 */
public class ResultSetTests extends AbstractMarshallerTest {

    protected static final Log logger = LogFactory.getLog(ResultSetTests.class);

    public ResultSetTests() throws Exception {
        super(ResultSet.class);
    }

    @Test
    public void testStringsToXml() throws Exception {
        Set<String> values = new HashSet<String>();
        values.add("stringA");
        values.add("stringB");
        values.add("stringC");
        String result = marshalObjectToXml(new ResultSet<String>(values));
        logger.debug("result: " + result);
    }
}
