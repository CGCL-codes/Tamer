package com.google.gxp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * GXP Test Suite Builder
 */
public class AllTests extends TestCase {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(com.google.gxp.base.GxpContextTest.class);
        suite.addTestSuite(com.google.gxp.css.ColorTest.class);
        suite.addTestSuite(com.google.gxp.css.CssAppenderTest.class);
        suite.addTestSuite(com.google.gxp.html.HtmlClosuresTest.class);
        suite.addTestSuite(com.google.gxp.js.JavascriptAppenderTest.class);
        suite.addTestSuite(com.google.gxp.js.JavascriptClosuresTest.class);
        suite.addTestSuite(com.google.gxp.text.PlaintextAppenderTest.class);
        suite.addTestSuite(com.google.gxp.text.PlaintextClosuresTest.class);
        suite.addTestSuite(com.google.gxp.compiler.CompilationSetTest.class);
        suite.addTestSuite(com.google.gxp.compiler.GxpcTestCaseTest.class);
        suite.addTestSuite(com.google.gxp.compiler.alerts.AlertTest.class);
        suite.addTestSuite(com.google.gxp.compiler.alerts.AlertSetTest.class);
        suite.addTestSuite(com.google.gxp.compiler.alerts.SourcePositionTest.class);
        suite.addTestSuite(com.google.gxp.compiler.alerts.UniquifyingAlertSinkTest.class);
        suite.addTestSuite(com.google.gxp.compiler.ant.GxpcTaskTest.class);
        suite.addTestSuite(com.google.gxp.compiler.cli.GxpcFlagsTest.class);
        suite.addTestSuite(com.google.gxp.compiler.cli.GxpcTest.class);
        suite.addTestSuite(com.google.gxp.compiler.collapse.SpaceCollapserTest.class);
        suite.addTestSuite(com.google.gxp.compiler.collapse.SpaceOperatorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.depend.DependencyCheckingTest.class);
        suite.addTestSuite(com.google.gxp.compiler.depend.SerializabilityTest.class);
        suite.addTestSuite(com.google.gxp.compiler.fs.FileRefTest.class);
        suite.addTestSuite(com.google.gxp.compiler.fs.InMemoryFileSystemTest.class);
        suite.addTestSuite(com.google.gxp.compiler.fs.SourcePathFileSystemTest.class);
        suite.addTestSuite(com.google.gxp.compiler.fs.SystemFileSystemTest.class);
        suite.addTestSuite(com.google.gxp.compiler.io.CIndenterTest.class);
        suite.addTestSuite(com.google.gxp.compiler.parser.NamespaceSetTest.class);
        suite.addTestSuite(com.google.gxp.compiler.parser.ParserTest.class);
        suite.addTestSuite(com.google.gxp.compiler.reparent.AttributeMapTest.class);
        suite.addTestSuite(com.google.gxp.compiler.reparent.EditablePartsTest.class);
        suite.addTestSuite(com.google.gxp.compiler.reparent.ReparenterTest.class);
        suite.addTestSuite(com.google.gxp.compiler.schema.SchemaParserTest.class);
        suite.addTestSuite(com.google.gxp.compiler.servicedir.ScopedServiceDirectoryTest.class);
        suite.addTestSuite(com.google.gxp.compiler.xmb.XmlCharsetEscaperTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.AnnotateErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.AttributeBundleErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.BasicErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.CallErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.ConditionalErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.I18nErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.InstantiableErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.InterfaceErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.LoopErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.MultiLingualErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.OutputElementErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.ParseErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.SchemaErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.TemplateErrorTest.class);
        suite.addTestSuite(com.google.gxp.compiler.errortests.UnextractableContentAlertTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.annotate.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.bundle.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.call.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.closures.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.i18n.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.instantiable.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.functests.multilingual.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.dynamictests.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.compiler.dynamictests.DynamicTest.class);
        suite.addTestSuite(com.google.gxp.rss.JavaCodeTest.class);
        suite.addTestSuite(com.google.gxp.rss.RssAppenderTest.class);
        suite.addTestSuite(com.google.gxp.rss.RssClosuresTest.class);
        return suite;
    }
}
