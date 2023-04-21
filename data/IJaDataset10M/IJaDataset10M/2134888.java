package test.org.mandarax.xkb;

import java.lang.reflect.Method;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.Prerequisite;
import org.mandarax.kernel.Query;
import org.mandarax.kernel.Rule;
import org.mandarax.kernel.SimplePredicate;
import org.mandarax.kernel.Term;
import org.mandarax.kernel.meta.DynaBeanFunction;
import org.mandarax.kernel.meta.JFunction;
import org.mandarax.kernel.meta.JPredicate;
import org.mandarax.sql.DefaultTypeMapping;
import org.mandarax.sql.OneColumnMapping;
import org.mandarax.sql.SQLClauseSet;
import org.mandarax.sql.SQLFunction;
import org.mandarax.sql.SQLPredicate;
import org.mandarax.util.LogicFactorySupport;
import org.mandarax.util.logging.LogCategories;

/**
 * Utility providing all kind of test data for XKB test cases.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.6
 */
public class TestData implements LogCategories {

    static LogicFactory lfactory = LogicFactory.getDefaultFactory();

    static LogicFactorySupport lfs = new LogicFactorySupport(lfactory);

    /**
     * Get a simple predicate associating Strings.
     * @return a simple predicate
     */
    public static SimplePredicate simplePredicate1() {
        Class[] struct = { String.class, String.class };
        SimplePredicate p = new SimplePredicate("is father of", struct);
        return p;
    }

    /**
     * Get a simple predicate associating beans (=instances of Person).
     * @return a simple predicate
     */
    public static SimplePredicate simplePredicate2() {
        Class[] struct = { Person.class, Person.class };
        SimplePredicate p = new SimplePredicate("is father of", struct);
        return p;
    }

    /**
     * Get a simple predicate associating Strings.
     * @return a simple predicate
     */
    public static SimplePredicate simplePredicate3() {
        Class[] struct = { Integer.class, Integer.class };
        SimplePredicate p = new SimplePredicate("<numeric dummy predicate>", struct);
        return p;
    }

    /**
     * Get a jpredicate
     * @return a jpredicate
     */
    public static JPredicate jPredicate() {
        try {
            Class clazz = Object.class;
            Class[] par = { clazz };
            Method m = clazz.getMethod("equals", par);
            JPredicate p = new JPredicate(m);
            p.setName("two objects are equal");
            return p;
        } catch (Exception x) {
            LOG_TEST.error("Error building jpredicate", x);
        }
        return null;
    }

    /**
     * Get a jfunction returning a string.
     * @return a jfunction
     */
    public static JFunction jFunction() {
        try {
            Class clazz = String.class;
            Class[] par = { clazz };
            Method m = clazz.getMethod("concat", par);
            JFunction f = new JFunction(m);
            f.setName("concatenate two strings");
            return f;
        } catch (Exception x) {
            LOG_TEST.error("Error building jpredicate", x);
        }
        return null;
    }

    /**
     * Get a dynabeanfunction returning a string.
     * @return a dyna bean function
     */
    public static DynaBeanFunction dynaBeanFunction() {
        try {
            Class clazz = String.class;
            Class[] par = { clazz };
            Method m = clazz.getMethod("concat", par);
            DynaBeanFunction f = new DynaBeanFunction(m, "an extension", "concat");
            f.setName("concatenate two strings");
            return f;
        } catch (Exception x) {
            LOG_TEST.error("Error building dyna bean predicate", x);
        }
        return null;
    }

    /**
     * Get a SQL function returning a string.
     * @return an sql function
     */
    public static SQLFunction sqlFunction1() {
        SQLFunction f = new SQLFunction();
        f.setDataSource(dataSource());
        f.setName("sample sql function");
        f.setQuery("SELECT NAME FROM PEOPLE WHERE ID = ?");
        Class[] struct = { Integer.class };
        f.setStructure(struct);
        f.setObjectRelationalMapping(new OneColumnMapping(String.class));
        return f;
    }

    /**
     * Get an SQL predicate associating two strings.
     * @return an sql predicate
     */
    public static SQLPredicate sqlPredicate() {
        SQLPredicate p = new SQLPredicate();
        p.setDataSource(dataSource());
        p.setName("sample sql predicate");
        p.setQuery("SELECT NAME,FATHER FROM FAMILY");
        Class[] struct = { String.class, String.class };
        p.setStructure(struct);
        p.setTypeMapping(new DefaultTypeMapping());
        return p;
    }

