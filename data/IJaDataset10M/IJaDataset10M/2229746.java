package net.sf.doolin.svnchlog.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.net.URL;
import net.sf.doolin.sdo.DataFactory;
import net.sf.doolin.sdo.DataSchema;
import net.sf.doolin.sdo.factory.json.JSONDataFactoryReader;
import net.sf.doolin.sdo.support.DataFactories;
import org.junit.Test;

public class ModelTest {

    private final JSONDataFactoryReader reader = new JSONDataFactoryReader();

    @Test
    public void types() throws IOException {
        DataFactory factory = DataFactories.typedFactory();
        URL url = getClass().getResource("/types.json");
        DataSchema schema = this.reader.read(url, factory);
        assertNotNull(schema);
        assertEquals("SVNChangeLog", schema.getName());
        assertNotNull(schema.getType("Project"));
        assertNotNull(schema.getType("ProjectConfiguration"));
        assertNotNull(schema.getType("ProjectIssueConnector"));
        assertNotNull(schema.getType("PromotionSchema"));
        assertNotNull(schema.getType("PromotionLevel"));
        assertNotNull(schema.getType("Parameter"));
        assertNotNull(schema.getType("IssueQuery"));
    }
}
