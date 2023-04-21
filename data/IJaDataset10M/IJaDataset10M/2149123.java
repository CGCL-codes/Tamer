package net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.containerManager.containers.media;

import java.util.ArrayList;
import net.sourceforge.x360mediaserve.api.database.ItemNotFoundException;
import net.sourceforge.x360mediaserve.api.database.QueryResult;
import net.sourceforge.x360mediaserve.api.database.items.Item;
import net.sourceforge.x360mediaserve.api.database.items.media.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.ContentDir;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.containerManager.containers.BrowseContainer;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.nodes.creators.SearchClass;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.nodes.creators.UPNPClasses;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.upnpObjects.container.SimpleSearchClass;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.contentDirectory.upnpObjects.container.UPNPContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Container that fetches images from data manager
 * @author tom
 *
 */
public class ImagesContainer extends BrowseContainer implements UPNPContainer {

    private static Logger logger = LoggerFactory.getLogger(ImagesContainer.class);

    ContentDir contentDir;

    private static SearchClass[] searchClasses = new SearchClass[] { new SimpleSearchClass(UPNPClasses.IMAGE_ITEM_TYPE, true) };

    /**
	 * 
	 */
    public ImagesContainer(ContentDir contentDir) {
        super();
        this.contentDir = contentDir;
    }

    public QueryResult doSearch(String containerID, String searchCriteria, String sortCriteria, long startIndex, long requestedCount) {
        logger.error("Container does not support search");
        return null;
    }

    public QueryResult getChild(String id) throws ItemNotFoundException {
        QueryResult<Item> result = contentDir.getMediaDB().getItemForID(id);
        result.addPrefix(this.getID());
        return result;
    }

    @Override
    public String getExternalIdForId(String id) {
        return id;
    }

    public long getChildCount() {
        return contentDir.getMediaDB().browseImageFolders("", 0, 1, true).getTotalNumber();
    }

    @Override
    public QueryResult getChildren(String containerID, String sortOrder, long startIndex, long requestedCount) {
        logger.info("Browsing image plugins");
        QueryResult result;
        if (containerID.length() == 0) {
            result = contentDir.getMediaDB().browseImageFolders(containerID, 0, 1000, true);
        } else {
            result = contentDir.getMediaDB().getContentForContainer(containerID, startIndex, requestedCount, null);
        }
        result.addPrefix(this.getID());
        return result;
    }

    public SearchClass[] getSearchClasses() {
        return searchClasses;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
