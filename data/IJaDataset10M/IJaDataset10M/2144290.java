package org.jxstar.service.util;

import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jxstar.dao.util.DBTypeUtil;
import org.jxstar.util.DateUtil;
import org.jxstar.util.StringValidator;

/**
 * 任务相关信息解析类，主要用于工作流的任务解析与上报组件。
 *
 * @author TonyTan
 * @version 1.0, 2011-1-29
 */
public class TaskUtil {

    /**
	 * 解析任务受限时间值，返回受限时间。
	 * @param limitValue -- 受限时间值，格式hh:mm 表示多数小时+分钟数
	 * @return
	 */
    public static String parseLimitDate(String limitValue) {
        String limitDate = "";
        if (limitValue == null || limitValue.length() == 0) return limitDate;
        String regex = "([0-9]+):([0-9]|[0-5][0-9])";
        Pattern p = Pattern.compile(regex);
        if (!p.matcher(limitValue).matches()) return limitDate;
        String[] hours = limitValue.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hours[0]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(hours[1]));
        return DateUtil.calendarToDateTime(calendar);
    }

    /**
	 * 解析字符串中的[字段名]，如：
	 * “您有编号为[card_code]的卡” 解析为：“您有编号为[2011030001]的卡”
	 * “[dept_id] like '1001%'” 解析为：“'10010001' like '1001%'”
	 * 
	 * @param strValue -- 需要解析的字符串
	 * @param mpData -- 解析数据来源
	 * @param isSql -- 是否为SQL语句，如果是，则解析参数后需要添加''
	 * @return
	 */
    public static String parseAppField(String taskDesc, Map<String, String> appData, boolean isSql) {
        Pattern p = Pattern.compile("\\[[^]]+\\]");
        Matcher m = p.matcher(taskDesc);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String name = m.group();
            name = name.substring(1, name.length() - 1);
            String value = appData.get(name);
            if (value == null) {
                value = "[no field]";
            } else {
                if (isSql) value = addChar(value);
            }
            m.appendReplacement(sb, value);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
	 * 字符串两头加上'
	 * @param str
	 * @return
	 */
    private static String addChar(String str) {
        if (DBTypeUtil.SQLSERVER.equals(DBTypeUtil.getDbmsType())) {
            if (str.indexOf(".") >= 0 && StringValidator.validValue(str, StringValidator.DOUBLE_TYPE)) return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("'").append(str).append("'");
        return sb.toString();
    }
}
