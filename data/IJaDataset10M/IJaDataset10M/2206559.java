package com.nullfish.lib.vfs.manipulation.abst;

import java.io.InputStream;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.GetInputStreamManipulation;

/**
 * @author shunji
 *
 */
public abstract class AbstractGetInputStreamManipulation extends AbstractManipulation implements GetInputStreamManipulation {

    InputStream rtn;

    public AbstractGetInputStreamManipulation(VFile file) {
        super(file);
    }

    /**
	 * 入力ストリームを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.GetInputStreamManipulation#getInputStream()
	 */
    public InputStream getInputStream() {
        return rtn;
    }

    public void doExecute() throws VFSException {
        rtn = doGetInputStream(file);
    }

    public abstract InputStream doGetInputStream(VFile file) throws VFSException;

    /**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
    public String getProgressMessage() {
        return "";
    }
}
