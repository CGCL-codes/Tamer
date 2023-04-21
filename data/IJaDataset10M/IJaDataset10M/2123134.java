package org.viewer.gui;

import org.viewer.centernode.CenterNode;
import java.util.TimerTask;

/**

 * Created by IntelliJ IDEA.

 * User: Sungchan

 * Date: Dec 4, 2005

 * Time: 11:27:08 AM

 * To change this template use File | Settings | File Templates.

 */
public class TTCN extends TimerTask {

    CenterNode cn;

    public TTCN(CenterNode cn) {
        this.cn = cn;
    }

    public void run() {
        cn.process();
    }
}
