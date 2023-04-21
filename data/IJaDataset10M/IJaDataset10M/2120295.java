package org.coode.oae.ui;

import javax.swing.Icon;
import org.coode.oae.ui.StaticListModel.StaticListItem;
import org.coode.oae.ui.VariableListModel.VariableListItem;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLObject;
import uk.ac.manchester.mae.evaluation.BindingModel;
import uk.ac.manchester.mae.evaluation.PropertyChainCell;
import uk.ac.manchester.mae.evaluation.PropertyChainModel;

/**
 * Specialized OWLCellRenderer that deals with VariableListItems and
 * StaticListItems containing either OWLObjects or PRopertyChainModel,
 * PropertyChainCell or BindingModel objects
 */
public class RenderableObjectCellRenderer extends OWLCellRenderer {

    private OWLEditorKit kit;

    public RenderableObjectCellRenderer(OWLEditorKit edkit) {
        super(edkit);
        this.kit = edkit;
    }

    @Override
    protected String getRendering(Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof OWLObject) {
            return super.getRendering(object);
        }
        Object item = null;
        if (object instanceof VariableListItem<?>) {
            item = ((VariableListItem<?>) object).getItem();
        } else if (object instanceof StaticListItem<?>) {
            item = ((StaticListItem<?>) object).getItem();
        }
        if (item != null) {
            if (item instanceof OWLObject) {
                return super.getRendering(item);
            }
            if (item instanceof PropertyChainModel) {
                return ((PropertyChainModel) item).getCell().render(this.kit.getOWLModelManager());
            }
            if (item instanceof PropertyChainCell) {
                return ((PropertyChainCell) item).render(this.kit.getOWLModelManager());
            }
            if (item instanceof BindingModel) {
                BindingModel b = (BindingModel) item;
                return b.getIdentifier() + "=" + b.getPropertyChainModel().render(this.kit.getOWLModelManager());
            }
        }
        return object.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Icon getIcon(Object object) {
        Object item = null;
        if (object instanceof VariableListItem) {
            item = ((VariableListItem) object).getItem();
        } else if (object instanceof StaticListItem) {
            item = ((StaticListItem) object).getItem();
        }
        if (item != null) {
            return super.getIcon(item);
        }
        return super.getIcon(object);
    }
}
