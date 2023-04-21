package net.sf.pim.action;

import net.sf.pim.UiUtil;
import net.sf.util.StringUtil;
import org.eclipse.swt.SWT;

/**
 * @author lzhang
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CurAction extends UiAction {

    public CurAction() {
        super();
        name = "今天";
        gif = "today.gif";
        quick = SWT.CTRL + SWT.ARROW_DOWN;
    }

    public void run() {
        super.run();
        if (UiUtil.getActiveTableEditor().getName().equals("日志")) UiUtil.showWork(parent, StringUtil.getCurrentDay()); else if (UiUtil.getActiveTableEditor().getName().indexOf("周") != -1) UiUtil.showChunk(parent, StringUtil.getCurrentWeek());
    }
}
