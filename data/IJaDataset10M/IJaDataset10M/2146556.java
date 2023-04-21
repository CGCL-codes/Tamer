package org.kalypso.nofdpidss.ui.view.projectsetup.navigation.timeseriesmanager.commands;

import java.util.Map;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.kalypso.ogc.sensor.timeseries.TimeserieConstants;

/**
 * @author Dirk Kuch
 */
public class TSMCommandW extends AbstractHandler implements IHandler, IElementUpdater {

    public static final String ID = "org.kalypso.nofdpidss.ui.application.time.series.manager.command.w";

    /**
   * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
    @Override
    public Object execute(final ExecutionEvent e) {
        final Event trigger = (Event) e.getTrigger();
        final ToolItem tool = (ToolItem) trigger.widget;
        final boolean enabled = tool.getSelection();
        TSMCommandHelper.hideTypes(TimeserieConstants.TYPE_WATERLEVEL, !enabled);
        return null;
    }

    /**
   * @see org.eclipse.ui.commands.IElementUpdater#updateElement(org.eclipse.ui.menus.UIElement, java.util.Map)
   */
    @SuppressWarnings("unchecked")
    public void updateElement(final UIElement element, final Map parameters) {
        final boolean hasAxis = TSMCommandHelper.hasAxisOfType(TimeserieConstants.TYPE_WATERLEVEL);
        element.setChecked(hasAxis);
    }
}
