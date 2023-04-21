package org.fto.jthink.jdbc;

import java.sql.*;
import java.sql.DriverManager;
import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.jdom.Element;

/**
 * 数据库连接工厂,用于创建java.sql.Connection数据库连接。
 * 此工厂通过JDBC数据库驱动直接创建数据库连接, 如果采用此工厂创建数据库连接, 
 * 建议采用连接池来管理连接,在配置文件fto-jthink.xml中可直接设置。
 * 
 * <pre><code>
 * 要使用此工厂,必须要在配置文件中正确配置相关连接信息，以下是在fto-jthink.xml文件中的配置信息
 * 用于连接到Mysql数据库。
 * 
			&lt;connection-factory&gt;
				<blockquote>
				&lt;!-- 工厂类定义 --&gt;
				&lt;factory-class&gt;org.fto.jthink.jdbc.DirectConnectionFactory&lt;/factory-class&gt;
				&lt;!-- driver-class --&gt;
				&lt;driver-class&gt;org.gjt.mm.mysql.Driver&lt;/driver-class&gt;
				&lt;!-- connection-url --&gt;
				&lt;url&gt;jdbc:mysql://localhost:3306/sample?useUnicode=true&amp;characterEncoding=UTF-8&lt;/url&gt;
				&lt;!-- user-name --&gt;
				&lt;user-name&gt;root&lt;/user-name&gt;
				&lt;!-- password --&gt;
				&lt;password&gt;&lt;/password&gt;
				</blockquote>
			&lt;/connection-factory&gt;

 * </code></pre>
 * 
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-16  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */
public final class DirectConnectionFactory implements ConnectionFactory {

    private String driver = null;

    private String url = null;

    private String user = null;

    private String password = null;

    /**
	 * 创建DirectConnectionFactory的实例
	 * 
	 * @param connCfg 连接配置信息
	 */
    public DirectConnectionFactory(Element connCfg) {
        Element connFctyCfg = connCfg.getChild("connection-factory");
        this.driver = connFctyCfg.getChildText("driver-class");
        this.url = connFctyCfg.getChildText("url");
        this.user = connFctyCfg.getChildText("user-name");
        this.password = connFctyCfg.getChildText("password");
    }

    /**
   * 建立数据库连接(Connection)
   * 
   * @return 数据库连接(Connection)
   * 
   */
    public Connection create() {
        try {
            Class.forName(driver);
            if (user == null || user.trim().length() == 0) {
                return DriverManager.getConnection(url);
            } else {
                return DriverManager.getConnection(url, user, password);
            }
        } catch (Exception ex) {
            throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_DB_CANNOT_GET_CONNECTION, "Cannot get connection!  URL = " + url, ex);
        }
    }
}
