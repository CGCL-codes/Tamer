package jsesh.editor;

import java.util.Iterator;
import jsesh.mdc.model.ModelElement;
import jsesh.mdc.model.TopItem;
import jsesh.mdc.model.operations.ChildOperation;
import jsesh.mdc.model.operations.Deletion;
import jsesh.mdc.model.operations.Insertion;
import jsesh.mdc.model.operations.ModelOperationVisitor;
import jsesh.mdc.model.operations.Modification;
import jsesh.mdc.model.operations.Replacement;
import jsesh.mdc.model.operations.ZoneModification;
import jsesh.mdcDisplayer.layout.SimpleViewBuilder;
import jsesh.mdcDisplayer.mdcView.MDCView;

/**
 * Updates an editor's view to keep it synchronized with its model.
 * @author S. Rosmorduc
 */
class MDCViewUpdater implements ModelOperationVisitor {

    private final JMDCEditor editor;

    /**
	 * @param editor
	 */
    MDCViewUpdater(JMDCEditor editor) {
        this.editor = editor;
    }

    public void visitChildOperation(ChildOperation operation) {
        int k;
        SimpleViewBuilder builder = new SimpleViewBuilder();
        for (k = 0; k < this.editor.documentView.getNumberOfSubviews() && this.editor.documentView.getSubView(k).getModel() != operation.getChildOperation().getElement(); k++) ;
        if (this.editor.documentView.getSubView(k).getModel() == operation.getChildOperation().getElement()) {
            MDCView subv = builder.buildView(operation.getChildOperation().getElement(), editor.getDrawingSpecifications());
            this.editor.documentView.replaceSubView(k, subv);
        }
        builder.reLayout(this.editor.documentView, editor.getDrawingSpecifications());
    }

    public void visitDeletion(Deletion deletion) {
        this.editor.documentView.remove(deletion.getStart(), deletion.getEnd());
        new SimpleViewBuilder().reLayout(this.editor.documentView, editor.getDrawingSpecifications());
    }

    public void visitInsertion(Insertion insertion) {
        SimpleViewBuilder builder = new SimpleViewBuilder();
        int index = insertion.getIndex();
        for (Iterator i = insertion.getChildren().iterator(); i.hasNext(); ) {
            MDCView subView = builder.buildView((ModelElement) i.next(), editor.getDrawingSpecifications());
            this.editor.documentView.addAt(index++, subView);
        }
        builder.reLayout(this.editor.documentView, editor.getDrawingSpecifications());
    }

    public void visitModification(Modification modification) {
        this.editor.documentView = null;
    }

    public void visitReplacement(Replacement replacement) {
        this.editor.documentView = null;
    }

    public void visitZoneModification(ZoneModification modification) {
        SimpleViewBuilder builder = new SimpleViewBuilder();
        for (int i = modification.getStart(); i < modification.getEnd(); i++) {
            TopItem it = this.editor.getHieroglyphicTextModel().getModel().getTopItemAt(i);
            MDCView v = builder.buildView(it, editor.getDrawingSpecifications());
            editor.documentView.replaceSubView(i, v);
        }
        builder.reLayout(this.editor.documentView, editor.getDrawingSpecifications());
    }
}
