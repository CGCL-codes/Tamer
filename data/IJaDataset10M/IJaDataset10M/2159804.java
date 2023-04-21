package com.google.gdt.eclipse.designer.uibinder.gef;

import com.google.gdt.eclipse.designer.uibinder.parser.UiBinderJavaParseValidator;
import org.eclipse.wb.core.editor.IDesignerEditor;
import org.eclipse.wb.internal.core.DesignerPlugin;
import org.eclipse.wb.internal.core.EnvironmentUtils;
import org.eclipse.wb.internal.core.editor.actions.SwitchAction;
import org.eclipse.wb.internal.core.editor.errors.WarningComposite;
import org.eclipse.wb.tests.gef.UiContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

/**
 * Test for {@link UiBinderJavaParseValidator}.
 * 
 * @author scheglov_ke
 */
public class UiBinderJavaParseValidatorTest extends UiBinderGefTest {

    @Override
    protected void tearDown() throws Exception {
        DesignerPlugin.setDisplayExceptionOnConsole(true);
        EnvironmentUtils.setTestingTime(true);
        super.tearDown();
    }

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    /**
   * If Java source works with UiBinder, then ui.xml should be designed instead.
   */
    public void test_showWarning() throws Exception {
        removeExceptionsListener();
        DesignerPlugin.setDisplayExceptionOnConsole(false);
        IFile javaFile = getFileSrc("test/client/Test.java");
        assertTrue(javaFile.exists());
        IEditorPart javaEditor = openJavaDesignEditor(javaFile);
        switchToDesign(javaEditor);
        UiContext context = new UiContext();
        Composite warningComposite = context.findFirstWidget(WarningComposite.class);
        assertNotNull(warningComposite);
        assertTrue(warningComposite.isVisible());
    }

    private static IEditorPart openJavaDesignEditor(IFile javaFile) throws PartInitException {
        return IDE.openEditor(DesignerPlugin.getActivePage(), javaFile, IDesignerEditor.ID);
    }

    private static void switchToDesign(IEditorPart javaEditor) {
        SwitchAction switchAction = new SwitchAction();
        switchAction.setActiveEditor(null, javaEditor);
        switchAction.run();
    }
}
