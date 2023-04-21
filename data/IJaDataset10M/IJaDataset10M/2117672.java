package org.openmobster.core.mobileCloud.api.ui.framework.navigation;

/**
 * @author openmobster@gmail.com
 *
 */
public abstract class Screen {

    private String id;

    public String getId() {
        return this.id;
    }

    void setId(String id) {
        this.id = id;
    }

    public abstract void render();

    public abstract Object getContentPane();

    public abstract void postRender();
}
