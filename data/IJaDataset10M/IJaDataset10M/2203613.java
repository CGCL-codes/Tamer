package genericlink.destination.jdbc;

import genericlink.transformers.FieldTransformer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class DatabaseDestinationCallback implements PreparedStatementCallback {

    private Map data;

    private DatabaseSink sink;

    public DatabaseDestinationCallback(Map data, DatabaseSink sink) {
        this.data = data;
        this.sink = sink;
    }

    public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        Set paramOrderNumbers = sink.getParamOrder().keySet();
        for (Iterator iterator = paramOrderNumbers.iterator(); iterator.hasNext(); ) {
            String paramOrder = (String) iterator.next();
            String fieldName = (String) sink.getParamOrder().get(paramOrder);
            FieldTransformer transformer = (FieldTransformer) sink.getParamTransformers().get(paramOrder);
            transformer.transform(data, Integer.valueOf(paramOrder).intValue(), fieldName, ps);
        }
        ps.execute();
        return null;
    }
}
