package org.datanucleus.store.xml.valuegenerator;

import java.util.Properties;
import javax.xml.xpath.XPathFactory;
import org.datanucleus.store.valuegenerator.AbstractDatastoreGenerator;
import org.datanucleus.store.valuegenerator.ValueGenerationBlock;
import org.datanucleus.store.valuegenerator.ValueGenerationException;

/**
 * Value generator for calling xpath generate-id().
 */
public class GenerateIDGenerator extends AbstractDatastoreGenerator {

    /**
     * Constructor.
     * @param name Symbolic name of the generator
     * @param props Any properties controlling its behaviour.
     */
    public GenerateIDGenerator(String name, Properties props) {
        super(name, props);
    }

    /**
     * Method to reserve a block of values.
     * Only ever reserves a single timestamp, to the time at which it is created.
     * @param size Number of elements to reserve.
     * @return The block.
     */
    protected ValueGenerationBlock reserveBlock(long size) {
        try {
            Object doc = connectionProvider.retrieveConnection().getConnection();
            String id = XPathFactory.newInstance().newXPath().evaluate("generate-id(.)", doc);
            ValueGenerationBlock block = new ValueGenerationBlock(new String[] { id });
            return block;
        } catch (Exception e) {
            throw new ValueGenerationException(e.getMessage(), e);
        } finally {
            connectionProvider.releaseConnection();
        }
    }
}
