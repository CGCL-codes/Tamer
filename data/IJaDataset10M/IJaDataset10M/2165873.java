package org.ddth.txbb.board.mvc;

import javax.servlet.ServletException;
import org.ddth.txbb.base.TXBBApp;
import org.ddth.txbb.base.TXBBConstants;
import org.ddth.txbb.base.mvc.TXBBViewer;
import com.atlassian.util.profiling.UtilTimerStack;

public class CommonBoardViewer extends TXBBViewer {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = 1063775960459036762L;

    public CommonBoardViewer(TXBBApp app, String domain, String action) {
        super(app, domain, action);
    }

    public void render() throws ServletException {
        if (TXBBConstants.PROFILING) UtilTimerStack.push("render");
        super.render();
        if (TXBBConstants.PROFILING) UtilTimerStack.pop("render");
    }
}
