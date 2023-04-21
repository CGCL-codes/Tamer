package ca.sqlpower.architect.ddl.critic.impl;

import ca.sqlpower.architect.ddl.OracleDDLGenerator;

public class OracleReservedWordsCritic extends ReservedWordsCritic {

    public OracleReservedWordsCritic() {
        super(StarterPlatformTypes.ORACLE.getName(), Messages.getString("OracleReservedWordsCritic.name"), OracleDDLGenerator.RESERVED_WORDS);
    }
}
