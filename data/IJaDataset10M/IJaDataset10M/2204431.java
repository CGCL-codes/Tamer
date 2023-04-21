package net.sourceforge.mxupdate.update.userinterface;

/**
 *
 * @author tmoxter
 * @version $Id: Setting_mxJPO.java 14 2008-09-12 15:19:41Z tmoxter $
 */
class Setting_mxJPO {

    String name = null;

    String value = null;

    void write() {
    }

    @Override
    public String toString() {
        return "[name=" + name + ", value=" + value + "]";
    }
}
