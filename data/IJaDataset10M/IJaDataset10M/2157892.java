package net.community.chest.swing.component.tree;

import javax.swing.tree.TreeModel;
import net.community.chest.awt.dom.UIReflectiveAttributesProxy;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <M> The reflected {@link TreeModel}
 * @author Lyor G.
 * @since Aug 21, 2008 12:52:48 PM
 */
public abstract class TreeModelReflectiveProxy<M extends TreeModel> extends UIReflectiveAttributesProxy<M> {

    protected TreeModelReflectiveProxy(Class<M> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    protected TreeModelReflectiveProxy(Class<M> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }
}
