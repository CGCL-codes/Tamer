package dbgate.ermanagement.support.persistant.inheritancetest;

import dbgate.DBClassStatus;
import dbgate.ermanagement.context.IEntityContext;
import dbgate.ermanagement.support.persistant.treetest.ITreeTestOne2OneEntity;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Sep 26, 2010
 * Time: 12:23:11 PM
 */
public abstract class InheritanceTestSuperEntityExt implements ITreeTestOne2OneEntity {

    private DBClassStatus status;

    private int idCol;

    private String name;

    public InheritanceTestSuperEntityExt() {
        status = DBClassStatus.NEW;
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

    public DBClassStatus getStatus() {
        return status;
    }

    public void setStatus(DBClassStatus status) {
        this.status = status;
    }

    public IEntityContext getContext() {
        return null;
    }
}
