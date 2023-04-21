package org.jwebsocket.plugins.sharedobjects;

import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author aschulze
 */
public class SharedCanvas extends BaseSharedObject {

    private List mCmds = new FastList();

    public void addCmd(String aCmd) {
        mCmds.add(aCmd);
    }

    public void removeCmd(int aIndex) {
        mCmds.remove(aIndex);
    }
}
