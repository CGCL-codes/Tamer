package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Stefan Christians 
 *  @version    $Id: DBRes_ja.java,v 1.2 2006/07/30 00:55:13 jjanke Exp $
 */
public class DBRes_ja extends ListResourceBundle {

    /** Data        */
    static final Object[][] contents = new String[][] { { "CConnectionDialog", "アデンピエーレの接続" }, { "Name", "名前" }, { "AppsHost", "アプリケーション・サーバ" }, { "AppsPort", "アプリケーション・ポート" }, { "TestApps", "アプリケーション・サーバのテスト" }, { "DBHost", "データベース・サーバ" }, { "DBPort", "データベース・ポート" }, { "DBName", "データベースの名前" }, { "DBUidPwd", "ユーザ / パスワード" }, { "ViaFirewall", "ファイアウォール" }, { "FWHost", "ファイアウォール・サーバ" }, { "FWPort", "ファイアウォール・ポート" }, { "TestConnection", "データベース・サーバのテスト" }, { "Type", "データベース" }, { "BequeathConnection", "継承接続" }, { "Overwrite", "オーバーライド" }, { "ConnectionProfile", "Connection" }, { "LAN", "LAN" }, { "TerminalServer", "Terminal Server" }, { "VPN", "VPN" }, { "WAN", "WAN" }, { "ConnectionError", "接続エラー" }, { "ServerNotActive", "サーバーが見つかりません" } };

    /**
	 * Get Contsnts
	 * @return contents
	 */
    public Object[][] getContents() {
        return contents;
    }
}
