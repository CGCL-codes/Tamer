package net.sourceforge.deco;

import java.util.Collections;
import net.sourceforge.deco.data.ClassLoaderBasedModule;
import net.sourceforge.deco.data.Path;
import net.sourceforge.deco.data.RootMethod;
import net.sourceforge.deco.parsers.utils.PackageChecker;
import net.sourceforge.deco.testclass.ClassWithAttribute;
import net.sourceforge.deco.testclass.EmptyClass;
import net.sourceforge.deco.testclass.EmptyInterface;
import net.sourceforge.deco.testclass.ExtensionClass;
import net.sourceforge.deco.testclass.ObjectCreation;
import org.jmock.Expectations;
import org.junit.Test;
import org.objectweb.asm.Type;

public class RuntimeDependencyAnalyzerTest extends JMockTestCase {

    static ClassLoaderBasedModule CLASS_LOADER_MODULE = new ClassLoaderBasedModule(RuntimeDependencyAnalyzerTest.class.getClassLoader());

    DependencyAnalyzer analyzer = new DependencyAnalyzer(new PackageChecker("java"));

    Path path = new Path();

    RuntimeDependencyConsumer report = context.mock(RuntimeDependencyConsumer.class);

    @Test(expected = ClassNotFoundException.class)
    public void testReportClassNotFound() throws Exception {
        context.checking(new Expectations() {

            {
                ignoring(report);
            }
        });
        RootMethod m = method(ExtensionClass.class, "toString", "()Ljava/lang/String;");
        Path emptyPath = new Path();
        analyzer.reportRuntimeDependencies(Collections.singleton(m), emptyPath, report);
    }

    @Test
    public void testRootMethodInstanciate() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(ExtensionClass.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
                one(report).addDependency(null, Type.getType(EmptyInterface.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(ExtensionClass.class, "toString", "()Ljava/lang/String;"), path, report);
    }

    @Test
    public void testStaticInitializerIsExecutedOnRootClass() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(ObjectCreation.class));
                one(report).addDependency(null, Type.getType(ExtensionClass.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
                one(report).addDependency(null, Type.getType(EmptyInterface.class));
                never(report).addDependency(null, Type.getType(ClassWithAttribute.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(ObjectCreation.class, "doNothing", "()V"), path, report);
    }

    @Test
    public void initializeWhenInstanciate() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(DummyChild.class));
                one(report).addDependency(null, Type.getType(Dummy.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "instanciateDummyChild", "()V"), path, report);
    }

    @Test
    public void initializeWhenStaticFieldAreRead() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(DummyChild.class));
                one(report).addDependency(null, Type.getType(Dummy.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "readParentStaticFieldViaChild", "()I"), path, report);
    }

    @Test
    public void initializeWhenStaticFieldAreWriten() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(Dummy.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "callStaticMethod", "()V"), path, report);
    }

    @Test
    public void initializeWhenStaticMethodCalled() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(Dummy.class));
                one(report).addDependency(null, Type.getType(EmptyClass.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "callStaticMethod", "()V"), path, report);
    }

    @Test
    public void dontLoadForDeclaration() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                never(report).addDependency(null, Type.getType(ExtensionClass.class));
                never(report).addDependency(null, Type.getType(EmptyClass.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "declareDummy", "()Ljava/lang/Object;"), path, report);
        context.assertIsSatisfied();
    }

    @Test
    public void dontInitializeForInstanceOf() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(DummyInterface.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "useInstanceOf", "(Ljava/lang/Object;)Z"), path, report);
        context.assertIsSatisfied();
    }

    @Test
    public void dontInitializeForArrayCreation() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(DummyInterface.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "createArray", "()Ljava/lang/Object;"), path, report);
        context.assertIsSatisfied();
    }

    @Test
    public void dontInitializeForCast() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyUserForInitializationTesting.class));
                one(report).addDependency(null, Type.getType(DummyInterface.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyUserForInitializationTesting.class, "useCast", "()Lnet/sourceforge/deco/RuntimeDependencyAnalyzerTest$DummyInterface;"), path, report);
        context.assertIsSatisfied();
    }

    @Test
    public void callAllExistingConcreteImpl() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyForTransitiveCall.class));
                one(report).addDependency(null, Type.getType(CallClass.class));
                one(report).addDependency(null, Type.getType(Call.class));
                one(report).addDependency(null, Type.getType(CallMarker1.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyForTransitiveCall.class, "callInterfaceOnConcreteClass", "()V"), path, report);
        context.assertIsSatisfied();
    }

    @Test
    public void callAlsoFuturConcreteClass() throws Exception {
        context.checking(new Expectations() {

            {
                one(report).addDependency(null, Type.getType(DummyForTransitiveCall.class));
                one(report).addDependency(null, Type.getType(CallClass.class));
                one(report).addDependency(null, Type.getType(Call.class));
                one(report).addDependency(null, Type.getType(CallMarker1.class));
            }
        });
        path.add(CLASS_LOADER_MODULE);
        analyzer.reportRuntimeDependencies(methods(DummyForTransitiveCall.class, "callInterfaceBeforeObjectCreation", "()V"), path, report);
        context.assertIsSatisfied();
    }

    private static RootMethod method(Class<?> cl, String method, String desc) {
        return new RootMethod(Type.getType(cl), method, desc);
    }

    private static Iterable<RootMethod> methods(Class<?> cl, String method, String desc) {
        return Collections.singleton(method(cl, method, desc));
    }

    static interface DummyInterface {

        static final ExtensionClass X = new ExtensionClass();
    }

    static class Dummy {

        static int aStaticField;

        static void aStaticMethod() {
        }

        static {
            new EmptyClass();
        }
    }

    static class DummyChild extends Dummy {
    }

    @SuppressWarnings("unused")
    static class DummyUserForInitializationTesting {

        public static void instanciateDummyChild() {
            DummyChild t = new DummyChild();
        }

        public static int readParentStaticFieldViaChild() {
            return DummyChild.aStaticField;
        }

        public static void callStaticMethod() {
            Dummy.aStaticMethod();
        }

        public static void writeStaticField() {
            Dummy.aStaticField = 3;
        }

        public static Object declareDummy() {
            Dummy o = null;
            return o == o ? o : null;
        }

        public static boolean useInstanceOf(Object p) {
            return p instanceof DummyInterface;
        }

        public static Object createArray() {
            return new DummyInterface[5];
        }

        public static DummyInterface useCast() {
            return (DummyInterface) new Object();
        }
    }

    static class CallMarker1 {

        public static void mark() {
        }

        ;
    }

    static interface Call {

        public void call();
    }

    static class CallClass implements Call {

        public void call() {
            CallMarker1.mark();
        }
    }

    static class DummyForTransitiveCall {

        public static void callInterfaceOnConcreteClass() {
            Call c = new CallClass();
            c.call();
        }

        public static void callInterfaceBeforeObjectCreation() {
            Call c = null;
            do {
                c.call();
                c = new CallClass();
            } while (true);
        }
    }
}
