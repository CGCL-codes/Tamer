package ftm;

import java.lang.InterruptedException;
import gui.tree.TreeObject;
import gui.tree.DynamicTree;
import opt.*;

public class FromToBridge extends Thread {

    protected static final String TABLE_CLASS = "TABLE";

    protected static final String FIELD_CLASS = "FIELD";

    protected Options opts;

    public Boolean isMDGComplete;

    private DynamicTree globalTree;

    private TreeObject globalFTMNode;

    private FromToObject srcFTM;

    private FromToObject tgtFTM;

    private FromToObject newFTM;

    private FromToManager globalFTMgr;

    private String sourceDisplay;

    private String targetDisplay;

    public FromToBridge(Options GlobalOptions, FromToObject sourceFTM, FromToObject targetFTM, FromToManager ftMgr) {
        initialize(GlobalOptions, sourceFTM, targetFTM, ftMgr);
    }

    private void initialize(Options GlobalOptions, FromToObject sourceFTM, FromToObject targetFTM, FromToManager ftMgr) {
        opts = GlobalOptions;
        srcFTM = sourceFTM;
        tgtFTM = targetFTM;
        newFTM = new FromToObject();
        globalFTMgr = ftMgr;
        isMDGComplete = true;
    }

    public FromToObject getFTMBridge() {
        return (newFTM);
    }

    public void run() {
        try {
            outputNoThread();
        } catch (InterruptedException exception) {
            opts.printtextln("Output Interrupted.");
            isMDGComplete = true;
        }
    }

    protected void outputNoThread() throws InterruptedException {
        if (srcFTM == null || tgtFTM == null || globalFTMgr == null) {
            opts.printtextln("FATAL ERROR: Source or Target Cross Reference is NULL, fatal error, contact RapidACE");
            return;
        }
        if (srcFTM.getChildren().size() == 0 || tgtFTM.getChildren().size() == 0) {
            opts.printtextln("Source or Target contains NO ELEMENTS.  Cannot Bridge.");
            return;
        }
        try {
            opts.printtextln("Beginning Bridge of Data Flows from " + srcFTM.toString() + " to " + tgtFTM.toString());
            isMDGComplete = false;
            FromToObject newList = globalFTMgr.mergeFromToObjects(srcFTM, tgtFTM);
            FromToManager.setupViewNames(newList);
            globalFTMgr.addNewList(newList);
            globalFTMgr.addFromToObjectToGUI(globalTree, this.sourceDisplay, this.targetDisplay, this.globalFTMNode, newList);
            if (isMDGComplete == true) throw (new InterruptedException());
            opts.printtextln("Bridge Complete");
            opts.printDivider();
        } catch (InterruptedException exception) {
            opts.printtextln("Output Interrupted.");
            isMDGComplete = true;
        }
    }

    public void setGUI(DynamicTree guiTree, TreeObject FromToNode, String srcDisplay, String tgtDisplay) {
        globalTree = guiTree;
        globalFTMNode = FromToNode;
        sourceDisplay = srcDisplay;
        targetDisplay = tgtDisplay;
    }
}
