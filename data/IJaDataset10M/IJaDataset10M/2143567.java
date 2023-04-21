package Absyn;

import java.io.Serializable;

/**
 * @author MaYunlei
 *
 */
public class FromClause implements Serializable {

    public TableRefList tablereflist;

    public FromClause(TableRefList trl) {
        tablereflist = trl;
    }
}