    /**
     * Get a data source.
     * @return a data source
     */
    public static DataSource dataSource() {
        DummyDataSource ds = new DummyDataSource();
        try {
            ds.setConnectString("jdbc:odbc:test");
            ds.setDriver(Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"));
            ds.setUserName("scott");
            ds.setPassword("tiger");
            ds.setLoginTimeout(5000);
        } catch (Exception x) {
            LOG_TEST.error("Cannot initialize dummy data source", x);
        }
        return ds;
    }

    /**
     * Get a simple rule.
     * @return a rule
     */
    public static Rule rule1() {
        return lfs.rule(prereq1(), fact1());
    }

    /**
     * Get a rule with two prerequisites connected by AND
     * @return a rule
     */
    public static Rule andRule() {
        Rule r = lfs.rule(prereq1(), prereq2(), fact3());
        r.setBodyOrConnected(false);
        return r;
    }

    /**
     * Get a rule with two prerequisites connected by AND
     * @return a rule
     */
    public static Rule orRule() {
        Rule r = lfs.rule(prereq1(), prereq2(), fact3());
        r.setBodyOrConnected(true);
        return r;
    }

    /**
     * Get a rule with a negated and an unnegated prerequisite connected by AND
     * @return a rule
     */
    public static Rule negRule() {
        Term[] terms = { lfs.variable("x", String.class), lfs.variable("y", String.class) };
        Prerequisite prereq1 = lfs.prereq(simplePredicate1(), terms, true);
        Prerequisite prereq2 = lfs.prereq(simplePredicate1(), terms, false);
        Fact concl = lfs.fact(simplePredicate1(), terms);
        Rule r = lfs.rule(prereq1, prereq2, concl);
        return r;
    }

    /**
     * Get a fact with a simple predicate and variable terms
     * @return a fact
     */
    public static Fact fact1() {
        Term[] terms = { lfs.variable("x", String.class), lfs.variable("y", String.class) };
        return lfs.fact(simplePredicate1(), terms);
    }

    /**
     * Get a fact with a simple predicate and variable terms
     * @return a fact
     */
    public static Fact fact2() {
        Term[] terms = { lfs.variable("a person", Person.class), lfs.variable("another person", Person.class) };
        return lfs.fact(simplePredicate2(), terms);
    }

    /**
     * Get a fact with a simple predicate and variable terms
     * @return a fact
     */
    public static Fact fact3() {
        Term[] terms = { lfs.variable("x", Integer.class), lfs.variable("y", Integer.class) };
        return lfs.fact(simplePredicate3(), terms);
    }

    /**
     * Get a prerequisite with a simple predicate and variable terms
     * @return a prerequisite
     */
    public static Prerequisite prereq1() {
        Term[] terms = { lfs.variable("x", String.class), lfs.variable("y", String.class) };
        return lfs.prereq(simplePredicate1(), terms);
    }

    /**
     * Get a prerequisite with a simple predicate and variable terms
     * @return a prerequisite
     */
    public static Prerequisite prereq2() {
        Term[] terms = { lfs.variable("a person", Person.class), lfs.variable("another person", Person.class) };
        return lfs.prereq(simplePredicate2(), terms);
    }

    /**
     * Get a prerequisite with a simple predicate and variable terms
     * @return a prerequisite
     */
    public static Prerequisite prereq3() {
        Term[] terms = { lfs.variable("x", Integer.class), lfs.variable("y", Integer.class) };
        return lfs.prereq(simplePredicate3(), terms);
    }

    /**
     * Get a fact with a simple predicate and constant terms
     * @return a fact
     */
    public static Fact fact4() {
        Term[] terms = { lfs.cons("Jens", String.class), lfs.cons("Max", String.class) };
        return lfs.fact(simplePredicate1(), terms);
    }

    /**
     * Get a fact with a simple predicate and constant terms
     * @return a fact
     */
    public static Fact fact5() {
        Person p1 = new Person(1, "Jens", 1966, 0, 12);
        Person p2 = new Person(2, "Max", 1993, 10, 10);
        Term[] terms = { lfs.cons(p1, Person.class), lfs.cons(p2, Person.class) };
        return lfs.fact(simplePredicate2(), terms);
    }

    /**
     * Get a fact with a simple predicate and constant terms
     * @return a fact
     */
    public static Fact fact6() {
        Term[] terms = { lfs.cons(new Integer(3), Integer.class), lfs.cons(new Integer(42), Integer.class) };
        return lfs.fact(simplePredicate3(), terms);
    }

    /**
     * Get a fact with a jpredicate and variable terms.
     * @return a fact
     */
    public static Fact fact7() {
        Term[] terms = { lfs.variable("x", Object.class), lfs.variable("y", Object.class) };
        return lfs.fact(jPredicate(), terms);
    }

    /**
     * Get a fact with a simple predicate, a jfunction and variable terms.
     * @return a fact
     */
    public static Fact fact8() {
        Term[] terms = { lfs.cplx(jFunction(), lfs.variable("x", String.class), lfs.variable("y", String.class)), lfs.variable("z", String.class) };
        return lfs.fact(simplePredicate1(), terms);
    }

    /**
     * Get a fact with a sql predicate and variable terms.
     * @return a fact
     */
    public static Fact fact9() {
        Term[] terms = { lfs.variable("x", String.class), lfs.variable("y", String.class) };
        return lfs.fact(sqlPredicate(), terms);
    }

    /**
     * Get a fact with a simple predicate, a sql function and variable terms.
     * @return a fact
     */
    public static Fact fact10() {
        Term[] terms = { lfs.cplx(sqlFunction1(), lfs.variable("x", Integer.class)), lfs.variable("z", String.class) };
        return lfs.fact(simplePredicate1(), terms);
    }

    /**
     * Get a fact with a simple predicate, a dyna bean function and variable terms.
     * @return a fact
     */
    public static Fact fact11() {
        Term[] terms = { lfs.cplx(dynaBeanFunction(), lfs.variable("x", String.class)), lfs.variable("z", String.class) };
        return lfs.fact(simplePredicate1(), terms);
    }

    /**
     * Get a SQL clause set.
     * @return a fact
     */
    public static SQLClauseSet sqlClauseSet() {
        return new SQLClauseSet(sqlPredicate(), "", 1000000);
    }

    /**
     * Get a query.
     * @return a query
     */
    public static Query query1() {
        return lfs.query(fact1(), "a query");
    }

    /**
     * Get a query.
     * @return a query
     */
    public static Query query2() {
        return lfs.query(fact1(), fact2(), "a query");
    }

    /**
     * Get a query.
     * @return a query
     */
    public static Query query3() {
        return lfs.query(fact3(), "a query");
    }
}
