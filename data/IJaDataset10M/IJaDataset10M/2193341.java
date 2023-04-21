package com.apachetune.core.ui.editors;

import com.apachetune.core.utils.StringValue;
import org.apache.commons.lang.NotImplementedException;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public abstract class SaveFilesHelperCallBackAdapter implements SaveFilesHelperCallBack {

    public void prepareSaveAllFiles(StringValue title, StringValue message) {
        throw new NotImplementedException();
    }

    public void prepareSaveFile(Object fileId, StringValue title, StringValue message) {
        throw new NotImplementedException();
    }

    public void saveFile(Object fileId) {
        throw new NotImplementedException();
    }

    public boolean isFileDirty(Object fileId) {
        throw new NotImplementedException();
    }
}
