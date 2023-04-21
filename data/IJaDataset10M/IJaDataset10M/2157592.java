package com.showdown.ui.allshows;

import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import com.dlist.DList;
import com.dlist.api.IDListListener;
import com.showdown.api.IEpisode;
import com.showdown.api.IShow;
import com.showdown.api.IShowListCache;
import com.showdown.api.IShowListCache.ShowCacheEvent;
import com.showdown.api.IShowListCache.ShowCacheListener;
import com.showdown.api.impl.ShowDownManager;
import com.showdown.log.ShowDownLog;
import com.showdown.resource.Messages;
import com.showdown.ui.FilterBox;
import com.showdown.ui.IStackSubPage;
import com.showdown.ui.ImageRegistry;
import com.showdown.ui.ImageRegistry.ShowDownImage;
import com.showdown.ui.PageStack.PageStackPages;
import com.showdown.ui.allshows.AllShowsPage.AllShowsSubPages;
import com.showdown.ui.dialog.NewShowImportDialog;
import com.showdown.ui.dialog.UpdateEndedDialog;

/**
 * IStack sub-page of {@link AllShowsPage} for showing the list of all shows.
 * @author Mat DeLong
 */
public class AllShowsListPage extends AbstractAddShowPage implements IStackSubPage, SelectionListener {

    private Composite body;

    private AllShowsPage parentPage;

    private FilterBox box;

    private DList<IShow> list;

    private MenuItem endedItem;

    private boolean active;

    /**
    * Constructor which specifies the parent Composite for this page
    * @param parent the parent composite
    * @param parentPage the parent stack page
    */
    public AllShowsListPage(Composite parent, AllShowsPage parentPage) {
        this.body = new Composite(parent, SWT.NONE);
        this.parentPage = parentPage;
        init();
    }

    /**
    * {@inheritDoc}
    */
    public int getPageId() {
        return AllShowsSubPages.LIST_PAGE.ordinal();
    }

