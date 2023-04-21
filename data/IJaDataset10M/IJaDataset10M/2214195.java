package net.simpleframework.core;

import javax.sql.DataSource;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IApplication {

    static final String defaultDataSourceName = "defaultDatasource";

    String getApplicationHomePath();

    String getApplicationAbsolutePath(String path);

    ApplicationConfig getApplicationConfig();

    DataSource getDataSource(String datasourceName);

    DataSource getDataSource();

    Object getBean(String key);
}
