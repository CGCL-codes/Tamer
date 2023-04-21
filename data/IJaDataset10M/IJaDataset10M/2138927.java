package dbgate.ermanagement.support.persistant.constraint;

import dbgate.DBColumnType;
import dbgate.ermanagement.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Sep 26, 2010
 * Time: 11:47:59 AM
 */
@DBTableInfo(tableName = "constraint_test_root")
public class ConstraintTestDeleteRestrictRootEntity extends DefaultServerDBClass {

    @DBColumnInfo(columnType = DBColumnType.INTEGER, key = true)
    private int idCol;

    @DBColumnInfo(columnType = DBColumnType.VARCHAR)
    private String name;

    @ForeignKeyInfo(name = "fk_root2one2manyent", relatedObjectType = ConstraintTestOne2ManyEntity.class, columnMappings = { @ForeignKeyColumnMapping(fromField = "idCol", toField = "idCol") }, updateRule = ReferentialRuleType.RESTRICT, deleteRule = ReferentialRuleType.RESTRICT)
    private Collection<ConstraintTestOne2ManyEntity> one2ManyEntities;

    @ForeignKeyInfo(name = "fk_root2one2oneent", relatedObjectType = ConstraintTestOne2OneEntity.class, columnMappings = { @ForeignKeyColumnMapping(fromField = "idCol", toField = "idCol") }, updateRule = ReferentialRuleType.RESTRICT, deleteRule = ReferentialRuleType.RESTRICT)
    private ConstraintTestOne2OneEntity one2OneEntity;

    public ConstraintTestDeleteRestrictRootEntity() {
        one2ManyEntities = new ArrayList<ConstraintTestOne2ManyEntity>();
    }

    public int getIdCol() {
        return idCol;
    }

    public void setIdCol(int idCol) {
        this.idCol = idCol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ConstraintTestOne2ManyEntity> getOne2ManyEntities() {
        return one2ManyEntities;
    }

    public void setOne2ManyEntities(Collection<ConstraintTestOne2ManyEntity> one2ManyEntities) {
        this.one2ManyEntities = one2ManyEntities;
    }

    public ConstraintTestOne2OneEntity getOne2OneEntity() {
        return one2OneEntity;
    }

    public void setOne2OneEntity(ConstraintTestOne2OneEntity one2OneEntity) {
        this.one2OneEntity = one2OneEntity;
    }
}
