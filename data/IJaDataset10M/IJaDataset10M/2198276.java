package gui.mcast.logic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import remote.fs.RemoteFile;
import remote.proxies.ReadFile;
import remote.services.RemoteFileDescriptor;
import remote.services.RemoteFileManagerServiceBean;
import remote.services.RemoteFileManagerServiceBeanProxy;

public class MessageHandlingFacade {

    private static MessageHandlingFacade instance = null;

    private RemoteFileManagerServiceBean fileManager = null;

    private ArrayList<String> fileList = null;

    /**
	 * get singleton instance
	 * @return the created instance if required
	 */
    public static MessageHandlingFacade getInstance() {
        if (null == instance) {
            instance = new MessageHandlingFacade();
        }
        return instance;
    }

    /**
	 * default constructor
	 *
	 */
    private MessageHandlingFacade() {
        fileManager = new RemoteFileManagerServiceBeanProxy().getRemoteFileManagerServiceBean();
        this.loadMessages();
    }

    private void loadMessages() {
        this.fileList = new ArrayList<String>();
        RemoteFile dir = new RemoteFile("/messages");
        String[] dirList = dir.list();
        for (String aFileName : dirList) {
            this.fileList.add(aFileName);
        }
    }

    /**
	 * 
	 * @return az elérhető üzenetek neveit adja vissza
	 */
    public ArrayList<String> getMessageList() {
        return this.fileList;
    }

    public int getMessagesCount() {
        return this.fileList.size();
    }
}
