package org.jowidgets.common.widgets;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentObservable;
import org.jowidgets.common.widgets.controller.IFocusObservable;
import org.jowidgets.common.widgets.controller.IKeyObservable;
import org.jowidgets.common.widgets.controller.IMouseObservable;
import org.jowidgets.common.widgets.controller.IPopupDetectionObservable;

public interface IComponentCommon extends IWidgetCommon, IComponentObservable, IFocusObservable, IKeyObservable, IMouseObservable, IPopupDetectionObservable {

    /**
	 * Marks the widget that a redraw is necessary
	 */
    void redraw();

    /**
	 * Enables or disabled the redraw of a component and its children.
	 * 
	 * If redraw is disabled, all changes of the component will not be made visible until
	 * redraw will be enabled again.
	 * 
	 * REMARK: This is a hint and will not work for all platforms
	 * 
	 * @param enabled The enabled state
	 */
    void setRedrawEnabled(boolean enabled);

    /**
	 * Try's to get the focus for the component. This is not always possible.
	 * 
	 * Developers must not assume that requesting the focus guarantee's that the
	 * component gets the focus. Only if the focusGained event was fired, the component
	 * has the focus.
	 * 
	 * @return false if the request definitively fails, true if it may succeed.
	 */
    boolean requestFocus();

    void setForegroundColor(final IColorConstant colorValue);

    void setBackgroundColor(final IColorConstant colorValue);

    IColorConstant getForegroundColor();

    IColorConstant getBackgroundColor();

    void setCursor(final Cursor cursor);

    void setVisible(final boolean visible);

    boolean isVisible();

    Dimension getSize();

    void setSize(final Dimension size);

    Position getPosition();

    void setPosition(final Position position);
}
