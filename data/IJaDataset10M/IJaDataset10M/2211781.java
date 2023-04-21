package org.stars.dao.sqlmapper.macro;

import org.stars.dao.sqlmapper.SqlDefinition;
import org.stars.dao.sqlmapper.SqlMacro;
import org.stars.dao.sqlmapper.SqlMapper;

public interface Macro {

    ReturnState execute(SqlMapper sqlMapper, SqlDefinition sqlQuery, SqlMacro sqlMacro) throws Exception;
}
