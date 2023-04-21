package org.gudy.azureus2.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.internat.MessageText;
import org.gudy.azureus2.core3.util.IndentWriter;
import org.gudy.azureus2.ui.swt.debug.ObfusticateImage;
import org.gudy.azureus2.ui.swt.plugins.UISWTView;
import org.gudy.azureus2.ui.swt.pluginsimpl.UISWTViewImpl;
import org.gudy.azureus2.ui.swt.views.table.utils.TableColumnCreator;
import org.gudy.azureus2.ui.swt.views.table.utils.TableColumnManager;
import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.ui.common.table.*;
import com.aelitis.azureus.ui.selectedcontent.SelectedContentManager;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.ui.tables.TableManager;
import org.gudy.azureus2.pluginsimpl.local.PluginCoreUtils;

/**
 * @author MjrTom
 *			2005/Dec/08: Avg Avail Item
 */
public class DetailedListView extends AbstractIView implements ObfusticateImage, IViewExtension {

    private static int SASH_WIDTH = 8;

    static final TableColumnCore[] tableIncompleteItems = TableColumnCreator.createIncompleteDM(TableManager.TABLE_MYTORRENTS_INCOMPLETE);

    private AzureusCore azureus_core;

    private MyTorrentsView torrentview;

    private UISWTViewImpl managerview;

    private Composite managerview_parent;

    private Composite form;

    private MyTorrentsView lastSelectedView;

    public DetailedListView(AzureusCore _azureus_core) {
        azureus_core = _azureus_core;
        TableColumnManager tcManager = TableColumnManager.getInstance();
        tcManager.addColumns(tableIncompleteItems);
    }

    public Composite getComposite() {
        return form;
    }

    public void delete() {
        if (torrentview != null) torrentview.delete();
        super.delete();
    }

