package cc.w3d.jawos2.tests.badTestableSimplex1.engines.xTestableEngineB;

import cc.w3d.jawos2.jinn2.tools.configurationtools.Configuration;
import cc.w3d.jawos2.tests.testableSimplex.engine.IXTestableEngine;
import cc.w3d.jawos2.tests.testableSimplex.engines.xTestableEngineA.XTestEngineA;

public class XTestEngineB implements IXTestableEngine {

    private String value = null;

    @Override
    public String method() {
        return "XTestDefaultEngine::method(" + value + ");";
    }

    @Override
    public IXTestableEngine getClone() {
        XTestEngineB r = new XTestEngineB();
        r.value = this.value;
        return r;
    }

    @Override
    public void configure(Configuration configuration) {
        value = configuration.get("value");
    }

    @Override
    public String getDefaultConfiguration() {
        return "value=B";
    }

    @Override
    public void setVal(String val) {
        this.value = val;
    }
}
