package org.actorsguildframework;

import java.util.Arrays;
import java.util.Date;
import junit.framework.Assert;
import org.actorsguildframework.BeanTest.SuperBaseBean;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.DefaultValue;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Prop;
import org.actorsguildframework.annotations.Shared;
import org.actorsguildframework.annotations.ThreadSafe;
import org.junit.Test;

/**
 */
public class VariousActorTest {

    public static class SomeSerializableObject implements java.io.Serializable {

        int a = 2;
    }

    public static class SomeImmutableObject implements Immutable {

        final int a = 3;
    }

    public static interface OpInterface {

        public AsyncResult<Integer> op(int a, int b);
    }

    public static class SomeActor extends Actor implements OpInterface {

        @Message
        public AsyncResult<Integer> op(int a, int b) {
            Assert.assertNotNull(getAgent());
            return result(a + b);
        }

        @Message
        public AsyncResult<Void> throwing() throws Exception {
            throw new Exception("foo");
        }

        @Message
        public AsyncResult<SomeSerializableObject> opValueTypes(int a, String s, SomeSerializableObject sso, SomeImmutableObject sio) {
            Assert.assertEquals(5, a);
            if (s != null) Assert.assertEquals("foobar", s);
            if (sso != null) Assert.assertEquals(2, sso.a);
            if (sio != null) Assert.assertEquals(3, sio.a);
            return result(sso);
        }

        @Message
        public AsyncResult<SomeImmutableObject> opReturnImmutable(SomeImmutableObject sio) {
            return result(sio);
        }

        @Message
        public AsyncResult<InheritingActor2> opReferenceTypes(InheritingActor1 inaa, OpInterface iwaa) {
            Assert.assertEquals(5, inaa.op(8, 3).get().intValue());
            Assert.assertEquals(24, iwaa.op(8, 3).get().intValue());
            return result((InheritingActor2) iwaa);
        }

        @Message
        public AsyncResult<String> opNullReturn() {
            return result(null);
        }

        @Message
        public AsyncResult<String[]> opArrays(String[] a, int[] b, Date[] c, String[] d, int bLen, int cLen) {
            Assert.assertEquals(bLen, b.length);
            Assert.assertEquals(cLen, c.length);
            return result(a);
        }

        @Message
        public AsyncResult<Void> sharedArgument(@Shared java.io.InputStream a) {
            Assert.assertTrue(a instanceof java.io.InputStream);
            Assert.assertFalse(a instanceof java.io.Serializable);
            Assert.assertNotNull(a);
            return noResult();
        }

        @ThreadSafe
        public void threadSafeMethod() {
        }
    }

    public static class InheritingActor1 extends SomeActor {

        @Message
        @Override
        public AsyncResult<Integer> op(int a, int b) {
            Assert.assertNotNull(getAgent());
            return result(a - b);
        }
    }

    public static class InheritingActor2 extends SomeActor {

        @Message
        @Override
        public AsyncResult<Integer> op(int a, int b) {
            Assert.assertNotNull(getAgent());
            return result(a * b);
        }
    }

    public static class ContainerA {

        public static class IdenticalActor extends Actor {

            @Message
            public AsyncResult<Integer> test() {
                return result(1);
            }
        }
    }

    public static class ContainerB {

        public static class IdenticalActor extends Actor {

            @Message
            public AsyncResult<Integer> test() {
                return result(2);
            }
        }
    }

    private static class PrivateActor extends Actor {

        PrivateActor() {
        }

        @Message
        public AsyncResult<Integer> test(int a) {
            return result(a);
        }
    }

    public abstract static class InitActor extends Actor {

        @DefaultValue("a")
        static final int A = 5;

        @Prop
        public abstract int getA();

        @Prop
        public abstract int getACopy();

        public abstract void setACopy(int a);

        @Initializer
        protected void init() {
            setACopy(getA());
        }

        @Message
        public AsyncResult<Integer> mul(int b) {
            return result(getA() * b);
        }
    }

    public abstract static class InitBaseActor extends Actor {

        int initNum = 1;

        public int a, b;

        @Initializer
        protected void initA() {
            a = initNum++;
        }

        @Initializer
        protected void initB() {
            b = initNum++;
        }
    }

    public abstract static class SuperBaseActor extends InitBaseActor {

        public int c, d;

        @Initializer
        protected void initC() {
            c = initNum++;
        }

        @Initializer
        protected void initD() {
            d = initNum++;
        }
    }

    @Model(ConcurrencyModel.Stateless)
    public abstract static class StatelessActor extends Actor {

        @Shared
        public final Object a = null;

        @Shared
        @Prop
        abstract Object getB();

        @Prop
        abstract String getC();

        public final String d = null;

        @Message
        public AsyncResult<Void> dummyMsg() {
            return noResult();
        }
    }

    @Test
    public void testSimpleActorAndMessage() throws Exception {
        DefaultAgent a = new DefaultAgent();
        SomeActor ac = a.create(SomeActor.class);
        Assert.assertEquals(5, ac.op(3, 2).get().intValue());
    }

