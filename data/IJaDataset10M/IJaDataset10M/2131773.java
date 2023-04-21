package com.android.ide.eclipse.adt.internal.editors.manifest.descriptors;

import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiResourceAttributeNode;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;

/**
 * Describes a Theme/Style XML attribute displayed by a {@link UiResourceAttributeNode}
 */
public final class ThemeAttributeDescriptor extends TextAttributeDescriptor {

    public ThemeAttributeDescriptor(String xmlLocalName, String uiName, String nsUri, String tooltip) {
        super(xmlLocalName, uiName, nsUri, tooltip);
    }

    /**
     * @return A new {@link UiResourceAttributeNode} linked to this theme descriptor.
     */
    @Override
    public UiAttributeNode createUiNode(UiElementNode uiParent) {
        return new UiResourceAttributeNode(ResourceType.STYLE, this, uiParent);
    }
}
