package org.eclipse.uml2.uml.editor.dialogs;

import java.util.Map;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.editor.UMLEditorPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;

public class Profile2EPackageConverterOptionsDialog extends OptionsDialog {

    public Profile2EPackageConverterOptionsDialog(Shell parent, String title, String message, Map<String, String> options) {
        super(parent, title, message, options);
    }

    @Override
    protected void createOptionAreas(Composite parent) {
        super.createOptionAreas(parent);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_EcoreTaggedValues_label"), UMLUtil.UML2EcoreConverter.OPTION__ECORE_TAGGED_VALUES, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, processChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_DerivedFeatures_label"), UMLUtil.UML2EcoreConverter.OPTION__DERIVED_FEATURES, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_DuplicateFeatureInheritance_label"), UMLUtil.UML2EcoreConverter.OPTION__DUPLICATE_FEATURE_INHERITANCE, new String[] { ignoreChoiceLabel, reportChoiceLabel, discardChoiceLabel, processChoiceLabel }, processChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_DuplicateFeatures_label"), UMLUtil.UML2EcoreConverter.OPTION__DUPLICATE_FEATURES, new String[] { ignoreChoiceLabel, reportChoiceLabel, discardChoiceLabel, processChoiceLabel }, processChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_DuplicateOperationInheritance_label"), UMLUtil.UML2EcoreConverter.OPTION__DUPLICATE_OPERATION_INHERITANCE, new String[] { ignoreChoiceLabel, reportChoiceLabel, discardChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_DuplicateOperations_label"), UMLUtil.UML2EcoreConverter.OPTION__DUPLICATE_OPERATIONS, new String[] { ignoreChoiceLabel, reportChoiceLabel, discardChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_RedefiningOperations_label"), UMLUtil.UML2EcoreConverter.OPTION__REDEFINING_OPERATIONS, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_RedefiningProperties_label"), UMLUtil.UML2EcoreConverter.OPTION__REDEFINING_PROPERTIES, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_SubsettingProperties_label"), UMLUtil.UML2EcoreConverter.OPTION__SUBSETTING_PROPERTIES, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_UnionProperties_label"), UMLUtil.UML2EcoreConverter.OPTION__UNION_PROPERTIES, new String[] { ignoreChoiceLabel, reportChoiceLabel, reportChoiceLabel }, processChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_SuperClassOrder_label"), UMLUtil.UML2EcoreConverter.OPTION__SUPER_CLASS_ORDER, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_AnnotationDetails_label"), UMLUtil.UML2EcoreConverter.OPTION__ANNOTATION_DETAILS, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, reportChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_InvariantConstraints_label"), UMLUtil.UML2EcoreConverter.OPTION__INVARIANT_CONSTRAINTS, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, ignoreChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_OperationBodies_label"), UMLUtil.UML2EcoreConverter.OPTION__OPERATION_BODIES, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, ignoreChoiceLabel);
        createOptionArea(parent, UMLEditorPlugin.INSTANCE.getString("_UI_Comments_label"), UMLUtil.UML2EcoreConverter.OPTION__COMMENTS, new String[] { ignoreChoiceLabel, reportChoiceLabel, processChoiceLabel }, ignoreChoiceLabel);
    }
}
