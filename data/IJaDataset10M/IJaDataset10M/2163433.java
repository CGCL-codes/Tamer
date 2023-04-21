package de.hybris.storm.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * The Attachment can represent a Link or a File. In both cases it does not
 * store the entity itself but it knows where to find it.
 * @version $Revision: 1.3 $, $Date: 2001/06/26 13:59:36 $
 */
public interface AttachmentRemote extends EJBObject {

    public StoryRemote getStory() throws RemoteException;

    public String getContent() throws RemoteException;

    public int getContentType() throws RemoteException;

    public String getDescription() throws RemoteException;

    public static final int FILE = 1;

    public static final int LINK = 2;
}
