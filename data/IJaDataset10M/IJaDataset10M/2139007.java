package org.leo.oglexplorer.search.impl;

import org.leo.oglexplorer.model.engine.impl.MySpaceSearchEngine;
import org.leo.oglexplorer.search.GenericSearchTest;

/**
 * MyspaceTest $Id: MyspaceTest.java 112 2011-08-02 02:19:57Z leolewis $
 * <pre>
 * </pre>
 * @author Leo Lewis
 */
public class MyspaceTest extends GenericSearchTest<MySpaceSearchEngine> {

    /**
	 * @see org.leo.oglexplorer.search.GenericSearchTest#createSearchEngine()
	 */
    @Override
    public MySpaceSearchEngine createSearchEngine() throws Exception {
        return new MySpaceSearchEngine();
    }
}
