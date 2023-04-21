package org.cybergarage.upnp.media.server;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.StringTokenizer;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.http.Parameter;
import org.cybergarage.http.ParameterList;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.media.server.action.BrowseAction;
import org.cybergarage.upnp.media.server.action.SearchAction;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;
import org.cybergarage.upnp.media.server.object.DIDLLite;
import org.cybergarage.upnp.media.server.object.Format;
import org.cybergarage.upnp.media.server.object.FormatList;
import org.cybergarage.upnp.media.server.object.SearchCap;
import org.cybergarage.upnp.media.server.object.SearchCapList;
import org.cybergarage.upnp.media.server.object.SearchCriteria;
import org.cybergarage.upnp.media.server.object.SearchCriteriaList;
import org.cybergarage.upnp.media.server.object.SortCap;
import org.cybergarage.upnp.media.server.object.SortCapList;
import org.cybergarage.upnp.media.server.object.SortCriterionList;
import org.cybergarage.upnp.media.server.object.container.RootNode;
import org.cybergarage.upnp.media.server.object.item.ItemNode;
import org.cybergarage.upnp.media.server.object.search.IdSearchCap;
import org.cybergarage.upnp.media.server.object.search.TitleSearchCap;
import org.cybergarage.upnp.media.server.object.sort.DCDateSortCap;
import org.cybergarage.upnp.media.server.object.sort.DCTitleSortCap;
import org.cybergarage.upnp.media.server.object.sort.UPnPClassSortCap;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.MyLogger;
import org.cybergarage.util.StringUtil;
import org.cybergarage.util.ThreadCore;

public class ContentDirectory extends ThreadCore implements ActionListener, QueryListener {

    public static final String SERVICE_TYPE = "urn:schemas-upnp-org:service:ContentDirectory:1";

    public static final String BROWSE = "Browse";

    public static final String SEARCH = "Search";

    public static final String GET_SEARCH_CAPABILITIES = "GetSearchCapabilities";

    public static final String SEARCH_CAPS = "SearchCaps";

    public static final String GET_SORT_CAPABILITIES = "GetSortCapabilities";

    public static final String SORT_CAPS = "SortCaps";

    public static final String GET_SYSTEM_UPDATE_ID = "GetSystemUpdateID";

    public static final String ID = "Id";

    public static final String SYSTEM_UPDATE_ID = "SystemUpdateID";

    public static final String CONTENT_EXPORT_URI = "/ExportContent";

    public static final String CONTENT_IMPORT_URI = "/ImportContent";

    public static final String CONTENT_ID = "id";

    private static MyLogger log = new MyLogger(ContentDirectory.class);

