package com.mea.common.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.mlib.sql.SQLUtil;

/**
 * 处理WEB项目SQL拼接问题,根据SQL中各个参数的来源,去取参数值,如果没有发现参数则将此条件替换掉
 * 最后完成SQL拼接后,再利用预编译的SQL设置参数,在一定程度上防止SQL注入
 * 
 * @author zitee@163.com
 * @创建时间 2009-09-09 10:04:58
 * @version 1.01
 */
public class WebSQLUtil {

    /**
	 * 参数来源- HTTP请求参数
	 */
    public static final int SOURCE_PARAMETER = 1;

    /**
	 * 参数来源- REQUEST对象属性 getAttribute
	 */
    public static final int SOURCE_REQUEST = 2;

    /**
	 * 参数来源- SESSION对象属性 getAttribute
	 */
    public static final int SOURCE_SESSION = 3;

    /**
	 * 参数类型-整型
	 */
    public static final int TYPE_INT = 1;

    /**
	 * 参数类型-长整型
	 */
    public static final int TYPE_LONG = 2;

    /**
	 * 参数类型-字符串类型
	 */
    public static final int TYPE_STRING = 3;

    /**
	 * 参数类型-日期类型 yyyy-MM-dd
	 */
    public static final int TYPE_DATE = 4;

    /**
	 * 参数类型-全模糊查询
	 */
    public static final int TYPE_PARAMETER_STRING_ALLLIKE = 5;

    /**
	 * 参数类型-左模糊查询
	 */
    public static final int TYPE_LEFTLIKE = 6;

    /**
	 * 参数类型-右模糊查询
	 */
    public static final int TYPE_RIGHTLIKE = 7;

    /**
	 * 将无法获取参数值的条件替换成 1=1
	 */
    public static final String NOPARAMETER_REPLACE = "1=1";

    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
	 * 处理SQL语句中参数问题,并且返回PreparedStatement直接执行可以得到ResultSet
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            原始SQL语句
	 * @param parameterSource
	 *            参数来源
	 * @param parameterNames
	 *            二维数组 sql字段名,参数名,参数类型,参数来源
	 * @param request
	 *            HTTP 请求对象
	 * @param noParameter
	 *            无法找到参数时的处理
	 * @return
	 * @throws ParseException
	 *             日期转换异常
	 * @throws SQLException
	 */
    public static PreparedStatement generateStatement(Connection conn, String sql, Object[][] parameters, HttpServletRequest request, String noParameter) throws ParseException, SQLException {
        List<SQLBean> beans = new ArrayList<SQLBean>();
        for (Object[] o : parameters) {
            SQLBean bean = new SQLBean((String) o[0], (String) o[1], (Integer) o[2], (Integer) o[3]);
            beans.add(bean);
        }
        sql = WebSQLUtil.generateSql(sql, beans, request, noParameter);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int index = 1;
        for (int i = 0; i < beans.size(); i++) {
            SQLBean bean = beans.get(i);
            if (bean.isFind()) {
                SQLUtil.setSQLParameterValue(preparedStatement, bean.getValue(), index++);
            }
        }
        return preparedStatement;
    }

    /**
	 * 查找给定的参数中哪些没有 并做标记
	 * 
	 * @param sql
	 * @param parameterSource
	 * @param parameterNames
	 * @param request
	 * @param noParameter
	 * @return
	 * @throws ParseException
	 */
    public static String generateSql(String sql, int parameterSource, Object[][] parameterNames, HttpServletRequest request, String noParameter) throws ParseException {
        for (Object[] parameterNamearr : parameterNames) {
            String parameterValue;
            String parameterName = parameterNamearr[1].toString();
            switch(parameterSource) {
                case SOURCE_PARAMETER:
                    parameterValue = request.getParameter(parameterName);
                    break;
                case SOURCE_REQUEST:
                    parameterValue = (String) request.getAttribute(parameterName);
                    break;
                case SOURCE_SESSION:
                    parameterValue = (String) request.getSession().getAttribute(parameterName);
                    break;
                default:
                    parameterValue = request.getParameter(parameterName);
                    break;
            }
            if (parameterValue == null) {
                sql = replaceNoParameter(sql, parameterNamearr[0].toString(), noParameter);
                parameterNamearr[0] = null;
            } else {
                parameterNamearr[2] = generateParameterValue(parameterValue, (Integer) parameterNamearr[2]);
            }
        }
        return sql;
    }

    /**
	 * 查找给定的参数中哪些没有 并做标记
	 * 
	 * @param sql
	 * @param beans
	 * @param request
	 * @param noParameter
	 * @return
	 * @throws ParseException
	 */
    public static String generateSql(String sql, List<SQLBean> beans, HttpServletRequest request, String noParameter) throws ParseException {
        for (SQLBean bean : beans) {
            String parameterValue = null;
            switch(bean.getParaSource()) {
                case SOURCE_PARAMETER:
                    parameterValue = request.getParameter(bean.getParaName());
                    break;
                case SOURCE_REQUEST:
                    parameterValue = (String) request.getAttribute(bean.getParaName());
                    break;
                case SOURCE_SESSION:
                    parameterValue = (String) request.getSession().getAttribute(bean.getParaName());
                    break;
                default:
                    parameterValue = request.getParameter(bean.getParaName());
                    break;
            }
            if (parameterValue == null) {
                sql = replaceNoParameter(sql, bean.getSqlName(), noParameter);
                bean.setFind(false);
            } else {
                bean.setValue(generateParameterValue(parameterValue, bean.getParaType()));
                bean.setFind(true);
            }
        }
        return sql;
    }

    /**
	 * 获取参数字符串值 并转换成对应的类型
	 * 
	 * @param parameterValue
	 * @param valueType
	 * @return
	 * @throws ParseException
	 */
    private static Object generateParameterValue(String parameterValue, int valueType) throws ParseException {
        Object value = null;
        switch(valueType) {
            case TYPE_INT:
                value = Integer.parseInt(parameterValue);
                break;
            case TYPE_LONG:
                value = Long.parseLong(parameterValue);
                break;
            case TYPE_STRING:
                value = parameterValue;
                break;
            case TYPE_DATE:
                value = DATA_FORMAT.parse(parameterValue);
                break;
            case TYPE_PARAMETER_STRING_ALLLIKE:
                value = "%" + parameterValue + "%";
                break;
            case TYPE_LEFTLIKE:
                value = "%" + parameterValue;
                break;
            case TYPE_RIGHTLIKE:
                value = parameterValue + "%";
                break;
            default:
                value = parameterValue;
        }
        return value;
    }

    /**
	 * 替换空值参数
	 * 
	 * @param sql
	 * @param sqlCode
	 * @param noParameter
	 * @return
	 */
    private static String replaceNoParameter(String sql, String sqlCode, String noParameter) {
        String replaceString = sqlCode + "[ =!<>%(like)]*\\?";
        return sql.replaceAll(replaceString, noParameter);
    }
}
