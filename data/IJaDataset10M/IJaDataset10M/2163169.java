package org.simpleframework.xml.load;

import java.io.StringReader;
import java.util.Collection;
import junit.framework.TestCase;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeBuilder;

public class ClassBuilderTest extends TestCase {

    private static final String SCHEMA = "<class type='org.simpleframework.xml.load.ClassBuilderTest$ExampleClass' nanme='example'>\n" + "  <order elements='name, list' attribute='version'/>\n" + "  <field>\n" + "    <annotate field='version' annotation='org.simpleframework.xml.Attribute' name='version' data='false'/>\n" + "    <annotate field='name' annotation='org.simpleframework.xml.Element' name='name' data='false'/>\n" + "  </field>\n" + "  <method>\n" + "    <annotate method='list' annotation='org.simpleframework.xml.ElementList' entry='value' name='valueList'/>\n" + "  </method>\n" + "  <commit method='commit'/>\n" + "  <validate method='validate'/>" + "</class>\n";

    private static class ExampleClass {

        private Collection<String> list;

        private int version;

        private String name;

        private void commit() {
        }

        private void validate() {
        }

        public Collection<String> getList() {
            return list;
        }

        public void setList(Collection<String> list) {
            this.list = list;
        }
    }

    public void testClassBuilder() throws Exception {
        State state = new MockDefinition();
        StringReader reader = new StringReader(SCHEMA);
        InputNode node = NodeBuilder.read(reader);
        Provider provider = new Provider(ExampleClass.class);
        ClassBuilder builder = new ClassBuilder(provider, "schema.class");
        builder.build(node, state);
        System.err.printf("[%s]%n", state.getRoot());
        System.err.printf("[%s]%n", state.getOrder());
        System.err.printf("[%s]%n", state.getCommit());
        System.err.printf("[%s]%n", state.getValidate());
        for (Contact contact : state.getContacts()) {
            System.err.printf("[%s]%n", contact);
        }
    }

    public static void main(String[] list) throws Exception {
        new ClassBuilderTest().testClassBuilder();
    }
}
