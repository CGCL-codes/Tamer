package hub.sam.mof.simulator.editor.diagram.edit.parts;

import hub.sam.mof.simulator.editor.diagram.part.M3ActionsVisualIDRegistry;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

/**
 * @generated
 */
public class M3ActionsEditPartFactory implements EditPartFactory {

    /**
	 * @generated
	 */
    public EditPart createEditPart(EditPart context, Object model) {
        if (model instanceof View) {
            View view = (View) model;
            switch(M3ActionsVisualIDRegistry.getVisualID(view)) {
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageEditPart.VISUAL_ID:
                    return new EPackageEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassEditPart.VISUAL_ID:
                    return new EClassEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassNameEditPart.VISUAL_ID:
                    return new EClassNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EPackage2EditPart.VISUAL_ID:
                    return new EPackage2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageNameEditPart.VISUAL_ID:
                    return new EPackageNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationEditPart.VISUAL_ID:
                    return new EAnnotationEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationSourceEditPart.VISUAL_ID:
                    return new EAnnotationSourceEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeEditPart.VISUAL_ID:
                    return new EDataTypeEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeNameEditPart.VISUAL_ID:
                    return new EDataTypeNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeInstanceClassNameEditPart.VISUAL_ID:
                    return new EDataTypeInstanceClassNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumEditPart.VISUAL_ID:
                    return new EEnumEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumNameEditPart.VISUAL_ID:
                    return new EEnumNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassEditPart.VISUAL_ID:
                    return new MClassEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassNameEditPart.VISUAL_ID:
                    return new MClassNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MActivityEditPart.VISUAL_ID:
                    return new MActivityEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.WrappingLabelEditPart.VISUAL_ID:
                    return new WrappingLabelEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAttributeEditPart.VISUAL_ID:
                    return new EAttributeEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EOperationEditPart.VISUAL_ID:
                    return new EOperationEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperation2EditPart.VISUAL_ID:
                    return new MOperation2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataType2EditPart.VISUAL_ID:
                    return new EDataType2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassName2EditPart.VISUAL_ID:
                    return new EClassName2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnum2EditPart.VISUAL_ID:
                    return new EEnum2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeName2EditPart.VISUAL_ID:
                    return new EDataTypeName2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeInstanceClassName2EditPart.VISUAL_ID:
                    return new EDataTypeInstanceClassName2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnum3EditPart.VISUAL_ID:
                    return new EEnum3EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumName2EditPart.VISUAL_ID:
                    return new EEnumName2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EStringToStringMapEntryEditPart.VISUAL_ID:
                    return new EStringToStringMapEntryEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAttribute2EditPart.VISUAL_ID:
                    return new EAttribute2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EOperation2EditPart.VISUAL_ID:
                    return new EOperation2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperationEditPart.VISUAL_ID:
                    return new MOperationEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperation3EditPart.VISUAL_ID:
                    return new MOperation3EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassAttributesEditPart.VISUAL_ID:
                    return new EClassAttributesEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassOperationsEditPart.VISUAL_ID:
                    return new EClassOperationsEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageContentsEditPart.VISUAL_ID:
                    return new EPackageContentsEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassAttributes2EditPart.VISUAL_ID:
                    return new EClassAttributes2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassOperations2EditPart.VISUAL_ID:
                    return new EClassOperations2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumLiteralsEditPart.VISUAL_ID:
                    return new EEnumLiteralsEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationDetailsEditPart.VISUAL_ID:
                    return new EAnnotationDetailsEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumLiterals2EditPart.VISUAL_ID:
                    return new EEnumLiterals2EditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassMAttributesEditPart.VISUAL_ID:
                    return new MClassMAttributesEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassMOperationsEditPart.VISUAL_ID:
                    return new MClassMOperationsEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationReferencesEditPart.VISUAL_ID:
                    return new EAnnotationReferencesEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EReferenceEditPart.VISUAL_ID:
                    return new EReferenceEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EReferenceNameEditPart.VISUAL_ID:
                    return new EReferenceNameEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EReferenceLowerBoundUpperBoundEditPart.VISUAL_ID:
                    return new EReferenceLowerBoundUpperBoundEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassESuperTypesEditPart.VISUAL_ID:
                    return new EClassESuperTypesEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassMetaObjectEditPart.VISUAL_ID:
                    return new MClassMetaObjectEditPart(view);
                case hub.sam.mof.simulator.editor.diagram.edit.parts.WrappingLabel2EditPart.VISUAL_ID:
                    return new WrappingLabel2EditPart(view);
            }
        }
        return createUnrecognizedEditPart(context, model);
    }

    /**
	 * @generated
	 */
    private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
        return null;
    }

    /**
	 * @generated
	 */
    public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
        if (source.getFigure() instanceof WrappingLabel) return new TextCellEditorLocator((WrappingLabel) source.getFigure()); else {
            return new LabelCellEditorLocator((Label) source.getFigure());
        }
    }

    /**
	 * @generated
	 */
    private static class TextCellEditorLocator implements CellEditorLocator {

        /**
		 * @generated
		 */
        private WrappingLabel wrapLabel;

        /**
		 * @generated
		 */
        public TextCellEditorLocator(WrappingLabel wrapLabel) {
            this.wrapLabel = wrapLabel;
        }

        /**
		 * @generated
		 */
        public WrappingLabel getWrapLabel() {
            return wrapLabel;
        }

        /**
		 * @generated
		 */
        public void relocate(CellEditor celleditor) {
            Text text = (Text) celleditor.getControl();
            Rectangle rect = getWrapLabel().getTextBounds().getCopy();
            getWrapLabel().translateToAbsolute(rect);
            if (getWrapLabel().isTextWrapOn() && getWrapLabel().getText().length() > 0) {
                rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
            } else {
                int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
                rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
            }
            if (!rect.equals(new Rectangle(text.getBounds()))) {
                text.setBounds(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }

    /**
	 * @generated
	 */
    private static class LabelCellEditorLocator implements CellEditorLocator {

        /**
		 * @generated
		 */
        private Label label;

        /**
		 * @generated
		 */
        public LabelCellEditorLocator(Label label) {
            this.label = label;
        }

        /**
		 * @generated
		 */
        public Label getLabel() {
            return label;
        }

        /**
		 * @generated
		 */
        public void relocate(CellEditor celleditor) {
            Text text = (Text) celleditor.getControl();
            Rectangle rect = getLabel().getTextBounds().getCopy();
            getLabel().translateToAbsolute(rect);
            int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
            rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
            if (!rect.equals(new Rectangle(text.getBounds()))) {
                text.setBounds(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }
}