    public static final String SCPD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" + "   <specVersion>\n" + "      <major>1</major>\n" + "      <minor>0</minor>\n" + "   </specVersion>\n" + "   <actionList>\n" + "      <action>\n" + "         <name>ExportResource</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>SourceURI</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>DestinationURI</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TransferID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>StopTransferResource</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>TransferID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>DestroyObject</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ObjectID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>DeleteResource</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ResourceURI</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>UpdateObject</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ObjectID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>CurrentTagValue</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>NewTagValue</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>Browse</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ObjectID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>BrowseFlag</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_BrowseFlag</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Filter</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>StartingIndex</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>RequestedCount</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>SortCriteria</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Result</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>NumberReturned</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TotalMatches</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>UpdateID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>GetTransferProgress</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>TransferID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TransferStatus</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferStatus</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TransferLength</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferLength</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TransferTotal</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferTotal</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>GetSearchCapabilities</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>SearchCaps</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>SearchCapabilities</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>CreateObject</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ContainerID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Elements</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>ObjectID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Result</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>Search</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ContainerID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>SearchCriteria</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_SearchCriteria</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Filter</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>StartingIndex</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>RequestedCount</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>SortCriteria</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>Result</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>NumberReturned</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TotalMatches</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>UpdateID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>GetSortCapabilities</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>SortCaps</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>SortCapabilities</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>ImportResource</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>SourceURI</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>DestinationURI</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>TransferID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>CreateReference</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "               <name>ContainerID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>ObjectID</name>\n" + "               <direction>in</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "            <argument>\n" + "               <name>NewID</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "      <action>\n" + "         <name>GetSystemUpdateID</name>\n" + "         <argumentList>\n" + "            <argument>\n" + "              <name>Id</name>\n" + "               <direction>out</direction>\n" + "               <relatedStateVariable>SystemUpdateID</relatedStateVariable>\n" + "            </argument>\n" + "         </argumentList>\n" + "      </action>\n" + "   </actionList>\n" + "   <serviceStateTable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_SortCriteria</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_TransferLength</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"yes\">\n" + "         <name>TransferIDs</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_UpdateID</name>\n" + "         <dataType>ui4</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_SearchCriteria</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_Filter</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"yes\">\n" + "         <name>ContainerUpdateIDs</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_Result</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_Index</name>\n" + "         <dataType>ui4</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_TransferID</name>\n" + "         <dataType>ui4</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_TagValueList</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_URI</name>\n" + "         <dataType>uri</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_ObjectID</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>SortCapabilities</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>SearchCapabilities</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_Count</name>\n" + "         <dataType>ui4</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_BrowseFlag</name>\n" + "         <dataType>string</dataType>\n" + "         <allowedValueList>\n" + "            <allowedValue>BrowseMetadata</allowedValue>\n" + "            <allowedValue>BrowseDirectChildren</allowedValue>\n" + "         </allowedValueList>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"yes\">\n" + "         <name>SystemUpdateID</name>\n" + "         <dataType>ui4</dataType>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_TransferStatus</name>\n" + "         <dataType>string</dataType>\n" + "         <allowedValueList>\n" + "            <allowedValue>COMPLETED</allowedValue>\n" + "            <allowedValue>ERROR</allowedValue>\n" + "            <allowedValue>IN_PROGRESS</allowedValue>\n" + "            <allowedValue>STOPPED</allowedValue>\n" + "         </allowedValueList>\n" + "      </stateVariable>\n" + "      <stateVariable sendEvents=\"no\">\n" + "         <name>A_ARG_TYPE_TransferTotal</name>\n" + "         <dataType>string</dataType>\n" + "      </stateVariable>\n" + "   </serviceStateTable>\n" + "</scpd>";

    public ContentDirectory(MediaServer mserver) {
        setMediaServer(mserver);
        systemUpdateID = 0;
        maxContentID = 0;
        setSystemUpdateInterval(DEFAULT_SYSTEMUPDATEID_INTERVAL);
        setContentUpdateInterval(DEFAULT_CONTENTUPDATE_INTERVAL);
        initRootNode();
        initSortCaps();
        initSearchCaps();
    }

    private MediaServer mediaServer;

    private void setMediaServer(MediaServer mserver) {
        mediaServer = mserver;
    }

    public MediaServer getMediaServer() {
        return mediaServer;
    }

    private Mutex mutex = new Mutex();

    public void lock() {
        mutex.lock();
    }

    public void unlock() {
        mutex.unlock();
    }

    private int systemUpdateID;

    public synchronized void updateSystemUpdateID() {
        systemUpdateID++;
    }

    public synchronized int getSystemUpdateID() {
        return systemUpdateID;
    }

    private int maxContentID;

    private synchronized int getNextContentID() {
        maxContentID++;
        return maxContentID;
    }

    public int getNextItemID() {
        return getNextContentID();
    }

    public int getNextContainerID() {
        return getNextContentID();
    }

    private RootNode rootNode;

    private void initRootNode() {
        rootNode = new RootNode();
        rootNode.setContentDirectory(this);
    }

    public RootNode getRootNode() {
        return rootNode;
    }

    private FormatList formatList = new FormatList();

    public boolean addPlugIn(Format format) {
        formatList.add(format);
        return true;
    }

    public Format getFormat(File file) {
        return formatList.getFormat(file);
    }

    public Collection getAllFormats() {
        return formatList.getAllFormats();
    }

    private SortCapList sortCapList = new SortCapList();

    public boolean addSortCap(SortCap sortCap) {
        sortCapList.add(sortCap);
        return true;
    }

    public int getNSortCaps() {
        return sortCapList.size();
    }

