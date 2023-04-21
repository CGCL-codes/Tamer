package com.jlect.swebing.ui.client;

import com.jlect.swebing.renderers.client.DefaultRenderers;
import com.jlect.swebing.renderers.client.RenderersConfig;
import com.jlect.swebing.renderers.client.TextAreaRenderer;
import com.jlect.swebing.renderers.client.TextRenderer;

/**
 * Text area component implementation
 *
 * @author Sergey Kozmin
 * @since 19.11.2007 10:40:04
 */
public class GTextArea extends TextComponentsBase {

    public GTextArea(String text) {
        super(text);
    }

    public GTextArea() {
        this("");
    }

    protected TextRenderer createTextRenderer() {
        return (TextAreaRenderer) RenderersConfig.getRenderersFactory().createComponentRenderer(DefaultRenderers.TEXTAREA.getName());
    }
}
