package org.jcvi.vics.web.gwt.download.client.formatter;

import org.jcvi.vics.web.gwt.common.client.model.download.DownloadableDataNode;

/**
 * Created by IntelliJ IDEA.
 * User: Lfoster
 * Date: Sep 7, 2006
 * Time: 5:20:49 PM
 * <p/>
 * Implement this to make a display of a data file.
 */
public interface MetaDataDisplay {

    void showFileMetaData(DownloadableDataNode dataFile, int x, int y);
}
