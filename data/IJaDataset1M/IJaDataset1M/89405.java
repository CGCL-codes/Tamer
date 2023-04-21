package edu.pku.sei.pgie.persistence.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import edu.pku.sei.pgie.beans.CodeEntity;

/**
 * @author HeLi
 */
public class EntityDAO extends DAOStub {

    public EntityDAO() {
        super();
    }

    public CodeEntity createEntity(CodeEntity entity) throws SQLException {
        if (entity == null) return null;
        QueryRunner runner = new QueryRunner(dataSource);
        Long uid = generateUID();
        runner.update("insert into ENTITY(CODEID,FQN,ENTITYTYPE,RANK,CONTEXT,LOCATION)\n" + "values(?,?,?,?,?,?)", new Object[] { uid, entity.getFqn(), entity.getEntityType(), entity.getRank(), entity.getContext(), entity.getLocation() });
        entity.setCodeId(uid);
        return entity;
    }

    public void deleteEntity(CodeEntity entity) throws SQLException {
        if (entity == null) return;
        QueryRunner runner = new QueryRunner(dataSource);
        runner.update("delete from ENTITY where CODEID = ?", new Object[] { entity.getCodeId() });
    }

    public void updateEntity(CodeEntity entity) throws SQLException {
        if (entity == null) return;
        QueryRunner runner = new QueryRunner(dataSource);
        runner.update("update ENTITY\n" + "set FQN = ?,ENTITYTYPE= ?,RANK = ?,CONTEXT = ?,LOCATION = ?\n" + "where CODEID = ?", new Object[] { entity.getFqn(), entity.getEntityType(), entity.getRank(), entity.getContext(), entity.getLocation(), entity.getCodeId() });
    }

    public List loadEntities() throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        Object result = runner.query("select * from ENTITY", new BeanListHandler(CodeEntity.class));
        return (List) result;
    }

    public CodeEntity loadEntity(Long id) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        Object result = runner.query("select * from ENTITY where CODEID = ?", id, new BeanHandler(CodeEntity.class));
        return (CodeEntity) result;
    }

    public CodeEntity LoadEntityByFqn(String fqn) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        Object result = runner.query("select * from ENTITY where FQN = ?", fqn, new BeanHandler(CodeEntity.class));
        return (CodeEntity) result;
    }
}
