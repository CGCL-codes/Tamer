package ao.dd.shell.impl.transfer.bulk;

import ao.dd.shell.auth.LoginCredential;
import ao.dd.shell.def.ShellFile;
import ao.dd.shell.def.TransferAgent;
import ao.util.io.AoFiles;
import ao.util.misc.Factory;
import ao.util.text.Txt;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: aostrovsky
 * Date: 24-Jun-2009
 * Time: 10:50:46 AM
 */
public class TransferSched {

    private static final Logger LOG = Logger.getLogger(TransferSched.class);

    private final Factory<TransferAgent> bots;

    public TransferSched(final LoginCredential immediateHost, final TransferType type) {
        this(new Factory<TransferAgent>() {

            public TransferAgent newInstance() {
                return type.transferAgent(immediateHost);
            }
        });
    }

    public TransferSched(Factory<TransferAgent> destinationClients) {
        bots = destinationClients;
    }

    public void uploadAll(String remoteFolder, File... localFiles) {
        uploadAll(remoteFolder, Txt.toString(localFiles));
    }

    public void uploadAll(String remoteFolder, String... localFilePaths) {
        Collection<PendingTransfer> transfers = new ArrayList<PendingTransfer>();
        for (String localFile : localFilePaths) {
            transfers.add(new PendingTransfer(localFile, remoteFolder + "/" + AoFiles.name(localFile)));
        }
        uploadAll(transfers);
    }

    public void uploadAll(Collection<? extends PendingTransfer> transfers) {
        uploadAll(transfers, -1);
    }

    public void uploadAll(Collection<? extends PendingTransfer> transfers, long maxBytesPerSecond) {
        TransferAgent bot = bots.newInstance();
        try {
            if (maxBytesPerSecond != -1) {
                bot.throttle(maxBytesPerSecond);
            } else {
                bot.unThrottle();
            }
            for (PendingTransfer transfer : transfers) {
                LOG.info("uploading: " + transfer);
                transfer.upload(bot);
            }
        } finally {
            bot.close();
        }
    }

    public void downloadAll(String localDirectory, Collection<? extends ShellFile> remoteFiles) {
        List<String> remoteFileNames = new ArrayList<String>();
        for (ShellFile fileInfo : remoteFiles) {
            remoteFileNames.add(fileInfo.path());
        }
        downloadAll(localDirectory, remoteFileNames.toArray(new String[remoteFileNames.size()]));
    }

    public void downloadAll(String localDirectory, String... remoteFiles) {
        Collection<PendingTransfer> transfers = new ArrayList<PendingTransfer>();
        for (String remoteFile : remoteFiles) {
            transfers.add(new PendingTransfer(remoteFile, localDirectory + "/" + new File(remoteFile).getName()));
        }
        downloadAll(transfers);
    }

    public void downloadAll(Collection<? extends PendingTransfer> transfers) {
        downloadAll(transfers, -1);
    }

    public void downloadAll(Collection<? extends PendingTransfer> transfers, long maxBytesPerSecond) {
        TransferAgent bot = bots.newInstance();
        try {
            if (maxBytesPerSecond != -1) {
                bot.throttle(maxBytesPerSecond);
            } else {
                bot.unThrottle();
            }
            for (PendingTransfer transfer : transfers) {
                LOG.info("downloading: " + transfer);
                transfer.download(bot);
            }
        } finally {
            bot.close();
        }
    }
}
