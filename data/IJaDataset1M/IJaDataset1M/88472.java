package eu.medeia.ui.internal.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import eu.medeia.model.ESBKnowledgeRepository;

/**
 * The Class IconLabelProvider.
 */
public class IconLabelProvider extends LabelProvider {

    public String getText(Object obj) {
        return obj.toString();
    }

    public Image getImage(Object obj) {
        return ESBKnowledgeRepository.getInstance().getIcon(obj.toString());
    }
}
