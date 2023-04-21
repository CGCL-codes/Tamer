package net.tourbook.srtm.download;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import net.tourbook.application.TourbookPlugin;
import net.tourbook.srtm.Messages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import de.byteholder.geoclipse.map.event.TileEventId;
import de.byteholder.geoclipse.tileinfo.TileInfoManager;

public class HTTPDownloader {

    public static final void get(final String urlBase, final String remoteFileName, final String localFilePathName) throws Exception {
        final TileInfoManager tileInfoMgr = TileInfoManager.getInstance();
        final long[] numWritten = new long[1];
        if (Display.getCurrent() != null) {
            tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_START_LOADING, remoteFileName, -99);
        }
        final Job monitorJob = new Job(Messages.job_name_downloadMonitor) {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_LOADING_MONITOR, remoteFileName, numWritten[0]);
                this.schedule(200);
                return Status.OK_STATUS;
            }
        };
        final Job downloadJob = new Job(Messages.job_name_httpDownload) {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_START_LOADING, remoteFileName, 0);
                final String address = urlBase + remoteFileName;
                System.out.println("load " + address);
                OutputStream outputStream = null;
                InputStream inputStream = null;
                try {
                    final URL url = new URL(address);
                    outputStream = new BufferedOutputStream(new FileOutputStream(localFilePathName));
                    final URLConnection urlConnection = url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    final byte[] buffer = new byte[1024];
                    int numRead;
                    while ((numRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, numRead);
                        numWritten[0] += numRead;
                    }
                    System.out.println("# Bytes localName = " + numWritten);
                } catch (final UnknownHostException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, NLS.bind(Messages.error_message_cannotConnectToServer, urlBase), e);
                } catch (final SocketTimeoutException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, NLS.bind(Messages.error_message_timeoutWhenConnectingToServer, urlBase), e);
                } catch (final Exception e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_END_LOADING, remoteFileName, 0);
                }
                System.out.println("get " + remoteFileName + " -> " + localFilePathName + " ...");
                return Status.OK_STATUS;
            }
        };
        monitorJob.schedule();
        downloadJob.schedule();
        try {
            downloadJob.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        try {
            monitorJob.cancel();
            monitorJob.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        final Exception e = (Exception) downloadJob.getResult().getException();
        if (e != null) {
        }
    }
}
