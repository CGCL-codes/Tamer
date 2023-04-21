package fr.unice.gfarce.dao;

public class OracleDaoFactory extends DaoFactory {

    @Override
    public IdentiteDao getIdentiteDao() {
        return new OracleIdentiteDao();
    }

    @Override
    public FormationDao getFormationDao() {
        return new OracleFormationDao();
    }
}
