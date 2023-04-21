package org.nomadpim.module.email;

import java.util.ArrayList;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.joda.time.format.DateTimeFormat;
import org.nomadpim.core.ui.OnlineState;
import org.nomadpim.core.ui.util.ColumnBasedViewerBuilder;
import org.nomadpim.core.ui.util.ComparatorToViewerSorterAdapter;
import org.nomadpim.core.ui.views.AbstractTimeIntervalBasedViewPart;
import org.nomadpim.core.ui.views.CollectionStructuredContentProvider;
import org.nomadpim.core.util.converter.ConversionException;
import org.nomadpim.core.util.converter.IConverter;

public class EMailView extends AbstractTimeIntervalBasedViewPart {

    private static final Object EMAIL_VIEW_FAMILY = new Object();

    private TableViewer tableViewer;

    private final Job fetchEMailsJob = new Job("fetch emails") {

        @Override
        public boolean belongsTo(Object family) {
            return family == EMAIL_VIEW_FAMILY;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            if (!OnlineState.getInstance().isOnline()) {
                return Status.CANCEL_STATUS;
            }
            SearchTerm searchTerm = GMailUtils.createTimeIntervalTerm(getCurrentTimeInterval());
            try {
                contentProvider.clear();
                GMailUtils.findMessages(searchTerm, GMailUtils.ALL_MAIL_FOLDER_NAME, new IMessageDataCollector() {

                    public void addMessage(MessageData message) {
                        contentProvider.add(message);
                    }
                });
            } catch (EMailAccessException e) {
                GMailUtils.showError(e);
            }
            return Status.OK_STATUS;
        }
    };

    private CollectionStructuredContentProvider<MessageData> contentProvider;

    @Override
    public void dispose() {
        tableViewer.getTable().dispose();
        super.dispose();
    }

    @Override
    public void doCreatePartControl(Composite parent) {
        ColumnBasedViewerBuilder builder = new ColumnBasedViewerBuilder();
        builder.addColumn("Received", 150, new IConverter<MessageData, String>() {

            public String convert(MessageData message) throws ConversionException {
                return DateTimeFormat.shortDateTime().print(message.getReceivedDate());
            }
        });
        builder.addColumn("Subject", 400, new IConverter<MessageData, String>() {

            public String convert(MessageData message) throws ConversionException {
                return message.getSubject();
            }
        });
        builder.addColumn("From", 250, new IConverter<MessageData, String>() {

            public String convert(MessageData message) throws ConversionException {
                return ((InternetAddress) message.getFrom()[0]).getAddress();
            }
        });
        tableViewer = builder.createTableViewer(parent);
        contentProvider = new CollectionStructuredContentProvider<MessageData>();
        tableViewer.setContentProvider(contentProvider);
        tableViewer.getTable().addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                open();
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
        tableViewer.setSorter(new ComparatorToViewerSorterAdapter<MessageData>(new MessageDataByReceivedDateComparator()));
        tableViewer.setInput(new ArrayList<MessageData>());
        restoreColumnWidths(tableViewer);
        IWorkbenchSiteProgressService service = (IWorkbenchSiteProgressService) getViewSite().getAdapter(IWorkbenchSiteProgressService.class);
        service.showBusyForFamily(EMAIL_VIEW_FAMILY);
    }

    private void open() {
        Object data = tableViewer.getTable().getSelection()[0].getData();
        if (data instanceof MessageData) {
            ((MessageData) data).open();
        }
    }

    @Override
    public void setFocus() {
        tableViewer.getTable().setFocus();
    }

    @Override
    protected void timeIntervalChanged() {
        if (fetchEMailsJob.getState() == Job.RUNNING) {
            try {
                fetchEMailsJob.join();
            } catch (InterruptedException e) {
            }
        }
        fetchEMailsJob.schedule();
    }
}
