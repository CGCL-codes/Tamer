package org.dbunit.database.search;

import com.gargoylesoftware.base.testing.EqualsTester;
import junit.framework.TestCase;

/**
 * @author gommma
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 826 $ $Date: 2008-10-05 13:42:03 -0400 (Sun, 05 Oct 2008) $
 * @since 2.4.0
 */
public class ForeignKeyRelationshipEdgeTest extends TestCase {

    public void testEqualsHashCode() {
        ForeignKeyRelationshipEdge e1 = new ForeignKeyRelationshipEdge("table1", "table2", "fk_col", "pk_col");
        ForeignKeyRelationshipEdge equal = new ForeignKeyRelationshipEdge("table1", "table2", "fk_col", "pk_col");
        ForeignKeyRelationshipEdge notEqual1 = new ForeignKeyRelationshipEdge("table1", "tableOther", "fk_col", "pk_col");
        ForeignKeyRelationshipEdge notEqual2 = new ForeignKeyRelationshipEdge("table1", "table2", "fk_col_other", "pk_col");
        ForeignKeyRelationshipEdge equalSubclass = new ForeignKeyRelationshipEdge("table1", "table2", "fk_col", "pk_col") {
        };
        new EqualsTester(e1, equal, notEqual1, equalSubclass);
        new EqualsTester(e1, equal, notEqual2, equalSubclass);
    }
}
