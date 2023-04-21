package org.wits.parsers.block;

import org.wits.debugger.WITSDebugger;
import org.wits.parsers.WITSParser;
import org.wits.patterns.StringHandler;

/**
 *
 * @author FJ
 */
public class PanelParser implements WITSParser {

    private String uncleanSGML = null;

    private WITSDebugger debugger = null;

    /**
     *
     * @param debugger
     */
    public void setDebugger(WITSDebugger debugger) {
        this.debugger = debugger;
    }

    /**
     *
     * @param uncleanSGML
     */
    public PanelParser(String uncleanSGML) {
        this.uncleanSGML = uncleanSGML;
    }

    /**
     *
     * @return
     */
    public String getProcessedText() {
        debugger.addLineBreak();
        debugger.showDebugMessage("PanelIC", 0, "PanelIC Invoked.");
        int offset = 0;
        StringBuilder _handle = new StringBuilder();
        while (true) {
            int l_loc = uncleanSGML.indexOf("{panel:", offset);
            int r_loc = uncleanSGML.indexOf("{panel}", l_loc + 1);
            if (l_loc == -1 || r_loc == -1) {
                _handle.append(uncleanSGML.substring(offset, uncleanSGML.length()));
                break;
            }
            int ll_loc = uncleanSGML.indexOf("}", l_loc);
            String bqCandidate = uncleanSGML.substring(ll_loc + 1, r_loc);
            _handle.append(uncleanSGML.substring(offset, l_loc));
            _handle.append(bqCandidate);
            offset = r_loc;
        }
        uncleanSGML = _handle.toString();
        StringHandler handler = new StringHandler();
        handler.setDebugger(debugger);
        uncleanSGML = handler.replace(uncleanSGML, "{panel}", "");
        return uncleanSGML;
    }
}
