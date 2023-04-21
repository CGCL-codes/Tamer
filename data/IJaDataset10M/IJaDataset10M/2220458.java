package de.tudresden.inf.rn.mobilis.media.views;

import android.os.Messenger;
import android.os.RemoteException;
import de.tudresden.inf.rn.mobilis.media.R;
import de.tudresden.inf.rn.mobilis.media.services.IRepositoryService;

public class RepositoryItemDownloadCommand extends RepositoryItemCommand {

    private IRepositoryService service;

    private Messenger resultMessenger;

    private int resultCode;

    private String content;

    public RepositoryItemDownloadCommand(String repository, String uid, String content, IRepositoryService service, Messenger resultMessenger, int resultCode) {
        super(repository, uid);
        this.content = content;
        this.service = service;
        this.resultMessenger = resultMessenger;
        this.resultCode = resultCode;
    }

    public void doCommand() {
        try {
            this.service.transfer(this.repository, this.content, this.uid, this.resultMessenger, this.resultCode);
        } catch (RemoteException e) {
        }
        ;
    }

    public int getIconRessource() {
        return R.drawable.list_download;
    }

    public int getNameRessource() {
        return R.string.command_download;
    }
}
