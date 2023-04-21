package org.grobid.test;

import org.grobid.core.GrobidFactory;
import org.grobid.core.engines.Engine;

/**
 * User: zholudev
 * Date: 11/21/11
 * Time: 7:17 PM
 */
public class EngineTest {

    protected static Engine engine;

    static {
        engine = GrobidFactory.instance.createEngine();
    }
}