    private void init() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        this.body.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        this.body.setLayout(layout);
        layout = new GridLayout();
        layout.marginHeight = 10;
        layout.marginWidth = 40;
        Composite filterBack = new Composite(body, SWT.NONE);
        filterBack.setLayout(layout);
        filterBack.setBackgroundImage(ImageRegistry.INSTANCE.getImage(ShowDownImage.BACKGROUND_FILTER));
        GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.heightHint = filterBack.getBackgroundImage().getImageData().height;
        filterBack.setLayoutData(data);
        box = new FilterBox(filterBack, Messages.FilterByShowName);
        box.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        createShowTable();
        createBottomBar(body);
        setButtonsEnabled(false);
    }

    private void createTableMenu() {
        if (list == null || list.isDisposed()) {
            return;
        }
        list.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                boolean included = ShowDownManager.INSTANCE.getAllShows().includeEndedShows();
                String message = included ? Messages.HideEnded : Messages.ShowEnded;
                if (list.getMenu() == null) {
                    Menu menu = new Menu(Display.getDefault().getShells()[0], SWT.POP_UP);
                    list.setMenu(menu);
                    MenuItem item = new MenuItem(menu, SWT.PUSH);
                    item.setText(Messages.AddFirst);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            addShowFirstEpisode();
                        }
                    });
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(Messages.AddLast);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            addShowLastEpisode();
                        }
                    });
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(Messages.AddNext);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            addShowNextEpisode();
                        }
                    });
                    new MenuItem(menu, SWT.SEPARATOR);
                    item = new MenuItem(menu, SWT.CASCADE);
                    item.setText(Messages.UpdateAllShowsList);
                    Menu subMenu = new Menu(menu);
                    item.setMenu(subMenu);
                    item = new MenuItem(subMenu, SWT.PUSH);
                    item.setText(Messages.UpdateEnded);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            updateEndedStatus();
                        }
                    });
                    item = new MenuItem(subMenu, SWT.PUSH);
                    item.setText(Messages.FindNewShows);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            discoverNewShows();
                        }
                    });
                    if (ShowDownManager.INSTANCE.getSDParams().getFlags().contains("-admin")) {
                        item = new MenuItem(subMenu, SWT.PUSH);
                        item.setText(Messages.ValidateShowList);
                        item.addSelectionListener(new SelectionAdapter() {

                            public void widgetSelected(SelectionEvent e0) {
                                validateShowList();
                            }
                        });
                    }
                    new MenuItem(menu, SWT.SEPARATOR);
                    endedItem = new MenuItem(menu, SWT.PUSH);
                    endedItem.setText(message);
                    endedItem.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e0) {
                            if (list != null && !list.isDisposed()) {
                                IShowListCache cache = ShowDownManager.INSTANCE.getAllShows();
                                cache.setIncludeEndedShows(!cache.includeEndedShows());
                                list.refresh();
                            }
                        }
                    });
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(Messages.AddShow);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e) {
                            parentPage.getPage(AllShowsSubPages.ADD_OR_EDIT).setShow(null);
                            parentPage.setActivePage(AllShowsSubPages.ADD_OR_EDIT);
                        }
                    });
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(Messages.EditSelectedShow);
                    item.addSelectionListener(new SelectionAdapter() {

                        public void widgetSelected(SelectionEvent e) {
                            parentPage.getPage(AllShowsSubPages.ADD_OR_EDIT).setShow(list.getSelectedItem());
                            parentPage.setActivePage(AllShowsSubPages.ADD_OR_EDIT);
                        }
                    });
                } else if (endedItem != null && !endedItem.isDisposed()) {
                    endedItem.setText(message);
                }
            }
        });
    }

    private void updateEndedStatus() {
        new UpdateEndedDialog().open();
    }

    private void discoverNewShows() {
        new NewShowImportDialog().open();
    }

    private void validateShowList() {
        List<IShow> shows = ShowDownManager.INSTANCE.getAllShows().getShowList().getShows();
        boolean foundInvalid = false;
        for (int i = 0; i < shows.size(); ++i) {
            IShow show = shows.get(i);
            if (shows.indexOf(show) != i) {
                foundInvalid = true;
                ShowDownLog.getInstance().logInfo(Messages.InvalidShowList);
            }
        }
        if (foundInvalid) {
            MessageDialog.openWarning(Display.getDefault().getActiveShell(), Messages.Error, Messages.InvalidShowListMsg);
        } else {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.Info, Messages.ValidShowListMsg);
        }
    }

    private void createShowTable() {
        IShowListCache cache = ShowDownManager.INSTANCE.getAllShows();
        list = new DList<IShow>(body, SWT.SINGLE);
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        list.setLabelProvider(new AllShowsLabelProvider());
        list.setTheme(new AllShowsListTheme());
        list.setRenderer(new AllShowsCellRenderer());
        list.setContentProvider(new AllShowsContentProvider(cache, box));
        createTableMenu();
        list.addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event event) {
                if (list.getContentProvider().getSize() == 0 && box.getFilterText().length() > 0) {
                    GC gc = event.gc;
                    gc.drawString(Messages.NoShowMatch, 10, 10);
                }
            }
        });
        cache.addShowCacheListener(new ShowCacheListener() {

            public void notifyCacheUpdate(IShow show, ShowCacheEvent event) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        if (list != null && !list.isDisposed()) {
                            setButtonsEnabled(false);
                            list.refresh();
                        }
                    }
                });
            }
        });
        ShowDownManager.INSTANCE.getMyShowList().addShowCacheListener(new ShowCacheListener() {

            public void notifyCacheUpdate(IShow show, ShowCacheEvent event) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        if (list != null && !list.isDisposed()) {
                            list.refresh();
                        }
                    }
                });
            }
        });
        box.getFilterField().addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                if (list != null && !list.isDisposed()) {
                    setButtonsEnabled(false);
                    list.refresh();
                }
            }
        });
        list.addListener(new IDListListener<IShow>() {

            public void selectionChanged() {
                setButtonsEnabled(!list.getSelectedItems().isEmpty());
            }
        });
        list.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                openSelectedShowSubPage();
            }
        });
    }

    private void setButtonsEnabled(boolean enabled) {
        addFirst.setEnabled(enabled);
        addLast.setEnabled(enabled);
        addNext.setEnabled(enabled);
    }

    private void openSelectedShowSubPage() {
        if (list != null && !list.isDisposed()) {
            IShow show = list.getSelectedItem();
            if (show != null) {
                parentPage.setActivePage(AllShowsSubPages.SHOW_INFO);
                parentPage.getPage(AllShowsSubPages.SHOW_INFO).setShow(show);
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    protected IShow getShow() {
        if (list != null && !list.isDisposed()) {
            return list.getSelectedItem();
        }
        return null;
    }

    /**
    * {@inheritDoc}
    */
    public Composite getComposite() {
        return body;
    }

    /**
    * {@inheritDoc}
    */
    public PageStackPages getPage() {
        return parentPage.getPage();
    }

    /**
    * {@inheritDoc}
    */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
    * {@inheritDoc}
    */
    public boolean isActive() {
        return active;
    }

    /**
    * {@inheritDoc}
    */
    public void setShow(IShow show) {
    }

    /**
    * {@inheritDoc}
    */
    public void setEpisode(IEpisode episode) {
    }
}
