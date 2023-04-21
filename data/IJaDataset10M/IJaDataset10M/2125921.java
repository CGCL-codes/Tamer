package com.doculibre.wicket.panels.tree.view.normal;

import wicket.ResourceReference;
import com.doculibre.wicket.panels.tree.view.ITreeViewConfiguration;

/**
 * Configuration par defaut de l'aspect visuel de l'arbre
 * @author Francis Baril
 *
 */
public class NormalTreeViewConfiguration implements ITreeViewConfiguration {

    public ResourceReference getEmptyImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_empty.gif");
    }

    public ResourceReference getVerticalJoinImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_vertical.gif");
    }

    /**
	 * @return l'image affichée lorsque un élément n'est pas le dernier enfant de son père
	 */
    public ResourceReference getJoinBottomImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_joinbottom.gif");
    }

    /**
	 * @return l'image affichée lorsque un élément est le dernier enfant de son père
	 */
    public ResourceReference getJoinImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_join.gif");
    }

    /**
	 * @return l'image affichée lorsque un élément n'est pas explosable
	 */
    public ResourceReference getUnexpandableImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_horizontal.gif");
    }

    /**
	 * @return l'image affichée lorsque un élément est explosé pour cacher ses enfants
	 */
    public ResourceReference getHideImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_minus.gif");
    }

    /**
	 * @return l'image affichée lorsque un élément n'est pas explosé mais peut l'être
	 */
    public ResourceReference getExpandableImage() {
        return new ResourceReference(NormalTreeViewConfiguration.class, "tree_plus.gif");
    }
}
