package ch.oxinia.webdav.davcommander;

import java.util.Vector;
import HTTPClient.ProgressObserver;

public class DownloadCaller extends Caller {

    private RemoteFileManager remoteFileManager;

    private String localDirectory;

    private Vector<String> fileQueue = new Vector<String>();

    private Vector<String> directoryQueue = new Vector<String>();

    public DownloadCaller(RemoteFileManager remoteFileManager, String localDirectory) {
        AbstractFileView fileView = remoteFileManager.getFileView();
        int[] remoteSelection = fileView.getSelectedRows();
        for (int i = 0; i < remoteSelection.length; i++) {
            String remoteResource = fileView.getName(remoteSelection[i]);
            if (fileView.isCollection(remoteSelection[i])) {
                directoryQueue.add(remoteResource);
            } else {
                fileQueue.add(remoteResource);
            }
            ProgressObserver.getInstance().fireProgressStart(remoteResource, ProgressObserver.ProgressType.DOWNLOAD);
        }
        this.remoteFileManager = remoteFileManager;
        this.localDirectory = localDirectory;
    }

    protected void executeCall() {
        ProgressObserver progressObserver = ProgressObserver.getInstance();
        for (int i = 0; i < directoryQueue.size(); i++) {
            if (progressObserver.queryStatus(directoryQueue.elementAt(i), ProgressObserver.ProgressType.DOWNLOAD)) {
                remoteFileManager.downloadDirectory(directoryQueue.elementAt(i), localDirectory);
            }
            progressObserver.fireProgressEnd(directoryQueue.elementAt(i), ProgressObserver.ProgressType.DOWNLOAD);
        }
        for (int i = 0; i < fileQueue.size(); i++) {
            if (progressObserver.queryStatus(fileQueue.elementAt(i), ProgressObserver.ProgressType.DOWNLOAD)) {
                remoteFileManager.downloadFile(fileQueue.elementAt(i), localDirectory);
            }
        }
    }
}
