package org.rubypeople.rdt.refactoring.tests.core.overridemethod;

import org.rubypeople.rdt.refactoring.core.overridemethod.MethodsOverrider;
import org.rubypeople.rdt.refactoring.documentprovider.StringDocumentProvider;
import org.rubypeople.rdt.refactoring.tests.TreeProviderTester;

public class TC_MethodOverriderTreeTest extends TreeProviderTester {

    private static final String superFileName = "super.rb";

    private static final String A_EXTENDS_X_DOCUMENT = "require \"" + superFileName + "\"\n" + "class A < X\n" + "end";

    private static final String TWO_CLASSES = "require \"" + superFileName + "\"\n" + "class A < X\n" + "end\n" + "class B < Y\n" + "end\n";

    private static final String TEST_DOCUMENT_SIMPLE = "class X\n" + "  def method arg\n" + "    @a = a\n" + "    @b = a\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_NO_CONSTRUCTOR_ARGS = "class X\n" + "  def initialize\n" + "    @a = 1\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_ONE_CONSTRUCTOR_ARG = "class X\n" + "  def initialize a\n" + "    @a = a\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_TREE_ONSTRUCTOR_ARGS = "class X\n" + "  def initialize a, b, c\n" + "    @a = a\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_TWO_CLASSES = "#Comment\n" + "class X\n" + "  def initialize a\n" + "    @a = a\n" + "  end\n" + "end\n" + "class Y\n" + "  #Comment\n" + "  def initialize b, c\n" + "    @x = b + c\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_METHOD_AND_CONSTRUCTOR = "class X\n" + "  def initialize arg0, arg1\n" + "    @a = a\n" + "  end\n" + "  def method arg\n" + "    @a = a\n" + "    @b = a\n" + "  end\n" + "end";

    private static final String TEST_DOCUMENT_TWO_METHOD_AND_TWO_CONSTRUCTOR = "class X\n" + "  def initialize arg0, arg1\n" + "    @a = a\n" + "  end\n" + "  def initialize arg\n" + "    @a = a\n" + "  end\n" + "  def method0 arg\n" + "    @b = a\n" + "  end\n" + "  def method1 arg\n" + "    @b = a\n" + "  end\n" + "end";

    public void testDocumentOneMethod() {
        addContent(new String[] { "A", "method arg" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentOneMethod.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_SIMPLE);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentNoConstructorArgs() {
        addContent(new String[] { "A", "initialize" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentNoConstructorArgs.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_NO_CONSTRUCTOR_ARGS);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentOneConstructorArg() {
        addContent(new String[] { "A", "initialize a" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentOneConstructorArg.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_ONE_CONSTRUCTOR_ARG);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentTreeConstructorArgs() {
        addContent(new String[] { "A", "initialize a, b, c" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentTreeConstructorArgs.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_TREE_ONSTRUCTOR_ARGS);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentTwoClasses() {
        addContent(new String[] { "A", "initialize a" });
        addContent(new String[] { "B", "initialize b, c" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentTwoClasses.rb", TWO_CLASSES);
        docProvider.addFile(superFileName, TEST_DOCUMENT_TWO_CLASSES);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentMethodAndConstructor() {
        addContent(new String[] { "A", "initialize arg0, arg1" });
        addContent(new String[] { "A", "method arg" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentMethodAndConstructor.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_METHOD_AND_CONSTRUCTOR);
        validate(new MethodsOverrider(docProvider));
    }

    public void testDocumentMethodsAndTwoConstructors() {
        addContent(new String[] { "A", "initialize arg" });
        addContent(new String[] { "A", "method0 arg" });
        addContent(new String[] { "A", "method1 arg" });
        StringDocumentProvider docProvider = new StringDocumentProvider("override_method_test_testDocumentMethodsAndTwoConstructors.rb", A_EXTENDS_X_DOCUMENT);
        docProvider.addFile(superFileName, TEST_DOCUMENT_TWO_METHOD_AND_TWO_CONSTRUCTOR);
        validate(new MethodsOverrider(docProvider));
    }
}
