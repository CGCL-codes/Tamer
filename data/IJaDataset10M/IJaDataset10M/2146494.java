package org.techienet.jspmenu.renderer;

import org.techienet.jspmenu.model.MenuItem;
import org.techienet.jspmenu.provider.MenuProvider;

public interface MenuRenderer {

    public abstract String render(MenuItem menu);
}
