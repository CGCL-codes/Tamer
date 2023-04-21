package cc.w3d.jawos.xpermisionsManager.engines.xeDataPermisionsManager.configurationManager;

import cc.w3d.jawos.xjawosdata.core.XJawosData;
import cc.w3d.jawos.xpermisionsManager.engines.xeDataPermisionsManager.XEDataPermisionsManager.EngineHandler;
import cc.w3d.jawos.xuidGenerator.core.XUidGenerator;

public class XEDataPermisionsManagerConfigurator {

    private EngineHandler engine;

    public XEDataPermisionsManagerConfigurator(EngineHandler engineHandler) {
        engine = engineHandler;
    }

    public void setReference(String reference) {
        engine.setReference(reference);
    }

    public void format() {
        engine.format();
    }

    public XJawosData getXJawosData() {
        return engine.getXJawosData();
    }

    public void setXJawosData(XJawosData xJawosData) {
        engine.setXJawosData(xJawosData);
    }

    public XUidGenerator getXUidGenerator() {
        return engine.getXUidGenerator();
    }

    public void setXUidGenerator(XUidGenerator xUidGenerator) {
        engine.setXUidGenerator(xUidGenerator);
    }
}