    public SortCap getSortCap(int n) {
        return sortCapList.getSortCap(n);
    }

    public SortCap getSortCap(String type) {
        return sortCapList.getSortCap(type);
    }

    private void initSortCaps() {
        addSortCap(new UPnPClassSortCap());
        addSortCap(new DCTitleSortCap());
        addSortCap(new DCDateSortCap());
    }

    private String getSortCapabilities() {
        String sortCapsStr = "";
        int nSortCaps = getNSortCaps();
        for (int n = 0; n < nSortCaps; n++) {
            SortCap sortCap = getSortCap(n);
            String type = sortCap.getType();
            if (0 < n) sortCapsStr += ",";
            sortCapsStr += type;
        }
        return sortCapsStr;
    }

    private SearchCapList searchCapList = new SearchCapList();

    public boolean addSearchCap(SearchCap searchCap) {
        searchCapList.add(searchCap);
        return true;
    }

    public SearchCapList getSearchCapList() {
        return searchCapList;
    }

    public int getNSearchCaps() {
        return searchCapList.size();
    }

    public SearchCap getSearchCap(int n) {
        return searchCapList.getSearchCap(n);
    }

    public SearchCap getSearchCap(String type) {
        return searchCapList.getSearchCap(type);
    }

    private void initSearchCaps() {
        addSearchCap(new IdSearchCap());
        addSearchCap(new TitleSearchCap());
    }

    private String getSearchCapabilities() {
        String searchCapsStr = "";
        int nSearchCaps = getNSearchCaps();
        for (int n = 0; n < nSearchCaps; n++) {
            SearchCap searchCap = getSearchCap(n);
            String type = searchCap.getPropertyName();
            if (0 < n) searchCapsStr += ",";
            searchCapsStr += type;
        }
        return searchCapsStr;
    }

    private DirectoryList dirList = new DirectoryList();

    private DirectoryList getDirectoryList() {
        return dirList;
    }

    public boolean addDirectory(Directory dir) {
        dir.setContentDirectory(this);
        dir.setID(getNextContainerID());
        dir.updateContentList();
        dirList.add(dir);
        rootNode.addContentNode(dir);
        updateSystemUpdateID();
        return true;
    }

    public boolean removeDirectory(String name) {
        Directory dirNode = dirList.getDirectory(name);
        if (dirNode == null) return false;
        dirList.remove(dirNode);
        rootNode.removeNode(dirNode);
        updateSystemUpdateID();
        return true;
    }

    public int getNDirectories() {
        return dirList.size();
    }

    public Directory getDirectory(int n) {
        return dirList.getDirectory(n);
    }

    public ContentNode findContentNodeByID(String id) {
        return getRootNode().findContentNodeByID(id);
    }

    public boolean actionControlReceived(Action action) {
        String actionName = action.getName();
        if (actionName.equals(BROWSE) == true) {
            BrowseAction browseAct = new BrowseAction(action);
            return browseActionReceived(browseAct);
        }
        if (actionName.equals(SEARCH) == true) {
            SearchAction searchAct = new SearchAction(action);
            return searchActionReceived(searchAct);
        }
        if (actionName.equals(GET_SEARCH_CAPABILITIES) == true) {
            Argument searchCapsArg = action.getArgument(SEARCH_CAPS);
            String searchCapsStr = getSearchCapabilities();
            searchCapsArg.setValue(searchCapsStr);
            return true;
        }
        if (actionName.equals(GET_SORT_CAPABILITIES) == true) {
            Argument sortCapsArg = action.getArgument(SORT_CAPS);
            String sortCapsStr = getSortCapabilities();
            sortCapsArg.setValue(sortCapsStr);
            return true;
        }
        if (actionName.equals(GET_SYSTEM_UPDATE_ID) == true) {
            Argument idArg = action.getArgument(ID);
            idArg.setValue(getSystemUpdateID());
            return true;
        }
        return false;
    }

    private boolean browseActionReceived(BrowseAction action) {
        if (action.isMetadata() == true) return browseMetadataActionReceived(action);
        if (action.isDirectChildren() == true) return browseDirectChildrenActionReceived(action);
        return false;
    }

