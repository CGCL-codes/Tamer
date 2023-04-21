package com.dqgen.criteria.criterion;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import java.lang.reflect.Constructor;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dqgen.criteria.CriteriaQuery;
import com.dqgen.criteria.CriteriaQueryFactory;
import com.dqgen.criteria.CriteriaQueryTranslator;
import com.dqgen.criteria.From;
import com.dqgen.criteria.Root;
import com.dqgen.criteria.SQLCriteriaQueryTranslator;
import com.dqgen.metamodel.MetaModels;

/**
 * @author ganesh
 *
 */
public class ArithmeticPropertyProjectionTest {

    private static Root root;

    private static CriteriaQueryTranslator translator;

    @BeforeClass
    public static void init() throws Exception {
        CriteriaQuery query = CriteriaQueryFactory.getInstance().createCriteriaQuery(MetaModels.account, "account", null);
        root = query.getRoot();
        Class<SQLCriteriaQueryTranslator> transClass = SQLCriteriaQueryTranslator.class;
        Constructor<SQLCriteriaQueryTranslator> cons = transClass.getDeclaredConstructor(CriteriaQuery.class);
        cons.setAccessible(true);
        translator = cons.newInstance(query);
    }

    @Test
    public void testArithmeticPropertyAdd() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.ADD);
        String sql = projection.toSQLString(0, translator, false);
        assertEquals("The SQL generated is not valid", "account.initial_balance+account.initial_balance".toUpperCase(), sql.toUpperCase());
    }

    @Test
    public void testArithmeticPropertySub() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.SUB);
        String sql = projection.toSQLString(0, translator, false);
        assertEquals("The SQL generated is not valid", "account.initial_balance-account.initial_balance".toUpperCase(), sql.toUpperCase());
    }

    @Test
    public void testArithmeticPropertyProd() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.PROD);
        String sql = projection.toSQLString(0, translator, false);
        assertEquals("The SQL generated is not valid", "account.initial_balance*account.initial_balance".toUpperCase(), sql.toUpperCase());
    }

    @Test
    public void testArithmeticPropertyDiv() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.DIV);
        String sql = projection.toSQLString(0, translator, false);
        assertEquals("The SQL generated is not valid", "account.initial_balance/account.initial_balance".toUpperCase(), sql.toUpperCase());
    }

    @Test
    public void testArithmeticPropertyMod() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.MOD);
        String sql = projection.toSQLString(0, translator, false);
        assertEquals("The SQL generated is not valid", "account.initial_balance%account.initial_balance".toUpperCase(), sql.toUpperCase());
    }

    @Test
    public void testGetTables() {
        ArithmeticPropertyProjection projection = new ArithmeticPropertyProjection(root.get(MetaModels.account.initialBalance), root.get(MetaModels.account.initialBalance), ArithmeticOperator.MOD);
        List<From> tables = projection.getTables(translator);
        assertTrue(tables.size() == 2);
        assertEquals(root, tables.get(0));
        assertEquals(root, tables.get(1));
    }
}
