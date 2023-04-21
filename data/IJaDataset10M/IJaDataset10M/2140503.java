package com.liferay.portal.upgrade.v4_3_0.util;

import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.util.dao.DataAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="WebIdUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WebIdUtil {

    public static String[] getWebIds() throws Exception {
        if (_webIds != null) {
            return _webIds;
        }
        List webIds = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = HibernateUtil.getConnection();
            ps = con.prepareStatement(_GET_WEB_IDS);
            rs = ps.executeQuery();
            while (rs.next()) {
                String companyId = rs.getString("companyId");
                webIds.add(companyId);
            }
        } finally {
            DataAccess.cleanUp(con, ps, rs);
        }
        _webIds = (String[]) webIds.toArray(new String[0]);
        return _webIds;
    }

    private static final String _GET_WEB_IDS = "select companyId from Company";

    private static String[] _webIds;
}