    private boolean browseMetadataActionReceived(BrowseAction action) {
        String objID = action.getObjectID();
        ContentNode node = findContentNodeByID(objID);
        if (node == null) return false;
        DIDLLite didlLite = new DIDLLite();
        didlLite.setContentNode(node);
        String result = didlLite.toString();
        action.setArgumentValue(BrowseAction.RESULT, result);
        action.setArgumentValue(BrowseAction.NUMBER_RETURNED, 1);
        action.setArgumentValue(BrowseAction.TOTAL_MACHES, 1);
        action.setArgumentValue(BrowseAction.UPDATE_ID, getSystemUpdateID());
        if (log.isTraceEnabled()) action.print();
        return true;
    }

    private void sortContentNodeList(ContentNode conNode[], SortCap sortCap, boolean ascSeq) {
        int nConNode = conNode.length;
        for (int i = 0; i < (nConNode - 1); i++) {
            int selIdx = i;
            for (int j = (i + 1); j < nConNode; j++) {
                int cmpRet = sortCap.compare(conNode[selIdx], conNode[j]);
                if (ascSeq == true && cmpRet < 0) selIdx = j;
                if (ascSeq == false && 0 < cmpRet) selIdx = j;
            }
            ContentNode conTmp = conNode[i];
            conNode[i] = conNode[selIdx];
            conNode[selIdx] = conTmp;
        }
    }

    private SortCriterionList getSortCriteriaArray(String sortCriteria) {
        SortCriterionList sortCriList = new SortCriterionList();
        StringTokenizer st = new StringTokenizer(sortCriteria, ", ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            sortCriList.add(token);
        }
        return sortCriList;
    }

    private ContentNodeList sortContentNodeList(ContentNodeList contentNodeList, String sortCriteria) {
        if (sortCriteria == null || sortCriteria.length() <= 0) return contentNodeList;
        int nChildNodes = contentNodeList.size();
        ContentNode conNode[] = new ContentNode[nChildNodes];
        for (int n = 0; n < nChildNodes; n++) conNode[n] = contentNodeList.getContentNode(n);
        SortCriterionList sortCritList = getSortCriteriaArray(sortCriteria);
        int nSortCrit = sortCritList.size();
        for (int n = 0; n < nSortCrit; n++) {
            String sortStr = sortCritList.getSortCriterion(n);
            if (log.isTraceEnabled()) {
                log.trace("{0} = {1}", new Object[] { String.valueOf(n), sortStr });
            }
            boolean ascSeq = true;
            char firstSortChar = sortStr.charAt(0);
            if (firstSortChar == '-') ascSeq = false;
            if (firstSortChar == '+' || firstSortChar == '-') sortStr = sortStr.substring(1);
            SortCap sortCap = getSortCap(sortStr);
            if (sortCap == null) continue;
            if (log.isTraceEnabled()) {
                log.trace("ascSeq = {0} sortCap = {1}", new Object[] { String.valueOf(ascSeq), String.valueOf(sortCap.getType()) });
            }
            sortContentNodeList(conNode, sortCap, ascSeq);
        }
        ContentNodeList sortedContentNodeList = new ContentNodeList();
        for (int n = 0; n < nChildNodes; n++) sortedContentNodeList.add(conNode[n]);
        return sortedContentNodeList;
    }

    private boolean browseDirectChildrenActionReceived(BrowseAction action) {
        String objID = action.getObjectID();
        ContentNode node = findContentNodeByID(objID);
        if (node == null) return false;
        ContentNodeList contentNodeList = new ContentNodeList();
        int nChildNodes = node.getNContentNodes();
        for (int n = 0; n < nChildNodes; n++) {
            ContentNode cnode = node.getContentNode(n);
            contentNodeList.add(cnode);
        }
        String sortCriteria = action.getSortCriteria();
        ContentNodeList sortedContentNodeList = sortContentNodeList(contentNodeList, sortCriteria);
        int startingIndex = action.getStartingIndex();
        if (startingIndex <= 0) startingIndex = 0;
        int requestedCount = action.getRequestedCount();
        if (requestedCount == 0) requestedCount = nChildNodes;
        DIDLLite didlLite = new DIDLLite();
        int numberReturned = 0;
        for (int n = startingIndex; (n < nChildNodes && numberReturned < requestedCount); n++) {
            ContentNode cnode = sortedContentNodeList.getContentNode(n);
            didlLite.addContentNode(cnode);
            numberReturned++;
        }
        String result = didlLite.toString();
        action.setResult(result);
        action.setNumberReturned(numberReturned);
        action.setTotalMaches(nChildNodes);
        action.setUpdateID(getSystemUpdateID());
        return true;
    }

