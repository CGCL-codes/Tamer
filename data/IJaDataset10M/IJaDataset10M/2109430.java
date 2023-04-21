package com.vayoodoot.client;

import com.vayoodoot.server.Server;
import com.vayoodoot.db.SharedDirectory;
import com.vayoodoot.db.SharedDirectoryManager;
import com.vayoodoot.partner.PartnerAccount;
import com.vayoodoot.partner.GoogleTalkAccount;
import com.vayoodoot.ui.explorer.Message2UIAdapterMock;
import com.vayoodoot.ui.explorer.ExplorerUIController;
import com.vayoodoot.ui.explorer.Message2UIAdapter;
import com.vayoodoot.ui.explorer.Message2UIAdapterImpl;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: May 31, 2007
 * Time: 11:32:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainUIClient {

    public static void main(String args[]) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        System.setProperty("java.library.path", "C:\\sachin\\work\\shoonya\\svn\\trunk\\fileshare\\lib\\jdic\\windows\\x86");
        Server server = new Server();
        server.startServer();
        Thread.sleep(2000);
        String[] sharedDirs = "C:\\SHARE1,C:\\SHARE2".split(",");
        for (int i = 0; i < sharedDirs.length; i++) {
            String shareName = sharedDirs[i].substring(sharedDirs[i].indexOf("\\") + 1, sharedDirs[i].length());
            SharedDirectory sharedDirectory = new SharedDirectory();
            sharedDirectory.setShareName(shareName);
            sharedDirectory.setLocalDirectory(sharedDirs[i]);
            SharedDirectoryManager.addSharedDirectory(sharedDirectory);
        }
        Client client1 = new Client("kingshetty@gmail.com", "mumbhai", PartnerAccount.GOOGLE_TALK);
        client1.setUiAdapter(new Message2UIAdapterMock());
        client1.setJunitTestMode(true);
        client1.startSocketListening();
        client1.login();
        Client client2 = new Client("debugger.kernel@gmail.com", "mumbhai", PartnerAccount.GOOGLE_TALK);
        client2.setUiAdapter(new Message2UIAdapterMock());
        client2.setJunitTestMode(true);
        client2.startSocketListening();
        client2.login();
        Client client = new Client("sachintheonly@gmail.com", "mumbhai", GoogleTalkAccount.GOOGLE_TALK);
        ExplorerUIController controller = new ExplorerUIController();
        Thread.currentThread().join();
    }
}
