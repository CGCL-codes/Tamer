package net.sourceforge.nattable.extension.glazedlists.groupBy;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.config.AbstractUiBindingConfiguration;
import net.sourceforge.nattable.extension.glazedlists.groupBy.command.UngroupByColumnIndexCommand;
import net.sourceforge.nattable.layer.LabelStack;
import net.sourceforge.nattable.layer.cell.LayerCell;
import net.sourceforge.nattable.ui.NatEventData;
import net.sourceforge.nattable.ui.binding.UiBindingRegistry;
import net.sourceforge.nattable.ui.matcher.MouseEventMatcher;
import net.sourceforge.nattable.ui.menu.IMenuItemProvider;
import net.sourceforge.nattable.ui.menu.MenuItemProviders;
import net.sourceforge.nattable.ui.menu.PopupMenuAction;
import net.sourceforge.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class GroupByHeaderMenuConfiguration extends AbstractUiBindingConfiguration {

    private final NatTable natTable;

    private final GroupByHeaderLayer groupByHeaderLayer;

    public GroupByHeaderMenuConfiguration(NatTable natTable, GroupByHeaderLayer groupByHeaderLayer) {
        this.natTable = natTable;
        this.groupByHeaderLayer = groupByHeaderLayer;
    }

    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, GroupByHeaderLayer.GROUP_BY_REGION, MouseEventMatcher.RIGHT_BUTTON) {

            @Override
            public boolean matches(NatTable natTable, MouseEvent event, LabelStack regionLabels) {
                if (super.matches(natTable, event, regionLabels)) {
                    int groupByColumnIndex = groupByHeaderLayer.getGroupByColumnIndexAtXY(event.x, event.y);
                    return groupByColumnIndex >= 0;
                }
                return false;
            }
        }, new PopupMenuAction(new PopupMenuBuilder(natTable).withMenuItemProvider(new IMenuItemProvider() {

            public void addMenuItem(final NatTable natTable, Menu popupMenu) {
                MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
                menuItem.setText("Ungroup By");
                menuItem.setEnabled(true);
                menuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        NatEventData natEventData = MenuItemProviders.getNatEventData(event);
                        int columnPosition = natEventData.getColumnPosition();
                        int rowPosition = natEventData.getRowPosition();
                        MouseEvent originalEvent = natEventData.getOriginalEvent();
                        LayerCell cell = natTable.getCellByPosition(columnPosition, rowPosition);
                        Rectangle bounds = cell.getBounds();
                        int groupByColumnIndex = groupByHeaderLayer.getGroupByColumnIndexAtXY(originalEvent.x - bounds.x, originalEvent.y - bounds.y);
                        natTable.doCommand(new UngroupByColumnIndexCommand(groupByColumnIndex));
                    }
                });
            }
        }).build()));
    }
}
