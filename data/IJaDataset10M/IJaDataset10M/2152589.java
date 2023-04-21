package com.metanology.mde.ui.pimEditor.model;

import org.eclipse.swt.graphics.Image;
import com.metanology.mde.core.metaModel.Component;
import com.metanology.mde.core.metaModel.MetaModelCache;
import com.metanology.mde.core.ui.common.MDEPluginImages;
import com.metanology.mde.core.ui.plugin.MDEPlugin;

/**
 * ClassNode contains data to draw graphic representation of 
 * a class. 
 * @author wwang
 */
public class ComponentNode extends LinkableNode {

    static final long serialVersionUID = 1;

    private String mcomponentId = null;

    private transient Component mcomponent = null;

    /**
     * Constructor for MetaClassNode.
     */
    public ComponentNode() {
        super();
    }

    /**
     * Returns the component.
     * @return Component
     */
    public Component getMcomponent() {
        MetaModelCache cache = MDEPlugin.getDefault().getRuntime().getModelCache();
        if (cache != null && mcomponentId != null) {
            this.mcomponent = cache.getComponent(mcomponentId);
        }
        return mcomponent;
    }

    /**
     * @see com.metanology.mde.ui.pimEditor.model.DiagramObjectNode#isValid()
     */
    public boolean isValid() {
        if (this.getMcomponent() == null) {
            return false;
        }
        return true;
    }

    /**
     * Sets the mcomponent.
     * @param mcomponent The mcomponent to set
     */
    public void init(Component mcomponent) {
        if (mcomponent != null) {
            this.mcomponentId = mcomponent.getObjId();
        } else {
            this.mcomponentId = null;
        }
        this.mcomponent = mcomponent;
    }

    /**
	 * Returns the mcomponentId.
	 * @return String
	 */
    public String getMcomponentId() {
        return mcomponentId;
    }

    /**
	 * Sets the mcomponentId.
	 * @param mcomponentId The mcomponentId to set
	 */
    public void setMcomponentId(String mcomponentId) {
        this.mcomponentId = mcomponentId;
    }

    public Image getIconImage() {
        return MDEPluginImages.get(MDEPluginImages.IMG_PIM_EDT_COMPONENT);
    }

    public String getLabel() {
        Component mcmp = getMcomponent();
        if (mcmp != null) return mcmp.getName();
        return "";
    }
}
