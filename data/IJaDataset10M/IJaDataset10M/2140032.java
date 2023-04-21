package org.jenetics;

import javolution.xml.stream.XMLStreamException;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: IntegerGeneTest.java,v 1.2 2008-09-23 19:18:27 fwilhelm Exp $
 */
public class IntegerGeneTest {

    @Test
    public void serialize() throws XMLStreamException {
        SerializeUtils.testSerialization(IntegerGene.valueOf(5, 0, 10));
        SerializeUtils.testSerialization(IntegerGene.valueOf(5, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}
