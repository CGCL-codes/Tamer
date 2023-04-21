package com.mobileares.midp.widgets.client.tree;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-3-22
 * Time: 16:04:36
 * To change this template use File | Settings | File Templates.
 */
public interface CTreeResource extends ClientBundle {

    /**
     * An image indicating a closed branch.
     */
    ImageResource treeClosed();

    /**
     * An image indicating a leaf.
     */
    ImageResource treeLeaf();

    /**
     * An image indicating an open branch.
     */
    ImageResource treeOpen();
}
