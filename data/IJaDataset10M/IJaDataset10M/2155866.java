package sarj;

import java.util.*;
import java.sql.*;
import org.junit.Test;
import org.junit.Assert;

public class ActiveRecordTest extends ActiveRecord {

    @Test
    public void getAndSetUsingString() {
        Customer customer = new Customer();
        String setString = "John";
        customer.set("NAME", "John");
        String getString = (String) customer.get("NAME");
        Assert.assertEquals(setString, getString);
    }

    @Test
    public void setUsingClass() {
        Customer customer = new Customer();
        customer.set("NAME", "John");
        Integer expectedId = 25;
        customer.set("ID", expectedId);
        Order order = new Order();
        order.set(customer);
        Integer actualId = (Integer) order.get("CUSTOMER_ID");
        Assert.assertEquals(expectedId, actualId);
    }

    @Test
    public void tableNameTest() {
        Assert.assertEquals("ACTIVE_RECORD_TEST", tableName());
    }

    @Test
    public void removeNullValuesTest() {
        this.values = new HashMap<String, Object>();
        this.values.put("one", 1);
        this.values.put("two", null);
        this.values.put("three", 3);
        this.values.put("four", null);
        this.values.put("five", 5);
        removeNullValues();
        Assert.assertEquals("Not 3 elements left", 3, this.values.size());
        Assert.assertEquals(1, this.values.get("one"));
        Assert.assertEquals(3, this.values.get("three"));
        Assert.assertEquals(5, this.values.get("five"));
    }

    public static class Customer extends ActiveRecord {
    }

    public static class Order extends ActiveRecord {
    }

    public static class LongerClassName extends ActiveRecord {
    }
}