    @Test
    public void testSimpleActorThrowing() throws Exception {
        DefaultAgent a = new DefaultAgent();
        SomeActor ac = a.create(SomeActor.class);
        AsyncResult<Void> r = ac.throwing();
        try {
            r.get();
        } catch (WrappedException e) {
            Assert.assertTrue(e.isWrapping(Exception.class));
            Assert.assertEquals("foo", e.getCause().getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testSimpleActorInheriting() throws Exception {
        DefaultAgent a = new DefaultAgent();
        InheritingActor2 ac = a.create(InheritingActor2.class);
        Assert.assertEquals(6, ac.op(3, 2).get().intValue());
        AsyncResult<Void> r = ac.throwing();
        Assert.assertFalse(r instanceof ImmediateResult);
        r.await();
    }

    @Test
    public void testValueTypes() {
        DefaultAgent a = new DefaultAgent();
        SomeActor ac = a.create(SomeActor.class);
        SomeSerializableObject sso = new SomeSerializableObject();
        SomeSerializableObject r = ac.opValueTypes(5, "foobar", sso, new SomeImmutableObject()).get();
        Assert.assertNotSame(sso, r);
        Assert.assertEquals(2, r.a);
        synchronized (this) {
            testValueTypesNull();
        }
    }

    @Test
    public void testValueTypesNull() {
        DefaultAgent a = new DefaultAgent();
        SomeActor ac = a.create(SomeActor.class);
        SomeSerializableObject r = ac.opValueTypes(5, null, null, null).get();
        Assert.assertNull(r);
    }

    @Test
    public void testImmutable() {
        DefaultAgent a = new DefaultAgent();
        SomeActor ac = a.create(SomeActor.class);
        SomeImmutableObject sio = new SomeImmutableObject();
        SomeImmutableObject r = ac.opReturnImmutable(sio).get();
        Assert.assertSame(sio, r);
    }

    @Test
    public void testReferenceTypes() {
        DefaultAgent a = new DefaultAgent();
        InheritingActor1 inaa = a.create(InheritingActor1.class);
        InheritingActor2 iwaa = a.create(InheritingActor2.class);
        SomeActor ac = a.create(SomeActor.class);
        InheritingActor2 r = ac.opReferenceTypes(inaa, iwaa).get();
        Assert.assertSame(iwaa, r);
        Assert.assertFalse(ac.opReferenceTypes(inaa, iwaa) instanceof ImmediateResult);
    }

    @Test
    public void testIdenticallyNamedActors() {
        DefaultAgent a = new DefaultAgent();
        ContainerA.IdenticalActor ia = a.create(ContainerA.IdenticalActor.class);
        ContainerB.IdenticalActor ib = a.create(ContainerB.IdenticalActor.class);
        Assert.assertEquals(1, ia.test().get().intValue());
        Assert.assertEquals(2, ib.test().get().intValue());
    }

    @Test
    public void testNullReturningMessage() {
        DefaultAgent a = new DefaultAgent();
        Assert.assertNull(a.create(SomeActor.class).opNullReturn().get());
    }

    @Test
    public void testArrays() {
        DefaultAgent da = new DefaultAgent();
        SomeActor nia = da.create(SomeActor.class);
        String[] aIn = { "haha", "abc", null, "x", "" };
        String[] aOut = nia.opArrays(aIn, new int[] { 1, 2, 3 }, new Date[] { new Date(2) }, null, 3, 1).get();
        Assert.assertTrue(Arrays.deepEquals(aIn, aOut));
    }

    @Test
    public void testSharedArgument() {
        DefaultAgent da = new DefaultAgent();
        SomeActor nia = da.create(SomeActor.class);
        nia.sharedArgument(System.in).get();
    }

    @Test
    public void testPrivateActor() {
        DefaultAgent da = new DefaultAgent();
        PrivateActor a = da.create(PrivateActor.class);
        int x = a.test(2).get();
        Assert.assertEquals(2, x);
    }

    @Test
    public void testPropAndInit() {
        DefaultAgent da = new DefaultAgent();
        InitActor a = da.create(InitActor.class);
        Assert.assertEquals(30, a.mul(6).get().intValue());
        Assert.assertEquals(5, a.getACopy());
        InitActor a2 = da.create(InitActor.class, new Props("a", 10));
        Assert.assertEquals(-50, a2.mul(-5).get().intValue());
        Assert.assertEquals(10, a2.getACopy());
        InitActor a3 = da.create(InitActor.class, new Props("a", 10).add("aCopy", 0));
        Assert.assertEquals(-50, a3.mul(-5).get().intValue());
        Assert.assertEquals(10, a3.getACopy());
    }

    @Test
    public void testInitializer() {
        DefaultAgent ag = new DefaultAgent();
        SuperBaseActor b = ag.create(SuperBaseActor.class);
        Assert.assertTrue((b.a == 1) || (b.a == 2));
        Assert.assertTrue((b.b == 1) || (b.b == 2));
        Assert.assertTrue(b.a != b.b);
        Assert.assertTrue((b.c == 3) || (b.c == 4));
        Assert.assertTrue((b.d == 3) || (b.d == 4));
        Assert.assertTrue(b.c != b.d);
    }

    @Test
    public void testStatelessActor() {
        DefaultAgent ag = new DefaultAgent();
        StatelessActor a = ag.create(StatelessActor.class, new Props("b", "foo").add("c", "bar"));
        a.dummyMsg().get();
    }
}
