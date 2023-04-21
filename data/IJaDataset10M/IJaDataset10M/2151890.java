package org.eclipse.tptp.test.tools.web.ui.ass.editor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.hyades.models.common.testprofile.TPFTestSuite;
import org.eclipse.hyades.models.common.util.ICommonConstants;
import org.eclipse.hyades.test.ui.TestUIExtension;
import org.eclipse.hyades.test.ui.internal.editor.BaseEditorPart;
import org.eclipse.hyades.ui.HyadesUIPlugin;
import org.eclipse.hyades.ui.extension.IAssociationDescriptor;
import org.eclipse.hyades.ui.extension.IAssociationMapping;

public class WebAssertionEditor extends BaseEditorPart {

    public static final String EDITOR_ID = "org.eclipse.tptp.test.tools.web.ui.editor.WebAssertionEditor";

    private String testSuiteType;

    /**
	 * Constructor for WebAssertionEditor
	 */
    public WebAssertionEditor() {
        super(TPFTestSuite.class);
    }

    /**
	 * @see org.eclipse.hyades.ui.editor.IHyadesEditorPart#getFileExtension()
	 */
    public String getFileExtension() {
        return ICommonConstants.TEST_SUITE_FILE_EXTENSION;
    }

    /**
	 * Returns the type of this editor's test suite.
	 * @return String
	 */
    public String getTestSuiteType() {
        return testSuiteType;
    }

    /**
	 * @see org.eclipse.hyades.test.ui.internal.editor.EMFEditorPart#identifyEditorDescriptor(org.eclipse.emf.ecore.EObject)
	 */
    protected IAssociationDescriptor identifyEditorDescriptor(EObject eObject) {
        TPFTestSuite testSuite = (TPFTestSuite) eObject;
        testSuiteType = testSuite.getType();
        TestUIExtension.registerTestSuiteType(testSuiteType);
        IAssociationMapping editorAssociationMapping = TestUIExtension.getTestSuiteMappingRegistry().getAssociationMapping(HyadesUIPlugin.EP_EDITOR_EXTENSIONS);
        return editorAssociationMapping.getAssociationDescriptor(testSuiteType, "org.eclipse.tptp.test.tools.web.ui.editor.WebAssertionEditorExtension");
    }
}
