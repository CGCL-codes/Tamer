package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddOutGateTool;
import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddSubModelTool;
import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddSystemResponseTool;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.event.ToolSelectionEvent;
import com.nokia.ats4.appmodel.perspective.modeldesign.ModelDesignPerspective;
import com.nokia.ats4.appmodel.perspective.Tool;
import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddEntryPointTool;
import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddExitPointTool;
import com.nokia.ats4.appmodel.perspective.modeldesign.tool.AddInGateTool;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * ToolSelectionCommand
 *
 * @author Timo Sillanp&auml;&auml;
 * @version $Revision: 2 $
 */
public class ToolSelectionCommand implements KendoEventListener {

    private static Logger log = Logger.getLogger(ToolSelectionCommand.class);

    private final Map<String, Tool> tools = new HashMap<String, Tool>();

    private ModelDesignPerspective perspective;

    /**
     * Creates a new instance of ToolSelectionCommand
     */
    public ToolSelectionCommand(ModelDesignPerspective perspective) {
        this.perspective = perspective;
        tools.put(Tool.SYSTEM_EVENT, new AddSystemResponseTool());
        tools.put(Tool.SUB_MODEL, new AddSubModelTool());
        tools.put(Tool.ENTRY_POINT, new AddEntryPointTool());
        tools.put(Tool.EXIT_POINT, new AddExitPointTool());
        tools.put(Tool.IN_GATE, new AddInGateTool());
        tools.put(Tool.OUT_GATE, new AddOutGateTool());
    }

    @Override
    public void processEvent(KendoEvent event) {
        if (event instanceof ToolSelectionEvent) {
            ToolSelectionEvent e = (ToolSelectionEvent) event;
            Tool tool = tools.get(e.getToolName());
            if (tool != null) {
                log.debug("Selected tool; " + e.getToolName());
                perspective.setSelectedTool(tool);
            } else {
                log.warn("Unknown tool " + e.getToolName() + " was selected.");
            }
        }
    }
}
