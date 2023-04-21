package de.hybris.storm.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Change represents the version change from one story (source) to another (target)
 * @version $Revision: 1.3 $, $Date: 2001/06/26 13:08:34 $
 */
public interface ChangeRemote extends EJBObject {

    public String getID() throws RemoteException;

    public UserRemote getUser() throws RemoteException;

    public StoryRemote getSourceStory() throws RemoteException;

    public StoryRemote getTargetStory() throws RemoteException;

    public int getType() throws RemoteException;

    public static final int SPLIT = 1;

    public static final int MERGE = 2;

    public static final int DELETE = 4;

    public static final int CREATE = 8;

    public static final int DATACHANGE = 16;

    public static final int PLANNINGCHANGE = 32;

    public static final int ESTIMATECHANGE = 64;

    public static final int FILEATTACHMENT = 128;

    public static final int LINKATTACHMENT = 256;
}
