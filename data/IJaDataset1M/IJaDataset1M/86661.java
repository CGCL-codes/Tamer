package com.bradrydzewski.tinyreport.model;

import com.bradrydzewski.tinyreport.jdbc.DataSet;
import com.bradrydzewski.tinyreport.jdbc.DataSourceFactory;
import com.bradrydzewski.tinyreport.jdbc.JdbcTemplate;
import com.bradrydzewski.tinyreport.jdbc.PreparedStatementCreatorImpl;
import com.bradrydzewski.tinyreport.jdbc.RowCallbackHandlerImpl;
import com.bradrydzewski.tinyreport.util.DataTypeConversionUtil;
import java.util.Map;
import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Brad Rydzewski
 */
@XmlRootElement
public class JdbcQuery extends DataQuery {

    private String sqlQuery;

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    @Override
    public DataSet execute(DataConnection dataConnection, Map<String, Parameter> params) {
        JdbcConnection conn = (JdbcConnection) dataConnection;
        DataSource ds = DataSourceFactory.createDataSource(conn.getDatabaseUrl(), conn.getDatabaseUser(), conn.getDatabasePassword(), conn.getDriverClass(), conn.getDatabaseJndiName());
        PreparedStatementCreatorImpl pstmt = new PreparedStatementCreatorImpl(getSqlQuery());
        for (JdbcParameter param : getParameters()) {
            Object reportParamValue = DataTypeConversionUtil.getCastedObjectToType(param.getType(), params.get(param.getReportParameter()));
            int sqlType = DataTypeConversionUtil.getSqlTypeFromDataTypeEnum(param.getType());
            pstmt.addParameter(param.getPosition(), reportParamValue, sqlType);
        }
        JdbcTemplate template = new JdbcTemplate(ds);
        DataSet results = template.query(pstmt, new RowCallbackHandlerImpl());
        return results;
    }
}
