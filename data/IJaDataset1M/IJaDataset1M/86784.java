package com.genia.toolbox.utils.manager.impl;

import javax.annotation.Resource;
import org.junit.Test;
import com.genia.toolbox.basics.manager.CsvManager;
import com.genia.toolbox.utils.manager.AbstractCsvManagerTest;

/**
 * implementation of {@link AbstractCsvManagerTest}.
 */
public class CsvManagerTest extends AbstractCsvManagerTest {

    /**
   * the {@link CsvManager} to test.
   */
    @Resource
    private CsvManager csvManager;

    /**
   * returns the {@link CsvManager} to test.
   * 
   * @return the {@link CsvManager} to test
   * @see com.genia.toolbox.utils.manager.AbstractCsvManagerTest#getCsvManager()
   */
    @Override
    protected CsvManager getCsvManager() {
        return csvManager;
    }

    /**
   * empty test for eclipse.
   */
    @Test
    public void empty4Eclipse() {
    }
}
