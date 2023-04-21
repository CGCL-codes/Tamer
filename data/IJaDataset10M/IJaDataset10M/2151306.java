package net.sourceforge.nattable.resize.config;

import net.sourceforge.nattable.config.AbstractUiBindingConfiguration;
import net.sourceforge.nattable.grid.GridRegion;
import net.sourceforge.nattable.resize.action.AutoResizeColumnAction;
import net.sourceforge.nattable.resize.action.ColumnResizeCursorAction;
import net.sourceforge.nattable.resize.event.ColumnResizeEventMatcher;
import net.sourceforge.nattable.resize.mode.ColumnResizeDragMode;
import net.sourceforge.nattable.ui.action.ClearCursorAction;
import net.sourceforge.nattable.ui.action.NoOpMouseAction;
import net.sourceforge.nattable.ui.binding.UiBindingRegistry;
import net.sourceforge.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;

public class DefaultColumnResizeBindings extends AbstractUiBindingConfiguration {

    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        uiBindingRegistry.registerFirstMouseMoveBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 0), new ColumnResizeCursorAction());
        uiBindingRegistry.registerMouseMoveBinding(new MouseEventMatcher(), new ClearCursorAction());
        uiBindingRegistry.registerFirstMouseDragMode(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1), new ColumnResizeDragMode());
        uiBindingRegistry.registerDoubleClickBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1), new AutoResizeColumnAction());
        uiBindingRegistry.registerSingleClickBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1), new NoOpMouseAction());
    }
}
