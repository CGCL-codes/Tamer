package org.sqlexp.enablement.mysql;

import org.sqlexp.enablement.Enablement;

/**
 * MySQL specific enablement.
 * @author Matthieu Réjou
 */
public class MysqlEnablement extends Enablement {

    /** */
    public MysqlEnablement() {
        super(new MysqlConnectionDefinition(), new MysqlLanguageStructure(), new MysqlSyntaxDefinitionFactory(), new MysqlDataManipulationFactory(), new MysqlDataDefinitionFactory(), new MysqlServerStructureReader(), new MysqlServerStructureWriter(), new MysqlQueryExecutor());
    }
}
