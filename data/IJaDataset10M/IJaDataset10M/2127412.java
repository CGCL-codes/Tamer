package net.sf.leechget.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.sf.leechget.descriptor.ServiceDescriptor;
import net.sf.leechget.executor.exception.UnknownExecutorInformation;
import net.sf.leechget.executor.information.DownloadExecutorInformation;
import net.sf.leechget.executor.information.ExecutorInformation;
import net.sf.leechget.executor.information.UploadExecutorInformation;
import net.sf.leechget.executor.listener.ExecutorEvent;
import net.sf.leechget.executor.listener.ExecutorListener;
import net.sf.leechget.executor.listener.ExecutorProgressListener;
import net.sf.leechget.executor.login.ExecutorLogin;
import net.sf.leechget.executor.login.ExecutorLoginDAO;
import net.sf.leechget.executor.thread.DownloaderThread;
import net.sf.leechget.executor.thread.UploaderThread;
import net.sf.leechget.service.ServiceHolder;
import net.sf.leechget.service.ServiceManager;
import net.sf.leechget.service.api.Downloader;
import net.sf.leechget.service.api.Logger;
import net.sf.leechget.service.api.Service;
import net.sf.leechget.service.api.Uploader;
import net.sf.leechget.transfer.Transfer;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Rogiel
 * 
 */
public class TransferExecutor implements Executor<Transfer> {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final ServiceManager manager;

    private final Injector injector;

    @Inject
    public TransferExecutor(final ServiceManager manager, final Injector injector) {
        this.manager = manager;
        this.injector = injector;
    }

    @Override
    public void execute(final Transfer transfer, final ExecutorInformation information, final ExecutorListener eventListener, final ExecutorProgressListener progressListener) throws UnknownExecutorInformation {
        final Runnable thread;
        if (information instanceof DownloadExecutorInformation) {
            final DownloadExecutorInformation downloadInformation = (DownloadExecutorInformation) information;
            final ServiceHolder serviceHolder = findDownloadService(transfer, downloadInformation);
            final Service service = serviceHolder.getService();
            final Logger logger = getDownloaderLogger(serviceHolder, service);
            final Downloader downloader = service.getDownloader(downloadInformation.getURL());
            thread = new DownloaderThread(downloader, logger, downloadInformation.getDownloadLocation(), eventListener, progressListener);
        } else if (information instanceof UploadExecutorInformation) {
            final UploadExecutorInformation uploadInformation = (UploadExecutorInformation) information;
            final ServiceHolder serviceHolder = findUploadService(transfer, uploadInformation);
            final Service service = serviceHolder.getService();
            final Logger logger = getUploaderLogger(serviceHolder, service);
            final Uploader uploader = service.getUploader(uploadInformation.getFilename(), uploadInformation.getFilesize());
            thread = new UploaderThread(uploader, logger, eventListener, progressListener);
        } else {
            throw new UnknownExecutorInformation();
        }
        eventListener.onQueue(new ExecutorEvent());
        executor.execute(thread);
    }

    private ServiceHolder findUploadService(final Transfer transfer, final UploadExecutorInformation information) {
        return null;
    }

    private ServiceHolder findDownloadService(final Transfer transfer, final DownloadExecutorInformation information) {
        return manager.getDownloaderFor(information.getURL());
    }

    private Logger getDownloaderLogger(ServiceHolder holder, Service service) {
        final ServiceDescriptor descriptor = holder.getDescriptor();
        if (descriptor.getDownloaderDescriptor().isLoginRequired()) {
            final ExecutorLogin login = getLogin(descriptor.getId());
            return service.getLogger(login.getLogin(), login.getPassword());
        }
        return null;
    }

    private Logger getUploaderLogger(ServiceHolder holder, Service service) {
        final ServiceDescriptor descriptor = holder.getDescriptor();
        if (descriptor.getUploaderDescriptor().isLoginRequired()) {
            final ExecutorLogin login = getLogin(descriptor.getId());
            return service.getLogger(login.getLogin(), login.getPassword());
        }
        return null;
    }

    private ExecutorLogin getLogin(final String serviceId) {
        final ExecutorLoginDAO dao = injector.getInstance(ExecutorLoginDAO.class);
        return dao.getRandomLogin(serviceId);
    }
}
