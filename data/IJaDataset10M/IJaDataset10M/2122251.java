package com.vayoodoot.ui.explorer;

import com.vayoodoot.partner.Buddy;
import com.vayoodoot.client.ClientException;
import com.vayoodoot.client.Client;
import com.vayoodoot.file.FileReceiver;
import com.vayoodoot.file.DirectoryReceiver;
import com.vayoodoot.message.DirectoryItem;
import com.vayoodoot.session.DirectConnectionUnavailableException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: May 29, 2007
 * Time: 10:22:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Message2UIAdapter {

    public void userOnline(Buddy buddy);

    public void userOffline(Buddy buddy);

    public void initiateSessionWithBuddy(Buddy buddy) throws ClientException, DirectConnectionUnavailableException;

    public void getDirectory(Buddy buddy, String directoryName) throws ClientException;

    public void setBuddyList(ArrayList buddies);

    public FileReceiver getFile(Buddy buddy, String localFile, String localName) throws ClientException;

    public DirectoryReceiver getDirectory(Buddy buddy, String localFile, String localName) throws ClientException;

    public DirectoryReceiver getMultipleFiles(Buddy buddy, String localDir, String remoteFile, List directoryItems) throws ClientException;

    public void setDirectConnectionAvailable(boolean b);

    public void serverSessionDisconnected();

    public void logout() throws ClientException;

    public void setClient(Client client);

    public void searchFiles(String searchQuery, String[] buddies) throws ClientException;
}
