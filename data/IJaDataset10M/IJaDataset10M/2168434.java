package org.lightframework.mvc.plugin.spring;

import org.lightframework.mvc.RouteManager;
import junit.framework.TestCase;

/**
 * Test Case of {@link RouteTable}
 * @author fenghm (fenghm@bingosoft.net)
 * @since 1.0.0
 */
public class TestRouteTable extends TestCase {

    public void test() {
        String string = "*		/user/{id}			user.view\n" + "get	/user/delete/{id}	user.delete\r\n" + "post	/					home.index";
        RouteTable table = new RouteTable();
        table.setRoutes(string);
        assertEquals(3, table.routes().size());
        assertEquals("*", table.routes().get(0).getMethod());
        assertEquals("/user/{id}", table.routes().get(0).getPath());
        assertEquals("user.view", table.routes().get(0).getAction());
        assertEquals("get", table.routes().get(1).getMethod());
        assertEquals("/user/delete/{id}", table.routes().get(1).getPath());
        assertEquals("user.delete", table.routes().get(1).getAction());
        assertEquals("post", table.routes().get(2).getMethod());
        assertEquals("/", table.routes().get(2).getPath());
        assertEquals("home.index", table.routes().get(2).getAction());
        table.register();
        assertEquals(3, RouteManager.table().size());
    }
}
