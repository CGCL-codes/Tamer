package consciouscode.seedling.config.properties;

import consciouscode.seedling.SeedlingException;
import consciouscode.seedling.SimpleBean;
import consciouscode.seedling.SimpleFactory;
import java.util.Properties;

public class PropertyConstructorTest extends PropertiesConfigTestCase {

    public static class CtorBean extends SimpleBean {

        public CtorBean() {
            fail("Unexpected call to ctor");
        }

        public CtorBean(SimpleBean otherNode) {
            super.setOtherNode(otherNode);
        }

        /** Always fails. */
        public CtorBean(int integer, SimpleBean otherNode) {
            assertEquals(null, otherNode);
            throw new IllegalArgumentException("ctor always fails");
        }

        public CtorBean(String textValue) {
            super(textValue);
        }

        public CtorBean(String textValue, int readOnlyIntValue) {
            super(textValue, readOnlyIntValue);
        }

        public CtorBean(boolean boolValue, Boolean booleanValue) {
            setBool(boolValue);
            setBoolean(booleanValue);
        }

        public CtorBean(int[] intArrayValue) {
            setIntArray(intArrayValue);
        }

        /**
         * Test case expects this to be private.
         */
        @SuppressWarnings("unused")
        private CtorBean(int intValue) {
            fail("Unexpected call to ctor");
        }

        @Override
        public void setText(String value) {
            fail("Unexpected call to setter");
        }
    }

    public void testNoConstructorProperty() throws Exception {
        Object node = makeNode(myProps);
        assertEquals("Unexpected node", null, node);
    }

    public void testNullConstructorProperty() throws Exception {
        setConstructorProperty(myProps, "null");
        Object node = makeNode(myProps);
        assertEquals("Unexpected node", null, node);
    }

    public void testVoidConstructorProperty() throws Exception {
        setConstructorProperty(myProps, SimpleFactory.class.getName() + ".static_void()");
        Object node = makeNode(myProps);
        assertEquals("Unexpected node", null, node);
    }

    public void testNoParamConstructor() throws Exception {
        setConstructorProperty(myProps, String.class);
        Object created = makeNode(myProps);
        assertNotNull(created);
        assertEquals(String.class, created.getClass());
    }

    public void testNoSuchClass() {
        myProps.setProperty(PropertiesConfigEvaluator.DOT_THIS_PROP, "new consciouscode.Rocks");
        try {
            makeNode(myProps);
            fail("Expected NodeInstantiationException");
        } catch (SeedlingException e) {
        }
    }

    public void testConstructor() throws Exception {
        CtorBean proto = new CtorBean("some value", 1234);
        proto.setBool(true);
        proto.setBoolean(new Boolean(false));
        proto.setInt(409342);
        proto.setInteger(new Integer(99865));
        setConstructorProperty(myProps, CtorBean.class, "(this.text, this.readOnlyInt)");
        myProps.setProperty("text", "\"some value\"");
        myProps.setProperty("readOnlyInt", Integer.toString(proto.getReadOnlyInt()));
        myProps.setProperty("bool", "true");
        myProps.setProperty("boolean", "false");
        myProps.setProperty("int", Integer.toString(proto.getInt()));
        myProps.setProperty("integer", proto.getInteger().toString());
        Object created = makeNode(myProps);
        assertNotNull(created);
        assertTrue(created instanceof CtorBean);
        CtorBean bean = (CtorBean) created;
        assertEquals(proto, bean);
    }

    public void testCtorWithNullStringLiteral() throws Exception {
        setConstructorProperty(myProps, CtorBean.class, "(null, 50)");
        CtorBean created = makeNode(myProps);
        assertEquals(null, created.getText());
        assertEquals(50, created.getReadOnlyInt());
    }

    public void testCtorWithLiterals() throws Exception {
        setConstructorProperty(myProps, CtorBean.class, "(\"hi\", 50)");
        CtorBean created = makeNode(myProps);
        assertEquals("hi", created.getText());
        assertEquals(50, created.getReadOnlyInt());
    }

    public void testCtorWithBooleanLiterals() throws Exception {
        setConstructorProperty(myProps, CtorBean.class, "(true, false)");
        CtorBean created = makeNode(myProps);
        assertTrue(created.getBool());
        assertFalse(created.getBoolean());
    }

    public void testCtorReferencesNode() throws Exception {
        SimpleBean other = new SimpleBean();
        getScratchBranch().installChild("Other", other);
        setConstructorProperty(myProps, CtorBean.class, "(Other)");
        CtorBean created = makeNode(myProps);
        assertSame(other, created.getOtherNode());
    }

    public void testNullConstructorParam() throws Exception {
        SimpleBean hasNull = new SimpleBean();
        assertNull(hasNull.getOtherNode());
        getScratchBranch().installChild("HasNull", hasNull);
        setConstructorProperty(myProps, CtorBean.class, "(this.otherNode)");
        myProps.setProperty("otherNode", "HasNull.otherNode");
        CtorBean created = makeNode(myProps);
        assertEquals(null, created.getOtherNode());
    }

    public void testCtorWithThisArray() throws Exception {
        setConstructorProperty(myProps, CtorBean.class, "(this.intArray)");
        myProps.setProperty("intArray", "[3,5]");
        CtorBean created = makeNode(myProps);
        assertEquals(new int[] { 3, 5 }, created.getIntArray());
    }

    public void testUndefinedConstructorParam() throws Exception {
        badCtor("(this.otherNode)");
    }

    public void testCtorWithSuperParam() throws Exception {
        badCtor("(super)");
    }

    public void testOldCreationSpecsFail() throws Exception {
        myProps.setProperty(".factory", SimpleFactory.class.getName() + ".static_intObject(11)");
        assertEquals(null, makeNode(myProps));
        myProps = new Properties();
        myProps.setProperty(".new", SimpleBean.class.getName());
        assertEquals(null, makeNode(myProps));
    }

    public void testConstructionWithNestedNew() throws SeedlingException {
        setConstructorProperty(myProps, CtorBean.class, "(new java.lang.String(\"hello\"))");
        CtorBean created = makeNode(myProps);
        assertEquals("hello", created.getText());
    }

    private void badCtor() {
        try {
            makeNode(myProps);
            fail("expected exception");
        } catch (SeedlingException e) {
        }
    }

    protected void badCtor(String params) {
        setConstructorProperty(myProps, CtorBean.class, params);
        badCtor();
    }
}
