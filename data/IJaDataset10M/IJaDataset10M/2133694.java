package org.primordion.xholon.tutorials.rcs;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.base.IXholon;

/**
 * Regulated Catalyzing System, version 2.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.5 (Created on Oct 22, 2006)
 */
public class AppRcs2 extends Application {

    protected void step() {
        root.preAct();
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        root.act();
    }

    public void wrapup() {
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        super.wrapup();
    }

    protected boolean shouldBePlotted(IXholon modelNode) {
        if ((modelNode.getXhcId() == CeRcs2.SolutionCE) || (modelNode.getXhcId() == CeRcs2.GlycogenCE)) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        appMain(args, "org.primordion.xholon.tutorials.rcs.AppRcs2", "./config/Rcs/Rcs2_xhn.xml");
    }
}
