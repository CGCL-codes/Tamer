package com.pawlowsky.zhu.mediafiler.gui;

import java.awt.datatransfer.DataFlavor;
import com.pawlowsky.zhu.mediafiler.database.MediaFile;

/**
 * 
 */
public class MediaFileListDataFlavor extends DataFlavor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private MediaFileListDataFlavor() {
        super(mediaFileClass, null);
    }

    public static final MediaFileListDataFlavor getInstance() {
        return instance;
    }

    private static MediaFileListDataFlavor createInstance() {
        try {
            return new MediaFileListDataFlavor();
        } catch (Exception ex) {
            MessageDialog.show(ex);
            return null;
        }
    }

    private static Class mediaFileClass = (new MediaFile[0]).getClass();

    private static final MediaFileListDataFlavor instance = createInstance();
}
