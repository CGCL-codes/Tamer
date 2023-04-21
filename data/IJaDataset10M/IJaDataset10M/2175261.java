package org.ujorm.orm.inheritance.sample;

import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Db;
import org.ujorm.orm.dialect.*;
import org.ujorm.orm.inheritance.sample.bo.Customer;
import org.ujorm.orm.inheritance.sample.bo.User;

/**
 * An table mapping to a database (a sample of usage).
 * @hidden
 */
@Db(schema = "db1", dialect = H2Dialect.class, user = "sa", password = "", jdbcUrl = "jdbc:h2:mem:db1")
public class Database extends OrmTable<Database> {

    /** User */
    public static final RelationToMany<Database, User> users = newRelation(User.class);

    /** Customer extends the User */
    public static final RelationToMany<Database, Customer> customers = newRelation(Customer.class);
}
