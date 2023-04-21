package org.remus.infomngmnt.search.tray;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.remus.infomngmnt.common.service.ITrayService;
import org.remus.infomngmnt.common.ui.UIUtil;
import org.remus.infomngmnt.common.ui.extension.AbstractTraySection;
import org.remus.infomngmnt.common.ui.swt.SearchText;
import org.remus.infomngmnt.provider.InfomngmntEditPlugin;
import org.remus.infomngmnt.search.Search;
import org.remus.infomngmnt.search.SearchFactory;
import org.remus.infomngmnt.search.SearchScope;
import org.remus.infomngmnt.search.service.LuceneSearchService;
import org.remus.infomngmt.common.ui.uimodel.provider.UimodelEditPlugin;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class SearchTray extends AbstractTraySection {

    public static final String DIALOG_SETTINGS_LAST_SEARCHTERM = "DIALOG_SETTINGS_LAST_SEARCHTERM";

    private IJobChangeListener searchJobListener;

    protected boolean restoreScheduled;

    @Override
    public void createDetailsPart(final Composite parent) {
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        parent.setLayout(gridLayout);
        SearchText searchText = new SearchText(parent, SWT.NO_BACKGROUND) {

            private Text text;

            @Override
            protected ToolBarManager doCreateButtons() {
                ToolBarManager doCreateButtons = super.doCreateButtons();
                SearchTray.this.toolkit.adapt(doCreateButtons.getControl());
                return doCreateButtons;
            }

            @Override
            protected Text doCreateFilterText(final Composite parent) {
                this.text = super.doCreateFilterText(parent);
                String string = UimodelEditPlugin.getPlugin().getDialogSettings().get(DIALOG_SETTINGS_LAST_SEARCHTERM);
                if (string != null) {
                    this.text.setText(string);
                }
                return this.text;
            }

            @Override
            protected void doAddListenerToTextField() {
                super.doAddListenerToTextField();
                this.text.addListener(SWT.Modify, new Listener() {

                    public void handleEvent(final Event event) {
                        UimodelEditPlugin.getPlugin().getDialogSettings().put(DIALOG_SETTINGS_LAST_SEARCHTERM, text.getText());
                    }
                });
            }

            @Override
            protected void handleSearchButtonPresses() {
                Search createSearch = SearchFactory.eINSTANCE.createSearch();
                createSearch.setScope(SearchScope.ALL);
                createSearch.setSearchString(this.text.getText());
                LuceneSearchService.getInstance().search(createSearch, true, true, null);
                SearchTray.this.restoreScheduled = true;
            }
        };
        this.toolkit.adapt(searchText);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.widthHint = 150;
        gridData.heightHint = SWT.DEFAULT;
        searchText.setLayoutData(gridData);
        final ProgressBar searchBar = new ProgressBar(parent, SWT.INDETERMINATE);
        GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData2.widthHint = SWT.DEFAULT;
        gridData2.heightHint = 10;
        searchBar.setLayoutData(gridData2);
        Job.getJobManager().addJobChangeListener(this.searchJobListener = new JobChangeAdapter() {

            @Override
            public void scheduled(final IJobChangeEvent event) {
                if (!parent.isDisposed() && !parent.getDisplay().isDisposed()) {
                    checkSearchBar(searchBar, parent.getDisplay());
                }
            }

            @Override
            public void done(final IJobChangeEvent event) {
                if (!parent.isDisposed() && !parent.getDisplay().isDisposed()) {
                    checkSearchBar(searchBar, parent.getDisplay());
                }
            }
        });
        checkSearchBar(searchBar, parent.getDisplay());
    }

    protected void checkSearchBar(final ProgressBar progressBar, final Display display) {
        display.asyncExec(new Runnable() {

            public void run() {
                if (!progressBar.isDisposed()) {
                    progressBar.setVisible(Job.getJobManager().find(LuceneSearchService.JOB_FAMILY).length > 0);
                    if (getTrayService() != null && Job.getJobManager().find(LuceneSearchService.JOB_FAMILY).length == 0 && SearchTray.this.restoreScheduled) {
                        getTrayService().restoreFromTray(UIUtil.getPrimaryWindow().getShell());
                        SearchTray.this.restoreScheduled = false;
                    }
                }
            }
        });
    }

    @Override
    public void dispose() {
        if (this.searchJobListener != null) {
            Job.getJobManager().removeJobChangeListener(this.searchJobListener);
        }
    }

    public ITrayService getTrayService() {
        final BundleContext bundleContext = InfomngmntEditPlugin.getPlugin().getBundle().getBundleContext();
        final ServiceReference serviceReference = bundleContext.getServiceReference(ITrayService.class.getName());
        if (serviceReference != null) {
            return (ITrayService) bundleContext.getService(serviceReference);
        }
        return null;
    }
}
