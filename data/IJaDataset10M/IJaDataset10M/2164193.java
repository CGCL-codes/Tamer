package org.jxstar.dm.ddl;

import java.util.List;
import java.util.Map;
import org.jxstar.dm.DdlIndex;
import org.jxstar.dm.DdlTable;
import org.jxstar.dm.DmException;
import org.jxstar.dm.util.DmConfig;
import org.jxstar.dm.util.DmUtil;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * Mysql表对象管理类。
 * 
 * @author TonyTan
 * @version 1.0, 2010-12-18
 */
public class MysqlDdlTable extends DdlTable {

    /**
	 * 构建表对象
	 */
    public MysqlDdlTable() {
        init();
    }

    /**
	 * 初始化全局对象
	 */
    public void init() {
        _parser = new MysqlDmParser();
        _fieldObj = new MysqlDdlField(_parser);
        _indexObj = new DdlIndex(_parser);
    }

    /**
	 * 构建生成表对象的SQL，MySQL数据库中不需要单独构建描述信息SQL
	 * @param tableId -- 表配置ID
	 * @return
	 * @throws DmException
	 */
    public List<String> getCreateSql(String tableId) throws DmException {
        Map<String, String> mpTable = DmConfig.getTableCfg(tableId);
        if (mpTable.isEmpty()) {
            throw new DmException(JsMessage.getValue("ddltable.tablenull"), tableId);
        }
        _dsname = mpTable.get("ds_name");
        String tableName = mpTable.get("table_name");
        if (DmUtil.existTable(tableName, _dsname)) {
            throw new DmException(JsMessage.getValue("ddltable.hasdbtable"), tableName);
        }
        List<String> lssql = FactoryUtil.newList();
        lssql.add(buildTable(mpTable));
        lssql.add(_indexObj.buildKey(mpTable));
        lssql.addAll(_indexObj.buildIndexs(mpTable));
        _log.showDebug("create table sql:" + lssql.toString());
        return lssql;
    }
}