    private SearchCriteriaList getSearchCriteriaList(String searchStr) {
        SearchCriteriaList searchList = new SearchCriteriaList();
        if (searchStr == null) return searchList;
        if (searchStr.compareTo("*") == 0) return searchList;
        StringTokenizer searchCriTokenizer = new StringTokenizer(searchStr, SearchCriteria.WCHARS);
        while (searchCriTokenizer.hasMoreTokens() == true) {
            String prop = searchCriTokenizer.nextToken();
            if (searchCriTokenizer.hasMoreTokens() == false) break;
            String binOp = searchCriTokenizer.nextToken();
            if (searchCriTokenizer.hasMoreTokens() == false) break;
            String value = searchCriTokenizer.nextToken();
            value = StringUtil.trim(value, "\"");
            String logOp = "";
            if (searchCriTokenizer.hasMoreTokens() == true) logOp = searchCriTokenizer.nextToken();
            SearchCriteria searchCri = new SearchCriteria();
            searchCri.setProperty(prop);
            searchCri.setOperation(binOp);
            searchCri.setValue(value);
            searchCri.setLogic(logOp);
            searchList.add(searchCri);
        }
        return searchList;
    }

    private int getSearchContentList(ContentNode node, SearchCriteriaList searchCriList, SearchCapList searchCapList, ContentNodeList contentNodeList) {
        if (searchCriList.compare(node, searchCapList) == true) contentNodeList.add(node);
        int nChildNodes = node.getNContentNodes();
        for (int n = 0; n < nChildNodes; n++) {
            ContentNode cnode = node.getContentNode(n);
            getSearchContentList(cnode, searchCriList, searchCapList, contentNodeList);
        }
        return contentNodeList.size();
    }

    private boolean searchActionReceived(SearchAction action) {
        String contaierID = action.getContainerID();
        ContentNode node = findContentNodeByID(contaierID);
        if (node == null) return false;
        String searchCriteria = action.getSearchCriteria();
        SearchCriteriaList searchCriList = getSearchCriteriaList(searchCriteria);
        SearchCapList searchCapList = getSearchCapList();
        int n;
        ContentNodeList contentNodeList = new ContentNodeList();
        int nChildNodes = node.getNContentNodes();
        for (n = 0; n < nChildNodes; n++) {
            ContentNode cnode = node.getContentNode(n);
            getSearchContentList(cnode, searchCriList, searchCapList, contentNodeList);
        }
        nChildNodes = contentNodeList.size();
        String sortCriteria = action.getSortCriteria();
        ContentNodeList sortedContentNodeList = sortContentNodeList(contentNodeList, sortCriteria);
        int startingIndex = action.getStartingIndex();
        if (startingIndex <= 0) startingIndex = 0;
        int requestedCount = action.getRequestedCount();
        if (requestedCount == 0) requestedCount = nChildNodes;
        DIDLLite didlLite = new DIDLLite();
        int numberReturned = 0;
        for (n = startingIndex; (n < nChildNodes && numberReturned < requestedCount); n++) {
            ContentNode cnode = sortedContentNodeList.getContentNode(n);
            didlLite.addContentNode(cnode);
            numberReturned++;
        }
        String result = didlLite.toString();
        action.setResult(result);
        action.setNumberReturned(numberReturned);
        action.setTotalMaches(nChildNodes);
        action.setUpdateID(getSystemUpdateID());
        return true;
    }

    public boolean queryControlReceived(StateVariable stateVar) {
        return false;
    }

