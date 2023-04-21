package com.centraview.common.helper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.ejb.EJBObject;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface CommonHelper extends EJBObject {

    public Vector getAllSource() throws RemoteException;

    public String getSourceName(int sourceid) throws RemoteException;

    public HashMap getSourceList() throws RemoteException;

    /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
    public void setDataSource(String ds) throws RemoteException;

    /**
   * Returns an ArrayList that represents the full folder path
   * of the given folderID. The list will be keyed on folderID
   * with the value being the folder name. This list can be used
   * in the view layer to display a linked list of the full path.
   * @param folderID The ID of the folder whose path we're asking for.
   * @param tableName An String that is containg the information of the Table.
   * @return ArrayList representing the full folder path.
   */
    public ArrayList getFolderFullPath(int folderID, String tableName) throws Exception, RemoteException;

    /**
   * Returns an ArrayList which contains HashMap objects representing the email
   * folders which are a sub-folder of the given <code>parentID</code> parameter.
   * @param individualID The individualID of the user who is asking for the list.
   * @param parentID The folderID of the folder for which we want the list of sub-folders.
   * @param tableName An String that is containg the information of the Table.
   * @return ArrayList of HashMaps representing the sub-folders.
   */
    public ArrayList getSubFolderList(int individualID, int parentID, String tableName) throws RemoteException;

    /**
  * Returns a sourceID for the Passed Source Name. If suppose the SourceId is -1 then we Insert the Source and get the SourceID
  *
  * @param  sourceName  sourceName contains the information of the Source Name
  * @return sourceID The ID of the Source we are looking for.
  */
    public int getSourceID(String sourceName) throws RemoteException;

    /**
   * This method will frame Collection moduleName and moduleID
   *
   * @return An Collection of the ModuleName as the Key Value and 
   * moduleID as value.
   */
    public HashMap getModuleList() throws RemoteException;

    /**
   * Gets the Location Value List for the location lookup.
   * @param individualId
   * @param parameters
   * @return
   */
    public ValueListVO getLocationValueList(int individualId, ValueListParameters parameters) throws RemoteException;
}
