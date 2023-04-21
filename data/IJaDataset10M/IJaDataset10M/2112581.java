package conga.param.ref;

public class ParameterReferenceTest extends RefTestCase {

    public void testCtor() {
        assertExplodes("ns", null);
        assertExplodes(null, "name");
        assertExplodes("", "");
        new ParameterReference("", " ");
    }

    private void assertExplodes(String ns, String name) {
        try {
            new ParameterReference(ns, name);
            fail();
        } catch (Exception e) {
        }
    }

    public void testGetters() {
        ParameterReference ref = new ParameterReference("ns", "name", "syn");
        assertEquals("name", ref.getName());
        assertEquals("ns", ref.getNamespace());
        assertEquals("syn", ref.getSyntacticalValue());
        assertTrue(ref.isExternal());
        ref = new ParameterReference("", "name");
        assertFalse(ref.isExternal());
    }
}
