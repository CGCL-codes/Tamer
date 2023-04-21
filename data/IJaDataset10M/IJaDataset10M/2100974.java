package org.thechiselgroup.choosel.visualization_component.graph.client;

public class LayoutException extends RuntimeException {

    private final String layout;

    public LayoutException(String layout) {
        super("Failed to layout graph with layout '" + layout + "'");
        this.layout = layout;
    }

    public LayoutException(String layout, Throwable cause) {
        super("Failed to layout graph with layout '" + layout + "'", cause);
        this.layout = layout;
    }

    public String getLayout() {
        return layout;
    }
}
