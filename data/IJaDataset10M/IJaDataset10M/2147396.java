package net.sourceforge.deco;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.deco.data.ClassBasedModule;
import net.sourceforge.deco.data.ClassLoaderBasedModule;
import net.sourceforge.deco.data.Module;
import net.sourceforge.deco.data.Path;
import net.sourceforge.deco.data.SrcModule;
import net.sourceforge.deco.parsers.utils.PackageChecker;
import net.sourceforge.deco.testclass.EmptyClass;
import net.sourceforge.deco.testclass.EmptyInterface;
import net.sourceforge.deco.testclass.ExtOfExExt;
import net.sourceforge.deco.testclass.ExtensionClass;
import net.sourceforge.deco.testclass.ExtensionOfExtensionClass;
import net.sourceforge.deco.testclass.InterfaceWithMethods;
import net.sourceforge.deco.testclass.MethodInvocation;
import net.sourceforge.deco.testclass.OverloadedMethodDecl;
import net.sourceforge.deco.testclass.OverloadedMethodUser;
import net.sourceforge.deco.testclass.SpecificRuntimeException;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.Test;
import org.objectweb.asm.Type;

public class CompileDependencyAnalyzerTest extends JMockTestCase {

    DependencyAnalyzer analyzer = new DependencyAnalyzer(new PackageChecker("java"), new NoLogLogger());

    Path path = new Path();

    Module unusedModule = new ClassBasedModule("unused.jar");

    ClassLoaderBasedModule classLoaderModule = new ClassLoaderBasedModule(this.getClass().getClassLoader());

    DependencyConsumer report = context.mock(DependencyConsumer.class);

    @Test
    public void reportUnunsedModules() throws IOException, ClassNotFoundException {
        SrcModule src = srcModule(EmptyClass.class);
        path.add(unusedModule);
        context.checking(new Expectations() {

            {
                never(report).addDependency(with(any(Type.class)), with(any(Type.class)), with(any(Type[].class)));
            }
        });
        analyzer.reportCompileDependencies(src, path, report);
    }

    @Test
    public void reportUsedModules() throws IOException, ClassNotFoundException {
        SrcModule src = srcModule(ExtensionClass.class);
        final Module usedModule = srcModule("used.jar", EmptyClass.class, EmptyInterface.class);
        path.add(usedModule);
        context.checking(new Expectations() {

            {
                atLeast(1).of(report).addDependency(with(any(Type.class)), with(any(Type.class)), with(any(Type[].class)));
                never(report).addDependency(with(equalTo(Type.getType(EmptyClass.class))), with(any(Type.class)), with(any(Type[].class)));
                never(report).addDependency(with(equalTo(Type.getType(EmptyInterface.class))), with(any(Type.class)), with(any(Type[].class)));
            }
        });
        analyzer.reportCompileDependencies(src, path, report);
    }

    @Test
    public void reportThrowsOfCalledMethod() throws IOException, ClassNotFoundException {
        SrcModule src = srcModule(MethodInvocation.class);
        path.add(classLoaderModule);
        context.checking(new Expectations() {

            {
                atLeast(1).of(report).addDependency(with(equal(Type.getType(MethodInvocation.class))), with(equal(Type.getType(SpecificRuntimeException.class))), with(any(Type[].class)));
                never(report).addDependency(with(equal(Type.getType(MethodInvocation.class))), with(equal(Type.getType(RuntimeException.class))), with(any(Type[].class)));
                ignoring(report).addDependency(with(equal(Type.getType(MethodInvocation.class))), with(anythingDifferentType(SpecificRuntimeException.class, RuntimeException.class)), with(any(Type[].class)));
            }
        });
        analyzer.reportCompileDependencies(src, path, report);
    }

    @Test
    public void reportParametersOfOverloadedMethods() throws IOException, ClassNotFoundException {
        SrcModule src = srcModule(OverloadedMethodUser.class);
        path.add(classLoaderModule);
        context.checking(new Expectations() {

            {
                Type EXT_TYPE = Type.getType(ExtensionClass.class);
                Type TESTED_TYPE = Type.getType(OverloadedMethodUser.class);
                Type METHOD_DECL_TYPE = Type.getType(OverloadedMethodDecl.class);
                one(report).addDependency(with(equal(TESTED_TYPE)), with(equal(METHOD_DECL_TYPE)), with(equal(new Type[] {})));
                one(report).addDependency(with(equal(TESTED_TYPE)), with(equal(EXT_TYPE)), with(equal(new Type[] { METHOD_DECL_TYPE })));
                one(report).addDependency(with(equal(TESTED_TYPE)), with(equal(Type.getType(InterfaceWithMethods.class))), with(equal(new Type[] { METHOD_DECL_TYPE })));
            }
        });
        analyzer.reportCompileDependencies(src, path, report);
    }

    private static Matcher<Type> anythingDifferentType(Class<?>... classes) {
        List<Matcher<? extends Type>> matchers = new LinkedList<Matcher<? extends Type>>();
        for (Class<?> c : classes) {
            matchers.add(not(equalTo(Type.getType(c))));
        }
        return allOf(matchers);
    }

    @Test
    public void reportParentClasses() throws IOException, ClassNotFoundException {
        path.add(classLoaderModule);
        SrcModule src = srcModule(ExtOfExExt.class);
        final Type EXT_EXT_EXT_TYPE = Type.getType(ExtOfExExt.class);
        final Type EXT_EXT_TYPE = Type.getType(ExtensionOfExtensionClass.class);
        final Type EXT_TYPE = Type.getType(ExtensionClass.class);
        context.checking(new Expectations() {

            {
                atLeast(1).of(report).addDependency(with(equal(EXT_EXT_EXT_TYPE)), with(equal(EXT_EXT_TYPE)), with(equal(new Type[] {})));
                atLeast(1).of(report).addDependency(with(equal(EXT_EXT_EXT_TYPE)), with(equal(EXT_TYPE)), with(equal(new Type[] { EXT_EXT_TYPE })));
                atLeast(1).of(report).addDependency(with(equal(EXT_EXT_EXT_TYPE)), with(equal(Type.getType(EmptyClass.class))), with(equal(new Type[] { EXT_TYPE, EXT_EXT_TYPE })));
                atLeast(1).of(report).addDependency(with(equal(EXT_EXT_EXT_TYPE)), with(equal(Type.getType(EmptyInterface.class))), with(equal(new Type[] { EXT_TYPE, EXT_EXT_TYPE })));
            }
        });
        analyzer.reportCompileDependencies(src, path, report);
    }

    private SrcModule srcModule(Class<?>... classes) {
        return srcModule("src.jar", classes);
    }

    private SrcModule srcModule(String name, Class<?>... classes) {
        ClassBasedModule src = new ClassBasedModule("src.jar");
        for (Class<?> c : classes) {
            src.addClass(c);
        }
        return src;
    }
}
