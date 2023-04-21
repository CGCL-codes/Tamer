package eu.medeia.ui.views;

import java.util.List;
import org.eclipse.jface.viewers.LabelProvider;
import eu.medeia.model.ESBKnowledgeRepository;
import eu.medeia.treemodel.TreeParent;
import eu.medeia.ui.internal.provider.ModelLabelProvider;

/**
 * The Class MetaModelTreeView.
 */
public class MetaModelTreeView extends TreeView {

    /**
	 * Instantiates a new meta model tree view.
	 */
    public MetaModelTreeView() {
    }

    @Override
    protected List<String> getModelIDs() {
        return ESBKnowledgeRepository.getInstance().getMetaModels();
    }

    @Override
    public LabelProvider getLabelProvider() {
        return new ModelLabelProvider();
    }

    @Override
    public void childCreated(TreeParent node) {
    }

    @Override
    public void relationCreated() {
    }
}
