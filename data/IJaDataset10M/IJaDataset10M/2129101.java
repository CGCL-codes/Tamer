package net.taylor.security.jpa;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityListeners;
import javax.security.auth.Subject;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.el.Context;
import net.taylor.el.ExpressionUtil;
import net.taylor.embedded.Bootstrap;
import net.taylor.embedded.ContainerTestCase;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.seam.security.Identity;

public class PermissionsTest extends ContainerTestCase {

    public PermissionsTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new PermissionsTest("testMutatePermissionCallbackListener"));
        suite.addTest(new PermissionsTest("testAccessGranted"));
        suite.addTest(new PermissionsTest("testAccessDenied"));
        suite.addTest(new PermissionsTest("testListFiltered"));
        suite.addTest(new PermissionsTest("testFilteredExpression"));
        suite.addTest(new PermissionsTest("testRoleFilteredExpression"));
        suite.addTest(new PermissionsTest("testUserFilteredExpression"));
        suite.addTest(new PermissionsTest("testJexl"));
        return new Bootstrap(suite);
    }

    protected void setUp() throws Exception {
        super.setUp();
        login("user", "user");
    }

    public void testMutatePermissionCallbackListener() throws Exception {
        System.out.println("testMutatePermissionCallbackListener");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                MutatePermissionCallbackListener callback = new MutatePermissionCallbackListener();
                Entity e = new Entity();
                e.setValue1("user");
                e.setOther(e);
                callback.doPrePersist(e);
                callback.doPreUpdate(e);
                boolean denied = false;
                try {
                    callback.doPreRemove(e);
                } catch (SecurityException ex) {
                    denied = true;
                }
                assertTrue(denied);
            }
        }.run();
    }

    public void testAccessGranted() throws Exception {
        System.out.println("testAccessGranted");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                AccessPermissionInterceptor interceptor = new AccessPermissionInterceptor();
                Entity e = new Entity();
                e.setValue1("user");
                e.setOther(e);
                Object output = interceptor.invoke(new MockInvocationContext(e));
                assertNotNull(output);
            }
        }.run();
    }

    public void testAccessDenied() throws Exception {
        System.out.println("testAccessDenied");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                AccessPermissionInterceptor interceptor = new AccessPermissionInterceptor();
                Entity e = new Entity();
                e.setValue1("fred");
                boolean denied = false;
                try {
                    interceptor.invoke(new MockInvocationContext(e));
                } catch (SecurityException ex) {
                    denied = true;
                }
                assertTrue(denied);
            }
        }.run();
    }

    @SuppressWarnings("unchecked")
    public void testListFiltered() throws Exception {
        System.out.println("testListFiltered");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                AccessPermissionInterceptor interceptor = new AccessPermissionInterceptor();
                Entity e = new Entity();
                e.setValue1("user");
                e.setOther(e);
                Entity e2 = new Entity();
                e2.setValue1("admin");
                e2.setOther(e2);
                List<Entity> list = new ArrayList<Entity>();
                list.add(e);
                list.add(e2);
                list = (List<Entity>) interceptor.invoke(new MockInvocationContext(list));
                assertEquals(1, list.size());
            }
        }.run();
    }

    public void testFilteredExpression() throws Exception {
        System.out.println("testFilteredExpression");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                Subject subject = Identity.instance().getSubject();
                Principal caller = Identity.instance().getPrincipal();
                String filter = PermissionsUtil.instance().getFilter(subject, caller, PermissionsTest.class, Entity.class, "t");
                System.out.println(filter);
                assertEquals("(t.other.value1 in ('User') or t.value1 = 'user')", filter);
            }
        }.run();
    }

    public void testRoleFilteredExpression() throws Exception {
        System.out.println("testRoleFilteredExpression");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                Subject subject = Identity.instance().getSubject();
                String filter = PermissionsUtil.instance().getRoleFilter(subject, Entity.class, "t");
                System.out.println(filter);
                assertEquals("t.other.value1 in ('User')", filter);
            }
        }.run();
    }

    public void testUserFilteredExpression() throws Exception {
        System.out.println("testUserFilteredExpression");
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                Subject subject = Identity.instance().getSubject();
                Principal caller = Identity.instance().getPrincipal();
                String filter = PermissionsUtil.instance().getUserFilter(subject, caller, Entity.class, "t");
                System.out.println(filter);
                assertEquals("t.value1 = 'user'", filter);
            }
        }.run();
    }

    @SuppressWarnings("unchecked")
    public void testJexl() throws Exception {
        new ComponentTest() {

            @Override
            protected void testComponents() throws Exception {
                Entity entity = new Entity();
                entity.setValue1("v1");
                entity.setValue2("v2");
                entity.getOther().setValue1("ev1");
                entity.getOther().setValue2("ev2");
                Context context = new Context();
                context.put("entity", entity);
                context.put("number", new Integer(10));
                Object result = ExpressionUtil.getValue(context, "#{entity.value1}");
                System.out.println("Result: " + result);
                assertEquals(entity.getValue1(), result);
                result = ExpressionUtil.getValue(context, "#{entity.other.value1}");
                System.out.println("Result: " + result);
                assertEquals(entity.getOther().getValue1(), result);
            }
        }.run();
    }

    @Permissions({ @PrincipalsAllowed(actions = { Action.ALL }, roles = { "Admin" }), @PrincipalsAllowed(actions = { Action.INSERT }, roles = { "User" }), @PrincipalsAllowed(actions = { Action.READ, Action.UPDATE }, userExpressions = { "value1" }, roleExpressions = { "other.value1" }) })
    @EntityListeners(MutatePermissionCallbackListener.class)
    public class Entity {

        private String value1;

        private String value2;

        private Entity other = null;

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value) {
            this.value1 = value;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public Entity getOther() {
            if (other == null) {
                other = new Entity();
            }
            return other;
        }

        public void setOther(Entity other) {
            this.other = other;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    public class MockInvocationContext implements InvocationContext {

        private Object output;

        public MockInvocationContext(Object output) {
            super();
            this.output = output;
        }

        public Map getContextData() {
            return null;
        }

        public Method getMethod() {
            return null;
        }

        public Object[] getParameters() {
            return null;
        }

        public Object getTarget() {
            return null;
        }

        public Object proceed() throws Exception {
            return output;
        }

        public void setParameters(Object[] arg0) {
        }
    }
}
