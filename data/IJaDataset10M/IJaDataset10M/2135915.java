package org.ufacekit.ui.core;

import org.junit.Before;
import org.junit.Test;
import org.ufacekit.ui.core.controls.UIComposite;
import org.ufacekit.ui.core.controls.UIApplicationWindow;
import org.ufacekit.ui.core.controls.UIWindow;
import org.ufacekit.ui.core.layouts.GridLayoutData;
import org.ufacekit.ui.core.layouts.UILayoutData;
import static junit.framework.Assert.*;

/**
 * @param <C>
 */
public abstract class UIElementTestCase<C extends UIElement> {

    private C element1;

    private C element2;

    private C element3;

    private UIApplicationWindow window;

    private UIComposite composite;

    private UIFactory<?> factory;

    /**
	 * 
	 */
    @Before
    public void createUI() {
        factory = doGetFactory();
        window = factory.createApplicationWindow(null, new UIApplicationWindow.ApplicationWindowUIInfo(factory.createFillLayout()));
        composite = factory.createComposite(window, new UIComposite.CompositeUIInfo(null, factory.createGridLayout(1, false)));
        element1 = doCreateUIElement(composite, new GridLayoutData(GridLayoutData.ALIGN_BEGINNING, GridLayoutData.DEFAULT, false, false));
        element2 = doCreateUIElement(composite, GridLayoutData.fillBothData());
        element3 = doCreateUIElement(composite, GridLayoutData.fillBothData());
        window.open();
    }

    /**
	 * @return the element
	 */
    protected C getElement1() {
        return element1;
    }

    /**
	 * @return the element
	 */
    protected C getElement2() {
        return element2;
    }

    /**
	 * @return the element
	 */
    protected C getElement3() {
        return element3;
    }

    /**
	 * @return the composite
	 */
    protected UIComposite getComposite() {
        return composite;
    }

    /**
	 * @return window
	 */
    protected UIWindow getControlsWindow() {
        return window;
    }

    /**
	 * @param comp
	 *            the composite
	 * @param layoutData
	 * @return the control
	 */
    protected abstract C doCreateUIElement(UIComposite comp, UILayoutData layoutData);

    /**
	 * @return the factory
	 */
    protected abstract UIFactory<?> doGetFactory();

    /**
	 * 
	 */
    @Test
    public void getFactory() {
        assertNotNull("The factory is not allowed to be null", getElement1().getFactory());
        assertSame("The factory has to be the one the widget is created with.", doGetFactory(), getElement1().getFactory());
    }

    /**
	 * 
	 */
    @Test
    public void getApplicationContext() {
        assertNotNull("The application context is not allowed to be null", getElement1().getApplicationContext());
    }

    /**
	 * 
	 */
    @Test
    public void getId() {
        assertNotNull("The id is not allowed to be null", getElement1().getId());
    }
}
