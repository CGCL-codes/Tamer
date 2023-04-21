package net.sf.magicmap.client.interfaces;

import java.util.Collection;

/**
 * @author Johannes Zapotoczky (johannes@zapotoczky.com)
 *
 */
public interface FetchRFIDTagsCallback {

    /**
	 * @param rfidTags
	 */
    public void rfidTagsFetched(Collection rfidTags);

    /**
	 * An error has occurred during fetch 
	 * @param e
	 */
    public void rfidTagFetchError(Exception e);
}
