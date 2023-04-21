package com.brilliance.utils;

import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sss.dbutils.AbstractDbUtils;
import org.sss.dbutils.CounterUtils;
import org.sss.el.FunctionMapper;

/**
 * OIT记录生成工具
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 604 $ $Date: 2009-11-16 08:12:11 -0500 (Mon, 16 Nov 2009) $
 */
public final class OITUtils extends AbstractDbUtils {

    static final Log log = LogFactory.getLog(OITUtils.class);

    @Override
    public final String getName() {
        return "OIT";
    }

    @Override
    public final boolean prepareValues(String[] values) throws SQLException, ClassNotFoundException, IOException {
        if (values[4] != null) values[4] = values[4].trim();
        if ("".equals(values[4])) values[4] = null;
        if (values[4] == null) return false;
        if (values[5] != null) values[5] = values[5].trim();
        if ("".equals(values[5])) values[5] = null;
        if (values[5] == null) {
            if (!"9".equals(values[4])) return false;
            values[5] = "By SSTF5.01";
        }
        if ("9".equals(values[4])) values[3] = "STP";
        if ("INF".equals(values[3])) values[4] = "0";
        values[5] = "<?xml version=\"1.0\"?>\n<tdfmt sel-start=\"" + (values[5].length() + 1) + "\">" + values[5] + "</tdfmt>\n";
        values[0] = FunctionMapper.str8(CounterUtils.getCounter("OIT"));
        return true;
    }
}
