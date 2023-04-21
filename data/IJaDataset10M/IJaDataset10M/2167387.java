package org.jxstar.db;

import java.util.Map;
import org.jxstar.dao.DaoParam;
import org.jxstar.test.base.TestBase;

/**
 * @author bingco
 *
 * 执行正确的SQL，测试连接池是否存在泄漏。
 */
public class TestConnPool extends TestBase {

    /**
	 * 执行一次更新一次查询SQL
	 * @return
	 */
    protected boolean exeTest() {
        String sValue = "DongHong";
        DaoParam param = _dao.createParam();
        param.addStringValue(sValue).addStringValue("EAM2004511");
        boolean bret = _dao.update(param);
        if (!bret) {
            _log.showDebug("=========TestConnPool.exeSQL 测试更新失败！");
            return false;
        }
        String sql1 = "select device_type from doss_card where devicecard_id = ?";
        DaoParam param1 = _dao.createParam(sql1);
        param1.addStringValue("EAM2004511").setDsName("default");
        Map<String, String> mp = _dao.queryMap(param1);
        if (mp == null || mp.isEmpty()) {
            _log.showDebug("=========TestConnPool.exeSQL 测试查询失败！");
            return false;
        }
        String sType = (String) mp.get("device_type");
        if (!sType.equals(sValue)) {
            _log.showDebug("=========TestConnPool.exeSQL 检查更新的值不正确！");
            return false;
        }
        return true;
    }
}
