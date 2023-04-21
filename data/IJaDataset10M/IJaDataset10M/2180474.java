package com.shine.plugins.CmsDBdao;

import java.sql.ResultSet;
import com.shine.DBUtil.DBUtil;
import com.shine.Plugin.Plugin;
import com.shine.Plugin.util.PluginTypes;
import com.shine.platform.interfaces.DBDaoIf;

/**
 * cms dao插件
 * 
 * @author viruscodecn@gmail.com
 * 
 */
public class CmsDBdaoPlugin extends Plugin implements DBDaoIf {

    private String cmsDBConfigPath = null;

    public CmsDBdaoPlugin() {
        this.initPlugin("CmsDBdao", "1.0", PluginTypes.CLASSPATH, "", "CmsDBdao");
    }

    @Override
    protected void pluginDestroy() {
    }

    @Override
    protected void pluginSleep() {
    }

    @Override
    protected void pluginStart() {
        this.cmsDBConfigPath = this.getClass().getResource("config/DBdaoConfig.xml").getPath().replace("%20", " ");
        DBUtil.getInstance().init(this.cmsDBConfigPath);
    }

    public ResultSet executeQuery(String jndi, String sql) {
        return DBUtil.getInstance().executeQuery(sql).getRs();
    }

    public int executeUpdate(String jndi, String sql) {
        return DBUtil.getInstance().executeUpdate(sql);
    }

    public Plugin getPlugin() {
        return this;
    }

    public String pluginStatus() {
        return this.pluginStatus();
    }
}
