package com.rapidminer.gui.operatortree.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import com.rapidminer.gui.actions.Actions;
import com.rapidminer.gui.templates.BuildingBlock;
import com.rapidminer.gui.templates.SaveAsBuildingBlockDialog;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.operator.Operator;
import com.rapidminer.tools.FileSystemService;

/**
 * Start the corresponding action.
 * 
 * @author Ingo Mierswa
 */
public class SaveBuildingBlockAction extends ResourceAction {

    private static final long serialVersionUID = 2238740826770976483L;

    private Actions actions;

    public SaveBuildingBlockAction(Actions actions) {
        super(true, "save_building_block");
        setCondition(OPERATOR_SELECTED, MANDATORY);
        setCondition(ROOT_SELECTED, DISALLOWED);
        this.actions = actions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Operator selectedOperator = this.actions.getSelectedOperator();
        if (selectedOperator != null) {
            SaveAsBuildingBlockDialog dialog = new SaveAsBuildingBlockDialog(selectedOperator);
            dialog.setVisible(true);
            if (dialog.isOk()) {
                BuildingBlock buildingBlock = dialog.getBuildingBlock(selectedOperator);
                String name = buildingBlock.getName();
                try {
                    File buildingBlockFile = FileSystemService.getUserConfigFile(name + ".buildingblock");
                    buildingBlock.save(buildingBlockFile);
                } catch (IOException ioe) {
                    SwingTools.showSimpleErrorMessage("cannot_write_building_block_file", ioe);
                }
            }
        }
    }
}
