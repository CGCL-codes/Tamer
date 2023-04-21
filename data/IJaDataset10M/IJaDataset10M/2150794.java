package org.ujoframework.orm_tutorial.sample;

import org.ujoframework.extensions.Property;
import org.ujoframework.orm.annot.Table;
import org.ujoframework.implementation.orm.OrmTable;
import org.ujoframework.implementation.orm.RelationToMany;
import org.ujoframework.orm.annot.Db;
import org.ujoframework.orm.annot.Procedure;
import org.ujoframework.orm.annot.View;
import org.ujoframework.orm.dialect.*;

/**
 * A class mapping to a database (sample of usage)
 * @hidden
 */
@Db(schema = "db1", dialect = H2Dialect.class, user = "sa", password = "", jdbcUrl = "jdbc:h2:mem:db1")
public class Database extends OrmTable<Database> {

    /** Customer order. The used annotation overwrites a database schema from the property schema. */
    @Table("ord_order")
    public static final RelationToMany<Database, Order> ORDERS = newRelation(Order.class);

    /** Items of the Customer order */
    @Table("ord_item")
    public static final RelationToMany<Database, Item> ORDER_ITEMS = newRelation(Item.class);

    /** View to aggregate data. */
    @View("ord_order")
    public static final RelationToMany<Database, ViewOrder> VIEW_ORDERS = newRelation(ViewOrder.class);

    /** Customer */
    @Table("ord_customer")
    public static final RelationToMany<Database, Customer> CUSTOMER = newRelation(Customer.class);

    /** Database stored procedure */
    @Procedure("ujorm_test")
    public static final Property<Database, MyProcedure> myProcedure = newProperty(MyProcedure.class);
}