    public void initialize(Composite parent) {
        if (form != null) {
            return;
        }
        form = new Composite(parent, SWT.NONE);
        FormLayout flayout = new FormLayout();
        flayout.marginHeight = 0;
        flayout.marginWidth = 0;
        form.setLayout(flayout);
        GridData gridData;
        gridData = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(gridData);
        GridLayout layout;
        final Composite child1 = new Composite(form, SWT.NULL);
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        child1.setLayout(layout);
        torrentview = new MyTorrentsView(azureus_core, "DetailedTorrentList", false, tableIncompleteItems);
        torrentview.initialize(child1);
        torrentview.getTableView().addSelectionListener(new TableSelectionAdapter() {

            public void selected(TableRowCore[] rows) {
                try {
                    DownloadManager[] dms = new DownloadManager[rows.length];
                    for (int i = 0; i < rows.length; i++) {
                        dms[i] = (DownloadManager) rows[i].getDataSource(true);
                    }
                    refreshDownloadView(dms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
        final Sash sash = new Sash(form, SWT.HORIZONTAL);
        final Composite child2 = new Composite(form, SWT.NULL);
        this.managerview_parent = child2;
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        child2.setLayout(layout);
        FormData formData;
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        child1.setLayoutData(formData);
        final FormData child1Data = formData;
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(child1);
        formData.height = SASH_WIDTH;
        sash.setLayoutData(formData);
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.top = new FormAttachment(sash);
        int weight = (int) (COConfigurationManager.getFloatParameter("DetailedList.SplitAt"));
        if (weight > 10000) {
            weight = 10000;
        } else if (weight < 100) {
            weight *= 100;
        }
        if (weight < 500) {
            weight = 500;
        } else if (weight > 9000) {
            weight = 9000;
        }
        sash.setData("PCT", new Double((float) weight / 10000));
        child2.setLayoutData(formData);
        sash.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                final boolean FASTDRAG = true;
                if (FASTDRAG && e.detail == SWT.DRAG) return;
                child1Data.height = e.y + e.height - SASH_WIDTH;
                form.layout();
                Double l = new Double((double) child1.getBounds().height / form.getBounds().height);
                sash.setData("PCT", l);
                if (e.detail != SWT.DRAG) COConfigurationManager.setParameter("DetailedList.SplitAt", (int) (l.doubleValue() * 10000));
            }
        });
        form.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                Double l = (Double) sash.getData("PCT");
                if (l != null) {
                    child1Data.height = (int) (form.getBounds().height * l.doubleValue());
                    form.layout();
                }
            }
        });
    }

    public void refresh() {
        if (getComposite() == null || getComposite().isDisposed()) return;
        torrentview.refresh();
    }

    public void updateLanguage() {
        if (getComposite() == null || getComposite().isDisposed()) return;
        torrentview.updateLanguage();
    }

    public String getFullTitle() {
        return MessageText.getString("DetailedListView.title");
    }

    private MyTorrentsView getCurrentView() {
        try {
            if (torrentview.isTableFocus()) lastSelectedView = torrentview;
        } catch (Exception ignore) {
        }
        return lastSelectedView;
    }

    public boolean isEnabled(String itemKey) {
        IView currentView = getCurrentView();
        if (currentView != null) return currentView.isEnabled(itemKey); else return false;
    }

    public void itemActivated(String itemKey) {
        IView currentView = getCurrentView();
        if (currentView != null) currentView.itemActivated(itemKey);
    }

    public String getIconBarPluginIdentity() {
        return "torrents";
    }

    public Object[] getIconBarPluginContextData() {
        DownloadManager[] dms = getSelectedDownloads();
        if (dms == null) {
            return null;
        }
        Download[] res = new Download[dms.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = PluginCoreUtils.wrap(dms[i]);
        }
        return res;
    }

    public DownloadManager[] getSelectedDownloads() {
        MyTorrentsView currentView = getCurrentView();
        if (currentView == null) {
            return null;
        }
        return currentView.getSelectedDownloads();
    }

    public void generateDiagnostics(IndentWriter writer) {
        super.generateDiagnostics(writer);
        try {
            writer.indent();
            writer.println("Downloading");
            writer.indent();
            torrentview.generateDiagnostics(writer);
        } finally {
            writer.exdent();
            writer.exdent();
        }
        try {
            writer.indent();
            writer.println("Seeding");
            writer.indent();
        } finally {
            writer.exdent();
            writer.exdent();
        }
    }

    public Image obfusticatedImage(Image image, Point shellOffset) {
        if (torrentview != null) {
            torrentview.obfusticatedImage(image, shellOffset);
        }
        return image;
    }

    public Menu getPrivateMenu() {
        return null;
    }

    public void viewActivated() {
        IView currentView = getCurrentView();
        if (currentView instanceof IViewExtension) {
            ((IViewExtension) currentView).viewActivated();
        }
        if (currentView instanceof MyTorrentsView) {
            ((MyTorrentsView) currentView).updateSelectedContent();
        }
    }

    public void viewDeactivated() {
        IView currentView = getCurrentView();
        if (currentView == null) {
            return;
        }
        if (currentView instanceof IViewExtension) {
            ((IViewExtension) currentView).viewDeactivated();
        }
        String ID = currentView.getShortTitle();
        if (currentView instanceof MyTorrentsView) {
            ID = ((MyTorrentsView) currentView).getTableView().getTableID();
        }
        SelectedContentManager.changeCurrentlySelectedContent(ID, null, torrentview == null ? null : torrentview.getTableView());
    }

    public void refreshDownloadView(DownloadManager[] dms) {
        System.out.println("refreshDownloadView change");
        if (dms.length != 1) return;
        boolean change = this.managerview == null || !dms[0].equals(this.managerview.getDataSource());
        if (!change) {
            return;
        }
        if (this.managerview != null) {
            this.managerview.delete();
        }
        try {
            this.managerview = new UISWTViewImpl(null, "DMView", new ManagerView(), dms[0]);
            this.managerview.setUseCoreDataSource(true);
            this.managerview.initialize(this.managerview_parent);
            this.managerview_parent.layout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
