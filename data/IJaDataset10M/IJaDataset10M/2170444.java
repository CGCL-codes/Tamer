package net.sf.ideoreport.engines.excel;

import net.sf.ideoreport.api.enginestructure.exception.EngineException;

/**
 * @author beausser
 *
 * Classe de test
 */
public class SimpleTemplatesTest extends ExcelEngineGenericTest {

    public void testProcessDefaultDataContainer() throws EngineException {
        traiterContainer(defaultDataContainer, defaultDataContainer.getName() + "_output.xls", defaultParams);
    }
}
