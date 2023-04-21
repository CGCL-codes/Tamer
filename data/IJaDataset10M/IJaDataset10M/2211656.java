package com.fh.auge.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.part.ViewPart;
import com.fh.auge.Activator;
import com.fh.auge.core.event.ApplicationEvent;
import com.fh.auge.core.event.ApplicationListener;
import com.fh.auge.core.tradelog.Trade;
import com.fh.auge.core.tradelog.TradeLogEvent;
import com.fh.auge.internal.FormatHolder;

public class TradeLogView extends ViewPart implements ApplicationListener {

    private TableViewer viewer;

    private PropertyDialogAction propertyDialogAction;

    private Action deleteItemAction;

    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        private Image upImage;

        private Image downImage;

        public String getColumnText(Object obj, int index) {
            Trade trade = (Trade) obj;
            String v = "";
            switch(index) {
                case 0:
                    v = trade.getSecurity().getName();
                    break;
                case 1:
                    v = "" + trade.getShares();
                    break;
                case 2:
                    v = FormatHolder.getInstance().getMoneyFormat().format(trade.getInvested());
                    break;
                case 3:
                    v = FormatHolder.getInstance().getMoneyFormat().format(trade.getReturned());
                    break;
                case 4:
                    v = FormatHolder.getInstance().getMoneyFormat().format(trade.getGain().getValue());
                    break;
                case 5:
                    v = FormatHolder.getInstance().getPercentFormat().format(trade.getGain().getPercantage());
                    break;
                case 6:
                    v = "" + trade.getHoldInterval().lengthInDaysInt();
                    break;
                default:
                    break;
            }
            return v;
        }

        public Image getColumnImage(Object obj, int index) {
            if (index == 0) {
                Trade trade = (Trade) obj;
                if (trade.getGain().isPositive()) return getUpImage();
                if (trade.getGain().isNegative()) return getDownImage();
            }
            return null;
        }

        private Image getUpImage() {
            if (upImage == null) upImage = Activator.getImageDescriptor("icons/uparrow.gif").createImage();
            return upImage;
        }

        private Image getDownImage() {
            if (downImage == null) downImage = Activator.getImageDescriptor("icons/downarrow.gif").createImage();
            return downImage;
        }
    }

    class NameSorter extends ViewerSorter {
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        viewer.getTable().setHeaderVisible(true);
        viewer.getTable().setLinesVisible(false);
        createColumns();
        Listener paintListener = new Listener() {

            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.MeasureItem:
                        {
                            event.height = Math.max(event.height, 18);
                            break;
                        }
                }
            }
        };
        viewer.getTable().addListener(SWT.MeasureItem, paintListener);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setSorter(new NameSorter());
        makeActions();
        hookContextMenu();
        viewer.setInput(Activator.getDefault().getTradeDao().getItems().toArray());
        Activator.getDefault().getEventMulticaster().addApplicationListener(this);
    }

    private void createColumns() {
        TableViewerColumn c = new TableViewerColumn(viewer, SWT.NONE);
        c.getColumn().setWidth(200);
        c.getColumn().setText("Name");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Shares#");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Invested");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Returned");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Profit/Loss");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Profit/Loss %");
        c = new TableViewerColumn(viewer, SWT.RIGHT);
        c.getColumn().setWidth(80);
        c.getColumn().setText("Hold(days)");
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    @Override
    public void dispose() {
        super.dispose();
        Activator.getDefault().getEventMulticaster().removeApplicationListener(this);
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof TradeLogEvent) {
            viewer.getTable().getDisplay().syncExec(new Runnable() {

                public void run() {
                    updateItems();
                }
            });
        }
    }

    private void makeActions() {
        propertyDialogAction = new PropertyDialogAction(getSite(), viewer) {

            @Override
            public void run() {
                super.run();
                viewer.refresh();
            }
        };
        propertyDialogAction.setText("Edit");
        deleteItemAction = new Action() {

            @SuppressWarnings("unchecked")
            public void run() {
                IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
                if (sel.isEmpty()) return;
                boolean result = MessageDialog.openConfirm(viewer.getControl().getShell(), "Delete", "Are you sure you want to delete item(s)?");
                if (result) {
                    try {
                        Activator.getDefault().getTradeDao().remove(sel.toList());
                        updateItems();
                    } catch (Exception e) {
                        MessageDialog.openError(getSite().getShell(), "Can't delete items", e.getMessage());
                    }
                }
            }
        };
        deleteItemAction.setText("Delete");
        deleteItemAction.setToolTipText("Delete");
        deleteItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        viewer.addSelectionChangedListener((new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateActionEnablement();
            }
        }));
        updateActionEnablement();
    }

    protected void updateActionEnablement() {
        IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
        deleteItemAction.setEnabled(!sel.isEmpty());
        propertyDialogAction.setEnabled(!sel.isEmpty() && sel.size() == 1);
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(propertyDialogAction);
        manager.add(deleteItemAction);
    }

    private void updateItems() {
        viewer.setInput(Activator.getDefault().getTradeDao().getItems().toArray());
    }
}
