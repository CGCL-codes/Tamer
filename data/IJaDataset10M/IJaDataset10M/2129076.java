package org.nomadpim.module.schedule.ui.component.weekoverview;

import java.util.Date;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.nomadpim.core.ui.UIFacade;
import org.nomadpim.core.ui.util.LinkAction;
import org.nomadpim.core.ui.viewers.DateServiceWithLinkActionViewPart;
import org.nomadpim.core.util.date.DateToContainingWeekConverter;
import org.nomadpim.core.util.date.SameWeekFilter;
import org.nomadpim.module.schedule.IScheduleModuleConstants;
import org.nomadpim.schedule.ui.component.weekoverview.IWeekOverviewEntry;

public class WeekOverviewView extends DateServiceWithLinkActionViewPart {

    public static final class WeekOverviewLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

        private Color red;

        private Color green;

        public WeekOverviewLabelProvider(Color green, Color red) {
            this.green = green;
            this.red = red;
        }

        public Color getBackground(Object element, int columnIndex) {
            return null;
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            IWeekOverviewEntry entry = (IWeekOverviewEntry) element;
            switch(columnIndex) {
                case 0:
                    return entry.getDescription();
                case 1:
                    return entry.getFinished() + " / " + entry.getTaskCount();
                default:
                    throw new RuntimeException("must not be reached");
            }
        }

        public Color getForeground(Object element, int columnIndex) {
            IWeekOverviewEntry entry = (IWeekOverviewEntry) element;
            switch(columnIndex) {
                case 0:
                    return null;
                case 1:
                    return entry.getFinished() == entry.getTaskCount() ? green : red;
                default:
                    throw new RuntimeException("must not be reached");
            }
        }
    }

    public static final String ID = IScheduleModuleConstants.PLUGIN_PREFIX + "WeekOverviewView";

    private final DateToContainingWeekConverter dateConverter = new DateToContainingWeekConverter();

    private TreeViewer viewer;

    private LinkAction linkAction;

    private Color red;

    private Color green;

    @Override
    public void createPartControl(Composite parent) {
        this.red = new Color(parent.getDisplay(), new RGB(255, 0, 0));
        this.green = new Color(parent.getDisplay(), new RGB(63, 255, 63));
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(new WeekOverviewTreeContentProvider());
        viewer.setLabelProvider(new WeekOverviewLabelProvider(green, red));
        viewer.getTree().addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                TreeItem[] selection = viewer.getTree().getSelection();
                for (TreeItem item : selection) {
                    IWeekOverviewEntry node = (IWeekOverviewEntry) item.getData();
                    if (node.getObject() != null) {
                        UIFacade.getOperationService().getOperation(node.getObject().getType()).execute(node.getObject());
                    }
                }
            }

            public void widgetSelected(SelectionEvent e) {
            }
        });
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
        {
            TreeColumn column = new TreeColumn(viewer.getTree(), SWT.NONE, 0);
            column.setText("Description");
            column.setWidth(350);
        }
        {
            TreeColumn column = new TreeColumn(viewer.getTree(), SWT.NONE, 1);
            column.setText("Tasks");
            column.setWidth(40);
        }
        getViewSite().getActionBars().getToolBarManager().add(new Action("2nd view") {

            @Override
            public void run() {
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID, "2", IWorkbenchPage.VIEW_ACTIVATE);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
        linkAction = new LinkAction();
        getViewSite().getActionBars().getToolBarManager().add(linkAction);
        dateChange(new Date());
    }

    public void dateChange(Date newDate) {
        viewer.setInput(dateConverter.convert(newDate));
    }

    @Override
    public void dispose() {
        viewer.getTree().dispose();
        red.dispose();
        green.dispose();
        super.dispose();
    }

    public LinkAction getLinkAction() {
        return linkAction;
    }

    public boolean isDateChanged(Date newDate) {
        return !new SameWeekFilter(getCurrentDate()).evaluate(newDate);
    }

    @Override
    public void setFocus() {
    }
}
