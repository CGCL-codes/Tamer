package com.google.gdt.eclipse.designer.core.builders;

import com.google.gdt.eclipse.designer.Activator;
import com.google.gdt.eclipse.designer.builders.participant.MyCompilationParticipant;
import com.google.gdt.eclipse.designer.builders.participant.QuickFixer;
import com.google.gdt.eclipse.designer.common.Constants;
import com.google.gdt.eclipse.designer.core.GTestUtils;
import org.eclipse.wb.tests.designer.core.AbstractJavaTest;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link MyCompilationParticipant}.
 * 
 * @author scheglov_ke
 */
public class CompilationParticipantTest extends AbstractJavaTest {

    private static final IMarkerResolutionGenerator2 m_importFixer = new QuickFixer();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (m_testProject == null) {
            do_projectCreate();
            GTestUtils.configure(m_testProject);
            GTestUtils.createModule(m_testProject, "test.Module");
            waitForAutoBuild();
        }
        MyCompilationParticipant.ENABLED = true;
        Activator.getDefault().getPreferenceStore().setValue(Constants.P_BUILDER_CHECK_CLIENT_CLASSPATH, true);
    }

    @Override
    protected void tearDown() throws Exception {
        Activator.getDefault().getPreferenceStore().setToDefault(Constants.P_BUILDER_CHECK_CLIENT_CLASSPATH);
        super.tearDown();
        do_projectDispose();
    }

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    /**
   * Not an "source" class, so no GWT markers.
   */
    public void test_notInSourcePackage() throws Exception {
        IFile sourceFile = setFileContentSrc("ext/Test.java", getSourceDQ("package ext;", "public class Test {", "  public Test() {", "    java.awt.Image image = null;", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Valid "source" class, no GWT markers.
   */
    public void test_noMarkers() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.Composite;", "public class MyComposite extends Composite {", "  public MyComposite() {", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Valid "source" class, no GWT markers.
   */
    public void test_validSpecialCases() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.*;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    int a;", "    int[] b;", "    UIObject[] c;", "    InnerType d;", "    java.util.List<String> e;", "  }", "  private class InnerType {", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Valid "source" class, no GWT markers.
   * <p>
   * Case when valid class repeats, is not tested really, just covers this branch.
   */
    public void test_validRepeat() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.*;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    UIObject a;", "    UIObject b;", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Annotations are ignored by GWT compiler, so we also should ignore them, even if annotation
   * class is not from imported GWT module.
   */
    public void test_validAnnotation_annotationClassItself() throws Exception {
        setFileContentSrc("anns/MyAnnotation.java", getSourceDQ("package anns;", "import java.lang.annotation.*;", "@Retention(RetentionPolicy.RUNTIME)", "@Target(ElementType.TYPE)", "public @interface MyAnnotation {", "}"));
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("// filler filler filler filler filler", "package test.client;", "import anns.MyAnnotation;", "@MyAnnotation", "public class Foo {", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Annotations are ignored by GWT compiler, so we also should ignore them, even if annotation
   * class is not from imported GWT module.
   */
    public void test_validAnnotation_TypeLiteral_inAnnotation() throws Exception {
        setFileContentSrc("annotations/MyClass.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package annotations;", "public interface MyClass {", "}"));
        setFileContentSrc("annotations/MyAnnotation.java", getSourceDQ("package annotations;", "import java.lang.annotation.*;", "@Retention(RetentionPolicy.RUNTIME)", "@Target(ElementType.TYPE)", "public @interface MyAnnotation {", "  Class<?> value();", "}"));
        IFile sourceFile = setFileContentSrc("test/client/Test.java", getSourceDQ("// filler filler filler filler filler", "package test.client;", "import annotations.*;", "@MyAnnotation(MyClass.class)", "class Foo {", "}", "@MyAnnotation(value=MyClass.class)", "class Bar {", "}", ""));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Enumerations are ignored by GWT compiler, even if we import them, but not when we use them, so
   * we also should ignore them, even if enum class is not from imported GWT module.
   */
    public void test_validEnum() throws Exception {
        setFileContentSrc("en/MyEnum.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package en;", "public enum MyEnum{", "}"));
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package test.client;", "import en.MyEnum;", "public class Foo {", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * When we declare more than one type in compilation unit, then there should not be error report
   * for secondary types.
   * <p>
   * http://code.google.com/p/google-web-toolkit/issues/detail?id=5580
   */
    public void test_validSecondaryType() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/Test.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package test.client;", "public class Test {", "  SecondaryType secondary = new SecondaryType();", "}", "class SecondaryType {", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).isEmpty();
    }

    /**
   * Attempt to use class from same project that is is not from imported GWT module.
   */
    public void test_invalidSameProject() throws Exception {
        setFileContentSrc("datas/MyData.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package datas;", "public class MyData {", "}"));
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("package test.client;", "public class Foo {", "  private datas.MyData data = null;", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(1);
    }

    /**
   * Validate {@link SimpleName} which is name of type in static {@link MethodInvocation}.
   */
    public void test_invalidSimpleName_staticMethodInvocation() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("package test.client;", "import javax.swing.JFrame;", "public class Foo {", "  public Foo() {", "    JFrame.getFrames();", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(1);
    }

    /**
   * Enumerations are ignored by GWT compiler when just imported, but you can not use them.
   */
    public void test_invalidEnum() throws Exception {
        setFileContentSrc("en/MyEnum.java", getSourceDQ("// filler filler filler filler filler", "// filler filler filler filler filler", "package en;", "public enum MyEnum {", "}"));
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("package test.client;", "import en.MyEnum;", "public class Foo {", "  MyEnum e;", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(1);
    }

    /**
   * Test that we ignore template variables.
   */
    public void test_validTemplate() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/Foo.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.*;", "public class Foo {", "  public <T extends Widget> T foo(int row, int column, T widget) {", "    return widget;", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(0);
    }

    /**
   * Invalid "source" class, has GWT marker.
   * <p>
   * Case when invalid class repeats, is not tested really, just covers this branch.
   */
    public void test_invalidDuplicate() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.Composite;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    java.awt.Image image_1 = null;", "    java.awt.Image image_2 = null;", "  }", "}"));
        waitForAutoBuild();
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(2);
    }

    /**
   * Invalid "source" class, but no GWT marker, because we disable compilation participant.
   */
    public void test_noMarker_AWTClass() throws Exception {
        IFile sourceFile;
        {
            IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
            preferenceStore.setValue(Constants.P_BUILDER_CHECK_CLIENT_CLASSPATH, false);
            try {
                sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.Composite;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    java.awt.Image image = null;", "  }", "}"));
                waitForAutoBuild();
            } finally {
                preferenceStore.setToDefault(Constants.P_BUILDER_CHECK_CLIENT_CLASSPATH);
            }
        }
        IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
        assertThat(markers).hasSize(0);
    }

    /**
   * Invalid "source" class, has GWT marker.
   * <p>
   * Reason: AWT classes are not accessible in GWT.
   */
    public void test_hasMarker_AWTClass() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.Composite;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    java.awt.Image image = null;", "  }", "}"));
        waitForAutoBuild();
        IMarker marker;
        {
            IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
            assertThat(markers).hasSize(1);
            marker = markers[0];
            assertResource(sourceFile, marker.getResource());
        }
        assertFalse(m_importFixer.hasResolutions(marker));
        assertThat(m_importFixer.getResolutions(marker)).isEmpty();
    }

    /**
   * Invalid "source" class, has GWT marker.
   * <p>
   * Reason: GWT class that is accessible in general, but some GWT module should be inherited.
   */
    public void test_hasMarker_GWTClass() throws Exception {
        IFile sourceFile = setFileContentSrc("test/client/MyComposite.java", getSourceDQ("package test.client;", "import com.google.gwt.user.client.ui.Composite;", "public class MyComposite extends Composite {", "  public MyComposite() {", "    com.google.gwt.xml.client.XMLParser parser = null;", "  }", "}"));
        waitForAutoBuild();
        IMarker marker;
        {
            IMarker[] markers = GTestUtils.getMyMarkers(sourceFile);
            assertThat(markers).hasSize(1);
            marker = markers[0];
            assertResource(sourceFile, marker.getResource());
        }
        {
            assertTrue(m_importFixer.hasResolutions(marker));
            IMarkerResolution[] resolutions = m_importFixer.getResolutions(marker);
            assertThat(resolutions).hasSize(1);
            assertEquals("Import GWT module com.google.gwt.xml.XML", resolutions[0].getLabel());
            resolutions[0].run(marker);
        }
        assertEquals(getDoubleQuotes2(new String[] { "<module>", "  <inherits name='com.google.gwt.user.User'/>", "  <entry-point class='test.client.Module'/>", "  <inherits name='com.google.gwt.xml.XML'/>", "</module>" }), getFileContentSrc("/test/Module.gwt.xml"));
    }

    /**
   * Asserts that {@link IResource} has expected path relative to its project.
   */
    private static void assertResource(IResource expectedresource, IResource resource) {
        String expectedPath = resource.getProjectRelativePath().toPortableString();
        String actualPath = resource.getProjectRelativePath().toPortableString();
        assertEquals(expectedPath, actualPath);
    }
}
