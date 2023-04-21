package de.hackerdan.projectcreator.ui.internal.wizard;

import org.eclipse.jface.viewers.LabelProvider;
import de.hackerdan.projectcreator.model.ProjectType;

/**
 * Zeigt den Namen von ProjektTypen an.
 *
 * @author Daniel Hirscher
 */
public class ProjectTypeLabelProvider extends LabelProvider {

    @Override
    public String getText(final Object element) {
        String text;
        if (element instanceof ProjectType) {
            final ProjectType projectType = (ProjectType) element;
            text = projectType.getName();
        } else {
            text = super.getText(element);
        }
        if (null == text) {
            text = "?";
        }
        return text;
    }
}
