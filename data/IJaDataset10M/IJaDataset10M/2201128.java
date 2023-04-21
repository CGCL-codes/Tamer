package com.google.gdt.eclipse.gph.egit.wizard;

import com.google.gdt.eclipse.gph.egit.EGitCheckoutProviderPlugin;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provider for git repo locations.
 */
class RepositoryLabelProvider extends LabelProvider {

    /**
   * Create a new RepositoryLabelProvider.
   */
    public RepositoryLabelProvider() {
    }

    @Override
    public Image getImage(Object element) {
        return EGitCheckoutProviderPlugin.getImage("repository_rep.gif");
    }
}
