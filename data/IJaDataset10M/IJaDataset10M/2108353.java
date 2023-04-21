package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Bui Chi Trung
 *  @version    $Id: DBRes_vi.java,v 1.2 2006/07/30 00:55:13 jjanke Exp $
 */
public class DBRes_vi extends ListResourceBundle {

    /** Data        */
    static final Object[][] contents = new String[][] { { "CConnectionDialog", "Kết nối" }, { "Name", "T�n" }, { "AppsHost", "M�y chủ ứng dụng" }, { "AppsPort", "Cổng ứng dụng" }, { "TestApps", "Thử nghiệm ứng dụng" }, { "DBHost", "M�y chủ CSDL" }, { "DBPort", "Cổng CSDL" }, { "DBName", "T�n CSDL" }, { "DBUidPwd", "Người d�ng / Mật khẩu" }, { "ViaFirewall", "Qua bức tường lửa" }, { "FWHost", "M�y chủ bức tường lửa" }, { "FWPort", "Cổng v�o bức tường lửa" }, { "TestConnection", "Kiểm tra CSDL" }, { "Type", "Loại CSDL" }, { "BequeathConnection", "Truyền lại kết nối" }, { "Overwrite", "Ghi đ�" }, { "ConnectionProfile", "Connection" }, { "LAN", "LAN" }, { "TerminalServer", "Terminal Server" }, { "VPN", "VPN" }, { "WAN", "WAN" }, { "ConnectionError", "Lỗi kết nối" }, { "ServerNotActive", "M�y chủ hiện kh�ng hoạt động" } };

    /**
	 * Get Contsnts
	 * @return contents
	 */
    public Object[][] getContents() {
        return contents;
    }
}
