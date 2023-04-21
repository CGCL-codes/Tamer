package org.ztemplates.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.ztemplates.form.ZFormElementMirrorTest;
import org.ztemplates.form.ZFormValuesTest;
import org.ztemplates.form.ZSerializeUtilTest;
import org.ztemplates.form.list.FormWithListTest;
import org.ztemplates.json.ZJsonTest;
import org.ztemplates.test.actions.classes.test1.ClassesTest;
import org.ztemplates.test.actions.expression.test1.ExpressionTest;
import org.ztemplates.test.actions.service.ActionServiceTest;
import org.ztemplates.test.actions.urlhandler.callbacks.test1.CallbacksTest;
import org.ztemplates.test.actions.urlhandler.callbacks.test2.ProcessorTest;
import org.ztemplates.test.actions.urlhandler.constructor.ConstructorTest;
import org.ztemplates.test.actions.urlhandler.form.FormTest;
import org.ztemplates.test.actions.urlhandler.form.FormTestOpCallback;
import org.ztemplates.test.actions.urlhandler.i18n.I18nTest;
import org.ztemplates.test.actions.urlhandler.match.collision.TreeCollisionTest;
import org.ztemplates.test.actions.urlhandler.match.collision2.TreeCollision2Test;
import org.ztemplates.test.actions.urlhandler.match.collision3.Collision3Test;
import org.ztemplates.test.actions.urlhandler.match.handler.ParseTreeTest;
import org.ztemplates.test.actions.urlhandler.match.handler.tail.TailParseTreeTest;
import org.ztemplates.test.actions.urlhandler.match.matchtree.MatchTreeTest;
import org.ztemplates.test.actions.urlhandler.match.test1.TreeTest;
import org.ztemplates.test.actions.urlhandler.match.test2.TreeTest2;
import org.ztemplates.test.actions.urlhandler.prop.plain.PropTest;
import org.ztemplates.test.actions.urlhandler.repository.nested.NestedHandlerTest;
import org.ztemplates.test.actions.urlhandler.repository.simple.UrlHandlerRepositoryTest;
import org.ztemplates.test.actions.urlhandler.repository.simple.ZUrlFactoryTest;
import org.ztemplates.test.actions.urlhandler.repository.trailingslash.TrailingSlashTest;
import org.ztemplates.test.actions.urlhandler.secure.nested.NestedSecureTest;
import org.ztemplates.test.actions.urlhandler.secure.simple.SimpleSecureTest;
import org.ztemplates.test.actions.urlhandler.tree.collision.test1.UrlHandlerRepositoryCollisionTest;
import org.ztemplates.test.actions.urlhandler.tree.parameters.ParameterTest;
import org.ztemplates.test.property.PropertiesTest;
import org.ztemplates.test.property.PropertyTest;
import org.ztemplates.test.reflection.ReflectionUtilTest;
import org.ztemplates.test.render.css.CssTest;
import org.ztemplates.test.render.impl.ReplaceUtilTest;
import org.ztemplates.test.render.impl.opt.ZRenderClassRepositoryTest;
import org.ztemplates.test.render.methodrepo.ZExposedMethodRepositoryTest;
import org.ztemplates.test.render.script.basic.ScriptTest;
import org.ztemplates.test.render.script.basic2.BasicScript2Test;
import org.ztemplates.test.render.script.cycle.ScriptCycleTest;
import org.ztemplates.test.render.script.duplicates.VariableNamesScriptTest;
import org.ztemplates.test.render.script.js.CachingJavaScriptProcessorTest;
import org.ztemplates.test.render.script.var.ScriptVarTest;

public class AllTests extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for org.ztemplates.actions");
        suite.addTestSuite(ExpressionTest.class);
        suite.addTestSuite(ClassesTest.class);
        suite.addTestSuite(ProcessorTest.class);
        suite.addTestSuite(ZUrlFactoryTest.class);
        suite.addTestSuite(UrlHandlerRepositoryTest.class);
        suite.addTestSuite(UrlHandlerRepositoryCollisionTest.class);
        suite.addTestSuite(NestedHandlerTest.class);
        suite.addTestSuite(ParameterTest.class);
        suite.addTestSuite(TrailingSlashTest.class);
        suite.addTestSuite(CallbacksTest.class);
        suite.addTestSuite(PropTest.class);
        suite.addTestSuite(PropertyTest.class);
        suite.addTestSuite(CssTest.class);
        suite.addTestSuite(TreeTest.class);
        suite.addTestSuite(TreeTest2.class);
        suite.addTestSuite(TreeCollisionTest.class);
        suite.addTestSuite(TreeCollision2Test.class);
        suite.addTestSuite(Collision3Test.class);
        suite.addTestSuite(ParseTreeTest.class);
        suite.addTestSuite(SimpleSecureTest.class);
        suite.addTestSuite(NestedSecureTest.class);
        suite.addTestSuite(MatchTreeTest.class);
        suite.addTestSuite(I18nTest.class);
        suite.addTestSuite(ScriptTest.class);
        suite.addTestSuite(ScriptCycleTest.class);
        suite.addTestSuite(TailParseTreeTest.class);
        suite.addTestSuite(ReplaceUtilTest.class);
        suite.addTestSuite(PropertiesTest.class);
        suite.addTestSuite(ScriptVarTest.class);
        suite.addTestSuite(BasicScript2Test.class);
        suite.addTestSuite(CachingJavaScriptProcessorTest.class);
        suite.addTestSuite(ConstructorTest.class);
        suite.addTestSuite(ActionServiceTest.class);
        suite.addTestSuite(FormTest.class);
        suite.addTestSuite(ReflectionUtilTest.class);
        suite.addTestSuite(ZFormElementMirrorTest.class);
        suite.addTestSuite(ZSerializeUtilTest.class);
        suite.addTestSuite(VariableNamesScriptTest.class);
        suite.addTestSuite(ZJsonTest.class);
        suite.addTestSuite(FormTestOpCallback.class);
        suite.addTestSuite(ZFormValuesTest.class);
        suite.addTestSuite(ZRenderClassRepositoryTest.class);
        suite.addTestSuite(ZExposedMethodRepositoryTest.class);
        suite.addTestSuite(FormWithListTest.class);
        return suite;
    }

    public static void main(String[] args) {
        TestRunner.run(AllTests.suite());
    }
}
