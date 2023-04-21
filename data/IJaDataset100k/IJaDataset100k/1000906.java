package net.sf.afluentes.orders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.sql.DataSource;
import net.sf.afluentes.jdbc.GetGeneratedKeysProcessor;

class Order {

    Integer id;

    Date date;

    Integer customerId;

    Order(Date date) {
        this.date = date;
    }
}

class InsertOrderProcessor extends GetGeneratedKeysProcessor<Order> {

    private static final String COMMAND = "insert into ORDERS (DATE, CUSTOMER_ID)" + " values (?, ?)";

    InsertOrderProcessor(DataSource dataSource) {
        super(dataSource, COMMAND);
    }

    @Override
    protected void setParameters(PreparedStatement statement, Order order) throws SQLException {
        statement.setTimestamp(1, new Timestamp(order.date.getTime()));
        statement.setInt(2, order.customerId);
    }

    @Override
    protected void setGeneratedKey(Order order, ResultSet result) throws SQLException {
        order.id = result.getInt(1);
    }
}
