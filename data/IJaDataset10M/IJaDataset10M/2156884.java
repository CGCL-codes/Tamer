package org.osmius.dao.jdbc.storedprocedures;

import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

public class AckAllEvents extends StoredProcedure {

    public AckAllEvents(DataSource dataSource, String string) {
        super(dataSource, string);
        declareParameter(new SqlParameter("user", Types.VARCHAR));
        declareParameter(new SqlParameter("typInstance", Types.VARCHAR));
        declareParameter(new SqlParameter("idnInstance", Types.VARCHAR));
        declareParameter(new SqlOutParameter("message", Types.VARCHAR));
        compile();
    }

    public Map execute(Map parameters) {
        return super.execute(parameters);
    }
}
