package org.plazmaforge.framework.config.object;

import java.util.List;

public interface IMenuContainerConfig extends IObjectConfig {

    List<IMenuItemConfig> getChildren();

    void addChild(IMenuItemConfig menu);

    void addChild(int index, IMenuItemConfig menu);

    void addChildBefore(IMenuItemConfig menu, IMenuItemConfig selectedMenu);

    void addChildAfter(IMenuItemConfig menu, IMenuItemConfig selectedMenu);

    boolean upChild(IMenuItemConfig menu);

    boolean downChild(IMenuItemConfig menu);

    void removeChild(IMenuItemConfig menu);
}
