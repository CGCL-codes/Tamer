package org.jxstar.dm.ddl;

import java.util.Map;
import org.jxstar.dao.util.DBTypeUtil;
import org.jxstar.dm.DmException;
import org.jxstar.dm.DmParser;
import org.jxstar.dm.util.DmUtil;
import org.jxstar.util.MapUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * ORACLE数据库配置模板解析类。
 *
 * @author TonyTan
 * @version 1.0, 2010-12-18
 */
public class OracleDmParser extends DmParser {

    public OracleDmParser() {
        super(DBTypeUtil.ORACLE);
    }

    /**
	 * 解析变量的值
	 * @param name -- 变量名
	 * @param mpData -- 解析用的数据
	 * @return
	 */
    protected String parseElement(String name, Map<String, String> mpData) throws DmException {
        String ret = null;
        if (name.equals("data_type")) {
            String dataType = MapUtil.getValue(mpData, "data_type");
            String dataSize = MapUtil.getValue(mpData, "data_size", "22");
            String dataScale = MapUtil.getValue(mpData, "data_scale", "0");
            ret = getDataType(dataType, dataSize, dataScale);
        } else if (name.equals("nullable") || name.equals("alert_nullable")) {
            String nullable = MapUtil.getValue(mpData, "nullable");
            if (nullable.equals("1")) {
                ret = "not null";
            } else {
                if (name.equals("nullable")) {
                    ret = "";
                } else {
                    ret = "null";
                }
            }
        } else if (name.equals("default_value") || name.equals("alert_default_value")) {
            String value = MapUtil.getValue(mpData, "default_value");
            String dataType = MapUtil.getValue(mpData, "data_type");
            if (value.length() > 0) {
                if (dataType.indexOf("char") < 0 || DmUtil.hasYinHao(value)) {
                    ret = "default " + value;
                } else {
                    ret = "default '" + value + "'";
                }
            } else {
                if (name.equals("default_value")) {
                    ret = "";
                } else {
                    ret = "default null";
                }
            }
        }
        return ret;
    }

    /**
	 * 构建数据类型语句
	 * @param dataType -- 数据类型，支持：number, char, varchar, date, blob 
	 * @param dataSize -- 数据长度
	 * @param dataScale -- 小数位
	 * @return
	 */
    protected String getDataType(String dataType, String dataSize, String dataScale) throws DmException {
        if (dataType == null || dataType.length() == 0) {
            throw new DmException(JsMessage.getValue("dmparser.typenull"));
        }
        StringBuilder sbret = new StringBuilder();
        if (dataType.equals("varchar")) {
            sbret.append("varchar2(").append(dataSize).append(")");
        } else if (dataType.equals("char")) {
            sbret.append("char(").append(dataSize).append(")");
        } else if (dataType.equals("date")) {
            sbret.append("date");
        } else if (dataType.equals("number")) {
            sbret.append("number(").append(dataSize).append(",").append(dataScale).append(")");
        } else if (dataType.equals("int")) {
            sbret.append("number(").append(dataSize).append(")");
        } else if (dataType.equals("blob")) {
            sbret.append("long raw");
        } else {
            sbret.append(dataType);
        }
        return sbret.toString();
    }
}
