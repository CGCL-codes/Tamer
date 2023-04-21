package com.jlect.swebing.renderers.client;

public interface RenderersFactory {

    /**
     * Creates renderer by its component id
     * @param componentId component id, for example "button"
     * @return component renderer instance
     */
    public Renderer createComponentRenderer(String componentId);
}
