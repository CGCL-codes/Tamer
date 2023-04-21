package org.gamegineer.table.ui;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import javax.swing.Icon;
import net.jcip.annotations.ThreadSafe;
import org.easymock.EasyMock;
import org.gamegineer.table.core.ComponentSurfaceDesigns;
import org.gamegineer.table.core.IComponentSurfaceDesign;

/**
 * A factory for creating various types of component surface design user
 * interface types suitable for testing.
 */
@ThreadSafe
public final class ComponentSurfaceDesignUIs {

    /**
     * Initializes a new instance of the {@code ComponentSurfaceDesignUIs}
     * class.
     */
    private ComponentSurfaceDesignUIs() {
    }

    public static IComponentSurfaceDesignUI cloneComponentSurfaceDesignUI(final IComponentSurfaceDesignUI componentSurfaceDesignUI) {
        assertArgumentNotNull(componentSurfaceDesignUI, "componentSurfaceDesignUI");
        return TableUIFactory.createComponentSurfaceDesignUI(componentSurfaceDesignUI.getId(), componentSurfaceDesignUI.getName(), componentSurfaceDesignUI.getIcon());
    }

    public static IComponentSurfaceDesignUI createComponentSurfaceDesignUI(final IComponentSurfaceDesign componentSurfaceDesign) {
        assertArgumentNotNull(componentSurfaceDesign, "componentSurfaceDesign");
        return TableUIFactory.createComponentSurfaceDesignUI(componentSurfaceDesign.getId(), componentSurfaceDesign.getId().toString(), EasyMock.createMock(Icon.class));
    }

    public static IComponentSurfaceDesignUI createUniqueComponentSurfaceDesignUI() {
        return createComponentSurfaceDesignUI(ComponentSurfaceDesigns.createUniqueComponentSurfaceDesign());
    }
}
