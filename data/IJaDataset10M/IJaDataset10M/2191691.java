package org.nightlabs.jfire.base.ui.prop.structedit;

import org.eclipse.swt.graphics.Image;
import org.nightlabs.i18n.I18nText;

public abstract class TreeNode {

    public abstract String getLabel();

    public abstract I18nText getI18nText();

    public abstract boolean hasChildren();

    public abstract TreeNode[] getChildren();

    public abstract boolean isEditable();

    public abstract Image getImage();
}
