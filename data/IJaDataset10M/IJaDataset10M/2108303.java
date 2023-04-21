package com.softaspects.jsf.component.container;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;

public interface LayoutManager {

    public void doLayout(ContainerInterface container);

    public Renderer getRenderer(FacesContext context);

    public boolean ignoreParentStyle();
}
