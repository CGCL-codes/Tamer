package databaseVersionControl.infra.script;

import java.sql.SQLException;
import databaseVersionControl.domain.control.AbstractScriptKey;
import databaseVersionControl.domain.script.ExecutableScript;
import databaseVersionControl.infra.connection.DataBaseProxy;
import databaseVersionControl.infra.exception.DVCGeneralException;

public class ScriptRunner {

    public static void install(AbstractScriptKey scriptKey, ExecutableScript script) {
        try {
            DataBaseProxy.install(script);
            registerExecutedKey(scriptKey);
        } catch (SQLException e) {
            throw new DVCGeneralException(String.format("Was not possible to install '%s'", scriptKey), e);
        }
    }

    public static void registerExecutedKey(AbstractScriptKey scriptKey) throws SQLException {
        if (scriptKey.isSystemKey()) return;
        String sql = "INSERT INTO INSTALLED_SCRIPTS (ID, CLASS_NAME, NUMBER) VALUES ( %d, '%s', %d )";
        DataBaseProxy.executeStatement(String.format(sql, DataBaseProxy.getNextId(), scriptKey.getDatabaseScriptKey(), scriptKey.getNumber()));
    }
}
