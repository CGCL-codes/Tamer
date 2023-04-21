package cc.w3d.jawos2.tests.badTestableSimplex2.engine;

import cc.w3d.jawos2.jinn2.simplexcore.simplexcore.engine.IEngine;

public interface IXTestableEngine extends IEngine<IXTestableEngine> {

    public String method();

    public void setVal(String val);
}
