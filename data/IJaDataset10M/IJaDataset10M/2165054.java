package org.centricframework.test;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import org.junit.Test;
import org.mvel2.MVEL;

public class MVELTest {

    @Test
    public void test1() {
        Map<String, Serializable> model = new Hashtable<String, Serializable>();
        User luc = new User();
        luc.setName("Luciano Blasetti");
        luc.setUserId("blasetti");
        model.put("user1", luc);
        User dan = new User();
        dan.setName("Daniele Conversa");
        dan.setUserId("conversa");
        model.put("user2", dan);
        model.put("user1", null);
        System.out.println(MVEL.eval("user1.name", model));
        System.out.println(MVEL.eval("user2.name", model));
        System.out.println(MVEL.eval("user3.name", model));
    }

    @SuppressWarnings("serial")
    class User implements Serializable {

        String userId;

        String name;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