    public void contentExportRequestRecieved(HTTPRequest httpReq) {
        String uri = httpReq.getURI();
        if (uri.startsWith(CONTENT_EXPORT_URI) == false) {
            httpReq.returnBadRequest();
            return;
        }
        ParameterList paramList = httpReq.getParameterList();
        if (log.isTraceEnabled()) {
            for (int n = 0; n < paramList.size(); n++) {
                Parameter param = paramList.getParameter(n);
                log.trace("param={0} val={1}", new Object[] { param.getName(), param.getValue() });
            }
        }
        String id = paramList.getValue(CONTENT_ID);
        ContentNode node = findContentNodeByID(id);
        if (node == null) {
            httpReq.returnBadRequest();
            return;
        }
        if (!(node instanceof ItemNode)) {
            httpReq.returnBadRequest();
            return;
        }
        ItemNode itemNode = (ItemNode) node;
        long contentLen = itemNode.getContentLength();
        String contentType = itemNode.getMimeType();
        InputStream contentIn = itemNode.getContentInputStream();
        if (contentLen <= 0 || contentType.length() <= 0 || contentIn == null) {
            httpReq.returnBadRequest();
            return;
        }
        MediaServer mserver = getMediaServer();
        ConnectionManager conMan = mserver.getConnectionManager();
        int conID = conMan.getNextConnectionID();
        ConnectionInfo conInfo = new ConnectionInfo(conID);
        conInfo.setProtocolInfo(contentType);
        conInfo.setDirection(ConnectionInfo.OUTPUT);
        conInfo.setStatus(ConnectionInfo.OK);
        conMan.addConnectionInfo(conInfo);
        HTTPResponse httpRes = new HTTPResponse();
        httpRes.setContentType(contentType);
        httpRes.setStatusCode(HTTPStatus.OK);
        httpRes.setContentLength(contentLen);
        httpRes.setContentInputStream(contentIn);
        httpReq.post(httpRes);
        try {
            contentIn.close();
        } catch (Exception e) {
            log.warn(e);
        }
        conMan.removeConnectionInfo(conID);
    }

    public String getInterfaceAddress() {
        return getMediaServer().getInterfaceAddress();
    }

    public int getHTTPPort() {
        return getMediaServer().getHTTPPort();
    }

    public String getContentExportURL(String id) {
        return "http://" + getInterfaceAddress() + ":" + getHTTPPort() + CONTENT_EXPORT_URI + "?" + CONTENT_ID + "=" + id;
    }

    public String getContentImportURL(String id) {
        return "http://" + getInterfaceAddress() + ":" + getHTTPPort() + CONTENT_IMPORT_URI + "?" + CONTENT_ID + "=" + id;
    }

    private static final int DEFAULT_SYSTEMUPDATEID_INTERVAL = 2000;

    private static final int DEFAULT_CONTENTUPDATE_INTERVAL = 60000;

    private long systemUpdateIDInterval;

    private long contentUpdateInterval;

    public void setSystemUpdateInterval(long itime) {
        systemUpdateIDInterval = itime;
    }

    public long getSystemUpdateIDInterval() {
        return systemUpdateIDInterval;
    }

    public void setContentUpdateInterval(long itime) {
        contentUpdateInterval = itime;
    }

    public long getContentUpdateInterval() {
        return contentUpdateInterval;
    }

    public void run() {
        MediaServer mserver = getMediaServer();
        StateVariable varSystemUpdateID = mserver.getStateVariable(SYSTEM_UPDATE_ID);
        int lastSystemUpdateID = 0;
        long lastContentUpdateTime = System.currentTimeMillis();
        while (isRunnable() == true) {
            try {
                Thread.sleep(getSystemUpdateIDInterval());
            } catch (InterruptedException e) {
                log.warn(e);
            }
            int currSystemUpdateID = getSystemUpdateID();
            if (lastSystemUpdateID != currSystemUpdateID) {
                varSystemUpdateID.setValue(currSystemUpdateID);
                lastSystemUpdateID = currSystemUpdateID;
            }
            long currTime = System.currentTimeMillis();
            if (getContentUpdateInterval() < (currTime - lastContentUpdateTime)) {
                getDirectoryList().update();
                lastContentUpdateTime = currTime;
            }
        }
    }
}
