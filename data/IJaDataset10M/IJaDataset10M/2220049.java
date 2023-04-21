package com.magic.magicstore;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.magic.magicstore.core.protocol.Protocol;
import com.magic.magicstore.view.SocketClient;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * �û�����ģ�������
 */
public class TestUserManagement extends TestCase {

    private SocketClient client;

    public TestUserManagement(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestUserManagement("testInsertUser"));
        suite.addTest(new TestUserManagement("testUpdateUser"));
        suite.addTest(new TestUserManagement("testDeleteUser"));
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        client = new SocketClient();
        client.createConnection();
        System.out.println(client.login("U001", "123456"));
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println(client.logout());
        client.closeConnection();
    }

    public void testInsertUser() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Protocol.ACTION_KEY, "usermanagementaction.insertUser");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", "U002");
            data.put("name", "�ڶ����û�");
            data.put("comment", "JUnit���Գ�������");
            data.put("email", "madawei1983@gmail.com");
            data.put("password", "123456");
            data.put("roleId", "3");
            data.put("telephone", "13857146413");
            map.put(Protocol.DATA_KEY, data);
            String result = client.sendData(JSONObject.toJSONString(map));
            System.out.println(result);
            Assert.assertEquals("success", ((HashMap<String, Object>) JSONValue.parse(result)).get("returnView"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testUpdateUser() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Protocol.ACTION_KEY, "usermanagementaction.updateUser");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", "U002");
            data.put("name", "�ڶ����û�");
            data.put("comment", "JUnit���Գ����޸ĵ�");
            data.put("email", "madawei1983@gmail.com");
            data.put("password", "123456");
            data.put("roleId", "3");
            data.put("telephone", "13857146413");
            map.put(Protocol.DATA_KEY, data);
            String result = client.sendData(JSONObject.toJSONString(map));
            System.out.println(result);
            Assert.assertEquals("success", ((HashMap<String, Object>) JSONValue.parse(result)).get("returnView"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testDeleteUser() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Protocol.ACTION_KEY, "usermanagementaction.deleteUser");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", "U002");
            map.put(Protocol.DATA_KEY, data);
            String result = client.sendData(JSONObject.toJSONString(map));
            System.out.println(result);
            Assert.assertEquals("success", ((HashMap<String, Object>) JSONValue.parse(result)).get("returnView"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
