package de.sonivis.tool.textmining.test.transformers;

import java.util.Map;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.datamodel.extractwizard.TransformerManager;
import de.sonivis.tool.core.tests.AbstractFilledDatabaseTestCase;
import de.sonivis.tool.transformers.textmining.AbstractTermTransformer;

/**
 * Test the term transformer.
 * 
 * @author Nette
 * @version $Revision$, $Date$
 */
public class AbstractTermTransformerTest extends AbstractFilledDatabaseTestCase {

    /**
	 * Logger at {@value} .
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTermTransformerTest.class.getName());

    /***************************************************************************
	 * JUnit Test Functions
	 **************************************************************************/
    public final void testTermTransformerType() throws Exception {
        final TransformerManager transformManager = new TransformerManager();
        final Map<String, AbstractTermTransformer> hmRegisteredTermTransformer = AbstractTermTransformer.getRegisteredTermTransformers();
        for (String strTermTransformerName : hmRegisteredTermTransformer.keySet()) {
            LOGGER.info("Test the term transformer: " + strTermTransformerName);
            final AbstractTermTransformer termTransformer = hmRegisteredTermTransformer.get(strTermTransformerName);
            transformManager.transform(termTransformer, this.infoSpace, new NullProgressMonitor());
            assertTrue(termTransformer.isTransformerExecuted(infoSpace));
            termTransformer.rollbackTransformer(infoSpace);
            assertFalse(termTransformer.isTransformerExecuted(infoSpace));
        }
    }
}
