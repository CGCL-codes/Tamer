package org.primordion.cellontro.app;

import org.primordion.xholon.app.Application;
import org.primordion.xholon.io.IAbout;
import org.primordion.xholon.service.AbstractXholonService;

/**
 * Cell Model with Autopoiesis.
 * <p>This is a variation of the Cell Model in which the cell bilayer contains lipids.
 * These lipids are created within the Cytosol. Because they are hydrophobic, the water
 * in the cytosol tends to push them towards the cell bilayer, which gradually incorporates them.
 * The activity of the cell bilayer is causally dependent on the quantity of lipids it contains.</p>
 * <p>For more information, see:
 * Webb, K., & White, T. (2004).
 * Combining Analysis and Synthesis in a Model of a Biological Cell.
 * SAC '04, March 14-17, 2004, Nicosia, Cyprus.</p>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.1 (Created on Oct 20, 2005)
 */
public class AppCellAutop extends Application {

    /**
	 * Constructor.
	 */
    public AppCellAutop() {
        super();
        chartViewer = null;
    }

    protected void step() {
        root.preAct();
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        root.act();
        root.postAct();
    }

    public void wrapup() {
        root.preAct();
        if (getUseDataPlotter()) {
            chartViewer.capture(timeStep);
        }
        super.wrapup();
    }

    public void about() {
        String title = "About Cellontro";
        String text;
        if (modelName == null) {
            text = "Cellontro using " + aboutText;
        } else {
            text = modelName + "\n" + "Cellontro using " + aboutText;
        }
        IAbout about = (IAbout) getService(AbstractXholonService.XHSRV_ABOUT);
        if (about != null) {
            about.about(title, text, 350, 200);
        } else {
            System.out.println(title);
            System.out.println(text);
        }
    }

    /**
	 * main
	 * @param args One optional command line argument.
	 */
    public static void main(String[] args) {
        appMain(args, "org.primordion.cellontro.app.AppCellAutop", "./config/cellontro/CellAutop/Autop_xhn.xml");
    }
}
