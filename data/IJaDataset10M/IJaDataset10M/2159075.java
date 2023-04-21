package net.sourceforge.schemata.schema.dao.jdom;

import java.io.InputStream;
import net.sourceforge.schemata.schema.dao.AbstractXmlSchemaDaoTest;
import net.sourceforge.schemata.schema.model.Schema;

/**
 * Tests the {@link JdomSchemaDao}.
 * 
 * @author Ken Weiner
 * @version $Id: JdomSchemaDaoTest.java,v 1.1 2005/11/28 18:24:17 kweiner Exp $
 * @since 0.1.0
 */
public class JdomSchemaDaoTest extends AbstractXmlSchemaDaoTest {

    public void testParser() throws Exception {
        InputStream schemaXml = getSchemaXml();
        InputStream schemaSchema = getSchemaSchema();
        JdomSchemaDao schemaDao = new JdomSchemaDao(schemaXml, schemaSchema);
        Schema schema = schemaDao.parseSchema();
        assertValidSchema(schema);
    }
}
