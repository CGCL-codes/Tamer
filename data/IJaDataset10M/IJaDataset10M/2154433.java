package org.stars.dao.sqlmapper.parser;

import java.util.List;
import org.stars.dao.sqlmapper.SqlBase;
import org.stars.dao.sqlmapper.SqlDefinition;
import org.stars.dao.sqlmapper.SqlFragment;
import org.stars.dao.sqlmapper.SqlMacro;
import org.stars.dao.sqlmapper.SqlMapper;
import org.stars.dao.sqlmapper.macro.MacroEvaluation;
import org.stars.dao.sqlmapper.macro.MacroExecutor;

/**
 * Executes the macro with source scope.
 * 
 * @author Francesco Benincasa
 *
 */
public class SubParser05_MacroExecuter {

    /**
	 * Execute macros with SOURCE evaluation
	 *
	 * @param item the item
	 * @throws Exception 
	 */
    public static void execute(MacroEvaluation when, SqlDefinition sql, SqlBase part) throws Exception {
        SqlMapper sqlMapper = sql.getSqlMapper();
        execute(when, sqlMapper, sql, null, part.getParts());
    }

    public static void execute(MacroEvaluation when, SqlMapper sqlMapper, SqlBase principal, SqlMacro parentMacro, List<SqlFragment> parts) throws Exception {
        for (SqlFragment item : parts) {
            if (item instanceof SqlMacro) {
                MacroExecutor.execute(when, sqlMapper, (SqlDefinition) principal, parentMacro, (SqlMacro) item);
                execute(when, sqlMapper, principal, (SqlMacro) item, item.getParts());
            }
        }
    }
}
