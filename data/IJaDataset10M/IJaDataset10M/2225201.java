package cc.w3d.jawos.jinn.xjawosdata.xjawosdata.engines.xeMysqlJawosData.configurationManager;

import cc.w3d.jawos.jinn.xdata.xdata.engines.xeMysqlData.configurationManager.XEMysqlDataConfigurator;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.engines.xeMysqlJawosData.XEMysqlJawosData.EngineHandler;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.XUidGenerator;

public class XEMysqlJawosDataConfigurator extends XEMysqlDataConfigurator {

    private EngineHandler engine;

    public XEMysqlJawosDataConfigurator(EngineHandler engineHandler) {
        super(engineHandler);
        engine = engineHandler;
    }

    public XUidGenerator getXUidGenerator() {
        return engine.getXUidGenerator();
    }

    public void setXUidGenerator(XUidGenerator xUidGenerator) {
        engine.setXUidGenerator(xUidGenerator);
    }
}
