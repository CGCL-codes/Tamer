package architecture.common.adaptor.connector.jdbc;

import java.util.Collections;
import java.util.List;
import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.util.StringUtils;

public class JdbcReadConnector extends AbstractJdbcConnector implements ReadConnector {

    @SuppressWarnings("unchecked")
    public Object pull(Context context) {
        log.debug("pull : " + context);
        String queryString = context.getObject("queryString", String.class);
        if (StringUtils.isEmpty(queryString)) {
            String queryName = context.getObject("queryName", String.class);
            if (!StringUtils.isEmpty(queryName)) {
                queryString = getQueryString(queryName);
            } else {
            }
        }
        List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);
        Object[] data = context.getObject("data", Object[].class);
        if (parameterMappings == null) {
            parameterMappings = Collections.EMPTY_LIST;
        }
        if (data == null) {
            data = new Object[0];
        }
        Object output;
        return pull(queryString, parameterMappings, data);
    }

    @Override
    protected String getQueryString(String key) {
        return null;
    }
}
