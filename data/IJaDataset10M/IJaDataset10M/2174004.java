package org.ddsteps.data.excel;

import org.ddsteps.data.DataLoader;
import org.ddsteps.data.support.DataLoaderFactory;
import org.ddsteps.data.support.DataLoaderProxySupport;
import junit.framework.TestCase;

public class CachingExcelDataLoaderTest extends TestCase {

    /**
	 * @throws Exception
	 * @deprecated
	 */
    public void testCachingExcelDataLoader() throws Exception {
        CachingExcelDataLoader loader = new CachingExcelDataLoader();
        assertSame(DataLoaderFactory.getCachingExcelDataLoader(), loader.getDataLoader());
    }

    /**
	 * @throws Exception
	 * @deprecated
	 */
    public void testGetInstance() throws Exception {
        DataLoader loader = CachingExcelDataLoader.getInstance();
        assertSame(DataLoaderFactory.getCachingExcelDataLoader(), loader);
    }
}
