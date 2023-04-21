package spamwatch.base.account.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import spamwatch.base.BasicAction;
import spamwatch.base.account.LocalFolderDataProvider;

public class FetchAction extends BasicAction {

    private LocalFolderDataProvider folderDataProvider;

    public FetchAction(LocalFolderDataProvider folderDataProvider) {
        this.folderDataProvider = folderDataProvider;
        putValue(Action.ACTION_COMMAND_KEY, BasicAction.ACTION_DELETE);
        putValue(Action.NAME, "Fetch");
    }

    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {

            public void run() {
                folderDataProvider.fetchLocalMessages();
            }
        }).start();
    }
}
