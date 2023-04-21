package org.javason.jsonrpc;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.javason.jsonrpc.description.Param;

public class DefaultParamService {

    public void cancel(@Param(name = "credentials") Credentials credentials, @Param(name = "customer") Customer customer, @Param(name = "orderId") String orderId, @Param(name = "reasonId") String reasonId, @Param(name = "overriddenRefund") Double overriddenRefund) {
        System.out.println("in cancel");
        System.out.println(" credentials:" + credentials);
        System.out.println(" customer:" + customer);
        System.out.println(" orderId:" + orderId);
        System.out.println(" reasonId:" + reasonId);
        System.out.println(" overriddenRefund:" + overriddenRefund);
        assert (overriddenRefund == null);
    }

    public static class Credentials implements Serializable {

        private static final long serialVersionUID = 530692807368949856L;

        private String username;

        private String password;

        public Credentials() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String login) {
            this.username = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }

    public static class Customer implements Serializable {

        private static final long serialVersionUID = -2459323547165745068L;

        public enum Type {

            D2C, B2B
        }

        private String guid;

        private Type type = Type.D2C;

        public Customer() {
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }
}
