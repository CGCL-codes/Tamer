package connex.plugins.filesharing;

import net.jxta.share.ContentAdvertisement;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ContentSearchListener {

    public void receiveResult(ContentAdvertisement cAdv);
}
